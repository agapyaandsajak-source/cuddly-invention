package com.frostsmp;

import com.frostsmp.ability.AbilityManager;
import com.frostsmp.api.StrengthAPI;
import com.frostsmp.command.*;
import com.frostsmp.config.ConfigManager;
import com.frostsmp.data.PlayerData;
import com.frostsmp.item.CustomItems;
import com.frostsmp.listener.*;
import com.frostsmp.bossbar.BossBarTask;
import com.frostsmp.manager.*;
import com.frostsmp.recipe.RecipeManager;
import com.frostsmp.tutorial.TutorialManager;
import com.frostsmp.util.ActionBarTask;
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
    private BossBarTask bossBarTask;
    private TutorialManager tutorialManager;
    private HomeManager homeManager;
    private BountyManager bountyManager;
    private ProtectionManager protectionManager;
    private CombatTagManager combatTagManager;
    private SoulBindManager soulBindManager;
    private PunishCommand punishCommand;
    private TrustCommand trustCommand;
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

        bountyManager = new BountyManager(this);
        protectionManager = new ProtectionManager(this);
        combatTagManager = new CombatTagManager(this);
        soulBindManager = new SoulBindManager(this);
        punishCommand = new PunishCommand(this);
        trustCommand = new TrustCommand(this);

        bossBarTask = new BossBarTask(this);
        bossBarTask.runTaskTimer(this, 0L, 20L);

        tutorialManager = new TutorialManager(this);

        homeManager = new HomeManager(this);

        loadSpawn();

        getCommand("strength").setExecutor(new StrengthCommand(this));
        getCommand("strengthadmin").setExecutor(new StrengthAdminCommand(this));
        getCommand("recipes").setExecutor(new RecipesCommand(this));
        getCommand("discord").setExecutor(new DiscordCommand(this));
        getCommand("role").setExecutor(new RoleCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("ez").setExecutor(new EzCommand(this));
        getCommand("tutorial").setExecutor(new TutorialCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("rtp").setExecutor(new RtpCommand(this));
        getCommand("menu").setExecutor(new MenuCommand(this));
        getCommand("bind").setExecutor(new BindCommand(this, soulBindManager));
        getCommand("bounty").setExecutor(new BountyCommand(this, bountyManager));
        getCommand("trust").setExecutor(trustCommand);
        getCommand("punish").setExecutor(punishCommand);

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerKillListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ItemUseListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EnchantListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CombatTagListener(this, combatTagManager), this);
        Bukkit.getPluginManager().registerEvents(new KillFarmingListener(this), this);
        Bukkit.getPluginManager().registerEvents(new TotemLimiterListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ProtectionListener(this, protectionManager), this);
        Bukkit.getPluginManager().registerEvents(soulBindManager, this);

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

    public BossBarTask getBossBarTask() {
        return bossBarTask;
    }

    public TutorialManager getTutorialManager() {
        return tutorialManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public BountyManager getBountyManager() {
        return bountyManager;
    }

    public ProtectionManager getProtectionManager() {
        return protectionManager;
    }

    public CombatTagManager getCombatTagManager() {
        return combatTagManager;
    }

    public SoulBindManager getSoulBindManager() {
        return soulBindManager;
    }

    public PunishCommand getPunishCommand() {
        return punishCommand;
    }

    public TrustCommand getTrustCommand() {
        return trustCommand;
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
