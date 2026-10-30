package com.frostsmp.listener;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.ability.WeaponClass;
import com.frostsmp.data.PlayerData;
import com.frostsmp.item.CustomItems;
import com.frostsmp.manager.SoulBindManager;
import com.frostsmp.util.MessageUtil;
import com.frostsmp.util.SpawnEffect;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemUseListener implements Listener {

    private final FrostStrengthSMP plugin;
    private static final Map<UUID, Integer> crownStrength = new HashMap<>();

    public ItemUseListener(FrostStrengthSMP plugin) {
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskTimer(plugin, this::scanCrown, 0L, 2L);
    }

    public static void removeCrown(Player player) {
        takeOffCrown(player);
    }

    private void scanCrown() {
        if (!plugin.getConfigManager().isCrownEnabled()) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack helm = player.getInventory().getHelmet();
            boolean wearing = CustomItems.isCrown(helm);
            UUID uuid = player.getUniqueId();
            if (wearing && !crownStrength.containsKey(uuid)) putOnCrown(player);
            else if (!wearing && crownStrength.containsKey(uuid)) takeOffCrown(player);
        }
    }

    private void putOnCrown(Player player) {
        PlayerData data = plugin.getPlayerData(player);
        crownStrength.put(player.getUniqueId(), data.getStrength());
        data.setStrength(plugin.getConfigManager().getMaxStrength());
        data.save();
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * 60 * 10, 4, false, false));
        player.sendMessage(MessageUtil.component(
                "&6&lCROWN ON: &fStrength set to max (&c" + data.getStrength() + "&f) and Health Boost V granted."));
    }

    private static void takeOffCrown(Player player) {
        Integer real = crownStrength.remove(player.getUniqueId());
        if (real == null) return;
        FrostStrengthSMP plugin = FrostStrengthSMP.getInstance();
        PlayerData data = plugin.getPlayerData(player);
        data.setStrength(real);
        data.save();
        player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
        player.sendMessage(MessageUtil.component(
                "&7Crown removed. Strength restored to &c" + data.getStrength() + "&7."));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (item == null) return;

        if (CustomItems.isStrengthBottle(item)) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                drinkBottle(player, item);
                return;
            }
        }

        if (CustomItems.isReviveToken(item)) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                useReviveToken(player, item);
                return;
            }
        }

        if (CustomItems.isStrengthShard(item)) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                useStrengthShard(player, item);
                return;
            }
        }

        if (CustomItems.isRerollGrimoire(item)) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                useRerollGrimoire(player, item);
                return;
            }
        }

        if (CustomItems.isDeathCertificate(item)) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                player.sendMessage(MessageUtil.component(
                        "&8&lDeath Certificate: &7Right-click to see kill details."));
                return;
            }
        }
    }

    private void drinkBottle(Player player, ItemStack item) {
        PlayerData data = plugin.getPlayerData(player);
        int max = plugin.getConfigManager().getMaxStrength();
        if (data.getStrength() >= max) {
            MessageUtil.send(player, "&cYou are already at maximum Strength!");
            return;
        }
        data.modifyStrength(1);
        data.save();
        MessageUtil.send(player, "&aYou drank a Strength Bottle! &7(&c" + (data.getStrength() - 1) + "&a \u2192 &c" + data.getStrength() + "&7)");
        int amount = item.getAmount();
        if (amount > 1) item.setAmount(amount - 1);
        else player.getInventory().remove(item);
    }

    private void useReviveToken(Player player, ItemStack item) {
        PlayerData data = plugin.getPlayerData(player);
        int min = plugin.getConfigManager().getMinStrength();

        if (!data.isLocked() || data.getStrength() != min) {
            MessageUtil.send(player, plugin.getConfigManager().getReviveCannot());
            return;
        }

        int amount = item.getAmount();
        if (amount > 1) item.setAmount(amount - 1);
        else player.getInventory().remove(item);

        data.setLocked(false);
        data.setStrength(plugin.getConfigManager().getReviveTokenRestoreStrength());
        data.save();

        if (plugin.getConfigManager().isReviveEffect()) {
            SpawnEffect.spawnReviveEffect(plugin, player);
        }

        MessageUtil.send(player, plugin.getConfigManager().getReviveUsed()
                .replace("{level}", String.valueOf(data.getStrength())));
    }

    private void useStrengthShard(Player player, ItemStack item) {
        PlayerData data = plugin.getPlayerData(player);
        int max = plugin.getConfigManager().getMaxStrength();
        if (data.getStrength() >= max) {
            MessageUtil.send(player, "&cYou are already at maximum Strength!");
            return;
        }
        if (plugin.getPunishCommand().isPunished(player)) {
            MessageUtil.send(player, "&c&lPUNISHED: Cannot use Strength Shard while punished.");
            return;
        }
        data.modifyStrength(1);
        data.save();
        MessageUtil.send(player, "&aYou crushed a Strength Shard! &7(&c" + (data.getStrength() - 1) + "&a \u2192 &c" + data.getStrength() + "&7)");
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0f, 1.2f);
        int amount = item.getAmount();
        if (amount > 1) item.setAmount(amount - 1);
        else player.getInventory().remove(item);
    }

    private void useRerollGrimoire(Player player, ItemStack item) {
        PlayerData data = plugin.getPlayerData(player);
        int cost = plugin.getConfigManager().getSoulBindCost();
        if (data.getStrength() < cost) {
            MessageUtil.send(player, "&cYou need &f" + cost + " Strength&c to reroll your class.");
            return;
        }
        data.modifyStrength(-cost);
        data.setClassAssigned(false);
        data.setWeaponClass(WeaponClass.NONE);
        data.save();
        int amount = item.getAmount();
        if (amount > 1) item.setAmount(amount - 1);
        else player.getInventory().remove(item);
        plugin.getAbilityManager().assignRandomClass(player);
        MessageUtil.send(player, "&aYour class has been rerolled! New class: &e" + data.getWeaponClass().getDisplayName());
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
    }

    public static class CleaveBuff {
        private static final Map<UUID, Long> buffs = new HashMap<>();
        public static void put(UUID uuid, long expiry) { buffs.put(uuid, expiry); }
        public static boolean hasBuff(UUID uuid) {
            Long e = buffs.get(uuid);
            return e != null && System.currentTimeMillis() < e;
        }
        public static void consume(UUID uuid) { buffs.remove(uuid); }
    }
}
