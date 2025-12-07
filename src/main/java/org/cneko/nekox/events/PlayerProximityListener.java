package org.cneko.nekox.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import org.cneko.nekox.NekoX;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class PlayerProximityListener implements Listener {
    private final NekoX plugin;
    private final Map<UUID, BukkitTask> playerTasks = new HashMap<>();
    private static final int CHECK_RADIUS = 25;
    private static final int CHECK_INTERVAL = 10; // 10 ticks = 0.5 seconds，提高更新频率

    public PlayerProximityListener(NekoX plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // 确保玩家不为null
        if (player != null) {
            startProximityCheckTask(player);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // 确保玩家不为null
        if (player != null) {
            cancelProximityCheckTask(player);
        }
    }

    private void startProximityCheckTask(Player player) {
        // 确保玩家不为null
        if (player == null) {
            return;
        }
        
        cancelProximityCheckTask(player);

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> checkNearbyPlayers(player), 0, CHECK_INTERVAL);
        playerTasks.put(player.getUniqueId(), task);
    }

    private void cancelProximityCheckTask(Player player) {
        // 确保玩家不为null
        if (player == null) {
            return;
        }
        
        UUID playerId = player.getUniqueId();
        if (playerTasks.containsKey(playerId)) {
            BukkitTask task = playerTasks.get(playerId);
            if (task != null) {
                task.cancel();
            }
            playerTasks.remove(playerId);
        }
    }

    private void checkNearbyPlayers(Player nekoPlayer) {
        // 确保玩家不为null且在线
        if (nekoPlayer == null || !nekoPlayer.isOnline()) {
            cancelProximityCheckTask(nekoPlayer);
            return;
        }

        // 异步检查猫娘状态和通知设置
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            // 添加空指针检查
            if (nekoPlayer == null || plugin == null || plugin.getNekoManager() == null || plugin.getPlayerConfigManager() == null) {
                return;
            }
            
            boolean isNeko = plugin.getNekoManager().isNeko(nekoPlayer);
            boolean isNoticeEnabled = plugin.getPlayerConfigManager().isNoticeEnabled(nekoPlayer);
            
            // 回到主线程执行UI操作
            Bukkit.getScheduler().runTask(plugin, () -> {
                // 再次检查玩家是否仍然在线且不为null
                if (nekoPlayer == null || !nekoPlayer.isOnline() || !isNeko || !isNoticeEnabled) {
                    return;
                }

                StringBuilder noticeMessage = new StringBuilder();
                boolean hasNearbyPlayers = false;

                // 获取猫娘玩家的位置，添加空指针检查
                Location nekoLocation = nekoPlayer.getLocation();
                if (nekoLocation == null) {
                    return;
                }

                for (Player nearbyPlayer : Bukkit.getOnlinePlayers()) {
                    // 添加多个空指针检查
                    if (nearbyPlayer == null || nearbyPlayer.equals(nekoPlayer)) {
                        continue;
                    }

                    // 检查世界是否相同
                    if (nearbyPlayer.getWorld() == null || nekoPlayer.getWorld() == null || 
                        !nearbyPlayer.getWorld().equals(nekoPlayer.getWorld())) {
                        continue;
                    }

                    // 获取附近玩家的位置
                    Location nearbyLocation = nearbyPlayer.getLocation();
                    if (nearbyLocation == null) {
                        continue;
                    }

                    double distance = nearbyLocation.distance(nekoLocation);

                    if (distance <= CHECK_RADIUS) {
                        if (hasNearbyPlayers) {
                            noticeMessage.append(", ");
                        }
                        noticeMessage.append(nearbyPlayer.getName()).append(": ").append(String.format("%.1f", distance)).append("m");
                        hasNearbyPlayers = true;
                    }
                }

                if (hasNearbyPlayers) {
                            String title = plugin.getLanguageManager().getMessage("player_notice.title");
                            // 为ActionBar消息添加颜色代码，使标题为金色，玩家列表为绿色
                            String message = "§6" + title + " §e> §a" + noticeMessage.toString();
                            // 确保在发送之前玩家仍然在线
                            if (nekoPlayer.isOnline()) {
                                // 使用Spigot API的sendActionBar方法，适配1.20.1+服务端
                            try {
                                // 直接使用Spigot API的sendActionBar方法，这是1.20.1+版本中最可靠的方式
                                nekoPlayer.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, 
                                    net.md_5.bungee.api.chat.TextComponent.fromLegacyText(message));
                            } catch (NoSuchMethodError | Exception e) {
                                // 如果失败，记录异常信息
                                plugin.getLogger().warning("ActionBar发送失败: " + e.getMessage());
                            }
                            }
                        }
            });
        });
    }
}