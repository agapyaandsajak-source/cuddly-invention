package com.frostsmp.listener;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.data.PlayerData;
import com.frostsmp.item.CustomItems;
import com.frostsmp.util.MessageUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathListener implements Listener {

    private final FrostStrengthSMP plugin;

    public PlayerDeathListener(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();

        if (plugin.getConfigManager().isLivesEnabled()
                && victim.getGameMode() != GameMode.CREATIVE) {
            PlayerData data = plugin.getPlayerData(victim);
            data.removeLife();
            int lives = data.getLives();
            data.save();

            String action = plugin.getConfigManager().getZeroLivesAction();
            if (lives <= 0) {
                if ("ban".equalsIgnoreCase(action)) {
                    victim.kickPlayer(plugin.getConfigManager().getPrefix()
                            + "&cYou ran out of lives and were banned.");
                    org.bukkit.Bukkit.getBanList(org.bukkit.BanList.Type.NAME)
                            .addBan(victim.getName(), "Ran out of lives (Frost SMP)", null, null);
                    return;
                } else if ("spectator".equalsIgnoreCase(action)) {
                    victim.setGameMode(GameMode.SPECTATOR);
                    MessageUtil.send(victim, "&cYou are out of lives! You are now a spectator.");
                    return;
                }
            } else {
                MessageUtil.send(victim, "&cYou lost a life! Lives remaining: &c" + lives);
            }

            if (plugin.getConfigManager().isDropBottleOnLivesDeath()) {
                dropBottle(victim.getLocation());
            }
        }

        if (plugin.getConfigManager().isStrengthLockEnabled()) {
            PlayerData data = plugin.getPlayerData(victim);
            if (data.getStrength() <= plugin.getConfigManager().getMinStrength()) {
                data.setLocked(true);
                data.save();

                if (plugin.getConfigManager().isNotifyAdminsOnLock()) {
                    String alert = plugin.getConfigManager().getAdminNotifyLock()
                            .replace("{player}", victim.getName());
                    plugin.getServer().getOnlinePlayers().forEach(p -> {
                        if (p.hasPermission("frostsmp.admin")) {
                            p.sendMessage(MessageUtil.component(alert));
                        }
                    });
                }

                String action = plugin.getConfigManager().getStrengthLockAction();
                if ("spectator".equalsIgnoreCase(action)) {
                    victim.setGameMode(GameMode.SPECTATOR);
                }
                MessageUtil.send(victim, plugin.getConfigManager().getStrengthLocked());
            }
        }

        if (victim.getKiller() == null
                && plugin.getConfigManager().isNaturalDeathCostsStrength()) {
            PlayerData data = plugin.getPlayerData(victim);
            int old = data.getStrength();
            data.modifyStrength(-1);
            int neu = data.getStrength();
            data.save();
            if (neu != old) {
                MessageUtil.send(victim, "&cYou lost 1 Strength from a natural death! "
                        + "(&c" + old + "&c \u2192 &c" + neu + "&c)");
            }
        }
    }

    private void dropBottle(Location loc) {
        ItemStack bottle = CustomItems.strengthBottle();
        loc.getWorld().dropItemNaturally(loc, bottle);
    }
}
