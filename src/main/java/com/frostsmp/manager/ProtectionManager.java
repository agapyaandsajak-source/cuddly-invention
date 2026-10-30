package com.frostsmp.manager;

import com.frostsmp.FrostStrengthSMP;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProtectionManager {

    private final FrostStrengthSMP plugin;
    // UUID -> expiry timestamp (millis)
    private final Map<UUID, Long> deathProtection = new HashMap<>();
    private final Map<UUID, Long> newbieProtection = new HashMap<>();

    public ProtectionManager(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    public void grantDeathProtection(Player player, long durationMillis) {
        deathProtection.put(player.getUniqueId(), System.currentTimeMillis() + durationMillis);
    }

    public void grantNewbieProtection(Player player, long durationMillis) {
        newbieProtection.put(player.getUniqueId(), System.currentTimeMillis() + durationMillis);
    }

    public boolean hasDeathProtection(Player player) {
        Long expiry = deathProtection.get(player.getUniqueId());
        if (expiry == null) return false;
        if (System.currentTimeMillis() >= expiry) {
            deathProtection.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    public boolean hasNewbieProtection(Player player) {
        Long expiry = newbieProtection.get(player.getUniqueId());
        if (expiry == null) return false;
        if (System.currentTimeMillis() >= expiry) {
            newbieProtection.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    public void removeDeathProtection(Player player) {
        deathProtection.remove(player.getUniqueId());
    }

    public void removeNewbieProtection(Player player) {
        newbieProtection.remove(player.getUniqueId());
    }

    public boolean isProtected(Player player) {
        return hasDeathProtection(player) || hasNewbieProtection(player);
    }
}
