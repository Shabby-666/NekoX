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
import org.cneko.nekox.events.NekoClimbingListener;
import org.cneko.nekox.events.ClimbListener;
import org.cneko.nekox.events.PlayerProximityListener;
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
            getLogger().info("配置文件加载完成");
            
            // 按顺序初始化核心组件，确保每个组件都成功初始化
            if (!initializeComponents()) {
                getLogger().severe("插件核心组件初始化失败，插件将禁用");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            
            // 初始化API
            try {
                NekoXAPI.initialize(this);
                getLogger().info("API初始化完成");
            } catch (Exception e) {
                getLogger().warning("API初始化失败，但插件将继续运行: " + e.getMessage());
            }
            
            // 注册事件监听器
            try {
                registerEvents();
                getLogger().info("事件监听器注册完成");
            } catch (Exception e) {
                getLogger().warning("事件监听器注册失败: " + e.getMessage());
            }
            
            // 注册命令
            try {
                registerCommands();
                getLogger().info("命令注册完成");
            } catch (Exception e) {
                getLogger().warning("命令注册失败: " + e.getMessage());
            }
            
            // 初始化统计
            try {
                initStats();
                getLogger().info("统计初始化完成");
            } catch (Exception e) {
                getLogger().warning("统计初始化失败: " + e.getMessage());
            }
            
            // 注册PlaceholderAPI扩展
            try {
                registerPlaceholderAPI();
            } catch (Exception e) {
                getLogger().warning("PlaceholderAPI注册失败: " + e.getMessage());
            }
            
            // 显示欢迎信息
            getLogger().info("正在启用NekoX......");
            getLogger().info("");
            getLogger().info("███╗   ██╗ ███████╗ ██╗  ██╗  ██████╗  ██╗  ██╗");
            getLogger().info("████╗  ██║ ██╔════╝ ██║ ██╔╝ ██╔═══██╗ ╚██╗██╔╝");
            getLogger().info("██╔██╗ ██║ █████╗   █████╔╝  ██║   ██║  ╚███╔╝ ");
            getLogger().info("██║╚██╗██║ ██╔══╝   ██╔═██╗  ██║   ██║  ██╔██╗ ");
            getLogger().info("██║ ╚████║ ███████╗ ██║  ██╗ ╚██████╔╝ ██╔╝ ██╗");
            getLogger().info("╚═╝  ╚═══╝ ╚══════╝ ╚═╝  ╚═╝  ╚═════╝  ╚═╝  ╚═╝");
            getLogger().info("NekoX插件已成功启用！喵~♪");
            getLogger().info("喜欢的话请给个Star吧qwq");
            getLogger().info("Github: https://github.com/Shabby-666/NekoX");
            
        } catch (Exception e) {
            getLogger().severe("插件启动过程中发生严重错误: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    
    /**
     * 按安全顺序初始化所有核心组件
     */
    private boolean initializeComponents() {
        try {
            // 第一步：初始化玩家配置管理器（依赖：无）
            getLogger().info("正在初始化玩家配置管理器...");
            playerConfigManager = new PlayerConfigManagerSafe(this);
            if (playerConfigManager == null) {
                getLogger().severe("玩家配置管理器初始化失败");
                return false;
            }
            getLogger().info("玩家配置管理器初始化完成");
            
            // 第二步：初始化语言管理器（依赖：无）
            getLogger().info("正在初始化语言管理器...");
            languageManager = new LanguageManager(this);
            if (languageManager == null) {
                getLogger().severe("语言管理器初始化失败");
                return false;
            }
            getLogger().info("语言管理器初始化完成");
            
            // 第三步：初始化技能管理器（依赖：无）
            getLogger().info("正在初始化技能管理器...");
            skillManager = new SkillManager(this);
            if (skillManager == null) {
                getLogger().severe("技能管理器初始化失败");
                return false;
            }
            getLogger().info("技能管理器初始化完成");
            
            // 第四步：初始化猫娘管理器（依赖：PlayerConfigManager）
            getLogger().info("正在初始化猫娘管理器...");
            nekoManager = new NekoManager(this);
            if (nekoManager == null) {
                getLogger().severe("猫娘管理器初始化失败");
                return false;
            }
            
            // 猫娘管理器初始化完成
            getLogger().info("猫娘管理器初始化完成");
            
            return true;
            
        } catch (Exception e) {
            getLogger().severe("组件初始化过程中发生错误: " + e.getMessage());
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
                getLogger().warning("取消夜间效果任务时发生异常: " + e.getMessage());
            }
        }
        
        // 关闭数据库连接
        if (playerConfigManager != null) {
            playerConfigManager.close();
        }
        
        getLogger().info("NekoX插件已成功禁用！再见~♪");
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
            getLogger().info("正在注册占位符......");
            try {
                // 创建并注册占位符扩展
                NekoXPlaceholderExpansion expansion = new NekoXPlaceholderExpansion(this);
                boolean registered = expansion.register();
                if (registered) {
                    getLogger().info("PlaceholderAPI支持已启用！");
                    getLogger().info("占位符注册成功！使用/nekox placeholders查看占位符列表");
                } else {
                    getLogger().warning("PlaceholderAPI扩展注册失败！");
                }
            } catch (Exception e) {
                getLogger().warning("注册PlaceholderAPI扩展时发生异常: " + e.getMessage());
            }
        } else {
            getLogger().info("PlaceholderAPI未安装，占位符功能将不可用。");
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