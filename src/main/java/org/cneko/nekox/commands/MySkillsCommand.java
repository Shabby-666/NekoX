package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.NekoManager;
import org.cneko.nekox.utils.SkillManager;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.SafeMessageUtils;
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
            sender.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.only_player", "Only players can use this command!"));
            return true;
        }
        
        Player player = (Player) sender;
        
        // 检查玩家是否是猫娘
        if (!nekoManager.isNeko(player)) {
            player.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.health.notneko", "You are not a neko!"));
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
        player.sendMessage("§6===== " + SafeMessageUtils.getSafeMessage(languageManager, "commands.myskills.title", "My Skills") + " §6=====");
        
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
        
        player.sendMessage("§e- /health §f- " + SafeMessageUtils.getSafeMessage(languageManager, "commands.health.name", "Health Restore"));
        player.sendMessage("  §f" + SafeMessageUtils.getSafeMessage(languageManager, "commands.health.description", "Restore health for you and your owner"));
        
        HashMap<String, String> replacements = new HashMap<>();
        if (onCooldown) {
            replacements.put("time", String.valueOf(remainingCooldown));
            player.sendMessage("  §f" + SafeMessageUtils.getSafeMessage(languageManager, "commands.myskills.cooldown-label", "Cooldown: ") + "§c" + SafeMessageUtils.replacePlaceholdersSafe(SafeMessageUtils.getSafeMessage(languageManager, "commands.myskills.cooldown", "{time} seconds remaining"), replacements));
        } else {
            player.sendMessage("  §f" + SafeMessageUtils.getSafeMessage(languageManager, "commands.myskills.cooldown-label", "Cooldown: ") + "§a" + SafeMessageUtils.getSafeMessage(languageManager, "commands.myskills.available", "Available"));
        }
        
        replacements.clear();
        replacements.put("cost", String.valueOf(plugin.getConfig().getInt("health-skill.hunger-cost", 4)));
        player.sendMessage("  §f" + SafeMessageUtils.replacePlaceholdersSafe(SafeMessageUtils.getSafeMessage(languageManager, "commands.health.cost", "Hunger Cost: {cost}"), replacements));
    }
    
    /**
     * 显示应激效果技能信息
     */
    private void showStressEffectSkill(Player player) {
        boolean onCooldown = skillManager.isSkillOnCooldown(player, SkillManager.SkillType.STRESS_PASSIVE);
        long remainingCooldown = skillManager.getRemainingCooldown(player, SkillManager.SkillType.STRESS_PASSIVE);
        boolean isActive = skillManager.isStressEffectActive(player);
        
        player.sendMessage("§e- " + SafeMessageUtils.getSafeMessage(languageManager, "commands.stress.name", "Stress Effect") + " §f- " + SafeMessageUtils.getSafeMessage(languageManager, "commands.stress.type", "Passive"));
        player.sendMessage("  §f" + SafeMessageUtils.getSafeMessage(languageManager, "commands.stress.description", "Get stress effect when attacked"));
        player.sendMessage("  §f" + SafeMessageUtils.getSafeMessage(languageManager, "commands.stress.status-label", "Status: ") + (isActive ? "§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.stress.active", "Active") : "§a" + SafeMessageUtils.getSafeMessage(languageManager, "commands.stress.inactive", "Inactive")));
        
        HashMap<String, String> replacements = new HashMap<>();
        if (onCooldown) {
            replacements.put("time", String.valueOf(remainingCooldown));
            player.sendMessage("  §f" + SafeMessageUtils.getSafeMessage(languageManager, "commands.myskills.cooldown-label", "Cooldown: ") + "§c" + SafeMessageUtils.replacePlaceholdersSafe(SafeMessageUtils.getSafeMessage(languageManager, "commands.myskills.cooldown", "{time} seconds remaining"), replacements));
        } else {
            player.sendMessage("  §f" + SafeMessageUtils.getSafeMessage(languageManager, "commands.myskills.cooldown-label", "Cooldown: ") + "§a" + SafeMessageUtils.getSafeMessage(languageManager, "commands.myskills.available", "Available"));
        }
    }
    
    /**
     * 显示被动攻击增强技能信息
     */
    private void showAttackBoostSkill(Player player) {
        player.sendMessage("§e- " + SafeMessageUtils.getSafeMessage(languageManager, "commands.attackboost.name", "Attack Boost") + " §f- " + SafeMessageUtils.getSafeMessage(languageManager, "commands.attackboost.type", "Passive"));
        player.sendMessage("  §f" + SafeMessageUtils.getSafeMessage(languageManager, "commands.attackboost.description", "Increase attack damage"));
        player.sendMessage("  §f" + SafeMessageUtils.getSafeMessage(languageManager, "commands.stress.status-label", "Status: ") + "§a" + SafeMessageUtils.getSafeMessage(languageManager, "commands.stress.active", "Active"));
        player.sendMessage("  §f" + SafeMessageUtils.getSafeMessage(languageManager, "commands.myskills.cooldown-label", "Cooldown: ") + "§a" + SafeMessageUtils.getSafeMessage(languageManager, "commands.myskills.available", "Available"));
    }
}