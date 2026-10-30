package com.frostsmp.manager;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpaManager {

    private final FrostStrengthSMP plugin;
    // target UUID -> requester UUID (pending TPA request)
    private final Map<UUID, UUID> pendingRequests = new HashMap<>();
    // requester UUID -> expiry timestamp
    private final Map<UUID, Long> requestExpiry = new HashMap<>();
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');
    private static final long TPA_TIMEOUT_MILLIS = 60_000; // 1 minute

    public TpaManager(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    public boolean requestTpa(Player requester, Player target) {
        if (requester.equals(target)) {
            requester.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cYou cannot teleport to yourself!"));
            return false;
        }

        if (plugin.getConfigManager().isStrengthLockEnabled()) {
            if (plugin.getPlayerData(requester).isLocked()) {
                requester.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cYou cannot teleport while locked at -5 Strength."));
                return false;
            }
            if (plugin.getPlayerData(target).isLocked()) {
                requester.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cThat player is locked and cannot be teleported to."));
                return false;
            }
        }

        UUID targetId = target.getUniqueId();
        UUID requesterId = requester.getUniqueId();

        // Cancel any existing request from this requester to this target
        pendingRequests.remove(targetId);
        requestExpiry.remove(requesterId);

        pendingRequests.put(targetId, requesterId);
        requestExpiry.put(requesterId, System.currentTimeMillis() + TPA_TIMEOUT_MILLIS);

        String reqMsg = LEGACY.deserialize(plugin.getConfigManager().getPrefix()
                + "&e" + requester.getName() + "&a wants to teleport to you.&7\n"
                + "&7Click &a/tpaccept &7to accept or &c/tpdeny &7to deny.");
        target.sendMessage(reqMsg);

        requester.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix()
                + "&aTeleport request sent to &f" + target.getName() + "&a."));

        return true;
    }

    public boolean acceptTpa(Player player) {
        UUID playerId = player.getUniqueId();
        UUID requesterId = pendingRequests.get(playerId);

        if (requesterId == null) {
            player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cYou have no pending teleport requests."));
            return false;
        }

        // Check if expired
        Long expiry = requestExpiry.get(requesterId);
        if (expiry != null && System.currentTimeMillis() >= expiry) {
            pendingRequests.remove(playerId);
            requestExpiry.remove(requesterId);
            player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cThe teleport request has expired."));
            return false;
        }

        Player requester = plugin.getServer().getPlayer(requesterId);
        if (requester == null || !requester.isOnline()) {
            pendingRequests.remove(playerId);
            requestExpiry.remove(requesterId);
            player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cThat player is no longer online."));
            return false;
        }

        // Check strength lock for both
        if (plugin.getConfigManager().isStrengthLockEnabled()) {
            if (plugin.getPlayerData(requester).isLocked()) {
                requester.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cYou are locked at -5 Strength and cannot teleport."));
                player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cThe requester is locked at -5 Strength."));
                pendingRequests.remove(playerId);
                requestExpiry.remove(requesterId);
                return false;
            }
            if (plugin.getPlayerData(player).isLocked()) {
                player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cYou are locked at -5 Strength and cannot accept teleports."));
                pendingRequests.remove(playerId);
                requestExpiry.remove(requesterId);
                return false;
            }
        }

        // Teleport requester to target
        requester.teleport(player.getLocation());

        String acceptMsg = LEGACY.deserialize(plugin.getConfigManager().getPrefix()
                + "&aTeleport request accepted! &f" + requester.getName() + "&a has been teleported to you.");
        player.sendMessage(acceptMsg);

        requester.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix()
                + "&aYou have been teleported to &f" + player.getName() + "&a."));

        pendingRequests.remove(playerId);
        requestExpiry.remove(requesterId);
        return true;
    }

    public boolean denyTpa(Player player) {
        UUID playerId = player.getUniqueId();
        UUID requesterId = pendingRequests.get(playerId);

        if (requesterId == null) {
            player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cYou have no pending teleport requests."));
            return false;
        }

        Player requester = plugin.getServer().getPlayer(requesterId);
        if (requester != null) {
            requester.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix()
                    + "&cYour teleport request was denied by &f" + player.getName() + "&c."));
        }

        pendingRequests.remove(playerId);
        requestExpiry.remove(requesterId);
        player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&aTeleport request denied."));
        return true;
    }

    public boolean hasPendingRequest(Player player) {
        return pendingRequests.containsKey(player.getUniqueId());
    }

    public void clearRequest(Player player) {
        pendingRequests.remove(player.getUniqueId());
        // Also clean up any expiry where this player is the requester
        requestExpiry.entrySet().removeIf(entry -> entry.getKey().equals(player.getUniqueId()));
    }
}
