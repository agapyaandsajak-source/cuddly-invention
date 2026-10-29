package com.frostsmp.listener;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.antiCheat.ViolationManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {

    private final FrostStrengthSMP plugin;
    private final ViolationManager violations;

    public MovementListener(FrostStrengthSMP plugin) {
        this.plugin = plugin;
        this.violations = new ViolationManager(plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!plugin.getConfigManager().isAntiCheatEnabled()) return;
        Player player = event.getPlayer();
        if (plugin.getConfigManager().isExemptOp() && player.isOp()) return;

        Location from = event.getFrom();
        Location to = event.getTo();
        if (from == null || to == null) return;

        if (plugin.getConfigManager().isFlyCheck()
                && !player.getAllowFlight()
                && !player.isFlying()
                && to.getY() - from.getY() > 0.5) {
            Location below1 = to.clone().subtract(0, 1, 0);
            Location below2 = to.clone().subtract(0, 2, 0);
            Block b1 = player.getWorld().getBlockAt(below1);
            Block b2 = player.getWorld().getBlockAt(below2);
            if (!b1.getType().isSolid() && !b2.getType().isSolid()) {
                violations.add(player, "fly", 5);
            }
        }

        double dist = from.distance(to);
        if (dist > plugin.getConfigManager().getSpeedMax()) {
            violations.add(player, "speed", 5);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!plugin.getConfigManager().isAntiCheatEnabled()) return;
        if (!(event.getDamager() instanceof Player)) return;
        Player attacker = (Player) event.getDamager();
        if (plugin.getConfigManager().isExemptOp() && attacker.isOp()) return;

        double dist = attacker.getLocation().distance(event.getEntity().getLocation());
        if (dist > plugin.getConfigManager().getReachMax()) {
            violations.add(attacker, "reach", 8);
        }

        violations.add(attacker, "attack", 1);
    }
}
