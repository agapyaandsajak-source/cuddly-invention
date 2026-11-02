package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.manager.DailyRewardManager;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DailyCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;
    private final DailyRewardManager dailyRewardManager;
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    public DailyCommand(FrostStrengthSMP plugin, DailyRewardManager dailyRewardManager) {
        this.plugin = plugin;
        this.dailyRewardManager = dailyRewardManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cPlayers only."));
            return true;
        }
        Player player = (Player) sender;

        if (!dailyRewardManager.canClaim(player)) {
            player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix()
                    + "&cDaily reward on cooldown! &7Come back in &f" + dailyRewardManager.getRemainingTimeString(player) + "&7."));
            return true;
        }

        dailyRewardManager.claim(player);
        return true;
    }
}
