package com.frostsmp.tutorial;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TutorialManager {

    private final FrostStrengthSMP plugin;
    private final Set<UUID> tutorialCompleted = new HashSet<>();

    public TutorialManager(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    public boolean isTutorialCompleted(Player player) {
        return tutorialCompleted.contains(player.getUniqueId());
    }

    public void markTutorialCompleted(Player player) {
        tutorialCompleted.add(player.getUniqueId());
    }

    public void showTutorialPrompt(Player player) {
        if (isTutorialCompleted(player)) return;

        LegacyComponentSerializer legacy = LegacyComponentSerializer.legacy('§');
        String prefix = plugin.getConfigManager().getPrefix();

        Component welcome = legacy.deserialize(prefix + "&eWelcome to &6&lFrosting SMP&e! &7Here is how the server works:");
        player.sendMessage(welcome);

        Component line1 = legacy.deserialize(prefix + "&7\u2022 &fKill players to gain +1 Strength. Die to lose -1 Strength.");
        player.sendMessage(line1);

        Component line2 = legacy.deserialize(prefix + "&7\u2022 &fStrength ranges from &c-5 &fto &c+5.&f At -5 you are locked until an admin pardons you.");
        player.sendMessage(line2);

        Component line3 = legacy.deserialize(prefix + "&7\u2022 &fUse &7/strength &fto check your Strength, class, and abilities.");
        player.sendMessage(line3);

        Component line4 = legacy.deserialize(prefix + "&7\u2022 &fUse &7/strength class &fto view your Weapon Class and &7/strength ability &fto use your Ultimate.");
        player.sendMessage(line4);

        Component line5 = legacy.deserialize(prefix + "&7\u2022 &fType &7/strength withdraw <n> &fto convert Strength into bottles.");
        player.sendMessage(line5);

        // Ask if they want to skip
        Component ask = legacy.deserialize(prefix + "&eDo you want to skip the tutorial?");

        Component yesBtn = legacy.deserialize("&a[YES]")
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial skip"))
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                        legacy.deserialize("&eClick to skip the tutorial")));

        Component noBtn = legacy.deserialize("&c[NO]")
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial start"))
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                        legacy.deserialize("&eClick to view the tutorial")));

        player.sendMessage(ask);
        player.sendMessage(yesBtn);
        player.sendMessage(noBtn);
    }

    public void skipTutorial(Player player) {
        markTutorialCompleted(player);
        MessageUtil.send(player, "&aTutorial skipped. Use &7/strength help &aif you need assistance later.");
    }

    public void startTutorial(Player player) {
        markTutorialCompleted(player);
        MessageUtil.send(player, "&eHere is a quick guide to Frosting SMP:");

        Component guide1 = legacyDeserialize("&7\u2022 &fKill other players to increase your Strength by +1 per kill.");
        player.sendMessage(guide1);

        Component guide2 = legacyDeserialize("&7\u2022 &fDying decreases your Strength by -1. Reaching -5 locks you out until an admin pardons you.");
        player.sendMessage(guide2);

        Component guide3 = legacyDeserialize("&7\u2022 &fUse &7/strength &fto check your current Strength and Weapon Class.");
        player.sendMessage(guide3);

        Component guide4 = legacyDeserialize("&7\u2022 &fUse &7/strength ability &fto activate your Weapon Class Ultimate.");
        player.sendMessage(guide4);

        Component guide5 = legacyDeserialize("&7\u2022 &fUse &7/strength withdraw <n> &fto turn Strength into bottles you can drink or trade.");
        player.sendMessage(guide5);

        Component guide6 = legacyDeserialize("&7\u2022 &fUse &7/role &fto view your role and &7/recipes &fto see crafting recipes.");
        player.sendMessage(guide6);

        Component guide7 = legacyDeserialize("&7\u2022 &fType &7/discord &fto join our Discord server!");
        player.sendMessage(guide7);

        MessageUtil.send(player, "&aTutorial complete! Good luck on Frosting SMP!");
    }

    private Component legacyDeserialize(String msg) {
        return LegacyComponentSerializer.legacy('§').deserialize(
                plugin.getConfigManager().getPrefix() + msg
        );
    }
}
