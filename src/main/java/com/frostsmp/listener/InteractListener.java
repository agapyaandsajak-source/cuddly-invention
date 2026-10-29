package com.frostsmp.listener;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.antiCheat.ViolationManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {

    private final FrostStrengthSMP plugin;
    private final ViolationManager violations;

    public InteractListener(FrostStrengthSMP plugin) {
        this.plugin = plugin;
        this.violations = new ViolationManager(plugin);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!plugin.getConfigManager().isAntiCheatEnabled()) return;
        Player player = event.getPlayer();
        if (plugin.getConfigManager().isExemptOp() && player.isOp()) return;

        if (plugin.getConfigManager().isFastBreak()) {
            violations.add(player, "break", 3);
        }

        if (plugin.getConfigManager().isEzWebsEnabled()
                && plugin.getConfigManager().isEzWebsInstantBreak()
                && event.getBlock().getType() == Material.COBWEB) {
            event.setDropItems(false);
            event.getBlock().setType(Material.AIR);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!plugin.getConfigManager().isEzWebsEnabled()) return;
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null && block.getType() == Material.COBWEB) {
            if (event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK) {
                block.setType(Material.AIR);
                block.getWorld().dropItemNaturally(block.getLocation(), new org.bukkit.inventory.ItemStack(Material.COBWEB));
                event.setCancelled(true);
            }
        }
    }
}
