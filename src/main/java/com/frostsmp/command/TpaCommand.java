package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.manager.TpaManager;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;
    private final TpaManager tpaManager;
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    public TpaCommand(FrostStrengthSMP plugin, TpaManager tpaManager) {
        this.plugin = plugin;
        this.tpaManager = tpaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cPlayers only."));
            return true;
        }

        Player player = (Player) sender;
        String cmd = command.getName().toLowerCase();

        switch (cmd) {
            case "tpa":
                if (args.length < 1) {
                    player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cUsage: /tpa <player>"));
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cPlayer not found."));
                    return true;
                }
                tpaManager.requestTpa(player, target);
                return true;
            case "tpaccept":
                tpaManager.acceptTpa(player);
                return true;
            case "tpdeny":
                tpaManager.denyTpa(player);
                return true;
            default:
                player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cUnknown command."));
                return true;
        }
    }
}
