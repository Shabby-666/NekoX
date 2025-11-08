# NekoX - Cute Neko Plugin

## What is this?
NekoX is a fun Minecraft plugin that allows players to become adorable nekos (cat girls) with special abilities and interactions.

**Simply put:** This plugin adds cute neko characters to your Minecraft server!

## Quick Start (3 Easy Steps)

### Step 1: Download the Plugin
1. Download the `NekoX-4.0-Nextgen.jar` file
2. Place this file in your server's `plugins` folder

### Step 2: Start the Server
1. Start your Minecraft server normally
2. The plugin will automatically create configuration files

### Step 3: Start Playing
1. In-game, use `/nekoset playername true` to turn a player into a neko
2. Neko players can now use all the fun features!

## What Can Nekos Do? (Awesome Features)

### 🐱 Basic Abilities
- **Night Vision**: Nekos can see clearly in the dark
- **Jump Boost**: Can jump higher than normal players
- **Sneak Speed**: Move faster and quieter while sneaking

### 💬 Cute Chat
- Special prefixes and suffixes for neko chat
- Automatic cute word replacements
- For example, "hello" might become "meow~ hello"

### 🍖 Special Diet
- Nekos can only eat meat! No vegetables or fruits
- Supports various meat-based foods

### 🐾 Claw Attacks
- Special claw attacks for nekos
- Extra damage to mobs
- Cooldown period to prevent spam



### 🛡️ Leather Armor Bonus
- Wearing leather armor provides speed bonuses
- Each piece of leather armor increases speed
- Stackable effect for multiple armor pieces
- Maximum bonus level is configurable

**Simply put:** Leather armor makes nekos faster!

### 👑 Owner System
- Nekos can have owners
- Special interactions between owners and nekos
- Can heal and help each other
- **Owner Death Effect**: When an owner dies, their nekos also die (configurable)
  - Option to keep neko inventory and experience levels

### 🌙 Night Abilities
- Neko abilities become stronger at night
- Peak power at midnight
- Automatically activates, no manual control needed

### 💪 Stress Response
- When neko health is very low
- Automatically gains super strength
- Lasts 1 minute to help escape danger

### ⚔️ Passive Attack Boost
- Nekos deal slightly more damage with attacks
- Increased knockback strength against enemies
- Applies to all weapon types
- Configurable damage and knockback multipliers

**Simply put:** Nekos hit harder and send enemies flying further!

### 🌿 Catnip Effects
- Nekos can use catnip items (default: wheat seeds)
- Temporary speed and jump boost
- Effects last for a duration
- Can change the item in configuration file

### 🛡️ Damage Adjustment
- **Fall Damage Immunity**: Nekos don't take any fall damage
- **Other Damage Boost**: Nekos take 0.8x more damage from other sources
- Configurable damage multiplier in settings

**Simply put:** Nekos are immune to falling, but more vulnerable to attacks!

### 🐾 Mob Repulsion
- **Creeper Repulsion**: Creepers won't explode near nekos, they get pushed away
- **Phantom Repulsion**: Phantoms won't attack nekos, they get pushed away
- Works automatically, no manual control needed

**Simply put:** Creepers and phantoms are scared of nekos and run away!

### 👁️ Mob Targeting Behavior
- Hostile mobs have increased detection distance for nekos
- Friendly mobs are more attracted to nekos
- Affects various mob types in different ways
- Configurable detection parameters

## 🌿 Catnip Detailed Usage Guide

### What is Catnip?
Catnip is a special feature in NekoX plugin that allows neko players to use specific items to gain temporary buff effects.

### 🎯 How to Use

**Basic Operation:**
1. **Must be a Neko Player** - Only players set as nekos can use catnip
2. **Hold Catnip Item** - Default uses wheat seeds (WHEAT_SEEDS)
3. **Right-Click to Use** - Hold the catnip item and right-click to activate the effect

**Specific Steps:**
1. Ensure you are a neko (admin uses `/nekoset yourname true`)
2. Obtain wheat seeds (or other configured catnip item)
3. Hold wheat seeds and right-click
4. Immediately gain speed and jump boost effects

### ⚡ Effect Details

**Catnip provides the following buff effects:**
- **Speed Boost** (Speed II) - Significantly increased movement speed
- **Jump Boost** (Jump Boost II) - Greatly enhanced jump height
- **Duration** - Default 60 seconds (configurable)

**Effect Characteristics:**
- Effects activate immediately
- Consumes one catnip item
- Has cooldown period, cannot be used continuously
- Only works for neko players

### ⚙️ Configuration Options

You can customize catnip settings in `config.yml`:

```yaml
cat-nip:
  enabled: true           # Enable/disable catnip feature
  item: "WHEAT_SEEDS"     # Catnip item ID (can be changed to other items)
  duration: 60            # Effect duration (seconds)
```

**Modifiable Item Options:**
- Default: `WHEAT_SEEDS` (wheat seeds)
- Can change to: `GRASS`, `FERN`, `VINE`, etc.
- Supports any Minecraft item ID

### 💡 Usage Tips
1. **Combat Assistance** - Use during PVP or mob fighting for speed and jump advantage
2. **Escape Tool** - Use when being chased to quickly escape danger
3. **Exploration Boost** - Use during long-distance travel to improve movement efficiency
4. **Building Aid** - Jump boost helps with building at heights
5. **Armor Selection** - Wear leather armor for maximum speed bonuses

### 🚫 Limitations

- Only neko players can use
- Requires consumption of catnip items
- Has usage cooldown
- Cannot be used with off-hand items (must be held in main hand)

### 🔄 Synergy with Other Features

- **Night Effects** - Catnip works better at night
- **Stress Response** - Combine with catnip when health is low for better effect
- **Armor Bonus** - Speed bonus stacks with leather armor effects

**Important Note:** Right-clicking with catnip in main hand consumes 1 item, off-hand items won't trigger the effect.

## Common Commands (In-Game Usage)

### Interaction Commands (Everyone can use)
- `/pat player` - Gently pat someone
- `/lovebite player` - Give a cute love bite
- `/earscratch player` - Scratch someone's ears
- `/purr` - Make purring sounds
- `/hiss player` - Hiss at someone
- `/scratch player` - Scratch someone with claws

### Ability Commands (Nekos only)
- `/nightvision` - Toggle night vision
- `/jumpboost` - Toggle jump boost
- `/swiftsneak` - Toggle sneak speed
- `/health` - Heal yourself and owner
- `/myskills` - View all available skills

### Owner System Commands
- `/owner add player` - Request to become someone's owner
- `/owner accept player` - Accept owner request
- `/owner remove player` - Remove owner relationship
- `/owner list` - View your owners

### Admin Commands
- `/nekox reload` - Reload plugin settings
- `/nekoset player true/false` - Set player as neko
- `/nekox language language` - Change plugin language

## Configuration (Optional Settings)

The plugin creates a `config.yml` file on first run. You can edit it with any text editor:

### Basic Settings
- `neko-chat`: Enable/disable neko chat effects
- `meat-only`: Whether nekos can only eat meat
- `owner-system`: Enable/disable owner system

### Ability Adjustments
- `claws`: Claw attack damage and cooldown
- `armor-bonus`: Speed bonus from leather armor
- `night-effects`: Night ability start/end times
- `health-skill`: Healing skill cooldown and cost

### Special Effects
- `cat-nip`: Catnip effects and duration
- `stress-effect`: Stress response trigger conditions
- `passive-attack-boost`: Attack damage bonus
- `neko-damage-modification`: Neko damage adjustment settings
- `neko-mob-behavior`: Neko mob repulsion settings

## Frequently Asked Questions

### Q: Which Minecraft versions are supported?
A: Supports 1.20.4 and 1.21.4 versions

### Q: How to turn a player into a neko?
A: Admin uses `/nekoset playername true`

### Q: What can nekos do that normal players can't?
A: Night vision, jump boost, sneak speed, special chat, claw attacks, etc.

### Q: What's the purpose of the owner system?
A: Nekos can have owners, they can heal each other and have special interactions

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
- Nekos are stronger at night, try to be active during nighttime
- Leather armor provides speed bonus to nekos
- Catnip gives temporary speed and jump boost
- Low health automatically triggers stress response for extra power

---

# NekoX - 可爱的猫娘插件

## 这是什么？
NekoX是一个让Minecraft变得更有趣的插件！它可以让玩家变成可爱的猫娘，拥有特殊能力和互动方式。

**简单来说：** 这个插件让你的服务器里出现可爱的猫娘角色！

## 快速开始（3步搞定）

### 第1步：下载插件
1. 下载 `NekoX-4.0-Nextgen.jar` 文件
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



### 🛡️ 皮革护甲加成
- 穿着皮革护甲可以获得速度加成
- 每件皮革护甲都能增加速度
- 多件护甲效果叠加
- 最大加成等级可配置

**简单来说：** 皮革护甲让猫娘跑得更快！

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
- **护甲加成** - 穿着皮革护甲时速度加成叠加

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

### 主人系统命令
- `/owner add 玩家` - 请求成为某人的主人
- `/owner accept 玩家` - 接受主人的请求
- `/owner remove 玩家` - 解除主人关系
- `/owner list` - 查看自己的主人

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

## 🔧 开发者相关文档

**💡 温馨提示：如果您只是普通用户，没有编程基础，可以跳过这部分内容！**

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

---

**享受你的猫娘冒险吧！🐾**
