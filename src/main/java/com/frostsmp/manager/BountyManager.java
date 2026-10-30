package com.frostsmp.manager;

import com.frostsmp.FrostStrengthSMP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class BountyManager {

    private final FrostStrengthSMP plugin;
    private final Map<UUID, Integer> bounties = new HashMap<>(); // target -> amount
    private final Map<UUID, UUID> bountyPlacers = new HashMap<>(); // target -> placer

    public BountyManager(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    public boolean placeBounty(Player placer, Player target, int amount) {
        PlayerDataChecker checker = new PlayerDataChecker();
        if (!checker.hasEnough(placer, amount)) {
            placer.sendMessage(plugin.getConfigManager().getPrefix() + "&cYou don't have enough Strength!");
            return false;
        }
        if (target.equals(placer)) {
            placer.sendMessage(plugin.getConfigManager().getPrefix() + "&cYou cannot place a bounty on yourself!");
            return false;
        }

        PlayerDataChecker.deduct(placer, amount);
        bounties.put(target.getUniqueId(), amount);
        bountyPlacers.put(target.getUniqueId(), placer.getUniqueId());

        target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, false, false));

        String msg = plugin.getConfigManager().getPrefix()
                + "&aBounty of &e" + amount + "&a placed on &f" + target.getName() + "&a!";
        Bukkit.broadcastMessage(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacy('&').deserialize(msg));

        if (plugin.getConfigManager().isBountiesAnnouncement()) {
            String targetMsg = plugin.getConfigManager().getPrefix() + "&c&lBOUNTY: &fYou have a bounty on your head!";
            target.sendMessage(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacy('&').deserialize(targetMsg));
        }
        return true;
    }

    public boolean collectBounty(Player collector, Player target) {
        UUID targetId = target.getUniqueId();
        Integer amount = bounties.get(targetId);
        if (amount == null || amount <= 0) return false;

        bounties.remove(targetId);
        bountyPlacers.remove(targetId);
        target.removePotionEffect(PotionEffectType.GLOWING);

        PlayerDataChecker.add(collector, amount);

        String msg = plugin.getConfigManager().getPrefix()
                + "&aYou collected the bounty of &e" + amount + "&a on &f" + target.getName() + "&a!";
        collector.sendMessage(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacy('&').deserialize(msg));

        String targetMsg = plugin.getConfigManager().getPrefix() + "&eYour bounty was collected by &f" + collector.getName() + "&e!";
        target.sendMessage(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacy('&').deserialize(targetMsg));
        return true;
    }

    public boolean hasBounty(Player player) {
        return bounties.containsKey(player.getUniqueId());
    }

    public int getBountyAmount(Player player) {
        return bounties.getOrDefault(player.getUniqueId(), 0);
    }

    public void removeBounty(Player player) {
        bounties.remove(player.getUniqueId());
        bountyPlacers.remove(player.getUniqueId());
        player.removePotionEffect(PotionEffectType.GLOWING);
    }

    public UUID getBountyPlacer(Player player) {
        return bountyPlacers.get(player.getUniqueId());
    }

    private static class PlayerDataChecker {
        static boolean hasEnough(Player p, int amount) {
            com.frostsmp.data.PlayerData data = com.frostsmp.FrostStrengthSMP.getInstance().getPlayerData(p);
            return data.getStrength() >= amount;
        }
        static void deduct(Player p, int amount) {
            com.frostsmp.data.PlayerData data = com.frostsmp.FrostStrengthSMP.getInstance().getPlayerData(p);
            data.modifyStrength(-amount);
            data.save();
        }
        static void add(Player p, int amount) {
            com.frostsmp.data.PlayerData data = com.frostsmp.FrostStrengthSMP.getInstance().getPlayerData(p);
            data.modifyStrength(amount);
            data.save();
        }
    }
}
