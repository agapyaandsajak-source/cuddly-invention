package com.frostsmp.manager;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class RankManager {

    private final FrostStrengthSMP plugin;
    // player UUID -> rank number (1 = best)
    private final Map<UUID, Integer> playerRanks = new HashMap<>();
    private final Map<UUID, Long> rankSetTime = new HashMap<>();
    public static final int MAX_RANK = 20;
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    public RankManager(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    public boolean isRanked(Player player) {
        return playerRanks.containsKey(player.getUniqueId());
    }

    public int getRank(Player player) {
        return playerRanks.getOrDefault(player.getUniqueId(), 0);
    }

    public void setRank(Player player, int rank) {
        if (rank < 1) rank = 1;
        if (rank > MAX_RANK) rank = MAX_RANK;
        playerRanks.put(player.getUniqueId(), rank);
        rankSetTime.put(player.getUniqueId(), System.currentTimeMillis());
        applyRankBonuses(player);
        plugin.getLogger().log(Level.INFO, "[RankManager] " + player.getName() + " set to rank #" + rank);
    }

    public void removeRank(Player player) {
        playerRanks.remove(player.getUniqueId());
        rankSetTime.remove(player.getUniqueId());
        removeRankBonuses(player);
    }

    public void swapRanks(Player killer, Player victim) {
        int killerRank = getRank(killer);
        int victimRank = getRank(victim);
        if (killerRank == 0 || victimRank == 0) return;
        if (victimRank < killerRank) {
            // Victim has better rank - swap
            setRank(killer, victimRank);
            setRank(victim, killerRank);
            Component msg = LEGACY.deserialize(plugin.getConfigManager().getPrefix()
                    + "&a&lRANK SWAP! &f" + killer.getName() + "&a took &f" + victim.getName() + "'s&a rank (#" + victimRank + ")!");
            Bukkit.broadcast(msg);
        }
    }

    public void assignRandomRanks() {
        java.util.List<Player> players = new java.util.ArrayList<>(Bukkit.getOnlinePlayers());
        java.util.Collections.shuffle(players);
        int total = players.size();
        for (int i = 0; i < players.size(); i++) {
            int rank = i + 1;
            if (rank > MAX_RANK) rank = MAX_RANK;
            setRank(players.get(i), rank);
        }
        plugin.getLogger().log(Level.INFO, "[RankManager] Assigned random ranks to " + total + " players");
    }

    public String getRankDisplay(Player player) {
        int rank = getRank(player);
        if (rank == 0) return "&7[UNRANKED]";
        return "&e[#" + rank + "]";
    }

    public Component getRankComponent(Player player) {
        return LEGACY.deserialize(getRankDisplay(player));
    }

    public int getMaxHearts(Player player) {
        int rank = getRank(player);
        if (rank == 0) return 20; // 10 hearts default
        // Rank 1 = 20 hearts, Rank 20 = 10.5 hearts (21 half-hearts)
        // Formula: 20 - (rank-1) * 0.5 hearts
        double hearts = 20.0 - (rank - 1) * 0.5;
        if (hearts < 10.5) hearts = 10.5;
        return (int) Math.round(hearts * 2); // return in half-hearts for attribute
    }

    public double getPotionDurationMultiplier(Player player) {
        int rank = getRank(player);
        if (rank == 0) return 1.0;
        // Rank 1 = 2.0x, Rank 20 = 1.05x
        return 2.0 - (rank - 1) * (1.0 / 19.0);
    }

    public double getXpMultiplier(Player player) {
        int rank = getRank(player);
        if (rank == 0) return 1.0;
        // Rank 1 = 3.0x, Rank 20 = 1.1x
        return 3.0 - (rank - 1) * (1.9 / 19.0);
    }

    public int getExtraInventorySlots(Player player) {
        int rank = getRank(player);
        if (rank == 0) return 0;
        if (rank <= 2) return 54;
        if (rank == 3) return 50;
        if (rank == 5) return 40;
        if (rank == 10) return 15;
        return 0;
    }

    public void applyRankBonuses(Player player) {
        // Set max health
        double maxHealth = getMaxHearts(player);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        player.setHealth(player.getHealth()); // recalculate
    }

    public void removeRankBonuses(Player player) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
    }

    public void applyPotionBoost(Player player, PotionEffect effect) {
        if (!isRanked(player)) return;
        double multiplier = getPotionDurationMultiplier(player);
        int newDuration = (int) (effect.getDuration() * multiplier);
        player.addPotionEffect(new PotionEffect(effect.getType(), newDuration, effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()));
    }

    public boolean isAdminAbuseReported(Player reporter, Player target, String reason) {
        // Log the abuse report
        plugin.getLogger().log(Level.WARNING, "[ADMIN ABUSE REPORT] " + reporter.getName() + " reported " + target.getName() + ": " + reason);
        // Notify AGAPYA
        Player agapya = Bukkit.getPlayer("AGAPYA");
        if (agapya != null) {
            agapya.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix()
                    + "&c&lADMIN ABUSE REPORT: &f" + reporter.getName() + "&c reported &f" + target.getName()
                    + "&c for: &f" + reason));
        }
        return true;
    }
}
