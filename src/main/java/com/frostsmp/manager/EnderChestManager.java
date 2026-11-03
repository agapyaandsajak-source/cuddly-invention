package com.frostsmp.manager;

import com.frostsmp.FrostStrengthSMP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnderChestManager implements Listener {

    private final FrostStrengthSMP plugin;
    // player UUID -> custom ender chest inventory
    private final Map<UUID, Inventory> enderChests = new HashMap<>();
    private static final int ENDER_CHEST_SIZE = 54; // 6 rows = big ender chest

    public EnderChestManager(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnderChestOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();

        // Only intercept actual ender chests
        if (event.getInventory().getType() != org.bukkit.inventory.InventoryType.ENDER_CHEST) return;

        event.setCancelled(true);

        // Get or create the player's big ender chest
        UUID uuid = player.getUniqueId();
        Inventory chest = enderChests.get(uuid);
        if (chest == null) {
            chest = Bukkit.createInventory(null, ENDER_CHEST_SIZE,
                    net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacy('&')
                            .deserialize("&d&lBig Ender Chest"));
            // Load existing items from player's real ender chest
            org.bukkit.inventory.Inventory realEC = player.getEnderChest();
            for (int i = 0; i < Math.min(realEC.getSize(), ENDER_CHEST_SIZE); i++) {
                if (realEC.getItem(i) != null) {
                    chest.setItem(i, realEC.getItem(i));
                }
            }
            enderChests.put(uuid, chest);
        }

        player.openInventory(chest);
    }

    public void saveEnderChest(Player player) {
        Inventory custom = enderChests.remove(player.getUniqueId());
        if (custom == null) return;

        // Save items back to player's real ender chest
        org.bukkit.inventory.Inventory realEC = player.getEnderChest();
        realEC.clear();
        for (int i = 0; i < Math.min(custom.getSize(), realEC.getSize()); i++) {
            if (custom.getItem(i) != null) {
                realEC.setItem(i, custom.getItem(i));
            }
        }
    }

    public boolean hasOpenChest(Player player) {
        return enderChests.containsKey(player.getUniqueId());
    }
}
