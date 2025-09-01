# NekoX - Minecraft Neko Plugin

## Table of Contents
- [English](#english)
- [中文](#中文)

---

## English

### Introduction
NekoX is a Minecraft plugin that transforms players into cute neko girls (cat girls) with special abilities and interactions. This plugin combines features from NekoC and toNeko plugins, creating a comprehensive neko experience for your Minecraft server.

["Version Update(2.0-RELEASE)"](https://github.com/Shabby-666/NekoX/tree/2.0-RELEASE)

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
- Special abilities like jump boost and night vision
- Neko chat with cute prefixes and suffixes
- Meat-only diet system
- Claw attack system
- Armor bonuses for leather armor
- Mob targeting adjustments

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
- `nekox.help` - Allows using the /nekoxhelp command (default: true)
- `nekox.admin` - Allows using admin commands (default: op)

### Statistics
The plugin integrates with bStats to help developers understand plugin usage.

### Notes
- Neko player data is stored in `nekos.json` in the plugin data folder
- Only players set as nekos (using /nekoset command) will have access to neko-exclusive commands and features
- Neko players will have special chat formatting with prefixes and suffixes

---

## 中文

### 介绍
NekoX是一个Minecraft插件，它可以将玩家转变为可爱的猫娘，并赋予她们特殊的能力和互动方式。这个插件融合了NekoC和toNeko插件的功能，为您的Minecraft服务器创造一个全面的猫娘体验。

["版本更新（2.0-RELEASE）"](https://github.com/Shabby-666/NekoX/tree/2.0-RELEASE)

原始插件链接：
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
- 跳跃提升和夜视等特殊能力
- 带有可爱前缀和后缀的猫娘聊天
- 只吃肉类的饮食系统
- 猫爪攻击系统
- 皮革护甲加成
- 生物目标调整

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
- `nekox.help` - 允许使用/nekoxhelp命令（默认：true）
- `nekox.admin` - 允许使用管理员命令（默认：op）

### 统计功能
插件集成了bStats统计功能，帮助开发者了解插件使用情况。

### 注意事项
- 猫娘玩家数据存储在插件数据文件夹中的`nekos.json`文件中
- 只有被设置为猫娘的玩家（使用/nekoset命令）才能访问猫娘专属命令和功能
- 猫娘玩家会有特殊的聊天格式，带有前缀和后缀
