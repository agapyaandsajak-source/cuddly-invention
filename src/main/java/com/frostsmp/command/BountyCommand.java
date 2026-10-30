package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.manager.BountyManager;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BountyCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;
    private final BountyManager bountyManager;

    public BountyCommand(FrostStrengthSMP plugin, BountyManager bountyManager) {
        this.plugin = plugin;
        this.bountyManager = bountyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfigManager().getNotPlayer());
            return true;
        }
        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&cUsage: /bounty <player> <amount>");
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&cPlayer not found.");
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&cInvalid amount.");
            return true;
        }

        if (amount <= 0) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&cAmount must be positive.");
            return true;
        }

        if (bountyManager.hasBounty(target)) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&cThat player already has a bounty!");
            return true;
        }

        bountyManager.placeBounty(player, target, amount);
        return true;
    }
}
