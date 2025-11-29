package org.cneko.nekox.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.commands.Climb;
import org.cneko.nekox.utils.NekoManager;
import java.util.UUID;

public class ClimbListener implements Listener {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    
    public ClimbListener(NekoX plugin) {
        this.plugin = plugin;
        this.nekoManager = plugin.getNekoManager();
    }
    
    // 移除不再需要的时间戳存储
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        try {
            Player player = event.getPlayer();
            UUID playerId = player.getUniqueId();
            
            // 检查是否是猫娘
            if (!nekoManager.isNeko(player)) {
                return;
            }
            
            // 获取爬墙命令实例
            Climb climbCommand = plugin.getClimbCommand();
            if (climbCommand == null) {
                return;
            }
            
            // 检查爬墙功能是否开启
            if (!climbCommand.getClimbStatus(player)) {
                return;
            }
            
            // 检查玩家前方是否有方块
            boolean hasBlockInFront = isWallInFront(player);
            
            // 检查玩家脚下前一格是否有方块
            boolean hasBlockBelowFront = isBlockBelowFront(player);
            
            // 如果脚下前一格没有方块，立即取消漂浮效果
            if (!hasBlockBelowFront) {
                climbCommand.removeClimbingEffect(player);
                return;
            }
            
            // 检查玩家是否在墙上并且正在尝试向上移动
            if (isPlayerOnWall(player) && player.isSprinting()) {
                // 应用爬墙效果（漂浮）
                climbCommand.applyClimbingEffect(player);
            } else if (!isMovingUpwards(event)) {
                // 只有当玩家不向上移动时才移除效果
                climbCommand.removeClimbingEffect(player);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("处理猫娘爬墙事件时发生异常: " + e.getMessage());
        }
    }
    
    /**
     * 检查玩家是否在墙上并且面向墙壁
     */
    private boolean isPlayerOnWall(Player player) {
        Location location = player.getLocation();
        Block playerBlock = location.getBlock();
        
        // 获取玩家面向的方向
        float yaw = location.getYaw();
        BlockFace facing = getFacingDirection(yaw);
        
        // 检查玩家面向的方块是否是墙壁
        Block facingBlock = playerBlock.getRelative(facing);
        if (facingBlock.getType().isSolid()) {
            return true;
        }
        
        // 同时检查玩家接触的方块（前后左右四个方向）
        BlockFace[] directions = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
        
        for (BlockFace direction : directions) {
            Block wallBlock = playerBlock.getRelative(direction);
            // 检查是否是实心方块（可以爬的墙）
            if (wallBlock.getType().isSolid()) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 检查玩家前方是否有墙
     */
    private boolean isWallInFront(Player player) {
        Location location = player.getLocation();
        Block playerBlock = location.getBlock();
        
        // 获取玩家面向的方向
        float yaw = location.getYaw();
        BlockFace facing = getFacingDirection(yaw);
        
        // 检查前方的方块
        Block frontBlock = playerBlock.getRelative(facing);
        
        // 检查前方方块是否为实心方块
        return frontBlock.getType().isSolid();
    }
    
    /**
     * 获取玩家面向的方向
     */
    private BlockFace getFacingDirection(float yaw) {
        // 将yaw转换为0-360范围
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
     * 检查玩家是否在向上移动
     */
    private boolean isMovingUpwards(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        
        // 检查Y坐标是否增加（向上移动）
        return to != null && to.getY() > from.getY();
    }
    
    /**
     * 检查玩家脚下前一格是否有方块（如图片所示的检测位置）
     */
    private boolean isBlockBelowFront(Player player) {
        Location location = player.getLocation();
        
        // 获取玩家面向的方向
        float yaw = location.getYaw();
        BlockFace facing = getFacingDirection(yaw);
        
        // 获取玩家脚下前一格的位置：面向方向的方块，然后向下一格
        Block frontBlock = location.getBlock().getRelative(facing);
        Block blockBelowFront = frontBlock.getRelative(BlockFace.DOWN);
        
        // 检查该方块是否为实心方块
        return blockBelowFront.getType().isSolid();
    }
}