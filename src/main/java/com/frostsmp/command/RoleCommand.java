package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.data.PlayerData;
import com.frostsmp.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RoleCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;

    public RoleCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.getConfigManager().isRolesEnabled()) {
            sender.sendMessage("&cRoles are disabled on this server.");
            return true;
        }
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("&cSpecify a player: /role info <player>");
                return true;
            }
            PlayerData data = plugin.getPlayerData((Player) sender);
            sender.sendMessage(MessageUtil.component("&7Your role: &e" + data.getRole()));
            return true;
        }
        String sub = args[0].toLowerCase();
        switch (sub) {
            case "list":
                sender.sendMessage("&6===== &lRoles &r&6=====");
                sender.sendMessage("&7MEMBER &f- Default player");
                sender.sendMessage("&7VIP &f- Small cooldown reduction");
                sender.sendMessage("&7MVP &f- Starts at +2 Strength");
                sender.sendMessage("&7STRENGTH_GOD &f- Starts at +3 Strength");
                sender.sendMessage("&7ADMIN &f- Staff role");
                break;
            case "info":
                if (args.length < 2) {
                    sender.sendMessage("&cUsage: /role info <player>");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) { sender.sendMessage("&cPlayer not found."); return true; }
                PlayerData data = plugin.getPlayerData(target);
                sender.sendMessage(MessageUtil.component("&7" + target.getName() + "&7's role: &e" + data.getRole()));
                break;
            case "set":
                if (!sender.hasPermission("frostsmp.role")) {
                    sender.sendMessage("&cNo permission.");
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage("&cUsage: /role set <player> <role>");
                    return true;
                }
                Player rTarget = Bukkit.getPlayer(args[1]);
                if (rTarget == null) { sender.sendMessage("&cPlayer not found."); return true; }
                plugin.getPlayerData(rTarget).setRole(args[2].toUpperCase());
                plugin.getPlayerData(rTarget).save();
                sender.sendMessage(MessageUtil.component("&aSet " + rTarget.getName() + "'s role to &e" + args[2].toUpperCase() + "&a."));
                break;
            default:
                sender.sendMessage("&cUsage: /role [list|info|set]");
                break;
        }
        return true;
    }
}
