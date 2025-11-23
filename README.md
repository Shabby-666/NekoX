# NekoX - Cute Neko Plugin

## What is this?

NekoX is a fun Minecraft plugin that makes Minecraft more interesting! It allows players to become cute neko girls with special abilities and interactive features.

**Simply put:** This plugin adds cute neko characters to your server!

## Quick Start (3 Easy Steps)

### Step 1: Download the Plugin

1. Download the `NekoX-5.0-ProMax+++.jar` file
2. Place this file in your server's `plugins` folder

### Step 2: Start the Server

1. Start your Minecraft server normally
2. The plugin will automatically create configuration files

### Step 3: Start Playing

1. In-game, type `/nekoset playername true` to turn a player into a neko
2. Neko players can now use all the fun features!

## What Can Nekos Do? (Awesome Features)

### 🐱 Basic Abilities

- **Night Vision**: Nekos can see clearly in the dark
- **Jump Boost**: Can jump higher
- **Sneak Speed**: Move faster and quieter while sneaking

### 💬 Cute Chat

- Neko chat has special prefixes and suffixes
- Automatically converts normal words into cute neko language
- For example, "hello" might become "meow~ hello"

### 🍖 Special Diet

- Nekos can only eat meat! No vegetables or fruits
- Supports various meat-based foods

### 🐾 Claw Attacks

- Nekos have special claw attacks
- Extra damage to mobs
- Has cooldown time, cannot be used continuously

### 🛡️ Leather Armor Bonus

- Wearing leather armor provides speed bonuses
- Each piece of leather armor increases speed
- Multiple armor pieces stack effects
- Maximum bonus level is configurable

**Simply put:** Leather armor makes nekos run faster!

### 👑 Owner System

- Nekos can have owners
- Special interactions between owners and nekos
- Can heal and help each other
- **Owner Death Effect**: When an owner dies, their nekos also die (configurable)
  - Option to keep neko inventory and experience level

### 🌙 Night Abilities

- Neko abilities become stronger at night
- Peak power at midnight
- Automatically activates, no manual control needed

### 💪 Stress Response

- When neko health is very low
- Automatically gains super strength
- Lasts 1 minute, helps escape danger

### ⚔️ Passive Attack Boost

- Nekos deal slightly higher damage when attacking
- Stronger knockback effect against enemies
- Applies to all weapon types
- Damage and knockback multiplier configurable

**Simply put:** Neko attacks are more powerful and can knock enemies further!

### 🌿 Catnip Effects

- Nekos use catnip items (default uses wheat seeds)
- Gain temporary speed and jump boost
- Effects last for a period of time
- Can modify the items used in configuration file

### 🛡️ Damage Adjustment

- **Fall Damage Immunity**: Nekos don't take damage from falling from heights
- **Other Damage Boost**: Nekos take 0.8x more damage from other sources
- Can adjust damage multiplier in configuration file

**Simply put:** Nekos are immune to falling but more vulnerable to attacks!

### 🐾 Mob Repulsion

- **Creeper Repulsion**: Creepers won't explode when encountering nekos, but are repelled
- **Phantom Repulsion**: Phantoms won't attack nekos, but are repelled
- Automatically activates, no manual operation needed

**Simply put:** Creepers and phantoms are afraid of nekos and will automatically avoid them!

### 👁️ Mob Targeting Behavior

- Hostile mobs have increased detection distance for nekos
- Friendly mobs are more attracted to nekos
- Different effects on different types of mobs
- Detection parameters configurable

### 🔔 Player Proximity Notification

- When other players enter a 25-block radius around a neko, the neko receives a title notification
- Shows the names and distances of nearby players
- Can enable/disable the feature with the `/playernotice` command
- Configuration information stored in SQLite database
- Helps nekos be aware of their surroundings

**Simply put:** Nekos receive notifications about nearby players!

## 🌿 Catnip Detailed Usage Guide

### What is Catnip?

Catnip is a special feature in the NekoX plugin that allows neko players to use specific items to gain temporary buff effects.

### 🎯 Usage Method

**Basic Operation:**
1. **Must be a Neko Player** - Only players set as nekos can use catnip
2. **Hold Catnip Item** - Default uses wheat seeds (WHEAT_SEEDS)
3. **Right-Click to Use** - Hold the catnip item and right-click to activate the effect

**Specific Steps:**
1. Ensure you are a neko (admin uses `/nekoset yourname true` to set)
2. Obtain wheat seeds (or other configured catnip items)
3. Hold wheat seeds and right-click
4. Immediately gain speed and jump boost effects

### ⚡ Effect Details

**Catnip provides the following buff effects:**
- **Speed Boost** (Speed II) - Significantly increased movement speed
- **Jump Boost** (Jump Boost II) - Greatly enhanced jump height
- **Duration** - Default 60 seconds (can be modified in configuration)

**Effect Characteristics:**
- Effects activate immediately
- Consumes one catnip item
- Has cooldown time, cannot be used continuously
- Only effective for neko players

### ⚙️ Configuration Options

Customize catnip settings in `config.yml`:

```yaml
cat-nip:
  enabled: true           # Enable/disable catnip feature
  item: "WHEAT_SEEDS"     # Catnip item ID (can be changed to other items)
  duration: 60            # Effect duration (seconds)
```

**Modifiable Item Options:**
- Default: `WHEAT_SEEDS` (wheat seeds)
- Can be changed to: `GRASS` (grass), `FERN` (fern), `VINE` (vine), etc.
- Supports any Minecraft item ID

### 💡 Usage Tips

1. **Combat Assistance** - Use during PVP or mob fighting to gain speed and jump advantage
2. **Escape Tool** - Use catnip to quickly escape danger when being chased
3. **Exploration Boost** - Use during long-distance travel to improve movement efficiency
4. **Building Aid** - Jump boost helps with building at heights
5. **Armor Selection** - Wear leather armor to get maximum speed bonus

### 🚫 Limitations

- Only neko players can use
- Requires consumption of catnip items
- Has usage cooldown
- Cannot be used with off-hand items (must be held in main hand)

### 🔄 Synergy with Other Features

- **Night Effects** - Catnip effects work better at night
- **Stress Response** - Better effect when combining with catnip when health is low
- **Armor Bonus** - Speed bonus stacks with leather armor effects

**Important Note:** Right-clicking with catnip in main hand consumes 1 catnip item, off-hand items won't trigger the effect.

## Common Commands (In-Game Usage)

### Interaction Commands (Everyone can use)

- `/pat player` - Gently pat someone
- `/lovebite player` - Give a cute love bite
- `/earscratch player` - Scratch someone's ears
- `/purr` - Make purring sounds
- `/hiss player` - Hiss at someone
- `/scratch player` - Scratch someone with claws
- `/attention player` - Attract other players' attention

### Ability Commands (Nekos only)

- `/nightvision` - Enable night vision
- `/jumpboost` - Jump boost
- `/swiftsneak` - Sneak speed
- `/health` - Heal yourself and owner
- `/myskills` - View all skills
- `/playernotice [on|off]` - Enable/disable player proximity notification

### Owner System Commands

- `/owner add player` - Request to become someone's owner
- `/owner accept player` - Accept owner request
- `/owner deny player` - Deny owner request
- `/owner remove player` - Remove owner relationship
- `/owner list` - View your owners
- `/owner mylist` - View your nekos

### Admin Commands

- `/nekox reload` - Reload plugin settings
- `/nekoset player true/false` - Set player as neko
- `/nekox language language` - Switch plugin language

## Configuration (Optional Settings)

The plugin creates a `config.yml` file on first run, which you can open and modify with a text editor:

### Basic Settings

- `neko-chat`: Enable/disable neko chat effects
- `meat-only`: Whether nekos can only eat meat
- `owner-system`: Enable/disable owner system

### Ability Adjustments

- `claws`: Claw attack damage and cooldown time
- `armor-bonus`: Speed bonus from leather armor
- `night-effects`: Night ability start and end times
- `health-skill`: Healing skill cooldown and cost

### Special Effects

- `cat-nip`: Catnip effects and duration
- `stress-effect`: Stress response trigger conditions
- `passive-attack-boost`: Attack damage bonus
- `armor-bonus`: Leather armor bonus settings
  - `enabled`: Enable/disable armor bonus
  - `leather-bonus`: Leather armor type list
  - `speed-bonus-per-piece`: Speed bonus per armor piece
- `neko-damage-modification`: Neko damage adjustment settings
  - `enabled`: Enable/disable damage adjustment feature
  - `fall-damage-immunity`: Fall damage immunity
  - `other-damage-multiplier`: Other damage increase multiplier
  - `debug`: Debug mode
- `neko-mob-behavior`: Neko mob behavior settings
  - `enabled`: Enable/disable mob behavior feature
  - `creeper-repulsion`: Creeper repulsion feature
  - `phantom-repulsion`: Phantom repulsion feature
  - `debug`: Debug mode
- `mob-targeting`: Mob targeting behavior settings
  - `enabled`: Enable/disable targeting behavior adjustment
  - `distance-increase`: Detection distance increase multiplier
  - `friendly-attraction`: Enable/disable friendly mob attraction
- `owner-death`: Owner death effect settings
  - `feature.enabled`: Enable/disable owner death effect
  - `keep-inventory`: Keep neko inventory
  - `keep-level`: Keep neko experience level

## Developer API

NekoX provides a comprehensive API for other plugins to integrate neko functionality.

### API Access

Using NekoX API in your plugin:

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
            getLogger().warning("NekoX plugin not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }
}
```

### Development Environment Setup

Since NekoX is not published to JitPack or other public repositories, you need to manually add it to your project:

1. **Maven Project Setup:**
   - Create a `libs` folder in your project directory
   - Copy the `NekoX-5.0-ProMax+++.jar` file from this plugin's target folder to your `libs` folder
   - Add the following dependency to your `pom.xml`:
   
   ```xml
   <dependency>
       <groupId>org.cneko</groupId>
       <artifactId>NekoX</artifactId>
       <version>5.0-ProMax+++</version>
       <scope>system</scope>
       <systemPath>${project.basedir}/libs/NekoX-5.0-ProMax+++.jar</systemPath>
   </dependency>
   ```

2. **Gradle Project Setup:**
   - Create a `libs` folder in your project directory
   - Copy the `NekoX-5.0-ProMax+++.jar` file to your `libs` folder
   - Add the following dependency to your `build.gradle`:
   
   ```gradle
   implementation files('libs/NekoX-5.0-ProMax+++.jar')
   ```

3. **Manual Setup (IDE):**
   - Create a `libs` folder in your project directory
   - Copy the `NekoX-5.0-ProMax+++.jar` file to your `libs` folder
   - Add the jar file to your project's build path in your IDE

### API Methods

#### Neko Status Check

- `boolean isNeko(Player player)` - Check if player is a neko
- `boolean isNeko(String playerName)` - Check if player (by name) is a neko

#### Player Retrieval

- `Set<String> getAllNekoNames()` - Get all neko player names (including offline)

#### Owner Relationship Management

- `Set<String> getOwnerNames(String nekoName)` - Get all owners of a neko
- `boolean isOwnerOf(String ownerName, String nekoName)` - Check if player is owner of a neko
- `boolean hasOwner(String playerName)` - Check if player has an owner

### Events

NekoX provides custom events for other plugins to listen to:

#### NekoStatusChangeEvent

Triggered when a player's neko status changes:
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

Triggered when owner relationships are added, removed, or requested:
```java
@EventHandler
public void onOwnerRelationshipChange(OwnerRelationshipEvent event) {
    String nekoName = event.getNekoName();
    String ownerName = event.getOwnerName();
    OwnerRelationshipEvent.RelationshipAction action = event.getAction();
    
    switch (action) {
        case ADD:
            // Handle relationship addition
            break;
        case REMOVE:
            // Handle relationship removal
            break;
        case REQUEST:
            // Handle relationship request
            break;
    }
}
```

## 🔧 Developer Documentation

**💡 Friendly Reminder: If you're just a regular user without programming knowledge, you can skip this section!**

### Maven Configuration

If you want to develop plugins that integrate with NekoX, here's the Maven configuration you need:

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
        <!-- PaperMC Repository -->
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
        <!-- Place the NekoX plugin jar file in your project's libs folder -->
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

### How to Compile the Plugin (For Developers Who Want to Build the Plugin)

If you want to compile the plugin file yourself, you can follow these steps:

#### For Minecraft 1.20.4 (Default Version)

Open command line and enter:
```bash
mvn clean install
```

#### For Minecraft 1.21.4

**Important Note: You need to install Java 21 on your computer first**
Open command line and enter:
```bash
mvn clean install -Ppaper-1.21.4
```

#### Current Limitations

- If your computer uses Java 17, you can only compile the 1.20.4 version
- To compile the 1.21.4 version, you need to install Java 21 first

### PlaceholderAPI Support

NekoX integrates with PlaceholderAPI and provides the following placeholders (requires PlaceholderAPI plugin to be installed):

| Placeholder | Description |
|-------------|-------------|
| `%nekox_is_neko%` | Check if player is a neko |
| `%nekox_humans%` | Get list of non-neko players |
| `%nekox_nekos%` | Get list of neko players |

Use the `/nekox placeholders` command to view all available placeholders and their status.

### Technical Implementation of Damage Adjustment Feature

#### Core Classes

- <mcfile name="NekoDamageListener.java" path="src\main\java\org\cneko\nekox\events\NekoDamageListener.java"></mcfile>: Damage handling listener

#### Implementation Logic

1. Listen for `EntityDamageEvent` events
2. Check if the damaged entity is a neko
3. If it's fall damage (`FALL` enum), cancel the damage event
4. If it's other damage, increase the damage by the configured multiplier

#### Testing Methods

- **Fall Test**: Nekos don't take damage when falling from heights, regular players take normal damage
- **Damage Test**: Nekos take 0.8x more damage when attacked
- **Configuration Test**: Modify configuration file to verify effects

### Technical Implementation of Mob Repulsion Feature

#### Core Classes

- <mcfile name="NekoMobBehaviorListener.java" path="src\main\java\org\cneko\nekox\events\NekoMobBehaviorListener.java"></mcfile>: Mob behavior listener

#### Implementation Logic

- **Creeper Repulsion**: Listen for `ExplosionPrimeEvent`, cancel explosion and apply reverse thrust
- **Phantom Repulsion**: Listen for `EntityTargetLivingEntityEvent`, cancel attack and apply reverse thrust
- Distance judgment: Effective within 4 blocks
- Configuration-driven, features can be toggled on/off individually

#### Testing Methods

- **Creeper Test**: Creepers won't explode near nekos, regular players experience normal explosions
- **Phantom Test**: Nekos aren't attacked by phantoms at night, regular players are attacked normally
- **Configuration Test**: Modify configuration file to verify repulsion effects

## Frequently Asked Questions

### Q: Which Minecraft versions are supported?
A: Supports 1.20.4 and 1.21.4 versions

### Q: How to turn a player into a neko?
A: Admin types `/nekoset playername true`

### Q: What can nekos do that regular players can't?
A: Night vision, jump boost, sneak speed, special chat, claw attacks, etc.

### Q: What's the purpose of the owner system?
A: Nekos can have owners, owners and nekos can heal each other and have special interactions

### Q: How to modify plugin settings?
A: Edit the `plugins/NekoX/config.yml` file

### Q: Is Chinese supported?
A: Yes! Type `/nekox language chinese` to switch to Chinese

## Technical Support

If you encounter problems:
1. Check if Minecraft version matches
2. Confirm plugin file is in correct location
3. Check error messages in server logs
4. Contact plugin developer for help

## Tips

- Nekos are stronger at night, try to be active during nighttime
- Leather armor provides speed bonus to nekos
- Catnip gives temporary speed and jump boost to nekos
- Low health automatically triggers stress response for extra power

---

# NekoX Developer Wiki

## Table of Contents

1. [Introduction](#introduction)
2. [Build Configuration](#build-configuration)
3. [API Access](#api-access)
4. [API Methods](#api-methods)
5. [Usage Examples](#usage-examples)
6. [Event System](#event-system)
7. [Data Storage](#data-storage)

## Introduction

NekoX is a feature-rich Minecraft neko plugin that adds neko characters and related functionality to servers. This developer Wiki aims to help other plugin developers understand how to integrate with the NekoX plugin.

## Build Configuration

### Maven Configuration

If you use Maven to build your plugin, add the following configuration to your pom.xml file:

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
        <!-- PaperMC Repository -->
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
        <!-- Place the NekoX plugin jar file in your project's libs folder -->
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

### Gradle Configuration

If you use Gradle to build your plugin, add the following configuration to your build.gradle file:

```gradle
plugins {
    id 'java'
}

group = 'your.group.id'
version = '1.0-SNAPSHOT'

repositories {
    // PaperMC Repository
    maven {
        name = 'papermc'
        url = 'https://repo.papermc.io/repository/maven-public/'
    }
    
    mavenCentral()
}

dependencies {
    // Paper API
    compileOnly 'io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT'
    
    // NekoX API
    // Place the NekoX plugin jar file in your project's libs folder
    implementation files('libs/NekoX-5.0-ProMax+++.jar')
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.withType(JavaCompile) {
    options.encoding = 'UTF-8'
}
```

### Manual Configuration

If you don't use build tools, you can directly add the NekoX plugin jar file to your project dependencies:

1. Copy the NekoX-5.0-ProMax+++.jar file from the NekoX project's target folder
2. Create a libs folder in your project
3. Put the NekoX-5.0-ProMax+++.jar file in the libs folder
4. Add the jar file to your project's build path in your IDE
5. Make sure to add NekoX as a dependency in plugin.yml

### Build and Deployment

After building your plugin, ensure:
1. Place the generated jar file in the server's plugins folder
2. Ensure the NekoX plugin is also installed
3. Restart the server to load all plugins

### Multi-Version Support

NekoX supports multiple Minecraft versions:
- For Minecraft 1.20.4, use Java 17
- For Minecraft 1.21.4, use Java 21

If you need to build a specific version, you can use Maven profiles:
```bash
# Build 1.20.4 version (default)
mvn clean install

# Build 1.21.4 version
mvn clean install -Ppaper-1.21.4
```

## API Access

### 1. Adding Dependencies

To use the NekoX API in your plugin, first you need to get the API instance:

```java
import org.cneko.nekox.api.NekoXAPI;

// In your plugin main class
public class YourPlugin extends JavaPlugin {
    private NekoXAPI nekoXAPI;
    
    @Override
    public void onEnable() {
        // Get NekoX API instance
        nekoXAPI = NekoXAPI.getInstance(this);
        if (nekoXAPI == null) {
            getLogger().warning("NekoX plugin not found, please ensure NekoX is installed and enabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        getLogger().info("Successfully connected to NekoX API!");
    }
}
```

### 2. plugin.yml Configuration

Add NekoX as a dependency in your plugin.yml file:

```yaml
name: YourPlugin
version: 1.0
main: your.package.YourPlugin
api-version: 1.13
depend: [NekoX]
```

## API Methods

### Neko Check Methods

#### `boolean isNeko(Player player)`

Check if specified player is a neko

**Parameters**: 
- `player`: Player to check

**Return Value**: 
- `true`: Player is a neko
- `false`: Player is not a neko

#### `boolean isNeko(String playerName)`

Check if specified player name is a neko (supports offline players)

**Parameters**: 
- `playerName`: Player name

**Return Value**: 
- `true`: Player is a neko
- `false`: Player is not a neko

### Player Retrieval Methods

#### `Set<String> getAllNekoNames()`

Get all neko player names (including offline players)

**Return Value**: 
- Set containing all neko player names

### Relationship Query Methods

#### `Set<String> getOwnerNames(String nekoName)`

Get all owner names of specified neko

**Parameters**: 
- `nekoName`: Neko player name

**Return Value**: 
- Set containing all owner player names

#### `boolean isOwnerOf(String ownerName, String nekoName)`

Check if specified player is owner of a neko

**Parameters**: 
- `ownerName`: Owner player name
- `nekoName`: Neko player name

**Return Value**: 
- `true`: Is owner
- `false`: Is not owner

#### `boolean hasOwner(String playerName)`

Check if specified player has an owner

**Parameters**: 
- `playerName`: Player name

**Return Value**: 
- `true`: Has owner
- `false`: Has no owner

## Usage Examples

### Example 1: Check if Player is Neko

```java
Player player = ...; // Get player instance
if (nekoXAPI.isNeko(player)) {
    player.sendMessage("You are a cute neko!");
} else {
    player.sendMessage("You are not a neko~");
}
```

### Example 2: Get All Neko Player Names

```java
Set<String> nekoNames = nekoXAPI.getAllNekoNames();
for (String nekoName : nekoNames) {
    // Process each neko player name
    getLogger().info("Neko: " + nekoName);
}
```

### Example 3: Check Owner Relationship

```java
String ownerName = ...; // Owner player name
String nekoName = ...;  // Neko player name

if (nekoXAPI.isOwnerOf(ownerName, nekoName)) {
    getLogger().info(ownerName + " is the owner of " + nekoName + "!");
}
```

### Example 4: Get All Owners of a Neko

```java
String nekoName = ...; // Neko player name
Set<String> owners = nekoXAPI.getOwnerNames(nekoName);

if (owners.isEmpty()) {
    getLogger().info(nekoName + " doesn't have an owner yet~");
} else {
    StringBuilder message = new StringBuilder(nekoName + "'s owners: ");
    for (String owner : owners) {
        message.append(owner).append(", ");
    }
    getLogger().info(message.toString());
}
```

## Event System

NekoX provides some custom events that other plugins can listen to in order to respond to neko-related behaviors.

### Available Events

#### NekoStatusChangeEvent

Triggered when a player's neko status changes

**Event Class**: `org.cneko.nekox.api.events.NekoStatusChangeEvent`
**Trigger Timing**: When a player is set as neko or neko status is cancelled

**Usage Example**:
```java
@EventHandler
public void onNekoStatusChange(NekoStatusChangeEvent event) {
    Player player = event.getPlayer();
    boolean isNeko = event.isNeko();
    boolean isOnline = event.isOnline();
    
    if (isNeko) {
        getLogger().info(player.getName() + " became a neko!");
    } else {
        getLogger().info(player.getName() + " is no longer a neko!");
    }
}
```

#### OwnerRelationshipEvent

Triggered when owner relationship changes

**Event Class**: `org.cneko.nekox.api.events.OwnerRelationshipEvent`
**Trigger Timing**: When adding, removing, or requesting owner relationship

**Usage Example**:
```java
@EventHandler
public void onOwnerRelationshipChange(OwnerRelationshipEvent event) {
    String nekoName = event.getNekoName();
    String ownerName = event.getOwnerName();
    OwnerRelationshipEvent.RelationshipAction action = event.getAction();
    
    switch (action) {
        case ADD:
            getLogger().info(ownerName + " became the owner of " + nekoName + "!");
            break;
        case REMOVE:
            getLogger().info(ownerName + " is no longer the owner of " + nekoName + "!");
            break;
        case REQUEST:
            getLogger().info(ownerName + " requested to become the owner of " + nekoName + "!");
            break;
    }
}
```

## Data Storage

NekoX uses SQLite database to store player data, including:
- Whether player is a neko
- Neko-owner relationships
- Player personalization settings

### Database Structure

#### player_configs Table

Store player basic configuration
- `player_name`: Player name (primary key)
- `notice_enabled`: Whether player proximity notification is enabled
- `is_neko`: Whether is a neko

#### neko_owners Table

Store neko-owner relationships
- `neko_name`: Neko name
- `owner_name`: Owner name

#### owner_requests Table

Store owner request relationships
- `requester_name`: Requester name
- `neko_name`: Requested neko name

### Notes

1. All database operations are executed asynchronously and won't block the main thread
2. It's recommended to access data through API methods rather than directly operating the database
3. Player data uses player name as identifier and supports offline player queries

## Best Practices

### Performance Optimization

1. Cache API instances to avoid repeated retrieval
2. Use asynchronous operations appropriately to avoid executing time-consuming tasks on the main thread
3. Release resources in a timely manner, especially when the plugin is disabled

### Error Handling

1. Always check if API instance is null
2. Handle possible exception cases, such as player not being online
3. Provide friendly error messages

### Compatibility

1. Check if NekoX plugin exists and is enabled
2. Adapt to different versions of NekoX API
3. Provide fallback solutions and alternative logic when NekoX is unavailable

## Support and Feedback

If you encounter problems or have improvement suggestions while using the NekoX API, please contact us through the following methods:
- GitHub Issues: [https://github.com/Shabby-666/NekoX/issues](https://github.com/Shabby-666/NekoX/issues)
- QQ: 959612591

Thank you for using NekoX!

---

# NekoX - 可爱的猫娘插件

## 这是什么？

NekoX是一个让Minecraft变得更有趣的插件！它可以让玩家变成可爱的猫娘，拥有特殊能力和互动方式。

**简单来说：** 这个插件让你的服务器里出现可爱的猫娘角色！

## 快速开始（3步搞定）

### 第1步：下载插件

1. 下载 `NekoX-5.0-ProMax+++.jar` 文件
2. 把这个文件放到你服务器的 `plugins` 文件夹里

### 第2步：启动服务器

1. 正常启动你的Minecraft服务器
2. 插件会自动创建配置文件

### 第3步：开始使用

1. 在游戏里输入 `/nekoset 玩家名字 true` 把玩家变成猫娘
2. 猫娘玩家就可以使用各种有趣的功能啦！

## 猫娘能做什么？（超有趣的功能）

### 🐱 基础能力

- **夜视能力**：猫娘在黑暗中也能看清楚
- **跳跃增强**：可以跳得更高
- **潜行加速**：潜行时移动更快更安静

### 💬 可爱聊天

- 猫娘聊天会有特殊的前缀和后缀
- 自动把普通词语变成可爱的猫娘用语
- 比如"你好"可能变成"喵~你好"

### 🍖 特殊饮食

- 猫娘只能吃肉！不能吃蔬菜水果
- 支持各种肉类食物

### 🐾 爪子攻击

- 猫娘有特殊的爪子攻击
- 对生物造成额外伤害
- 有冷却时间，不能连续使用

### 🛡️ 皮革护甲加成

- 穿着皮革护甲可以获得速度加成
- 每件皮革护甲都能增加速度
- 多件护甲效果叠加
- 最大加成等级可配置

**简单来说：** 皮革护甲让猫娘跑得更快！

### 👑 主人系统

- 猫娘可以认主人
- 主人和猫娘有特殊互动
- 可以互相治疗和帮助
- **主人死亡效应**：当主人死亡时，其猫娘也会死亡（可配置）
  - 可选择是否保留猫娘的物品栏和经验等级

### 🌙 夜间能力

- 晚上猫娘能力会变强
- 午夜时能力达到最强
- 自动生效，不用手动开启

### 💪 应激反应

- 当猫娘生命值很低时
- 会自动获得超强力量
- 持续1分钟，帮助脱离危险

### ⚔️ 被动攻击增强

- 猫娘攻击时造成略微更高的伤害
- 对敌人有更强的击退效果
- 适用于所有武器类型
- 伤害和击退倍数可配置

**简单来说：** 猫娘的攻击更有力，能把敌人打得更远！

### 🌿 猫薄荷效果

- 猫娘使用猫薄荷物品（默认使用小麦种子）
- 获得临时速度和跳跃提升
- 效果持续一段时间
- 可以在配置文件中修改使用的物品

### 🛡️ 伤害调整

- **免疫跌落伤害**：猫娘从高处跌落不会受到伤害
- **其他伤害增强**：猫娘受到的其他伤害增加0.8倍
- 可以在配置文件中调整伤害倍数

**简单来说：** 猫娘不怕摔，但更容易受伤！

### 🐾 生物驱赶

- **苦力怕驱赶**：苦力怕遇到猫娘不会爆炸，而是被驱赶
- **幻翼驱赶**：幻翼不会攻击猫娘，而是被驱赶
- 自动生效，无需手动操作

**简单来说：** 苦力怕和幻翼都怕猫娘，会自动躲开！

### 👁️ 生物目标行为

- 敌对生物对猫娘的检测距离增加
- 友好生物更容易被猫娘吸引
- 对不同类型的生物有不同影响
- 检测参数可配置

### 🔔 玩家接近提醒

- 当其他玩家进入猫娘25格范围内时，猫娘会收到标题通知
- 显示附近玩家的名称和距离
- 可通过`/playernotice`命令开启/关闭功能
- 配置信息存储在SQLite数据库中
- 帮助猫娘了解周围环境

**简单来说：** 猫娘会收到附近玩家的通知！

## 🌿 猫薄荷详细使用指南

### 什么是猫薄荷？

猫薄荷是NekoX插件中的一个特殊功能，让猫娘玩家使用特定物品获得临时增益效果。

### 🎯 使用方法

**基本操作：**

1. **必须是猫娘玩家** - 只有被设置为猫娘的玩家才能使用猫薄荷
2. **手持猫薄荷物品** - 默认使用小麦种子（WHEAT_SEEDS）
3. **右键使用** - 手持猫薄荷物品右键点击即可激活效果

**具体步骤：**

1. 确保你是猫娘（管理员使用 `/nekoset 你的名字 true` 设置）
2. 获取小麦种子（或其他配置的猫薄荷物品）
3. 手持小麦种子右键点击
4. 立即获得速度和跳跃提升效果

### ⚡ 效果详情

**猫薄荷提供以下增益效果：**

- **速度提升** (Speed II) - 移动速度大幅增加
- **跳跃提升** (Jump Boost II) - 跳跃高度显著提升
- **持续时间** - 默认60秒（可在配置中修改）

**效果特点：**

- 效果立即生效
- 消耗一个猫薄荷物品
- 有冷却时间，不能连续使用
- 只对猫娘玩家有效

### ⚙️ 配置选项

在 `config.yml` 中可以自定义猫薄荷设置：

```yaml
cat-nip:
  enabled: true           # 是否启用猫薄荷功能
  item: "WHEAT_SEEDS"     # 猫薄荷物品ID（可修改为其他物品）
  duration: 60            # 效果持续时间（秒）
```

**可修改的物品选项：**

- 默认：`WHEAT_SEEDS`（小麦种子）
- 可改为：`GRASS`（草）、`FERN`（蕨类）、`VINE`（藤蔓）等
- 支持任何Minecraft物品ID

### 💡 使用技巧

1. **战斗辅助** - 在PVP或打怪时使用，获得速度和跳跃优势
2. **逃跑利器** - 被追击时使用猫薄荷快速脱离危险
3. **探索加速** - 长途旅行时使用，提高移动效率
4. **建筑辅助** - 跳跃提升有助于搭建高处建筑
5. **护甲选择** - 穿皮革护甲可获得最大速度加成

### 🚫 限制条件

- 只有猫娘玩家可以使用
- 需要消耗猫薄荷物品
- 有使用冷却时间
- 副手持物时无法使用（必须主手持物）

### 🔄 与其他功能的配合

- **夜间效果** - 晚上使用猫薄荷效果更佳
- **应激反应** - 生命值低时配合猫薄荷效果更好
- **护甲加成** - 速度加成与皮革护甲效果叠加

**重要提示：** 主手持物右键会消耗1个猫薄荷物品，副手持物不会触发效果。

## 常用命令（游戏内使用）

### 互动命令（所有人都能用）

- `/pat 玩家` - 轻轻拍拍别人
- `/lovebite 玩家` - 给个可爱的咬咬
- `/earscratch 玩家` - 挠挠耳朵
- `/purr` - 发出呼噜声
- `/hiss 玩家` - 对别人发出嘶嘶声
- `/scratch 玩家` - 用爪子抓一下
- `/attention 玩家` - 吸引其他玩家的注意

### 能力命令（只有猫娘能用）

- `/nightvision` - 开启夜视
- `/jumpboost` - 跳跃增强
- `/swiftsneak` - 潜行加速
- `/health` - 治疗自己和主人
- `/myskills` - 查看所有技能
- `/playernotice [on|off]` - 开启/关闭玩家接近提醒

### 主人系统命令

- `/owner add 玩家` - 请求成为某人的主人
- `/owner accept 玩家` - 接受主人的请求
- `/owner deny 玩家` - 拒绝主人的请求
- `/owner remove 玩家` - 解除主人关系
- `/owner list` - 查看自己的主人
- `/owner mylist` - 查看自己的猫娘

### 管理员命令

- `/nekox reload` - 重新加载插件设置
- `/nekoset 玩家 true/false` - 设置玩家为猫娘
- `/nekox language 语言` - 切换插件语言

## 配置说明（可选设置）

插件第一次运行时会创建 `config.yml` 文件，你可以用记事本打开修改：

### 基本设置

- `neko-chat`: 开启/关闭猫娘聊天特效
- `meat-only`: 猫娘是否只能吃肉
- `owner-system`: 是否开启主人系统

### 能力调整

- `claws`: 爪子攻击的伤害和冷却时间
- `armor-bonus`: 皮革护甲提供的速度加成
- `night-effects`: 夜间能力的开始和结束时间
- `health-skill`: 治疗技能的冷却和消耗

### 特殊效果

- `cat-nip`: 猫薄荷的效果和持续时间
- `stress-effect`: 应激反应的触发条件
- `passive-attack-boost`: 攻击伤害加成
- `armor-bonus`: 皮革护甲加成设置
  - `enabled`: 是否启用护甲加成
  - `leather-bonus`: 皮革护甲类型列表
  - `speed-bonus-per-piece`: 每件护甲的速度加成
- `neko-damage-modification`: 猫娘伤害调整设置
  - `enabled`: 是否启用伤害调整功能
  - `fall-damage-immunity`: 免疫跌落伤害
  - `other-damage-multiplier`: 其他伤害增加倍数
  - `debug`: 调试模式
- `neko-mob-behavior`: 猫娘生物行为设置
  - `enabled`: 是否启用生物行为功能
  - `creeper-repulsion`: 苦力怕驱赶功能
  - `phantom-repulsion`: 幻翼驱赶功能
  - `debug`: 调试模式
- `mob-targeting`: 生物目标行为设置
  - `enabled`: 是否启用目标行为调整
  - `distance-increase`: 检测距离增加倍数
  - `friendly-attraction`: 是否启用友好生物吸引
- `owner-death`: 主人死亡效应设置
  - `feature.enabled`: 是否启用主人死亡效应
  - `keep-inventory`: 是否保留猫娘物品栏
  - `keep-level`: 是否保留猫娘经验等级

## 开发者API

NekoX为其他插件提供了全面的API，以便集成猫娘功能。

### API接入

在您的插件中使用NekoX API：

1. 在 `plugin.yml` 中添加依赖：
```yaml
depend: [NekoX]
```

2. 在插件中获取API实例：
```java
import org.cneko.nekox.api.NekoXAPI;

public class YourPlugin extends JavaPlugin {
    private NekoXAPI nekoXAPI;
    
    @Override
    public void onEnable() {
        nekoXAPI = NekoXAPI.getInstance(this);
        if (nekoXAPI == null) {
            getLogger().warning("未找到NekoX插件！");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }
}
```

### 开发环境设置方法

由于NekoX没有发布到JitPack或其他公共仓库，您需要手动将其添加到您的项目中：

1. **Maven项目设置：**
   - 在您的项目目录中创建一个 `libs` 文件夹
   - 将本插件target文件夹中的 `NekoX-5.0-ProMax+++.jar` 文件复制到您的 `libs` 文件夹
   - 在您的 `pom.xml` 中添加以下依赖：
   
   ```xml
   <dependency>
       <groupId>org.cneko</groupId>
       <artifactId>NekoX</artifactId>
       <version>5.0-ProMax+++</version>
       <scope>system</scope>
       <systemPath>${project.basedir}/libs/NekoX-5.0-ProMax+++.jar</systemPath>
   </dependency>
   ```

2. **Gradle项目设置：**
   - 在您的项目目录中创建一个 `libs` 文件夹
   - 将 `NekoX-5.0-ProMax+++.jar` 文件复制到您的 `libs` 文件夹
   - 在您的 `build.gradle` 中添加以下依赖：
   
   ```gradle
   implementation files('libs/NekoX-5.0-ProMax+++.jar')
   ```

3. **手动设置（IDE）：**
   - 在您的项目目录中创建一个 `libs` 文件夹
   - 将 `NekoX-5.0-ProMax+++.jar` 文件复制到您的 `libs` 文件夹
   - 在您的IDE中将jar文件添加到项目的构建路径

### API方法

#### 猫娘状态检查

- `boolean isNeko(Player player)` - 检查玩家是否为猫娘
- `boolean isNeko(String playerName)` - 检查玩家（按名称）是否为猫娘

#### 玩家获取

- `Set<String> getAllNekoNames()` - 获取所有猫娘玩家名称（包括离线）

#### 主人关系管理

- `Set<String> getOwnerNames(String nekoName)` - 获取猫娘的所有主人名称
- `boolean isOwnerOf(String ownerName, String nekoName)` - 检查玩家是否为猫娘的主人
- `boolean hasOwner(String playerName)` - 检查玩家是否有主人

### 事件

NekoX提供了自定义事件供其他插件监听：

#### NekoStatusChangeEvent

当玩家的猫娘状态改变时触发：
```java
@EventHandler
public void onNekoStatusChange(NekoStatusChangeEvent event) {
    Player player = event.getPlayer();
    boolean isNeko = event.isNeko();
    boolean isOnline = event.isOnline();
    // 处理状态变更
}
```

#### OwnerRelationshipEvent

当主人关系添加、移除或申请时触发：
```java
@EventHandler
public void onOwnerRelationshipChange(OwnerRelationshipEvent event) {
    String nekoName = event.getNekoName();
    String ownerName = event.getOwnerName();
    OwnerRelationshipEvent.RelationshipAction action = event.getAction();
    
    switch (action) {
        case ADD:
            // 处理关系添加
            break;
        case REMOVE:
            // 处理关系移除
            break;
        case REQUEST:
            // 处理关系申请
            break;
    }
}
```

## 🔧 开发者相关文档

**💡 温馨提示：如果您只是普通用户，没有编程基础，可以跳过这部分内容！**

### Maven配置

如果您想开发与NekoX集成的插件，以下是您需要的Maven配置：

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
        <!-- PaperMC仓库 -->
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
        <!-- 将NekoX插件jar文件放在项目的libs文件夹中 -->
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

### 如何编译插件（适合想自己构建插件的开发者）

如果您想要自己编译插件文件，可以按照以下步骤操作：

#### 对于 Minecraft 1.20.4（默认版本）

打开命令行，输入：
```bash
mvn clean install
```

#### 对于 Minecraft 1.21.4

**重要提示：您需要先在电脑上安装 Java 21**
打开命令行，输入：
```bash
mvn clean install -Ppaper-1.21.4
```

#### 当前限制

- 如果您的电脑使用的是 Java 17，您只能编译 1.20.4 版本
- 要编译 1.21.4 版本，您需要先安装 Java 21

### PlaceholderAPI 支持

NekoX 集成了 PlaceholderAPI，提供以下占位符（需要安装 PlaceholderAPI 插件）：

| 占位符 | 描述 |
|--------|------|
| `%nekox_is_neko%` | 检查玩家是否为猫娘 |
| `%nekox_humans%` | 获取非猫娘玩家列表 |
| `%nekox_nekos%` | 获取猫娘玩家列表 |

使用 `/nekox placeholders` 命令可以查看所有可用的占位符及其状态。

### 伤害调整功能技术实现

#### 核心类

- <mcfile name="NekoDamageListener.java" path="src\main\java\org\cneko\nekox\events\NekoDamageListener.java"></mcfile>：伤害处理监听器

#### 实现逻辑

1. 监听 `EntityDamageEvent` 事件
2. 检查受伤实体是否为猫娘
3. 如果是跌落伤害（`FALL` 枚举），则取消伤害事件
4. 如果是其他伤害，则按配置倍数增加伤害

#### 测试方法

- **跌落测试**：猫娘从高处跳下不会受伤，普通玩家正常受伤
- **伤害测试**：猫娘受到攻击时伤害增加0.8倍
- **配置测试**：修改配置文件验证效果

### 生物驱赶功能技术实现

#### 核心类

- <mcfile name="NekoMobBehaviorListener.java" path="src\main\java\org\cneko\nekox\events\NekoMobBehaviorListener.java"></mcfile>：生物行为监听器

#### 实现逻辑

- **苦力怕驱赶**：监听 `ExplosionPrimeEvent`，取消爆炸并施加反向推力
- **幻翼驱赶**：监听 `EntityTargetLivingEntityEvent`，取消攻击并施加反向推力
- 距离判断：4格范围内生效
- 配置驱动，可单独开关功能

#### 测试方法

- **苦力怕测试**：猫娘靠近苦力怕不会爆炸，普通玩家正常爆炸
- **幻翼测试**：夜晚猫娘不会被幻翼攻击，普通玩家正常被攻击
- **配置测试**：修改配置文件验证驱赶效果

## 常见问题

### Q: 插件支持哪些Minecraft版本？
A: 支持1.20.4和1.21.4版本

### Q: 如何把玩家变成猫娘？
A: 管理员输入 `/nekoset 玩家名字 true`

### Q: 猫娘能做什么普通玩家不能做的事？
A: 夜视、跳跃增强、潜行加速、特殊聊天、爪子攻击等

### Q: 主人系统有什么用？
A: 猫娘可以认主人，主人和猫娘可以互相治疗，有特殊互动

### Q: 如何修改插件设置？
A: 修改 `plugins/NekoX/config.yml` 文件

### Q: 支持中文吗？
A: 支持！输入 `/nekox language chinese` 切换中文

## 技术支持

如果遇到问题：
1. 检查Minecraft版本是否匹配
2. 确认插件文件放对了位置
3. 查看服务器日志中的错误信息
4. 可以联系插件开发者寻求帮助

## 小贴士

- 猫娘在晚上会更强，尽量晚上活动
- 皮革护甲可以给猫娘提供速度加成
- 猫薄荷可以让猫娘暂时获得速度和跳跃提升
- 生命值低时会自动触发应激反应获得力量

---

# NekoX 开发者 Wiki

## 目录

1. [简介](#简介)
2. [构建配置](#构建配置)
3. [API接入](#api接入)
4. [API方法](#api方法)
5. [使用示例](#使用示例)
6. [事件系统](#事件系统)
7. [数据存储](#数据存储)

## 简介

NekoX是一个功能丰富的Minecraft猫娘插件，为服务器添加了猫娘角色和相关功能。本开发者Wiki旨在帮助其他插件开发者了解如何与NekoX插件进行集成。

## 构建配置

### Maven配置

如果您使用Maven构建您的插件，需要在pom.xml文件中添加以下配置：

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
        <!-- PaperMC仓库 -->
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
        <!-- 将NekoX插件jar文件放在项目的libs文件夹中 -->
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

### Gradle配置

如果您使用Gradle构建您的插件，需要在build.gradle文件中添加以下配置：

```gradle
plugins {
    id 'java'
}

group = 'your.group.id'
version = '1.0-SNAPSHOT'

repositories {
    // PaperMC仓库
    maven {
        name = 'papermc'
        url = 'https://repo.papermc.io/repository/maven-public/'
    }
    
    mavenCentral()
}

dependencies {
    // Paper API
    compileOnly 'io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT'
    
    // NekoX API
    // 将NekoX插件jar文件放在项目的libs文件夹中
    implementation files('libs/NekoX-5.0-ProMax+++.jar')
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.withType(JavaCompile) {
    options.encoding = 'UTF-8'
}
```

### 手动配置

如果您不使用构建工具，可以直接将NekoX插件的jar文件添加到您的项目依赖中：

1. 从NekoX项目的target文件夹中复制NekoX-5.0-ProMax+++.jar文件
2. 在您的项目中创建一个libs文件夹
3. 将NekoX-5.0-ProMax+++.jar文件放入libs文件夹
4. 在您的IDE中将该jar文件添加到项目的构建路径中
5. 确保在plugin.yml中添加NekoX作为依赖

### 构建和部署

构建您的插件后，确保：

1. 将生成的jar文件放入服务器的plugins文件夹
2. 确保NekoX插件也已安装
3. 重启服务器以加载所有插件

### 多版本支持

NekoX支持多个Minecraft版本：

- 对于Minecraft 1.20.4，使用Java 17
- 对于Minecraft 1.21.4，使用Java 21

如果需要构建特定版本，可以使用Maven profiles：

```bash
# 构建1.20.4版本（默认）
mvn clean install

# 构建1.21.4版本
mvn clean install -Ppaper-1.21.4
```

## API接入

### 1. 添加依赖

在您的插件中使用NekoX API，首先需要获取API实例：

```java
import org.cneko.nekox.api.NekoXAPI;

// 在您的插件主类中
public class YourPlugin extends JavaPlugin {
    private NekoXAPI nekoXAPI;
    
    @Override
    public void onEnable() {
        // 获取NekoX API实例
        nekoXAPI = NekoXAPI.getInstance(this);
        if (nekoXAPI == null) {
            getLogger().warning("NekoX插件未找到，请确保NekoX已安装并启用！");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        getLogger().info("成功连接到NekoX API！");
    }
}
```

### 2. plugin.yml配置

在您的plugin.yml文件中添加NekoX作为依赖：

```yaml
name: YourPlugin
version: 1.0
main: your.package.YourPlugin
api-version: 1.13
depend: [NekoX]
```

## API方法

### 猫娘检查方法

#### `boolean isNeko(Player player)`

检查指定玩家是否为猫娘

**参数**: 
- `player`: 要检查的玩家

**返回值**: 
- `true`: 玩家是猫娘
- `false`: 玩家不是猫娘

#### `boolean isNeko(String playerName)`

检查指定玩家名是否为猫娘（支持离线玩家）

**参数**: 
- `playerName`: 玩家名

**返回值**: 
- `true`: 玩家是猫娘
- `false`: 玩家不是猫娘

### 玩家获取方法

#### `Set<String> getAllNekoNames()`

获取所有猫娘玩家名（包括离线玩家）

**返回值**: 
- 包含所有猫娘玩家名的Set集合

### 关系查询方法

#### `Set<String> getOwnerNames(String nekoName)`

获取指定猫娘的所有主人名

**参数**: 
- `nekoName`: 猫娘玩家名

**返回值**: 
- 包含所有主人玩家名的Set集合

#### `boolean isOwnerOf(String ownerName, String nekoName)`

检查指定玩家是否为某猫娘的主人

**参数**: 
- `ownerName`: 主人玩家名
- `nekoName`: 猫娘玩家名

**返回值**: 
- `true`: 是主人
- `false`: 不是主人

#### `boolean hasOwner(String playerName)`

检查指定玩家是否有主人

**参数**: 
- `playerName`: 玩家名

**返回值**: 
- `true`: 有主人
- `false`: 没有主人

## 使用示例

### 示例1: 检查玩家是否为猫娘

```java
Player player = ...; // 获取玩家实例
if (nekoXAPI.isNeko(player)) {
    player.sendMessage("你是可爱的猫娘！");
} else {
    player.sendMessage("你不是猫娘哦~");
}
```

### 示例2: 获取所有猫娘玩家名

```java
Set<String> nekoNames = nekoXAPI.getAllNekoNames();
for (String nekoName : nekoNames) {
    // 处理每个猫娘玩家名
    getLogger().info("猫娘: " + nekoName);
}
```

### 示例3: 检查主人关系

```java
String ownerName = ...; // 主人玩家名
String nekoName = ...;  // 猫娘玩家名

if (nekoXAPI.isOwnerOf(ownerName, nekoName)) {
    getLogger().info(ownerName + " 是 " + nekoName + " 的主人！");
}
```

### 示例4: 获取猫娘的所有主人

```java
String nekoName = ...; // 猫娘玩家名
Set<String> owners = nekoXAPI.getOwnerNames(nekoName);

if (owners.isEmpty()) {
    getLogger().info(nekoName + " 还没有主人呢~");
} else {
    StringBuilder message = new StringBuilder(nekoName + " 的主人们: ");
    for (String owner : owners) {
        message.append(owner).append(", ");
    }
    getLogger().info(message.toString());
}
```

## 事件系统

NekoX提供了一些自定义事件，其他插件可以监听这些事件来响应猫娘相关的行为。

### 可用事件

#### NekoStatusChangeEvent

当玩家的猫娘状态发生变化时触发

**事件类**: `org.cneko.nekox.api.events.NekoStatusChangeEvent`
**触发时机**: 玩家被设置为猫娘或取消猫娘身份时

**使用示例**:

```java
@EventHandler
public void onNekoStatusChange(NekoStatusChangeEvent event) {
    Player player = event.getPlayer();
    boolean isNeko = event.isNeko();
    boolean isOnline = event.isOnline();
    
    if (isNeko) {
        getLogger().info(player.getName() + " 成为了猫娘！");
    } else {
        getLogger().info(player.getName() + " 不再是猫娘了！");
    }
}
```

#### OwnerRelationshipEvent

当主人关系发生变化时触发

**事件类**: `org.cneko.nekox.api.events.OwnerRelationshipEvent`
**触发时机**: 添加、移除或申请主人关系时

**使用示例**:

```java
@EventHandler
public void onOwnerRelationshipChange(OwnerRelationshipEvent event) {
    String nekoName = event.getNekoName();
    String ownerName = event.getOwnerName();
    OwnerRelationshipEvent.RelationshipAction action = event.getAction();
    
    switch (action) {
        case ADD:
            getLogger().info(ownerName + " 成为了 " + nekoName + " 的主人！");
            break;
        case REMOVE:
            getLogger().info(ownerName + " 不再是 " + nekoName + " 的主人了！");
            break;
        case REQUEST:
            getLogger().info(ownerName + " 申请成为 " + nekoName + " 的主人！");
            break;
    }
}
```

## 数据存储

NekoX使用SQLite数据库存储玩家数据，包括：

- 玩家是否为猫娘
- 猫娘与主人的关系
- 玩家的个性化设置

### 数据库结构

#### player_configs表

存储玩家基本配置

- `player_name`: 玩家名（主键）
- `notice_enabled`: 玩家接近提醒功能是否启用
- `is_neko`: 是否为猫娘

#### neko_owners表

存储猫娘与主人关系

- `neko_name`: 猫娘名
- `owner_name`: 主人名

#### owner_requests表

存储主人申请关系

- `requester_name`: 申请者名
- `neko_name`: 被申请的猫娘名

### 注意事项

1. 所有数据库操作都是异步执行的，不会阻塞主线程
2. 建议通过API方法访问数据，而不是直接操作数据库
3. 玩家数据使用玩家名作为标识，支持离线玩家查询

## 最佳实践

### 性能优化

1. 缓存API实例，避免重复获取
2. 合理使用异步操作，避免在主线程中执行耗时任务
3. 及时释放资源，特别是在插件禁用时

### 错误处理

1. 始终检查API实例是否为null
2. 处理可能的异常情况，如玩家不在线等
3. 提供友好的错误提示信息

### 兼容性

1. 检查NekoX插件是否存在和启用
2. 适配不同版本的NekoX API
3. 提供降级方案，当NekoX不可用时的备选逻辑

## 支持与反馈

如果您在使用NekoX API时遇到问题或有改进建议，请通过以下方式联系我们：

- GitHub Issues: [https://github.com/Shabby-666/NekoX/issues](https://github.com/Shabby-666/NekoX/issues)
- QQ：959612591

感谢您使用NekoX！