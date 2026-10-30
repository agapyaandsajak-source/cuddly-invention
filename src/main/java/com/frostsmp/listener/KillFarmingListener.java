package com.frostsmp.listener;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KillFarmingListener implements Listener {

    private final FrostStrengthSMP plugin;
    // killer UUID -> victim UUID -> list of kill timestamps (millis)
    private final Map<UUID, Map<UUID, java.util.List<Long>>> killTimes = new HashMap<>();

    public KillFarmingListener(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!plugin.getConfigManager().isAntiFarmEnabled()) return;

        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        if (killer == null || killer.equals(victim)) return;

        UUID killerId = killer.getUniqueId();
        UUID victimId = victim.getUniqueId();
        long now = System.currentTimeMillis();
        long windowMillis = plugin.getConfigManager().getAntiFarmWindowMinutes() * 60L * 1000L;

        killTimes.computeIfAbsent(killerId, k -> new HashMap<>());
        Map<UUID, java.util.List<Long>> victimMap = killTimes.get(killerId);
        victimMap.computeIfAbsent(victimId, v -> new java.util.ArrayList<>());
        java.util.List<Long> times = victimMap.get(victimId);

        // Clean old entries
        times.removeIf(t -> now - t > windowMillis);
        times.add(now);

        int maxKills = plugin.getConfigManager().getAntiFarmMaxKillsPerVictim();
        if (times.size() > maxKills) {
            // Deny strength gain for this kill
            event.setDeathMessage(null);
            killer.sendMessage(plugin.getConfigManager().getPrefix()
                    + "&cAnti kill-farming: You cannot gain strength from this victim yet!");
        }
    }
}
