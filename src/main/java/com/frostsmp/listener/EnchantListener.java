package com.frostsmp.listener;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantListener implements Listener {

    private final FrostStrengthSMP plugin;

    public EnchantListener(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    private boolean isMace(ItemStack item) {
        return item != null && item.getType() == Material.MACE;
    }

    @EventHandler
    public void onEnchantTable(EnchantItemEvent event) {
        if (isMace(event.getItem())) {
            event.setCancelled(true);
            if (event.getEnchanter() instanceof Player) {
                MessageUtil.send((Player) event.getEnchanter(), plugin.getConfigManager().getMaceBlocked());
            }
        }
    }

    @EventHandler
    public void onPrepareEnchantment(PrepareItemEnchantEvent event) {
        if (isMace(event.getItem())) {
            event.getInventory().setItem(0, new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        if (!plugin.getConfigManager().isEzAnvilEnabled()) return;
        AnvilInventory inv = event.getInventory();
        inv.setRepairCost(plugin.getConfigManager().getEzAnvilRepairCost());
    }

    @EventHandler
    public void onAnvilClick(InventoryClickEvent event) {
        if (!(event.getInventory() instanceof AnvilInventory)) return;
        ItemStack result = event.getCurrentItem();
        if (result == null || result.getType() == Material.AIR) return;
        ItemStack[] contents = event.getInventory().getContents();
        for (ItemStack slotItem : contents) {
            if (isMace(slotItem)) {
                event.setCancelled(true);
                if (event.getWhoClicked() instanceof Player) {
                    MessageUtil.send((Player) event.getWhoClicked(), plugin.getConfigManager().getMaceBlocked());
                }
                return;
            }
        }
    }
}
