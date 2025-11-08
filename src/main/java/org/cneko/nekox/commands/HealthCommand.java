package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.NekoManager;
import org.cneko.nekox.utils.SkillManager;
import org.cneko.nekox.utils.LanguageManager;
import java.util.HashMap;

public class HealthCommand implements CommandExecutor {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    private final SkillManager skillManager;
    private final LanguageManager languageManager;
    
    public HealthCommand(NekoX plugin) {
        this.plugin = plugin;
        this.nekoManager = plugin.getNekoManager();
        this.skillManager = plugin.getSkillManager();
        this.languageManager = plugin.getLanguageManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c" + languageManager.getMessage("commands.health.notneko"));
            return true;
        }
        
        Player neko = (Player) sender;
        
        // 检查玩家是否是猫娘
        if (!nekoManager.isNeko(neko)) {
            neko.sendMessage("§c" + languageManager.getMessage("commands.health.notneko"));
            return true;
        }
        
        // 检查冷却时间
        if (skillManager.isSkillOnCooldown(neko, SkillManager.SkillType.HEALTH_RESTORE)) {
            long remaining = skillManager.getRemainingCooldown(neko, SkillManager.SkillType.HEALTH_RESTORE);
            HashMap<String, String> replacements = new HashMap<>();
            replacements.put("time", String.valueOf(remaining));
            neko.sendMessage("§c" + languageManager.replacePlaceholders(languageManager.getMessage("commands.health.cooldown"), replacements));
            return true;
        }
        
        // 检查饱食度是否足够
        int hungerCost = plugin.getConfig().getInt("health-skill.hunger-cost", 4);
        if (neko.getFoodLevel() < hungerCost) {
            HashMap<String, String> replacements = new HashMap<>();
            replacements.put("cost", String.valueOf(hungerCost));
            neko.sendMessage("§c" + languageManager.replacePlaceholders(languageManager.getMessage("commands.health.lowhunger"), replacements));
            return true;
        }
        
        // 获取猫娘的主人
        java.util.Set<Player> owners = nekoManager.getOwners(neko);
        
        if (owners.isEmpty()) {
            neko.sendMessage("§c" + languageManager.getMessage("commands.health.noowner"));
            return true;
        }
        
        // 扣除饱食度
        neko.setFoodLevel(neko.getFoodLevel() - hungerCost);
        
        // 应用恢复效果
        applyHealthRestore(neko, owners);
        
        // 设置冷却时间
        skillManager.setCooldown(neko, SkillManager.SkillType.HEALTH_RESTORE);
        
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
        HashMap<String, String> nekoReplacements = new HashMap<>();
        nekoReplacements.put("level", String.valueOf(nekoLevel + 1));
        neko.sendMessage("§a" + languageManager.replacePlaceholders(languageManager.getMessage("commands.health.success"), nekoReplacements));
        
        // 给每个主人添加恢复效果
        for (Player owner : owners) {
            double ownerHealth = owner.getHealth();
            double ownerMaxHealth = owner.getMaxHealth();
            int ownerLevel = calculateRestoreLevel(ownerHealth, ownerMaxHealth, maxLevel);
            
            owner.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, ownerLevel, false, false));
            HashMap<String, String> ownerReplacements = new HashMap<>();
            ownerReplacements.put("neko", neko.getName());
            ownerReplacements.put("level", String.valueOf(ownerLevel + 1));
            owner.sendMessage("§a" + languageManager.replacePlaceholders(languageManager.getMessage("commands.health.owner_success"), ownerReplacements));
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