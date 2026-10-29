package com.frostsmp.ability;

public enum WeaponClass {
    NONE("None", "No class assigned yet.", "No class assigned yet."),

    SWORD("Sword", "Critical strikes deal extra damage.", "Whirlwind: strike all nearby enemies."),
    AXE("Axe", "Chance to disable shields briefly.", "Cleave: next strike is massive."),
    BOW("Bow", "Arrows fly faster and deal extra damage.", "Barrage: fire a volley of arrows."),
    CROSSBOW("Crossbow", "Shots pierce through multiple targets.", "Blast: fire an explosive bolt."),
    TRIDENT("Trident", "Throws deal bonus damage.", "Storm: call lightning on your target."),
    MACE("Mace", "Smash hits deal bonus damage.", "Seismic Smash: slam the ground."),
    SPEAR("Spear", "Increased reach and knockback.", "Lunge: dash forward and impale."),
    SHIELD("Shield", "Blocking reduces incoming damage.", "Shield Bash: stun and knock back."),
    ARMOR("Armor", "Damage reduction and absorption hearts.", "Iron Skin: temporary damage reduction.");

    private final String displayName;
    private final String passiveDesc;
    private final String ultimateDesc;

    WeaponClass(String displayName, String passiveDesc, String ultimateDesc) {
        this.displayName = displayName;
        this.passiveDesc = passiveDesc;
        this.ultimateDesc = ultimateDesc;
    }

    public String getDisplayName() { return displayName; }
    public String getPassiveDesc() { return passiveDesc; }
    public String getUltimateDesc() { return ultimateDesc; }
}
