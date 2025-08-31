package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.NekoManager;

public class Nightvision implements CommandExecutor {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    
    public Nightvision(NekoX plugin) {
        this.plugin = plugin;
        this.nekoManager = plugin.getNekoManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§c控制台需要指定玩家！");
                return true;
            }
            
            Player player = (Player) sender;
            
            // 检查玩家是否是猫娘
            if (!nekoManager.isNeko(player)) {
                player.sendMessage("§c只有猫娘可以使用此命令！喵~");
                return true;
            }
            
            applyNightvision(player);
            return true;
        }
        
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§c找不到玩家: " + args[0]);
            return true;
        }
        
        // 检查目标玩家是否是猫娘
        if (!nekoManager.isNeko(target)) {
            sender.sendMessage("§c只有猫娘可以使用此命令！喵~");
            return true;
        }
        
        applyNightvision(target);
        if (sender instanceof Player && sender != target) {
            sender.sendMessage("§e你给 §a" + target.getName() + " §e施加了夜视效果！");
        }
        
        return true;
    }
    
    private void applyNightvision(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 6000, 0, false, false));
        player.sendMessage("§e你获得了夜视效果！");
    }
}