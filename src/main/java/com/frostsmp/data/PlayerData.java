package com.frostsmp.data;

import com.frostsmp.FrostStrengthSMP;
import com.frostsmp.ability.WeaponClass;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    private int rebirths;

    // Home locations (Donut SMP style named homes)
    private final Map<String, Location> homes = new HashMap<>();

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
            this.rebirths = 0;
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
        this.rebirths = cfg.getInt("rebirths", 0);
        this.lastAttackMillis = cfg.getLong("last-attack-millis", 0);
        this.locked = cfg.getBoolean("locked", false);

        // Named homes (Donut SMP style)
        String homePrefix = "homes.";
        for (String key : cfg.getKeys(false)) {
            if (key.startsWith("homes.")) {
                String homeName = key.substring("homes.".length());
                String path = "homes." + homeName;
                String worldName = cfg.getString(path + ".world");
                if (worldName != null) {
                    org.bukkit.World world = org.bukkit.Bukkit.getWorld(worldName);
                    if (world != null) {
                        double x = cfg.getDouble(path + ".x");
                        double y = cfg.getDouble(path + ".y");
                        double z = cfg.getDouble(path + ".z");
                        float yaw = (float) cfg.getDouble(path + ".yaw", 0.0);
                        float pitch = (float) cfg.getDouble(path + ".pitch", 0.0);
                        homes.put(homeName, new Location(world, x, y, z, yaw, pitch));
                    }
                }
            }
        }

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
            cfg.set("rebirths", rebirths);
            cfg.set("last-attack-millis", lastAttackMillis);
            cfg.set("locked", locked);

            // Named homes (Donut SMP style)
            for (Map.Entry<String, Location> entry : homes.entrySet()) {
                String path = "homes." + entry.getKey();
                Location loc = entry.getValue();
                cfg.set(path + ".world", loc.getWorld().getName());
                cfg.set(path + ".x", loc.getX());
                cfg.set(path + ".y", loc.getY());
                cfg.set(path + ".z", loc.getZ());
                cfg.set(path + ".yaw", loc.getYaw());
                cfg.set(path + ".pitch", loc.getPitch());
            }
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

    public int getRebirths() { return rebirths; }
    public void setRebirths(int rebirths) { this.rebirths = rebirths; }
    public void addRebirth() { this.rebirths++; }

    public Location getHomeLocation(String name) {
        return homes.get(name);
    }

    public void setHomeLocation(String name, Location location) {
        if (location == null) {
            homes.remove(name);
            return;
        }
        homes.put(name, location);
    }

    public boolean hasHome(String name) {
        return homes.containsKey(name);
    }

    public Map<String, Location> getAllHomes() {
        return new HashMap<>(homes);
    }

    public int getHomeCount() {
        return homes.size();
    }

    public void removeHome(String name) {
        homes.remove(name);
    }

    public void clearHomes() {
        homes.clear();
    }
}
