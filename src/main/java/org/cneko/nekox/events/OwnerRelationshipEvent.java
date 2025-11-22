package org.cneko.nekox.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 主人关系变更事件
 * 当猫娘与主人的关系发生变化时触发
 */
public class OwnerRelationshipEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player neko;
    private final Player owner;
    private final boolean added;

    public OwnerRelationshipEvent(Player neko, Player owner, boolean added) {
        this.neko = neko;
        this.owner = owner;
        this.added = added;
    }

    /**
     * 获取猫娘玩家
     * @return 猫娘玩家实例
     */
    public Player getNeko() {
        return neko;
    }

    /**
     * 获取主人玩家
     * @return 主人玩家实例
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * 检查是添加还是移除关系
     * @return true表示添加关系，false表示移除关系
     */
    public boolean isAdded() {
        return added;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}