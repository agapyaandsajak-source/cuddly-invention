# Frost Strength SMP — Feature Prompt / Summary

Use this document as a reference for what has been added to the Frost Strength
SMP plugin. You can copy sections of this into server listings, Discord
announcements, plugin descriptions, or AI image-generation prompts.

---

## 1. Server Identity

- **Server Name:** Frost SMP
- **Server IP:** play.frostsmp.example.com (replace with your real IP)
- **Discord Invite:** https://discord.gg/vPeQfEmyh
- **Minecraft Version:** 1.21.x (Paper 26.1.2)
- **Plugin Name:** FrostStrengthSMP
- **Plugin Version:** 1.0.0
- **Author:** FrostSMP

---

## 2. Core Strength System (Kill-to-Power)

| Feature | Description |
|---------|-------------|
| Kill Reward | +1 Strength per player kill |
| Death Penalty | −1 Strength per player death |
| Damage Bonus | +1 attack damage (half heart) per Strength level |
| Persistence | Strength saves across death, respawn, and relogs (per UUID) |
| Min Strength | −5 (configurable) |
| Max Strength | +5 (configurable) |
| Max Kill Drop | Killing at max strength drops a Strength Bottle |
| Natural Death | Optional strength loss on mob/fall/lava deaths |

---

## 3. Strength Lock (−5) & Admin Decision

When a player's Strength drops to −5, they become **locked**:
- Forced into **Spectator Mode**
- Cannot deal or take damage
- Cannot interact meaningfully

**Admin Options:**
- `/strengthadmin ban <player>` — Ban the locked player
- `/strengthadmin pardon <player>` — Pardon, restore to +1, set to Survival
- `/strengthadmin revive <player>` — Admin revive (restore strength)
- Admins are notified in chat: `&c&lALERT: <player> has hit -5 Strength and is locked!`

---

## 4. Revive Token (Expensive Recovery Item)

- **Item:** Nether Star with CustomModelData (103), enchanted glow, custom name `&c&lRevive Token of Frost`
- **Lore:** `&7Use when locked at -5 Strength. &7Restores you to +1 Strength. &cVery expensive to craft!`
- **Crafting Recipe:** 8 Diamond Blocks + 1 Nether Star + 1 Strength Bottle (very expensive)
- **Usage:** Right-click to consume. **Only works when Strength is −5** (locked)
- **Effect:** Unlocks player, restores Strength to +1, plays revive effect (heart particles + star firework + ender dragon death sound)
- **Command:** `/strength revive` (consumes token from inventory)

---

## 5. Weapon Classes (9 Classes)

| Class | Passive (+2) | Ultimate (+4) |
|-------|-------------|---------------|
| Sword | Critical strikes deal extra damage | Whirlwind: strike all nearby enemies |
| Axe | Chance to disable shields | Cleave: next strike is massive + disables shield |
| Bow | Arrows fly faster, extra damage | Barrage: fire 5 arrows quickly |
| Crossbow | Shots pierce multiple targets | Blast: fire explosive fireball |
| Trident | Throws deal bonus damage | Storm: call lightning on target |
| Mace | Smash hits deal bonus damage | Seismic Smash: slam ground, damage all nearby |
| Spear | Increased reach and knockback | Lunge: dash forward and impale |
| Shield | Blocking reduces damage | Shield Bash: stun + knock back enemies |
| Armor | Damage reduction + absorption | Iron Skin: absorption + resistance 15s |

- Random class assigned on first join
- Passives are always active once unlocked
- Ultimates on cooldown (configurable, default 200 ticks = 10 seconds)
- Ultimate announcements broadcast globally

---

## 6. Custom-Designed Items

| Item | Base Material | Model Data | Glow | Use |
|------|--------------|------------|------|-----|
| Strength Bottle | Potion | 102 | No | Drink for +1 Strength |
| The Crown | Golden Helmet | 101 | Yes | Max Strength + 20 hearts |
| Revive Token | Nether Star | 103 | Yes | Revive from −5 lock |

All items use PersistentDataContainer NBT tags for identification.
CustomModelData allows resource pack mapping to unique textures.

---

## 7. EZ Items (Quality of Life)

| Command | Item | Recipe | Notes |
|---------|------|--------|-------|
| `/ez gaps` | Golden Apple ×16 | 8 Gold Ingots + 1 Apple | Instant-consume option |
| `/ez anvil` | — | — | Free anvil repair/rename cost |
| `/ez shulker` | Shulker Box ×4 | 1 Chest + 2 Shulker Shells | Stackable, easy craft |
| `/ez webs` | Cobweb ×32 | 4 String (2×2) | Instant break option |

---

## 8. Custom Design Effects (Particles + Sounds)

| Effect | Particles | Sound | When |
|--------|-----------|-------|------|
| Kill Effect | EXPLOSION_LARGE + FLAME | Entity generic explode | On player kill |
| Level-Up Effect | FIREWORKS_SPARK | Entity player levelup | Strength gain / killstreak |
| Revive Effect | HEART + STAR firework | Entity player levelup + ender dragon death | Revive Token used |
| Spawn Effect | END_ROD ring + STAR firework | Entity ender dragon growl | First join / /spawn |
| Trail Effect | Configurable (default VILLAGER_HAPPY) | — | While walking (optional) |

Action bar displays live Strength: `&c⚔ Strength: &f{level}&c/{max} &7| &6Frost SMP`

---

## 9. Roles System

| Role | Prefix | Starting Strength | Cooldown Reduction |
|------|--------|-------------------|-------------------|
| Member | &7[Member] | +1 | 0% |
| VIP | &a[VIP] | +1 | 10% |
| MVP | &e[MVP] | +2 | 15% |
| Strength God | &c&l[God] | +3 | 20% |
| Admin | &4[Admin] | +1 | 25% |

Commands: `/role list`, `/role info <player>`, `/role set <player> <role>`

---

## 10. Built-In Anti-Cheat (Grim-Style)

| Check | Detection | Action |
|-------|-----------|--------|
| Reach | Distance > 4.2 blocks | Violation points |
| Speed | Movement > 1.15 blocks/tick | Violation points |
| Fly | Flying without support | Violation points |
| Fast Break | Breaking blocks too quickly | Violation points |
| Auto-Clicker | > 16 CPS | Violation points |
| Kill Aura | Attacking every tick | Violation points |

- Violation points decay over time
- Kick when threshold exceeded (default 15 points)
- Ops exempt by default

---

## 11. Additional Features

| Feature | Description |
|---------|-------------|
| Killstreaks | Announcements + fireworks + Strength Bottle at 3/5/10+ streaks |
| Nicknames | `/strength nick <name>` |
| Mace Lock | Maces cannot be enchanted via table, anvil, or books |
| Custom Spawn | Configurable spawn location with particle/firework effect |
| `/recipes` | View all Frost SMP crafting recipes |
| `/discord` | Clickable, hoverable Discord invite |
| Public API | `com.frostsmp.api.StrengthAPI` for other plugins |
| Configurable | Everything in `config.yml`, reload with `/strengthadmin reload` |

---

## 12. Commands Reference

### Player Commands
- `/strength` — Check Strength
- `/strength class` — View Weapon Class
- `/strength ability` — Use Ultimate
- `/strength withdraw <n>` — Convert Strength to bottles
- `/strength revive` — Use Revive Token
- `/strength nick <name>` — Set nickname
- `/recipes` — View recipes
- `/discord` — Join Discord
- `/role` — Role management
- `/spawn` — Teleport to spawn
- `/ez <gaps|anvil|shulker|webs>` — EZ items

### Admin Commands
- `/strengthadmin set <player> <amount>`
- `/strengthadmin reload`
- `/strengthadmin reset <player>`
- `/strengthadmin ban <player>`
- `/strengthadmin pardon <player>`
- `/strengthadmin revive <player>`

---

## 13. Permissions

| Permission | Default | Description |
|------------|---------|-------------|
| frostsmp.use | true | Basic commands |
| frostsmp.admin | op | Admin commands |
| frostsmp.role | op | Role management |
| frostsmp.ez | true | EZ item commands |

---

## 14. Build & Install

```bash
cd /home/user/FrostStrengthSMP
./build.sh          # or: mvn clean package
# -> target/FrostStrengthSMP-1.0.0.jar
```

1. Copy the jar to your Paper server's `plugins/` folder
2. Start the server to generate `config.yml`
3. Edit `config.yml` (Discord, server IP, anti-cheat thresholds, etc.)
4. Run `/strengthadmin reload` in-game
5. Done — Frost SMP is live!

---

## 15. Code Protection

Run the built jar through **ProGuard**, **Allatori**, or **DashO** to obfuscate
internal class/method names. Example ProGuard config is in `README.md`.

---

**Enjoy Frost SMP — where every kill makes you stronger, and every death brings you closer to the −5 lock.** ❄️⚔️
