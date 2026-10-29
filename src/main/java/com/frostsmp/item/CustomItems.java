package com.frostsmp.item;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.config.ConfigManager;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class CustomItems {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('§');

    private static final NamespacedKey KEY_STRENGTH_BOTTLE = new NamespacedKey(FrostStrengthSMP.getInstance(), "strength_bottle");
    private static final NamespacedKey KEY_CROWN = new NamespacedKey(FrostStrengthSMP.getInstance(), "the_crown");
    private static final NamespacedKey KEY_REVIVE_TOKEN = new NamespacedKey(FrostStrengthSMP.getInstance(), "revive_token");

    public static boolean isStrengthBottle(ItemStack item) {
        return hasTag(item, KEY_STRENGTH_BOTTLE);
    }

    public static boolean isCrown(ItemStack item) {
        return hasTag(item, KEY_CROWN);
    }

    public static boolean isReviveToken(ItemStack item) {
        return hasTag(item, KEY_REVIVE_TOKEN);
    }

    private static boolean hasTag(ItemStack item, NamespacedKey key) {
        if (item == null || item.getType() == Material.AIR) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    private static ItemStack applyDesign(ItemStack item, String name, List<String> lore,
                                         int customModelData, boolean glow, NamespacedKey tagKey) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.displayName(LEGACY.deserialize(name));

        List<net.kyori.adventure.text.Component> loreComponents = new ArrayList<>();
        for (String line : lore) {
            loreComponents.add(LEGACY.deserialize(line));
        }
        meta.lore(loreComponents);

        if (customModelData > 0) {
            meta.setCustomModelData(customModelData);
        }

        if (glow) {
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        meta.getPersistentDataContainer().set(tagKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack strengthBottle() {
        ConfigManager cfg = FrostStrengthSMP.getInstance().getConfigManager();
        ItemStack item = new ItemStack(Material.POTION, 1);
        List<String> lore = new ArrayList<>(cfg.getBottleItemLore());
        if (lore.isEmpty()) {
            lore.add("&7Drink to gain +1 Strength.");
        }
        String loreLine = lore.get(0).replace("{max}", String.valueOf(cfg.getMaxStrength()));
        lore.set(0, loreLine);
        return applyDesign(item, cfg.getBottleItemName(), lore,
                cfg.getBottleCustomModelData(), cfg.isBottleGlow(), KEY_STRENGTH_BOTTLE);
    }

    public static ItemStack crown() {
        ConfigManager cfg = FrostStrengthSMP.getInstance().getConfigManager();
        ItemStack item = new ItemStack(Material.GOLDEN_HELMET, 1);
        return applyDesign(item, cfg.getCrownItemName(), cfg.getCrownItemLore(),
                cfg.getCrownCustomModelData(), cfg.isCrownGlow(), KEY_CROWN);
    }

    public static ItemStack reviveToken() {
        ConfigManager cfg = FrostStrengthSMP.getInstance().getConfigManager();
        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        List<String> lore = new ArrayList<>(cfg.getReviveTokenLore());
        return applyDesign(item, cfg.getReviveTokenName(), lore,
                cfg.getReviveTokenCustomModelData(), cfg.isReviveTokenGlow(), KEY_REVIVE_TOKEN);
    }
}
