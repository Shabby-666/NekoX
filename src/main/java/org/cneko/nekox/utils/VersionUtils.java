package org.cneko.nekox.utils;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.potion.PotionEffectType;

public class VersionUtils {
    private static boolean isVersion1214OrHigherField = false;
    
    static {
        // 检测服务器版本
        Server server = Bukkit.getServer();
        String version = server.getVersion();
        
        // 简单的版本检测逻辑
        try {
            if (version.contains("1.21.4") || version.contains("1.21.5")) {
                isVersion1214OrHigherField = true;
            } else if (version.contains("1.21")) {
                isVersion1214OrHigherField = true;
            }
        } catch (Exception e) {
            // 版本检测失败时默认使用旧版本
            isVersion1214OrHigherField = false;
        }
    }
    
    /**
     * 获取跳跃提升效果类型，兼容不同版本。
     * 
     * @return 跳跃提升效果类型
     */
    public static PotionEffectType getJumpBoostEffect() {
        if (isVersion1214OrHigher()) {
            // 1.21.4及以上版本使用JUMP_BOOST
            try {
                return PotionEffectType.getByName("JUMP_BOOST");
            } catch (Exception e) {
                // 降级处理
                return PotionEffectType.JUMP;
            }
        } else {
            // 1.21.4以下版本使用JUMP
            return PotionEffectType.JUMP;
        }
    }
    
    /**
     * 获取夜视效果类型，兼容不同版本。
     * 
     * @return 夜视效果类型
     */
    public static PotionEffectType getNightVisionEffect() {
        if (isVersion1214OrHigher()) {
            // 1.21.4及以上版本使用NIGHT_VISION
            try {
                return PotionEffectType.getByName("NIGHT_VISION");
            } catch (Exception e) {
                // 降级处理
                return PotionEffectType.NIGHT_VISION;
            }
        } else {
            // 1.21.4以下版本使用NIGHT_VISION
            return PotionEffectType.NIGHT_VISION;
        }
    }
    
    /**
     * 获取速度效果类型，兼容不同版本。
     * 
     * @return 速度效果类型
     */
    public static PotionEffectType getSpeedEffect() {
        if (isVersion1214OrHigher()) {
            // 1.21.4及以上版本使用SPEED
            try {
                return PotionEffectType.getByName("SPEED");
            } catch (Exception e) {
                // 降级处理
                return PotionEffectType.SPEED;
            }
        } else {
            // 1.21.4以下版本使用SPEED
            return PotionEffectType.SPEED;
        }
    }
    
    /**
     * 检查是否为1.21.4及以上版本。
     * 
     * @return 如果是1.21.4及以上版本则返回true，否则返回false
     */
    public static boolean isVersion1214OrHigher() {
        return isVersion1214OrHigherField;
    }
}