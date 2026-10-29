package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DiscordCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;

    public DiscordCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String url = plugin.getConfigManager().getDiscord();
        Component text = MessageUtil.component(plugin.getConfigManager().getDiscord())
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, url))
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                        MessageUtil.component(plugin.getConfigManager().getDiscordHover())));

        if (sender instanceof org.bukkit.entity.Player) {
            ((org.bukkit.entity.Player) sender).sendMessage(text);
        } else {
            sender.sendMessage(LegacyComponentSerializer.legacy('§').serialize(text).replace("§", "&"));
        }
        return true;
    }
}
