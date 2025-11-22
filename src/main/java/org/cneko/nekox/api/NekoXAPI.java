package org.cneko.nekox.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.NekoManager;

import java.util.Set;
import java.util.UUID;

/**
 * NekoX插件API，供其他插件使用
 */
public class NekoXAPI {
    private static NekoXAPI instance;
    private final NekoX plugin;
    private final NekoManager nekoManager;

    private NekoXAPI(NekoX plugin) {
        this.plugin = plugin;
        this.nekoManager = plugin.getNekoManager();
    }

    /**
     * 获取API实例
     *
     * @param plugin 请求API的插件
     * @return NekoXAPI实例
     */
    public static NekoXAPI getInstance(Plugin plugin) {
        if (instance == null) {
            throw new IllegalStateException("NekoXAPI未初始化，请确保NekoX插件已加载");
        }
        return instance;
    }

    /**
     * 内部初始化方法，仅由NekoX插件调用
     *
     * @param plugin NekoX插件实例
     */
    public static void initialize(NekoX plugin) {
        if (instance == null) {
            instance = new NekoXAPI(plugin);
        }
    }

    /**
     * 检查玩家是否是猫娘
     *
     * @param player 要检查的玩家
     * @return 如果是猫娘返回true，否则返回false
     */
    public boolean isNeko(Player player) {
        return nekoManager.isNeko(player);
    }

    /**
     * 检查玩家是否是猫娘（通过玩家名）
     *
     * @param playerName 玩家名
     * @return 如果是猫娘返回true，否则返回false
     */
    public boolean isNeko(String playerName) {
        return nekoManager.isNeko(playerName);
    }

    /**
     * 获取所有猫娘玩家名
     *
     * @return 所有猫娘玩家名集合
     */
    public Set<String> getAllNekoNames() {
        return nekoManager.getAllNekoNames();
    }

    /**
     * 获取猫娘的所有主人名
     *
     * @param nekoName 猫娘名
     * @return 主人名集合
     */
    public Set<String> getOwnerNames(String nekoName) {
        return nekoManager.getOwnerNames(nekoName);
    }

    /**
     * 检查玩家是否有主人
     *
     * @param playerName 玩家名
     * @return 如果有主人返回true，否则返回false
     */
    public boolean hasOwner(String playerName) {
        return nekoManager.hasOwner(playerName);
    }

    /**
     * 检查玩家是否是某只猫娘的主人
     *
     * @param ownerName  主人名
     * @param nekoName   猫娘名
     * @return 如果是主人返回true，否则返回false
     */
    public boolean isOwnerOf(String ownerName, String nekoName) {
        return nekoManager.isOwnerOf(ownerName, nekoName);
    }
}