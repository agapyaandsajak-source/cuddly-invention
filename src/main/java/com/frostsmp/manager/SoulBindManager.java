package com.frostsmp.manager;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.item.CustomItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoulBindManager implements Listener {

    private final FrostStrengthSMP plugin;
    // UUID -> bound weapon material name (for loyalty return tracking)
    private final Map<UUID, String> soulBoundWeapons = new HashMap<>();

    public SoulBindManager(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    public boolean bindWeapon(Player player, ItemStack item) {
        if (item == null || item.getType() == org.bukkit.Material.AIR) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&cYou must be holding a weapon!");
            return false;
        }
        if (CustomItems.isSoulBound(item)) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&cThat item is already soul-bound!");
            return false;
        }
        com.frostsmp.data.PlayerData data = plugin.getPlayerData(player);
        int cost = plugin.getConfigManager().getSoulBindCost();
        if (data.getStrength() < cost) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&cNot enough Strength to bind (cost: &f" + cost + "&c).");
            return false;
        }

        data.modifyStrength(-cost);
        data.save();
        CustomItems.makeSoulBound(item);
        soulBoundWeapons.put(player.getUniqueId(), item.getType().name());

        player.sendMessage(plugin.getConfigManager().getPrefix()
                + "&aYour &f" + item.getType().name() + "&a is now soul-bound!");
        return true;
    }

    public boolean isSoulBound(ItemStack item) {
        return CustomItems.isSoulBound(item);
    }

    public boolean hasSoulBoundWeapon(Player player) {
        return soulBoundWeapons.containsKey(player.getUniqueId());
    }

    public void onPlayerDeath(Player player, ItemStack weaponInHand) {
        if (weaponInHand != null && CustomItems.isSoulBound(weaponInHand)) {
            // Weapon is kept - mark for loyalty return on respawn
            soulBoundWeapons.put(player.getUniqueId(), weaponInHand.getType().name());
        }
    }

    public void onRespawn(Player player) {
        // Loyalty return: if they had a soul-bound weapon, it stays in inventory
        // (it was never removed because we cancel the drop)
        if (hasSoulBoundWeapon(player)) {
            player.sendMessage(plugin.getConfigManager().getPrefix()
                    + "&eYour soul-bound weapon stays with you!");
        }
    }

    public boolean isUndroppable(ItemStack item) {
        return CustomItems.isSoulBound(item)
                && plugin.getConfigManager().isSoulBindUndroppable();
    }
}
