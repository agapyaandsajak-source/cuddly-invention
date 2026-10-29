package com.frostsmp.antiCheat;

import com.frostsmp.FrostStrengthSMP;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ViolationManager {

    private final FrostStrengthSMP plugin;
    private final Map<UUID, Integer> points = new HashMap<>();
    private final Map<UUID, Long> lastDecay = new HashMap<>();

    public ViolationManager(FrostStrengthSMP plugin) {
        this.plugin = plugin;
        plugin.getServer().getScheduler().runTaskTimer(plugin, this::decay, 20L * 10, 20L * 10);
    }

    public void add(Player player, String type, int amount) {
        if (!plugin.getConfigManager().isAntiCheatEnabled()) return;
        UUID uuid = player.getUniqueId();
        int current = points.getOrDefault(uuid, 0) + amount;
        points.put(uuid, current);
        lastDecay.put(uuid, System.currentTimeMillis());

        if (current >= plugin.getConfigManager().getViolationThreshold()) {
            if (plugin.getConfigManager().isKickOnViolation()) {
                player.kickPlayer(plugin.getConfigManager().getPrefix()
                        + "&cKicked for suspicious activity (Anti-Cheat: " + type + ").");
            }
            points.remove(uuid);
        }
    }

    private void decay() {
        long now = System.currentTimeMillis();
        points.keySet().forEach(uuid -> {
            Long last = lastDecay.get(uuid);
            if (last != null && now - last > 20_000) {
                int current = points.getOrDefault(uuid, 0) - 1;
                if (current <= 0) {
                    points.remove(uuid);
                    lastDecay.remove(uuid);
                } else {
                    points.put(uuid, current);
                }
            }
        });
    }
}
