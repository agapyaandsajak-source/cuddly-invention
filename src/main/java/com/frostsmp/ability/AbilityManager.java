package com.frostsmp.ability;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.data.PlayerData;
import com.frostsmp.listener.ItemUseListener;
import com.frostsmp.util.MessageUtil;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

public class AbilityManager {

    private final FrostStrengthSMP plugin;
    private final Random random = new Random();
    private final Map<String, Long> ultimateCooldowns = new HashMap<>();

    public AbilityManager(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    public WeaponClass getRandomClass() {
        WeaponClass[] classes = WeaponClass.values();
        int idx = random.nextInt(Math.max(1, classes.length - 1));
        WeaponClass cls = classes[idx];
        if (cls == WeaponClass.NONE) cls = WeaponClass.SWORD;
        return cls;
    }

    public void assignRandomClass(Player player) {
        PlayerData data = plugin.getPlayerData(player);
        if (data.isClassAssigned()) return;
        WeaponClass cls = getRandomClass();
        data.setWeaponClass(cls);
        data.setClassAssigned(true);
        data.save();
        player.sendMessage(plugin.getConfigManager().getPrefix()
                + " Your Weapon Class is: &e" + cls.getDisplayName()
                + " &7(Passive at +2, Ultimate at +4)");
    }

    public WeaponClass getPlayerClass(Player player) {
        return plugin.getPlayerData(player).getWeaponClass();
    }

    public boolean isUltimateOnCooldown(Player player) {
        Long end = ultimateCooldowns.get(player.getName());
        if (end == null) return false;
        long now = System.currentTimeMillis();
        if (now >= end) {
            ultimateCooldowns.remove(player.getName());
            return false;
        }
        return true;
    }

    public void setUltimateCooldown(Player player, int ticks) {
        ultimateCooldowns.put(player.getName(), System.currentTimeMillis() + (ticks * 50L));
    }

    public long getRemainingCooldownMillis(Player player) {
        Long end = ultimateCooldowns.get(player.getName());
        if (end == null) return 0;
        return Math.max(0, end - System.currentTimeMillis());
    }

    public void clearCooldown(Player player) {
        ultimateCooldowns.remove(player.getName());
    }

    public boolean hasPassive(Player player) {
        PlayerData data = plugin.getPlayerData(player);
        return data.isClassAssigned()
                && data.getWeaponClass() != WeaponClass.NONE
                && data.getStrength() >= plugin.getConfigManager().getPassiveUnlock();
    }

    public boolean hasUltimate(Player player) {
        PlayerData data = plugin.getPlayerData(player);
        return data.isClassAssigned()
                && data.getWeaponClass() != WeaponClass.NONE
                && data.getStrength() >= plugin.getConfigManager().getUltimateUnlock();
    }

    public void activateUltimate(Player player) {
        PlayerData data = plugin.getPlayerData(player);
        if (!data.isClassAssigned() || data.getWeaponClass() == WeaponClass.NONE) {
            MessageUtil.send(player, "&cYou have no Weapon Class assigned.");
            return;
        }
        if (!hasUltimate(player)) {
            MessageUtil.send(player, "&cYour Ultimate unlocks at Strength &4"
                    + plugin.getConfigManager().getUltimateUnlock() + "&c!");
            return;
        }
        if (isUltimateOnCooldown(player)) {
            long rem = getRemainingCooldownMillis(player) / 1000L;
            MessageUtil.send(player, "&cUltimate on cooldown (&c" + rem + "s&c).");
            return;
        }

        WeaponClass cls = data.getWeaponClass();
        int cooldown = plugin.getConfigManager().getUltimateCooldownTicks();
        setUltimateCooldown(player, cooldown);

        boolean announced = plugin.getConfigManager().isAnnounceUltimate();
        Location loc = player.getLocation();
        World world = loc.getWorld();
        net.kyori.adventure.text.Component broadcast = null;

        switch (cls) {
            case SWORD:
                int hit = 0;
                for (Entity ent : world.getNearbyEntities(loc, 3.5, 3.5, 3.5)) {
                    if (ent instanceof LivingEntity && ent != player) {
                        ((LivingEntity) ent).damage(6.0 + data.getStrength(), player);
                        hit++;
                    }
                }
                MessageUtil.send(player, "&6&lWHIRLWIND! &fStruck &c" + hit + "&f enemies.");
                broadcast = MessageUtil.component("&6" + player.getName() + "&f used Whirlwind Ultimate!");
                break;
            case AXE:
                MessageUtil.send(player, "&6&lCLEAVE! &fYour next strike within 10s is devastating.");
                ItemUseListener.CleaveBuff.put(player.getUniqueId(), System.currentTimeMillis() + 10000L);
                world.playSound(loc, Sound.BLOCK_ANVIL_LAND, 1.0f, 1.2f);
                broadcast = MessageUtil.component("&6" + player.getName() + "&f readies Cleave Ultimate!");
                break;
            case BOW:
                for (int i = 0; i < 5; i++) {
                    Projectile arrow = world.spawn(loc.add(0, 1, 0), Arrow.class);
                    Vector dir = loc.getDirection().clone();
                    dir.rotateAroundX((Math.random() - 0.5) * 0.5);
                    dir.rotateAroundY((Math.random() - 0.5) * 0.5);
                    arrow.setVelocity(dir.multiply(1.5));
                    arrow.setShooter(player);
                    loc.subtract(0, 1, 0);
                }
                world.playSound(loc, Sound.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f);
                MessageUtil.send(player, "&6&lBARRAGE! &fFired 5 arrows.");
                broadcast = MessageUtil.component("&6" + player.getName() + "&f unleashed Barrage!");
                break;
            case CROSSBOW:
                Fireball fb = world.spawn(loc.add(0, 1, 0), Fireball.class);
                fb.setDirection(loc.getDirection().multiply(1.2));
                fb.setShooter(player);
                world.playSound(loc, Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
                MessageUtil.send(player, "&6&lBLAST! &fFired an explosive bolt.");
                broadcast = MessageUtil.component("&6" + player.getName() + "&f fired Blast!");
                break;
            case TRIDENT:
                Location target = loc.add(loc.getDirection().multiply(20));
                world.strikeLightning(target);
                world.playSound(target, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
                MessageUtil.send(player, "&6&lSTORM! &fLightning strikes your target.");
                broadcast = MessageUtil.component("&6" + player.getName() + "&f called a Storm!");
                break;
            case MACE:
                int mhit = 0;
                for (Entity ent : world.getNearbyEntities(loc, 4, 2, 4)) {
                    if (ent instanceof LivingEntity && ent != player) {
                        LivingEntity le = (LivingEntity) ent;
                        le.damage(8.0 + data.getStrength(), player);
                        le.setVelocity(new Vector(0, 1.2, 0));
                        mhit++;
                    }
                }
                world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.8f);
                MessageUtil.send(player, "&6&lSEISMIC SMASH! &fSlammed &c" + mhit + "&f targets.");
                broadcast = MessageUtil.component("&6" + player.getName() + "&f used Seismic Smash!");
                break;
            case SPEAR:
                Vector dash = loc.getDirection().multiply(2.5).setY(0.5);
                player.setVelocity(dash);
                world.playSound(loc, Sound.ENTITY_DOLPHIN_JUMP, 1.0f, 1.2f);
                MessageUtil.send(player, "&6&lLUNGE! &fYou dashed forward.");
                broadcast = MessageUtil.component("&6" + player.getName() + "&f lunged with their Spear!");
                break;
            case SHIELD:
                int shit = 0;
                for (Entity ent : world.getNearbyEntities(loc, 3, 1.5, 3)) {
                    if (ent instanceof LivingEntity && ent != player) {
                        LivingEntity le = (LivingEntity) ent;
                        le.damage(4.0 + data.getStrength(), player);
                        Vector push = ent.getLocation().toVector().subtract(loc.toVector()).normalize().multiply(1.5);
                        push.setY(0.4);
                        le.setVelocity(push);
                        shit++;
                    }
                }
                world.playSound(loc, Sound.ENTITY_IRON_GOLEM_ATTACK, 1.0f, 1.0f);
                MessageUtil.send(player, "&6&lSHIELD BASH! &fBashed &c" + shit + "&f enemies.");
                broadcast = MessageUtil.component("&6" + player.getName() + "&f used Shield Bash!");
                break;
            case ARMOR:
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 15, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20 * 15, 1));
                world.playSound(loc, Sound.ENTITY_IRON_GOLEM_HURT, 1.0f, 1.0f);
                MessageUtil.send(player, "&6&lIRON SKIN! &fAbsorption and Resistance for 15s.");
                broadcast = MessageUtil.component("&6" + player.getName() + "&f activated Iron Skin!");
                break;
            default:
                break;
        }
        if (announced && broadcast != null) {
            Bukkit.broadcast(broadcast);
        }
        plugin.getLogger().log(Level.INFO, "[AbilityManager] " + player.getName() + " used " + cls + " ultimate");
    }

    public void log(Level level, String msg) {
        plugin.getLogger().log(level, "[AbilityManager] " + msg);
    }
}
