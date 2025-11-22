package org.cneko.nekox.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.LanguageManager;

public class NekoMobBehaviorListener implements Listener {
    private final NekoX plugin;
    
    public NekoMobBehaviorListener(NekoX plugin) {
        this.plugin = plugin;
    }
    
    /**
     * 监听苦力怕爆炸事件
     * 当苦力怕遇到猫娘时，阻止爆炸并驱赶苦力怕
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        FileConfiguration config = plugin.getConfig();
        
        // 检查功能是否启用
        if (!config.getBoolean("neko-mob-behavior.enabled", true)) {
            return;
        }
        
        // 检查实体是否为苦力怕
        if (!(event.getEntity() instanceof Creeper)) {
            return;
        }
        
        Creeper creeper = (Creeper) event.getEntity();
        
        // 检查附近是否有猫娘玩家
        for (Player player : creeper.getWorld().getPlayers()) {
            if (plugin.getNekoManager().isNeko(player)) {
                // 检查距离是否在爆炸范围内
                double distance = creeper.getLocation().distance(player.getLocation());
                if (distance <= 4.0) { // 苦力怕爆炸范围约为3-4格
                    // 阻止苦力怕爆炸
                    event.setCancelled(true);
                    
                    // 驱赶苦力怕：让苦力怕远离猫娘
                    if (config.getBoolean("neko-mob-behavior.creeper-repulsion", true)) {
                        // 计算远离猫娘的方向
                        org.bukkit.util.Vector direction = player.getLocation().toVector()
                                .subtract(creeper.getLocation().toVector())
                                .normalize()
                                .multiply(-1); // 反向
                        
                        // 给苦力怕一个推力，让它远离猫娘
                        creeper.setVelocity(direction.multiply(0.5));
                        
                        // 可选：发送调试消息
                        if (config.getBoolean("neko-mob-behavior.debug", false)) {
                            player.sendMessage(plugin.getLanguageManager().getMessage("mob.repulsion.creeper_repelled"));
                        }
                    }
                    
                    return;
                }
            }
        }
    }
    
    /**
     * 监听生物目标锁定事件
     * 阻止幻翼攻击猫娘并驱赶幻翼
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        FileConfiguration config = plugin.getConfig();
        
        // 检查功能是否启用
        if (!config.getBoolean("neko-mob-behavior.enabled", true)) {
            return;
        }
        
        // 检查目标是否为玩家
        if (!(event.getTarget() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getTarget();
        
        // 检查是否是猫娘
        if (!plugin.getNekoManager().isNeko(player)) {
            return;
        }
        
        // 检查实体是否为幻翼
        if (event.getEntity() instanceof Phantom) {
            Phantom phantom = (Phantom) event.getEntity();
            
            // 阻止幻翼攻击猫娘
            event.setCancelled(true);
            
            // 驱赶幻翼：让幻翼远离猫娘
            if (config.getBoolean("neko-mob-behavior.phantom-repulsion", true)) {
                // 计算远离猫娘的方向
                org.bukkit.util.Vector direction = player.getLocation().toVector()
                        .subtract(phantom.getLocation().toVector())
                        .normalize()
                        .multiply(-1); // 反向
                
                // 给幻翼一个推力，让它远离猫娘
                phantom.setVelocity(direction.multiply(0.8));
                
                // 可选：发送调试消息
                if (config.getBoolean("neko-mob-behavior.debug", false)) {
                    player.sendMessage(plugin.getLanguageManager().getMessage("mob.repulsion.phantom_repelled"));
                }
            }
        }
    }
}