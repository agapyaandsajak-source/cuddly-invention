package com.frostsmp.listener;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.manager.RankManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TabListener implements Listener {

    private final FrostStrengthSMP plugin;
    private final RankManager rankManager;
    private final Map<UUID, Team> playerTeams = new HashMap<>();
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    public TabListener(FrostStrengthSMP plugin, RankManager rankManager) {
        this.plugin = plugin;
        this.rankManager = rankManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        updateTabName(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        removeTabName(player);
    }

    public void updateTabName(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        String teamName = "rank_" + player.getUniqueId();

        // Remove existing team
        Team existing = scoreboard.getTeam(teamName);
        if (existing != null) existing.unregister();

        Team team = scoreboard.registerNewTeam(teamName);
        team.addEntry(player.getName());

        if (rankManager.isRanked(player)) {
            int rank = rankManager.getRank(player);
            String prefix = rankManager.getRankDisplay(player);
            team.prefix(LEGACY.deserialize(prefix));
        } else {
            team.prefix(LEGACY.deserialize("&7[UNRANKED]"));
        }

        // No suffix - don't show strength
        team.suffix(LEGACY.deserialize(""));

        playerTeams.put(player.getUniqueId(), team);
    }

    public void removeTabName(Player player) {
        UUID uuid = player.getUniqueId();
        Team team = playerTeams.remove(uuid);
        if (team != null) {
            team.unregister();
        }
    }

    public void refreshAll() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            updateTabName(player);
        }
    }
}
