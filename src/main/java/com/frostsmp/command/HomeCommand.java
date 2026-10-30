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

import java.util.Map;

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
            // List all homes
            return cmdHomeList(player);
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "set":
            case "sethome":
                if (args.length < 2) {
                    return cmdSetHome(player, "home");
                }
                return cmdSetHome(player, args[1]);
            case "remove":
            case "delhome":
            case "removehome":
                if (args.length < 2) {
                    MessageUtil.send(player, "&cUsage: /home remove <name>");
                    return true;
                }
                return cmdRemoveHome(player, args[1]);
            case "list":
                return cmdHomeList(player);
            default:
                // Treat as home name to teleport to
                return cmdHome(player, sub);
        }
    }

    private boolean cmdHome(Player player, String name) {
        if (!homeManager.hasHome(player, name)) {
            MessageUtil.send(player, "&cYou don't have a home named &f" + name + "&c! Use &7/home list &cto see your homes.");
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

        Location home = homeManager.getHome(player, name);
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
        MessageUtil.send(player, "&aTeleported to home &f" + name + "&a.");
        return true;
    }

    private boolean cmdSetHome(Player player, String name) {
        if (plugin.getConfigManager().isStrengthLockEnabled() && plugin.getPlayerData(player).isLocked()) {
            MessageUtil.send(player, "&cYou cannot set home while locked at -5 Strength.");
            return true;
        }

        int maxHomes = homeManager.getMaxHomes(player);
        if (maxHomes > 0 && !homeManager.hasHome(player, name) && homeManager.getHomeCount(player) >= maxHomes) {
            MessageUtil.send(player, "&cYou have reached the maximum of &f" + maxHomes + "&c homes! Use &7/home remove <name> &cto remove one first.");
            return true;
        }

        homeManager.setHome(player, name, player.getLocation());
        MessageUtil.send(player, "&aHome &f" + name + "&a set at your current location.");
        return true;
    }

    private boolean cmdRemoveHome(Player player, String name) {
        if (!homeManager.hasHome(player, name)) {
            MessageUtil.send(player, "&cYou don't have a home named &f" + name + "&c.");
            return true;
        }

        homeManager.removeHome(player, name);
        MessageUtil.send(player, "&aHome &f" + name + "&a removed.");
        return true;
    }

    private boolean cmdHomeList(Player player) {
        Map<String, Location> homes = homeManager.getAllHomes(player);
        int maxHomes = homeManager.getMaxHomes(player);

        if (homes.isEmpty()) {
            MessageUtil.send(player, "&cYou don't have any homes set! Use &7/home set <name> &cto set one.");
            return true;
        }

        player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&eYour homes (&f" + homes.size() + "&e/&f" + maxHomes + "&e):"));
        for (String name : homes.keySet()) {
            player.sendMessage(LEGACY.deserialize("&7\u2022 &f" + name));
        }
        return true;
    }
}
