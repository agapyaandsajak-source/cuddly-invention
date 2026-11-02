package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    public BroadcastCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cPlayers only."));
            return true;
        }
        Player player = (Player) sender;

        // Only AGAPYA and admins can broadcast
        if (!player.getName().equalsIgnoreCase("AGAPYA") && !player.hasPermission("frostsmp.admin")) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&cOnly AGAPYA or admins can use this command.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&cUsage: /broadcast <message>");
            return true;
        }

        String message = String.join(" ", args);
        Component comp = LEGACY.deserialize(plugin.getConfigManager().getPrefix() + message);

        // Send to middle of screen using action bar
        Bukkit.getOnlinePlayers().forEach(p -> p.sendActionBar(comp));

        sender.sendMessage(plugin.getConfigManager().getPrefix() + "&aBroadcast sent to all players' screens!");
        return true;
    }
}
