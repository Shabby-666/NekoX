package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.NekoManager;

public class Purr implements CommandExecutor {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    
    public Purr(NekoX plugin) {
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
        
        // 发送消息给玩家和周围的人
        player.sendMessage("§e你发出了舒服的呼噜声！");
        player.getWorld().playSound(player.getLocation(), "entity.cat.purr", 1.0f, 1.0f);
        
        // 通知周围的玩家
        for (Player nearby : player.getWorld().getPlayers()) {
            if (nearby != player && nearby.getLocation().distance(player.getLocation()) <= 10) {
                nearby.sendMessage("§a" + player.getName() + " §e发出了舒服的呼噜声！");
            }
        }
        
        return true;
    }
}