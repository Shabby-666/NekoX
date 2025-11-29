package org.cneko.nekox.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import org.cneko.nekox.NekoX;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerProximityListener implements Listener {
    private final NekoX plugin;
    private final Map<UUID, BukkitTask> playerTasks = new HashMap<>();
    private static final int CHECK_RADIUS = 25;
    private static final int CHECK_INTERVAL = 20; // 20 ticks = 1 second

    public PlayerProximityListener(NekoX plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        startProximityCheckTask(player);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        cancelProximityCheckTask(player);
    }

    private void startProximityCheckTask(Player player) {
        cancelProximityCheckTask(player);

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> checkNearbyPlayers(player), 0, CHECK_INTERVAL);
        playerTasks.put(player.getUniqueId(), task);
    }

    private void cancelProximityCheckTask(Player player) {
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
        if (!nekoPlayer.isOnline()) {
            cancelProximityCheckTask(nekoPlayer);
            return;
        }

        // 异步检查猫娘状态和通知设置
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            boolean isNeko = plugin.getNekoManager().isNeko(nekoPlayer);
            boolean isNoticeEnabled = plugin.getPlayerConfigManager().isNoticeEnabled(nekoPlayer);
            
            // 回到主线程执行UI操作
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (!isNeko || !isNoticeEnabled) {
                    return;
                }

                StringBuilder noticeMessage = new StringBuilder();
                boolean hasNearbyPlayers = false;

                for (Player nearbyPlayer : Bukkit.getOnlinePlayers()) {
                    if (nearbyPlayer.equals(nekoPlayer)) {
                        continue;
                    }

                    if (!nearbyPlayer.getWorld().equals(nekoPlayer.getWorld())) {
                        continue;
                    }

                    double distance = nearbyPlayer.getLocation().distance(nekoPlayer.getLocation());

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
                    String subtitle = noticeMessage.toString();
                    nekoPlayer.sendTitle(title, subtitle, 5, 40, 5);
                }
            });
        });
    }
}