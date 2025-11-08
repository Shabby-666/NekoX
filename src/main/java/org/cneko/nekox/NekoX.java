package org.cneko.nekox;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
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
import org.cneko.nekox.commands.Purr;
import org.cneko.nekox.commands.Scratch;
import org.cneko.nekox.commands.SwiftSneak;
import org.cneko.nekox.events.ArmorEvent;
import org.cneko.nekox.events.NekoChat;
import org.cneko.nekox.events.MeatOnly;
import org.cneko.nekox.events.CatNip;
import org.cneko.nekox.events.Claws;
import org.cneko.nekox.events.MobTargetEvent;
import org.cneko.nekox.events.OwnerDeathListener;
import org.cneko.nekox.events.NightEffectsListener;
import org.cneko.nekox.events.StressEffectListener;
import org.cneko.nekox.events.PassiveAttackBoost;
import org.cneko.nekox.events.NekoDamageListener;
import org.cneko.nekox.events.NekoMobBehaviorListener;
import org.cneko.nekox.events.NekoClimbingListener;
import org.cneko.nekox.utils.NekoManager;
import org.cneko.nekox.utils.SkillManager;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.NekoXPlaceholderExpansion;

public class NekoX extends JavaPlugin {
    private static NekoX instance;
    private NekoManager nekoManager;
    private SkillManager skillManager;
    private NightEffectsListener nightEffectsListener;
    private LanguageManager languageManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // 加载配置
        saveDefaultConfig();
        
        // 初始化猫娘管理器
        nekoManager = new NekoManager(this);
        
        // 初始化技能管理器
        skillManager = new SkillManager(this);
        
        // 初始化语言管理器
        languageManager = new LanguageManager(this);
        
        // 注册事件监听器
        registerEvents();
        
        // 注册命令
        registerCommands();
        
        // 初始化统计
        initStats();
        
        // 注册PlaceholderAPI扩展
        registerPlaceholderAPI();
        
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
        getLogger().info("NekoX插件已成功启用！喵~♪");
        getLogger().info("喜欢的话请给个Star吧qwq");
        getLogger().info("Github: https://github.com/Shabby-666/NekoX");
    }
    
    @Override
    public void onDisable() {
        // 保存配置
        saveConfig();
        
        // 取消夜间效果任务
        if (nightEffectsListener != null) {
            nightEffectsListener.cancelTask();
        }
        
        getLogger().info("NekoX插件已成功禁用！再见~♪");
    }
    
    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new NekoChat(), this);
        Bukkit.getPluginManager().registerEvents(new MeatOnly(), this);
        Bukkit.getPluginManager().registerEvents(new CatNip(), this);
        Bukkit.getPluginManager().registerEvents(new Claws(), this);
        Bukkit.getPluginManager().registerEvents(new MobTargetEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ArmorEvent(), this);
        Bukkit.getPluginManager().registerEvents(new OwnerDeathListener(), this);
        nightEffectsListener = new NightEffectsListener();
        Bukkit.getPluginManager().registerEvents(nightEffectsListener, this);
        Bukkit.getPluginManager().registerEvents(new StressEffectListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PassiveAttackBoost(this), this);
        Bukkit.getPluginManager().registerEvents(new NekoDamageListener(this), this);
        Bukkit.getPluginManager().registerEvents(new NekoMobBehaviorListener(this), this);
        Bukkit.getPluginManager().registerEvents(new NekoClimbingListener(this), this);
    }
    
    private void registerCommands() {
        getCommand("pat").setExecutor(new Pat(this));
        getCommand("lovebite").setExecutor(new Lovebite(this));
        getCommand("earscratch").setExecutor(new EarScratch(this));
        getCommand("purr").setExecutor(new Purr(this));
        getCommand("hiss").setExecutor(new Hiss(this));
        getCommand("scratch").setExecutor(new Scratch(this));
        getCommand("attention").setExecutor(new Attention(this));
        getCommand("nightvision").setExecutor(new Nightvision(this));
        getCommand("jumpboost").setExecutor(new Jumpboost(this));
        getCommand("swiftsneak").setExecutor(new SwiftSneak(this));
        NekoXCommand nekoxCommand = new NekoXCommand(this);
        getCommand("nekox").setExecutor(nekoxCommand);
        getCommand("nekox").setTabCompleter(nekoxCommand);
        getCommand("nekoxhelp").setExecutor(new NekoXHelp(this));
        getCommand("nekoset").setExecutor(new NekoSetCommand(this));
        getCommand("owner").setExecutor(new OwnerCommand(this));
        getCommand("health").setExecutor(new HealthCommand(this));
        getCommand("myskills").setExecutor(new MySkillsCommand(this));
    }
    
    private void initStats() {
        // 初始化bStats统计
        int pluginId = 27197; // NekoX插件的实际ID
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
            // 创建并注册占位符扩展
            NekoXPlaceholderExpansion expansion = new NekoXPlaceholderExpansion(this);
            boolean registered = expansion.register();
            if (registered) {
                getLogger().info("PlaceholderAPI支持已启用！");
                getLogger().info("占位符注册成功！使用/nekox placeholders查看占位符列表");
            } else {
                getLogger().warning("PlaceholderAPI扩展注册失败！");
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
}