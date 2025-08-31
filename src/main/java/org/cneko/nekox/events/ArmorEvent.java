package org.cneko.nekox.events;

// 移除未使用的 import 语句
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.cneko.nekox.NekoX;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ArmorEvent implements Listener {
    private final NekoX plugin;
    private final Map<Player, Integer> leatherArmorCount = new HashMap<>();
    private final Set<Player> hasSpeedBonus = new HashSet<>();
    
    public ArmorEvent() {
        this.plugin = NekoX.getInstance();
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // 玩家加入时检查护甲
        checkArmor(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        // 玩家切换手持物品时检查护甲（可能间接影响护甲）
        checkArmor(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // 玩家退出时清理数据
        leatherArmorCount.remove(event.getPlayer());
        hasSpeedBonus.remove(event.getPlayer());
    }
    
    private void checkArmor(Player player) {
        FileConfiguration config = plugin.getConfig();
        
        if (!config.getBoolean("armor-bonus.enabled", true)) {
            return;
        }
        
        // 统计玩家穿着的皮革护甲数量
        int count = 0;
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && config.getStringList("armor-bonus.leather-bonus").contains(armor.getType().name())) {
                count++;
            }
        }
        
        // 获取之前记录的护甲数量
// 由于 previousCount 未被使用，移除该变量声明
        leatherArmorCount.put(player, count);
        
        // 移除旧的速度效果
        if (hasSpeedBonus.contains(player)) {
            player.removePotionEffect(PotionEffectType.SPEED);
            hasSpeedBonus.remove(player);
        }
        
        // 如果玩家穿着皮革护甲，添加速度效果
        if (count > 0) {
            double speedBonus = config.getDouble("armor-bonus.speed-bonus-per-piece", 0.03) * count;
            int amplifier = (int) Math.min(4, Math.ceil(speedBonus * 10)); // 限制最大等级为4
            
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, amplifier, false, false));
            hasSpeedBonus.add(player);
        }
    }
}