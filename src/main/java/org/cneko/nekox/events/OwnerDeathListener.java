package org.cneko.nekox.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.NekoManager;

import java.util.Set;

public class OwnerDeathListener implements Listener {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    
    public OwnerDeathListener() {
        this.plugin = NekoX.getInstance();
        this.nekoManager = plugin.getNekoManager();
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onOwnerDeath(PlayerDeathEvent event) {
        FileConfiguration config = plugin.getConfig();
        
        // 检查功能是否启用
        if (!config.getBoolean("owner-death.feature.enabled", true)) {
            return;
        }
        
        Player owner = event.getEntity();
        
        // 获取该主人拥有的所有在线猫娘
        Set<Player> nekos = nekoManager.getNekosByOwner(owner);
        
        if (nekos.isEmpty()) {
            return; // 没有拥有的猫娘，无需处理
        }
        
        // 对每个猫娘执行死亡操作
        for (Player neko : nekos) {
            // 保存猫娘的物品栏和经验（可选）
            // 这里可以根据配置决定是否保存物品栏和经验
            boolean keepInventory = config.getBoolean("owner-death.keep-inventory", false);
            boolean keepLevel = config.getBoolean("owner-death.keep-level", false);
            
            // 可以添加一条消息通知猫娘
            neko.sendMessage("§c你的主人已死亡，你也随之倒下了...");
            
            // 使猫娘死亡
            neko.setHealth(0.0);
        }
    }
}