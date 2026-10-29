package com.frostsmp.api;

import org.bukkit.entity.Player;

public interface StrengthAPI {
    int getStrength(Player player);
    void setStrength(Player player, int amount);
    void modifyStrength(Player player, int amount);
}
