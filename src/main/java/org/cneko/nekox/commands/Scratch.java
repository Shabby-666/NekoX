package org.cneko.nekox.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.SafeMessageUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scratch implements CommandExecutor, TabCompleter {
    private final NekoX plugin;
    private final LanguageManager languageManager;
    
    public Scratch(NekoX plugin) {
        this.plugin = plugin;
        this.languageManager = plugin.getLanguageManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.only_player", "Only players can use this command!"));
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            player.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.scratch", "Usage: /scratch <player>"));
            return true;
        }
        
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.player_not_found", "Player not found!"));
            return true;
        }
        
        // 发送消息给玩家
        HashMap<String, String> replacements = new HashMap<>();
        replacements.put("player", target.getName());
        String scratchMessage = SafeMessageUtils.getSafeMessage(languageManager, "commands.scratch.success", "You scratched {player}!");
        player.sendMessage("§e" + SafeMessageUtils.replacePlaceholdersSafe(scratchMessage, replacements));
        
        replacements.put("player", player.getName());
        target.sendMessage("§a" + SafeMessageUtils.replacePlaceholdersSafe(scratchMessage, replacements));
        
        // 造成少量伤害
        target.damage(1.0);
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // 提供在线玩家列表补全
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(player.getName());
                }
            }
        }
        
        return completions;
    }
}