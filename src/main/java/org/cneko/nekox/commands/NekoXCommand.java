package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.cneko.nekox.NekoX;

public class NekoXCommand implements CommandExecutor {
    private final NekoX plugin;
    
    public NekoXCommand(NekoX plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§6===== NekoX =====");
            sender.sendMessage("§e版本: 1.0.0");
            sender.sendMessage("§e作者: CNeko");
            sender.sendMessage("§e输入 /nekox help 查看帮助");
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
                    sender.sendMessage("§c你没有权限执行此命令！");
                    return true;
                }
                plugin.reloadConfig();
                sender.sendMessage("§eNekoX配置已重新加载！");
                break;
            case "version":
            case "v":
                sender.sendMessage("§6NekoX 版本 §e1.0.0");
                break;
            default:
                sender.sendMessage("§c未知命令参数！输入 /nekox help 查看帮助");
                break;
        }
        
        return true;
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6===== NekoX 帮助 =====");
        sender.sendMessage("§e/pat <玩家> - 抚摸猫娘");
        sender.sendMessage("§e/lovebite <玩家> - 给猫娘一个爱的咬痕");
        sender.sendMessage("§e/earscratch <玩家> - 挠猫娘的耳朵");
        sender.sendMessage("§e/purr - 让猫娘发出呼噜声");
        sender.sendMessage("§e/hiss <玩家> - 让猫娘发出嘶嘶声");
        sender.sendMessage("§e/scratch <玩家> - 让猫娘挠人");
        sender.sendMessage("§e/attention <玩家> - 吸引猫娘的注意");
        sender.sendMessage("§e/nightvision [玩家] - 获得夜视效果");
        sender.sendMessage("§e/jumpboost [玩家] - 获得跳跃提升效果");
        sender.sendMessage("§e/swiftsneak [玩家] - 获得迅捷潜行效果");
        sender.sendMessage("§e/nekox - 查看插件信息");
        sender.sendMessage("§e/nekox reload - 重新加载配置");
        sender.sendMessage("§e/nekox version - 查看插件版本");
        sender.sendMessage("§e/nekoset <玩家> <true/false> - 设置玩家的猫娘状态(需要管理员权限)");
    }
}