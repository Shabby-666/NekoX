package org.cneko.nekox.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.cneko.nekox.NekoX;

public class NekoDamageListener implements Listener {
    private final NekoX plugin;
    
    public NekoDamageListener(NekoX plugin) {
        this.plugin = plugin;
    }
    
    /**
     * 监听猫娘受到伤害时的事件
     * 实现：免疫跌落伤害，其他伤害调整为普通玩家状态下的1.8倍
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getEntity();
        
        // 检查是否是猫娘
        if (!plugin.getNekoManager().isNeko(player)) {
            return;
        }
        
        // 检查功能是否启用
        if (!plugin.getConfig().getBoolean("neko-damage-modification.enabled", true)) {
            return;
        }
        
        // 获取伤害原因
        EntityDamageEvent.DamageCause cause = event.getCause();
        
        // 免疫跌落伤害
        if (cause == EntityDamageEvent.DamageCause.FALL && 
            plugin.getConfig().getBoolean("neko-damage-modification.fall-damage-immunity", true)) {
            event.setCancelled(true);
            return;
        }
        
        // 其他伤害增加倍数
        double damageMultiplier = plugin.getConfig().getDouble("neko-damage-modification.other-damage-multiplier", 1.8);
        
        // 使用Minecraft的伤害计算系统，通过修改最终伤害来实现1.8倍效果
        // 注意：这里我们修改的是最终伤害值，确保伤害计算正确
        double originalDamage = event.getDamage();
        double increasedDamage = originalDamage * damageMultiplier;
        event.setDamage(increasedDamage);
        
        // 可选：发送伤害调整消息（调试用）
        if (plugin.getConfig().getBoolean("neko-damage-modification.debug", false)) {
            player.sendMessage(String.format("§e伤害调整：%.1f → %.1f (原因：%s, 倍数：%.1f)", 
                originalDamage, increasedDamage, cause.toString(), damageMultiplier));
        }
    }
}