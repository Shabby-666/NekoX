package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.NekoManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HealthCommand implements CommandExecutor {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    
    public HealthCommand(NekoX plugin) {
        this.plugin = plugin;
        this.nekoManager = plugin.getNekoManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家可以使用此命令！");
            return true;
        }
        
        Player neko = (Player) sender;
        
        // 检查玩家是否是猫娘
        if (!nekoManager.isNeko(neko)) {
            neko.sendMessage("§c只有猫娘可以使用此命令！喵~");
            return true;
        }
        
        // 检查冷却时间
        int cooldownTime = plugin.getConfig().getInt("health-skill.cooldown", 60);
        UUID nekoUUID = neko.getUniqueId();
        
        if (cooldowns.containsKey(nekoUUID)) {
            long lastUsed = cooldowns.get(nekoUUID);
            long currentTime = System.currentTimeMillis();
            long elapsedTime = (currentTime - lastUsed) / 1000; // 转换为秒
            
            if (elapsedTime < cooldownTime) {
                long remaining = cooldownTime - elapsedTime;
                neko.sendMessage("§c技能正在冷却中，还需等待 " + remaining + " 秒！喵~");
                return true;
            }
        }
        
        // 检查饱食度是否足够
        int hungerCost = plugin.getConfig().getInt("health-skill.hunger-cost", 4);
        if (neko.getFoodLevel() < hungerCost) {
            neko.sendMessage("§c饱食度不足！需要 " + hungerCost + " 点饱食度才能使用此技能。喵~");
            return true;
        }
        
        // 获取猫娘的主人
        java.util.Set<Player> owners = nekoManager.getOwners(neko);
        
        if (owners.isEmpty()) {
            neko.sendMessage("§c你还没有主人，无法使用此技能！喵~");
            return true;
        }
        
        // 扣除饱食度
        neko.setFoodLevel(neko.getFoodLevel() - hungerCost);
        
        // 应用恢复效果
        applyHealthRestore(neko, owners);
        
        // 设置冷却时间
        cooldowns.put(nekoUUID, System.currentTimeMillis());
        
        return true;
    }
    
    private void applyHealthRestore(Player neko, java.util.Set<Player> owners) {
        int maxLevel = plugin.getConfig().getInt("health-skill.max-level", 5);
        
        // 计算猫娘的恢复等级
        double nekoHealth = neko.getHealth();
        double nekoMaxHealth = neko.getMaxHealth();
        int nekoLevel = calculateRestoreLevel(nekoHealth, nekoMaxHealth, maxLevel);
        
        // 给猫娘添加恢复效果
        neko.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, nekoLevel, false, false));
        neko.sendMessage("§a你使用了健康恢复技能！获得了 " + (nekoLevel + 1) + " 级的生命恢复效果。喵~");
        
        // 给每个主人添加恢复效果
        for (Player owner : owners) {
            double ownerHealth = owner.getHealth();
            double ownerMaxHealth = owner.getMaxHealth();
            int ownerLevel = calculateRestoreLevel(ownerHealth, ownerMaxHealth, maxLevel);
            
            owner.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, ownerLevel, false, false));
            owner.sendMessage("§a你的猫娘 §e" + neko.getName() + " §a为你施加了生命恢复效果！获得了 " + (ownerLevel + 1) + " 级的生命恢复效果。");
        }
    }
    
    private int calculateRestoreLevel(double currentHealth, double maxHealth, int maxLevel) {
        // 生命值越低，恢复等级越高
        double healthPercentage = currentHealth / maxHealth;
        int level;
        
        if (healthPercentage <= 0.2) {
            level = maxLevel;
        } else if (healthPercentage <= 0.4) {
            level = maxLevel - 1;
        } else if (healthPercentage <= 0.6) {
            level = maxLevel - 2;
        } else if (healthPercentage <= 0.8) {
            level = maxLevel - 3;
        } else {
            level = maxLevel - 4;
        }
        
        // 确保等级不小于0
        return Math.max(level, 0);
    }
}