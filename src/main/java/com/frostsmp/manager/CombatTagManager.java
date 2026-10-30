package com.frostsmp.manager;

import com.frostsmp.FrostStrengthSMP;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatTagManager {

    private final FrostStrengthSMP plugin;
    private final Map<UUID, Long> combatTags = new HashMap<>(); // player -> expiry timestamp

    public CombatTagManager(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    public void tag(Player player, long durationMillis) {
        combatTags.put(player.getUniqueId(), System.currentTimeMillis() + durationMillis);
    }

    public void untag(Player player) {
        combatTags.remove(player.getUniqueId());
    }

    public boolean isTagged(Player player) {
        Long expiry = combatTags.get(player.getUniqueId());
        if (expiry == null) return false;
        if (System.currentTimeMillis() >= expiry) {
            combatTags.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    public long getRemainingSeconds(Player player) {
        Long expiry = combatTags.get(player.getUniqueId());
        if (expiry == null) return 0;
        long remaining = expiry - System.currentTimeMillis();
        return Math.max(0, remaining / 1000);
    }
}
