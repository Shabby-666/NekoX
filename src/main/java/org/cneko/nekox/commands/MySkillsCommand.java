package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.NekoManager;
import org.cneko.nekox.utils.SkillManager;
import org.cneko.nekox.utils.LanguageManager;
import java.util.HashMap;

public class MySkillsCommand implements CommandExecutor {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    private final SkillManager skillManager;
    private final LanguageManager languageManager;
    
    public MySkillsCommand(NekoX plugin) {
        this.plugin = plugin;
        this.nekoManager = plugin.getNekoManager();
        this.skillManager = plugin.getSkillManager();
        this.languageManager = plugin.getLanguageManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c" + languageManager.getMessage("commands.only_player"));
            return true;
        }
        
        Player player = (Player) sender;
        
        // 检查玩家是否是猫娘
        if (!nekoManager.isNeko(player)) {
            player.sendMessage("§c" + languageManager.getMessage("commands.health.notneko"));
            return true;
        }
        
        // 显示技能列表
        showSkillList(player);
        
        return true;
    }
    
    /**
     * 显示技能列表
     */
    private void showSkillList(Player player) {
        player.sendMessage("§6===== " + languageManager.getMessage("commands.myskills.title") + " §6=====");
        
        // 显示主动技能
        player.sendMessage("§a【主动技能】");
        showHealthRestoreSkill(player);
        
        // 显示被动技能
        player.sendMessage("§a【被动技能】");
        showStressEffectSkill(player);
        showAttackBoostSkill(player);
        
        // 如果还有其他被动技能，可以在这里添加
        player.sendMessage("§6=====================");
    }
    
    /**
     * 显示生命恢复技能信息
     */
    private void showHealthRestoreSkill(Player player) {
        boolean onCooldown = skillManager.isSkillOnCooldown(player, SkillManager.SkillType.HEALTH_RESTORE);
        long remainingCooldown = skillManager.getRemainingCooldown(player, SkillManager.SkillType.HEALTH_RESTORE);
        
        player.sendMessage("§e- /health §f- " + languageManager.getMessage("commands.health.name"));
        player.sendMessage("  §f" + languageManager.getMessage("commands.health.description"));
        
        HashMap<String, String> replacements = new HashMap<>();
        if (onCooldown) {
            replacements.put("time", String.valueOf(remainingCooldown));
            player.sendMessage("  §f" + languageManager.getMessage("commands.myskills.cooldown-label") + "§c" + languageManager.replacePlaceholders(languageManager.getMessage("commands.myskills.cooldown"), replacements));
        } else {
            player.sendMessage("  §f" + languageManager.getMessage("commands.myskills.cooldown-label") + "§a" + languageManager.getMessage("commands.myskills.available"));
        }
        
        replacements.clear();
        replacements.put("cost", String.valueOf(plugin.getConfig().getInt("health-skill.hunger-cost", 4)));
        player.sendMessage("  §f" + languageManager.replacePlaceholders(languageManager.getMessage("commands.health.cost"), replacements));
    }
    
    /**
     * 显示应激效果技能信息
     */
    private void showStressEffectSkill(Player player) {
        boolean onCooldown = skillManager.isSkillOnCooldown(player, SkillManager.SkillType.STRESS_PASSIVE);
        long remainingCooldown = skillManager.getRemainingCooldown(player, SkillManager.SkillType.STRESS_PASSIVE);
        boolean isActive = skillManager.isStressEffectActive(player);
        
        player.sendMessage("§e- " + languageManager.getMessage("commands.stress.name") + " §f- " + languageManager.getMessage("commands.stress.type"));
        player.sendMessage("  §f" + languageManager.getMessage("commands.stress.description"));
        player.sendMessage("  §f" + languageManager.getMessage("commands.stress.status-label") + (isActive ? "§c" + languageManager.getMessage("commands.stress.active") : "§a" + languageManager.getMessage("commands.stress.inactive")));
        
        HashMap<String, String> replacements = new HashMap<>();
        if (onCooldown) {
            replacements.put("time", String.valueOf(remainingCooldown));
            player.sendMessage("  §f" + languageManager.getMessage("commands.myskills.cooldown-label") + "§c" + languageManager.replacePlaceholders(languageManager.getMessage("commands.myskills.cooldown"), replacements));
        } else {
            player.sendMessage("  §f" + languageManager.getMessage("commands.myskills.cooldown-label") + "§a" + languageManager.getMessage("commands.myskills.available"));
        }
    }
    
    /**
     * 显示被动攻击增强技能信息
     */
    private void showAttackBoostSkill(Player player) {
        player.sendMessage("§e- " + languageManager.getMessage("commands.attackboost.name") + " §f- " + languageManager.getMessage("commands.attackboost.type"));
        player.sendMessage("  §f" + languageManager.getMessage("commands.attackboost.description"));
        player.sendMessage("  §f" + languageManager.getMessage("commands.stress.status-label") + "§a" + languageManager.getMessage("commands.stress.active"));
        player.sendMessage("  §f" + languageManager.getMessage("commands.myskills.cooldown-label") + "§a" + languageManager.getMessage("commands.myskills.available"));
    }
}