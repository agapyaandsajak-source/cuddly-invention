package com.frostsmp.listener;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.ability.AbilityManager;
import com.frostsmp.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final FrostStrengthSMP plugin;

    public PlayerQuitListener(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerData data = plugin.getAllPlayerData().get(uuid);
        if (data != null) {
            data.save();
        }
        plugin.removePlayerData(uuid);
        AbilityManager abilityManager = plugin.getAbilityManager();
        if (abilityManager != null) {
            abilityManager.clearCooldown(player);
        }
        com.frostsmp.listener.ItemUseListener.removeCrown(player);
        plugin.getTpaManager().clearRequest(player);
    }
}
