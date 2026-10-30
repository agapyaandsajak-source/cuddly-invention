package com.frostsmp.listener;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.manager.CombatTagManager;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Level;

public class CombatTagListener implements Listener {

    private final FrostStrengthSMP plugin;
    private final CombatTagManager combatTagManager;
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    public CombatTagListener(FrostStrengthSMP plugin, CombatTagManager combatTagManager) {
        this.plugin = plugin;
        this.combatTagManager = combatTagManager;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        if (plugin.getConfigManager().isCombatTagEnabled()) {
            long duration = plugin.getConfigManager().getCombatTagDuration() * 1000L;
            combatTagManager.tag(victim, duration);
            combatTagManager.tag(attacker, duration);

            if (plugin.getConfigManager().isCombatTagBlockCommands()) {
                String tagged = plugin.getConfigManager().getPrefix() + "&cYou are in combat! Logging out will kill you.";
                victim.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(tagged));
                attacker.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(tagged));
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!plugin.getConfigManager().isCombatTagEnabled()
                || !plugin.getConfigManager().isCombatTagBlockCommands()) return;

        Player player = event.getPlayer();
        if (!combatTagManager.isTagged(player)) return;

        String cmd = event.getMessage().toLowerCase();
        // Allow essential commands even in combat
        if (cmd.startsWith("/spawn") || cmd.startsWith("/home")
                || cmd.startsWith("/rtp") || cmd.startsWith("/strength")
                || cmd.startsWith("/menu") || cmd.startsWith("/help")
                || cmd.startsWith("/discord") || cmd.startsWith("/role")) {
            return;
        }

        event.setCancelled(true);
        player.sendMessage(plugin.getConfigManager().getPrefix()
                + "&cYou are in combat! You cannot use commands right now.");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getConfigManager().isCombatTagEnabled()) return;
        if (!combatTagManager.isTagged(player)) return;

        if (plugin.getConfigManager().isCombatTagLogoutKills()) {
            // Kill the player by setting health to 0
            player.setHealth(0.0);
            String msg = plugin.getConfigManager().getPrefix()
                    + "&c" + player.getName() + "&c logged out during combat and was slain!";
            Bukkit.broadcast(LEGACY.deserialize(msg));
        }
        combatTagManager.untag(player);
    }
}
