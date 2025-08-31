package org.cneko.nekox;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.cneko.nekox.commands.*;
import org.cneko.nekox.events.*;
import org.cneko.nekox.utils.NekoManager;

public class NekoX extends JavaPlugin {
    private static NekoX instance;
    private NekoManager nekoManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // 加载配置
        saveDefaultConfig();
        
        // 初始化猫娘管理器
        nekoManager = new NekoManager(this);
        
        // 注册事件监听器
        registerEvents();
        
        // 注册命令
        registerCommands();
        
        // 初始化统计
        initStats();
        
        getLogger().info("NekoX插件已成功启用！喵~♪");
    }
    
    @Override
    public void onDisable() {
        // 保存配置
        saveConfig();
        
        getLogger().info("NekoX插件已成功禁用！再见~♪");
    }
    
    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new NekoChat(), this);
        Bukkit.getPluginManager().registerEvents(new MeatOnly(), this);
        Bukkit.getPluginManager().registerEvents(new CatNip(), this);
        Bukkit.getPluginManager().registerEvents(new Claws(), this);
        Bukkit.getPluginManager().registerEvents(new MobTargetEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ArmorEvent(), this);
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
        getCommand("nekox").setExecutor(new NekoXCommand(this));
        getCommand("nekoxhelp").setExecutor(new NekoXHelp());
        getCommand("nekoset").setExecutor(new NekoSetCommand(this));
    }
    
    private void initStats() {
        // 初始化bStats统计
        int pluginId = 12345; // 替换为实际的插件ID
        new org.bstats.bukkit.Metrics(this, pluginId);
    }
    
    public static NekoX getInstance() {
        return instance;
    }
    
    public NekoManager getNekoManager() {
        return nekoManager;
    }
}