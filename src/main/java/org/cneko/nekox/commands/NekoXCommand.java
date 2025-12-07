package org.cneko.nekox.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.LanguageManager;
// 移除未使用的导入
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        // 检查LanguageManager是否已正确初始化
        if (languageManager == null) {
            sender.sendMessage("§cNekoX插件语言系统未正确初始化，请联系管理员检查插件配置。");
            plugin.getLogger().severe("LanguageManager未正确初始化，请检查插件配置文件和语言文件。");
            return true;
        }
        
        if (args.length == 0) {
            // 添加空值检查，确保消息不会返回null
            String pluginName = getSafeMessage("plugin.name", "NekoX");
            String pluginVersion = getSafeMessage("plugin.version", "Version: 5.2-ProMax+++");
            String chineseAuthor = getSafeMessage("plugin.chinese_author", "This modified version author: _Chinese_Player_");
            String helpInfo = getSafeMessage("plugin.help_info", "Type /nekoxhelp for help");
            String originalAuthors = getSafeMessage("plugin.original_authors", "Original Authors");
            
            // 为所有消息添加统一格式
            sender.sendMessage("§dNekoX §e>> §6===== " + pluginName + " =====");
            sender.sendMessage("§dNekoX §e>> §e" + pluginVersion);
            sender.sendMessage(chineseAuthor);
            sender.sendMessage(helpInfo);
            sender.sendMessage("§dNekoX §e>> §6===== " + originalAuthors + " =====");
            sender.sendMessage("§dNekoX §e>> ToNeko：https://modrinth.com/user/CrystalNeko");
            sender.sendMessage("§dNekoX §e>> NekoC：https://modrinth.com/user/Kurumi78");
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
                    sender.sendMessage(getSafeMessage("commands.no_permission", "You don't have permission to execute this command!"));
                    return true;
                }
                plugin.reloadConfig();
                sender.sendMessage(getSafeMessage("commands.nekox.reloaded", "NekoX configuration has been reloaded!"));
                break;
            case "version":
            case "v":
                String name = getSafeMessage("plugin.name", "NekoX");
                String versionLabel = getSafeMessage("plugin.version_label", "Version");
                String versionFull = getSafeMessage("plugin.version_full", "5.2-ProMax+++");
                // 为版本消息添加前缀
                sender.sendMessage("§dNekoX §e>> §6" + name + " " + versionLabel + " §e" + versionFull);
                break;
            case "language":
            case "lang":
                handleLanguageCommand(sender, args);
                break;
            case "placeholders":
                handlePlaceholdersCommand(sender);
                break;
            default:
                sender.sendMessage(getSafeMessage("commands.unknown", "Unknown command argument! Type /nekoxhelp for help"));
                break;
        }
        
        return true;
    }
    
    /**
     * 安全获取消息，防止空指针异常
     */
    private String getSafeMessage(String key, String defaultValue) {
        if (languageManager == null) {
            return defaultValue;
        }
        String message = languageManager.getMessage(key);
        return message != null ? message : defaultValue;
    }
    
    /**
     * 处理语言切换命令
     */
    private void handleLanguageCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            // 显示当前语言和可用语言列表
            HashMap<String, String> replacements = new HashMap<>();
            replacements.put("language", languageManager != null ? languageManager.getCurrentLanguage() : "English");
            sender.sendMessage("§e" + replacePlaceholdersSafe(getSafeMessage("commands.language.current", "Current language: %language%"), replacements));
            
            replacements.clear();
            Set<String> availableLanguages = languageManager != null ? languageManager.getAvailableLanguages() : java.util.Set.of("English", "简体中文");
            replacements.put("languages", String.join(", ", availableLanguages));
            sender.sendMessage("§e" + replacePlaceholdersSafe(getSafeMessage("commands.language.available", "Available languages: %languages%"), replacements));
            
            sender.sendMessage("§e" + getSafeMessage("commands.language.usage", "Usage: /nekox language <language>"));
            return;
        }
        
        String language = args[1];
        Set<String> availableLanguages = languageManager != null ? languageManager.getAvailableLanguages() : java.util.Set.of("English", "简体中文");
        
        if (availableLanguages.contains(language) && languageManager != null) {
            languageManager.setLanguage(language);
            HashMap<String, String> replacements = new HashMap<>();
            replacements.put("language", language);
            sender.sendMessage("§a" + replacePlaceholdersSafe(getSafeMessage("commands.language.changed", "Language has been changed to %language%!"), replacements));
        } else {
            HashMap<String, String> replacements = new HashMap<>();
            replacements.put("languages", String.join(", ", availableLanguages));
            sender.sendMessage("§c" + replacePlaceholdersSafe(getSafeMessage("commands.language.invalid", "Invalid language! Available languages: %languages%"), replacements));
        }
    }
    
    /**
     * 安全替换占位符，防止空指针异常
     */
    private String replacePlaceholdersSafe(String message, Map<String, String> placeholders) {
        if (message == null || placeholders == null || placeholders.isEmpty()) {
            return message != null ? message : "";
        }
        
        String result = message;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            if (entry.getValue() != null) {
                result = result.replace("%" + entry.getKey() + "%", entry.getValue());
            }
        }
        
        return result;
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6===== " + getSafeMessage("commands.help.title", "NekoX Help") + " =====");
        sender.sendMessage("§e" + getSafeMessage("commands.help.pat", "/pat <player> - Pet a neko"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.lovebite", "/lovebite <player> - Give a love bite"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.earscratch", "/earscratch <player> - Scratch a neko's ears"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.purr", "/purr - Make a cute purring sound"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.hiss", "/hiss <player> - Hiss at a player"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.scratch", "/scratch <player> - Scratch a player"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.attention", "/attention <player> - Attract a player's attention"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.nightvision", "/nightvision [player] - Gain night vision effect"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.jumpboost", "/jumpboost [player] - Gain jump boost effect"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.swiftsneak", "/swiftsneak [player] - Gain swift sneak effect"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.health", "/health - Neko restores own and owner's health"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.climb", "/climb - Toggle climbing feature"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.pullthetail", "/pullthetail - Toggle tail pull feature"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.nekox", "/nekox - View plugin information"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.nekox_reload", "/nekox reload - Reload configuration"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.nekox_version", "/nekox version - View plugin version"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.nekoset", "/nekoset <player> <true/false> - Set player's neko status (requires admin permission)"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.nekox_language", "/nekox language <language> - Change plugin language"));
        sender.sendMessage("§e" + getSafeMessage("commands.help.nekox_placeholders", "/nekox placeholders - View placeholder list"));
    }
    
    /**
     * 处理占位符列表命令
     */
    private void handlePlaceholdersCommand(CommandSender sender) {
        // 检查PlaceholderAPI是否已安装
        boolean placeholderAPIInstalled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        
        // 直接定义占位符列表，避免依赖PlaceholderAPI类
        List<String> placeholders = Arrays.asList("is_neko", "humans", "nekos");
        int totalPlaceholders = placeholders.size();
        int registeredPlaceholders = placeholderAPIInstalled ? totalPlaceholders : 0;
        
        // 发送占位符列表信息
        sender.sendMessage("§dNekoX §e>> §6===== NekoX 占位符列表 ======");
        if (placeholderAPIInstalled) {
            sender.sendMessage("§dNekoX §e>> §ePlaceholderAPI 已安装");
        } else {
            sender.sendMessage("§dNekoX §e>> §cPlaceholderAPI 未安装");
        }
        
        sender.sendMessage("§dNekoX §e>> §e成功注册的占位符：" + registeredPlaceholders + "/" + totalPlaceholders);
        
        // 显示所有占位符
        sender.sendMessage("§dNekoX §e>> §a所有占位符：");
        for (String placeholder : placeholders) {
            String status = placeholderAPIInstalled ? "§a✓" : "§c✗";
            sender.sendMessage("§dNekoX §e>> §e- %nekox_" + placeholder + "% " + status);
        }
        
        if (!placeholderAPIInstalled) {
            sender.sendMessage("§dNekoX §e>> §c提示：安装PlaceholderAPI后可以使用这些占位符。");
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
                Set<String> availableLanguages = languageManager != null ? languageManager.getAvailableLanguages() : java.util.Set.of("English", "简体中文");
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