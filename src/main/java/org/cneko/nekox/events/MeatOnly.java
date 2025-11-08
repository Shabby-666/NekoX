package org.cneko.nekox.events;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.cneko.nekox.NekoX;

import java.util.List;

public class MeatOnly implements Listener {
    private final NekoX plugin;
    
    public MeatOnly() {
        this.plugin = NekoX.getInstance();
    }
    
    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        FileConfiguration config = plugin.getConfig();
        
        if (!config.getBoolean("meat-only.enabled", true)) {
            return;
        }
        
        Player player = event.getPlayer();
        Material foodType = event.getItem().getType();
        
        // 获取允许食用的食物列表
        List<String> allowedFoods = config.getStringList("meat-only.allowed-foods");
        
        // 检查食物是否在允许列表中
        if (!allowedFoods.contains(foodType.name())) {
            // 取消食用
            event.setCancelled(true);
            player.sendMessage(plugin.getLanguageManager().getMessage("meat_only.restriction"));
        }
    }
}