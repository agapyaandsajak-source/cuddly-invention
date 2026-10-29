package com.frostsmp.util;

import com.frostsmp.FrostStrengthSMP;

public class RoleManager {

    public static String getRolePrefix(String role) {
        if (role == null) return "";
        String path = "roles.roles." + role + ".prefix";
        String prefix = FrostStrengthSMP.getInstance().getConfigManager().getRawConfig().getString(path);
        return prefix == null ? "" : prefix;
    }

    public static double getCooldownReduction(String role) {
        if (role == null) return 0.0;
        String path = "roles.roles." + role + ".cooldown-reduction";
        return FrostStrengthSMP.getInstance().getConfigManager().getRawConfig().getDouble(path, 0.0);
    }
}
