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

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    private static final NamespacedKey KEY_STRENGTH_BOTTLE = new NamespacedKey(FrostStrengthSMP.getInstance(), "strength_bottle");
    private static final NamespacedKey KEY_CROWN = new NamespacedKey(FrostStrengthSMP.getInstance(), "the_crown");
    private static final NamespacedKey KEY_REVIVE_TOKEN = new NamespacedKey(FrostStrengthSMP.getInstance(), "revive_token");
    private static final NamespacedKey KEY_STRENGTH_SHARD = new NamespacedKey(FrostStrengthSMP.getInstance(), "strength_shard");
    private static final NamespacedKey KEY_REROLL_GRIMOIRE = new NamespacedKey(FrostStrengthSMP.getInstance(), "reroll_grimoire");
    private static final NamespacedKey KEY_DEATH_CERTIFICATE = new NamespacedKey(FrostStrengthSMP.getInstance(), "death_certificate");
    private static final NamespacedKey KEY_SOUL_BOUND = new NamespacedKey(FrostStrengthSMP.getInstance(), "soul_bound");
    private static final NamespacedKey KEY_FROST_SWORD = new NamespacedKey(FrostStrengthSMP.getInstance(), "frost_sword");
    private static final NamespacedKey KEY_FROST_AXE = new NamespacedKey(FrostStrengthSMP.getInstance(), "frost_axe");
    private static final NamespacedKey KEY_FROST_MACE = new NamespacedKey(FrostStrengthSMP.getInstance(), "frost_mace");
    private static final NamespacedKey KEY_FROST_HELMET = new NamespacedKey(FrostStrengthSMP.getInstance(), "frost_helmet");
    private static final NamespacedKey KEY_FROST_CHESTPLATE = new NamespacedKey(FrostStrengthSMP.getInstance(), "frost_chestplate");
    private static final NamespacedKey KEY_FROST_LEGGINGS = new NamespacedKey(FrostStrengthSMP.getInstance(), "frost_leggings");
    private static final NamespacedKey KEY_FROST_BOOTS = new NamespacedKey(FrostStrengthSMP.getInstance(), "frost_boots");

    public static boolean isStrengthBottle(ItemStack item) {
        return hasTag(item, KEY_STRENGTH_BOTTLE);
    }

    public static boolean isCrown(ItemStack item) {
        return hasTag(item, KEY_CROWN);
    }

    public static boolean isReviveToken(ItemStack item) {
        return hasTag(item, KEY_REVIVE_TOKEN);
    }

    public static boolean isStrengthShard(ItemStack item) {
        return hasTag(item, KEY_STRENGTH_SHARD);
    }

    public static boolean isRerollGrimoire(ItemStack item) {
        return hasTag(item, KEY_REROLL_GRIMOIRE);
    }

    public static boolean isDeathCertificate(ItemStack item) {
        return hasTag(item, KEY_DEATH_CERTIFICATE);
    }

    public static boolean isSoulBound(ItemStack item) {
        return hasTag(item, KEY_SOUL_BOUND);
    }

    public static boolean isFrostSword(ItemStack item) {
        return hasTag(item, KEY_FROST_SWORD);
    }

    public static boolean isFrostAxe(ItemStack item) {
        return hasTag(item, KEY_FROST_AXE);
    }

    public static boolean isFrostMace(ItemStack item) {
        return hasTag(item, KEY_FROST_MACE);
    }

    public static boolean isFrostHelmet(ItemStack item) {
        return hasTag(item, KEY_FROST_HELMET);
    }

    public static boolean isFrostChestplate(ItemStack item) {
        return hasTag(item, KEY_FROST_CHESTPLATE);
    }

    public static boolean isFrostLeggings(ItemStack item) {
        return hasTag(item, KEY_FROST_LEGGINGS);
    }

    public static boolean isFrostBoots(ItemStack item) {
        return hasTag(item, KEY_FROST_BOOTS);
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

    public static ItemStack strengthShard() {
        ConfigManager cfg = FrostStrengthSMP.getInstance().getConfigManager();
        ItemStack item = new ItemStack(Material.DIAMOND, 1);
        List<String> lore = new ArrayList<>(cfg.getStrengthShardLore());
        return applyDesign(item, cfg.getStrengthShardName(), lore,
                cfg.getStrengthShardCustomModelData(), cfg.isStrengthShardGlow(), KEY_STRENGTH_SHARD);
    }

    public static ItemStack rerollGrimoire() {
        ConfigManager cfg = FrostStrengthSMP.getInstance().getConfigManager();
        ItemStack item = new ItemStack(Material.BOOK, 1);
        List<String> lore = new ArrayList<>(cfg.getRerollGrimoireLore());
        return applyDesign(item, cfg.getRerollGrimoireName(), lore,
                cfg.getRerollGrimoireCustomModelData(), cfg.isRerollGrimoireGlow(), KEY_REROLL_GRIMOIRE);
    }

    public static ItemStack deathCertificate(String killer, String victim) {
        ConfigManager cfg = FrostStrengthSMP.getInstance().getConfigManager();
        ItemStack item = new ItemStack(Material.PAPER, 1);
        List<String> lore = new ArrayList<>(cfg.getDeathCertificateLore());
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i)
                    .replace("{killer}", killer)
                    .replace("{victim}", victim));
        }
        return applyDesign(item, cfg.getDeathCertificateName(), lore,
                cfg.getDeathCertificateCustomModelData(), cfg.isDeathCertificateGlow(), KEY_DEATH_CERTIFICATE);
    }

    public static ItemStack makeSoulBound(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        meta.getPersistentDataContainer().set(KEY_SOUL_BOUND, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    // ============ Custom Weapons ============

    public static ItemStack frostSword() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1);
        return applyDesign(item, "&d&lFrost Sword",
                java.util.List.of("&7A blade forged from eternal ice.", "&7Deals bonus damage based on Strength."),
                201, true, KEY_FROST_SWORD);
    }

    public static ItemStack frostAxe() {
        ItemStack item = new ItemStack(Material.DIAMOND_AXE, 1);
        return applyDesign(item, "&d&lFrost Axe",
                java.util.List.of("&7An axe chilled to the core.", "&7Chance to disable shields on hit."),
                202, true, KEY_FROST_AXE);
    }

    public static ItemStack frostMace() {
        ItemStack item = new ItemStack(Material.MACE, 1);
        return applyDesign(item, "&d&lFrost Mace",
                java.util.List.of("&7A heavy mace of frozen void.", "&7Smash strikes with seismic force."),
                203, true, KEY_FROST_MACE);
    }

    // ============ Custom Armor ============

    public static ItemStack frostHelmet() {
        ItemStack item = new ItemStack(Material.DIAMOND_HELMET, 1);
        return applyDesign(item, "&d&lFrost Helmet",
                java.util.List.of("&7Helm of the Frost SMP.", "&7Grants resistance while worn."),
                211, true, KEY_FROST_HELMET);
    }

    public static ItemStack frostChestplate() {
        ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        return applyDesign(item, "&d&lFrost Chestplate",
                java.util.List.of("&7Chestplate of the Frost SMP.", "&7Reduces incoming damage."),
                212, true, KEY_FROST_CHESTPLATE);
    }

    public static ItemStack frostLeggings() {
        ItemStack item = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        return applyDesign(item, "&d&lFrost Leggings",
                java.util.List.of("&7Leggings of the Frost SMP.", "&7Increases movement speed."),
                213, true, KEY_FROST_LEGGINGS);
    }

    public static ItemStack frostBoots() {
        ItemStack item = new ItemStack(Material.DIAMOND_BOOTS, 1);
        return applyDesign(item, "&d&lFrost Boots",
                java.util.List.of("&7Boots of the Frost SMP.", "&7Prevents fall damage."),
                214, true, KEY_FROST_BOOTS);
    }
}
