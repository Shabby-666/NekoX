package org.cneko.nekox.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.cneko.nekox.NekoX;

/**
 * 被动攻击增强监听器
 * 为猫娘提供小幅攻击伤害提升和更大的击退力度
 */
public class PassiveAttackBoost implements Listener {
    private final NekoX plugin;
    
    public PassiveAttackBoost(NekoX plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // 检查伤害者是否为玩家
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getDamager();
        FileConfiguration config = plugin.getConfig();
        
        // 检查是否启用了这个功能
        if (!config.getBoolean("passive-attack-boost.enabled", true)) {
            return;
        }
        
        // 检查玩家是否是猫娘
        if (!plugin.getNekoManager().isNeko(player)) {
            return;
        }
        
        // 增加伤害值（小幅提升）
        double damageBonus = config.getDouble("passive-attack-boost.damage-bonus", 0.5);
        event.setDamage(event.getDamage() + damageBonus);
        
        // 增加击退力度
        double knockbackMultiplier = config.getDouble("passive-attack-boost.knockback-multiplier", 1.2);
        // 检查是否为1.20.4或更早版本的兼容处理
        try {
            // 尝试使用1.21+版本的方法
            Class<?> eventClass = event.getClass();
            java.lang.reflect.Method getKnockbackMethod = eventClass.getMethod("getKnockbackStrength");
            java.lang.reflect.Method setKnockbackMethod = eventClass.getMethod("setKnockbackStrength", float.class);
            float originalKnockback = ((Number) getKnockbackMethod.invoke(event)).floatValue();
            setKnockbackMethod.invoke(event, (float)(originalKnockback * knockbackMultiplier));
        } catch (Exception e) {
            // 如果反射失败（旧版本），不做额外处理
        }
    }
}