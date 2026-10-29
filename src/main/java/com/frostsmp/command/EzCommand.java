package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.item.CustomItems;
import com.frostsmp.util.MessageUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EzCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;

    public EzCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("&cPlayer-only command.");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            sender.sendMessage("&6===== &lEZ Items &r&6=====");
            sender.sendMessage("&7/ez gaps &f- Get Ez Golden Apples");
            sender.sendMessage("&7/ez anvil &f- Free anvil repairs");
            sender.sendMessage("&7/ez shulker &f- Get Ez Shulker Boxes");
            sender.sendMessage("&7/ez webs &f- Get Ez Cobwebs");
            return true;
        }
        String sub = args[0].toLowerCase();
        switch (sub) {
            case "gaps":
                if (!plugin.getConfigManager().isEzGapsEnabled()) {
                    MessageUtil.send(player, "&cEz Gaps are disabled.");
                    return true;
                }
                ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 16);
                if (player.getInventory().firstEmpty() == -1) {
                    player.getWorld().dropItemNaturally(player.getLocation(), gapple);
                } else {
                    player.getInventory().addItem(gapple);
                }
                MessageUtil.send(player, plugin.getConfigManager().getEzgapsGiven());
                break;
            case "anvil":
                if (!plugin.getConfigManager().isEzAnvilEnabled()) {
                    MessageUtil.send(player, "&cEz Anvil is disabled.");
                    return true;
                }
                MessageUtil.send(player, plugin.getConfigManager().getEzanvil());
                break;
            case "shulker":
                if (!plugin.getConfigManager().isEzShulkerEnabled()) {
                    MessageUtil.send(player, "&cEz Shulker is disabled.");
                    return true;
                }
                ItemStack shulker = new ItemStack(Material.SHULKER_BOX, 4);
                if (player.getInventory().firstEmpty() == -1) {
                    player.getWorld().dropItemNaturally(player.getLocation(), shulker);
                } else {
                    player.getInventory().addItem(shulker);
                }
                MessageUtil.send(player, plugin.getConfigManager().getEzshulker());
                break;
            case "webs":
                if (!plugin.getConfigManager().isEzWebsEnabled()) {
                    MessageUtil.send(player, "&cEz Webs are disabled.");
                    return true;
                }
                ItemStack web = new ItemStack(Material.COBWEB, 32);
                if (player.getInventory().firstEmpty() == -1) {
                    player.getWorld().dropItemNaturally(player.getLocation(), web);
                } else {
                    player.getInventory().addItem(web);
                }
                MessageUtil.send(player, plugin.getConfigManager().getEzwebs());
                break;
            default:
                sender.sendMessage("&cUsage: /ez <gaps|anvil|shulker|webs>");
                break;
        }
        return true;
    }
}
