package org.cneko.nekox.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
// 由于 org.bukkit.potion.PotionEffectType 未被使用，移除该导入语句
import org.cneko.nekox.NekoX;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Claws implements Listener {
    private final NekoX plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    
    public Claws(NekoX plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        
        FileConfiguration config = plugin.getConfig();
        
        if (!config.getBoolean("claws.enabled", true)) {
            return;
        }
        
        Player player = (Player) event.getDamager();
        
        // 检查玩家是否是猫娘
        if (!plugin.getNekoManager().isNeko(player)) {
            return;
        }
        
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        
        // 检查冷却时间
        if (cooldowns.containsKey(playerId) && currentTime - cooldowns.get(playerId) < config.getInt("claws.cooldown", 10) * 1000) {
            return;
        }
        
        // 检查目标是否为生物
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        
        // 增加伤害
        double damageBonus = config.getDouble("claws.damage-bonus", 2.0);
        event.setDamage(event.getDamage() + damageBonus);
        
        // 重置冷却时间
        cooldowns.put(playerId, currentTime);
        
        // 发送消息
        player.sendMessage(plugin.getLanguageManager().getMessage("claws.sharpened"));
    }
}