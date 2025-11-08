package org.cneko.nekox.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.cneko.nekox.NekoX;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LanguageManager {
    private final NekoX plugin;
    private final Map<String, FileConfiguration> languageFiles = new HashMap<>();
    private String defaultLanguage = "English";
    private final File languagesDir;
    private final File languageConfigFile;
    private FileConfiguration languageConfig;

    public LanguageManager(NekoX plugin) {
        this.plugin = plugin;
        
        // 创建Languages文件夹
        this.languagesDir = new File(plugin.getDataFolder(), "Languages");
        if (!languagesDir.exists()) {
            languagesDir.mkdirs();
        }
        
        // 创建Language.yml配置文件
        this.languageConfigFile = new File(plugin.getDataFolder(), "Language.yml");
        if (!languageConfigFile.exists()) {
            createDefaultLanguageConfig();
        }
        
        languageConfig = YamlConfiguration.loadConfiguration(languageConfigFile);
        defaultLanguage = languageConfig.getString("Language", "English");
        
        // 加载默认语言文件
        loadDefaultLanguageFiles();
        
        // 加载配置的语言列表
        loadLanguages();
    }
    
    private void createDefaultLanguageConfig() {
        try {
            Files.createFile(languageConfigFile.toPath());
            FileConfiguration config = YamlConfiguration.loadConfiguration(languageConfigFile);
            config.set("Language", "English");
            config.set("Languages", new String[]{"English", "简体中文"});
            config.save(languageConfigFile);
        } catch (IOException e) {
            plugin.getLogger().severe("无法创建Language.yml配置文件: " + e.getMessage());
        }
    }
    
    private void loadDefaultLanguageFiles() {
        // 创建默认的English语言文件
        File englishFile = new File(languagesDir, "English.yml");
        if (!englishFile.exists()) {
            createDefaultEnglishFile();
        }
        
        // 创建默认的简体中文语言文件
        File chineseFile = new File(languagesDir, "简体中文.yml");
        if (!chineseFile.exists()) {
            createDefaultChineseFile();
        }
        
        // 无论文件是否存在，都加载默认语言文件到映射中
        loadLanguageFile("English");
        loadLanguageFile("简体中文");
    }
    
    private void loadLanguageFile(String language) {
        File languageFile = new File(languagesDir, language + ".yml");
        if (languageFile.exists()) {
            try {
                FileConfiguration config = YamlConfiguration.loadConfiguration(languageFile);
                languageFiles.put(language, config);
            } catch (Exception e) {
                plugin.getLogger().severe("加载语言文件 " + language + " 时出错: " + e.getMessage());
            }
        } else {
            plugin.getLogger().warning("语言文件 " + language + ".yml 不存在，将无法使用该语言的翻译");
        }
    }
    
    private void createDefaultEnglishFile() {
        try {
            File englishFile = new File(languagesDir, "English.yml");
            Files.createFile(englishFile.toPath());
            FileConfiguration config = YamlConfiguration.loadConfiguration(englishFile);
            
            // 基本插件消息
            config.set("plugin.enabled", "NekoX plugin has been successfully enabled!");
            config.set("plugin.disabled", "NekoX plugin has been successfully disabled!");
            config.set("plugin.name", "NekoX");
            config.set("plugin.version", "Version: 4.0-Nextgen");
            config.set("plugin.chinese_author", "This modified version author: _Chinese_Player_");
            config.set("plugin.help_info", "Type /nekoxhelp for help");
            config.set("plugin.original_authors", "Original Authors");
            config.set("plugin.version_label", "Version");
            config.set("plugin.version_full", "4.0-Nextgen");
            config.set("commands.nekox.reloaded", "NekoX configuration has been reloaded!");
            config.set("commands.language.current", "Current language: %language%");
            config.set("commands.language.available", "Available languages: %languages%");
            config.set("commands.language.usage", "Usage: /nekox language <language>");
            config.set("commands.no_permission", "You don't have permission to execute this command!");
            
            // 猫薄荷效果消息
            config.set("catnip.effect", "You feel the effects of catnip!");
            
            // 命令消息
            config.set("commands.pat.success", "You petted %player% affectionately!");
            config.set("commands.lovebite.success", "You gave %player% a love bite!");
            config.set("commands.earscratch.success", "You scratched %player%'s ears!");
            config.set("commands.purr.success", "You purred cutely!");
            config.set("commands.hiss.success", "You hissed at %player%!");
            config.set("commands.scratch.success", "You scratched %player%!");
            config.set("commands.attention.success", "You attracted %player%'s attention!");
            config.set("commands.nightvision.success", "You gained night vision!");
            config.set("commands.jumpboost.success", "You gained jump boost!");
            config.set("commands.swiftsneak.success", "You gained swift sneak!");
            config.set("commands.health.name", "Health Restore");
            config.set("commands.health.description", "Description: Restore your and your owner's health");
            config.set("commands.health.cost", "Cost: %cost% hunger points");
            config.set("commands.health.success", "You used Health Restore! Gained %level% level regeneration effect!");
            config.set("commands.health.owner_success", "Your neko %neko% gave you regeneration effect! Gained %level% level regeneration effect!");
            config.set("commands.health.cooldown", "Skill is on cooldown! Wait %time% seconds.");
            config.set("commands.health.notneko", "You are not a neko!");
            config.set("commands.health.lowhunger", "You need %cost% hunger points to use this skill!");
            config.set("commands.health.noowner", "You don't have an owner, cannot use this skill!");
            config.set("commands.myskills.title", "Your Skills");
            config.set("commands.myskills.notplayer", "Only players can use this command!");
            config.set("commands.myskills.available", "Available");
            config.set("commands.myskills.cooldown", "Cooldown: %time%s");
            config.set("commands.myskills.cooldown-label", "Cooldown: ");
            config.set("commands.stress.name", "Stress Response");
            config.set("commands.stress.type", "Passive Skill");
            config.set("commands.stress.description", "Description: Automatically activated when health is below 6, gain Strength 50 for 1 minute");
            config.set("commands.stress.status-label", "Status: ");
            config.set("commands.stress.active", "Active");
            config.set("commands.stress.inactive", "Inactive");
            
            // 被动攻击增强
            config.set("commands.attackboost.name", "Passive Attack Boost");
            config.set("commands.attackboost.type", "Passive Skill");
            config.set("commands.attackboost.description", "Description: Slightly increased attack damage and knockback power");
            
            // 伤害调整功能消息
            config.set("damage.adjustment.message", "Damage adjusted: %.1f → %.1f (Reason: %s, Multiplier: %.1f)");
            config.set("damage.adjustment.fall_immunity", "You are immune to fall damage!");
            config.set("damage.adjustment.other_damage_boost", "You take more damage from other sources!");
            
            // 生物驱赶功能消息
            config.set("mob.repulsion.creeper_repelled", "Creeper has been repelled! Meow~");
            config.set("mob.repulsion.phantom_repelled", "Phantom has been repelled! Meow~");
            
            // 猫爪功能消息
            config.set("claws.sharpened", "Your claws have become sharper!");
            
            // 只吃肉类功能消息
            config.set("meat_only.restriction", "As a neko, you can only eat meat!");
            
            config.set("commands.language.changed", "Language has been changed to %language%!");
            config.set("commands.language.invalid", "Invalid language! Available languages: %languages%");
            config.set("commands.unknown", "Unknown command argument! Type /nekoxhelp for help");
            config.set("commands.help.title", "NekoX Help");
            config.set("commands.help.welcome", "Welcome to NekoX! Here are all available commands:");
            config.set("commands.help.interaction", "Interaction Commands");
            config.set("commands.help.effects", "Effect Commands");
            config.set("commands.help.management", "Management Commands");
            config.set("commands.help.owner_system", "Owner System Commands");
            config.set("commands.help.owner_list", "/owner list - View your owner list");
            config.set("commands.help.owner_mylist", "/owner mylist - View your nekos list");
            config.set("commands.help.ending", "Type commands in chat to use them!");
            config.set("commands.help.pat", "/pat <player> - Pet a neko");
            config.set("commands.help.lovebite", "/lovebite <player> - Give a love bite");
            config.set("commands.help.earscratch", "/earscratch <player> - Scratch a neko's ears");
            config.set("commands.help.purr", "/purr - Make a cute purring sound");
            config.set("commands.help.hiss", "/hiss <player> - Hiss at a player");
            config.set("commands.help.scratch", "/scratch <player> - Scratch a player");
            config.set("commands.help.attention", "/attention <player> - Attract a player's attention");
            config.set("commands.help.nightvision", "/nightvision [player] - Gain night vision effect");
            config.set("commands.help.jumpboost", "/jumpboost [player] - Gain jump boost effect");
            config.set("commands.help.swiftsneak", "/swiftsneak [player] - Gain swift sneak effect");
            config.set("commands.help.health", "/health - Neko restores own and owner's health");
            config.set("commands.help.nekox", "/nekox - View plugin information");
            config.set("commands.help.nekox_reload", "/nekox reload - Reload configuration");
            config.set("commands.help.nekox_version", "/nekox version - View plugin version");
            config.set("commands.help.nekoset", "/nekoset <player> <true/false> - Set player's neko status (requires admin permission)");
            config.set("commands.help.nekox_language", "/nekox language <language> - Change plugin language");
            config.set("commands.help.nekox_placeholders", "/nekox placeholders - View placeholder list");
            
            // 命令消息
            config.set("commands.only_player", "Only players can use this command!");
            
            // Owner command help
            config.set("commands.help.owner", "Owner Commands: /owner <add/accept/deny/remove/list/mylist/forceadd> [player]");
            config.set("commands.help.owner_add", "Usage: /owner add <player> - Send owner request to a player");
            config.set("commands.help.owner_accept", "Usage: /owner accept <player> - Accept owner request from a player");
            config.set("commands.help.owner_deny", "Usage: /owner deny <player> - Deny owner request from a player");
            config.set("commands.help.owner_remove", "Usage: /owner remove <player> - Remove owner/master relationship");
            config.set("commands.help.owner_forceadd", "Usage: /owner forceadd <player> - Force add owner relationship (admin only)");
            
            // Player messages
            config.set("commands.player_not_found", "Player not found!");
            
            // Owner command messages
            config.set("commands.owner.cant_add_self", "You can't add yourself as owner!");
            config.set("commands.owner.target_not_neko", "The target is not a neko!");
            config.set("commands.owner.already_owner", "You are already the owner of this neko!");
            config.set("commands.owner.request_already_sent", "You have already sent a request to this neko!");
            config.set("commands.owner.request_sent", "Owner request sent!");
            config.set("commands.owner.request_received", "You received an owner request from %player%!");
            config.set("commands.owner.request_help", "Type /owner accept %player% to accept, or /owner deny %player% to deny");
            config.set("commands.owner.not_neko", "You are not a neko!");
            config.set("commands.owner.no_pending_request", "No pending owner request from that player!");
            config.set("commands.owner.accepted_target", "You accepted %player% as your owner!");
            config.set("commands.owner.accepted_by_target", "%player% accepted your owner request!");
            config.set("commands.owner.denied_target", "You denied %player%'s owner request!");
            config.set("commands.owner.denied_by_target", "%player% denied your owner request!");
            config.set("commands.owner.remove_owner", "You removed %player% as your owner!");
            config.set("commands.owner.removed_as_owner", "You were removed as %player%'s owner!");
            config.set("commands.owner.remove_master", "You removed %player% as your master!");
            config.set("commands.owner.removed_as_master", "You were removed as %player%'s master!");
            config.set("commands.owner.no_owner_relation", "No owner relationship exists between you and that player!");
            config.set("commands.owner.no_owners", "You don't have any owners!");
            config.set("commands.owner.admin_tip", "Admin tip: Use /owner forceadd <player> to force add an owner");
            config.set("commands.owner.owners_list", "===== Your Owners ====");
            config.set("commands.owner.no_nekos", "You don't have any nekos!");
            config.set("commands.owner.nekos_list", "===== Your Nekos ====");
            config.set("commands.owner.target_not_neko_admin", "%player% is not a neko!");
            config.set("commands.owner.force_add_success", "You forcefully added %player% as your owner!");
            config.set("commands.owner.force_add_target", "%player% forcefully added you as their owner!");
            
            // NekoSet command messages
            config.set("commands.nekoset.invalid_param", "Invalid parameter! Please use true or false.");
            config.set("commands.nekoset.set_to_neko", "You set %player% to neko status!");
            config.set("commands.nekoset.set_to_neko_target", "You have been set to neko status!");
            config.set("commands.nekoset.set_to_human", "You set %player% to human status!");
            config.set("commands.nekoset.set_to_human_target", "You have been set to human status!");
            config.set("commands.nekoset.already_neko", "%player% is already a neko!");
            config.set("commands.nekoset.already_not_neko", "%player% is already not a neko!");
            
            config.save(englishFile);
        } catch (IOException e) {
            plugin.getLogger().severe("无法创建English.yml语言文件: " + e.getMessage());
        }
    }
    
    private void createDefaultChineseFile() {
        try {
            File chineseFile = new File(languagesDir, "简体中文.yml");
            Files.createFile(chineseFile.toPath());
            FileConfiguration config = YamlConfiguration.loadConfiguration(chineseFile);
            
            // 基本插件消息
            config.set("plugin.enabled", "NekoX插件已成功启用！");
            config.set("plugin.disabled", "NekoX插件已成功禁用！");
            config.set("plugin.name", "NekoX");
            config.set("plugin.version", "版本: 4.0-Nextgen");
            config.set("plugin.chinese_author", "此改版作者: _Chinese_Player_");
            config.set("plugin.help_info", "输入 /nekoxhelp 查看帮助");
            config.set("plugin.original_authors", "原作者");
            config.set("plugin.version_label", "版本");
            config.set("plugin.version_full", "4.0-Nextgen");
            config.set("commands.nekox.reloaded", "NekoX配置已重新加载！");
            config.set("commands.language.current", "当前语言: %language%");
            config.set("commands.language.available", "可用语言: %languages%");
            config.set("commands.language.usage", "用法: /nekox language <语言>");
            config.set("commands.no_permission", "你没有权限执行此命令！");
            
            // 猫薄荷效果消息
            config.set("catnip.effect", "你感受到了猫薄荷的效果！");
            
            // 命令消息
            config.set("commands.pat.success", "你温柔地抚摸了%player%！");
            config.set("commands.lovebite.success", "你给了%player%一个爱的咬痕！");
            config.set("commands.earscratch.success", "你挠了挠%player%的耳朵！");
            config.set("commands.purr.success", "你发出了可爱的呼噜声！");
            config.set("commands.hiss.success", "你对着%player%发出了嘶嘶声！");
            config.set("commands.scratch.success", "你抓伤了%player%！");
            config.set("commands.attention.success", "你吸引了%player%的注意！");
            config.set("commands.nightvision.success", "你获得了夜视能力！");
            config.set("commands.jumpboost.success", "你获得了跳跃提升效果！");
            config.set("commands.swiftsneak.success", "你获得了迅捷潜行效果！");
            config.set("commands.health.name", "健康恢复");
            config.set("commands.health.description", "描述: 恢复你和主人的生命值");
            config.set("commands.health.cost", "消耗: %cost%点饱食度");
            config.set("commands.health.success", "你使用了健康恢复！获得了%level%级生命恢复效果！");
            config.set("commands.health.owner_success", "你的猫娘%neko%给了你生命恢复效果！获得了%level%级生命恢复效果！");
            config.set("commands.health.cooldown", "技能正在冷却中！请等待%time%秒。");
            config.set("commands.health.notneko", "你不是猫娘！");
            config.set("commands.health.lowhunger", "你需要%cost%点饱食度才能使用此技能！");
            config.set("commands.health.noowner", "你没有主人，无法使用此技能！");
            config.set("commands.myskills.title", "你的技能");
            config.set("commands.myskills.notplayer", "只有玩家才能使用此命令！");
            config.set("commands.myskills.available", "可用");
            config.set("commands.myskills.cooldown", "冷却中: %time%s");
            config.set("commands.myskills.cooldown-label", "冷却中: ");
            config.set("commands.stress.name", "应激反应");
            config.set("commands.stress.type", "被动技能");
            config.set("commands.stress.description", "描述: 当生命值低于6时自动激活，获得50级力量效果，持续1分钟");
            config.set("commands.stress.status-label", "状态: ");
            config.set("commands.stress.active", "已激活");
            config.set("commands.stress.inactive", "未激活");
            
            // 被动攻击增强
            config.set("commands.attackboost.name", "被动攻击增强");
            config.set("commands.attackboost.type", "被动技能");
            config.set("commands.attackboost.description", "描述: 小幅提升攻击伤害和击退力度");
            
            // 伤害调整功能消息
            config.set("damage.adjustment.message", "伤害调整：%.1f → %.1f (原因：%s, 倍数：%.1f)");
            config.set("damage.adjustment.fall_immunity", "你免疫了跌落伤害！");
            config.set("damage.adjustment.other_damage_boost", "你受到了更多其他来源的伤害！");
            
            // 生物驱赶功能消息
            config.set("mob.repulsion.creeper_repelled", "苦力怕被驱赶了！喵~");
            config.set("mob.repulsion.phantom_repelled", "幻翼被驱赶了！喵~");
            
            // 猫爪功能消息
            config.set("claws.sharpened", "你的爪子变得更加锋利！");
            
            // 只吃肉类功能消息
            config.set("meat_only.restriction", "作为猫娘，你只能吃肉类食物！");
            
            config.set("commands.language.changed", "语言已更改为%language%！");
            config.set("commands.language.invalid", "无效的语言！可用语言: %languages%");
            config.set("commands.unknown", "未知的命令参数！输入/nekoxhelp获取帮助");
            config.set("commands.help.title", "NekoX帮助");
            config.set("commands.help.welcome", "欢迎使用NekoX！以下是所有可用命令：");
            config.set("commands.help.interaction", "互动命令");
            config.set("commands.help.effects", "效果命令");
            config.set("commands.help.management", "管理命令");
            config.set("commands.help.owner_system", "主人系统命令");
            config.set("commands.help.owner_list", "/owner list - 查看你的主人列表");
            config.set("commands.help.owner_mylist", "/owner mylist - 查看你的猫娘列表");
            config.set("commands.help.ending", "在聊天栏中输入命令来使用它们！");
            config.set("commands.help.pat", "/pat <玩家> - 抚摸猫娘");
            config.set("commands.help.lovebite", "/lovebite <玩家> - 给予爱的咬痕");
            config.set("commands.help.earscratch", "/earscratch <玩家> - 挠猫娘耳朵");
            config.set("commands.help.purr", "/purr - 发出可爱的呼噜声");
            config.set("commands.help.hiss", "/hiss <玩家> - 对着玩家发出嘶嘶声");
            config.set("commands.help.scratch", "/scratch <玩家> - 抓伤玩家");
            config.set("commands.help.attention", "/attention <玩家> - 吸引玩家注意");
            config.set("commands.help.nightvision", "/nightvision [玩家] - 获得夜视效果");
            config.set("commands.help.jumpboost", "/jumpboost [玩家] - 获得跳跃提升效果");
            config.set("commands.help.swiftsneak", "/swiftsneak [玩家] - 获得迅捷潜行效果");
            config.set("commands.help.health", "/health - 猫娘恢复自身和主人的生命值");
            config.set("commands.help.nekox", "/nekox - 查看插件信息");
            config.set("commands.help.nekox_reload", "/nekox reload - 重载配置");
            config.set("commands.help.nekox_version", "/nekox version - 查看插件版本");
            config.set("commands.help.nekoset", "/nekoset <玩家> <true/false> - 设置玩家的猫娘状态(需要管理员权限)");
            config.set("commands.help.nekox_language", "/nekox language <语言> - 更改插件语言");
            config.set("commands.help.nekox_placeholders", "/nekox placeholders - 查看占位符列表");
            
            // 通用命令消息
            config.set("commands.only_player", "只有玩家才能使用此命令！");
            
            // 主人命令帮助
            config.set("commands.help.owner", "主人命令: /owner <add/accept/deny/remove/list/mylist/forceadd> [玩家]");
            config.set("commands.help.owner_add", "用法: /owner add <玩家> - 向玩家发送主人请求");
            config.set("commands.help.owner_accept", "用法: /owner accept <玩家> - 接受玩家的主人请求");
            config.set("commands.help.owner_deny", "用法: /owner deny <玩家> - 拒绝玩家的主人请求");
            config.set("commands.help.owner_remove", "用法: /owner remove <玩家> - 移除主人/仆人的关系");
            config.set("commands.help.owner_forceadd", "用法: /owner forceadd <玩家> - 强制添加主人关系(仅管理员)");
            
            // 玩家消息
            config.set("commands.player_not_found", "找不到玩家！");
            
            // 主人命令消息
            config.set("commands.owner.cant_add_self", "你不能添加自己为所有者！");
            config.set("commands.owner.target_not_neko", "目标不是猫娘！");
            config.set("commands.owner.already_owner", "你已经是这个猫娘的所有者了！");
            config.set("commands.owner.request_already_sent", "你已经向这个猫娘发送了请求！");
            config.set("commands.owner.request_sent", "主人请求已发送！");
            config.set("commands.owner.request_received", "你收到了来自%player%的主人请求！");
            config.set("commands.owner.request_help", "输入 /owner accept %player% 接受，或 /owner deny %player% 拒绝");
            config.set("commands.owner.not_neko", "你不是猫娘！");
            config.set("commands.owner.no_pending_request", "该玩家没有待处理的主人请求！");
            config.set("commands.owner.accepted_target", "你接受了%player%作为你的主人！");
            config.set("commands.owner.accepted_by_target", "%player%接受了你的主人请求！");
            config.set("commands.owner.denied_target", "你拒绝了%player%的主人请求！");
            config.set("commands.owner.denied_by_target", "%player%拒绝了你的主人请求！");
            config.set("commands.owner.remove_owner", "你移除了%player%作为你的主人！");
            config.set("commands.owner.removed_as_owner", "你被%player%移除了主人身份！");
            config.set("commands.owner.remove_master", "你移除了%player%作为你的主人！");
            config.set("commands.owner.removed_as_master", "你被%player%移除了主人身份！");
            config.set("commands.owner.no_owner_relation", "你和该玩家之间不存在主人关系！");
            config.set("commands.owner.no_owners", "你没有任何主人！");
            config.set("commands.owner.admin_tip", "管理员提示: 使用 /owner forceadd <玩家> 强制添加主人");
            config.set("commands.owner.owners_list", "===== 你的主人 ====");
            config.set("commands.owner.no_nekos", "你没有任何猫娘！");
            config.set("commands.owner.nekos_list", "===== 你的猫娘 ====");
            config.set("commands.owner.target_not_neko_admin", "%player%不是猫娘！");
            config.set("commands.owner.force_add_success", "你强制将%player%添加为你的主人！");
            config.set("commands.owner.force_add_target", "%player%强制将你添加为他们的主人！");
            
            // NekoSet命令消息
            config.set("commands.nekoset.invalid_param", "参数无效！请使用true或false。");
            config.set("commands.nekoset.set_to_neko", "你将%player%设置为猫娘状态！");
            config.set("commands.nekoset.set_to_neko_target", "你已被设置为猫娘状态！");
            config.set("commands.nekoset.set_to_human", "你将%player%设置为人类状态！");
            config.set("commands.nekoset.set_to_human_target", "你已被设置为人类状态！");
            config.set("commands.nekoset.already_neko", "%player%已经是猫娘了！");
            config.set("commands.nekoset.already_not_neko", "%player%已经不是猫娘了！");
            
            config.save(chineseFile);
        } catch (IOException e) {
            plugin.getLogger().severe("无法创建简体中文.yml语言文件: " + e.getMessage());
        }
    }
    
    private void loadLanguages() {
        try {
            String[] languages = languageConfig.getStringList("Languages").toArray(new String[0]);
            for (String language : languages) {
                loadLanguageFile(language);
            }
        } catch (Exception e) {
            plugin.getLogger().severe("加载语言文件时出错: " + e.getMessage());
        }
    }
    
    public String getMessage(String key) {
        return getMessage(key, defaultLanguage, null);
    }
    
    public String getMessage(String key, String language) {
        return getMessage(key, language, null);
    }
    
    public String getMessage(String key, Map<String, String> placeholders) {
        return getMessage(key, defaultLanguage, placeholders);
    }
    
    public String getMessage(String key, String language, Map<String, String> placeholders) {
        // 检查语言是否存在
        if (!languageFiles.containsKey(language)) {
            language = defaultLanguage;
        }
        
        FileConfiguration config = languageFiles.get(language);
        if (config == null || !config.contains(key)) {
            // 如果在指定语言中找不到，则尝试在默认语言中查找
            if (!language.equals(defaultLanguage) && languageFiles.containsKey(defaultLanguage)) {
                config = languageFiles.get(defaultLanguage);
                if (config != null && config.contains(key)) {
                    return replacePlaceholders(config.getString(key), placeholders);
                }
            }
            // 如果都找不到，返回键本身
            return key;
        }
        
        return replacePlaceholders(config.getString(key), placeholders);
    }
    
    public String replacePlaceholders(String message, Map<String, String> placeholders) {
        if (message == null || placeholders == null || placeholders.isEmpty()) {
            return message;
        }
        
        String result = message;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        
        return result;
    }
    
    public void setLanguage(String language) {
        if (languageFiles.containsKey(language)) {
            defaultLanguage = language;
            languageConfig.set("Language", language);
            try {
                languageConfig.save(languageConfigFile);
            } catch (IOException e) {
                plugin.getLogger().severe("无法保存语言配置: " + e.getMessage());
            }
        }
    }
    
    public String getCurrentLanguage() {
        return defaultLanguage;
    }
    
    public Set<String> getAvailableLanguages() {
        return languageFiles.keySet();
    }
    
    public void reload() {
        languageFiles.clear();
        languageConfig = YamlConfiguration.loadConfiguration(languageConfigFile);
        defaultLanguage = languageConfig.getString("Language", "English");
        loadDefaultLanguageFiles();
        loadLanguages();
    }
}