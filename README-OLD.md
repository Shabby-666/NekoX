# NekoX - Cute Neko Plugin

## What is this?
NekoX is a fun Minecraft plugin that lets players become cute cat girls with special powers and ways to interact with others.

**Simply put:** This plugin adds cute cat girl characters to your Minecraft server!

## Quick Start (3 Easy Steps)

### Step 1: Download the Plugin
1. Download the `NekoX-5.0-ProMax+++.jar` file
2. Put this file in your server's `plugins` folder

### Step 2: Start the Server
1. Start your Minecraft server normally
2. The plugin will automatically create configuration files

### Step 3: Start Playing
1. In-game, use `/nekoset playername true` to turn a player into a cat girl
2. Cat girl players can now use all the fun features!

## What Can Cat Girls Do? (Super Fun Features)

### 🐱 Basic Powers
- **Night Vision**: Cat girls can see clearly in the dark
- **Jump Boost**: Can jump higher than normal players
- **Sneak Speed**: Move faster and quieter while sneaking

### 💬 Cute Chat
- Cat girl chat has special prefixes and suffixes
- Automatically changes normal words to cute cat girl talk
- For example, "hello" becomes "meow~ hello"

### 🍖 Special Diet
- Cat girls can only eat meat! No vegetables or fruits allowed
- Works with various meat-based foods

### 🐾 Claw Attacks
- Cat girls have special claw attacks
- Does extra damage to mobs
- Has cooldown time so you can't spam it

### 🛡️ Leather Armor Bonus
- Wearing leather armor gives speed bonuses
- Each piece of leather armor increases speed
- More armor pieces = more speed boost
- Maximum bonus level can be changed in settings

**Simply put:** Leather armor makes cat girls run faster!

### 👑 Owner System
- Cat girls can have owners
- Special interactions between owners and cat girls
- Can heal and help each other
- **Owner Death Effect**: When an owner dies, their cat girls also die (can be turned off)
  - Option to keep cat girl inventory and experience levels

### 🌙 Night Powers
- Cat girl abilities get stronger at night
- Strongest power at midnight
- Works automatically, no need to turn on

### 💪 Panic Mode
- When cat girl health gets very low
- Automatically gets super strength
- Lasts 1 minute to help escape danger

### ⚔️ Attack Boost
- Cat girls hit a little harder
- Knocks enemies back further
- Works with all weapon types
- Damage and knockback can be changed in settings

**Simply put:** Cat girls hit harder and send enemies flying!

### 🌿 Catnip Effects
- Cat girls can use catnip items (default: wheat seeds)
- Get temporary speed and jump boost
- Effects last for some time
- Can change the item in the settings file

### 🛡️ Damage Adjustment
- **No Fall Damage**: Cat girls don't get hurt from falling
- **More Other Damage**: Cat girls take 0.8x more damage from other sources
- Damage multiplier can be changed in settings

**Simply put:** Cat girls don't get hurt from falling, but get hurt more from other things!

### 🐾 Mob Scaring
- **Creeper Scaring**: Creepers won't explode near cat girls, they get pushed away
- **Phantom Scaring**: Phantoms won't attack cat girls, they get pushed away
- Works automatically, no need to control

**Simply put:** Creepers and phantoms are scared of cat girls and run away!

### 👁️ Mob Attention
- Hostile mobs notice cat girls from farther away
- Friendly mobs are more attracted to cat girls
- Affects different mob types in different ways
- Detection distance can be changed in settings

### 🔔 Player Nearby Alert
- Cat girls get title alerts when other players come within 25 blocks
- Shows names and distances of nearby players
- Can be turned on/off with `/playernotice` command
- Settings saved in SQLite database
- Helps cat girls know who's around

**Simply put:** Cat girls get alerts when players are nearby!

## 🌿 Catnip Detailed Usage Guide

### What is Catnip?
Catnip is a special power in NekoX that lets cat girl players use certain items to get temporary boosts.

### 🎯 How to Use

**Basic Steps:**
1. **Must be a Cat Girl** - Only players turned into cat girls can use catnip
2. **Hold Catnip Item** - By default, use wheat seeds
3. **Right-Click to Use** - Hold the catnip item and right-click to get the boost

**Step by Step:**
1. Make sure you're a cat girl (admin uses `/nekoset yourname true`)
2. Get some wheat seeds (or other catnip item you set)
3. Hold the wheat seeds and right-click
4. Immediately get speed and jump boost

### ⚡ Boost Details

**Catnip gives these temporary boosts:**
- **Speed Boost** - Move much faster
- **Jump Boost** - Jump much higher
- **Duration** - Lasts 60 seconds (can be changed)

**Boost Features:**
- Works right away
- Uses up one catnip item
- Has cooldown time between uses
- Only works for cat girl players

### ⚙️ Settings Options

You can change catnip settings in `config.yml`:

```
cat-nip:
  enabled: true           # Turn catnip on/off
  item: "WHEAT_SEEDS"     # Catnip item (can use other items)
  duration: 60            # How long boost lasts (seconds)
```

**Items You Can Use:**
- Default: `WHEAT_SEEDS` (wheat seeds)
- Can change to: `GRASS`, `FERN`, `VINE`, etc.
- Works with any Minecraft item ID

### 💡 Tips
1. **Fight Help** - Use during fights for speed and jump advantage
2. **Escape Tool** - Use when running from danger to get away fast
3. **Travel Boost** - Use when walking far to move faster
4. **Building Help** - Jump boost helps build high things
5. **Armor Choice** - Wear leather armor for maximum speed

### 🚫 Limits

- Only cat girl players can use
- Uses up catnip items
- Has cooldown time
- Must hold in main hand (off-hand won't work)

### 🔄 Works With Other Powers

- **Night Powers** - Catnip works better at night
- **Panic Mode** - Use with low health for better effect
- **Armor Bonus** - Speed bonus adds to leather armor effects

**Important:** Right-clicking with catnip in main hand uses 1 item, off-hand items won't work.

## Common Commands (Game Commands)

### Fun Commands (Everyone can use)
- `/pat player` - Gently pat someone
- `/lovebite player` - Give a cute love bite
- `/earscratch player` - Scratch someone's ears
- `/purr` - Make purring sounds
- `/hiss player` - Hiss at someone
- `/scratch player` - Scratch someone with claws
- `/attention player` - Get other players' attention

### Power Commands (Cat girls only)
- `/nightvision` - Turn night vision on/off
- `/jumpboost` - Turn jump boost on/off
- `/swiftsneak` - Turn sneak speed on/off
- `/health` - Heal yourself and owner
- `/myskills` - See all your skills
- `/playernotice [on|off]` - Turn player nearby alerts on/off

### Owner Commands
- `/owner add player` - Ask to become someone's owner
- `/owner accept player` - Accept an owner request
- `/owner deny player` - Reject an owner request
- `/owner remove player` - End owner relationship
- `/owner list` - See your owners
- `/owner mylist` - See your cat girls

### Admin Commands
- `/nekox reload` - Reload plugin settings
- `/nekoset player true/false` - Make player a cat girl or human
- `/nekox language language` - Change plugin language

## Configuration (Optional Settings)

The plugin creates a `config.yml` file the first time it runs. You can open it with Notepad to change settings:

### Basic Settings
- `neko-chat`: Turn cat girl chat effects on/off
- `meat-only`: Whether cat girls can only eat meat
- `owner-system`: Turn owner system on/off

### Power Settings
- `claws`: Claw attack damage and cooldown time
- `armor-bonus`: Leather armor speed bonus
- `night-effects`: When night powers start and end
- `health-skill`: Heal skill cooldown and cost

### Special Effects
- `cat-nip`: Catnip effects and duration
- `stress-effect`: When panic mode triggers
- `passive-attack-boost`: Attack damage bonus
- `armor-bonus`: Leather armor bonus settings
  - `enabled`: Turn armor bonus on/off
  - `leather-bonus`: List of leather armor types
  - `speed-bonus-per-piece`: Speed bonus per armor piece
- `neko-damage-modification`: Cat girl damage adjustment settings
  - `enabled`: Turn damage adjustment on/off
  - `fall-damage-immunity`: Immune to fall damage
  - `other-damage-multiplier`: Other damage increase multiplier
  - `debug`: Debug mode
- `neko-mob-behavior`: Cat girl mob behavior settings
  - `enabled`: Turn mob behavior on/off
  - `creeper-repulsion`: Creeper repulsion feature
  - `phantom-repulsion`: Phantom repulsion feature
  - `debug`: Debug mode
- `mob-targeting`: Mob targeting behavior settings
  - `enabled`: Turn targeting adjustment on/off
  - `distance-increase`: Detection distance increase multiplier
  - `friendly-attraction`: Turn friendly mob attraction on/off
- `owner-death`: Owner death effect settings
  - `feature.enabled`: Turn owner death effect on/off
  - `keep-inventory`: Keep cat girl inventory
  - `keep-level`: Keep cat girl experience level

## Developer API

NekoX provides a complete API for other plugins to work with cat girl features.

### How to Use API
To use the NekoX API in your plugin:

1. Add dependency in `plugin.yml`:
```yaml
depend: [NekoX]
```

2. Get API instance in your plugin:
```java
import org.cneko.nekox.api.NekoXAPI;

public class YourPlugin extends JavaPlugin {
    private NekoXAPI nekoXAPI;
    
    @Override
    public void onEnable() {
        nekoXAPI = NekoXAPI.getInstance(this);
        if (nekoXAPI == null) {
            getLogger().warning("Can't find NekoX plugin!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }
}
```

### Development Setup

Since NekoX isn't on JitPack or other public repos, you need to add it manually:

1. **Maven Setup:**
   - Create a `libs` folder in your project
   - Copy `NekoX-5.0-ProMax+++.jar` to your `libs` folder
   - Add this dependency in your `pom.xml`:
   
   ```xml
   <dependency>
       <groupId>org.cneko</groupId>
       <artifactId>NekoX</artifactId>
       <version>5.0-ProMax+++</version>
       <scope>system</scope>
       <systemPath>${project.basedir}/libs/NekoX-5.0-ProMax+++.jar</systemPath>
   </dependency>
   ```

2. **Gradle Setup:**
   - Create a `libs` folder in your project
   - Copy `NekoX-5.0-ProMax+++.jar` to your `libs` folder
   - Add this dependency in your `build.gradle`:
   
   ```gradle
   implementation files('libs/NekoX-5.0-ProMax+++.jar')
   ```

3. **Manual Setup (IDE):**
   - Create a `libs` folder in your project
   - Copy `NekoX-5.0-ProMax+++.jar` to your `libs` folder
   - In your IDE, add the jar file to your project's build path

### API Methods

#### Check Cat Girl Status
- `boolean isNeko(Player player)` - Check if player is a cat girl
- `boolean isNeko(String playerName)` - Check if player (by name) is a cat girl

#### Get Players
- `Set<String> getAllNekoNames()` - Get all cat girl player names (including offline)

#### Owner Relationship Management
- `Set<String> getOwnerNames(String nekoName)` - Get all owners of a cat girl
- `boolean isOwnerOf(String ownerName, String nekoName)` - Check if player is owner of cat girl
- `boolean hasOwner(String playerName)` - Check if player has an owner

### Events

NekoX provides custom events for other plugins to listen to:

#### NekoStatusChangeEvent
Triggered when player's cat girl status changes:
```java
@EventHandler
public void onNekoStatusChange(NekoStatusChangeEvent event) {
    Player player = event.getPlayer();
    boolean isNeko = event.isNeko();
    boolean isOnline = event.isOnline();
    // Handle status change
}
```

#### OwnerRelationshipEvent
Triggered when owner relationship is added, removed, or requested:
```java
@EventHandler
public void onOwnerRelationshipChange(OwnerRelationshipEvent event) {
    String nekoName = event.getNekoName();
    String ownerName = event.getOwnerName();
    OwnerRelationshipEvent.RelationshipAction action = event.getAction();
    
    switch (action) {
        case ADD:
            // Handle relationship added
            break;
        case REMOVE:
            // Handle relationship removed
            break;
        case REQUEST:
            // Handle relationship requested
            break;
    }
}
```

See [DEVELOPER-WIKI.md](DEVELOPER-WIKI.md) for detailed API docs and examples.

## 🔧 Developer Docs

**💡 Tip: If you're just a regular user with no programming knowledge, you can skip this part!**

### Maven Configuration

If you want to develop plugins that work with NekoX, here's the Maven config you need:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>your.group.id</groupId>
    <artifactId>YourPlugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    
    <name>YourPlugin</name>
    
    <properties>
        <java.version>17</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <repositories>
        <!-- PaperMC repo -->
        <repository>
            <id>papermc-repo</id>
            <url>https://repo.papermc.io/repository/maven-public/</url>
        </repository>
    </repositories>
    
    <dependencies>
        <!-- Paper API -->
        <dependency>
            <groupId>io.papermc.paper</groupId>
            <artifactId>paper-api</artifactId>
            <version>1.20.4-R0.1-SNAPSHOT</version>
            <scope>provided</scope>
        </dependency>
        
        <!-- NekoX API -->
        <!-- Put NekoX plugin jar file in project's libs folder -->
        <dependency>
            <groupId>org.cneko</groupId>
            <artifactId>NekoX</artifactId>
            <version>5.0-ProMax+++</version>
            <scope>system</scope>
            <systemPath>${project.basedir}/libs/NekoX-5.0-ProMax+++.jar</systemPath>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### How to Build Plugin (For developers who want to build the plugin themselves)

If you want to compile the plugin file yourself, follow these steps:

#### For Minecraft 1.20.4 (default version)
Open command line and type:
```bash
mvn clean install
```

#### For Minecraft 1.21.4
**Important: You need to install Java 21 on your computer first**
Open command line and type:
```bash
mvn clean install -Ppaper-1.21.4
```

#### Current Limitations
- If your computer uses Java 17, you can only compile the 1.20.4 version
- To compile the 1.21.4 version, you need to install Java 21 first

### PlaceholderAPI Support

NekoX integrates with PlaceholderAPI and provides these placeholders (requires PlaceholderAPI plugin):

| Placeholder | Description |
|-------------|-------------|
| `%nekox_is_neko%` | Check if player is a cat girl |
| `%nekox_humans%` | Get list of non-cat girl players |
| `%nekox_nekos%` | Get list of cat girl players |

Use `/nekox placeholders` command to see all available placeholders and their status.

### Damage Adjustment Technical Implementation

#### Core Classes
- <mcfile name="NekoDamageListener.java" path="src\main\java\org\cneko\nekox\events\NekoDamageListener.java"></mcfile>: Damage handling listener

#### Implementation Logic
1. Listen to `EntityDamageEvent` event
2. Check if damaged entity is a cat girl
3. If it's fall damage (`FALL` enum), cancel the damage event
4. If it's other damage, increase damage by config multiplier

#### Test Methods
- **Fall Test**: Cat girls don't get hurt from falling, normal players do
- **Damage Test**: Cat girls take 0.8x more damage when attacked
- **Config Test**: Modify config file to verify effects

### Mob Repulsion Technical Implementation

#### Core Classes
- <mcfile name="NekoMobBehaviorListener.java" path="src\main\java\org\cneko\nekox\events\NekoMobBehaviorListener.java"></mcfile>: Mob behavior listener

#### Implementation Logic
- **Creeper Repulsion**: Listen to `ExplosionPrimeEvent`, cancel explosion and apply reverse force
- **Phantom Repulsion**: Listen to `EntityTargetLivingEntityEvent`, cancel attack and apply reverse force
- Distance check: Works within 4 blocks
- Config driven, features can be turned on/off separately

#### Test Methods
- **Creeper Test**: Creepers near cat girls don't explode, normal players do
- **Phantom Test**: Cat girls aren't attacked by phantoms at night, normal players are
- **Config Test**: Modify config file to verify repulsion effects

## Common Questions

### Q: Which Minecraft versions does the plugin support?
A: Supports 1.20.4 and 1.21.4 versions

### Q: How to turn a player into a cat girl?
A: Admin types `/nekoset playername true`

### Q: What can cat girls do that normal players can't?
A: Night vision, jump boost, sneak speed, special chat, claw attacks, etc.

### Q: What's the purpose of the owner system?
A: Cat girls can have owners, they can heal each other and have special interactions

### Q: How to modify plugin settings?
A: Edit `plugins/NekoX/config.yml` file

### Q: Is Chinese language supported?
A: Yes! Use `/nekox language chinese` to switch to Chinese

## Technical Support

If you encounter problems:
1. Check if Minecraft version matches
2. Confirm plugin file is in correct location
3. Check error messages in server logs
4. Contact plugin developer for help

## Tips
- Cat girls are stronger at night, try to be active during nighttime
- Leather armor can give cat girls speed bonus
- Catnip gives temporary speed and jump boost
- Low health automatically triggers stress response for extra power

---

**Enjoy your cat girl adventure! 🐾**