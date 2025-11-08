package org.cneko.nekox.utils;

import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkillManager {
    private final NekoX plugin;
    private final Map<UUID, Map<String, Long>> skillCooldowns = new HashMap<>(); // 存储每个玩家每个技能的冷却时间
    private final Map<UUID, Boolean> stressEffectActive = new HashMap<>(); // 存储应激效果是否激活
    
    public enum SkillType {
        HEALTH_RESTORE, // 主动技能：生命恢复
        STRESS_PASSIVE // 被动技能：应激反应
    }
    
    public SkillManager(NekoX plugin) {
        this.plugin = plugin;
    }
    
    /**
     * 检查技能是否在冷却中
     */
    public boolean isSkillOnCooldown(Player player, SkillType skillType) {
        UUID playerUUID = player.getUniqueId();
        String skillKey = skillType.name();
        
        if (!skillCooldowns.containsKey(playerUUID) || !skillCooldowns.get(playerUUID).containsKey(skillKey)) {
            return false;
        }
        
        long lastUsed = skillCooldowns.get(playerUUID).get(skillKey);
        long currentTime = System.currentTimeMillis();
        long cooldownTime = getCooldownTime(skillType);
        
        return (currentTime - lastUsed) < cooldownTime;
    }
    
    /**
     * 获取技能剩余冷却时间（秒）
     */
    public long getRemainingCooldown(Player player, SkillType skillType) {
        UUID playerUUID = player.getUniqueId();
        String skillKey = skillType.name();
        
        if (!skillCooldowns.containsKey(playerUUID) || !skillCooldowns.get(playerUUID).containsKey(skillKey)) {
            return 0;
        }
        
        long lastUsed = skillCooldowns.get(playerUUID).get(skillKey);
        long currentTime = System.currentTimeMillis();
        long cooldownTime = getCooldownTime(skillType);
        long remaining = cooldownTime - (currentTime - lastUsed);
        
        return Math.max(remaining / 1000, 0); // 转换为秒
    }
    
    /**
     * 设置技能冷却时间
     */
    public void setCooldown(Player player, SkillType skillType) {
        UUID playerUUID = player.getUniqueId();
        String skillKey = skillType.name();
        
        skillCooldowns.putIfAbsent(playerUUID, new HashMap<>());
        skillCooldowns.get(playerUUID).put(skillKey, System.currentTimeMillis());
    }
    
    /**
     * 获取技能的冷却时间（毫秒）
     */
    private long getCooldownTime(SkillType skillType) {
        switch (skillType) {
            case HEALTH_RESTORE:
                return plugin.getConfig().getInt("health-skill.cooldown", 60) * 1000L;
            case STRESS_PASSIVE:
                return 300 * 1000L; // 应激被动冷却5分钟
            default:
                return 0;
        }
    }
    
    /**
     * 检查应激效果是否激活
     */
    public boolean isStressEffectActive(Player player) {
        return stressEffectActive.getOrDefault(player.getUniqueId(), false);
    }
    
    /**
     * 设置应激效果状态
     */
    public void setStressEffectActive(Player player, boolean active) {
        stressEffectActive.put(player.getUniqueId(), active);
    }
    
    /**
     * 清除所有冷却时间（用于测试或特殊情况）
     */
    public void clearAllCooldowns(Player player) {
        skillCooldowns.remove(player.getUniqueId());
    }
}