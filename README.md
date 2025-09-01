# NekoX

## Table of Contents
- [English](#english)
- [中文](#中文)

---

## English

### Introduction
NekoX is a Minecraft plugin that transforms players into cute neko girls (cat girls) with special abilities and interactions. This plugin combines features from NekoC and toNeko plugins, creating a comprehensive neko experience for your Minecraft server.

Original plugin repositories:
- toNeko: https://modrinth.com/plugin/toneko
- NekoC: https://modrinth.com/plugin/nekoc

### Version Compatibility
This plugin supports two versions of Paper API through Maven profiles:
- Default: Paper 1.20.4 (using Java 17)
- Optional: Paper 1.21.4 (requires Java 21)

### How to Compile

#### Compile for 1.20.4 (Default)
```bash
mvn clean install
```

#### Compile for 1.21.4
**Note: You need to have Java 21 installed to compile the 1.21.4 version**
```bash
mvn clean install -Ppaper-1.21.4
```

### Current System Limitations
The current system is using Java 17, which cannot directly compile the 1.21.4 version. If you need to compile the 1.21.4 version, please install Java 21 first.

### Features
- Multiple neko-related commands
- Neko-themed event handlers
- Catnip effects
- Special abilities like jump boost, night vision, and swift sneak
- Neko chat with cute prefixes and suffixes
- Meat-only diet system
- Claw attack system
- Armor bonuses for leather armor
- Mob targeting adjustments
- Owner system for nekos to have owners and additional interactions
- Health restore skill for nekos to heal themselves and their owners
- Automatic night enhancements for nekos (night vision, speed, jump boost)
- Owner-death system where nekos die when their owner dies
- bStats integration for usage statistics

### Command List

#### Interaction Commands
- `/pat <player>` - Gently pat another player
- `/lovebite <player>` - Give another player a loving bite
- `/earscratch <player>` - Gently scratch another player's ears
- `/purr` - Make a comfortable purring sound
- `/hiss <player>` - Hiss at another player
- `/scratch <player>` - Scratch another player with claws
- `/attention <player>` - Attract another player's attention

#### Effect Commands
- `/nightvision [player]` - Get night vision effect (defaults to self)
- `/jumpboost [player]` - Get jump boost effect (defaults to self)
- `/swiftsneak [player]` - Get swift sneak effect (defaults to self)
- `/health` - Neko-only skill to restore health for themselves and their owners (requires food and has cooldown)

#### Owner System Commands
- `/owner <subcommand>` - Manage owner relationships and interactions
  - `/owner add <player>` - Send a request to become the owner of a neko player
  - `/owner accept <player>` - Accept an owner request from a player
  - `/owner deny <player>` - Deny an owner request from a player
  - `/owner forceadd <player>` - Become an owner without consent (admin only)
  - `/owner remove <player>` - Remove owner relationship with a player (both nekos and owners can use this)
  - `/owner list` - View all your owners (as a neko)
  - `/owner mylist` - View all nekos you own

#### Management Commands
- `/nekox` - View plugin information
- `/nekox reload` - Reload plugin configuration (requires admin permission)
- `/nekox version` - View plugin version
- `/nekoset <player> <true/false>` - Set a player's neko status (requires admin permission)
- `/nekoxhelp` - View help information

### Configuration File
The plugin will generate a `config.yml` file after the first run, which you can configure according to your needs.

Key configuration options:
- `neko-chat` - Enable/disable special chat formatting for neko players
- `meat-only` - Restrict neko players to only eat meat and certain other foods
- `cat-nip` - Configure catnip item and effects
- `claws` - Configure claw attack damage and cooldown
- `mob-targeting` - Adjust how mobs target neko players
- `armor-bonus` - Configure speed bonuses for wearing leather armor
- `owner-system` - Enable/disable the owner system functionality
- `health-skill` - Configure the health restore skill (cooldown, hunger cost, max level)
- `night-effects` - Configure automatic night enhancements (enabled, check interval, start/end/peak times, max level)
- `owner-death` - Configure owner-death system (enabled, keep inventory, keep level)

### Permission System
The plugin includes a complete permission node system that can be managed through permission plugins.

Key permissions:
- `nekox.pat` - Allows using the /pat command (default: true)
- `nekox.lovebite` - Allows using the /lovebite command (default: true)
- `nekox.earscratch` - Allows using the /earscratch command (default: true)
- `nekox.purr` - Allows using the /purr command (default: true)
- `nekox.hiss` - Allows using the /hiss command (default: true)
- `nekox.scratch` - Allows using the /scratch command (default: true)
- `nekox.attention` - Allows using the /attention command (default: true)
- `nekox.nightvision` - Allows using the /nightvision command (default: true)
- `nekox.jumpboost` - Allows using the /jumpboost command (default: true)
- `nekox.swiftsneak` - Allows using the /swiftsneak command (default: true)
- `nekox.health` - Allows using the /health command (default: true for nekos)
- `nekox.help` - Allows using the /nekoxhelp command (default: true)
- `nekox.admin` - Allows using admin commands (default: op)
- `nekox.owner` - Allows using the /owner command (default: true)
- `nekox.owner.set` - Allows setting an owner (default: true)
- `nekox.owner.remove` - Allows removing an owner (default: true)
- `nekox.owner.list` - Allows listing owner relationships (default: true)
- `nekox.owner.info` - Allows viewing owner information (default: true)

### Statistics
The plugin integrates with bStats to help developers understand plugin usage.

[![bStats](https://bstats.org/signatures/bukkit/NekoX-Plugin.svg)](https://bstats.org/plugin/bukkit/NekoX-Plugin/27133)

### Notes
- Neko player data is stored in `nekos.json` in the plugin data folder
- Only players set as nekos (using /nekoset command) will have access to neko-exclusive commands and features
- Neko players will have special chat formatting with prefixes and suffixes
- The owner system allows nekos to form special bonds with other players, unlocking additional interactions
- At night, nekos automatically receive enhanced abilities (night vision, speed, jump boost) that increase in strength towards midnight
- Nekos can use the /health command to restore health for themselves and their owners, but it consumes hunger and has a cooldown
- If enabled, when an owner dies, all their nekos will also die
- All bStats data is anonymous and helps developers improve the plugin

---

## 中文

### 介绍
NekoX是一个Minecraft插件，它可以将玩家转变为可爱的猫娘，并赋予她们特殊的能力和互动方式。这个插件融合了NekoC和toNeko插件的功能，为您的Minecraft服务器创造一个全面的猫娘体验。

原始插件仓库：
- toNeko: https://modrinth.com/plugin/toneko
- NekoC: https://modrinth.com/plugin/nekoc

### 版本兼容性
这个插件通过Maven profiles支持两个版本的Paper API：
- 默认：Paper 1.20.4（使用Java 17）
- 可选：Paper 1.21.4（需要Java 21）

### 如何编译

#### 编译1.20.4版本（默认）
```bash
mvn clean install
```

#### 编译1.21.4版本
**注意：需要安装Java 21才能编译1.21.4版本**
```bash
mvn clean install -Ppaper-1.21.4
```

### 当前系统限制
当前系统使用的是Java 17，无法直接编译1.21.4版本。如果需要编译1.21.4版本，请先安装Java 21。

### 功能特性
- 多种猫娘相关命令
- 猫娘主题事件处理器
- 猫薄荷效果
- 跳跃提升、夜视和迅捷潜行等特殊能力
- 带有可爱前缀和后缀的猫娘聊天
- 只吃肉类的饮食系统
- 猫爪攻击系统
- 皮革护甲加成
- 生物目标调整
- 主人系统，让猫娘可以拥有主人并进行额外互动
- 猫娘专有的健康恢复技能，可以同时治疗自己和主人
- 夜间自动增强效果（夜视、速度、跳跃提升）
- 主人死亡时猫娘也会死亡的系统
- bStats统计集成，收集使用数据帮助开发者改进插件

### 命令列表

#### 互动命令
- `/pat <玩家>` - 温柔地抚摸另一个玩家
- `/lovebite <玩家>` - 给另一个玩家一个爱的咬痕
- `/earscratch <玩家>` - 轻轻地挠另一个玩家的耳朵
- `/purr` - 发出舒服的呼噜声
- `/hiss <玩家>` - 对着另一个玩家发出嘶嘶声
- `/scratch <玩家>` - 用爪子挠另一个玩家
- `/attention <玩家>` - 吸引另一个玩家的注意

#### 效果命令
- `/nightvision [玩家]` - 获得夜视效果（默认自己）
- `/jumpboost [玩家]` - 获得跳跃提升效果（默认自己）
- `/swiftsneak [玩家]` - 获得迅捷潜行效果（默认自己）
- `/health` - 猫娘专属技能，可以恢复自己和主人的生命值（消耗饱食度且有冷却时间）

#### 主人系统命令
- `/owner <子命令>` - 管理主人关系和互动
  - `/owner add <玩家>` - 向指定猫娘玩家发送主人申请
  - `/owner accept <玩家>` - 接受指定玩家的主人申请
  - `/owner deny <玩家>` - 拒绝指定玩家的主人申请
  - `/owner forceadd <玩家>` - 无需猫娘同意直接成为其主人(管理员专用)
  - `/owner remove <玩家>` - 移除与指定玩家的主人关系(猫娘和主人都可以使用)
  - `/owner list` - 查看你(作为猫娘)的所有主人
  - `/owner mylist` - 查看你拥有的所有猫娘

#### 管理命令
- `/nekox` - 查看插件信息
- `/nekox reload` - 重新加载插件配置（需要管理员权限）
- `/nekox version` - 查看插件版本
- `/nekoset <玩家> <true/false>` - 设置玩家的猫娘状态（需要管理员权限）
- `/nekoxhelp` - 查看帮助信息

### 配置文件
插件首次运行后会生成`config.yml`文件，您可以根据需要进行配置。

主要配置选项：
- `neko-chat` - 启用/禁用猫娘玩家的特殊聊天格式
- `meat-only` - 限制猫娘玩家只能吃肉和某些其他食物
- `cat-nip` - 配置猫薄荷物品和效果
- `claws` - 配置猫爪攻击伤害和冷却时间
- `mob-targeting` - 调整生物对猫娘玩家的目标锁定
- `armor-bonus` - 配置穿戴皮革护甲的速度加成
- `owner-system` - 启用/禁用主人系统功能
- `health-skill` - 配置健康恢复技能（冷却时间、饱食度消耗、最大等级）
- `night-effects` - 配置夜间自动增强效果（启用状态、检查间隔、开始/结束/峰值时间、最大等级）
- `owner-death` - 配置主人死亡系统（启用状态、是否保留物品栏、是否保留等级）

### 权限系统
插件包含完整的权限节点系统，可以通过权限插件进行权限管理。

主要权限：
- `nekox.pat` - 允许使用/pat命令（默认：true）
- `nekox.lovebite` - 允许使用/lovebite命令（默认：true）
- `nekox.earscratch` - 允许使用/earscratch命令（默认：true）
- `nekox.purr` - 允许使用/purr命令（默认：true）
- `nekox.hiss` - 允许使用/hiss命令（默认：true）
- `nekox.scratch` - 允许使用/scratch命令（默认：true）
- `nekox.attention` - 允许使用/attention命令（默认：true）
- `nekox.nightvision` - 允许使用/nightvision命令（默认：true）
- `nekox.jumpboost` - 允许使用/jumpboost命令（默认：true）
- `nekox.swiftsneak` - 允许使用/swiftsneak命令（默认：true）
- `nekox.health` - 允许使用/health命令（默认：猫娘可用）
- `nekox.help` - 允许使用/nekoxhelp命令（默认：true）
- `nekox.admin` - 允许使用管理员命令（默认：op）
- `nekox.owner` - 允许使用/owner命令（默认：true）
- `nekox.owner.set` - 允许设置主人（默认：true）
- `nekox.owner.remove` - 允许解除主人（默认：true）
- `nekox.owner.list` - 允许列出主人关系（默认：true）
- `nekox.owner.info` - 允许查看主人信息（默认：true）

### 统计功能
插件集成了bStats统计功能，帮助开发者了解插件使用情况。

[![bStats](https://bstats.org/signatures/bukkit/NekoX-Plugin.svg)](https://bstats.org/plugin/bukkit/NekoX-Plugin/27133)

### 注意事项
- 猫娘玩家数据存储在插件数据文件夹中的`nekos.json`文件中
- 只有被设置为猫娘的玩家（使用/nekoset命令）才能访问猫娘专属命令和功能
- 猫娘玩家会有特殊的聊天格式，带有前缀和后缀
- 主人系统允许猫娘与其他玩家建立特殊关系，解锁额外的互动功能
- 在夜间，猫娘会自动获得增强能力（夜视、速度、跳跃提升），效果强度在午夜达到峰值
- 猫娘可以使用/health命令恢复自己和主人的生命值，但会消耗饱食度且有冷却时间
- 如启用相关功能，当主人死亡时，其所有猫娘也会随之死亡
- 所有bStats数据都是匿名的，有助于开发者改进插件