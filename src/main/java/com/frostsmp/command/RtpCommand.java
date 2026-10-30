package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.manager.HomeManager;
import com.frostsmp.util.MessageUtil;
import com.frostsmp.util.SafeTeleportUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class RtpCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;
    private final HomeManager homeManager;
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    public RtpCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
        this.homeManager = plugin.getHomeManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cPlayers only."));
            return true;
        }

        Player player = (Player) sender;

        if (plugin.getConfigManager().isStrengthLockEnabled() && plugin.getPlayerData(player).isLocked()) {
            MessageUtil.send(player, "&cYou cannot teleport while locked at -5 Strength.");
            return true;
        }

        int cooldownSeconds = plugin.getConfigManager().getRtpCooldown();
        if (cooldownSeconds > 0 && homeManager.isRtpCooldown(player)) {
            long remaining = homeManager.getRtpCooldownRemaining(player) / 1000L;
            MessageUtil.send(player, "&cRTP on cooldown (&c" + remaining + "s&c).");
            return true;
        }

        Location safeLoc = homeManager.findRtpLocation(player);
        if (safeLoc == null) {
            MessageUtil.send(player, "&cCould not find a safe RTP location. Try again.");
            return true;
        }

        player.teleport(safeLoc);
        homeManager.setRtpCooldown(player, cooldownSeconds);
        MessageUtil.send(player, "&aRandomly teleported to a safe location.");
        return true;
    }
}
