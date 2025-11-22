package org.cneko.nekox;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.HandlerList;
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
import org.cneko.nekox.events.PlayerProximityListener;
import org.cneko.nekox.utils.NekoManager;
import org.cneko.nekox.utils.SkillManager;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.NekoXPlaceholderExpansion;
import org.cneko.nekox.utils.PlayerConfigManager;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class NekoX extends JavaPlugin {
    private static NekoX instance;
    private NekoManager nekoManager;
    private SkillManager skillManager;
    private NightEffectsListener nightEffectsListener;
    private LanguageManager languageManager;
    private PlayerConfigManager playerConfigManager;
    
    // 保存已注册的监听器和命令引用，以便在插件禁用时注销它们
    private final List<Object> registeredListeners = new ArrayList<>();
    private final List<CommandExecutor> registeredCommands = new ArrayList<>();
    
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
        
        // 初始化玩家配置管理器
        playerConfigManager = new PlayerConfigManager(this);
        
        // 初始化API
        NekoXAPI.initialize(this);
        
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
            nightEffectsListener.cancelTask();
        }
        
        // 关闭数据库连接
        if (playerConfigManager != null) {
            playerConfigManager.close();
        }
        
        getLogger().info("NekoX插件已成功禁用！再见~♪");
    }
    
    private void registerEvents() {
        NekoChat nekoChat = new NekoChat();
        MeatOnly meatOnly = new MeatOnly();
        CatNip catNip = new CatNip();
        Claws claws = new Claws();
        MobTargetEvent mobTargetEvent = new MobTargetEvent();
        ArmorEvent armorEvent = new ArmorEvent();
        OwnerDeathListener ownerDeathListener = new OwnerDeathListener();
        PlayerProximityListener playerProximityListener = new PlayerProximityListener(this);
        
        Bukkit.getPluginManager().registerEvents(nekoChat, this);
        Bukkit.getPluginManager().registerEvents(meatOnly, this);
        Bukkit.getPluginManager().registerEvents(catNip, this);
        Bukkit.getPluginManager().registerEvents(claws, this);
        Bukkit.getPluginManager().registerEvents(mobTargetEvent, this);
        Bukkit.getPluginManager().registerEvents(armorEvent, this);
        Bukkit.getPluginManager().registerEvents(ownerDeathListener, this);
        Bukkit.getPluginManager().registerEvents(playerProximityListener, this);
        
        // 保存监听器引用
        registeredListeners.add(nekoChat);
        registeredListeners.add(meatOnly);
        registeredListeners.add(catNip);
        registeredListeners.add(claws);
        registeredListeners.add(mobTargetEvent);
        registeredListeners.add(armorEvent);
        registeredListeners.add(ownerDeathListener);
        registeredListeners.add(playerProximityListener);
        
        nightEffectsListener = new NightEffectsListener();
        Bukkit.getPluginManager().registerEvents(nightEffectsListener, this);
        registeredListeners.add(nightEffectsListener);
        
        StressEffectListener stressEffectListener = new StressEffectListener(this);
        Bukkit.getPluginManager().registerEvents(stressEffectListener, this);
        registeredListeners.add(stressEffectListener);
        
        PassiveAttackBoost passiveAttackBoost = new PassiveAttackBoost(this);
        Bukkit.getPluginManager().registerEvents(passiveAttackBoost, this);
        registeredListeners.add(passiveAttackBoost);
        
        NekoDamageListener nekoDamageListener = new NekoDamageListener(this);
        Bukkit.getPluginManager().registerEvents(nekoDamageListener, this);
        registeredListeners.add(nekoDamageListener);
        
        NekoMobBehaviorListener nekoMobBehaviorListener = new NekoMobBehaviorListener(this);
        Bukkit.getPluginManager().registerEvents(nekoMobBehaviorListener, this);
        registeredListeners.add(nekoMobBehaviorListener);
        
        NekoClimbingListener nekoClimbingListener = new NekoClimbingListener(this);
        Bukkit.getPluginManager().registerEvents(nekoClimbingListener, this);
        registeredListeners.add(nekoClimbingListener);
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
    
    public PlayerConfigManager getPlayerConfigManager() {
        return playerConfigManager;
    }
}