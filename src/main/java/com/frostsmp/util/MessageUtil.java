package com.frostsmp.util;

import com.frostsmp.FrostStrengthSMP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public final class MessageUtil {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    private MessageUtil() {}

    public static void send(Player player, String message) {
        if (player == null || message == null) return;
        FrostStrengthSMP plugin = FrostStrengthSMP.getInstance();
        String prefixed = plugin.getConfigManager().getPrefix() + message;
        player.sendMessage(LEGACY.deserialize(prefixed));
    }

    public static Component component(String message) {
        FrostStrengthSMP plugin = FrostStrengthSMP.getInstance();
        String prefixed = plugin.getConfigManager().getPrefix() + message;
        return LEGACY.deserialize(prefixed);
    }

    public static String plain(Component component) {
        return LEGACY.serialize(component).replace("§", "&");
    }

    public static void actionBar(Player player, String message) {
        if (player == null || message == null) return;
        FrostStrengthSMP plugin = FrostStrengthSMP.getInstance();
        if (!plugin.getConfigManager().isActionBarEnabled()) return;
        String formatted = message
                .replace("{level}", String.valueOf(plugin.getPlayerData(player).getStrength()))
                .replace("{max}", String.valueOf(plugin.getConfigManager().getMaxStrength()));
        Component comp = LEGACY.deserialize(plugin.getConfigManager().getPrefix() + formatted);
        player.sendActionBar(comp);
    }
}
