package org.cneko.nekox.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.cneko.nekox.NekoX;

public class NekoClimbingListener implements Listener {
    private final NekoX plugin;
    
    public NekoClimbingListener(NekoX plugin) {
        this.plugin = plugin;
    }
    
    /**
     * 监听玩家移动事件，实现猫娘爬墙功能
     * 有梯子时速度正常，无梯子时速度减半
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        FileConfiguration config = plugin.getConfig();
        
        // 检查功能是否启用
        if (!config.getBoolean("neko-climbing.enabled", true)) {
            return;
        }
        
        Player player = event.getPlayer();
        
        // 检查是否是猫娘
        if (!plugin.getNekoManager().isNeko(player)) {
            return;
        }
        
        // 检查玩家是否在爬墙（接触墙壁且向上移动）
        if (isClimbingWall(player)) {
            // 检查玩家是否有梯子
            boolean hasLadder = hasLadderNearby(player);
            
            // 根据是否有梯子调整爬墙速度
            if (hasLadder) {
                // 有梯子：正常速度
                applyClimbingEffect(player, 1);
                
                // 发送调试消息
                if (config.getBoolean("neko-climbing.debug", false)) {
                    player.sendMessage("§a有梯子辅助，爬墙速度正常！");
                }
            } else {
                // 无梯子：速度减半
                applyClimbingEffect(player, 0);
                
                // 发送调试消息
                if (config.getBoolean("neko-climbing.debug", false)) {
                    player.sendMessage("§c没有梯子，爬墙速度减半！");
                }
            }
        } else {
            // 不在爬墙时移除效果
            removeClimbingEffect(player);
        }
    }
    
    /**
     * 检查玩家是否在爬墙
     */
    private boolean isClimbingWall(Player player) {
        // 玩家必须在地面上方且向上移动
        if (player.isOnGround() || player.getVelocity().getY() <= 0) {
            return false;
        }
        
        // 检查玩家是否接触墙壁
        Block playerBlock = player.getLocation().getBlock();
        
        // 检查玩家周围的墙壁（前后左右四个方向）
        BlockFace[] directions = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
        
        for (BlockFace direction : directions) {
            Block wallBlock = playerBlock.getRelative(direction);
            
            // 检查是否是实心方块（可以爬的墙）
            if (isClimbableWall(wallBlock.getType())) {
                // 检查玩家是否在跳跃状态且面向墙壁
                if (player.isSprinting() || player.isSneaking()) {
                    // 检查玩家是否面向墙壁
                    float yaw = player.getLocation().getYaw();
                    BlockFace facing = getFacingDirection(yaw);
                    
                    // 如果玩家面向墙壁方向，则判定为爬墙
                    if (facing == direction || facing == direction.getOppositeFace()) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * 根据玩家朝向获取面向方向
     */
    private BlockFace getFacingDirection(float yaw) {
        // 将yaw转换为0-360度
        yaw = (yaw % 360 + 360) % 360;
        
        if (yaw >= 315 || yaw < 45) {
            return BlockFace.SOUTH;
        } else if (yaw >= 45 && yaw < 135) {
            return BlockFace.WEST;
        } else if (yaw >= 135 && yaw < 225) {
            return BlockFace.NORTH;
        } else {
            return BlockFace.EAST;
        }
    }
    
    /**
     * 检查是否是可爬的墙壁
     */
    private boolean isClimbableWall(Material material) {
        // 实心方块都可以爬
        return material.isSolid();
    }
    
    /**
     * 检查玩家附近是否有梯子
     */
    private boolean hasLadderNearby(Player player) {
        Block playerBlock = player.getLocation().getBlock();
        
        // 检查玩家周围3x3x3范围内的梯子
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block block = playerBlock.getRelative(x, y, z);
                    if (block.getType() == Material.LADDER) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * 应用爬墙效果
     * @param player 玩家
     * @param hasLadder 是否有梯子（0=无梯子，1=有梯子）
     */
    private void applyClimbingEffect(Player player, int hasLadder) {
        // 移除旧的效果
        removeClimbingEffect(player);
        
        if (hasLadder == 1) {
            // 有梯子：正常爬墙速度
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 0, false, false));
        } else {
            // 无梯子：爬墙速度减半（给予缓慢效果）
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0, false, false));
        }
    }
    
    /**
     * 移除爬墙效果
     */
    private void removeClimbingEffect(Player player) {
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.SLOW);
    }
}