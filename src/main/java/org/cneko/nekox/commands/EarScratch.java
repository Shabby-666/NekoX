package org.cneko.nekox.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.NekoManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EarScratch implements CommandExecutor, TabCompleter {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    private final LanguageManager languageManager;
    
    public EarScratch(NekoX plugin) {
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
        
        if (args.length == 0) {
            player.sendMessage("§c" + languageManager.getMessage("commands.help.earscratch"));
            return true;
        }
        
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§c" + languageManager.getMessage("commands.player_not_found"));
            return true;
        }
        
        // 发送消息给玩家
        HashMap<String, String> replacements = new HashMap<>();
        replacements.put("player", target.getName());
        player.sendMessage("§e" + languageManager.replacePlaceholders(languageManager.getMessage("commands.earscratch.success"), replacements));
        
        replacements.put("player", player.getName());
        target.sendMessage("§a" + languageManager.replacePlaceholders(languageManager.getMessage("commands.earscratch.success"), replacements));
        
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