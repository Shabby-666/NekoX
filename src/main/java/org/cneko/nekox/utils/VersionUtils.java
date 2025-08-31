package org.cneko.nekox.utils;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.potion.PotionEffectType;

public class VersionUtils {
    private static boolean isVersion1_21_4OrHigher = false;
    
    static {
        // 检测服务器版本
        Server server = Bukkit.getServer();
        String version = server.getVersion();
        
        // 简单的版本检测逻辑
        try {
            if (version.contains("1.21.4") || version.contains("1.21.5")) {
                isVersion1_21_4OrHigher = true;
            } else if (version.contains("1.21")) {
                isVersion1_21_4OrHigher = true;
            }
        } catch (Exception e) {
            // 版本检测失败时默认使用旧版本
            isVersion1_21_4OrHigher = false;
        }
    }
    
    /**
     * 获取跳跃提升效果类型，兼容不同版本
     */
    public static PotionEffectType getJumpBoostEffect() {
        if (isVersion1_21_4OrHigher) {
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
     * 检查是否为1.21.4及以上版本
     */
    public static boolean isVersion1_21_4OrHigher() {
        return isVersion1_21_4OrHigher;
    }
}