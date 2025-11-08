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

public class NekoSetCommand implements CommandExecutor, TabCompleter {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    private final LanguageManager languageManager;
    
    public NekoSetCommand(NekoX plugin) {
        this.plugin = plugin;
        this.nekoManager = plugin.getNekoManager();
        this.languageManager = plugin.getLanguageManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 检查命令发送者是否有权限
        if (!sender.hasPermission("nekox.admin")) {
            sender.sendMessage(languageManager.getMessage("commands.no_permission"));
            return true;
        }
        
        // 检查参数数量
        if (args.length < 2) {
            sender.sendMessage(languageManager.getMessage("commands.help.nekoset"));
            return true;
        }
        
        // 获取目标玩家
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(languageManager.getMessage("commands.player_not_found"));
            return true;
        }
        
        // 解析是否设置为猫娘
        boolean isNeko;
        try {
            isNeko = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            sender.sendMessage(languageManager.getMessage("commands.nekoset.invalid_param"));
            return true;
        }
        
        // 设置玩家的猫娘状态
        boolean changed = nekoManager.setNeko(target, isNeko);
        
        if (changed) {
            if (isNeko) {
                HashMap<String, String> replacements1 = new HashMap<>();
                replacements1.put("player", target.getName());
                sender.sendMessage(languageManager.getMessage("commands.nekoset.set_to_neko", replacements1));
                target.sendMessage(languageManager.getMessage("commands.nekoset.set_to_neko_target"));
            } else {
                HashMap<String, String> replacements2 = new HashMap<>();
                replacements2.put("player", target.getName());
                sender.sendMessage(languageManager.getMessage("commands.nekoset.set_to_human", replacements2));
                target.sendMessage(languageManager.getMessage("commands.nekoset.set_to_human_target"));
            }
        } else {
            if (isNeko) {
                HashMap<String, String> replacements3 = new HashMap<>();
                replacements3.put("player", target.getName());
                sender.sendMessage(languageManager.getMessage("commands.nekoset.already_neko", replacements3));
            } else {
                HashMap<String, String> replacements4 = new HashMap<>();
                replacements4.put("player", target.getName());
                sender.sendMessage(languageManager.getMessage("commands.nekoset.already_not_neko", replacements4));
            }
        }
        
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
        } else if (args.length == 2) {
            // 提供布尔值补全
            if ("true".startsWith(args[1].toLowerCase())) {
                completions.add("true");
            }
            if ("false".startsWith(args[1].toLowerCase())) {
                completions.add("false");
            }
        }
        
        return completions;
    }
}