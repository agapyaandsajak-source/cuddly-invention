package com.frostsmp.listener;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.manager.ProtectionManager;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ProtectionListener implements Listener {

    private final FrostStrengthSMP plugin;
    private final ProtectionManager protectionManager;

    public ProtectionListener(FrostStrengthSMP plugin, ProtectionManager protectionManager) {
        this.plugin = plugin;
        this.protectionManager = protectionManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.getConfigManager().isNewbieProtectionEnabled()) {
            long duration = plugin.getConfigManager().getNewbieProtectionDuration() * 1000L;
            protectionManager.grantNewbieProtection(player, duration);
            String msg = plugin.getConfigManager().getPrefix()
                    + "&aNewbie protection active for 1 hour - no strength loss on death.";
            player.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(msg));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();

        if (plugin.getConfigManager().isDeathProtectionEnabled()) {
            if (protectionManager.hasDeathProtection(victim)) {
                String msg = plugin.getConfigManager().getPrefix()
                        + "&aDeath protection active - no strength lost.";
                victim.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(msg));
                protectionManager.removeDeathProtection(victim);
                return; // Don't apply strength loss
            }
        }

        // Grant death protection for next death
        if (plugin.getConfigManager().isDeathProtectionEnabled()) {
            long duration = plugin.getConfigManager().getDeathProtectionDuration() * 1000L;
            protectionManager.grantDeathProtection(victim, duration);
        }
    }
}
