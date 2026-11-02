package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.manager.RankManager;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AbuseCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;
    private final RankManager rankManager;
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    public AbuseCommand(FrostStrengthSMP plugin, RankManager rankManager) {
        this.plugin = plugin;
        this.rankManager = rankManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cPlayers only."));
            return true;
        }
        Player reporter = (Player) sender;

        if (args.length < 2) {
            reporter.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cUsage: /abuse <player> <reason>"));
            reporter.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&7Example: /abuse AdminName spawnkilling"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            reporter.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cPlayer not found."));
            return true;
        }

        String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));

        rankManager.isAdminAbuseReported(reporter, target, reason);

        reporter.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix()
                + "&aAdmin abuse report sent to &cAGAPYA&a."));
        reporter.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix()
                + "&7Reported: &f" + target.getName() + "&7 | Reason: &f" + reason));

        // Broadcast to all admins
        String alert = plugin.getConfigManager().getPrefix()
                + "&c&lADMIN ABUSE REPORT: &f" + reporter.getName() + "&c reported &f" + target.getName()
                + "&c for: &f" + reason;
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (p.hasPermission("frostsmp.admin")) {
                p.sendMessage(LEGACY.deserialize(alert));
            }
        });

        return true;
    }
}
