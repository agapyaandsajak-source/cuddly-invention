package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LocateCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;

    public LocateCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfigManager().getNotPlayer());
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("frostsmp.admin")) {
            player.sendMessage(plugin.getConfigManager().getNoPermission());
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&cUsage: /locate <structure>");
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&7Example: /locate ancient_city");
            return true;
        }

        // Use vanilla /locate structure - finds structures, not players
        player.performCommand("locate structure " + args[0]);
        return true;
    }
}
