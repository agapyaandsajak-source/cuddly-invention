package com.frostsmp.util;

import com.frostsmp.FrostStrengthSMP;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class SpawnEffect {

    private static final Random RANDOM = new Random();

    public static void spawnKillEffect(FrostStrengthSMP plugin, Location loc) {
        if (!plugin.getConfigManager().isKillEffect()) return;
        World world = loc.getWorld();
        if (world == null) return;
        ParticleEffect.EXPLOSION.spawn(world, loc.getX(), loc.getY() + 1, loc.getZ(), 1, 0, 0, 0, 0);
        ParticleEffect.FLAME.spawn(world, loc.getX(), loc.getY() + 1, loc.getZ(), 15, 0.4, 0.4, 0.4, 0.15);
        spawnFirework(world, loc.clone().add(0, 1, 0), FireworkEffect.Type.BALL_LARGE);
        world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
    }

    public static void spawnLevelUpEffect(FrostStrengthSMP plugin, Player player) {
        if (!plugin.getConfigManager().isLevelupEffect()) return;
        Location loc = player.getLocation();
        World world = loc.getWorld();
        if (world == null) return;
        ParticleEffect.FIREWORK.spawn(world, loc.getX(), loc.getY() + 1.5, loc.getZ(), 30, 0.6, 0.6, 0.6, 0.25);
        spawnFirework(world, loc.clone().add(0, 1, 0), FireworkEffect.Type.BURST);
        world.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
    }

    public static void spawnReviveEffect(FrostStrengthSMP plugin, Player player) {
        if (!plugin.getConfigManager().isReviveEffect()) return;
        Location loc = player.getLocation();
        World world = loc.getWorld();
        if (world == null) return;
        ParticleEffect.HEART.spawn(world, loc.getX(), loc.getY() + 1.5, loc.getZ(), 20, 0.4, 0.6, 0.4, 0.1);
        spawnFirework(world, loc.clone().add(0, 1, 0), FireworkEffect.Type.STAR);
        world.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
        world.playSound(loc, Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0f, 1.0f);
    }

    public static void spawnSpawnEffect(FrostStrengthSMP plugin, Location loc) {
        World world = loc.getWorld();
        if (world == null) return;
        for (int i = 0; i < 12; i++) {
            double angle = (i / 12.0) * Math.PI * 2;
            double dx = Math.cos(angle) * 2.0;
            double dz = Math.sin(angle) * 2.0;
            ParticleEffect.END_ROD.spawn(world, loc.getX() + dx, loc.getY() + 1, loc.getZ() + dz, 1, 0, 0, 0, 0);
        }
        spawnFirework(world, loc.clone().add(0, 2, 0), FireworkEffect.Type.STAR);
        world.playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
    }

    public static void launchPlayer(Player player) {
        player.setVelocity(new Vector(0, 0.4, 0));
    }

    private static void spawnFirework(World world, Location loc, FireworkEffect.Type type) {
        Firework fw = (Firework) world.spawnEntity(loc, EntityType.FIREWORK_ROCKET);
        FireworkMeta meta = fw.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder()
                .flicker(RANDOM.nextBoolean())
                .trail(RANDOM.nextBoolean())
                .with(type)
                .withColor(org.bukkit.Color.fromRGB(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256)))
                .withFade(org.bukkit.Color.fromRGB(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256)))
                .build();
        meta.addEffect(effect);
        meta.setPower(1);
        fw.setFireworkMeta(meta);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!fw.isDead()) fw.remove();
            }
        }.runTaskLater(FrostStrengthSMP.getInstance(), 100L);
    }
}
