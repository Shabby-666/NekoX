package org.cneko.nekox.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class NekoXPlaceholderExpansion extends PlaceholderExpansion {

    private final NekoX plugin;
    private final NekoManager nekoManager;

    public NekoXPlaceholderExpansion(NekoX plugin) {
        this.plugin = plugin;
        this.nekoManager = plugin.getNekoManager();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "nekox";
    }

    @Override
    public @NotNull String getAuthor() {
        return "NekoX Team";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // 让扩展在PlaceholderAPI重载时仍然保持注册状态
    }

    @Override
    public boolean canRegister() {
        return true; // 允许注册扩展
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        // 检查玩家是否为猫娘
        if (identifier.equals("is_neko")) {
            return String.valueOf(nekoManager.isNeko(player));
        }

        // 获取人类玩家列表（非猫娘）
        if (identifier.equals("humans")) {
            List<String> humanPlayers = new ArrayList<>();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!nekoManager.isNeko(onlinePlayer)) {
                    humanPlayers.add(onlinePlayer.getName());
                }
            }
            return String.join(", ", humanPlayers);
        }

        // 获取猫娘玩家列表
        if (identifier.equals("nekos")) {
            Set<Player> nekoPlayers = nekoManager.getNekoPlayers();
            List<String> nekoNames = new ArrayList<>();
            for (Player nekoPlayer : nekoPlayers) {
                nekoNames.add(nekoPlayer.getName());
            }
            return String.join(", ", nekoNames);
        }

        return null; // 如果占位符不存在，则返回null
    }
    
    /**
     * 获取所有注册的占位符列表
     * @return 占位符标识符列表
     */
    public static List<String> getAllPlaceholders() {
        return Arrays.asList("is_neko", "humans", "nekos");
    }
}