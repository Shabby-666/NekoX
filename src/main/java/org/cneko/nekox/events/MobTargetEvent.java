package org.cneko.nekox.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.cneko.nekox.NekoX;

public class MobTargetEvent implements Listener {
    private final NekoX plugin;
    
    public MobTargetEvent() {
        this.plugin = NekoX.getInstance();
    }
    
    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        FileConfiguration config = plugin.getConfig();
        
        if (!config.getBoolean("mob-targeting.enabled", true)) {
            return;
        }
        
        // 检查目标是否为玩家
        if (!(event.getTarget() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getTarget();
        
        // 检查实体是否为LivingEntity
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        
        LivingEntity entity = (LivingEntity) event.getEntity();
        
        // 检查实体是否为敌对生物
        if (isHostileEntity(entity)) {
            // 增加敌对生物的目标距离
            double distanceIncrease = config.getDouble("mob-targeting.distance-increase", 2.0);
            double newDistance = entity.getLocation().distance(player.getLocation()) * distanceIncrease;
            
            // 如果玩家离得更远，取消目标锁定
            if (newDistance > entity.getEyeHeight() * 16) {
                event.setCancelled(true);
            }
        }
        
        // 友好生物更亲近玩家
        if (config.getBoolean("mob-targeting.friendly-attraction", true) && isFriendlyEntity(entity)) {
            // 可以在这里添加友好生物主动靠近玩家的逻辑
            // 例如，降低友好生物对玩家的警惕性
        }
    }
    
    private boolean isHostileEntity(LivingEntity entity) {
        return entity instanceof Creeper || 
               entity instanceof Skeleton || 
               entity instanceof Zombie || 
               entity instanceof Spider || 
               entity instanceof Enderman || 
               entity instanceof Blaze || 
               entity instanceof WitherSkeleton || 
               entity instanceof Stray || 
               entity instanceof Husk || 
               entity instanceof Drowned || 
               entity instanceof Piglin || 
               entity instanceof PiglinBrute;
    }
    
    private boolean isFriendlyEntity(LivingEntity entity) {
        return entity instanceof Villager || 
               entity instanceof IronGolem || 
               entity instanceof Snowman || 
               entity instanceof Cat || 
               entity instanceof Wolf || 
               entity instanceof Horse || 
               entity instanceof Donkey || 
               entity instanceof Mule || 
               entity instanceof Llama;
    }
}