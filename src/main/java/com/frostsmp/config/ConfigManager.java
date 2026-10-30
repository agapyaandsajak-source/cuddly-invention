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

    private boolean antiFarmEnabled;
    private int antiFarmMaxKillsPerVictim;
    private int antiFarmWindowMinutes;

    private boolean combatTagEnabled;
    private int combatTagDuration;
    private boolean combatTagLogoutKills;
    private boolean combatTagBlockCommands;

    private boolean deathProtectionEnabled;
    private int deathProtectionDuration;

    private boolean newbieProtectionEnabled;
    private int newbieProtectionDuration;

    private boolean totemLimiterEnabled;
    private int totemMax;

    private boolean assignRandomClass;
    private int passiveUnlock;
    private int ultimateUnlock;
    private int ultimateCooldownTicks;
    private boolean announceUltimate;

    private boolean soulBindingEnabled;
    private int soulBindCost;
    private boolean soulBindKeepOnDeath;
    private boolean soulBindLoyaltyReturn;
    private boolean soulBindUndroppable;
    private boolean soulBindHoldingParticles;

    private boolean bountiesEnabled;
    private boolean bountiesAnnouncement;

    private boolean crownEnabled;
    private String crownItemName;
    private List<String> crownItemLore;
    private int crownCustomModelData;
    private boolean crownGlow;

    private String bottleItemName;
    private List<String> bottleItemLore;
    private int bottleCustomModelData;
    private boolean bottleGlow;

    private String strengthShardName;
    private List<String> strengthShardLore;
    private int strengthShardCustomModelData;
    private boolean strengthShardGlow;

    private String rerollGrimoireName;
    private List<String> rerollGrimoireLore;
    private int rerollGrimoireCustomModelData;
    private boolean rerollGrimoireGlow;

    private String deathCertificateName;
    private List<String> deathCertificateLore;
    private int deathCertificateCustomModelData;
    private boolean deathCertificateGlow;

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

    private boolean spawnEnabled;
    private String spawnWorld;
    private double spawnX, spawnY, spawnZ;
    private double spawnYaw, spawnPitch;
    private boolean firstJoinEffect;

    private boolean homeEnabled;
    private int homeCooldownSeconds;
    private int maxHomes;

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
    // Soul binding messages
    private String bindSuccess;
    private String bindAlready;
    private String bindCost;
    private String bindNotEnough;
    private String bindKept;
    // Bounty messages
    private String bountyPlaced;
    private String bountyCollected;
    private String bountyTarget;
    // Combat tag messages
    private String combatTagged;
    private String combatLogKilled;
    // Protection messages
    private String deathProtected;
    private String protectionExpired;
    private String newbieProtected;
    private String newbieExpired;
    // Totem messages
    private String totemLimit;
    // Trust messages
    private String trustAdded;
    private String trustRemoved;
    private String trustList;
    private String trustEmpty;
    private String trustSelf;
    // Maintenance messages
    private String maintenanceOn;
    private String maintenanceOff;
    private String maintenanceActive;
    // Punish messages
    private String punishUsed;

    public ConfigManager(FrostStrengthSMP plugin) {
        this.plugin = plugin;
    }

    public void load() {
        config = plugin.getConfig();

        serverName = config.getString("server.name", "&d&lFrost &b&lSMP");
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

        antiFarmEnabled = config.getBoolean("anti-farm.enabled", true);
        antiFarmMaxKillsPerVictim = config.getInt("anti-farm.max-kills-per-victim", 2);
        antiFarmWindowMinutes = config.getInt("anti-farm.window-minutes", 10);

        combatTagEnabled = config.getBoolean("combat-tag.enabled", true);
        combatTagDuration = config.getInt("combat-tag.duration-seconds", 15);
        combatTagLogoutKills = config.getBoolean("combat-tag.logout-kills", true);
        combatTagBlockCommands = config.getBoolean("combat-tag.block-commands", true);

        deathProtectionEnabled = config.getBoolean("death-protection.enabled", true);
        deathProtectionDuration = config.getInt("death-protection.duration-seconds", 3600);

        newbieProtectionEnabled = config.getBoolean("newbie-protection.enabled", true);
        newbieProtectionDuration = config.getInt("newbie-protection.duration-seconds", 3600);

        totemLimiterEnabled = config.getBoolean("totem-limiter.enabled", true);
        totemMax = config.getInt("totem-limiter.max-totems", 2);

        assignRandomClass = config.getBoolean("classes.assign-random-class", true);
        passiveUnlock = config.getInt("classes.passive-unlock", 2);
        ultimateUnlock = config.getInt("classes.ultimate-unlock", 4);
        ultimateCooldownTicks = config.getInt("classes.ultimate-cooldown-ticks", 200);
        announceUltimate = config.getBoolean("classes.announce-ultimate", true);

        soulBindingEnabled = config.getBoolean("soul-binding.enabled", true);
        soulBindCost = config.getInt("soul-binding.cost-strength", 1);
        soulBindKeepOnDeath = config.getBoolean("soul-binding.keep-on-death", true);
        soulBindLoyaltyReturn = config.getBoolean("soul-binding.loyalty-return", true);
        soulBindUndroppable = config.getBoolean("soul-binding.undroppable", true);
        soulBindHoldingParticles = config.getBoolean("soul-binding.holding-particles", true);

        bountiesEnabled = config.getBoolean("bounties.enabled", true);
        bountiesAnnouncement = config.getBoolean("bounties.announcement", true);

        crownEnabled = config.getBoolean("crown.enabled", true);
        crownItemName = config.getString("crown.item-name", "&d&lThe Crown of Frost");
        crownItemLore = config.getStringList("crown.item-lore");
        crownCustomModelData = config.getInt("crown.custom-model-data", 101);
        crownGlow = config.getBoolean("crown.glow", true);

        bottleItemName = config.getString("bottle.item-name", "&d&lFrost Strength Bottle");
        bottleItemLore = config.getStringList("bottle.item-lore");
        bottleCustomModelData = config.getInt("bottle.custom-model-data", 102);
        bottleGlow = config.getBoolean("bottle.glow", false);

        strengthShardName = config.getString("strength-shard.item-name", "&e&lStrength Shard");
        strengthShardLore = config.getStringList("strength-shard.item-lore");
        strengthShardCustomModelData = config.getInt("strength-shard.custom-model-data", 104);
        strengthShardGlow = config.getBoolean("strength-shard.glow", true);

        rerollGrimoireName = config.getString("reroll-grimoire.item-name", "&5&lReroll Grimoire");
        rerollGrimoireLore = config.getStringList("reroll-grimoire.item-lore");
        rerollGrimoireCustomModelData = config.getInt("reroll-grimoire.custom-model-data", 105);
        rerollGrimoireGlow = config.getBoolean("reroll-grimoire.glow", true);

        deathCertificateName = config.getString("death-certificate.item-name", "&8&lDeath Certificate");
        deathCertificateLore = config.getStringList("death-certificate.item-lore");
        deathCertificateCustomModelData = config.getInt("death-certificate.custom-model-data", 106);
        deathCertificateGlow = config.getBoolean("death-certificate.glow", false);

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
        actionBarFormat = config.getString("actionbar.format", "&d\u2694 Strength: &f{level}&d/{max} &7| &bFrost SMP");

        prefix = config.getString("messages.prefix", "&d&lFrost &b&lSMP &8\u203a &f");
        gainedStrength = config.getString("messages.gained-strength", "&a+1 Strength! &7(&c{level}&7)");
        lostStrength = config.getString("messages.lost-strength", "&c-1 Strength! &7(&c{level}&7)");
        strengthMax = config.getString("messages.strength-max", "&eAlready at maximum Strength (&c{max}&e).");
        strengthMin = config.getString("messages.strength-min", "&eAlready at minimum Strength (&c{min}&e).");
        strengthLocked = config.getString("messages.strength-locked", "&c&lLOCKED! &7Your Strength is &c-5&7.");
        yourStrength = config.getString("messages.your-strength", "&eYour Strength: &c{level}&e/&c{max}&e.");
        otherStrength = config.getString("messages.other-strength", "&e{target}&7's Strength: &c{level}&e/&c{max}&e.");
        strengthSet = config.getString("messages.strength-set", "&aSet {target}'s Strength to &c{level}&a.");
        strengthWithdrew = config.getString("messages.strength-withdrew", "&aWithdrew &c{amount}&a Strength into shard(s).");
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

        bindSuccess = config.getString("messages.bind-success", "&aYour &f{item}&a is now soul-bound!");
        bindAlready = config.getString("messages.bind-already", "&cThat item is already soul-bound.");
        bindCost = config.getString("messages.bind-cost", "&cYou need &f1 Strength&c to bind this item.");
        bindNotEnough = config.getString("messages.bind-not-enough", "&cNot enough Strength to bind.");
        bindKept = config.getString("messages.bind-kept", "&eYour soul-bound weapon returns to you!");
        bountyPlaced = config.getString("messages.bounty-placed", "&aBounty of &e{amount}&a placed on &f{target}&a.");
        bountyCollected = config.getString("messages.bounty-collected", "&aYou collected the bounty on &f{target}&a!");
        bountyTarget = config.getString("messages.bounty-target", "&cYou have a bounty on your head!");
        combatTagged = config.getString("messages.combat-tagged", "&cYou are in combat! Logging out will kill you.");
        combatLogKilled = config.getString("messages.combat-log-killed", "&cYou logged out during combat and were slain!");
        deathProtected = config.getString("messages.death-protected", "&aDeath protection active - no strength lost.");
        protectionExpired = config.getString("messages.protection-expired", "&cYour death protection has expired.");
        newbieProtected = config.getString("messages.newbie-protected", "&aNewbie protection active for 1 hour.");
        newbieExpired = config.getString("messages.newbie-expired", "&cYour newbie protection has expired.");
        totemLimit = config.getString("messages.totem-limit", "&cYou cannot carry more than &e{max}&c totems!");
        trustAdded = config.getString("messages.trust-added", "&aAdded &f{target}&a to your trusted players.");
        trustRemoved = config.getString("messages.trust-removed", "&aRemoved &f{target}&a from your trusted players.");
        trustList = config.getString("messages.trust-list", "&eTrusted players: &f{list}");
        trustEmpty = config.getString("messages.trust-empty", "&eYou have no trusted players.");
        trustSelf = config.getString("messages.trust-self", "&cYou cannot trust yourself.");
        maintenanceOn = config.getString("messages.maintenance-on", "&cMaintenance mode enabled.");
        maintenanceOff = config.getString("messages.maintenance-off", "&aMaintenance mode disabled.");
        maintenanceActive = config.getString("messages.maintenance-active", "&cThe server is in maintenance mode.");
        punishUsed = config.getString("messages.punish-used", "&c&lPUNISHED: &f{player} &cgets double strength loss for 24h.");
    }

    public void reload() {
        plugin.reloadConfig();
        load();
    }

    public FileConfiguration getRawConfig() {
        return config;
    }

    // Getters - all fields
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

    public boolean isAntiFarmEnabled() { return antiFarmEnabled; }
    public int getAntiFarmMaxKillsPerVictim() { return antiFarmMaxKillsPerVictim; }
    public int getAntiFarmWindowMinutes() { return antiFarmWindowMinutes; }

    public boolean isCombatTagEnabled() { return combatTagEnabled; }
    public int getCombatTagDuration() { return combatTagDuration; }
    public boolean isCombatTagLogoutKills() { return combatTagLogoutKills; }
    public boolean isCombatTagBlockCommands() { return combatTagBlockCommands; }

    public boolean isDeathProtectionEnabled() { return deathProtectionEnabled; }
    public int getDeathProtectionDuration() { return deathProtectionDuration; }

    public boolean isNewbieProtectionEnabled() { return newbieProtectionEnabled; }
    public int getNewbieProtectionDuration() { return newbieProtectionDuration; }

    public boolean isTotemLimiterEnabled() { return totemLimiterEnabled; }
    public int getTotemMax() { return totemMax; }

    public boolean isAssignRandomClass() { return assignRandomClass; }
    public int getPassiveUnlock() { return passiveUnlock; }
    public int getUltimateUnlock() { return ultimateUnlock; }
    public int getUltimateCooldownTicks() { return ultimateCooldownTicks; }
    public boolean isAnnounceUltimate() { return announceUltimate; }

    public boolean isSoulBindingEnabled() { return soulBindingEnabled; }
    public int getSoulBindCost() { return soulBindCost; }
    public boolean isSoulBindKeepOnDeath() { return soulBindKeepOnDeath; }
    public boolean isSoulBindLoyaltyReturn() { return soulBindLoyaltyReturn; }
    public boolean isSoulBindUndroppable() { return soulBindUndroppable; }
    public boolean isSoulBindHoldingParticles() { return soulBindHoldingParticles; }

    public boolean isBountiesEnabled() { return bountiesEnabled; }
    public boolean isBountiesAnnouncement() { return bountiesAnnouncement; }

    public boolean isCrownEnabled() { return crownEnabled; }
    public String getCrownItemName() { return crownItemName; }
    public List<String> getCrownItemLore() { return crownItemLore; }
    public int getCrownCustomModelData() { return crownCustomModelData; }
    public boolean isCrownGlow() { return crownGlow; }

    public String getBottleItemName() { return bottleItemName; }
    public List<String> getBottleItemLore() { return bottleItemLore; }
    public int getBottleCustomModelData() { return bottleCustomModelData; }
    public boolean isBottleGlow() { return bottleGlow; }

    public String getStrengthShardName() { return strengthShardName; }
    public List<String> getStrengthShardLore() { return strengthShardLore; }
    public int getStrengthShardCustomModelData() { return strengthShardCustomModelData; }
    public boolean isStrengthShardGlow() { return strengthShardGlow; }

    public String getRerollGrimoireName() { return rerollGrimoireName; }
    public List<String> getRerollGrimoireLore() { return rerollGrimoireLore; }
    public int getRerollGrimoireCustomModelData() { return rerollGrimoireCustomModelData; }
    public boolean isRerollGrimoireGlow() { return rerollGrimoireGlow; }

    public String getDeathCertificateName() { return deathCertificateName; }
    public List<String> getDeathCertificateLore() { return deathCertificateLore; }
    public int getDeathCertificateCustomModelData() { return deathCertificateCustomModelData; }
    public boolean isDeathCertificateGlow() { return deathCertificateGlow; }

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

    public String getBindSuccess() { return bindSuccess; }
    public String getBindAlready() { return bindAlready; }
    public String getBindCost() { return bindCost; }
    public String getBindNotEnough() { return bindNotEnough; }
    public String getBindKept() { return bindKept; }
    public String getBountyPlaced() { return bountyPlaced; }
    public String getBountyCollected() { return bountyCollected; }
    public String getBountyTarget() { return bountyTarget; }
    public String getCombatTagged() { return combatTagged; }
    public String getCombatLogKilled() { return combatLogKilled; }
    public String getDeathProtected() { return deathProtected; }
    public String getProtectionExpired() { return protectionExpired; }
    public String getNewbieProtected() { return newbieProtected; }
    public String getNewbieExpired() { return newbieExpired; }
    public String getTotemLimit() { return totemLimit; }
    public String getTrustAdded() { return trustAdded; }
    public String getTrustRemoved() { return trustRemoved; }
    public String getTrustList() { return trustList; }
    public String getTrustEmpty() { return trustEmpty; }
    public String getTrustSelf() { return trustSelf; }
    public String getMaintenanceOn() { return maintenanceOn; }
    public String getMaintenanceOff() { return maintenanceOff; }
    public String getMaintenanceActive() { return maintenanceActive; }
    public String getPunishUsed() { return punishUsed; }

    public void save() {
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save config.yml", e);
        }
    }

    public net.kyori.adventure.text.Component component(String msg) {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacy('&')
                .deserialize(msg == null ? "" : msg);
    }
}
