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
     * 获取所有在线猫娘玩家
     *
     * @return 所有在线猫娘玩家集合
     */
    public Set<Player> getNekoPlayers() {
        return nekoManager.getNekoPlayers();
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
     * 获取猫娘的所有主人
     *
     * @param neko 猫娘玩家对象
     * @return 主人玩家对象集合
     */
    public Set<Player> getOwners(Player neko) {
        return nekoManager.getOwners(neko);
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

    /**
     * 检查玩家是否是某只猫娘的主人（通过玩家对象）
     *
     * @param owner 主人玩家对象
     * @param neko  猫娘玩家对象
     * @return 如果是主人返回true，否则返回false
     */
    public boolean isOwner(Player owner, Player neko) {
        return nekoManager.isOwner(owner, neko);
    }

    /**
     * 为指定猫娘添加主人（通过玩家对象）
     *
     * @param neko  猫娘玩家对象
     * @param owner 主人玩家对象
     */
    public void addOwner(Player neko, Player owner) {
        nekoManager.addOwner(neko, owner);
    }

    /**
     * 为指定猫娘添加主人（通过玩家名）
     *
     * @param nekoName  猫娘玩家名
     * @param ownerName 主人玩家名
     */
    public void addOwner(String nekoName, String ownerName) {
        // 检查猫娘是否存在且是猫娘
        if (!isNeko(nekoName)) {
            throw new IllegalArgumentException("玩家 " + nekoName + " 不是猫娘或不存在");
        }

        // 直接调用NekoManager的方法来添加主人关系
        nekoManager.addOwnerByName(nekoName, ownerName);
    }

    /**
     * 移除指定猫娘的主人（通过玩家对象）
     *
     * @param neko  猫娘玩家对象
     * @param owner 主人玩家对象
     */
    public void removeOwner(Player neko, Player owner) {
        nekoManager.removeOwner(neko, owner);
    }

    /**
     * 移除指定猫娘的主人（通过玩家名）
     *
     * @param nekoName  猫娘玩家名
     * @param ownerName 主人玩家名
     */
    public void removeOwner(String nekoName, String ownerName) {
        // 检查猫娘是否存在且是猫娘
        if (!isNeko(nekoName)) {
            throw new IllegalArgumentException("玩家 " + nekoName + " 不是猫娘或不存在");
        }

        // 直接调用NekoManager的方法来移除主人关系
        nekoManager.removeOwnerByName(nekoName, ownerName);
    }

    /**
     * 设置玩家为猫娘
     *
     * @param playerName 玩家名
     * @param isNeko     是否为猫娘
     */
    public void setNeko(String playerName, boolean isNeko) {
        nekoManager.setNekoByName(playerName, isNeko);
    }

    /**
     * 设置玩家为猫娘（通过玩家对象）
     *
     * @param player 玩家对象
     * @param isNeko 是否为猫娘
     */
    public void setNeko(Player player, boolean isNeko) {
        nekoManager.setNeko(player, isNeko);
    }

    /**
     * 获取某个主人的所有猫娘
     *
     * @param owner 主人玩家对象
     * @return 猫娘玩家对象集合
     */
    public Set<Player> getNekosByOwner(Player owner) {
        return nekoManager.getNekosByOwner(owner);
    }

    // 主人申请相关方法

    /**
     * 发送主人申请
     *
     * @param requester 申请者玩家对象
     * @param neko      被申请的猫娘玩家对象
     */
    public void sendOwnerRequest(Player requester, Player neko) {
        nekoManager.sendOwnerRequest(requester, neko);
    }

    /**
     * 检查是否有主人申请
     *
     * @param requester 申请者玩家对象
     * @param neko      被申请的猫娘玩家对象
     * @return 如果有申请返回true，否则返回false
     */
    public boolean hasOwnerRequest(Player requester, Player neko) {
        return nekoManager.hasOwnerRequest(requester, neko);
    }

    /**
     * 获取猫娘收到的所有申请
     *
     * @param neko 猫娘玩家对象
     * @return 申请者玩家对象集合
     */
    public Set<Player> getOwnerRequests(Player neko) {
        return nekoManager.getOwnerRequests(neko);
    }

    /**
     * 接受主人申请
     *
     * @param requester 申请者玩家对象
     * @param neko      被申请的猫娘玩家对象
     */
    public void acceptOwnerRequest(Player requester, Player neko) {
        nekoManager.acceptOwnerRequest(requester, neko);
    }

    /**
     * 拒绝主人申请
     *
     * @param requester 申请者玩家对象
     * @param neko      被申请的猫娘玩家对象
     */
    public void denyOwnerRequest(Player requester, Player neko) {
        nekoManager.denyOwnerRequest(requester, neko);
    }

    // 通知状态相关方法

    /**
     * 设置玩家的通知状态
     *
     * @param playerName 玩家名
     * @param enabled    通知是否启用
     */
    public void setNoticeEnabled(String playerName, boolean enabled) {
        // 直接调用PlayerConfigManager的方法来设置玩家通知状态
        nekoManager.getPlugin().getPlayerConfigManager().setNekoByName(playerName, nekoManager.getPlugin().getPlayerConfigManager().isNeko(playerName));
        nekoManager.getPlugin().getPlayerConfigManager().setNoticeEnabledDirect(playerName, enabled);
    }

    /**
     * 设置玩家的通知状态（通过玩家对象）
     *
     * @param player  玩家对象
     * @param enabled 通知是否启用
     */
    public void setNoticeEnabled(Player player, boolean enabled) {
        // 直接调用PlayerConfigManager的方法来设置玩家通知状态
        nekoManager.getPlugin().getPlayerConfigManager().setNeko(player, nekoManager.getPlugin().getPlayerConfigManager().isNeko(player));
        nekoManager.getPlugin().getPlayerConfigManager().setNoticeEnabled(player, enabled);
    }

    /**
     * 检查玩家的通知状态
     *
     * @param player 玩家对象
     * @return 如果通知启用返回true，否则返回false
     */
    public boolean isNoticeEnabled(Player player) {
        return nekoManager.getPlugin().getPlayerConfigManager().isNoticeEnabled(player);
    }

    // 新增API方法：允许外部插件直接写入主人关系到数据库

    /**
     * 直接向数据库添加猫娘与主人的关系（不触发事件）
     * 此方法适用于其他插件需要批量导入或同步数据的场景
     *
     * @param nekoName  猫娘玩家名
     * @param ownerName 主人玩家名
     * @throws IllegalArgumentException 如果猫娘不存在或不是猫娘
     */
    public void addOwnerDirect(String nekoName, String ownerName) {
        // 检查猫娘是否存在且是猫娘
        if (!isNeko(nekoName)) {
            throw new IllegalArgumentException("玩家 " + nekoName + " 不是猫娘或不存在");
        }

        // 直接调用PlayerConfigManager的方法来添加主人关系（不触发事件）
        nekoManager.getPlugin().getPlayerConfigManager().addOwnerDirect(nekoName, ownerName);
    }

    /**
     * 直接从数据库移除猫娘与主人的关系（不触发事件）
     * 此方法适用于其他插件需要批量删除或同步数据的场景
     *
     * @param nekoName  猫娘玩家名
     * @param ownerName 主人玩家名
     */
    public void removeOwnerDirect(String nekoName, String ownerName) {
        // 检查猫娘是否存在且是猫娘
        if (!isNeko(nekoName)) {
            throw new IllegalArgumentException("玩家 " + nekoName + " 不是猫娘或不存在");
        }

        // 直接调用PlayerConfigManager的方法来移除主人关系（不触发事件）
        nekoManager.getPlugin().getPlayerConfigManager().removeOwnerDirect(nekoName, ownerName);
    }

    /**
     * 直接设置玩家为猫娘（不触发事件）
     * 此方法适用于其他插件需要批量设置或同步数据的场景
     *
     * @param playerName 玩家名
     * @param isNeko     是否为猫娘
     */
    public void setNekoDirect(String playerName, boolean isNeko) {
        // 直接调用PlayerConfigManager的方法来设置玩家为猫娘（不触发事件）
        nekoManager.getPlugin().getPlayerConfigManager().setNekoDirect(playerName, isNeko);
    }

    /**
     * 直接设置玩家的通知状态（不触发事件）
     * 此方法适用于其他插件需要批量设置或同步数据的场景
     *
     * @param playerName 玩家名
     * @param enabled    通知是否启用
     */
    public void setNoticeEnabledDirect(String playerName, boolean enabled) {
        // 直接调用PlayerConfigManager的方法来设置玩家通知状态（不触发事件）
        nekoManager.getPlugin().getPlayerConfigManager().setNoticeEnabledDirect(playerName, enabled);
    }
}