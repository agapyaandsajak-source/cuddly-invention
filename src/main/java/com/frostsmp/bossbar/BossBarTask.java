package com.frostsmp.bossbar;

import com.frostsmp.FrostStrengthSMP;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BossBarTask extends BukkitRunnable {

    private final FrostStrengthSMP plugin;
    private final BossBar bossBar;

    public BossBarTask(FrostStrengthSMP plugin) {
        this.plugin = plugin;
        this.bossBar = Bukkit.createBossBar(
                plugin.getConfigManager().getPrefix() + "Frosting SMP",
                BarColor.BLUE,
                BarStyle.SOLID
        );
        bossBar.setProgress(1.0);
        bossBar.setVisible(true);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!bossBar.getPlayers().contains(player)) {
                bossBar.addPlayer(player);
            }
        }
        // Remove players who are no longer online
        bossBar.getPlayers().stream()
                .filter(p -> !p.isOnline())
                .forEach(bossBar::removePlayer);
    }

    public BossBar getBossBar() {
        return bossBar;
    }
}
