package org.cneko.nekox.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.NekoXPlaceholderExpansion;
// 移除未使用的导入
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class NekoXCommand implements CommandExecutor, TabCompleter {
    private final NekoX plugin;
    private final LanguageManager languageManager;
    
    public NekoXCommand(NekoX plugin) {
        this.plugin = plugin;
        this.languageManager = plugin.getLanguageManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§6===== " + languageManager.getMessage("plugin.name") + " =====");
            sender.sendMessage("§e" + languageManager.getMessage("plugin.version"));
            sender.sendMessage("§e" + languageManager.getMessage("plugin.chinese_author"));
            sender.sendMessage("§e" + languageManager.getMessage("plugin.help_info"));
            sender.sendMessage("§e===== " + languageManager.getMessage("plugin.original_authors") + " =====");
            sender.sendMessage("§eToNeko：https://modrinth.com/user/CrystalNeko");
            sender.sendMessage("§eNekoC：https://modrinth.com/user/Kurumi78");
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "help":
            case "h":
                sendHelp(sender);
                break;
            case "reload":
            case "r":
                if (!sender.hasPermission("nekox.admin")) {
                    sender.sendMessage("§c" + languageManager.getMessage("commands.no_permission"));
                    return true;
                }
                plugin.reloadConfig();
                sender.sendMessage("§e" + languageManager.getMessage("commands.nekox.reloaded"));
                break;
            case "version":
            case "v":
                sender.sendMessage("§6" + languageManager.getMessage("plugin.name") + " " + languageManager.getMessage("plugin.version_label") + " §e" + languageManager.getMessage("plugin.version_full"));
                break;
            case "language":
            case "lang":
                handleLanguageCommand(sender, args);
                break;
            case "placeholders":
                handlePlaceholdersCommand(sender);
                break;
            default:
                sender.sendMessage("§c" + languageManager.getMessage("commands.unknown"));
                break;
        }
        
        return true;
    }
    
    /**
     * 处理语言切换命令
     */
    private void handleLanguageCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            // 显示当前语言和可用语言列表
            HashMap<String, String> replacements = new HashMap<>();
            replacements.put("language", languageManager.getCurrentLanguage());
            sender.sendMessage("§e" + languageManager.replacePlaceholders(languageManager.getMessage("commands.language.current"), replacements));
            
            replacements.clear();
            replacements.put("languages", String.join(", ", languageManager.getAvailableLanguages()));
            sender.sendMessage("§e" + languageManager.replacePlaceholders(languageManager.getMessage("commands.language.available"), replacements));
            
            sender.sendMessage("§e" + languageManager.getMessage("commands.language.usage"));
            return;
        }
        
        String language = args[1];
        Set<String> availableLanguages = languageManager.getAvailableLanguages();
        
        if (availableLanguages.contains(language)) {
            languageManager.setLanguage(language);
            HashMap<String, String> replacements = new HashMap<>();
            replacements.put("language", language);
            sender.sendMessage("§a" + languageManager.replacePlaceholders(languageManager.getMessage("commands.language.changed"), replacements));
        } else {
            HashMap<String, String> replacements = new HashMap<>();
            replacements.put("languages", String.join(", ", availableLanguages));
            sender.sendMessage("§c" + languageManager.replacePlaceholders(languageManager.getMessage("commands.language.invalid"), replacements));
        }
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6===== " + languageManager.getMessage("commands.help.title") + " =====");
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.pat"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.lovebite"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.earscratch"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.purr"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.hiss"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.scratch"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.attention"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.nightvision"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.jumpboost"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.swiftsneak"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.health"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.nekox"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.nekox_reload"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.nekox_version"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.nekoset"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.nekox_language"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.nekox_placeholders"));
    }
    
    /**
     * 处理占位符列表命令
     */
    private void handlePlaceholdersCommand(CommandSender sender) {
        // 检查PlaceholderAPI是否已安装
        boolean placeholderAPIInstalled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        
        // 获取所有占位符列表
        List<String> allPlaceholders = NekoXPlaceholderExpansion.getAllPlaceholders();
        int totalPlaceholders = allPlaceholders.size();
        int registeredPlaceholders = placeholderAPIInstalled ? totalPlaceholders : 0;
        
        // 发送占位符列表信息
        sender.sendMessage("§6===== NekoX 占位符列表 ======");
        if (placeholderAPIInstalled) {
            sender.sendMessage("§ePlaceholderAPI 已安装");
        } else {
            sender.sendMessage("§cPlaceholderAPI 未安装");
        }
        
        sender.sendMessage("§e成功注册的占位符：" + registeredPlaceholders + "/" + totalPlaceholders);
        
        // 显示所有占位符
        sender.sendMessage("§a所有占位符：");
        for (String placeholder : allPlaceholders) {
            String status = placeholderAPIInstalled ? "§a✓" : "§c✗";
            sender.sendMessage("§e- %nekox_" + placeholder + "% " + status);
        }
        
        if (!placeholderAPIInstalled) {
            sender.sendMessage("§c提示：安装PlaceholderAPI后可以使用这些占位符。");
        }
    }
    
    /**
     * 实现TabCompleter接口的方法，提供命令补全功能
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        
        // 检查是否是nekox命令
        if (cmd.getName().equalsIgnoreCase("nekox")) {
            // 当没有参数时，返回所有子命令
            if (args.length == 1) {
                List<String> subcommands = Arrays.asList(
                    "help", "h",
                    "reload", "r",
                    "version", "v",
                    "language", "lang",
                    "placeholders"
                );
                
                for (String subcommand : subcommands) {
                    if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                        completions.add(subcommand);
                    }
                }
            }
            // 对于language子命令，提供语言列表补全
            else if (args.length == 2 && (args[0].equalsIgnoreCase("language") || args[0].equalsIgnoreCase("lang"))) {
                Set<String> availableLanguages = languageManager.getAvailableLanguages();
                for (String language : availableLanguages) {
                    if (language.toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(language);
                    }
                }
            }
        }
        
        return completions;
    }
}