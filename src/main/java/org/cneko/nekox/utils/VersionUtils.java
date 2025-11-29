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
        // 尝试使用JUMP常量（通用方式）
        try {
            // 使用反射检查是否存在JUMP_BOOST常量
            try {
                return (PotionEffectType) PotionEffectType.class.getDeclaredField("JUMP_BOOST").get(null);
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
                // 如果没有JUMP_BOOST，则使用JUMP
                return PotionEffectType.JUMP;
            }
        } catch (Exception e) {
            // 发生其他异常时，使用JUMP常量作为最后的手段
            try {
                return PotionEffectType.JUMP;
            } catch (Exception ex) {
                // 所有方法都失败时返回null
                return null;
            }
        }
    }
    
    /**
     * 获取夜视效果类型，兼容不同版本。
     * 
     * @return 夜视效果类型
     */
    public static PotionEffectType getNightVisionEffect() {
        // 直接返回NIGHT_VISION枚举常量
        return PotionEffectType.NIGHT_VISION;
    }
    
    /**
     * 获取速度效果类型，兼容不同版本。
     * 
     * @return 速度效果类型
     */
    public static PotionEffectType getSpeedEffect() {
        // 直接返回SPEED枚举常量
        return PotionEffectType.SPEED;
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