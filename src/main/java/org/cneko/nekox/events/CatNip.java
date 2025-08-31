package org.cneko.nekox.events;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.VersionUtils;

public class CatNip implements Listener {
    private final NekoX plugin;
    
    public CatNip() {
        this.plugin = NekoX.getInstance();
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // 忽略副手持物的交互
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        
        FileConfiguration config = plugin.getConfig();
        
        if (!config.getBoolean("cat-nip.enabled", true)) {
            return;
        }
        
        Player player = event.getPlayer();
        
        // 检查玩家是否手持猫薄荷物品
        if (player.getInventory().getItemInMainHand().getType() != Material.matchMaterial(config.getString("cat-nip.item", "WHEAT_SEEDS"))) {
            return;
        }
        
        // 应用猫薄荷效果
        int duration = config.getInt("cat-nip.duration", 60) * 20; // 转换为刻
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 1, false, false));
        player.addPotionEffect(new PotionEffect(VersionUtils.getJumpBoostEffect(), duration, 1, false, false));
        player.sendMessage("§e你感到异常兴奋！喵~♪");
        
        // 消耗一个猫薄荷
        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
    }
}