package com.frostsmp.data;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.ability.WeaponClass;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerData {

    private final FrostStrengthSMP plugin;
    private final UUID uuid;
    private final File dataFile;

    private int strength;
    private String role;
    private WeaponClass weaponClass;
    private boolean classAssigned;
    private int kills;
    private int deaths;
    private int lives;
    private int killstreak;
    private long lastAttackMillis;
    private boolean locked;

    public PlayerData(UUID uuid) {
        this.plugin = FrostStrengthSMP.getInstance();
        this.uuid = uuid;
        this.dataFile = new File(plugin.getDataFolder() + "/players", uuid + ".yml");
        load();
    }

    public void load() {
        if (!dataFile.exists()) {
            this.strength = plugin.getConfigManager().getStartingStrength();
            this.role = "MEMBER";
            this.weaponClass = WeaponClass.NONE;
            this.classAssigned = false;
            this.kills = 0;
            this.deaths = 0;
            this.lives = plugin.getConfigManager().getStartingLives();
            this.killstreak = 0;
            this.lastAttackMillis = 0;
            this.locked = false;
            save();
            return;
        }

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dataFile);
        this.strength = cfg.getInt("strength", plugin.getConfigManager().getStartingStrength());
        this.role = cfg.getString("role", "MEMBER");
        try {
            this.weaponClass = WeaponClass.valueOf(cfg.getString("weapon-class", "NONE"));
        } catch (IllegalArgumentException e) {
            this.weaponClass = WeaponClass.NONE;
        }
        this.classAssigned = cfg.getBoolean("class-assigned", false);
        this.kills = cfg.getInt("kills", 0);
        this.deaths = cfg.getInt("deaths", 0);
        this.lives = cfg.getInt("lives", plugin.getConfigManager().getStartingLives());
        this.killstreak = cfg.getInt("killstreak", 0);
        this.lastAttackMillis = cfg.getLong("last-attack-millis", 0);
        this.locked = cfg.getBoolean("locked", false);

        int min = plugin.getConfigManager().getMinStrength();
        int max = plugin.getConfigManager().getMaxStrength();
        if (strength < min) strength = min;
        if (strength > max) strength = max;
    }

    public void save() {
        try {
            if (dataFile.getParentFile() != null) {
                dataFile.getParentFile().mkdirs();
            }
            YamlConfiguration cfg = new YamlConfiguration();
            cfg.set("strength", strength);
            cfg.set("role", role);
            cfg.set("weapon-class", weaponClass.name());
            cfg.set("class-assigned", classAssigned);
            cfg.set("kills", kills);
            cfg.set("deaths", deaths);
            cfg.set("lives", lives);
            cfg.set("killstreak", killstreak);
            cfg.set("last-attack-millis", lastAttackMillis);
            cfg.set("locked", locked);
            cfg.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save data for " + uuid);
        }
    }

    public UUID getUuid() { return uuid; }

    public int getStrength() { return strength; }
    public void setStrength(int strength) {
        int min = plugin.getConfigManager().getMinStrength();
        int max = plugin.getConfigManager().getMaxStrength();
        this.strength = Math.max(min, Math.min(max, strength));
        if (this.strength <= min) {
            this.locked = true;
        }
    }
    public void modifyStrength(int amount) { setStrength(getStrength() + amount); }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public WeaponClass getWeaponClass() { return weaponClass; }
    public void setWeaponClass(WeaponClass weaponClass) { this.weaponClass = weaponClass; }

    public boolean isClassAssigned() { return classAssigned; }
    public void setClassAssigned(boolean classAssigned) { this.classAssigned = classAssigned; }

    public int getKills() { return kills; }
    public void addKill() { this.kills++; }

    public int getDeaths() { return deaths; }
    public void addDeath() { this.deaths++; }

    public int getLives() { return lives; }
    public void setLives(int lives) { this.lives = lives; }
    public void removeLife() { this.lives--; }

    public int getKillstreak() { return killstreak; }
    public void setKillstreak(int killstreak) { this.killstreak = killstreak; }
    public void addKillstreak() { this.killstreak++; }
    public void resetKillstreak() { this.killstreak = 0; }

    public long getLastAttackMillis() { return lastAttackMillis; }
    public void setLastAttackMillis(long millis) { this.lastAttackMillis = millis; }

    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
}
