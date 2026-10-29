package com.frostsmp.command;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.tutorial.TutorialManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TutorialCommand implements CommandExecutor {

    private final FrostStrengthSMP plugin;
    private final TutorialManager tutorialManager;

    public TutorialCommand(FrostStrengthSMP plugin) {
        this.plugin = plugin;
        this.tutorialManager = plugin.getTutorialManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("&cThis command is for players only.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            if (tutorialManager.isTutorialCompleted(player)) {
                sender.sendMessage("&aYou have already completed the tutorial.");
            } else {
                tutorialManager.showTutorialPrompt(player);
            }
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "skip":
                tutorialManager.skipTutorial(player);
                return true;
            case "start":
                tutorialManager.startTutorial(player);
                return true;
            default:
                sender.sendMessage("&cUsage: /tutorial [skip|start]");
                return true;
        }
    }
}
