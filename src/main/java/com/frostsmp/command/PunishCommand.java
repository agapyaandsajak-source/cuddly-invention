package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PunishCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;
    // UUID -> expiry timestamp (millis) for punish (double strength loss)
    private final Map<UUID, Long> punishedUntil = new HashMap<>();

    public PunishCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfigManager().getNotPlayer());
            return true;
        }
        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&cUsage: /punish <player>");
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&cPlayer not found.");
            return true;
        }

        // Punish: double strength loss for 24 hours
        long duration = 24L * 60 * 60 * 1000;
        punishedUntil.put(target.getUniqueId(), System.currentTimeMillis() + duration);

        String msg = plugin.getConfigManager().getPrefix()
                + "&c&lPUNISHED: &f" + target.getName() + "&c now takes double strength loss for 24h.";
        player.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(msg));

        String targetMsg = plugin.getConfigManager().getPrefix()
                + "&c&lYou have been PUNISHED! Double strength loss for 24 hours.";
        target.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(targetMsg));
        return true;
    }

    public void applyPunish(Player player) {
        long duration = 24L * 60 * 60 * 1000;
        punishedUntil.put(player.getUniqueId(), System.currentTimeMillis() + duration);
    }

    public boolean isPunished(Player player) {
        Long expiry = punishedUntil.get(player.getUniqueId());
        if (expiry == null) return false;
        if (System.currentTimeMillis() >= expiry) {
            punishedUntil.remove(player.getUniqueId());
            return false;
        }
        return true;
    }
}
