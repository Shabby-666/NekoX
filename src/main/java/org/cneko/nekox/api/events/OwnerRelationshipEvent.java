package org.cneko.nekox.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 当猫娘与主人的关系发生变化时触发的事件
 */
public class OwnerRelationshipEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    
    private final String nekoName;
    private final String ownerName;
    private final RelationshipAction action;
    
    public OwnerRelationshipEvent(String nekoName, String ownerName, RelationshipAction action) {
        this.nekoName = nekoName;
        this.ownerName = ownerName;
        this.action = action;
    }
    
    /**
     * 获取猫娘玩家名
     *
     * @return 猫娘玩家名
     */
    public String getNekoName() {
        return nekoName;
    }
    
    /**
     * 获取主人玩家名
     *
     * @return 主人玩家名
     */
    public String getOwnerName() {
        return ownerName;
    }
    
    /**
     * 获取关系操作类型
     *
     * @return 关系操作类型
     */
    public RelationshipAction getAction() {
        return action;
    }
    
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    /**
     * 关系操作类型枚举
     */
    public enum RelationshipAction {
        /**
         * 添加主人关系
         */
        ADD,
        
        /**
         * 移除主人关系
         */
        REMOVE,
        
        /**
         * 主人申请
         */
        REQUEST
    }
}