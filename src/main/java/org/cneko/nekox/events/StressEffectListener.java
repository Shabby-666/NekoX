package org.cneko.nekox.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.SkillManager;

public class StressEffectListener implements Listener {
    private final NekoX plugin;
    
    public StressEffectListener(NekoX plugin) {
        this.plugin = plugin;
    }
    
    /**
     * 监听玩家受到伤害时的事件
     */
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getEntity();
        
        // 检查是否是猫娘
        if (!plugin.getNekoManager().isNeko(player)) {
            return;
        }
        
        // 检查应激效果是否已经激活
        SkillManager skillManager = plugin.getSkillManager();
        if (skillManager.isStressEffectActive(player)) {
            return;
        }
        
        // 检查是否在冷却中
        if (skillManager.isSkillOnCooldown(player, SkillManager.SkillType.STRESS_PASSIVE)) {
            return;
        }
        
        // 计算受到伤害后的生命值
        double newHealth = player.getHealth() - event.getFinalDamage();
        
        // 如果生命值低于6，则触发应激效果
        if (newHealth < 6) {
            applyStressEffect(player);
        }
    }
    
    /**
     * 监听玩家恢复生命值时的事件
     */
    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getEntity();
        
        // 检查是否是猫娘
        if (!plugin.getNekoManager().isNeko(player)) {
            return;
        }
        
        // 如果生命值恢复到6以上，则移除应激效果
        if (player.getHealth() + event.getAmount() >= 6 && 
            plugin.getSkillManager().isStressEffectActive(player)) {
            removeStressEffect(player);
        }
    }
    
    /**
     * 应用应激效果
     */
    private void applyStressEffect(Player player) {
        SkillManager skillManager = plugin.getSkillManager();
        
        // 设置应激效果为激活状态
        skillManager.setStressEffectActive(player, true);
        
        // 应用力量50效果，持续1分钟，无粒子特效，并强制覆盖现有效果
        player.addPotionEffect(new PotionEffect(
            PotionEffectType.INCREASE_DAMAGE, // 力量效果
            1200, // 持续时间（1分钟 = 20 * 60 = 1200 ticks）
            49, // 等级50（因为Bukkit中等级从0开始计数，所以50级是49）
            false, // 隐藏粒子效果
            false // 隐藏图标效果
        ), true); // 强制覆盖现有效果
        
        // 设置冷却时间
        skillManager.setCooldown(player, SkillManager.SkillType.STRESS_PASSIVE);
        
        // 发送聊天消息提醒
        player.sendMessage("§c生命值低于6，已触发应激被动！喵~");
        
        // 发送标题提醒
        player.sendTitle(
            "§c应激状态", // 主标题
            "§e获得力量50效果！喵~", // 副标题
            10, // 淡入时间（ticks）
            40, // 停留时间（ticks）
            10  // 淡出时间（ticks）
        );
    }
    
    /**
     * 移除应激效果
     */
    private void removeStressEffect(Player player) {
        // 移除力量效果
        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        
        // 设置应激效果为非激活状态
        plugin.getSkillManager().setStressEffectActive(player, false);
    }
}