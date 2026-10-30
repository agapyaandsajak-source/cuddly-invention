package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.data.PlayerData;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StrengthAdminCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;

    public StrengthAdminCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("frostsmp.admin")) {
            sender.sendMessage(MessageUtil.component("&cNo permission."));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(MessageUtil.component("&6===== &lFrost &f&lSMP Admin &r&6====="));
            sender.sendMessage(MessageUtil.component("&7/strengthadmin set <player> <amount>"));
            sender.sendMessage(MessageUtil.component("&7/strengthadmin reload"));
            sender.sendMessage(MessageUtil.component("&7/strengthadmin reset <player>"));
            sender.sendMessage(MessageUtil.component("&7/strengthadmin ban <player>"));
            sender.sendMessage(MessageUtil.component("&7/strengthadmin pardon <player>"));
            sender.sendMessage(MessageUtil.component("&7/strengthadmin revive <player>"));
            sender.sendMessage(MessageUtil.component("&7/strengthadmin punish <player>"));
            return true;
        }
        String sub = args[0].toLowerCase();
        switch (sub) {
            case "set":
                if (args.length < 3) {
                    sender.sendMessage(MessageUtil.component("&cUsage: /strengthadmin set <player> <amount>"));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) { sender.sendMessage(MessageUtil.component("&cPlayer not found.")); return true; }
                int amount;
                try { amount = Integer.parseInt(args[2]); } catch (NumberFormatException e) {
                    sender.sendMessage(MessageUtil.component("&cInvalid number.")); return true;
                }
                PlayerData data = plugin.getPlayerData(target);
                data.setStrength(amount);
                data.save();
                sender.sendMessage(MessageUtil.component("&aSet " + target.getName() + "'s Strength to " + data.getStrength()));
                MessageUtil.send(target, "&eYour Strength was set to &c" + data.getStrength() + "&e by an admin.");
                break;
            case "reload":
                plugin.getConfigManager().reload();
                sender.sendMessage(MessageUtil.component("&aFrost SMP config reloaded."));
                break;
            case "reset":
                if (args.length < 2) { sender.sendMessage(MessageUtil.component("&cUsage: /strengthadmin reset <player>")); return true; }
                Player rTarget = Bukkit.getPlayer(args[1]);
                if (rTarget == null) { sender.sendMessage(MessageUtil.component("&cPlayer not found.")); return true; }
                PlayerData rData = plugin.getPlayerData(rTarget);
                rData.setStrength(plugin.getConfigManager().getStartingStrength());
                rData.setLocked(false);
                rData.save();
                sender.sendMessage(MessageUtil.component("&aReset " + rTarget.getName() + "'s Strength."));
                break;
            case "ban":
                if (args.length < 2) { sender.sendMessage(MessageUtil.component("&cUsage: /strengthadmin ban <player>")); return true; }
                Player bTarget = Bukkit.getPlayer(args[1]);
                if (bTarget == null) { sender.sendMessage(MessageUtil.component("&cPlayer not found.")); return true; }
                Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(bTarget.getName(), "Banned by admin (Frost SMP)", null, null);
                bTarget.kickPlayer("&cYou have been banned from Frost SMP.");
                sender.sendMessage(plugin.getConfigManager().getAdminBanned().replace("{player}", bTarget.getName()));
                break;
            case "pardon":
                if (args.length < 2) { sender.sendMessage(MessageUtil.component("&cUsage: /strengthadmin pardon <player>")); return true; }
                Player pTarget = Bukkit.getPlayer(args[1]);
                if (pTarget == null) { sender.sendMessage(MessageUtil.component("&cPlayer not found.")); return true; }
                PlayerData pData = plugin.getPlayerData(pTarget);
                pData.setLocked(false);
                pData.setStrength(plugin.getConfigManager().getReviveTokenRestoreStrength());
                pData.save();
                pTarget.setGameMode(org.bukkit.GameMode.SURVIVAL);
                sender.sendMessage(plugin.getConfigManager().getAdminPardoned().replace("{player}", pTarget.getName()));
                MessageUtil.send(pTarget, "&aAn admin has pardoned you! You can play again.");
                break;
            case "revive":
                if (args.length < 2) { sender.sendMessage(MessageUtil.component("&cUsage: /strengthadmin revive <player>")); return true; }
                Player vTarget = Bukkit.getPlayer(args[1]);
                if (vTarget == null) { sender.sendMessage(MessageUtil.component("&cPlayer not found.")); return true; }
                PlayerData vData = plugin.getPlayerData(vTarget);
                vData.setLocked(false);
                vData.setStrength(plugin.getConfigManager().getReviveTokenRestoreStrength());
                vData.save();
                vTarget.setGameMode(org.bukkit.GameMode.SURVIVAL);
                sender.sendMessage(plugin.getConfigManager().getAdminRevived().replace("{player}", vTarget.getName()));
                MessageUtil.send(vTarget, "&aAn admin has revived you! Strength restored.");
                break;
            case "punish":
                if (args.length < 2) { sender.sendMessage(MessageUtil.component("&cUsage: /strengthadmin punish <player>")); return true; }
                Player punTarget = Bukkit.getPlayer(args[1]);
                if (punTarget == null) { sender.sendMessage(MessageUtil.component("&cPlayer not found.")); return true; }
                plugin.getPunishCommand().applyPunish(punTarget);
                sender.sendMessage(MessageUtil.component("&c&lPUNISHED: " + punTarget.getName() + " now takes double strength loss for 24h."));
                MessageUtil.send(punTarget, "&c&lYou have been PUNISHED! Double strength loss for 24 hours.");
                break;
            default:
                sender.sendMessage(MessageUtil.component("&cUnknown subcommand."));
                break;
        }
        return true;
    }
}
