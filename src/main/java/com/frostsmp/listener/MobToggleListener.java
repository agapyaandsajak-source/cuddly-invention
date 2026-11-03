package com.frostsmp.listener;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.command.MobToggleCommand;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.UUID;

public class MobToggleListener implements Listener {

    private final FrostStrengthSMP plugin;
    private final MobToggleCommand mobToggleCommand;
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    public MobToggleListener(FrostStrengthSMP plugin, MobToggleCommand mobToggleCommand) {
        this.plugin = plugin;
        this.mobToggleCommand = mobToggleCommand;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Monster)) return;

        // Check if any player nearby has mob toggle off
        LivingEntity entity = event.getEntity();
        for (Player player : entity.getWorld().getPlayers()) {
            if (mobToggleCommand.isMobToggleOff(player)
                    && player.getLocation().distance(entity.getLocation()) <= 60) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Monster)) return;
        if (!(event.getTarget() instanceof Player)) return;

        Player target = (Player) event.getTarget();
        if (mobToggleCommand.isMobToggleOff(target)) {
            event.setCancelled(true);
        }
    }
}
