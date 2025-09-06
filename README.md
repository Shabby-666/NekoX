# NekoX

## 目录
- [English](#english)
- [中文](#中文)

---

## English

### What is NekoX?
NekoX is a fun Minecraft plugin that lets players become cute cat girls (nekos) with special abilities and ways to interact with others. It combines features from the NekoC and toNeko plugins to create a complete cat girl experience for your server.

Original plugin creators:
- toNeko: https://modrinth.com/plugin/toneko
- NekoC: https://modrinth.com/plugin/nekoc

### Which Minecraft Versions Does It Work With?
This plugin works with two versions of Minecraft:
- Default: Minecraft 1.20.4 (runs on Java 17)
- Optional: Minecraft 1.21.4 (needs Java 21)

### How to Compile (For Server Owners Who Want to Build It Themselves)
If you want to create the plugin file yourself, follow these simple steps:

#### For Minecraft 1.20.4 (Default)
Open the command line and type:
```bash
mvn clean install
```

#### For Minecraft 1.21.4
**Important: You need to have Java 21 installed on your computer first**
Open the command line and type:
```bash
mvn clean install -Ppaper-1.21.4
```

### Current Limitation
If your computer is using Java 17, you can only make the 1.20.4 version. To make the 1.21.4 version, you'll need to install Java 21 first.

### What Can NekoX Do?
- Let players become cat girls with special abilities
- Add cute cat-themed interactions between players
- Give cat girls special powers like seeing in the dark, jumping higher, and moving quietly
- Make cat girls chat with cute messages (with special prefixes and suffixes)
- Create a special diet system where cat girls can only eat meat
- Add claw attacks for cat girls
- Give bonuses when cat girls wear leather armor
- Change how monsters target cat girls
- Let cat girls have owners and enjoy special interactions
- Let cat girls heal themselves and their owners with a special health skill
- Give cat girls stronger abilities at night (that get even better near midnight)
- Optional feature: Make cat girls die when their owner dies
- Track anonymous usage data to help improve the plugin

### How to Use NekoX Commands
Here are all the commands you can use in the game:

#### Fun Interaction Commands
- `/pat <player>` - Gently pat someone
- `/lovebite <player>` - Give someone a playful bite
- `/earscratch <player>` - Scratch someone's ears gently
- `/purr` - Make a happy purring sound
- `/hiss <player>` - Hiss at someone playfully
- `/scratch <player>` - Scratch someone with your claws
- `/attention <player>` - Get someone's attention

#### Ability Commands
- `/nightvision [player]` - Get the ability to see in the dark (works on yourself or someone else)
- `/jumpboost [player]` - Jump higher than normal (works on yourself or someone else)
- `/swiftsneak [player]` - Move quietly and quickly while sneaking (works on yourself or someone else)
- `/health` - (Only for cat girls) Heal yourself and your owner, but it uses hunger and you can't use it too often

#### Owner System Commands
- `/owner <subcommand>` - Manage your owner relationships
  - `/owner add <player>` - Ask to become someone's owner
  - `/owner accept <player>` - Accept an owner request from someone
  - `/owner deny <player>` - Say no to an owner request from someone
  - `/owner forceadd <player>` - Become someone's owner without asking (only for admins)
  - `/owner remove <player>` - End an owner relationship (both cat girls and owners can use this)
  - `/owner list` - See all your owners (if you're a cat girl)
  - `/owner mylist` - See all the cat girls you own

#### Admin Commands
- `/nekox` - See information about the plugin
- `/nekox reload` - Update the plugin settings without restarting the server (admins only)
- `/nekox version` - See which version of the plugin you're using
- `/nekoset <player> <true/false>` - Turn someone into a cat girl or back (admins only)
- `/nekoxhelp` - See this list of commands

### How to Change Plugin Settings
The first time you run the plugin, it will create a `config.yml` file. You can open this file with Notepad to change how the plugin works.

Here are the most important settings you can change:
- `neko-chat` - Turn special cat girl chat messages on or off
- `meat-only` - Make cat girls only able to eat meat
- `cat-nip` - Change how catnip works
- `claws` - Adjust how much damage claw attacks do and how often you can use them
- `mob-targeting` - Change how monsters react to cat girls
- `armor-bonus` - Make leather armor give cat girls more speed
- `owner-system` - Turn the owner system on or off
- `health-skill` - Change how the /health command works (how often you can use it, how much hunger it uses)
- `night-effects` - Adjust the special night abilities (when they start, how strong they are)
- `owner-death` - Choose if cat girls should die when their owner dies, and if they keep their items/levels

### How Permissions Work
The plugin uses a permission system to control who can use which commands. Most commands are available to everyone by default, but some (like admin commands) are only for server owners.

### How to See How Many People Use NekoX
The plugin uses bStats to track how many servers use it. This data is completely anonymous and helps the developers make the plugin better.

[![bStats](https://bstats.org/signatures/bukkit/NekoX-Plugin.svg)](https://bstats.org/plugin/bukkit/NekoX-Plugin/27133)

### Important Things to Know
- Cat girl player information is saved in a file called `nekos.json` in the plugin's folder
- Only players who are set as cat girls (using the /nekoset command) can use cat girl features
- Cat girls have special chat messages with cute additions
- The owner system lets cat girls form special bonds with other players
- At night, cat girls automatically get better at seeing in the dark, moving faster, and jumping higher
- Cat girls can use /health to heal themselves and their owners, but it uses their food and can't be used too often
- If the owner-death feature is turned on, all cat girls will die when their owner dies

---

## 中文

### NekoX是什么？
NekoX是一个有趣的Minecraft插件，它可以让玩家变成可爱的猫娘，并拥有特殊能力和与其他玩家互动的方式。这个插件融合了NekoC和toNeko插件的功能，为您的服务器创造一个完整的猫娘体验。

原始插件作者：
- toNeko: https://modrinth.com/plugin/toneko
- NekoC: https://modrinth.com/plugin/nekoc

### 支持哪些Minecraft版本？
这个插件支持两个版本的Minecraft：
- 默认：Minecraft 1.20.4（运行在Java 17上）
- 可选：Minecraft 1.21.4（需要Java 21）

### 如何自己编译插件（适合想自己构建插件的服务器管理员）
如果您想自己创建插件文件，请按照以下简单步骤操作：

#### 对于Minecraft 1.20.4（默认）
打开命令行并输入：
```bash
mvn clean install
```

#### 对于Minecraft 1.21.4
**重要提示：您需要先在电脑上安装Java 21**
打开命令行并输入：
```bash
mvn clean install -Ppaper-1.21.4
```

### 当前限制
如果您的电脑使用的是Java 17，您只能制作1.20.4版本。要制作1.21.4版本，您需要先安装Java 21。

### NekoX能做什么？
- 让玩家变成拥有特殊能力的猫娘
- 添加可爱的猫主题玩家互动方式
- 赋予猫娘特殊能力，如夜视、高跳和潜行加速
- 让猫娘聊天时有可爱的消息格式（带有特殊前缀和后缀）
- 创建特殊饮食系统，猫娘只能吃肉
- 为猫娘添加爪子攻击
- 猫娘穿戴皮革护甲时获得额外加成
- 改变怪物对猫娘的攻击目标选择
- 让猫娘可以拥有主人并享受特殊互动
- 让猫娘使用特殊健康技能治疗自己和主人
- 在夜间赋予猫娘更强的能力（午夜时效果最佳）
- 可选功能：主人死亡时猫娘也会死亡
- 收集匿名使用数据以帮助改进插件

### 如何使用NekoX命令
以下是游戏中可以使用的所有命令：

#### 有趣的互动命令
- `/pat <玩家>` - 温柔地抚摸某人
- `/lovebite <玩家>` - 给某人一个调皮的咬痕
- `/earscratch <玩家>` - 轻轻地挠某人的耳朵
- `/purr` - 发出开心的呼噜声
- `/hiss <玩家>` - 调皮地对某人发出嘶嘶声
- `/scratch <玩家>` - 用爪子挠某人
- `/attention <玩家>` - 引起某人的注意

#### 能力命令
- `/nightvision [玩家]` - 获得夜视能力（对自己或他人使用）
- `/jumpboost [玩家]` - 能够跳得更高（对自己或他人使用）
- `/swiftsneak [玩家]` - 潜行时移动更安静快速（对自己或他人使用）
- `/health` - （仅限猫娘使用）治疗自己和主人，但会消耗饱食度且有冷却时间

#### 主人系统命令
- `/owner <子命令>` - 管理您的主人关系
  - `/owner add <玩家>` - 请求成为某人的主人
  - `/owner accept <玩家>` - 接受某人的主人请求
  - `/owner deny <玩家>` - 拒绝某人的主人请求
  - `/owner forceadd <玩家>` - 无需请求直接成为某人的主人（仅管理员可用）
  - `/owner remove <玩家>` - 结束主人关系（猫娘和主人都可以使用）
  - `/owner list` - 查看您的所有主人（如果您是猫娘）
  - `/owner mylist` - 查看您拥有的所有猫娘

#### 管理员命令
- `/nekox` - 查看插件信息
- `/nekox reload` - 无需重启服务器即可更新插件设置（仅限管理员）
- `/nekox version` - 查看您使用的插件版本
- `/nekoset <玩家> <true/false>` - 将某人变为猫娘或恢复正常（仅限管理员）
- `/nekoxhelp` - 查看此命令列表

### 如何更改插件设置
首次运行插件时，它会创建一个`config.yml`文件。您可以用记事本打开此文件来更改插件的工作方式。

以下是您可以更改的最重要设置：
- `neko-chat` - 开启或关闭猫娘的特殊聊天消息
- `meat-only` - 让猫娘只能吃肉
- `cat-nip` - 更改猫薄荷的效果
- `claws` - 调整爪子攻击的伤害和使用频率
- `mob-targeting` - 更改怪物对猫娘的反应方式
- `armor-bonus` - 让皮革护甲给猫娘提供更多速度
- `owner-system` - 开启或关闭主人系统
- `health-skill` - 更改/health命令的工作方式（使用频率、消耗的饱食度）
- `night-effects` - 调整特殊夜间能力（何时开始、效果强度）
- `owner-death` - 选择主人死亡时猫娘是否也死亡，以及她们是否保留物品/等级

### 权限系统说明
插件使用权限系统来控制谁可以使用哪些命令。大多数命令默认对所有人可用，但有些（如管理员命令）仅对服务器所有者可用。

### 如何查看有多少人使用NekoX
插件使用bStats来跟踪有多少服务器使用它。这些数据完全匿名，有助于开发者改进插件。

[![bStats](https://bstats.org/signatures/bukkit/NekoX-Plugin.svg)](https://bstats.org/plugin/bukkit/NekoX-Plugin/27133)

### 重要须知
- 猫娘玩家信息保存在插件文件夹中的`nekos.json`文件中
- 只有被设置为猫娘的玩家（使用/nekoset命令）才能使用猫娘功能
- 猫娘聊天时会有带有可爱前缀和后缀的特殊消息
- 主人系统让猫娘可以与其他玩家建立特殊关系
- 在夜间，猫娘会自动获得更好的夜视能力、移动速度和跳跃能力
- 猫娘可以使用/health治疗自己和主人，但会消耗饱食度且不能频繁使用
- 如果开启了主人死亡功能，当主人死亡时，所有猫娘也会死亡