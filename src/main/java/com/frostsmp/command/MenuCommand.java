package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MenuCommand implements CommandExecutor, Listener {

    private final FrostStrengthSMP plugin;
    private final Map<UUID, Integer> playerPage = new HashMap<>();

    public MenuCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtil.component("&cPlayers only."));
            return true;
        }

        Player player = (Player) sender;
        playerPage.put(player.getUniqueId(), 0);
        openMenu(player, 0);
        return true;
    }

    private void openMenu(Player player, int page) {
        Inventory gui = Bukkit.createInventory(null, 54, MessageUtil.component("&b❄ Frost SMP Menu &8» &7Page " + (page + 1)));

        // Fill background with gray stained glass panes
        for (int i = 0; i < 54; i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, createItem(Material.GRAY_STAINED_GLASS_PANE, " "));
            }
        }

        // Tab items (top row)
        gui.setItem(0, createItem(Material.COMPASS, "&b&l» Tab: Stats", "&7View your strength & class"));
        gui.setItem(1, createItem(Material.BOOK, "&b&l» Tab: Classes", "&7View weapon classes"));
        gui.setItem(2, createItem(Material.GOLDEN_HELMET, "&b&l» Tab: Crown", "&7View crown info"));
        gui.setItem(3, createItem(Material.NETHER_STAR, "&b&l» Tab: Rebirth", "&7Rebirth information"));
        gui.setItem(4, createItem(Material.BARRIER, "&c&l» Close", "&7Close the menu"));

        // Navigation arrows
        if (page > 0) {
            gui.setItem(48, createItem(Material.ARROW, "&bPrevious Page", "&7Click to go back"));
        }
        gui.setItem(49, createItem(Material.RED_STAINED_GLASS_PANE, "&cClose Menu", "&7Click to close"));
        if (page < 1) {
            gui.setItem(50, createItem(Material.ARROW, "&bNext Page", "&7Click to continue"));
        }

        // Page 0 content: Stats
        if (page == 0) {
            var data = plugin.getPlayerData(player);
            gui.setItem(11, createItem(Material.PLAYER_HEAD, "&b&lStrength: &f" + data.getStrength(), "&7Level: &f" + data.getStrength() + "&7/&f" + plugin.getConfigManager().getMaxStrength()));
            gui.setItem(12, createItem(Material.DIAMOND_SWORD, "&b&lClass: &f" + data.getWeaponClass().getDisplayName(), "&7Passive at +2, Ultimate at +4"));
            gui.setItem(13, createItem(Material.CLOCK, "&b&lRebirths: &f" + data.getRebirths(), "&7Rebirth cost: &f" + ((data.getRebirths() + 1) * 10000)));
            gui.setItem(14, createItem(Material.TOTEM_OF_UNDYING, "&b&lRole: &f" + data.getRole(), "&7Cooldown reduction active"));
        }

        // Page 1 content: Shop / Info
        if (page == 1) {
            gui.setItem(11, createItem(Material.GOLDEN_APPLE, "&b&l2x Cash", "&7Gamepass: Doubles earnings"));
            gui.setItem(12, createItem(Material.CLOCK, "&b&lAuto-Click", "&7Automatic clicking QoL"));
            gui.setItem(13, createItem(Material.ENDER_PEARL, "&b&lSuper Luck", "&7+100% hatch rates"));
            gui.setItem(14, createItem(Material.NETHER_STAR, "&b&lVIP Status", "&7Cosmetics & passive boost"));
        }

        player.openInventory(gui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        String title = event.getView().getTitle();

        if (!title.contains("Frost SMP Menu")) return;

        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;

        int slot = event.getSlot();
        int currentPage = playerPage.getOrDefault(player.getUniqueId(), 0);

        if (slot == 0) {
            // Tab: Stats
            playerPage.put(player.getUniqueId(), 0);
            openMenu(player, 0);
        } else if (slot == 1) {
            // Tab: Classes
            player.sendMessage(MessageUtil.component("&bWeapon Classes: &7Sword, Axe, Bow, Crossbow, Trident, Mace, Spear, Shield, Armor"));
        } else if (slot == 2) {
            // Tab: Crown
            player.sendMessage(MessageUtil.component("&b&lThe Crown: &fMax Strength + Health Boost V &7(20 hearts)"));
        } else if (slot == 3) {
            // Tab: Rebirth
            player.sendMessage(MessageUtil.component("&bRebirth Cost: &f" + ((plugin.getPlayerData(player).getRebirths() + 1) * 10000) + " &7Cash"));
        } else if (slot == 4) {
            // Close
            player.closeInventory();
        } else if (slot == 48 && currentPage > 0) {
            playerPage.put(player.getUniqueId(), currentPage - 1);
            openMenu(player, currentPage - 1);
        } else if (slot == 49) {
            player.closeInventory();
        } else if (slot == 50 && currentPage < 1) {
            playerPage.put(player.getUniqueId(), currentPage + 1);
            openMenu(player, currentPage + 1);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        playerPage.remove(event.getPlayer().getUniqueId());
    }

    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.displayName(MessageUtil.component(name));
        List<net.kyori.adventure.text.Component> loreComponents = new ArrayList<>();
        for (String line : lore) {
            loreComponents.add(MessageUtil.component(line));
        }
        meta.lore(loreComponents);
        item.setItemMeta(meta);
        return item;
    }
}
