package org.cneko.nekox.utils;

import org.bukkit.Bukkit;
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
        
        // 先初始化必要的文件对象，避免在异常情况下未初始化
        this.languagesDir = new File(plugin.getDataFolder(), "Languages");
        this.languageConfigFile = new File(plugin.getDataFolder(), "Language.yml");
        
        try {
            // 确保插件数据文件夹存在
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            
            // 创建Languages文件夹
            if (!languagesDir.exists()) {
                languagesDir.mkdirs();
            }
            
            // 创建Language.yml配置文件
            if (!languageConfigFile.exists()) {
                createDefaultLanguageConfig();
            }
            
            languageConfig = YamlConfiguration.loadConfiguration(languageConfigFile);
            defaultLanguage = languageConfig.getString("Language", "English");
            
            // 加载默认语言文件
            loadDefaultLanguageFiles();
            
            // 加载配置的语言列表
            loadLanguages();
        } catch (Exception e) {
            // 使用Bukkit.getLogger()直接输出，避免依赖languageManager
            Bukkit.getLogger().severe("§dNekoX §e>> Language manager initialization failed, plugin will run in English mode: " + e.getMessage());
            // 即使失败也要确保基础功能可用
            createFallbackLanguageConfig();
        }
    }
    
    /**
     * 创建后备语言配置，确保即使文件初始化失败也能提供基础消息
     */
    private void createFallbackLanguageConfig() {
        try {
            // 创建内存中的后备配置
            FileConfiguration fallbackConfig = new org.bukkit.configuration.file.YamlConfiguration();
            
            // 基础插件消息
            fallbackConfig.set("plugin.enabled", "NekoX plugin has been successfully enabled!");
            fallbackConfig.set("plugin.disabled", "NekoX plugin has been successfully disabled!");
            fallbackConfig.set("plugin.name", "NekoX");
            fallbackConfig.set("plugin.disabled", "NekoX plugin has been successfully disabled!");
            
            // 命令消息
            fallbackConfig.set("commands.only_player", "Only players can use this command!");
            fallbackConfig.set("commands.health.notneko", "You are not a neko!");
            fallbackConfig.set("commands.player_not_found", "Player not found!");
            fallbackConfig.set("commands.pat.success", "You patted {player}!");
            fallbackConfig.set("commands.lovebite.success", "You gave {player} a love bite!");
            fallbackConfig.set("commands.earscratch.success", "You scratched {player}'s ears!");
            fallbackConfig.set("commands.purr.success", "You made a cute purring sound!");
            fallbackConfig.set("commands.hiss.success", "You hissed at {player}!");
            fallbackConfig.set("commands.scratch.success", "You scratched {player}!");
            fallbackConfig.set("commands.attention.success", "You attracted {player}'s attention!");
            fallbackConfig.set("commands.nightvision.success", "You gained night vision!");
            fallbackConfig.set("commands.jumpboost.success", "You gained jump boost effect!");
            fallbackConfig.set("commands.swiftsneak.success", "You gained swift sneak effect!");
            
            // 将后备配置添加到内存映射中
            languageFiles.put("English", fallbackConfig);
            plugin.getLogger().info("§dNekoX §e>> Fallback language config created, plugin will run in basic English mode");
        } catch (Exception e) {
            plugin.getLogger().severe("§dNekoX §e>> Failed to create fallback language config: " + e.getMessage());
        }
    }
    
    private void createDefaultLanguageConfig() {
        try {
            Files.createFile(languageConfigFile.toPath());
            FileConfiguration config = YamlConfiguration.loadConfiguration(languageConfigFile);
            config.set("Language", "English");
            config.set("Languages", java.util.Arrays.asList("English", "简体中文"));
            config.save(languageConfigFile);
        } catch (IOException e) {
            plugin.getLogger().severe("无法创建Language.yml配置文件: " + e.getMessage());
        }
    }
    
    private void loadDefaultLanguageFiles() {
        // 创建或更新默认的English语言文件
        File englishFile = new File(languagesDir, "English.yml");
        createDefaultEnglishFile();
        
        // 创建或更新默认的简体中文语言文件
        File chineseFile = new File(languagesDir, "简体中文.yml");
        createDefaultChineseFile();
        
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
                plugin.getLogger().severe("§dNekoX §e>> Failed to load language file " + language + ": " + e.getMessage());
            }
        } else {
            plugin.getLogger().warning("§dNekoX §e>> Language file " + language + ".yml not found, this language translation will be unavailable");
        }
    }
    
    private void createDefaultEnglishFile() {
        try {
            File englishFile = new File(languagesDir, "English.yml");
            FileConfiguration config;
            
            // 如果文件存在，加载现有配置；否则创建新文件
            if (englishFile.exists()) {
                config = YamlConfiguration.loadConfiguration(englishFile);
            } else {
                Files.createFile(englishFile.toPath());
                config = YamlConfiguration.loadConfiguration(englishFile);
            }
            
            // 英文配置
            config.set("plugin.version", "Version: 5.2-ProMax+++");
            config.set("plugin.version_full", "5.2-ProMax+++");
            config.set("plugin.author", "Shabby");
            config.set("plugin.github", "https://github.com/Shabby-666/NekoX");
            
            // 基本插件消息
            config.set("plugin.enabled", "NekoX plugin has been successfully enabled!");
            config.set("plugin.disabled", "NekoX plugin has been successfully disabled!");
            config.set("plugin.name", "NekoX");
            config.set("plugin.chinese_author", "This modified version author: _Chinese_Player_");
            config.set("plugin.help_info", "Type /nekoxhelp for help");
            config.set("plugin.original_authors", "Original Authors");
            config.set("plugin.version_label", "Version");
            config.set("commands.nekox.reloaded", "NekoX configuration has been reloaded!");
            config.set("commands.language.current", "Current language: %language%");
            config.set("commands.language.available", "Available languages: %languages%");
            config.set("commands.language.usage", "Usage: /nekox language <language>");
            config.set("commands.no_permission", "You don't have permission to execute this command!");
            
            // 猫薄荷效果消息
            config.set("catnip.effect", "You feel the effects of catnip!");
            
            // 命令消息
            config.set("commands.pat.success", "You gently petted %player%!");
            config.set("commands.lovebite.success", "You gave %player% a love bite!");
            config.set("commands.earscratch.success", "You scratched %player%'s ears!");
            config.set("commands.purr.success", "You made a cute purring sound!");
            config.set("commands.hiss.success", "You hissed at %player%!");
            config.set("commands.scratch.success", "You scratched %player%!");
            config.set("commands.attention.success", "You attracted %player%'s attention!");
            config.set("commands.nightvision.success", "You gained night vision!");
            config.set("commands.jumpboost.success", "You gained jump boost effect!");
            config.set("commands.swiftsneak.success", "You gained swift sneak effect!");
            config.set("commands.health.name", "Health Recovery");
            config.set("commands.health.description", "Description: Restore your and your owner's health");
            config.set("commands.health.cost", "Cost: %cost% hunger points");
            config.set("commands.health.success", "You used health recovery! Gained %level% level health regeneration effect!");
            config.set("commands.health.owner_success", "Your neko %neko% gave you health regeneration effect! Gained %level% level health regeneration effect!");
            config.set("commands.health.cooldown", "Skill is on cooldown! Please wait %time% seconds.");
            config.set("commands.health.notneko", "You are not a neko!");
            config.set("commands.health.lowhunger", "You need %cost% hunger points to use this skill!");
            config.set("commands.health.noowner", "You don't have an owner, can't use this skill!");
            config.set("commands.myskills.title", "Your Skills");
            config.set("commands.myskills.notplayer", "Only players can use this command!");
            config.set("commands.myskills.available", "Available");
            config.set("commands.myskills.cooldown", "Cooldown: %time%s");
            config.set("commands.myskills.cooldown-label", "Cooldown: ");
            config.set("commands.stress.name", "Stress Response");
            config.set("commands.stress.type", "Passive Skill");
            config.set("commands.stress.description", "Description: Automatically activates when health is below 6, gain level 50 strength effect for 1 minute");
            config.set("commands.stress.status-label", "Status: ");
            config.set("commands.stress.active", "Active");
            config.set("commands.stress.inactive", "Inactive");
            
            // 被动攻击增强
            config.set("commands.attackboost.name", "Passive Attack Boost");
            config.set("commands.attackboost.type", "Passive Skill");
            config.set("commands.attackboost.description", "Description: Slightly increased attack damage and knockback power");
            
            // 伤害调整功能消息
            config.set("damage.adjustment.message", "Damage adjustment: %.1f → %.1f (Reason: %s, Multiplier: %.1f)");
            config.set("damage.adjustment.fall_immunity", "You are immune to fall damage!");
            
            // Tail pull feature messages
            config.set("tailpull.pull_message", "You pulled {target}'s tail");
            config.set("tailpull.pull_message_target", "{player} pulled your tail");
            config.set("tailpull.command.toggle_enabled", "Tail pull feature enabled");
            config.set("tailpull.command.toggle_disabled", "Tail pull feature disabled");
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
            config.set("commands.help.myskills", "/myskills - View all neko skills and their cooldowns");
            config.set("commands.help.playernotice", "/playernotice [on|off] - Toggle player proximity notifications");
            config.set("commands.help.nekox", "/nekox - View plugin information");
            config.set("commands.help.nekox_reload", "/nekox reload - Reload configuration");
            config.set("commands.help.nekox_version", "/nekox version - View plugin version");
            config.set("commands.help.nekoset", "/nekoset <player> <true/false> - Set player's neko status (requires admin permission)");
            config.set("commands.help.nekox_language", "/nekox language <language> - Change plugin language");
            config.set("commands.help.nekox_placeholders", "/nekox placeholders - View placeholder list");
            config.set("commands.help.pullthetail", "/pullthetail - Toggle tail pull feature");
            
            // 命令消息
            config.set("commands.only_player", "Only players can use this command!");
            
            // 启动信息
            config.set("plugin.starting", "Enabling NekoX......");
            config.set("plugin.enabled", "NekoX plugin has been successfully enabled! Meow~♪");
            config.set("plugin.star_prompt", "If you like it, please give a Star qwq");
            config.set("plugin.github", "Github: https://github.com/Shabby-666/NekoX");
            config.set("plugin.startup_error", "A serious error occurred during plugin startup: ");
            config.set("plugin.components_failed", "Plugin core components initialization failed, plugin will be disabled");
            config.set("plugin.config_loaded", "Configuration file loaded successfully");
            config.set("plugin.api_initialized", "API initialized successfully");
            config.set("plugin.api_failed", "API initialization failed, but plugin will continue to run: ");
            config.set("plugin.listeners_registered", "Event listeners registered successfully");
            config.set("plugin.listeners_failed", "Event listeners registration failed: ");
            config.set("plugin.commands_registered", "Commands registered successfully");
            config.set("plugin.commands_failed", "Commands registration failed: ");
            config.set("plugin.stats_initialized", "Statistics initialized successfully");
            config.set("plugin.stats_failed", "Statistics initialization failed: ");
            config.set("plugin.placeholder_failed", "PlaceholderAPI registration failed: ");
            config.set("plugin.placeholder_registering", "Registering placeholders......");
            config.set("plugin.placeholder_enabled", "PlaceholderAPI support enabled!");
            config.set("plugin.placeholder_registered", "Placeholders registered successfully! Use /nekox placeholders to view the list of placeholders");
            config.set("plugin.placeholder_error", "An exception occurred while registering PlaceholderAPI expansion: ");
            config.set("plugin.placeholder_not_installed", "PlaceholderAPI is not installed, placeholder features will be unavailable.");
            config.set("plugin.init_players_config", "Initializing player configuration manager...");
            config.set("plugin.players_config_done", "Player configuration manager initialized successfully");
            config.set("plugin.init_language", "Initializing language manager...");
            config.set("plugin.language_done", "Language manager initialized successfully");
            config.set("plugin.init_skills", "Initializing skill manager...");
            config.set("plugin.skills_done", "Skill manager initialized successfully");
            config.set("plugin.init_neko", "Initializing neko manager...");
            config.set("plugin.neko_done", "Neko manager initialized successfully");
            config.set("plugin.init_component_error", "Error occurred during component initialization: ");
            config.set("plugin.init_unexpected_error", "Unexpected error occurred during component initialization: ");
            
            // 数据库相关消息
            config.set("database.sqlite_driver_loaded", "SQLite JDBC driver loaded successfully");
            config.set("database.sqlite_driver_not_found", "SQLite JDBC driver not found! Please make sure sqlite-jdbc dependency is added");
            config.set("database.creating_data_folder", "Creating plugin data folder: %path%");
            config.set("database.file_path", "Database file path: %path%");
            config.set("database.file_not_writable", "Database file is not writable: %path%");
            config.set("database.connecting", "Attempting to connect to database: %url%");
            config.set("database.connected", "Database connection successful!");
            config.set("database.foreign_keys_enabled", "Foreign key constraints enabled");
            config.set("database.table_created", "Created/verified %table% table");
            config.set("database.initialized", "Database initialization completed");
            config.set("database.connection_failed", "Database connection failed: Connection object is null or closed");
            config.set("database.sql_error", "Database SQL error: ");
            config.set("database.error_code", "Error code: %code%");
            config.set("database.sql_state", "SQL state: %state%");
            config.set("database.init_exception", "Database initialization exception: %exception%: %message%");
            config.set("database.column_added", "Added missing column: %column%");
            config.set("database.table_check_error", "Error checking or fixing table structure: %message%");
            config.set("database.memory_mode_initialized", "Initialized memory mode, player data will be lost after plugin restart");
            config.set("database.skip_loading", "Memory mode or database unavailable, skipping loading configuration");
            config.set("database.loaded_configs", "Loaded %count% player configurations");
            config.set("database.loaded_owner_relations", "Loaded %count% owner relationships");
            config.set("database.loaded_requests", "Loaded %count% owner requests");
            config.set("database.connection_closed", "Database connection closed");
            config.set("database.close_failed", "Failed to close database connection: %message%");
            config.set("database.operation_failed", "Database operation failed, data saved to memory");
            config.set("database.connection_failed_memory", "Database connection failed, running in memory mode");
            config.set("database.init_manager_failed", "Failed to initialize player configuration manager: ");
            config.set("database.using_memory_mode", "Running in memory mode");
            
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
            
            // Player notice command messages
            config.set("commands.playernotice.enabled", "Player notice is currently enabled");
            config.set("commands.playernotice.disabled", "Player notice is currently disabled");
            config.set("commands.playernotice.enabled_success", "Player notice has been enabled");
            config.set("commands.playernotice.disabled_success", "Player notice has been disabled");
            config.set("commands.playernotice.usage", "Usage: /playernotice [on|off]");
            
            // Player notice messages
            config.set("player_notice.title", "Nearby Players");
            
            // 爬墙命令消息
            config.set("commands.climb.enabled", "Climbing feature enabled!");
            config.set("commands.climb.disabled", "Climbing feature disabled!");
            
            config.save(englishFile);
        } catch (IOException e) {
            plugin.getLogger().severe("无法创建English.yml语言文件: " + e.getMessage());
        }
    }

    private void createDefaultChineseFile() {
        try {
            File chineseFile = new File(languagesDir, "简体中文.yml");
            FileConfiguration config;
            
            // 如果文件存在，加载现有配置；否则创建新文件
            if (chineseFile.exists()) {
                config = YamlConfiguration.loadConfiguration(chineseFile);
            } else {
                Files.createFile(chineseFile.toPath());
                config = YamlConfiguration.loadConfiguration(chineseFile);
            }
            
            // 基本插件消息
            config.set("plugin.enabled", "NekoX插件已成功启用！");
            config.set("plugin.disabled", "NekoX插件已成功禁用！");
            config.set("plugin.name", "NekoX");
            config.set("plugin.chinese_author", "此改版作者: _Chinese_Player_");
            config.set("plugin.help_info", "输入 /nekoxhelp 查看帮助");
            config.set("plugin.original_authors", "原作者");
            config.set("plugin.version_label", "版本");
            config.set("plugin.version_full", "5.2-ProMax+++");
            config.set("plugin.version", "版本: 5.2-ProMax+++");
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
            
            // 尾巴拉扯功能消息
            config.set("tailpull.pull_message", "你薅了一下{target}的尾巴");
            config.set("tailpull.pull_message_target", "{player}薅了一下你的尾巴");
            config.set("tailpull.command.toggle_enabled", "尾巴拉扯功能已开启");
            config.set("tailpull.command.toggle_disabled", "尾巴拉扯功能已关闭");
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
            config.set("commands.help.myskills", "/myskills - 查看所有猫娘技能及其冷却时间");
            config.set("commands.help.playernotice", "/playernotice [on|off] - 开启/关闭玩家接近提醒功能");
            config.set("commands.help.nekox", "/nekox - 查看插件信息");
            config.set("commands.help.nekox_reload", "/nekox reload - 重载配置");
            config.set("commands.help.nekox_version", "/nekox version - 查看插件版本");
            config.set("commands.help.nekoset", "/nekoset <玩家> <true/false> - 设置玩家的猫娘状态(需要管理员权限)");
            config.set("commands.help.nekox_language", "/nekox language <语言> - 更改插件语言");
            config.set("commands.help.nekox_placeholders", "/nekox placeholders - 查看占位符列表");
            config.set("commands.help.pullthetail", "/pullthetail - 开关尾巴拉扯功能");
            
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
            
            // Player notice command messages
            config.set("commands.playernotice.enabled", "玩家接近提醒功能当前已启用");
            config.set("commands.playernotice.disabled", "玩家接近提醒功能当前已禁用");
            config.set("commands.playernotice.enabled_success", "玩家接近提醒功能已启用");
            config.set("commands.playernotice.disabled_success", "玩家接近提醒功能已禁用");
            config.set("commands.playernotice.usage", "用法: /playernotice [on|off]");
            
            // Player notice title messages
            config.set("player_notice.title", "附近玩家");
            
            // 爬墙命令消息
            config.set("commands.climb.enabled", "爬墙功能已开启！");
            config.set("commands.climb.disabled", "爬墙功能已关闭！");
            
            // 启动信息
            config.set("plugin.starting", "正在启用NekoX......");
            config.set("plugin.enabled", "NekoX插件已成功启用！喵~♪");
            config.set("plugin.star_prompt", "喜欢的话请给个Star吧qwq");
            config.set("plugin.github", "Github: https://github.com/Shabby-666/NekoX");
            config.set("plugin.startup_error", "插件启动过程中发生严重错误: ");
            config.set("plugin.components_failed", "插件核心组件初始化失败，插件将禁用");
            config.set("plugin.config_loaded", "配置文件加载完成");
            config.set("plugin.api_initialized", "API初始化完成");
            config.set("plugin.api_failed", "API初始化失败，但插件将继续运行: ");
            config.set("plugin.listeners_registered", "事件监听器注册完成");
            config.set("plugin.listeners_failed", "事件监听器注册失败: ");
            config.set("plugin.commands_registered", "命令注册完成");
            config.set("plugin.commands_failed", "命令注册失败: ");
            config.set("plugin.stats_initialized", "统计初始化完成");
            config.set("plugin.stats_failed", "统计初始化失败: ");
            config.set("plugin.placeholder_failed", "PlaceholderAPI注册失败: ");
            config.set("plugin.placeholder_registering", "正在注册占位符......");
            config.set("plugin.placeholder_enabled", "PlaceholderAPI支持已启用！");
            config.set("plugin.placeholder_registered", "占位符注册成功！使用/nekox placeholders查看占位符列表");
            config.set("plugin.placeholder_error", "注册PlaceholderAPI扩展时发生异常: ");
            config.set("plugin.placeholder_not_installed", "PlaceholderAPI未安装，占位符功能将不可用。");
            config.set("plugin.init_players_config", "正在初始化玩家配置管理器...");
            config.set("plugin.players_config_done", "玩家配置管理器初始化完成");
            config.set("plugin.init_language", "正在初始化语言管理器...");
            config.set("plugin.language_done", "语言管理器初始化完成");
            config.set("plugin.init_skills", "正在初始化技能管理器...");
            config.set("plugin.skills_done", "技能管理器初始化完成");
            config.set("plugin.init_neko", "正在初始化猫娘管理器...");
            config.set("plugin.neko_done", "猫娘管理器初始化完成");
            config.set("plugin.init_component_error", "组件初始化过程中发生错误: ");
            config.set("plugin.init_unexpected_error", "组件初始化过程中发生未预期的错误: ");
            
            // 数据库相关消息
            config.set("database.sqlite_driver_loaded", "SQLite JDBC驱动已加载");
            config.set("database.sqlite_driver_not_found", "SQLite JDBC驱动未找到！请确保已添加sqlite-jdbc依赖");
            config.set("database.creating_data_folder", "创建插件数据文件夹: %path%");
            config.set("database.file_path", "数据库文件路径: %path%");
            config.set("database.file_not_writable", "数据库文件不可写: %path%");
            config.set("database.connecting", "尝试连接数据库: %url%");
            config.set("database.connected", "数据库连接成功！");
            config.set("database.foreign_keys_enabled", "启用外键约束");
            config.set("database.table_created", "创建/验证 %table% 表");
            config.set("database.initialized", "数据库初始化完成");
            config.set("database.connection_failed", "数据库连接失败：连接对象为null或已关闭");
            config.set("database.sql_error", "数据库SQL错误: ");
            config.set("database.error_code", "错误代码: %code%");
            config.set("database.sql_state", "SQL状态: %state%");
            config.set("database.init_exception", "数据库初始化异常: %exception%: %message%");
            config.set("database.column_added", "已添加缺失的列: %column%");
            config.set("database.table_check_error", "检查或修复表结构时出错: %message%");
            config.set("database.memory_mode_initialized", "初始化内存模式，玩家数据将在插件重启后丢失");
            config.set("database.skip_loading", "内存模式或数据库不可用，跳过加载配置");
            config.set("database.loaded_configs", "加载了 %count% 个玩家配置");
            config.set("database.loaded_owner_relations", "加载了 %count% 个主人关系");
            config.set("database.loaded_requests", "加载了 %count% 个主人申请");
            config.set("database.connection_closed", "数据库连接已关闭");
            config.set("database.close_failed", "关闭数据库连接失败: %message%");
            config.set("database.operation_failed", "数据库操作失败，数据已保存到内存");
            config.set("database.connection_failed_memory", "数据库连接失败，将使用内存模式运行");
            config.set("database.init_manager_failed", "初始化玩家配置管理器失败: ");
            config.set("database.using_memory_mode", "将使用内存模式运行");
            
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
        if (key == null || key.trim().isEmpty()) {
            return "";
        }
        return getMessage(key, defaultLanguage, null);
    }
    
    public String getMessage(String key, String language) {
        if (key == null || key.trim().isEmpty()) {
            return "";
        }
        if (language == null || language.trim().isEmpty()) {
            language = defaultLanguage;
        }
        return getMessage(key, language, null);
    }
    
    public String getMessage(String key, Map<String, String> placeholders) {
        if (key == null || key.trim().isEmpty()) {
            return "";
        }
        return getMessage(key, defaultLanguage, placeholders);
    }
    
    public String getMessage(String key, String language, Map<String, String> placeholders) {
        if (key == null || key.trim().isEmpty()) {
            return "";
        }
        
        // 检查语言是否存在
        if (language == null || language.trim().isEmpty() || !languageFiles.containsKey(language)) {
            language = defaultLanguage;
        }
        
        FileConfiguration config = languageFiles.get(language);
        if (config == null || !config.contains(key)) {
            // 如果在指定语言中找不到，则尝试在默认语言中查找
            if (!language.equals(defaultLanguage) && languageFiles.containsKey(defaultLanguage)) {
                config = languageFiles.get(defaultLanguage);
                if (config != null && config.contains(key)) {
                    String message = config.getString(key);
                    return message != null ? replacePlaceholders(message, placeholders) : key;
                }
            }
            // 如果都找不到，返回键本身
            return key;
        }
        
        String message = config.getString(key);
        String result = message != null ? replacePlaceholders(message, placeholders) : key;
        // 为标题相关和版本相关的翻译键不添加前缀
        if ("plugin.name".equals(key) || "plugin.version_label".equals(key) || "plugin.version_full".equals(key) || "plugin.version".equals(key) || "plugin.original_authors".equals(key) || "commands.help.title".equals(key) || "commands.help.interaction".equals(key) || "commands.help.effects".equals(key) || "commands.help.management".equals(key) || "commands.help.owner_system".equals(key) || "player_notice.title".equals(key)) {
            return result; // 标题和版本相关的翻译键不添加前缀和颜色
        }
        // 在消息前添加前缀，除非是帮助信息或特殊格式的消息
        if (!result.startsWith("=====") && !result.startsWith("Version: ") && !result.startsWith("版本: ") && !result.startsWith("§6=====") && !result.startsWith("§a=====") && !result.startsWith("§ePlaceholderAPI") && !result.startsWith("§cPlaceholderAPI") && !result.startsWith("§e成功注册的占位符") && !result.startsWith("§a所有占位符") && !result.startsWith("§e- %nekox_") && !result.startsWith("§c提示：")) {
            result = "§dNekoX §e>> " + result;
        } else {
            // 为特殊格式的消息添加颜色代码
            if (result.startsWith("=====")) {
                result = "§6" + result; // 标题使用金色
            } else if (result.startsWith("Version: ") || result.startsWith("版本: ")) {
                result = "§e" + result; // 版本信息使用黄色
            } else if (result.startsWith("You don't have permission") || result.startsWith("You are not a neko") || result.startsWith("Invalid language") || result.startsWith("Unknown command") || result.startsWith("你没有权限") || result.startsWith("你不是猫娘") || result.startsWith("无效的语言") || result.startsWith("未知的命令") || result.startsWith("NekoX插件语言系统未正确初始化") || result.startsWith("Player not found") || result.startsWith("玩家未找到") || result.startsWith("无效的参数")) {
                result = "§c" + result; // 错误消息使用红色
            } else if (result.startsWith("You gently petted") || result.startsWith("You gave") || result.startsWith("You scratched") || result.startsWith("You made") || result.startsWith("You hissed") || result.startsWith("You attracted") || result.startsWith("You gained") || result.startsWith("You used") || result.startsWith("Your neko") || result.startsWith("Skill is on cooldown") || result.startsWith("You need") || result.startsWith("You don't have") || result.startsWith("You pulled") || result.startsWith("Tail pull feature") || result.startsWith("You feel") || result.startsWith("You are immune") || result.startsWith("You take more") || result.startsWith("Creeper has been") || result.startsWith("Phantom has been") || result.startsWith("Your claws have") || result.startsWith("As a neko") || result.startsWith("Language has been changed") || result.startsWith("你温柔地抚摸了") || result.startsWith("你给了") || result.startsWith("你挠了挠") || result.startsWith("你发出了") || result.startsWith("你对着") || result.startsWith("你吸引了") || result.startsWith("你获得了") || result.startsWith("你使用了") || result.startsWith("你的猫娘") || result.startsWith("技能正在冷却中") || result.startsWith("你需要") || result.startsWith("你没有") || result.startsWith("你薅了一下") || result.startsWith("尾巴拉扯功能") || result.startsWith("你感受到了") || result.startsWith("你免疫了") || result.startsWith("你受到了更多") || result.startsWith("苦力怕被驱赶了") || result.startsWith("幻翼被驱赶了") || result.startsWith("你的爪子变得") || result.startsWith("作为猫娘") || result.startsWith("语言已更改为") || result.startsWith("NekoX配置已重新加载") || result.startsWith("插件已成功启用") || result.startsWith("插件已成功禁用")) {
                result = "§e" + result; // 成功消息和状态消息使用黄色
            } else if (result.startsWith("Current language") || result.startsWith("Available languages") || result.startsWith("Usage:") || result.startsWith("当前语言") || result.startsWith("可用语言") || result.startsWith("用法:") || result.startsWith("输入 /nekoxhelp 查看帮助")) {
                result = "§a" + result; // 信息类消息使用绿色
            }
        }
        return result;
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