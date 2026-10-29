package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RecipesCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;

    public RecipesCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("&6===== &lFrost &f&lSMP Recipes &r&6=====");
        sender.sendMessage("&7Strength Bottle: &f8 Amethyst Shards + 1 Honey Bottle");
        sender.sendMessage("&7The Crown: &f8 Gold Blocks + 1 Nether Star");
        if (plugin.getConfigManager().isEzGapsRecipe()) {
            sender.sendMessage("&7Ez Golden Apple: &f8 Gold Ingots + 1 Apple");
        }
        if (plugin.getConfigManager().isEzShulkerRecipe()) {
            sender.sendMessage("&7Ez Shulker Box: &f1 Chest + 2 Shulker Shells");
        }
        if (plugin.getConfigManager().isEzWebsRecipe()) {
            sender.sendMessage("&7Ez Cobweb: &f4 String (2x2)");
        }
        sender.sendMessage("&7Use /ez for EZ items: gaps, anvil, shulker, webs");
        return true;
    }
}
