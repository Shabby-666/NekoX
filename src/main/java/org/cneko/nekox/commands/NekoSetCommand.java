package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.NekoManager;

public class NekoSetCommand implements CommandExecutor {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    
    public NekoSetCommand(NekoX plugin) {
        this.plugin = plugin;
        this.nekoManager = plugin.getNekoManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 检查命令发送者是否有权限
        if (!sender.hasPermission("nekox.admin")) {
            sender.sendMessage("§c你没有权限使用此命令！");
            return true;
        }
        
        // 检查参数数量
        if (args.length < 2) {
            sender.sendMessage("§c用法: /nekoset <玩家> <true/false>");
            return true;
        }
        
        // 获取目标玩家
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§c找不到玩家: " + args[0]);
            return true;
        }
        
        // 解析是否设置为猫娘
        boolean isNeko;
        try {
            isNeko = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            sender.sendMessage("§c请指定true或false作为第二个参数！");
            return true;
        }
        
        // 设置玩家的猫娘状态
        boolean changed = nekoManager.setNeko(target, isNeko);
        
        if (changed) {
            if (isNeko) {
                sender.sendMessage("§a成功将玩家 " + target.getName() + " 设置为猫娘！");
                target.sendMessage("§e你被设置为猫娘了！喵~♪");
            } else {
                sender.sendMessage("§a成功取消玩家 " + target.getName() + " 的猫娘状态！");
                target.sendMessage("§e你的猫娘状态已被取消！");
            }
        } else {
            if (isNeko) {
                sender.sendMessage("§e玩家 " + target.getName() + " 已经是猫娘了！");
            } else {
                sender.sendMessage("§e玩家 " + target.getName() + " 本来就不是猫娘！");
            }
        }
        
        return true;
    }
}