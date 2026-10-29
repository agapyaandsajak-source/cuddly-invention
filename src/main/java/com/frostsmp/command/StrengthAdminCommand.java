package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.data.PlayerData;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StrengthAdminCommand implements CommandExecutor {

    // PLAIN removed
    private final FrostStrengthSMP plugin;

    public StrengthAdminCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("frostsmp.admin")) {
            sender.sendMessage("&cNo permission.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("&6===== &lFrost &f&lSMP Admin &r&6=====");
            sender.sendMessage("&7/strengthadmin set <player> <amount>");
            sender.sendMessage("&7/strengthadmin reload");
            sender.sendMessage("&7/strengthadmin reset <player>");
            sender.sendMessage("&7/strengthadmin ban <player>");
            sender.sendMessage("&7/strengthadmin pardon <player>");
            sender.sendMessage("&7/strengthadmin revive <player>");
            return true;
        }
        String sub = args[0].toLowerCase();
        switch (sub) {
            case "set":
                if (args.length < 3) {
                    sender.sendMessage("&cUsage: /strengthadmin set <player> <amount>");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) { sender.sendMessage("&cPlayer not found."); return true; }
                int amount;
                try { amount = Integer.parseInt(args[2]); } catch (NumberFormatException e) {
                    sender.sendMessage("&cInvalid number."); return true;
                }
                PlayerData data = plugin.getPlayerData(target);
                data.setStrength(amount);
                data.save();
                sender.sendMessage("&aSet " + target.getName() + "'s Strength to " + data.getStrength());
                MessageUtil.send(target, "&eYour Strength was set to &c" + data.getStrength() + "&e by an admin.");
                break;
            case "reload":
                plugin.getConfigManager().reload();
                sender.sendMessage("&aFrost SMP config reloaded.");
                break;
            case "reset":
                if (args.length < 2) { sender.sendMessage("&cUsage: /strengthadmin reset <player>"); return true; }
                Player rTarget = Bukkit.getPlayer(args[1]);
                if (rTarget == null) { sender.sendMessage("&cPlayer not found."); return true; }
                PlayerData rData = plugin.getPlayerData(rTarget);
                rData.setStrength(plugin.getConfigManager().getStartingStrength());
                rData.setLocked(false);
                rData.save();
                sender.sendMessage("&aReset " + rTarget.getName() + "'s Strength.");
                break;
            case "ban":
                if (args.length < 2) { sender.sendMessage("&cUsage: /strengthadmin ban <player>"); return true; }
                Player bTarget = Bukkit.getPlayer(args[1]);
                if (bTarget == null) { sender.sendMessage("&cPlayer not found."); return true; }
                Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(bTarget.getName(), "Banned by admin (Frost SMP)", null, null);
                bTarget.kickPlayer("&cYou have been banned from Frost SMP.");
                sender.sendMessage(plugin.getConfigManager().getAdminBanned().replace("{player}", bTarget.getName()));
                break;
            case "pardon":
                if (args.length < 2) { sender.sendMessage("&cUsage: /strengthadmin pardon <player>"); return true; }
                Player pTarget = Bukkit.getPlayer(args[1]);
                if (pTarget == null) { sender.sendMessage("&cPlayer not found."); return true; }
                PlayerData pData = plugin.getPlayerData(pTarget);
                pData.setLocked(false);
                pData.setStrength(plugin.getConfigManager().getReviveTokenRestoreStrength());
                pData.save();
                pTarget.setGameMode(org.bukkit.GameMode.SURVIVAL);
                sender.sendMessage(plugin.getConfigManager().getAdminPardoned().replace("{player}", pTarget.getName()));
                MessageUtil.send(pTarget, "&aAn admin has pardoned you! You can play again.");
                break;
            case "revive":
                if (args.length < 2) { sender.sendMessage("&cUsage: /strengthadmin revive <player>"); return true; }
                Player vTarget = Bukkit.getPlayer(args[1]);
                if (vTarget == null) { sender.sendMessage("&cPlayer not found."); return true; }
                PlayerData vData = plugin.getPlayerData(vTarget);
                vData.setLocked(false);
                vData.setStrength(plugin.getConfigManager().getReviveTokenRestoreStrength());
                vData.save();
                vTarget.setGameMode(org.bukkit.GameMode.SURVIVAL);
                sender.sendMessage(plugin.getConfigManager().getAdminRevived().replace("{player}", vTarget.getName()));
                MessageUtil.send(vTarget, "&aAn admin has revived you! Strength restored.");
                break;
            default:
                sender.sendMessage("&cUnknown subcommand.");
                break;
        }
        return true;
    }
}
