package com.frostsmp.config;

import com.frostsmp.FrostStrengthSMP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class ConfigManager {

    private final FrostStrengthSMP plugin;
    private FileConfiguration config;

    private String serverName;
    private String discord;

    private int startingStrength;
    private int minStrength;
    private int maxStrength;
    private double damagePerLevel;
    private boolean gainOnKill;
    private int gainAmount;
    private boolean lossOnDeath;
    private int lossAmount;
    private boolean dropBottleAtMax;
    private boolean naturalDeathCostsStrength;
    private boolean dropBottleOnDeath;

    private boolean strengthLockEnabled;
    private String strengthLockAction;
    private boolean notifyAdminsOnLock;

    private boolean killstreaksEnabled;
    private boolean killstreaksAnnounce;
    private int rewardBottleEvery;

    private boolean assignRandomClass;
    private int passiveUnlock;
    private int ultimateUnlock;
    private int ultimateCooldownTicks;
    private boolean announceUltimate;

    private boolean crownEnabled;
    private String crownItemName;
    private List<String> crownItemLore;
    private int crownCustomModelData;
    private boolean crownGlow;

    private String bottleItemName;
    private List<String> bottleItemLore;
    private int bottleCustomModelData;
    private boolean bottleGlow;

    private boolean reviveTokenEnabled;
    private String reviveTokenName;
    private List<String> reviveTokenLore;
    private int reviveTokenCustomModelData;
    private boolean reviveTokenGlow;
    private int reviveTokenRestoreStrength;

    private boolean ezGapsEnabled;
    private boolean ezGapsInstant;
    private boolean ezGapsRecipe;
    private boolean ezAnvilEnabled;
    private int ezAnvilRepairCost;
    private int ezAnvilRenameCost;
    private boolean ezShulkerEnabled;
    private int ezShulkerStack;
    private boolean ezShulkerRecipe;
    private boolean ezWebsEnabled;
    private boolean ezWebsInstantBreak;
    private boolean ezWebsRecipe;

    private boolean rolesEnabled;

    private boolean livesEnabled;
    private int startingLives;
    private String zeroLivesAction;
    private boolean dropBottleOnLivesDeath;

    private boolean antiCheatEnabled;
    private boolean kickOnViolation;
    private int violationThreshold;
    private double reachMax;
    private double speedMax;
    private boolean flyCheck;
    private boolean fastBreak;
    private int autoClickerMaxCps;
    private int killAuraTickThreshold;
    private boolean exemptOp;

    private boolean spawnEnabled;
    private String spawnWorld;
    private double spawnX, spawnY, spawnZ;
    private double spawnYaw, spawnPitch;
    private boolean firstJoinEffect;

    // Home settings
    private boolean homeEnabled;
    private int homeCooldownSeconds;
    private int maxHomes;

    // RTP settings
    private boolean rtpEnabled;
    private int rtpCooldownSeconds;
    private int rtpRange;
    private int rtpMaxAttempts;

    private boolean killEffect;
    private boolean levelupEffect;
    private boolean reviveEffect;
    private boolean trailEnabled;
    private String trailParticle;

    private boolean actionBarEnabled;
    private int actionBarInterval;
    private String actionBarFormat;

    private String prefix;
    private String gainedStrength;
    private String lostStrength;
    private String strengthMax;
    private String strengthMin;
    private String strengthLocked;
    private String yourStrength;
    private String otherStrength;
    private String strengthSet;
    private String strengthWithdrew;
    private String noPermission;
    private String notPlayer;
    private String reloaded;
    private String discordMsg;
    private String discordHover;
    private String maceBlocked;
    private String ezgapsGiven;
    private String ezanvil;
    private String ezshulker;
    private String ezwebs;
    private String reviveUsed;
    private String reviveCannot;
    private String adminNotifyLock;
    private String adminBanned;
    private String adminPardoned;
    private String adminRevived;

    public ConfigManager(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    public void load() {
        config = plugin.getConfig();

        serverName = config.getString("server.name", "&6&lFrost &f&lSMP");
        discord = config.getString("server.discord", "https://discord.gg/vPeQfEmyh");

        startingStrength = config.getInt("strength.starting", 1);
        minStrength = config.getInt("strength.min", -5);
        maxStrength = config.getInt("strength.max", 5);
        damagePerLevel = config.getDouble("strength.damage-per-level", 1.0);
        gainOnKill = config.getBoolean("strength.gain-on-kill", true);
        gainAmount = config.getInt("strength.gain-amount", 1);
        lossOnDeath = config.getBoolean("strength.loss-on-death", true);
        lossAmount = config.getInt("strength.loss-amount", 1);
        dropBottleAtMax = config.getBoolean("strength.drop-bottle-at-max", true);
        naturalDeathCostsStrength = config.getBoolean("strength.natural-death-costs-strength", false);
        dropBottleOnDeath = config.getBoolean("strength.drop-bottle-on-death", true);

        strengthLockEnabled = config.getBoolean("strength-lock.enabled", true);
        strengthLockAction = config.getString("strength-lock.action-on-min", "spectator");
        notifyAdminsOnLock = config.getBoolean("strength-lock.notify-admins", true);

        killstreaksEnabled = config.getBoolean("killstreaks.enabled", true);
        killstreaksAnnounce = config.getBoolean("killstreaks.announce", true);
        rewardBottleEvery = config.getInt("killstreaks.reward-bottle-every", 5);

        assignRandomClass = config.getBoolean("classes.assign-random-class", true);
        passiveUnlock = config.getInt("classes.passive-unlock", 2);
        ultimateUnlock = config.getInt("classes.ultimate-unlock", 4);
        ultimateCooldownTicks = config.getInt("classes.ultimate-cooldown-ticks", 200);
        announceUltimate = config.getBoolean("classes.announce-ultimate", true);

        crownEnabled = config.getBoolean("crown.enabled", true);
        crownItemName = config.getString("crown.item-name", "&6&lThe Crown of Frost");
        crownItemLore = config.getStringList("crown.item-lore");
        crownCustomModelData = config.getInt("crown.custom-model-data", 101);
        crownGlow = config.getBoolean("crown.glow", true);

        bottleItemName = config.getString("bottle.item-name", "&d&lFrost Strength Bottle");
        bottleItemLore = config.getStringList("bottle.item-lore");
        bottleCustomModelData = config.getInt("bottle.custom-model-data", 102);
        bottleGlow = config.getBoolean("bottle.glow", false);

        reviveTokenEnabled = config.getBoolean("revive-token.enabled", true);
        reviveTokenName = config.getString("revive-token.item-name", "&c&lRevive Token of Frost");
        reviveTokenLore = config.getStringList("revive-token.item-lore");
        reviveTokenCustomModelData = config.getInt("revive-token.custom-model-data", 103);
        reviveTokenGlow = config.getBoolean("revive-token.glow", true);
        reviveTokenRestoreStrength = config.getInt("revive-token.restore-strength", 1);

        ezGapsEnabled = config.getBoolean("ez.gaps.enabled", true);
        ezGapsInstant = config.getBoolean("ez.gaps.instant-consume", true);
        ezGapsRecipe = config.getBoolean("ez.gaps.recipe-enabled", true);
        ezAnvilEnabled = config.getBoolean("ez.anvil.enabled", true);
        ezAnvilRepairCost = config.getInt("ez.anvil.repair-cost", 0);
        ezAnvilRenameCost = config.getInt("ez.anvil.rename-cost", 0);
        ezShulkerEnabled = config.getBoolean("ez.shulker.enabled", true);
        ezShulkerStack = config.getInt("ez.shulker.stack-size", 64);
        ezShulkerRecipe = config.getBoolean("ez.shulker.recipe-enabled", true);
        ezWebsEnabled = config.getBoolean("ez.webs.enabled", true);
        ezWebsInstantBreak = config.getBoolean("ez.webs.instant-break", true);
        ezWebsRecipe = config.getBoolean("ez.webs.recipe-enabled", true);

        rolesEnabled = config.getBoolean("roles.enabled", true);

        livesEnabled = config.getBoolean("lives.enabled", false);
        startingLives = config.getInt("lives.starting-lives", 3);
        zeroLivesAction = config.getString("lives.zero-lives-action", "ban");
        dropBottleOnLivesDeath = config.getBoolean("lives.drop-bottle-on-lives-death", true);

        antiCheatEnabled = config.getBoolean("anticheat.enabled", true);
        kickOnViolation = config.getBoolean("anticheat.kick-on-violation", true);
        violationThreshold = config.getInt("anticheat.violation-threshold", 15);
        reachMax = config.getDouble("anticheat.reach-max", 4.2);
        speedMax = config.getDouble("anticheat.speed-max", 1.15);
        flyCheck = config.getBoolean("anticheat.fly-check", true);
        fastBreak = config.getBoolean("anticheat.fast-break", true);
        autoClickerMaxCps = config.getInt("anticheat.auto-clicker-max-cps", 16);
        killAuraTickThreshold = config.getInt("anticheat.kill-aura-tick-threshold", 3);
        exemptOp = config.getBoolean("anticheat.exempt-op", true);

        spawnEnabled = config.getBoolean("spawn.enabled", true);
        spawnWorld = config.getString("spawn.world", "world");
        spawnX = config.getDouble("spawn.x", 0.5);
        spawnY = config.getDouble("spawn.y", 66.0);
        spawnZ = config.getDouble("spawn.z", 0.5);
        spawnYaw = config.getDouble("spawn.yaw", 0.0);
        spawnPitch = config.getDouble("spawn.pitch", 0.0);
        firstJoinEffect = config.getBoolean("spawn.first-join-effect", true);

        homeEnabled = config.getBoolean("home.enabled", true);
        homeCooldownSeconds = config.getInt("home.cooldown-seconds", 5);
        maxHomes = config.getInt("home.max-homes", 1);

        rtpEnabled = config.getBoolean("rtp.enabled", true);
        rtpCooldownSeconds = config.getInt("rtp.cooldown-seconds", 30);
        rtpRange = config.getInt("rtp.range", 5000);
        rtpMaxAttempts = config.getInt("rtp.max-attempts", 100);

        killEffect = config.getBoolean("effects.kill-effect", true);
        levelupEffect = config.getBoolean("effects.levelup-effect", true);
        reviveEffect = config.getBoolean("effects.revive-effect", true);
        trailEnabled = config.getBoolean("effects.trail-enabled", false);
        trailParticle = config.getString("effects.trail-particle", "VILLAGER_HAPPY");

        actionBarEnabled = config.getBoolean("actionbar.enabled", true);
        actionBarInterval = config.getInt("actionbar.interval-seconds", 3);
        actionBarFormat = config.getString("actionbar.format", "&c\u2694 Strength: &f{level}&c/{max} &7| &6Frost SMP");

        prefix = config.getString("messages.prefix", "&6&lFrost &f&lSMP &8\u203a &f");
        gainedStrength = config.getString("messages.gained-strength", "&a+1 Strength! &7(&c{level}&7)");
        lostStrength = config.getString("messages.lost-strength", "&c-1 Strength! &7(&c{level}&7)");
        strengthMax = config.getString("messages.strength-max", "&eAlready at maximum Strength (&c{max}&e).");
        strengthMin = config.getString("messages.strength-min", "&eAlready at minimum Strength (&c{min}&e).");
        strengthLocked = config.getString("messages.strength-locked", "&c&lLOCKED! &7Your Strength is &c-5&7.");
        yourStrength = config.getString("messages.your-strength", "&eYour Strength: &c{level}&e/&c{max}&e.");
        otherStrength = config.getString("messages.other-strength", "&e{target}&7's Strength: &c{level}&e/&c{max}&e.");
        strengthSet = config.getString("messages.strength-set", "&aSet {target}'s Strength to &c{level}&a.");
        strengthWithdrew = config.getString("messages.strength-withdrew", "&aWithdrew &c{amount}&a Strength into bottle(s).");
        noPermission = config.getString("messages.no-permission", "&cNo permission.");
        notPlayer = config.getString("messages.not-player", "&cPlayers only.");
        reloaded = config.getString("messages.reloaded", "&aFrost SMP config reloaded.");
        discordMsg = config.getString("messages.discord", "&6Join our Discord: &bhttps://discord.gg/vPeQfEmyh");
        discordHover = config.getString("messages.discord-hover", "&7Click to join the Frost SMP Discord!");
        maceBlocked = config.getString("messages.mace-blocked", "&cMaces cannot be enchanted on Frost SMP.");
        ezgapsGiven = config.getString("messages.ezgaps-given", "&aYou received &eEz Golden Apples&a!");
        ezanvil = config.getString("messages.ezanvil", "&eEZ Anvil: repair/rename cost set to &c0&e.");
        ezshulker = config.getString("messages.ezshulker", "&aYou received &eEz Shulker Boxes&a!");
        ezwebs = config.getString("messages.ezwebs", "&aYou received &eEz Cobwebs&a!");
        reviveUsed = config.getString("messages.revive-used", "&aYou were revived! Strength restored to &c{level}&a.");
        reviveCannot = config.getString("messages.revive-cannot", "&cYou cannot use a Revive Token (Strength is not -5).");
        adminNotifyLock = config.getString("messages.admin-notify-lock", "&c&lALERT: &f{player} &chas hit -5 Strength and is locked!");
        adminBanned = config.getString("messages.admin-banned", "&aBanned &e{player}&a.");
        adminPardoned = config.getString("messages.admin-pardoned", "&aPardoned &e{player}&a. They can play again.");
        adminRevived = config.getString("messages.admin-revived", "&aRevived &e{player}&a. Strength restored.");
    }

    public void reload() {
        plugin.reloadConfig();
        load();
    }

    public FileConfiguration getRawConfig() {
        return config;
    }

    public String getServerName() { return serverName; }
    public String getDiscord() { return discord; }

    public int getStartingStrength() { return startingStrength; }
    public int getMinStrength() { return minStrength; }
    public int getMaxStrength() { return maxStrength; }
    public double getDamagePerLevel() { return damagePerLevel; }
    public boolean isGainOnKill() { return gainOnKill; }
    public int getGainAmount() { return gainAmount; }
    public boolean isLossOnDeath() { return lossOnDeath; }
    public int getLossAmount() { return lossAmount; }
    public boolean isDropBottleAtMax() { return dropBottleAtMax; }
    public boolean isNaturalDeathCostsStrength() { return naturalDeathCostsStrength; }
    public boolean isDropBottleOnDeath() { return dropBottleOnDeath; }

    public boolean isStrengthLockEnabled() { return strengthLockEnabled; }
    public String getStrengthLockAction() { return strengthLockAction; }
    public boolean isNotifyAdminsOnLock() { return notifyAdminsOnLock; }

    public boolean isKillstreaksEnabled() { return killstreaksEnabled; }
    public boolean isKillstreaksAnnounce() { return killstreaksAnnounce; }
    public int getRewardBottleEvery() { return rewardBottleEvery; }

    public boolean isAssignRandomClass() { return assignRandomClass; }
    public int getPassiveUnlock() { return passiveUnlock; }
    public int getUltimateUnlock() { return ultimateUnlock; }
    public int getUltimateCooldownTicks() { return ultimateCooldownTicks; }
    public boolean isAnnounceUltimate() { return announceUltimate; }

    public boolean isCrownEnabled() { return crownEnabled; }
    public String getCrownItemName() { return crownItemName; }
    public List<String> getCrownItemLore() { return crownItemLore; }
    public int getCrownCustomModelData() { return crownCustomModelData; }
    public boolean isCrownGlow() { return crownGlow; }

    public String getBottleItemName() { return bottleItemName; }
    public List<String> getBottleItemLore() { return bottleItemLore; }
    public int getBottleCustomModelData() { return bottleCustomModelData; }
    public boolean isBottleGlow() { return bottleGlow; }

    public boolean isReviveTokenEnabled() { return reviveTokenEnabled; }
    public String getReviveTokenName() { return reviveTokenName; }
    public List<String> getReviveTokenLore() { return reviveTokenLore; }
    public int getReviveTokenCustomModelData() { return reviveTokenCustomModelData; }
    public boolean isReviveTokenGlow() { return reviveTokenGlow; }
    public int getReviveTokenRestoreStrength() { return reviveTokenRestoreStrength; }

    public boolean isEzGapsEnabled() { return ezGapsEnabled; }
    public boolean isEzGapsInstant() { return ezGapsInstant; }
    public boolean isEzGapsRecipe() { return ezGapsRecipe; }
    public boolean isEzAnvilEnabled() { return ezAnvilEnabled; }
    public int getEzAnvilRepairCost() { return ezAnvilRepairCost; }
    public int getEzAnvilRenameCost() { return ezAnvilRenameCost; }
    public boolean isEzShulkerEnabled() { return ezShulkerEnabled; }
    public int getEzShulkerStack() { return ezShulkerStack; }
    public boolean isEzShulkerRecipe() { return ezShulkerRecipe; }
    public boolean isEzWebsEnabled() { return ezWebsEnabled; }
    public boolean isEzWebsInstantBreak() { return ezWebsInstantBreak; }
    public boolean isEzWebsRecipe() { return ezWebsRecipe; }

    public boolean isRolesEnabled() { return rolesEnabled; }

    public boolean isLivesEnabled() { return livesEnabled; }
    public int getStartingLives() { return startingLives; }
    public String getZeroLivesAction() { return zeroLivesAction; }
    public boolean isDropBottleOnLivesDeath() { return dropBottleOnLivesDeath; }

    public boolean isAntiCheatEnabled() { return antiCheatEnabled; }
    public boolean isKickOnViolation() { return kickOnViolation; }
    public int getViolationThreshold() { return violationThreshold; }
    public double getReachMax() { return reachMax; }
    public double getSpeedMax() { return speedMax; }
    public boolean isFlyCheck() { return flyCheck; }
    public boolean isFastBreak() { return fastBreak; }
    public int getAutoClickerMaxCps() { return autoClickerMaxCps; }
    public int getKillAuraTickThreshold() { return killAuraTickThreshold; }
    public boolean isExemptOp() { return exemptOp; }

    public boolean isSpawnEnabled() { return spawnEnabled; }
    public String getSpawnWorld() { return spawnWorld; }
    public double getSpawnX() { return spawnX; }
    public double getSpawnY() { return spawnY; }
    public double getSpawnZ() { return spawnZ; }
    public double getSpawnYaw() { return spawnYaw; }
    public double getSpawnPitch() { return spawnPitch; }
    public boolean isFirstJoinEffect() { return firstJoinEffect; }

    public boolean isHomeEnabled() { return homeEnabled; }
    public int getHomeCooldown() { return homeCooldownSeconds; }
    public int getMaxHomes() { return maxHomes; }

    public boolean isRtpEnabled() { return rtpEnabled; }
    public int getRtpCooldown() { return rtpCooldownSeconds; }
    public int getRtpRange() { return rtpRange; }
    public int getRtpMaxAttempts() { return rtpMaxAttempts; }

    public boolean isKillEffect() { return killEffect; }
    public boolean isLevelupEffect() { return levelupEffect; }
    public boolean isReviveEffect() { return reviveEffect; }
    public boolean isTrailEnabled() { return trailEnabled; }
    public String getTrailParticle() { return trailParticle; }

    public boolean isActionBarEnabled() { return actionBarEnabled; }
    public int getActionBarInterval() { return actionBarInterval; }
    public String getActionBarFormat() { return actionBarFormat; }

    public String getPrefix() { return prefix; }
    public String getGainedStrength() { return gainedStrength; }
    public String getLostStrength() { return lostStrength; }
    public String getStrengthMax() { return strengthMax; }
    public String getStrengthMin() { return strengthMin; }
    public String getStrengthLocked() { return strengthLocked; }
    public String getYourStrength() { return yourStrength; }
    public String getOtherStrength() { return otherStrength; }
    public String getStrengthSet() { return strengthSet; }
    public String getStrengthWithdrew() { return strengthWithdrew; }
    public String getNoPermission() { return noPermission; }
    public String getNotPlayer() { return notPlayer; }
    public String getReloaded() { return reloaded; }
    public String getDiscordMsg() { return discordMsg; }
    public String getDiscordHover() { return discordHover; }
    public String getMaceBlocked() { return maceBlocked; }
    public String getEzgapsGiven() { return ezgapsGiven; }
    public String getEzanvil() { return ezanvil; }
    public String getEzshulker() { return ezshulker; }
    public String getEzwebs() { return ezwebs; }
    public String getReviveUsed() { return reviveUsed; }
    public String getReviveCannot() { return reviveCannot; }
    public String getAdminNotifyLock() { return adminNotifyLock; }
    public String getAdminBanned() { return adminBanned; }
    public String getAdminPardoned() { return adminPardoned; }
    public String getAdminRevived() { return adminRevived; }

    public void save() {
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save config.yml", e);
        }
    }

    public net.kyori.adventure.text.Component component(String msg) {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacy('§')
                .deserialize(msg == null ? "" : msg);
    }
}
