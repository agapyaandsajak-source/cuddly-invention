package com.frostsmp.listener;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class TotemLimiterListener implements Listener {

    private final FrostStrengthSMP plugin;

    public TotemLimiterListener(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    private int countTotems(Player player) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.TOTEM_OF_UNDYING) {
                count += item.getAmount();
            }
        }
        ItemStack off = player.getInventory().getItemInOffHand();
        if (off != null && off.getType() == Material.TOTEM_OF_UNDYING) {
            count += off.getAmount();
        }
        return count;
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (!plugin.getConfigManager().isTotemLimiterEnabled()) return;
        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();
        if (item == null || item.getType() != Material.TOTEM_OF_UNDYING) return;

        int current = countTotems(player);
        int max = plugin.getConfigManager().getTotemMax();
        int adding = item.getAmount();

        if (current + adding > max) {
            event.setCancelled(true);
            player.sendMessage(plugin.getConfigManager().getPrefix()
                    + "&cTotem limit: You cannot carry more than &e" + max + "&c totems!");
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!plugin.getConfigManager().isTotemLimiterEnabled()) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() != Material.TOTEM_OF_UNDYING) return;

        int current = countTotems(player) - item.getAmount();
        int max = plugin.getConfigManager().getTotemMax();
        int targetSlot = event.getSlot();

        // Simplified: check if putting into inventory would exceed limit
        if (current + item.getAmount() > max) {
            event.setCancelled(true);
            player.sendMessage(plugin.getConfigManager().getPrefix()
                    + "&cTotem limit: You cannot carry more than &e" + max + "&c totems!");
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!plugin.getConfigManager().isTotemLimiterEnabled()) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        for (ItemStack item : event.getNewItems().values()) {
            if (item == null || item.getType() != Material.TOTEM_OF_UNDYING) continue;
            int current = countTotems(player) - item.getAmount();
            int max = plugin.getConfigManager().getTotemMax();
            if (current + item.getAmount() > max) {
                event.setCancelled(true);
                player.sendMessage(plugin.getConfigManager().getPrefix()
                        + "&cTotem limit: You cannot carry more than &e" + max + "&c totems!");
                return;
            }
        }
    }
}
