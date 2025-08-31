package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;

public class Pat implements CommandExecutor {
    private final NekoX plugin;
    
    public Pat(NekoX plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家可以使用此命令！");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            player.sendMessage("§c用法: /pat <玩家>");
            return true;
        }
        
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§c找不到玩家: " + args[0]);
            return true;
        }
        
        // 发送消息给玩家
        player.sendMessage("§e你温柔地抚摸了 §a" + target.getName() + " §e的头！");
        target.sendMessage("§a" + player.getName() + " §e温柔地抚摸了你的头！");
        
        // 可以在这里添加粒子效果或其他视觉反馈
        
        return true;
    }
}