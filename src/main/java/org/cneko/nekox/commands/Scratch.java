package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;

public class Scratch implements CommandExecutor {
    private final NekoX plugin;
    
    public Scratch(NekoX plugin) {
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
            player.sendMessage("§c用法: /scratch <玩家>");
            return true;
        }
        
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§c找不到玩家: " + args[0]);
            return true;
        }
        
        // 发送消息给玩家
        player.sendMessage("§e你用爪子挠了 §a" + target.getName() + " §e！");
        target.sendMessage("§a" + player.getName() + " §e用爪子挠了你！");
        
        // 造成少量伤害
        target.damage(1.0);
        
        return true;
    }
}