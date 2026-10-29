package com.frostsmp.listener;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.data.PlayerData;
import com.frostsmp.item.CustomItems;
import com.frostsmp.util.MessageUtil;
import com.frostsmp.util.SpawnEffect;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerJoinListener implements Listener {

    private final FrostStrengthSMP plugin;

    public PlayerJoinListener(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData data = plugin.getPlayerData(player);
        data.load();

        if (plugin.getConfigManager().isAssignRandomClass() && !data.isClassAssigned()) {
            plugin.getAbilityManager().assignRandomClass(player);
        }

        if (plugin.getConfigManager().isSpawnEnabled()) {
            if (!player.hasPlayedBefore()) {
                player.teleport(plugin.getSpawnLocation());
                SpawnEffect.spawnSpawnEffect(plugin, plugin.getSpawnLocation());
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    player.setVelocity(player.getLocation().getDirection().multiply(0.4).setY(0.4));
                }, 2L);
            } else {
                player.teleport(plugin.getSpawnLocation());
            }
        }

        player.sendMessage(MessageUtil.component("&7Welcome to &6&lFrost &f&lSMP &7!" +
                " &7Strength: &c" + data.getStrength() + "&7/&c" + plugin.getConfigManager().getMaxStrength()
                + "&7 | Class: &e" + data.getWeaponClass().getDisplayName()));

        if (!player.hasPlayedBefore()) {
            ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 2);
            player.getInventory().addItem(gapple);
            ItemStack web = new ItemStack(Material.COBWEB, 16);
            player.getInventory().addItem(web);
        }
    }
}
