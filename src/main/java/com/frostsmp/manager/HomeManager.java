package com.frostsmp.manager;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.SafeTeleportUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeManager {

    private final FrostStrengthSMP plugin;
    private final Map<UUID, Long> homeCooldowns = new HashMap<>();
    private final Map<UUID, Long> rtpCooldowns = new HashMap<>();

    public HomeManager(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    public boolean isHomeCooldown(Player player) {
        Long end = homeCooldowns.get(player.getUniqueId());
        if (end == null) return false;
        long now = System.currentTimeMillis();
        if (now >= end) {
            homeCooldowns.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    public long getHomeCooldownRemaining(Player player) {
        Long end = homeCooldowns.get(player.getUniqueId());
        if (end == null) return 0;
        return Math.max(0, end - System.currentTimeMillis());
    }

    public void setHomeCooldown(Player player, int seconds) {
        homeCooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (seconds * 1000L));
    }

    public boolean isRtpCooldown(Player player) {
        Long end = rtpCooldowns.get(player.getUniqueId());
        if (end == null) return false;
        long now = System.currentTimeMillis();
        if (now >= end) {
            rtpCooldowns.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    public long getRtpCooldownRemaining(Player player) {
        Long end = rtpCooldowns.get(player.getUniqueId());
        if (end == null) return 0;
        return Math.max(0, end - System.currentTimeMillis());
    }

    public void setRtpCooldown(Player player, int seconds) {
        rtpCooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (seconds * 1000L));
    }

    public Location getHome(Player player) {
        return plugin.getPlayerData(player).getHomeLocation();
    }

    public void setHome(Player player, Location location) {
        plugin.getPlayerData(player).setHomeLocation(location);
    }

    public boolean hasHome(Player player) {
        return plugin.getPlayerData(player).getHomeLocation() != null;
    }

    public void removeHome(Player player) {
        plugin.getPlayerData(player).setHomeLocation(null);
    }

    public Location findRtpLocation(Player player) {
        World world = player.getWorld();
        int range = plugin.getConfigManager().getRtpRange();
        int maxAttempts = plugin.getConfigManager().getRtpMaxAttempts();
        return SafeTeleportUtil.findSafeRtpLocation(world, player.getLocation().getBlockX(), player.getLocation().getBlockZ(), range, maxAttempts);
    }
}
