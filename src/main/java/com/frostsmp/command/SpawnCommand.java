package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import com.frostsmp.util.SpawnEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;

    public SpawnCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("&cPlayer-only command.");
            return true;
        }
        Player player = (Player) sender;
        if (plugin.getConfigManager().isSpawnEnabled()) {
            player.teleport(plugin.getSpawnLocation());
            SpawnEffect.spawnSpawnEffect(plugin, plugin.getSpawnLocation());
            MessageUtil.send(player, "&aTeleported to Frost SMP spawn!");
        } else {
            MessageUtil.send(player, "&cSpawn is disabled.");
        }
        return true;
    }
}
