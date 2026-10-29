package com.frostsmp;

import com.frostsmp.ability.AbilityManager;
import com.frostsmp.api.StrengthAPI;
import com.frostsmp.command.*;
import com.frostsmp.config.ConfigManager;
import com.frostsmp.data.PlayerData;
import com.frostsmp.listener.*;
import com.frostsmp.recipe.RecipeManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class FrostStrengthSMP extends JavaPlugin implements StrengthAPI {

    private static FrostStrengthSMP instance;
    private ConfigManager configManager;
    private AbilityManager abilityManager;
    private RecipeManager recipeManager;
    private final Map<UUID, PlayerData> playerData = new HashMap<>();
    private Location spawnLocation;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        configManager.load();

        abilityManager = new AbilityManager(this);
        recipeManager = new RecipeManager(this);
        recipeManager.registerRecipes();

        loadSpawn();

        getCommand("strength").setExecutor(new StrengthCommand(this));
        getCommand("strengthadmin").setExecutor(new StrengthAdminCommand(this));
        getCommand("recipes").setExecutor(new RecipesCommand(this));
        getCommand("discord").setExecutor(new DiscordCommand(this));
        getCommand("role").setExecutor(new RoleCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("ez").setExecutor(new EzCommand(this));

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerKillListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ItemUseListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EnchantListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MovementListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(this), this);

        if (configManager.isActionBarEnabled()) {
            new ActionBarTask(this).runTaskTimer(this, 0L, configManager.getActionBarInterval() * 20L);
        }

        getLogger().log(Level.INFO, "Frost Strength SMP has been enabled!");
    }

    @Override
    public void onDisable() {
        for (PlayerData data : playerData.values()) {
            data.save();
        }
        playerData.clear();
        getLogger().log(Level.INFO, "Frost Strength SMP has been disabled!");
    }

    public static FrostStrengthSMP getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public AbilityManager getAbilityManager() {
        return abilityManager;
    }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerData.computeIfAbsent(uuid, PlayerData::new);
    }

    public PlayerData getPlayerData(Player player) {
        return getPlayerData(player.getUniqueId());
    }

    public void removePlayerData(UUID uuid) {
        PlayerData data = playerData.remove(uuid);
        if (data != null) {
            data.save();
        }
    }

    public Map<UUID, PlayerData> getAllPlayerData() {
        return playerData;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    private void loadSpawn() {
        String worldName = configManager.getSpawnWorld();
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            world = Bukkit.getWorlds().get(0);
        }
        spawnLocation = new Location(
                world,
                configManager.getSpawnX(),
                configManager.getSpawnY(),
                configManager.getSpawnZ(),
                (float) configManager.getSpawnYaw(),
                (float) configManager.getSpawnPitch()
        );
    }

    @Override
    public int getStrength(Player player) {
        return getPlayerData(player).getStrength();
    }

    @Override
    public void setStrength(Player player, int amount) {
        PlayerData data = getPlayerData(player);
        data.setStrength(amount);
        data.save();
    }

    @Override
    public void modifyStrength(Player player, int amount) {
        PlayerData data = getPlayerData(player);
        data.modifyStrength(amount);
        data.save();
    }
}
