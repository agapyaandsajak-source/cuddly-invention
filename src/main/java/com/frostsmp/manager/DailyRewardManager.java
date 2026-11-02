package com.frostsmp.manager;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DailyRewardManager {

    private final FrostStrengthSMP plugin;
    // player UUID -> last claim timestamp (millis)
    private final Map<UUID, Long> lastClaim = new HashMap<>();
    private static final long COOLDOWN_MILLIS = 24 * 60 * 60 * 1000; // 24 hours
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    public DailyRewardManager(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    public boolean canClaim(Player player) {
        Long last = lastClaim.get(player.getUniqueId());
        if (last == null) return true;
        return System.currentTimeMillis() - last >= COOLDOWN_MILLIS;
    }

    public long getRemainingMillis(Player player) {
        Long last = lastClaim.get(player.getUniqueId());
        if (last == null) return 0;
        long remaining = COOLDOWN_MILLIS - (System.currentTimeMillis() - last);
        return Math.max(0, remaining);
    }

    public void claim(Player player) {
        lastClaim.put(player.getUniqueId(), System.currentTimeMillis());

        // Give rewards: strength bottle + some strength
        com.frostsmp.data.PlayerData data = plugin.getPlayerData(player);
        int strengthGain = 1;
        data.modifyStrength(strengthGain);
        data.save();

        player.getInventory().addItem(com.frostsmp.item.CustomItems.strengthBottle());

        player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix()
                + "&a&lDAILY REWARD CLAIMED! &7+1 Strength &7+ 1 Strength Bottle"));
    }

    public String getRemainingTimeString(Player player) {
        long millis = getRemainingMillis(player);
        long hours = millis / (1000 * 60 * 60);
        long minutes = (millis % (1000 * 60 * 60)) / (1000 * 60);
        return hours + "h " + minutes + "m";
    }
}
