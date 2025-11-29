package org.cneko.nekox.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.NekoManager;
import org.cneko.nekox.utils.SafeMessageUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Nightvision implements CommandExecutor, TabCompleter {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    private final LanguageManager languageManager;
    
    public Nightvision(NekoX plugin) {
        this.plugin = plugin;
        this.nekoManager = plugin.getNekoManager();
        this.languageManager = plugin.getLanguageManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
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
            
            applyNightvision(player);
            return true;
        }
        
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.player_not_found", "Player not found!"));
            return true;
        }
        
        // 检查目标玩家是否是猫娘
        if (!nekoManager.isNeko(target)) {
            sender.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.health.notneko", "Target player is not a neko!"));
            return true;
        }
        
        applyNightvision(target);
        if (sender instanceof Player && sender != target) {
            HashMap<String, String> replacements = new HashMap<>();
            replacements.put("player", target.getName());
            String message = SafeMessageUtils.getSafeMessage(languageManager, "commands.nightvision.success_other", "You gave night vision to {player}!");
            sender.sendMessage("§e" + SafeMessageUtils.replacePlaceholdersSafe(message, replacements));
        }
        
        return true;
    }
    
    private void applyNightvision(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 6000, 0, false, false));
        player.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.nightvision.success", "Night vision effect applied!"));
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