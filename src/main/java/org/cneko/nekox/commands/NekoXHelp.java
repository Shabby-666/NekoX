package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.LanguageManager;

public class NekoXHelp implements CommandExecutor {
    private final LanguageManager languageManager;
    
    public NekoXHelp(NekoX plugin) {
        this.languageManager = plugin.getLanguageManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 发送帮助信息
        sender.sendMessage("§6===== " + languageManager.getMessage("commands.help.title") + " =====");
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.welcome"));
        
        // 猫娘互动命令
        sender.sendMessage("§a===== " + languageManager.getMessage("commands.help.interaction") + " =====");
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.pat"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.lovebite"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.earscratch"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.purr"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.hiss"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.scratch"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.attention"));
        
        // 效果命令
        sender.sendMessage("§a===== " + languageManager.getMessage("commands.help.effects") + " =====");
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.nightvision"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.jumpboost"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.swiftsneak"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.health"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.myskills"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.playernotice"));
        
        // 管理命令
        sender.sendMessage("§a===== " + languageManager.getMessage("commands.help.management") + " =====");
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.nekox"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.nekox_reload"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.nekox_version"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.nekoset"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.nekox_language"));
        
        // 主人系统命令
        sender.sendMessage("§a===== " + languageManager.getMessage("commands.help.owner_system") + " =====");
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.owner_add"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.owner_accept"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.owner_deny"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.owner_forceadd"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.owner_remove"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.owner_list"));
        sender.sendMessage("§e" + languageManager.getMessage("commands.help.owner_mylist"));
        
        // 提示信息
        if (sender instanceof Player) {
            sender.sendMessage("§6" + languageManager.getMessage("commands.help.ending"));
        }
        
        return true;
    }
}