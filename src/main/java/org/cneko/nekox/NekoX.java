package org.cneko.nekox;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.cneko.nekox.api.NekoXAPI;
import org.cneko.nekox.commands.Attention;
import org.cneko.nekox.commands.EarScratch;
import org.cneko.nekox.commands.HealthCommand;
import org.cneko.nekox.commands.Hiss;
import org.cneko.nekox.commands.Jumpboost;
import org.cneko.nekox.commands.Lovebite;
import org.cneko.nekox.commands.MySkillsCommand;
import org.cneko.nekox.commands.NekoSetCommand;
import org.cneko.nekox.commands.NekoXCommand;
import org.cneko.nekox.commands.NekoXHelp;
import org.cneko.nekox.commands.Nightvision;
import org.cneko.nekox.commands.OwnerCommand;
import org.cneko.nekox.commands.Pat;
import org.cneko.nekox.commands.PlayerNoticeCommand;
import org.cneko.nekox.commands.Climb;
import org.cneko.nekox.commands.PullTheTailCommand;
import org.cneko.nekox.commands.Purr;
import org.cneko.nekox.commands.Scratch;
import org.cneko.nekox.commands.SwiftSneak;
import org.cneko.nekox.events.ArmorEvent;
import org.cneko.nekox.events.NekoChat;
import org.cneko.nekox.events.MeatOnly;
import org.cneko.nekox.events.CatNip;
import org.cneko.nekox.events.Claws;
import org.cneko.nekox.events.OwnerDeathListener;
import org.cneko.nekox.events.NightEffectsListener;
import org.cneko.nekox.events.StressEffectListener;
import org.cneko.nekox.events.PassiveAttackBoost;
import org.cneko.nekox.events.NekoDamageListener;
import org.cneko.nekox.events.NekoMobBehaviorListener;

import org.cneko.nekox.events.ClimbListener;
import org.cneko.nekox.events.PlayerProximityListener;
import org.cneko.nekox.events.TailPullListener;
import org.cneko.nekox.utils.NekoManager;
import org.cneko.nekox.utils.SkillManager;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.NekoXPlaceholderExpansion;
import org.cneko.nekox.utils.PlayerConfigManagerSafe;

import java.util.ArrayList;
import java.util.List;

public class NekoX extends JavaPlugin {
    private static NekoX instance;
    private NekoManager nekoManager;
    private SkillManager skillManager;
    private NightEffectsListener nightEffectsListener;
    private LanguageManager languageManager;
    private PlayerConfigManagerSafe playerConfigManager;
    private Climb climbCommand; // 添加爬墙命令引用
    
    // 保存已注册的监听器和命令引用，以便在插件禁用时注销它们
    private final List<Object> registeredListeners = new ArrayList<>();
    private final List<CommandExecutor> registeredCommands = new ArrayList<>();
    
    @Override
    public void onEnable() {
        instance = this;
        
        try {
            // 加载配置
            saveDefaultConfig();
            
            // 按顺序初始化核心组件，确保每个组件都成功初始化
            if (!initializeComponents()) {
                getLogger().severe("插件核心组件初始化失败，插件将禁用");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            
            // 配置已加载，使用languageManager发送消息
            getLogger().info(languageManager.getMessage("plugin.config_loaded"));
            
            // 初始化API
            try {
                NekoXAPI.initialize(this);
                getLogger().info(languageManager.getMessage("plugin.api_initialized"));
            } catch (Exception e) {
                getLogger().warning(languageManager.getMessage("plugin.api_failed") + e.getMessage());
            }
            
            // 注册事件监听器
            try {
                registerEvents();
                getLogger().info(languageManager.getMessage("plugin.listeners_registered"));
            } catch (Exception e) {
                getLogger().warning(languageManager.getMessage("plugin.listeners_failed") + e.getMessage());
            }
            
            // 注册命令
            try {
                registerCommands();
                getLogger().info(languageManager.getMessage("plugin.commands_registered"));
            } catch (Exception e) {
                getLogger().warning(languageManager.getMessage("plugin.commands_failed") + e.getMessage());
            }
            
            // 初始化统计
            try {
                initStats();
                getLogger().info(languageManager.getMessage("plugin.stats_initialized"));
            } catch (Exception e) {
                getLogger().warning(languageManager.getMessage("plugin.stats_failed") + e.getMessage());
            }
            
            // 注册PlaceholderAPI扩展
            try {
                registerPlaceholderAPI();
            } catch (Exception e) {
                getLogger().warning(languageManager.getMessage("plugin.placeholder_failed") + e.getMessage());
            }
            
            // 显示欢迎信息
            getLogger().info(languageManager.getMessage("plugin.starting"));
            getLogger().info("");
            getLogger().info("███╗   ██╗ ███████╗ ██╗  ██╗  ██████╗  ██╗  ██╗");
            getLogger().info("████╗  ██║ ██╔════╝ ██║ ██╔╝ ██╔═══██╗ ╚██╗██╔╝");
            getLogger().info("██╔██╗ ██║ █████╗   █████╔╝  ██║   ██║  ╚███╔╝ ");
            getLogger().info("██║╚██╗██║ ██╔══╝   ██╔═██╗  ██║   ██║  ██╔██╗ ");
            getLogger().info("██║ ╚████║ ███████╗ ██║  ██╗ ╚██████╔╝ ██╔╝ ██╗");
            getLogger().info("╚═╝  ╚═══╝ ╚══════╝ ╚═╝  ╚═╝  ╚═════╝  ╚═╝  ╚═╝");
            getLogger().info(languageManager.getMessage("plugin.enabled"));
            getLogger().info(languageManager.getMessage("plugin.star_prompt"));
            getLogger().info(languageManager.getMessage("plugin.github"));
            
        } catch (Exception e) {
            // 现在可以安全使用languageManager，因为它已经在initializeComponents()最开始初始化了
            getLogger().severe(languageManager.getMessage("plugin.startup_error") + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    
    /**
     * 按安全顺序初始化所有核心组件
     */
    private boolean initializeComponents() {
        // 首先单独初始化语言管理器，确保它优先于其他组件
        try {
            // 直接初始化语言管理器，不使用硬编码消息
            languageManager = new LanguageManager(this);
            // 语言管理器初始化完成后，使用一致的前缀格式
            getLogger().info(languageManager.getMessage("plugin.language_done"));
        } catch (Exception e) {
            // 使用Bukkit.getLogger()直接输出，避免依赖languageManager
            Bukkit.getLogger().severe("§dNekoX §e>> Language manager initialization failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        try {
            // 第一步：初始化玩家配置管理器（依赖：LanguageManager）
            try {
                getLogger().info(languageManager.getMessage("plugin.init_players_config"));
                playerConfigManager = new PlayerConfigManagerSafe(this);
                getLogger().info(languageManager.getMessage("plugin.players_config_done"));
            } catch (Exception e) {
                getLogger().severe(languageManager.getMessage("plugin.startup_error") + "玩家配置管理器初始化失败: " + e.getMessage());
                return false;
            }
            
            // 第二步：初始化技能管理器（依赖：无）
            try {
                getLogger().info(languageManager.getMessage("plugin.init_skills"));
                skillManager = new SkillManager(this);
                getLogger().info(languageManager.getMessage("plugin.skills_done"));
            } catch (Exception e) {
                getLogger().severe(languageManager.getMessage("plugin.startup_error") + "技能管理器初始化失败: " + e.getMessage());
                return false;
            }
            
            // 第三步：初始化猫娘管理器（依赖：PlayerConfigManager）
            try {
                getLogger().info(languageManager.getMessage("plugin.init_neko"));
                nekoManager = new NekoManager(this);
                getLogger().info(languageManager.getMessage("plugin.neko_done"));
            } catch (Exception e) {
                getLogger().severe(languageManager.getMessage("plugin.startup_error") + "猫娘管理器初始化失败: " + e.getMessage());
                return false;
            }
            
            return true;
            
        } catch (Exception e) {
            // 现在可以安全使用languageManager，因为它已经在前面初始化了
            getLogger().severe(languageManager.getMessage("plugin.init_unexpected_error") + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void onDisable() {
        // 注销所有已注册的监听器
        for (Object listener : registeredListeners) {
            if (listener instanceof org.bukkit.event.Listener) {
                HandlerList.unregisterAll((org.bukkit.event.Listener) listener);
            }
        }
        
        // 注销所有已注册的命令
        unregisterCommands();
        
        // 保存配置
        saveConfig();
        
        // 取消夜间效果任务
        if (nightEffectsListener != null) {
            try {
                nightEffectsListener.cancelTask();
            } catch (Exception e) {
                getLogger().warning(languageManager.getMessage("plugin.startup_error") + "取消夜间效果任务时发生异常: " + e.getMessage());
            }
        }
        
        // 关闭数据库连接
        if (playerConfigManager != null) {
            playerConfigManager.close();
        }
        
        // 使用统一的语言管理器获取关闭消息
        getLogger().info(languageManager.getMessage("plugin.disabled"));
    }
    
    private void registerEvents() {
        // 注册所有事件监听器
        registeredListeners.add(new ArmorEvent(this));
        registeredListeners.add(new CatNip(this));
        registeredListeners.add(new Claws(this));
        registeredListeners.add(new MeatOnly(this));
        registeredListeners.add(new NekoChat(this));
        // 只保留一个爬墙监听器以避免冲突
        // registeredListeners.add(new NekoClimbingListener(this));
        registeredListeners.add(new ClimbListener(this));
        registeredListeners.add(new NekoDamageListener(this));
        registeredListeners.add(new NekoMobBehaviorListener(this));
        registeredListeners.add(new NightEffectsListener(this));
        registeredListeners.add(new OwnerDeathListener(this));
        registeredListeners.add(new PassiveAttackBoost(this));
        registeredListeners.add(new PlayerProximityListener(this));
        registeredListeners.add(new StressEffectListener(this));
        registeredListeners.add(new TailPullListener(this));
        
        // 获取nightEffectsListener引用以便在disable时取消任务
        for (Object listener : registeredListeners) {
            if (listener instanceof NightEffectsListener) {
                nightEffectsListener = (NightEffectsListener) listener;
                break;
            }
        }
        
        for (Object listener : registeredListeners) {
            Bukkit.getPluginManager().registerEvents((Listener) listener, this);
        }
    }
    
    private void registerCommands() {
        Pat pat = new Pat(this);
        Lovebite lovebite = new Lovebite(this);
        EarScratch earScratch = new EarScratch(this);
        Purr purr = new Purr(this);
        Hiss hiss = new Hiss(this);
        Scratch scratch = new Scratch(this);
        Attention attention = new Attention(this);
        Nightvision nightvision = new Nightvision(this);
        Jumpboost jumpboost = new Jumpboost(this);
        SwiftSneak swiftSneak = new SwiftSneak(this);
        NekoXCommand nekoxCommand = new NekoXCommand(this);
        NekoXHelp nekoXHelp = new NekoXHelp(this);
        NekoSetCommand nekoSetCommand = new NekoSetCommand(this);
        OwnerCommand ownerCommand = new OwnerCommand(this);
        HealthCommand healthCommand = new HealthCommand(this);
        MySkillsCommand mySkillsCommand = new MySkillsCommand(this);
        PlayerNoticeCommand playerNoticeCommand = new PlayerNoticeCommand(this);
        climbCommand = new Climb(this); // 初始化爬墙命令
        PullTheTailCommand pullTheTailCommand = new PullTheTailCommand(this);
        
        getCommand("pat").setExecutor(pat);
        getCommand("lovebite").setExecutor(lovebite);
        getCommand("earscratch").setExecutor(earScratch);
        getCommand("purr").setExecutor(purr);
        getCommand("hiss").setExecutor(hiss);
        getCommand("scratch").setExecutor(scratch);
        getCommand("attention").setExecutor(attention);
        getCommand("nightvision").setExecutor(nightvision);
        getCommand("jumpboost").setExecutor(jumpboost);
        getCommand("swiftsneak").setExecutor(swiftSneak);
        getCommand("nekox").setExecutor(nekoxCommand);
        getCommand("nekox").setTabCompleter(nekoxCommand);
        getCommand("nekoxhelp").setExecutor(nekoXHelp);
        getCommand("nekoset").setExecutor(nekoSetCommand);
        getCommand("owner").setExecutor(ownerCommand);
        getCommand("health").setExecutor(healthCommand);
        getCommand("myskills").setExecutor(mySkillsCommand);
        getCommand("playernotice").setExecutor(playerNoticeCommand);
        getCommand("climb").setExecutor(climbCommand);
        getCommand("pullthetail").setExecutor(pullTheTailCommand);
        
        // 保存命令引用
        registeredCommands.add(pat);
        registeredCommands.add(lovebite);
        registeredCommands.add(earScratch);
        registeredCommands.add(purr);
        registeredCommands.add(hiss);
        registeredCommands.add(scratch);
        registeredCommands.add(attention);
        registeredCommands.add(nightvision);
        registeredCommands.add(jumpboost);
        registeredCommands.add(swiftSneak);
        registeredCommands.add(nekoxCommand);
        registeredCommands.add(nekoXHelp);
        registeredCommands.add(nekoSetCommand);
        registeredCommands.add(ownerCommand);
        registeredCommands.add(healthCommand);
        registeredCommands.add(mySkillsCommand);
        registeredCommands.add(playerNoticeCommand);
        registeredCommands.add(pullTheTailCommand);
        registeredCommands.add(climbCommand); // 添加爬墙命令到注册列表
    }
    
    private void unregisterCommands() {
        // 注销所有已注册的命令
        getCommand("pat").setExecutor(null);
        getCommand("lovebite").setExecutor(null);
        getCommand("earscratch").setExecutor(null);
        getCommand("purr").setExecutor(null);
        getCommand("hiss").setExecutor(null);
        getCommand("scratch").setExecutor(null);
        getCommand("attention").setExecutor(null);
        getCommand("nightvision").setExecutor(null);
        getCommand("jumpboost").setExecutor(null);
        getCommand("swiftsneak").setExecutor(null);
        getCommand("nekox").setExecutor(null);
        getCommand("nekox").setTabCompleter(null);
        getCommand("nekoxhelp").setExecutor(null);
        getCommand("nekoset").setExecutor(null);
        getCommand("owner").setExecutor(null);
        getCommand("health").setExecutor(null);
        getCommand("myskills").setExecutor(null);
        getCommand("playernotice").setExecutor(null);
        getCommand("climb").setExecutor(null); // 注销爬墙命令
        getCommand("pullthetail").setExecutor(null); // 注销薅尾巴命令
    }
    
    private void initStats() {
        // 初始化bStats统计
        int pluginId = 27133; // NekoX插件的正确ID
        new org.bstats.bukkit.Metrics(this, pluginId);
    }
    
    /**
     * 注册PlaceholderAPI扩展
     */
    private void registerPlaceholderAPI() {
        // 检查PlaceholderAPI是否已安装
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            // 输出正在注册占位符的日志
            getLogger().info(languageManager.getMessage("plugin.placeholder_registering"));
            try {
                // 创建并注册占位符扩展
                NekoXPlaceholderExpansion expansion = new NekoXPlaceholderExpansion(this);
                boolean registered = expansion.register();
                if (registered) {
                    getLogger().info(languageManager.getMessage("plugin.placeholder_enabled"));
                    getLogger().info(languageManager.getMessage("plugin.placeholder_registered"));
                } else {
                    getLogger().warning(languageManager.getMessage("plugin.placeholder_failed"));
                }
            } catch (Exception e) {
                getLogger().warning(languageManager.getMessage("plugin.placeholder_error") + e.getMessage());
            }
        } else {
            getLogger().info(languageManager.getMessage("plugin.placeholder_not_installed"));
        }
    }
    
    public static NekoX getInstance() {
        return instance;
    }
    
    public NekoManager getNekoManager() {
        return nekoManager;
    }
    
    public SkillManager getSkillManager() {
        return skillManager;
    }
    
    public LanguageManager getLanguageManager() {
        return languageManager;
    }
    
    public PlayerConfigManagerSafe getPlayerConfigManager() {
        return playerConfigManager;
    }
    
    public Climb getClimbCommand() {
        return climbCommand;
    }
    
}