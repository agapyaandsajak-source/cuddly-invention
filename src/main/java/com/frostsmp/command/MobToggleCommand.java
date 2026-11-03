package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MobToggleCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;
    // Track which players have mobs toggled off
    private final Set<UUID> mobToggleOff = new HashSet<>();
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    public MobToggleCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cPlayers only."));
            return true;
        }
        Player player = (Player) sender;

        UUID uuid = player.getUniqueId();
        boolean currentlyOff = mobToggleOff.contains(uuid);

        if (currentlyOff) {
            // Turn mobs back on
            mobToggleOff.remove(uuid);
            player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix()
                    + "&e&l❄ Mob Toggle: &fHostile mobs &aENABLED&f around you."));
        } else {
            // Turn mobs off - remove hostile mobs in surrounding area
            mobToggleOff.add(uuid);
            int radius = 50; // blocks around player
            int removed = 0;
            World world = player.getWorld();
            for (Entity entity : world.getNearbyEntities(player.getLocation(), radius, radius, radius)) {
                if (entity instanceof Monster && !entity.equals(player)) {
                    entity.remove();
                    removed++;
                }
            }
            player.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix()
                    + "&e&l❄ Mob Toggle: &fHostile mobs &cDISABLED&f around you (&c" + removed + "&f cleared)."));
        }
        return true;
    }

    public boolean isMobToggleOff(Player player) {
        return mobToggleOff.contains(player.getUniqueId());
    }

    public void clearMobToggle(Player player) {
        mobToggleOff.remove(player.getUniqueId());
    }
}
