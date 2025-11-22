package org.cneko.nekox.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 当玩家的猫娘状态发生变化时触发的事件
 */
public class NekoStatusChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    
    private final Player player;
    private final boolean isNeko;
    private final boolean isOnline;
    
    public NekoStatusChangeEvent(Player player, boolean isNeko, boolean isOnline) {
        this.player = player;
        this.isNeko = isNeko;
        this.isOnline = isOnline;
    }
    
    /**
     * 获取状态发生变化的玩家
     *
     * @return 玩家对象
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * 获取玩家的新状态
     *
     * @return 如果玩家现在是猫娘则返回true，否则返回false
     */
    public boolean isNeko() {
        return isNeko;
    }
    
    /**
     * 获取玩家是否在线
     *
     * @return 如果玩家在线则返回true，否则返回false
     */
    public boolean isOnline() {
        return isOnline;
    }
    
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}