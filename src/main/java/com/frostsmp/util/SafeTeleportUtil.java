package com.frostsmp.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Random;

public class SafeTeleportUtil {

    private static final Random RANDOM = new Random();

    // Blocks considered unsafe to land on
    private static final org.bukkit.Material[] UNSAFE_LAND = {
            org.bukkit.Material.LAVA,
            org.bukkit.Material.WATER,
            org.bukkit.Material.CACTUS,
            org.bukkit.Material.FIRE,
            org.bukkit.Material.SOUL_SAND,
            org.bukkit.Material.MAGMA_BLOCK
    };

    // Blocks considered unsafe to be inside (suffocation)
    private static final org.bukkit.Material[] UNSAFE_INSIDE = {
            org.bukkit.Material.LAVA,
            org.bukkit.Material.WATER
    };

    public static boolean isUnsafeLand(org.bukkit.Material material) {
        for (org.bukkit.Material unsafe : UNSAFE_LAND) {
            if (material == unsafe) return true;
        }
        return false;
    }

    public static boolean isUnsafeInside(org.bukkit.Material material) {
        for (org.bukkit.Material unsafe : UNSAFE_INSIDE) {
            if (material == unsafe) return true;
        }
        return false;
    }

    /**
     * Finds a safe random teleport location within the given range.
     * Returns null if no safe spot found after maxAttempts.
     */
    public static Location findSafeRtpLocation(World world, int centerX, int centerZ, int range, int maxAttempts) {
        int minY = world.getMinHeight();
        int maxY = world.getMaxHeight();

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int x = centerX + RANDOM.nextInt(range * 2 + 1) - range;
            int z = centerZ + RANDOM.nextInt(range * 2 + 1) - range;

            // Find the highest solid block at this X,Z
            int y = maxY - 1;
            Block topBlock = null;
            Block blockBelow = null;

            while (y > minY) {
                Block current = world.getBlockAt(x, y, z);
                if (!current.isEmpty()) {
                    topBlock = current;
                    blockBelow = world.getBlockAt(x, y - 1, z);
                    break;
                }
                y--;
            }

            if (topBlock == null || blockBelow == null) continue;

            org.bukkit.Material landType = blockBelow.getType();
            org.bukkit.Material standType = topBlock.getType();
            org.bukkit.Material headType = world.getBlockAt(x, y + 1, z).getType();

            // Must land on solid ground, not unsafe
            if (isUnsafeLand(landType)) continue;
            if (!blockBelow.isEmpty() && !landType.isSolid()) continue;

            // Head space must be empty and not inside unsafe block
            if (!headType.isAir() && isUnsafeInside(headType)) continue;
            if (!standType.isAir() && isUnsafeInside(standType)) continue;

            // Need at least 2 blocks of headroom
            Block head2 = world.getBlockAt(x, y + 2, z);
            if (!head2.isEmpty() && isUnsafeInside(head2.getType())) continue;

            // Safe spot found
            Location loc = new Location(world, x + 0.5, y + 1, z + 0.5);
            return loc;
        }

        return null;
    }
}
