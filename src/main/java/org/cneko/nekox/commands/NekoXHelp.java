package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NekoXHelp implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 发送帮助信息
        sender.sendMessage("§6===== NekoX 猫娘插件帮助 =====");
        sender.sendMessage("§e欢迎使用NekoX猫娘插件！以下是所有可用命令：");
        
        // 猫娘互动命令
        sender.sendMessage("§a===== 互动命令 =====");
        sender.sendMessage("§e/pat <玩家> - 温柔地抚摸另一个玩家");
        sender.sendMessage("§e/lovebite <玩家> - 给另一个玩家一个爱的咬痕");
        sender.sendMessage("§e/earscratch <玩家> - 轻轻地挠另一个玩家的耳朵");
        sender.sendMessage("§e/purr - 发出舒服的呼噜声");
        sender.sendMessage("§e/hiss <玩家> - 对着另一个玩家发出嘶嘶声");
        sender.sendMessage("§e/scratch <玩家> - 用爪子挠另一个玩家");
        sender.sendMessage("§e/attention <玩家> - 吸引另一个玩家的注意");
        
        // 效果命令
        sender.sendMessage("§a===== 效果命令 =====");
        sender.sendMessage("§e/nightvision [玩家] - 获得夜视效果(默认自己)");
        sender.sendMessage("§e/jumpboost [玩家] - 获得跳跃提升效果(默认自己)");
        sender.sendMessage("§e/swiftsneak [玩家] - 获得迅捷潜行效果(默认自己)");
        
        // 管理命令
        sender.sendMessage("§a===== 管理命令 =====");
        sender.sendMessage("§e/nekox - 查看插件信息");
        sender.sendMessage("§e/nekox reload - 重新加载插件配置(需要管理员权限)");
        sender.sendMessage("§e/nekox version - 查看插件版本");
        sender.sendMessage("§e/nekoset <玩家> <true/false> - 设置玩家的猫娘状态(需要管理员权限)");
        
        // 提示信息
        if (sender instanceof Player) {
            sender.sendMessage("§6喵~希望你喜欢这个插件！♪");
        }
        
        return true;
    }
}