package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.data.PlayerData;
import com.frostsmp.item.CustomItems;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class StrengthCommand implements CommandExecutor {

    // PLAIN removed
    private final FrostStrengthSMP plugin;

    public StrengthCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        String sub = args[0].toLowerCase();
        switch (sub) {
            case "get":
            case "strength":
                return cmdStrength(sender, args);
            case "set":
                return cmdSet(sender, args);
            case "class":
                return cmdClass(sender);
            case "ability":
                return cmdAbility(sender);
            case "withdraw":
                return cmdWithdraw(sender, args);
            case "revive":
                return cmdRevive(sender);
            case "nick":
                return cmdNick(sender, args);
            case "trail":
                return cmdTrail(sender);
            case "reload":
                return cmdReload(sender);
            case "help":
            default:
                sendHelp(sender);
                return true;
        }
    }

    private boolean cmdStrength(CommandSender sender, String[] args) {
        Player target;
        if (args.length >= 2) {
            if (!sender.hasPermission("frostsmp.admin")) {
                sender.sendMessage("&cYou need admin permission to check others.");
                return true;
            }
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("&cPlayer not found.");
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage("&cSpecify a player: /strength get <player>");
                return true;
            }
            target = (Player) sender;
        }
        PlayerData data = plugin.getPlayerData(target);
        Component msg = MessageUtil.component("&7" + target.getName()
                + "'s Strength: &c" + data.getStrength()
                + "&7/&c" + plugin.getConfigManager().getMaxStrength()
                + (data.isLocked() ? " &c[locked]" : ""));
        if (sender instanceof Player) ((Player) sender).sendMessage(msg);
        else sender.sendMessage(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacy('§').serialize(msg).replace("§", "&"));
        return true;
    }

    private boolean cmdSet(CommandSender sender, String[] args) {
        if (!sender.hasPermission("frostsmp.admin")) {
            sender.sendMessage("&cNo permission.");
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage("&cUsage: /strength set <player> <amount>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("&cPlayer not found.");
            return true;
        }
        int amount;
        try { amount = Integer.parseInt(args[2]); } catch (NumberFormatException e) {
            sender.sendMessage("&cInvalid number.");
            return true;
        }
        PlayerData data = plugin.getPlayerData(target);
        data.setStrength(amount);
        data.save();
        MessageUtil.send(target, "&eYour Strength was set to &c" + data.getStrength() + "&e by an admin.");
        sender.sendMessage("&aSet " + target.getName() + "'s Strength to " + data.getStrength());
        return true;
    }

    private boolean cmdClass(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("&cPlayer-only command.");
            return true;
        }
        Player player = (Player) sender;
        PlayerData data = plugin.getPlayerData(player);
        player.sendMessage(MessageUtil.component("&7Your Weapon Class: &e" + data.getWeaponClass().getDisplayName()));
        return true;
    }

    private boolean cmdAbility(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("&cPlayer-only command.");
            return true;
        }
        plugin.getAbilityManager().activateUltimate((Player) sender);
        return true;
    }

    private boolean cmdWithdraw(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("&cPlayer-only command.");
            return true;
        }
        Player player = (Player) sender;
        if (args.length < 2) {
            sender.sendMessage("&cUsage: /strength withdraw <amount>");
            return true;
        }
        int amount;
        try { amount = Integer.parseInt(args[1]); } catch (NumberFormatException e) {
            sender.sendMessage("&cInvalid number.");
            return true;
        }
        if (amount <= 0) {
            MessageUtil.send(player, "&cAmount must be positive.");
            return true;
        }
        PlayerData data = plugin.getPlayerData(player);
        if (data.getStrength() < amount) {
            MessageUtil.send(player, "&cYou don't have that much Strength.");
            return true;
        }
        int min = plugin.getConfigManager().getMinStrength();
        if (data.getStrength() - amount < min) {
            MessageUtil.send(player, "&cThat would drop you below minimum Strength.");
            return true;
        }
        data.modifyStrength(-amount);
        data.save();
        PlayerInventory inv = player.getInventory();
        ItemStack bottle = CustomItems.strengthBottle();
        bottle.setAmount(amount);
        if (inv.firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), bottle);
            MessageUtil.send(player, "&cInventory full! Bottle dropped at your feet.");
        } else {
            inv.addItem(bottle);
        }
        MessageUtil.send(player, "&aWithdrew &c" + amount + " &aStrength into bottle(s).");
        return true;
    }

    private boolean cmdRevive(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("&cPlayer-only command.");
            return true;
        }
        Player player = (Player) sender;
        PlayerData data = plugin.getPlayerData(player);
        int min = plugin.getConfigManager().getMinStrength();

        if (!data.isLocked() || data.getStrength() != min) {
            MessageUtil.send(player, plugin.getConfigManager().getReviveCannot());
            return true;
        }

        boolean hasToken = false;
        int tokenSlot = -1;
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null && CustomItems.isReviveToken(contents[i])) {
                hasToken = true;
                tokenSlot = i;
                break;
            }
        }

        if (!hasToken) {
            MessageUtil.send(player, "&cYou need a Revive Token to use this command.");
            return true;
        }

        ItemStack token = contents[tokenSlot];
        if (token.getAmount() > 1) token.setAmount(token.getAmount() - 1);
        else player.getInventory().setItem(tokenSlot, (ItemStack) null);

        data.setLocked(false);
        data.setStrength(plugin.getConfigManager().getReviveTokenRestoreStrength());
        data.save();
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);

        if (plugin.getConfigManager().isReviveEffect()) {
            com.frostsmp.util.SpawnEffect.spawnReviveEffect(plugin, player);
        }

        MessageUtil.send(player, plugin.getConfigManager().getReviveUsed()
                .replace("{level}", String.valueOf(data.getStrength())));
        return true;
    }

    private boolean cmdNick(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("&cPlayer-only command.");
            return true;
        }
        Player player = (Player) sender;
        if (args.length < 2) {
            sender.sendMessage("&cUsage: /strength nick <name>");
            return true;
        }
        String nick = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        player.displayName(MessageUtil.component("&f" + nick));
        player.sendMessage(MessageUtil.component("&aNickname set to &f" + nick + "&a."));
        return true;
    }

    private boolean cmdTrail(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("&cPlayer-only command.");
            return true;
        }
        Player player = (Player) sender;
        boolean enabled = plugin.getConfigManager().isTrailEnabled();
        player.sendMessage(MessageUtil.component("&7Particle trail: &e" + (enabled ? "enabled" : "disabled")));
        return true;
    }

    private boolean cmdReload(CommandSender sender) {
        if (!sender.hasPermission("frostsmp.admin")) {
            sender.sendMessage("&cNo permission.");
            return true;
        }
        plugin.getConfigManager().reload();
        sender.sendMessage("&aFrost SMP config reloaded.");
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("&6===== &lFrost &f&lSMP &r&6=====");
        sender.sendMessage("&7/strength &f- Check your Strength");
        sender.sendMessage("&7/strength class &f- View your Weapon Class");
        sender.sendMessage("&7/strength ability &f- Use your Ultimate");
        sender.sendMessage("&7/strength withdraw <n> &f- Convert Strength to bottles");
        sender.sendMessage("&7/strength nick <name> &f- Set nickname");
        if (sender.hasPermission("frostsmp.admin")) {
            sender.sendMessage("&7/strength set <p> <n> &f- Set a player's Strength");
            sender.sendMessage("&7/strength reload &f- Reload config");
        }
        sender.sendMessage("&7Version &f" + plugin.getDescription().getVersion());
    }
}
