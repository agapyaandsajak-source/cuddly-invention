package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.manager.HomeManager;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class HomeCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;
    private final HomeManager homeManager;
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    public HomeCommand(FrostStrengthSMP plugin) {
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

        if (args.length == 0) {
            return cmdHome(player);
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "set":
            case "sethome":
                return cmdSetHome(player);
            case "remove":
            case "delhome":
            case "home remove":
                return cmdRemoveHome(player);
            default:
                sender.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cUsage: /home [set|remove]"));
                return true;
        }
    }

    private boolean cmdHome(Player player) {
        if (!homeManager.hasHome(player)) {
            MessageUtil.send(player, "&cYou don't have a home set! Use &7/home set &cto set one.");
            return true;
        }

        if (plugin.getConfigManager().isStrengthLockEnabled() && plugin.getPlayerData(player).isLocked()) {
            MessageUtil.send(player, "&cYou cannot teleport while locked at -5 Strength.");
            return true;
        }

        int cooldownSeconds = plugin.getConfigManager().getHomeCooldown();
        if (cooldownSeconds > 0 && homeManager.isHomeCooldown(player)) {
            long remaining = homeManager.getHomeCooldownRemaining(player) / 1000L;
            MessageUtil.send(player, "&cHome teleport on cooldown (&c" + remaining + "s&c).");
            return true;
        }

        Location home = homeManager.getHome(player);
        if (home == null) {
            MessageUtil.send(player, "&cYour home location is invalid.");
            return true;
        }

        // Check if home world is valid
        if (home.getWorld() == null) {
            MessageUtil.send(player, "&cYour home world is not loaded.");
            return true;
        }

        player.teleport(home);
        homeManager.setHomeCooldown(player, cooldownSeconds);
        MessageUtil.send(player, "&aTeleported to your home.");
        return true;
    }

    private boolean cmdSetHome(Player player) {
        if (plugin.getConfigManager().isStrengthLockEnabled() && plugin.getPlayerData(player).isLocked()) {
            MessageUtil.send(player, "&cYou cannot set home while locked at -5 Strength.");
            return true;
        }

        int maxHomes = plugin.getConfigManager().getMaxHomes();
        if (maxHomes > 0 && homeManager.hasHome(player)) {
            // Only 1 home supported for now
            MessageUtil.send(player, "&cYou already have a home set! Use &7/home remove &cto remove it first.");
            return true;
        }

        homeManager.setHome(player, player.getLocation());
        MessageUtil.send(player, "&aHome set at your current location.");
        return true;
    }

    private boolean cmdRemoveHome(Player player) {
        if (!homeManager.hasHome(player)) {
            MessageUtil.send(player, "&cYou don't have a home set.");
            return true;
        }

        homeManager.removeHome(player);
        MessageUtil.send(player, "&aHome removed.");
        return true;
    }
}
