package org.cneko.nekox.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 猫娘状态变更事件
 * 当玩家的猫娘状态发生变化时触发
 */
public class NekoStatusChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final boolean isNeko;
    private final boolean oldStatus;

    public NekoStatusChangeEvent(Player player, boolean isNeko, boolean oldStatus) {
        this.player = player;
        this.isNeko = isNeko;
        this.oldStatus = oldStatus;
    }

    /**
     * 获取状态变更的玩家
     * @return 玩家实例
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * 获取新的猫娘状态
     * @return true表示成为猫娘，false表示不再是猫娘
     */
    public boolean isNeko() {
        return isNeko;
    }

    /**
     * 获取旧的猫娘状态
     * @return true表示之前是猫娘，false表示之前不是猫娘
     */
    public boolean getOldStatus() {
        return oldStatus;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}