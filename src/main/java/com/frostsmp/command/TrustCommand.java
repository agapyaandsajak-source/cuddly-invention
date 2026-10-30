package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TrustCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;
    private final Map<UUID, Set<UUID>> trustedPlayers = new java.util.HashMap<>();

    public TrustCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfigManager().getNotPlayer());
            return true;
        }
        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&cUsage: /trust <add|remove|list> [player]");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "add":
                if (args.length < 2) {
                    player.sendMessage(plugin.getConfigManager().getPrefix() + "&cUsage: /trust add <player>");
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(plugin.getConfigManager().getPrefix() + "&cPlayer not found.");
                    return true;
                }
                if (target.equals(player)) {
                    player.sendMessage(plugin.getConfigManager().getPrefix() + "&cYou cannot trust yourself.");
                    return true;
                }
                addTrust(player.getUniqueId(), target.getUniqueId());
                String added = plugin.getConfigManager().getPrefix()
                        + "&aAdded &f" + target.getName() + "&a to your trusted players.";
                player.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(added));
                break;
            case "remove":
                if (args.length < 2) {
                    player.sendMessage(plugin.getConfigManager().getPrefix() + "&cUsage: /trust remove <player>");
                    return true;
                }
                Player target2 = plugin.getServer().getPlayer(args[1]);
                if (target2 == null) {
                    player.sendMessage(plugin.getConfigManager().getPrefix() + "&cPlayer not found.");
                    return true;
                }
                removeTrust(player.getUniqueId(), target2.getUniqueId());
                String removed = plugin.getConfigManager().getPrefix()
                        + "&aRemoved &f" + target2.getName() + "&a from your trusted players.";
                player.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(removed));
                break;
            case "list":
                Set<UUID> trusts = getTrusted(player.getUniqueId());
                if (trusts.isEmpty()) {
                    player.sendMessage(plugin.getConfigManager().getPrefix() + "&eYou have no trusted players.");
                } else {
                    String list = trusts.stream()
                            .map(uuid -> {
                                Player p = plugin.getServer().getPlayer(uuid);
                                return p != null ? p.getName() : "Unknown";
                            })
                            .collect(Collectors.joining("&f, &e"));
                    String listMsg = plugin.getConfigManager().getPrefix()
                            + "&eTrusted players: &e" + list;
                    player.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(listMsg));
                }
                break;
            default:
                player.sendMessage(plugin.getConfigManager().getPrefix() + "&cUsage: /trust <add|remove|list> [player]");
                break;
        }
        return true;
    }

    private void addTrust(UUID player, UUID target) {
        trustedPlayers.computeIfAbsent(player, k -> new HashSet<>()).add(target);
    }

    private void removeTrust(UUID player, UUID target) {
        Set<UUID> set = trustedPlayers.get(player);
        if (set != null) set.remove(target);
    }

    private Set<UUID> getTrusted(UUID player) {
        return trustedPlayers.getOrDefault(player, java.util.Collections.emptySet());
    }
}
