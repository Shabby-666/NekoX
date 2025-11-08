package org.cneko.nekox.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.NekoManager;

public class NekoChat implements Listener {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    
    public NekoChat() {
        this.plugin = NekoX.getInstance();
        this.nekoManager = plugin.getNekoManager();
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        FileConfiguration config = plugin.getConfig();
        
        if (!config.getBoolean("neko-chat.enabled", true)) {
            return;
        }
        
        Player player = event.getPlayer();
        
        // 检查玩家是否是猫娘
        if (!nekoManager.isNeko(player)) {
            return; // 不是猫娘，不应用聊天效果
        }
        
        String message = event.getMessage();
        
        // 添加猫娘聊天前缀和后缀
        String prefix = config.getString("neko-chat.prefix", "§6[猫娘] §f");
        String suffix = config.getString("neko-chat.suffix", " §6喵~");
        
        // 从配置文件中读取并应用关键字替换规则
        if (config.contains("neko-chat.keyword-replace")) {
            java.util.Map<String, Object> replaceRules = config.getConfigurationSection("neko-chat.keyword-replace").getValues(false);
            for (java.util.Map.Entry<String, Object> entry : replaceRules.entrySet()) {
                String keyword = entry.getKey();
                String replacement = String.valueOf(entry.getValue());
                message = message.replaceAll(keyword, replacement);
            }
        }
        
        // 设置新的聊天格式
        event.setFormat(prefix + player.getDisplayName() + "§f: " + message + suffix);
    }
}