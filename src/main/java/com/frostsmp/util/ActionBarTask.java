package com.frostsmp.util;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionBarTask extends BukkitRunnable {

    private final FrostStrengthSMP plugin;

    public ActionBarTask(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!plugin.getConfigManager().isActionBarEnabled()) {
            cancel();
            return;
        }
        String format = plugin.getConfigManager().getActionBarFormat();
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = plugin.getPlayerData(player);
            String text = format
                    .replace("{level}", String.valueOf(data.getStrength()))
                    .replace("{max}", String.valueOf(plugin.getConfigManager().getMaxStrength()));
            MessageUtil.actionBar(player, text);
        }
    }
}
