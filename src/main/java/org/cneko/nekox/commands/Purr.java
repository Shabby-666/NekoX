package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.NekoManager;
import org.cneko.nekox.utils.LanguageManager;
import java.util.HashMap;

public class Purr implements CommandExecutor {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    private final LanguageManager languageManager;
    
    public Purr(NekoX plugin) {
        this.plugin = plugin;
        this.nekoManager = plugin.getNekoManager();
        this.languageManager = plugin.getLanguageManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c" + languageManager.getMessage("commands.only_player"));
            return true;
        }
        
        Player player = (Player) sender;
        
        // 检查玩家是否是猫娘
        if (!nekoManager.isNeko(player)) {
            player.sendMessage("§c" + languageManager.getMessage("commands.health.notneko"));
            return true;
        }
        
        // 发送消息给玩家和周围的人
        player.sendMessage("§e" + languageManager.getMessage("commands.purr.success"));
        player.getWorld().playSound(player.getLocation(), "entity.cat.purr", 1.0f, 1.0f);

        // 通知周围的玩家
        for (Player nearby : player.getWorld().getPlayers()) {
            if (nearby != player && nearby.getLocation().distance(player.getLocation()) <= 10) {
                HashMap<String, String> replacements = new HashMap<>();
                replacements.put("player", player.getName());
                nearby.sendMessage("§a" + languageManager.replacePlaceholders(languageManager.getMessage("commands.pat.success"), replacements));
            }
        }
        
        return true;
    }
}