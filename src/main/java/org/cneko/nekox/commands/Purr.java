package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.NekoManager;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.SafeMessageUtils;
import java.util.HashMap;

public class Purr implements CommandExecutor {
    private final NekoManager nekoManager;
    private final LanguageManager languageManager;
    
    public Purr(NekoX plugin) {
        this.nekoManager = plugin.getNekoManager();
        this.languageManager = plugin.getLanguageManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.only_player", "Only players can use this command!"));
            return true;
        }
        
        Player player = (Player) sender;
        
        // 检查玩家是否是猫娘
        if (!nekoManager.isNeko(player)) {
            player.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.health.notneko", "You are not a neko!"));
            return true;
        }
        
        // 发送消息给玩家和周围的人
        player.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.purr.success", "You purr cutely!"));
        player.getWorld().playSound(player.getLocation(), "entity.cat.purr", 1.0f, 1.0f);

        // 通知周围的玩家
        for (Player nearby : player.getWorld().getPlayers()) {
            if (nearby != player && nearby.getLocation().distance(player.getLocation()) <= 10) {
                HashMap<String, String> replacements = new HashMap<>();
                replacements.put("player", player.getName());
                String purrMessage = SafeMessageUtils.getSafeMessage(languageManager, "commands.purr.heard", "{player} is purring cutely!");
                nearby.sendMessage("§a" + SafeMessageUtils.replacePlaceholdersSafe(purrMessage, replacements));
            }
        }
        
        return true;
    }
}