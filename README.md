# FrostStrengthSMP
A complete, feature-packed **Paper/Spigot** Strength SMP plugin for **Frost SMP** (Paper 26.1.2 / MC 1.21.x).

Researched from what players find most fun in Strength SMPs: kill-to-power progression, strength as damage, weapon classes, killstreaks, EZ mechanics, custom spawns, Discord integration, roles, a built-in Grim-style anti-cheat, and a **revive token system** for players locked at -5 strength.

---

## Features

**Core Strength System**
- Kill a player → **+1 Strength**; die to a player → **−1 Strength** (configurable amounts).
- Strength = attack damage (+1 HP / half a heart per level by default).
- Persists across death, respawn, and relogs (saved per UUID).
- Min/max caps fully configurable. Minimum is now **-5**.
- Killing a player while at max strength drops a **Strength Bottle**.

**Strength Lock at -5 (Admin Decision)**
- When a player's strength drops to **-5**, they are **locked**: forced into spectator mode and cannot fight, take damage, or interact.
- Admins are notified (`&c&lALERT: <player> has hit -5 Strength and is locked!`).
- The admin decides what happens:
  - `/strengthadmin ban <player>` — ban them from the server.
  - `/strengthadmin pardon <player>` — pardon them, restore strength to +1, set back to survival.
  - `/strengthadmin revive <player>` — admin revive (restore strength).
- Players can also save themselves with a **Revive Token**.

**Revive Token (Very Expensive)**
- Custom-designed Nether Star with **CustomModelData**, enchanted glow, and custom name/lore.
- Crafting recipe: 8 Diamond Blocks + 1 Nether Star + 1 Strength Bottle (very expensive).
- Right-click to consume — **only works when strength is -5** (locked).
- Restores strength to +1, unlocks the player, and plays a custom revive effect (hearts + star firework + sounds).
- `/strength revive` command also consumes a token from inventory.

**Custom Effects (Design Effects)**
- Kill effect: explosion + flame particles + sound at kill location.
- Level-up effect: firework spark burst + level-up sound.
- Revive effect: heart particles + star firework + ender dragon death sound.
- Spawn effect: end-rod ring + firework + ender dragon growl on first join/spawn.
- Optional particle trail while walking.
- Action bar showing live Strength.

**Custom-Designed Items**
- **Strength Bottle** — CustomModelData, optional glow, custom name/lore.
- **The Crown** — CustomModelData, glow, custom name/lore.
- **Revive Token** — CustomModelData, glow, custom name/lore (Nether Star).
- All items use PersistentDataContainer NBT tags for identification.

**The Crown** — wear it for max Strength + Health Boost V (20 hearts); take it off to restore real Strength (crown-safe across death/logout).

**Weapon Classes** (9 classes, random on first join): Sword, Axe, Bow, Crossbow, Trident, Mace, Spear, Shield, Armor.
- Passive at Strength +2 (always active).
- Ultimate at Strength +4 (crouch + right-click or `/strength ability`) with cooldown.
- Abilities: Whirlwind, Cleave, Barrage, Blast, Storm, Seismic Smash, Lunge, Shield Bash, Iron Skin.

**Killstreaks** — announcements, fireworks, and Strength Bottle rewards at 3/5/10+ streaks.

**EZ Items**
- `/ez gaps` — Ez Golden Apples (instant-consume option).
- `/ez anvil` — free anvil repair/rename cost.
- `/ez shulker` — Ez Shulker Boxes (stackable, easy recipe).
- `/ez webs` — Ez Cobwebs (instant break + recipe).
- Custom recipes registered for all EZ items.

**Roles** — `/role list | info | set`: Member, VIP, MVP, Strength God, Admin. Each has a colored prefix, starting strength, and cooldown reduction.

**Discord** — `/discord` shows a clickable, hoverable invite to **https://discord.gg/vPeQfEmyh**.

**Custom Spawn** — `/spawn` teleports to a configurable spawn location with a custom particle/firework design effect. First join auto-teleports to spawn.

**Built-in Anti-Cheat (Grim-style)**
- Reach, speed, fly, fast-break, auto-clicker, and kill-aura checks.
- Violation points decay over time; kick when threshold exceeded.
- Ops exempt by default.

**Mace Lock** — Maces cannot be enchanted via table, anvil, or enchanted books.

**Commands**
- `/strength` — check strength / class / ability / withdraw / revive / nick / trail
- `/strengthadmin` — set / reload / reset / ban / pardon / revive (admin)
- `/recipes` — view Frost SMP crafting recipes
- `/discord` — join the Frost SMP Discord
- `/role` — manage roles
- `/spawn` — teleport to spawn
- `/ez` — gaps / anvil / shulker / webs

**Permissions:** `frostsmp.use`, `frostsmp.admin`, `frostsmp.role`, `frostsmp.ez`

**Public API:** `com.frostsmp.api.StrengthAPI` for other plugins.

---

## Build

Requires **Java 17+** and **Maven**. Match the `paper.version` in `pom.xml` to your server (Paper 26.1.2).

```bash
cd /home/user/FrostStrengthSMP
mvn clean package
# -> target/FrostStrengthSMP-1.0.0.jar
```

Drop the jar into your Paper server's `plugins/` folder, restart, then edit `config.yml` and run `/strengthadmin reload`.

If Maven cannot reach `repo.papermc.io`, compile manually:

```bash
javac -cp path/to/paper-api.jar -d out $(find src -name "*.java")
jar cf FrostStrengthSMP.jar -C out . -C src/main/resources .
```

---

## Code Protection (Obfuscation)

To make the code hard to read/crack, run the built jar through **ProGuard**, **Allatori**, or **DashO** after `mvn package`. Example ProGuard config:

```
-keep class com.frostsmp.FrostStrengthSMP { public *; }
-keep class * implements com.frostsmp.api.StrengthAPI { public *; }
-keepnames class com.frostsmp.** { *; }
```

This renames internal classes/methods to meaningless identifiers while keeping plugin entry points working.

---

## Compatibility
- Built for **Paper 26.1.2** (MC 1.21.x).
- `api-version: 1.21` in `plugin.yml`.
- No client mods required — all custom items use server-side NBT tags, CustomModelData, and vanilla textures.
