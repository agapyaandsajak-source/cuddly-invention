package com.frostsmp.listener;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.data.PlayerData;
import com.frostsmp.item.CustomItems;
import com.frostsmp.util.MessageUtil;
import com.frostsmp.util.SpawnEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerKillListener implements Listener {

    private final FrostStrengthSMP plugin;

    public PlayerKillListener(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer == null || killer.equals(victim)) return;

        PlayerData vData = plugin.getPlayerData(victim);
        if (plugin.getConfigManager().isStrengthLockEnabled() && vData.isLocked()) {
            event.setCancelled(true);
            return;
        }

        if (!plugin.getConfigManager().isGainOnKill()
                && !plugin.getConfigManager().isLossOnDeath()) return;

        PlayerData kd = plugin.getPlayerData(killer);

        int oldKiller = kd.getStrength();
        int oldVictim = vData.getStrength();

        if (plugin.getConfigManager().isGainOnKill()) {
            kd.addKill();
            kd.addKillstreak();
            int gain = plugin.getConfigManager().getGainAmount();
            kd.modifyStrength(gain);
            int newKiller = kd.getStrength();
            kd.save();

            if (newKiller != oldKiller) {
                MessageUtil.send(killer, "&a+1 Strength from a kill! &7(&c" + oldKiller + "&a \u2192 &c" + newKiller + "&7)");
                SpawnEffect.spawnKillEffect(plugin, killer.getLocation());
            }
            if (newKiller >= plugin.getConfigManager().getMaxStrength()
                    && plugin.getConfigManager().isDropBottleAtMax()) {
                dropBottle(victim.getLocation());
            }

            handleKillstreak(killer, kd);
        }

        if (plugin.getConfigManager().isLossOnDeath()) {
            vData.addDeath();
            vData.resetKillstreak();
            int loss = plugin.getConfigManager().getLossAmount();
            vData.modifyStrength(-loss);
            int newVictim = vData.getStrength();
            vData.save();

            if (newVictim != oldVictim) {
                MessageUtil.send(victim, "&c-1 Strength from death! &7(&c" + oldVictim + "&c \u2192 &c" + newVictim + "&c)");
                if (newVictim <= plugin.getConfigManager().getMinStrength()) {
                    MessageUtil.send(victim, "&cYou are at minimum Strength (-5) and are now locked!");
                }
            }
        }
    }

    private void handleKillstreak(Player killer, PlayerData kd) {
        if (!plugin.getConfigManager().isKillstreaksEnabled()) return;
        int streak = kd.getKillstreak();
        if (streak % plugin.getConfigManager().getRewardBottleEvery() == 0) {
            ItemStack bottle = CustomItems.strengthBottle();
            if (killer.getInventory().firstEmpty() != -1) {
                killer.getInventory().addItem(bottle);
            } else {
                killer.getWorld().dropItemNaturally(killer.getLocation(), bottle);
            }
            MessageUtil.send(killer, "&eKillstreak reward: +1 Strength Bottle!");
        }
        if (plugin.getConfigManager().isKillstreaksAnnounce()) {
            switch (streak) {
                case 3:
                case 5:
                case 10:
                    String streakMsg = plugin.getConfigManager().getPrefix()
                            + "&e" + killer.getName() + " is on a &c" + streak + "&e killstreak!";
                    killer.getWorld().getPlayers().forEach(p -> p.sendMessage(MessageUtil.component(streakMsg)));
                    SpawnEffect.spawnLevelUpEffect(plugin, killer);
                    break;
                default:
                    if (streak > 10 && streak % 5 == 0) {
                        String godMsg = plugin.getConfigManager().getPrefix()
                                + "&c&lGODLIKE: &f" + killer.getName() + " &cis on a &c" + streak + "+&c killstreak!";
                        killer.getWorld().getPlayers().forEach(p -> p.sendMessage(MessageUtil.component(godMsg)));
                    }
                    break;
            }
        }
    }

    private void dropBottle(Location loc) {
        ItemStack bottle = CustomItems.strengthBottle();
        loc.getWorld().dropItemNaturally(loc, bottle);
    }
}
