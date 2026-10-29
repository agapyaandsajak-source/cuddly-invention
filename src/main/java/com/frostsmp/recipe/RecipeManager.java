package com.frostsmp.recipe;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.item.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class RecipeManager {

    private final FrostStrengthSMP plugin;

    public RecipeManager(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    public void registerRecipes() {
        NamespacedKey[] keys = {
                new NamespacedKey(plugin, "frost_strength_bottle"),
                new NamespacedKey(plugin, "frost_crown"),
                new NamespacedKey(plugin, "frost_revive_token"),
                new NamespacedKey(plugin, "frost_ez_gapple"),
                new NamespacedKey(plugin, "frost_ez_shulker"),
                new NamespacedKey(plugin, "frost_ez_web"),
        };
        for (NamespacedKey key : keys) {
            Recipe existing = Bukkit.getRecipe(key);
            if (existing != null) {
                Bukkit.removeRecipe(key);
            }
        }

        ShapedRecipe bottle = new ShapedRecipe(new NamespacedKey(plugin, "frost_strength_bottle"), CustomItems.strengthBottle());
        bottle.shape("GGG", "GBG", "GGG");
        bottle.setIngredient('G', Material.AMETHYST_SHARD);
        bottle.setIngredient('B', Material.HONEY_BOTTLE);
        Bukkit.addRecipe(bottle);

        if (plugin.getConfigManager().isCrownEnabled()) {
            ShapedRecipe crown = new ShapedRecipe(new NamespacedKey(plugin, "frost_crown"), CustomItems.crown());
            crown.shape("GGG", "GNG", "GGG");
            crown.setIngredient('G', Material.GOLD_BLOCK);
            crown.setIngredient('N', Material.NETHER_STAR);
            Bukkit.addRecipe(crown);
        }

        if (plugin.getConfigManager().isReviveTokenEnabled()) {
            ShapedRecipe token = new ShapedRecipe(new NamespacedKey(plugin, "frost_revive_token"), CustomItems.reviveToken());
            token.shape("DDD", "NSN", "DDD");
            token.setIngredient('D', Material.DIAMOND_BLOCK);
            token.setIngredient('N', Material.NETHER_STAR);
            token.setIngredient('S', Material.POTION);
            Bukkit.addRecipe(token);
        }

        if (plugin.getConfigManager().isEzGapsRecipe()) {
            ShapelessRecipe gapple = new ShapelessRecipe(new NamespacedKey(plugin, "frost_ez_gapple"), new ItemStack(Material.GOLDEN_APPLE));
            gapple.addIngredient(8, Material.GOLD_INGOT);
            gapple.addIngredient(1, Material.APPLE);
            Bukkit.addRecipe(gapple);
        }

        if (plugin.getConfigManager().isEzShulkerRecipe()) {
            ShapelessRecipe shulker = new ShapelessRecipe(new NamespacedKey(plugin, "frost_ez_shulker"), new ItemStack(Material.SHULKER_BOX));
            shulker.addIngredient(1, Material.CHEST);
            shulker.addIngredient(2, Material.SHULKER_SHELL);
            Bukkit.addRecipe(shulker);
        }

        if (plugin.getConfigManager().isEzWebsRecipe()) {
            ShapedRecipe web = new ShapedRecipe(new NamespacedKey(plugin, "frost_ez_web"), new ItemStack(Material.COBWEB));
            web.shape("SS", "SS");
            web.setIngredient('S', Material.STRING);
            Bukkit.addRecipe(web);
        }
    }
}
