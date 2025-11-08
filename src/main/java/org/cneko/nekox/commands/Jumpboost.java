package org.cneko.nekox.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.NekoManager;
import org.cneko.nekox.utils.VersionUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Jumpboost implements CommandExecutor, TabCompleter {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    private final LanguageManager languageManager;
    
    public Jumpboost(NekoX plugin) {
        this.plugin = plugin;
        this.nekoManager = plugin.getNekoManager();
        this.languageManager = plugin.getLanguageManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
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
            
            applyJumpboost(player);
            return true;
        }
        
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§c" + languageManager.getMessage("commands.player_not_found"));
            return true;
        }
        
        // 检查目标玩家是否是猫娘
        if (!nekoManager.isNeko(target)) {
            sender.sendMessage("§c" + languageManager.getMessage("commands.health.notneko"));
            return true;
        }
        
        applyJumpboost(target);
        if (sender instanceof Player && sender != target) {
            HashMap<String, String> replacements = new HashMap<>();
            replacements.put("player", target.getName());
            sender.sendMessage("§e" + languageManager.replacePlaceholders(languageManager.getMessage("commands.pat.success"), replacements));
        }
        
        return true;
    }
    
    private void applyJumpboost(Player player) {
        player.addPotionEffect(new PotionEffect(VersionUtils.getJumpBoostEffect(), 3000, 1, false, false));
        player.sendMessage("§e" + languageManager.getMessage("commands.jumpboost.success"));
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