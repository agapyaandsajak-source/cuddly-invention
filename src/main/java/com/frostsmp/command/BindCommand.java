package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.manager.SoulBindManager;
import com.frostsmp.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BindCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;
    private final SoulBindManager soulBindManager;

    public BindCommand(FrostStrengthSMP plugin, SoulBindManager soulBindManager) {
        this.plugin = plugin;
        this.soulBindManager = soulBindManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfigManager().getNotPlayer());
            return true;
        }
        Player player = (Player) sender;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (soulBindManager.bindWeapon(player, item)) {
            player.getInventory().setItemInMainHand(item);
        }
        return true;
    }
}
