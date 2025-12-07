package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.SafeMessageUtils;

public class NekoXHelp implements CommandExecutor {
    private final LanguageManager languageManager;
    
    public NekoXHelp(NekoX plugin) {
        this.languageManager = plugin.getLanguageManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 检查LanguageManager是否已正确初始化
        if (languageManager == null) {
            sender.sendMessage("§cNekoX插件语言系统未正确初始化，请联系管理员检查插件配置。");
            return true;
        }
        
        // 发送帮助信息
        sender.sendMessage("§6===== " + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.title", "NekoX Help") + " =====");
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.welcome", "Welcome to NekoX!"));
        
        // 猫娘互动命令
        sender.sendMessage("§a===== " + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.interaction", "Interaction Commands") + " =====");
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.pat", "/pat <player> - Pet a neko"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.lovebite", "/lovebite <player> - Give a love bite"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.earscratch", "/earscratch <player> - Scratch a neko's ears"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.purr", "/purr - Make a cute purring sound"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.hiss", "/hiss <player> - Hiss at a player"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.scratch", "/scratch <player> - Scratch a player"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.attention", "/attention <player> - Attract a player's attention"));
        
        // 效果命令
        sender.sendMessage("§a===== " + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.effects", "Effect Commands") + " ======");
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.nightvision", "/nightvision [player] - Gain night vision effect"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.jumpboost", "/jumpboost [player] - Gain jump boost effect"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.swiftsneak", "/swiftsneak [player] - Gain swift sneak effect"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.health", "/health - Neko restores own and owner's health"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.myskills", "/myskills - View your neko skills"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.playernotice", "/playernotice - Toggle player proximity notices"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.climb", "/climb - Toggle climbing feature"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.pullthetail", "/pullthetail - Toggle tail pull feature"));
        sender.sendMessage("§e/climb - " + SafeMessageUtils.getSafeMessage(languageManager, "commands.climb.enabled", "Climbing enabled").replace("!", ""));
        
        // 主人系统命令
        sender.sendMessage("§a===== " + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner_system", "Owner System Commands") + " =====");
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner", "/owner - View owner information"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner_add", "/owner add <player> - Add an owner"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner_accept", "/owner accept - Accept owner request"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner_deny", "/owner deny - Deny owner request"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner_remove", "/owner remove <player> - Remove an owner"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner_list", "/owner list - List all owners"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner_mylist", "/owner mylist - List your owners"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner_forceadd", "/owner forceadd <player> - Force add an owner (admin)"));
        
        // 管理命令
        sender.sendMessage("§a===== " + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.management", "Management Commands") + " =====");
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.nekox", "/nekox - View plugin information"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.nekox_reload", "/nekox reload - Reload configuration"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.nekox_version", "/nekox version - View plugin version"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.nekoset", "/nekoset <player> <true/false> - Set player's neko status"));
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.nekox_language", "/nekox language <language> - Change plugin language"));
        
        sender.sendMessage("§e" + SafeMessageUtils.getSafeMessage(languageManager, "commands.help.ending", "For more details, check the plugin documentation!"));
        
        return true;
    }
}