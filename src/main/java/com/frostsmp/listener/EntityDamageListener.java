package com.frostsmp.listener;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.ability.WeaponClass;
import com.frostsmp.data.PlayerData;
import com.frostsmp.util.MessageUtil;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityDamageListener implements Listener {

    private final FrostStrengthSMP plugin;
    private final Map<UUID, Long> shieldDisabled = new HashMap<>();

    public EntityDamageListener(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        LivingEntity victim = (LivingEntity) event.getEntity();
        Entity damager = event.getDamager();

        Player attacker = resolveAttacker(damager);
        if (attacker == null) return;

        PlayerData data = plugin.getPlayerData(attacker);

        if (plugin.getConfigManager().isStrengthLockEnabled() && data.isLocked()) {
            event.setCancelled(true);
            return;
        }

        applyStrengthDamage(attacker, event);

        if (!data.isClassAssigned() || data.getWeaponClass() == WeaponClass.NONE) return;

        WeaponClass cls = data.getWeaponClass();
        int str = data.getStrength();
        int passiveUnlock = plugin.getConfigManager().getPassiveUnlock();
        if (str < passiveUnlock) return;

        if (cls == WeaponClass.AXE && ItemUseListener.CleaveBuff.hasBuff(attacker.getUniqueId())) {
            ItemUseListener.CleaveBuff.consume(attacker.getUniqueId());
            event.setDamage(event.getDamage() + 12.0);
            if (victim instanceof Player) {
                Player vPlayer = (Player) victim;
                shieldDisabled.put(vPlayer.getUniqueId(), System.currentTimeMillis() + 5000L);
                attacker.sendMessage(MessageUtil.component("&aCleave empowered strike!"));
                vPlayer.sendMessage(MessageUtil.component("&cYour shield is disabled!"));
            }
        }

        switch (cls) {
            case SWORD:
                if (attacker.isSprinting() || attacker.getFallDistance() > 0.0) {
                    event.setDamage(event.getDamage() + 1.5);
                }
                break;
            case AXE:
                if (victim instanceof Player) {
                    Player vPlayer = (Player) victim;
                    if (isBlocking(vPlayer) && Math.random() < 0.25) {
                        shieldDisabled.put(vPlayer.getUniqueId(), System.currentTimeMillis() + 5000L);
                        vPlayer.sendMessage(MessageUtil.component("&cYour shield has been disabled by an Axe user!"));
                        attacker.sendMessage(MessageUtil.component("&aYou disabled " + vPlayer.getName() + "'s shield!"));
                    }
                }
                break;
            case SPEAR:
                break;
            default:
                break;
        }

        if (victim instanceof Player) {
            Player vPlayer = (Player) victim;
            PlayerData vData = plugin.getPlayerData(vPlayer);
            if (plugin.getConfigManager().isStrengthLockEnabled() && vData.isLocked()) {
                event.setCancelled(true);
                return;
            }

            WeaponClass vCls = vData.getWeaponClass();
            if (vData.isClassAssigned() && vCls != WeaponClass.NONE
                    && vData.getStrength() >= passiveUnlock) {
                applyVictimPassive(vPlayer, vCls, event);
            }
        }
    }

    @EventHandler
    public void onDamageTaken(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        PlayerData data = plugin.getPlayerData(player);
        WeaponClass cls = data.getWeaponClass();

        if (plugin.getConfigManager().isStrengthLockEnabled() && data.isLocked()) {
            event.setCancelled(true);
            return;
        }

        Long disabledUntil = shieldDisabled.get(player.getUniqueId());
        if (disabledUntil != null && System.currentTimeMillis() >= disabledUntil) {
            shieldDisabled.remove(player.getUniqueId());
        }

        if (data.isClassAssigned() && cls == WeaponClass.SHIELD
                && data.getStrength() >= plugin.getConfigManager().getPassiveUnlock()
                && isBlocking(player)) {
            double reduction = 0.25 + (data.getStrength() * 0.05);
            event.setDamage(event.getDamage() * (1.0 - reduction));
        }

        if (data.isClassAssigned() && cls == WeaponClass.ARMOR
                && data.getStrength() >= plugin.getConfigManager().getPassiveUnlock()) {
            double reduction = 0.10 + (data.getStrength() * 0.04);
            if (reduction > 0.5) reduction = 0.5;
            event.setDamage(event.getDamage() * (1.0 - reduction));
        }
    }

    private void applyStrengthDamage(Player attacker, EntityDamageByEntityEvent event) {
        PlayerData data = plugin.getPlayerData(attacker);
        double bonus = data.getStrength() * plugin.getConfigManager().getDamagePerLevel();
        if (bonus > 0) event.setDamage(event.getDamage() + bonus);
    }

    private void applyVictimPassive(Player victim, WeaponClass cls, EntityDamageByEntityEvent event) {
        int str = plugin.getPlayerData(victim).getStrength();
        switch (cls) {
            case SHIELD:
                if (isBlocking(victim)) {
                    double reduction = 0.25 + (str * 0.05);
                    event.setDamage(event.getDamage() * (1.0 - reduction));
                }
                break;
            case ARMOR:
                double reduction = 0.10 + (str * 0.04);
                if (reduction > 0.5) reduction = 0.5;
                event.setDamage(event.getDamage() * (1.0 - reduction));
                break;
            default:
                break;
        }
    }

    private Player resolveAttacker(Entity damager) {
        if (damager instanceof Player) return (Player) damager;
        if (damager instanceof org.bukkit.entity.Projectile) {
            Entity shooter = ((org.bukkit.entity.Projectile) damager).getShooter();
            if (shooter instanceof Player) return (Player) shooter;
        }
        return null;
    }

    private boolean isBlocking(Player player) {
        ItemStack off = player.getInventory().getItemInOffHand();
        ItemStack main = player.getInventory().getItemInMainHand();
        return (off != null && off.getType().name().equals("SHIELD"))
                || (main != null && main.getType().name().equals("SHIELD"));
    }
}
