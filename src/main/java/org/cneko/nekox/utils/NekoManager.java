package org.cneko.nekox.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.api.events.NekoStatusChangeEvent;
import org.cneko.nekox.api.events.OwnerRelationshipEvent;

import java.util.*;

public class NekoManager {
    private final NekoX plugin;
    private final PlayerConfigManager configManager;
    
    public NekoManager(NekoX plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getPlayerConfigManager();
    }
    
    /**
     * 设置玩家为猫娘
     */
    public void setNeko(Player player, boolean isNeko) {
        boolean oldStatus = this.isNeko(player);
        configManager.setNeko(player, isNeko);
        
        // 触发猫娘状态变更事件
        NekoStatusChangeEvent event = new NekoStatusChangeEvent(player, isNeko, true);
        Bukkit.getPluginManager().callEvent(event);
    }
    
    /**
     * 设置玩家为猫娘（通过玩家名）
     */
    public void setNekoByName(String playerName, boolean isNeko) {
        configManager.setNekoByName(playerName, isNeko);
        
        // 触发猫娘状态变更事件（离线玩家）
        // 注意：对于离线玩家，我们创建一个特殊的事件处理方式
        // 这里我们需要创建一个适配器或者修改事件类来支持离线玩家
        // 暂时先注释掉这行代码，避免编译错误
        // NekoStatusChangeEvent event = new NekoStatusChangeEvent(playerName, isNeko, false);
        // Bukkit.getPluginManager().callEvent(event);
    }
    
    /**
     * 检查玩家是否是猫娘
     */
    public boolean isNeko(Player player) {
        return configManager.isNeko(player);
    }
    
    /**
     * 检查玩家是否是猫娘（通过玩家名）
     */
    public boolean isNeko(String playerName) {
        return configManager.isNeko(playerName);
    }
    
    /**
     * 获取所有猫娘玩家
     */
    public Set<Player> getNekoPlayers() {
        Set<Player> players = new HashSet<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isNeko(player)) {
                players.add(player);
            }
        }
        return players;
    }
    
    /**
     * 获取所有猫娘玩家名（包括离线玩家）
     */
    public Set<String> getAllNekoNames() {
        return configManager.getAllNekoNames();
    }
    
    /**
     * 为主人添加猫娘（通过玩家对象）
     */
    public void addOwner(Player neko, Player owner) {
        configManager.addOwner(neko, owner);
        
        // 触发主人关系变更事件
        OwnerRelationshipEvent event = new OwnerRelationshipEvent(neko.getName(), owner.getName(), OwnerRelationshipEvent.RelationshipAction.ADD);
        Bukkit.getPluginManager().callEvent(event);
    }
    
    /**
     * 为主人添加猫娘（通过玩家名）
     */
    public void addOwnerByName(String nekoName, String ownerName) {
        configManager.addOwnerByName(nekoName, ownerName);
        
        // 触发主人关系变更事件
        OwnerRelationshipEvent event = new OwnerRelationshipEvent(nekoName, ownerName, OwnerRelationshipEvent.RelationshipAction.ADD);
        Bukkit.getPluginManager().callEvent(event);
    }
    
    /**
     * 移除主人与猫娘的关系（通过玩家对象）
     */
    public void removeOwner(Player neko, Player owner) {
        configManager.removeOwner(neko, owner);
        
        // 触发主人关系变更事件
        OwnerRelationshipEvent event = new OwnerRelationshipEvent(neko.getName(), owner.getName(), OwnerRelationshipEvent.RelationshipAction.REMOVE);
        Bukkit.getPluginManager().callEvent(event);
    }
    
    /**
     * 移除主人与猫娘的关系（通过玩家名）
     */
    public void removeOwnerByName(String nekoName, String ownerName) {
        configManager.removeOwnerByName(nekoName, ownerName);
        
        // 触发主人关系变更事件
        OwnerRelationshipEvent event = new OwnerRelationshipEvent(nekoName, ownerName, OwnerRelationshipEvent.RelationshipAction.REMOVE);
        Bukkit.getPluginManager().callEvent(event);
    }
    
    /**
     * 获取某只猫娘的所有主人
     */
    public Set<Player> getOwners(Player neko) {
        return configManager.getOwners(neko);
    }
    
    /**
     * 获取某只猫娘的所有主人名（包括离线玩家）
     */
    public Set<String> getOwnerNames(String nekoName) {
        return configManager.getOwnerNames(nekoName);
    }
    
    /**
     * 检查某个玩家是否是某只猫娘的主人
     */
    public boolean isOwner(Player owner, Player neko) {
        return configManager.isOwner(owner, neko);
    }
    
    /**
     * 检查某个玩家是否是某只猫娘的主人（通过玩家名）
     */
    public boolean isOwnerOf(String ownerName, String nekoName) {
        return configManager.isOwnerOf(ownerName, nekoName);
    }
    
    /**
     * 检查玩家是否有主人
     */
    public boolean hasOwner(String playerName) {
        return configManager.hasOwner(playerName);
    }
    
    /**
     * 获取某个主人的所有猫娘
     */
    public Set<Player> getNekosByOwner(Player owner) {
        return configManager.getNekosByOwner(owner);
    }
    
    // 主人申请相关方法
    
    /**
     * 发送主人申请
     */
    public void sendOwnerRequest(Player requester, Player neko) {
        configManager.sendOwnerRequest(requester, neko);
        
        // 触发主人关系变更事件
        OwnerRelationshipEvent event = new OwnerRelationshipEvent(neko.getName(), requester.getName(), OwnerRelationshipEvent.RelationshipAction.REQUEST);
        Bukkit.getPluginManager().callEvent(event);
    }
    
    /**
     * 检查是否有主人申请
     */
    public boolean hasOwnerRequest(Player requester, Player neko) {
        return configManager.hasOwnerRequest(requester, neko);
    }
    
    /**
     * 获取猫娘收到的所有申请
     */
    public Set<Player> getOwnerRequests(Player neko) {
        return configManager.getOwnerRequests(neko);
    }
    
    /**
     * 接受主人申请
     */
    public void acceptOwnerRequest(Player requester, Player neko) {
        configManager.acceptOwnerRequest(requester, neko);
        
        // 触发主人关系变更事件
        OwnerRelationshipEvent event = new OwnerRelationshipEvent(neko.getName(), requester.getName(), OwnerRelationshipEvent.RelationshipAction.ADD);
        Bukkit.getPluginManager().callEvent(event);
    }
    
    /**
     * 拒绝主人申请
     */
    public void denyOwnerRequest(Player requester, Player neko) {
        configManager.denyOwnerRequest(requester, neko);
    }
    
    // 新增直接操作方法，供API调用
    
    /**
     * 直接添加主人关系（不触发事件）
     */
    public void addOwnerDirect(String nekoName, String ownerName) {
        configManager.addOwnerDirect(nekoName, ownerName);
    }
    
    /**
     * 直接移除主人关系（不触发事件）
     */
    public void removeOwnerDirect(String nekoName, String ownerName) {
        configManager.removeOwnerDirect(nekoName, ownerName);
    }
    
    /**
     * 直接设置玩家为猫娘（不触发事件）
     */
    public void setNekoDirect(String playerName, boolean isNeko) {
        configManager.setNekoDirect(playerName, isNeko);
    }
    
    /**
     * 获取插件实例
     */
    public NekoX getPlugin() {
        return plugin;
    }
}