package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.item.CustomItems;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RecipesCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacy('&');

    public RecipesCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(LEGACY.deserialize(plugin.getConfigManager().getPrefix() + "&cPlayers only."));
            return true;
        }
        Player player = (Player) sender;

        // Open recipe GUI
        Inventory gui = Bukkit.createInventory(null, 54, LEGACY.deserialize("&d&l❄ Frost SMP Recipes"));

        // Strength Bottle
        gui.setItem(10, recipeItem(CustomItems.strengthBottle(),
                "&7Recipe: &f[Amethyst] [Amethyst] [Amethyst]",
                "&7         [Amethyst] [Honey] [Amethyst]",
                "&7         [Amethyst] [Amethyst] [Amethyst]"));

        // The Crown
        gui.setItem(11, recipeItem(CustomItems.crown(),
                "&7Recipe: &f[GoldBlock] [GoldBlock] [GoldBlock]",
                "&7         [GoldBlock] [NetherStar] [GoldBlock]",
                "&7         [GoldBlock] [GoldBlock] [GoldBlock]"));

        // Revive Token
        gui.setItem(12, recipeItem(CustomItems.reviveToken(),
                "&7Recipe: &f[DiamondBlock] [NetherStar] [DiamondBlock]",
                "&7         [DiamondBlock] [StrengthBottle] [DiamondBlock]",
                "&7         [DiamondBlock] [NetherStar] [DiamondBlock]"));

        // Strength Shard
        gui.setItem(13, recipeItem(CustomItems.strengthShard(),
                "&7Recipe: &f[Diamond] [Diamond] [Diamond]",
                "&7         [Diamond] [StrengthBottle] [Diamond]",
                "&7         [Diamond] [Diamond] [Diamond]"));

        // Reroll Grimoire
        gui.setItem(14, recipeItem(CustomItems.rerollGrimoire(),
                "&7Recipe: &f[Amethyst] [Amethyst] [Amethyst]",
                "&7         [Amethyst] [Book] [Amethyst]",
                "&7         [Amethyst] [Amethyst] [Amethyst]"));

        // Frost Sword
        gui.setItem(19, recipeItem(CustomItems.frostSword(),
                "&7Recipe: &fCustom sword obtained from &e/mobtoggle &7events or admin."));

        // Frost Axe
        gui.setItem(20, recipeItem(CustomItems.frostAxe(),
                "&7Recipe: &fCustom axe obtained from &e/mobtoggle &7events or admin."));

        // Frost Mace
        gui.setItem(21, recipeItem(CustomItems.frostMace(),
                "&7Recipe: &fCustom mace obtained from &e/mobtoggle &7events or admin."));

        // Frost Armor pieces
        gui.setItem(28, recipeItem(CustomItems.frostHelmet(),
                "&7Recipe: &fCustom armor from &e/mobtoggle &7events or admin."));
        gui.setItem(29, recipeItem(CustomItems.frostChestplate(),
                "&7Recipe: &fCustom armor from &e/mobtoggle &7events or admin."));
        gui.setItem(30, recipeItem(CustomItems.frostLeggings(),
                "&7Recipe: &fCustom armor from &e/mobtoggle &7events or admin."));
        gui.setItem(31, recipeItem(CustomItems.frostBoots(),
                "&7Recipe: &fCustom armor from &e/mobtoggle &7events or admin."));

        // Fill empty slots with stained glass
        for (int i = 0; i < 54; i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, glassPane(Material.BLUE_STAINED_GLASS_PANE, " "));
            }
        }

        player.openInventory(gui);
        return true;
    }

    private ItemStack recipeItem(ItemStack item, String... recipeLines) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        List<Component> loreComponents = new ArrayList<>();
        for (String line : recipeLines) {
            loreComponents.add(LEGACY.deserialize(line));
        }
        meta.lore(loreComponents);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack glassPane(Material material, String name) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(LEGACY.deserialize(name));
            item.setItemMeta(meta);
        }
        return item;
    }
}
