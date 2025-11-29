package org.cneko.nekox.commands;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.NekoManager;
import org.cneko.nekox.utils.SafeMessageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Climb implements CommandExecutor, TabCompleter {
    private final NekoManager nekoManager;
    private final LanguageManager languageManager;
    
    // 存储玩家的爬墙状态
    private final HashMap<String, Boolean> climbStatus = new HashMap<>();
    
    public Climb(NekoX plugin) {
        this.nekoManager = plugin.getNekoManager();
        this.languageManager = plugin.getLanguageManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.only_player", "Only players can use this command!"));
            return true;
        }
        
        Player player = (Player) sender;
        
        // 检查玩家是否是猫娘
        if (!nekoManager.isNeko(player)) {
            player.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.health.notneko", "You are not a neko!"));
            return true;
        }
        
        // 切换爬墙状态
        String playerName = player.getName();
        boolean currentStatus = climbStatus.getOrDefault(playerName, true);
        boolean newStatus = !currentStatus;
        
        climbStatus.put(playerName, newStatus);
        
        if (newStatus) {
            player.sendMessage("§a" + SafeMessageUtils.getSafeMessage(languageManager, "commands.climb.enabled", "爬墙功能已开启！"));
        } else {
            player.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.climb.disabled", "爬墙功能已关闭！"));
            // 移除漂浮效果
            player.removePotionEffect(PotionEffectType.LEVITATION);
        }
        
        return true;
    }
    
    /**
     * 获取玩家面向的方向
     */
    public BlockFace getFacingDirection(float yaw) {
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
     * 检查玩家前方是否有墙
     * 简化版：只检查前方是否有实心方块
     */
    public boolean isWallInFront(Player player) {
        Location location = player.getLocation();
        Block playerBlock = location.getBlock();
        
        // 获取玩家面向的方向
        BlockFace facing = getFacingDirection(location.getYaw());
        
        // 检查前方的方块
        Block frontBlock = playerBlock.getRelative(facing);
        
        // 检查前方方块是否为实心方块（简化逻辑）
        return frontBlock.getType().isSolid();
    }
    

    
    /**
     * 应用爬墙效果（漂浮效果）
     */
    public void applyClimbingEffect(Player player) {
        // 移除旧的效果
        player.removePotionEffect(PotionEffectType.LEVITATION);
        
        // 添加漂浮效果（LEVITATION V，持续40tick）
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 40, 4, false, false));
    }
    
    /**
     * 移除爬墙效果
     */
    public void removeClimbingEffect(Player player) {
        player.removePotionEffect(PotionEffectType.LEVITATION);
    }
    
    /**
     * 获取玩家的爬墙状态
     */
    public boolean getClimbStatus(Player player) {
        return climbStatus.getOrDefault(player.getName(), true);
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return new ArrayList<>(); // 不需要补全参数
    }
}