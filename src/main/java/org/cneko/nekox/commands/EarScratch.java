package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.NekoManager;

public class EarScratch implements CommandExecutor {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    
    public EarScratch(NekoX plugin) {
        this.plugin = plugin;
        this.nekoManager = plugin.getNekoManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家可以使用此命令！");
            return true;
        }
        
        Player player = (Player) sender;
        
        // 检查玩家是否是猫娘
        if (!nekoManager.isNeko(player)) {
            player.sendMessage("§c只有猫娘可以使用此命令！喵~");
            return true;
        }
        
        if (args.length == 0) {
            player.sendMessage("§c用法: /earscratch <玩家>");
            return true;
        }
        
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§c找不到玩家: " + args[0]);
            return true;
        }
        
        // 发送消息给玩家
        player.sendMessage("§e你轻轻地挠了 §a" + target.getName() + " §e的耳朵！");
        target.sendMessage("§a" + player.getName() + " §e轻轻地挠了你的耳朵！");
        
        return true;
    }
}