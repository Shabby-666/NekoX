package org.cneko.nekox.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.LanguageManager;

/**
 * 尾巴拉扯监听器
 * 实现：玩家蹲下右键点击猫娘玩家时，发送提示消息，造成1点伤害后立即回复1点生命，并播放猫叫声
 */

public class TailPullListener implements Listener {
    private final NekoX plugin;
    private final LanguageManager lang;
    private static final String SOUND_CAT_MEW = "entity.cat.meow";
    private static final double SOUND_RADIUS = 15.0;
    private static final double DAMAGE_AMOUNT = 1.0;
    private static final double HEAL_AMOUNT = 1.0;
    
    // 添加基于时间的去重机制，记录上次处理事件的时间和玩家
    private long lastProcessedTime = 0;
    private String lastClicker = "";
    private String lastTarget = "";
    private static final long COOLDOWN_MS = 100; // 100毫秒冷却时间，防止重复触发
    
    public TailPullListener(NekoX plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        // 防止事件重复触发
        if (event.isCancelled()) {
            return;
        }
        
        FileConfiguration config = plugin.getConfig();
        
        // 检查功能是否启用
        if (!config.getBoolean("tail-pull.enabled", true)) {
            return;
        }
        
        // 检查被点击的是否为玩家
        if (!(event.getRightClicked() instanceof Player)) {
            return;
        }
        
        Player clicker = event.getPlayer();
        Player target = (Player) event.getRightClicked();
        
        // 检查点击者是否蹲下
        if (!clicker.isSneaking()) {
            return;
        }
        
        // 检查目标是否为猫娘
        if (!plugin.getNekoManager().isNeko(target)) {
            return;
        }
        
        // 检查被点击玩家的尾巴拉扯功能是否开启
        if (!plugin.getNekoManager().isTailPullEnabled(target)) {
            return;
        }
        
        // 基于时间和玩家的去重机制
        long currentTime = System.currentTimeMillis();
        String clickerName = clicker.getName();
        String targetName = target.getName();
        
        // 检查是否在冷却时间内，且是同一个点击者和目标
        if (currentTime - lastProcessedTime < COOLDOWN_MS && 
            lastClicker.equals(clickerName) && 
            lastTarget.equals(targetName)) {
            return; // 在冷却时间内，直接返回
        }
        
        // 更新上次处理时间和玩家信息
        lastProcessedTime = currentTime;
        lastClicker = clickerName;
        lastTarget = targetName;
        
        // 取消事件，防止重复处理
        event.setCancelled(true);
        
        // 发送提示消息给点击者
        String clickerMessage = lang.getMessage("tailpull.pull_message");
        clickerMessage = clickerMessage.replace("{target}", target.getName());
        clicker.sendMessage(clickerMessage);
        
        // 发送提示消息给被点击的猫娘玩家，带有颜色代码
        String targetMessage = lang.getMessage("tailpull.pull_message_target");
        targetMessage = "§c" + targetMessage.replace("{player}", clicker.getName());
        target.sendMessage(targetMessage);
        
        // 对目标造成1点伤害
        target.damage(DAMAGE_AMOUNT);
        
        // 立即回复1点生命
        target.setHealth(Math.min(target.getHealth() + HEAL_AMOUNT, target.getMaxHealth()));
        
        // 向半径15格内的所有玩家播放猫叫声
        target.getWorld().playSound(target.getLocation(), SOUND_CAT_MEW, 1.0f, 1.0f);
    }
}
