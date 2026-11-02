package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.manager.RankManager;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class RankCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;
    private final RankManager rankManager;
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    public RankCommand(FrostStrengthSMP plugin, RankManager rankManager) {
        this.plugin = plugin;
        this.rankManager = rankManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cPlayers only."));
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&6===== &lRanked SMP &r&6====="));
            player.sendMessage(LEGACY.deserialize("&7/rank &f- View your rank"));
            player.sendMessage(LEGACY.deserialize("&7/rank top &f- View top ranked players"));
            if (player.hasPermission("frostsmp.admin")) {
                player.sendMessage(LEGACY.deserialize("&7/rank set <player> <1-20> &f- Set a player's rank"));
                player.sendMessage(LEGACY.deserialize("&7/rank remove <player> &f- Remove a player's rank"));
                player.sendMessage(LEGACY.deserialize("&7/rank start &f- Assign random ranks to all online players"));
                player.sendMessage(LEGACY.deserialize("&7/rank manage &f- Open rank management GUI"));
            }
            player.sendMessage(LEGACY.deserialize("&7/abuse <player> <reason> &f- Report admin abuse to AGAPYA"));
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "top":
                return cmdTop(player);
            case "set":
                if (!player.hasPermission("frostsmp.admin")) {
                    player.sendMessage(plugin.getConfigManager().getNoPermission());
                    return true;
                }
                if (args.length < 3) {
                    player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cUsage: /rank set <player> <1-20>"));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cPlayer not found."));
                    return true;
                }
                int rank;
                try { rank = Integer.parseInt(args[2]); } catch (NumberFormatException e) {
                    player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cInvalid rank number."));
                    return true;
                }
                rankManager.setRank(target, rank);
                player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&aSet &f" + target.getName() + "&a's rank to #" + rank + "."));
                return true;
            case "remove":
                if (!player.hasPermission("frostsmp.admin")) {
                    player.sendMessage(plugin.getConfigManager().getNoPermission());
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cUsage: /rank remove <player>"));
                    return true;
                }
                Player rTarget = Bukkit.getPlayer(args[1]);
                if (rTarget == null) {
                    player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cPlayer not found."));
                    return true;
                }
                rankManager.removeRank(rTarget);
                player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&aRemoved &f" + rTarget.getName() + "&a's rank."));
                return true;
            case "start":
                if (!player.hasPermission("frostsmp.admin")) {
                    player.sendMessage(plugin.getConfigManager().getNoPermission());
                    return true;
                }
                rankManager.assignRandomRanks();
                player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&aRandom ranks assigned to all online players!"));
                return true;
            case "manage":
                if (!player.hasPermission("frostsmp.admin")) {
                    player.sendMessage(plugin.getConfigManager().getNoPermission());
                    return true;
                }
                openRankGui(player);
                return true;
            default:
                return cmdOwnRank(player);
        }
    }

    private boolean cmdOwnRank(Player player) {
        if (!rankManager.isRanked(player)) {
            player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&7You are unranked. Play and PvP to climb the ranks!"));
        } else {
            int rank = rankManager.getRank(player);
            player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix()
                    + "&eYour Rank: &f#" + rank + "&e of " + rankManager.MAX_RANK));
            player.sendMessage(LEGACY.deserialize("&7Max Hearts: &f" + rankManager.getMaxHearts(player) / 2.0
                    + " &7| Potion Duration: &fx" + String.format("%.2f", rankManager.getPotionDurationMultiplier(player))));
        }
        return true;
    }

    private boolean cmdTop(Player player) {
        player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&6&lTop Ranked Players:"));
        Bukkit.getOnlinePlayers().stream()
                .filter(rankManager::isRanked)
                .sorted((a, b) -> Integer.compare(rankManager.getRank(a), rankManager.getRank(b)))
                .limit(10)
                .forEach(p -> {
                    int rank = rankManager.getRank(p);
                    player.sendMessage(LEGACY.deserialize("&7#" + rank + " &f" + p.getName() + " &7(" + rankManager.getMaxHearts(p) / 2.0 + " hearts)"));
                });
        return true;
    }

    private void openRankGui(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "&6&lRank Management");
        Bukkit.getOnlinePlayers().stream()
                .filter(rankManager::isRanked)
                .sorted((a, b) -> Integer.compare(rankManager.getRank(a), rankManager.getRank(b)))
                .forEach(p -> {
                    int rank = rankManager.getRank(p);
                    ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
                    SkullMeta meta = (SkullMeta) item.getItemMeta();
                    if (meta != null) {
                        meta.setOwningPlayer(p);
                        meta.displayName(LEGACY.deserialize("&e#" + rank + " &f" + p.getName()));
                        meta.lore(List.of(
                                LEGACY.deserialize("&7Rank: &f#" + rank),
                                LEGACY.deserialize("&7Hearts: &f" + rankManager.getMaxHearts(p) / 2.0),
                                LEGACY.deserialize("&7Extra Slots: &f" + rankManager.getExtraInventorySlots(p)),
                                LEGACY.deserialize(""),
                                LEGACY.deserialize("&cClick to set/remove rank")
                        ));
                        item.setItemMeta(meta);
                    }
                    gui.addItem(item);
                });
        player.openInventory(gui);
    }
}
