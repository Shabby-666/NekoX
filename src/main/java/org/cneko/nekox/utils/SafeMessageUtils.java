package org.cneko.nekox.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 安全消息工具类，防止空指针异常
 */
public class SafeMessageUtils {
    
    // 基础默认消息映射
    private static final Map<String, String> DEFAULT_MESSAGES = new HashMap<>();
    
    static {
        // 基础命令消息
        DEFAULT_MESSAGES.put("commands.only_player", "Only players can use this command!");
        DEFAULT_MESSAGES.put("commands.health.notneko", "You are not a neko!");
        DEFAULT_MESSAGES.put("commands.player_not_found", "Player not found!");
        DEFAULT_MESSAGES.put("commands.no_permission", "You don't have permission to execute this command!");
        
        // 成功消息
        DEFAULT_MESSAGES.put("commands.pat.success", "You patted {player}!");
        DEFAULT_MESSAGES.put("commands.lovebite.success", "You gave {player} a love bite!");
        DEFAULT_MESSAGES.put("commands.earscratch.success", "You scratched {player}'s ears!");
        DEFAULT_MESSAGES.put("commands.purr.success", "You made a cute purring sound!");
        DEFAULT_MESSAGES.put("commands.hiss.success", "You hissed at {player}!");
        DEFAULT_MESSAGES.put("commands.scratch.success", "You scratched {player}!");
        DEFAULT_MESSAGES.put("commands.attention.success", "You attracted {player}'s attention!");
        DEFAULT_MESSAGES.put("commands.nightvision.success", "You gained night vision!");
        DEFAULT_MESSAGES.put("commands.jumpboost.success", "You gained jump boost effect!");
        DEFAULT_MESSAGES.put("commands.swiftsneak.success", "You gained swift sneak effect!");
        
        // 帮助消息
        DEFAULT_MESSAGES.put("commands.help.owner", "Usage: /owner <add|accept|deny|remove|list|mylist|forceadd> [player]");
        DEFAULT_MESSAGES.put("commands.help.owner_add", "Usage: /owner add <player>");
        DEFAULT_MESSAGES.put("commands.help.owner_accept", "Usage: /owner accept <player>");
        DEFAULT_MESSAGES.put("commands.help.owner_deny", "Usage: /owner deny <player>");
        DEFAULT_MESSAGES.put("commands.help.owner_remove", "Usage: /owner remove <player>");
        DEFAULT_MESSAGES.put("commands.help.owner_forceadd", "Usage: /owner forceadd <player>");
        
        // 主人命令相关消息
        DEFAULT_MESSAGES.put("commands.owner.cant_add_self", "You cannot add yourself as owner!");
        DEFAULT_MESSAGES.put("commands.owner.target_not_neko", "Target player is not a neko!");
        DEFAULT_MESSAGES.put("commands.owner.already_owner", "You are already the owner of this neko!");
        DEFAULT_MESSAGES.put("commands.owner.request_already_sent", "Owner request already sent!");
        DEFAULT_MESSAGES.put("commands.owner.request_sent", "Owner request sent successfully!");
        DEFAULT_MESSAGES.put("commands.owner.request_received", "{player} wants to be your owner!");
        DEFAULT_MESSAGES.put("commands.owner.request_help", "Use /owner accept {player} or /owner deny {player}");
        DEFAULT_MESSAGES.put("commands.owner.not_neko", "You are not a neko!");
        DEFAULT_MESSAGES.put("commands.owner.no_pending_request", "No pending owner request found!");
        DEFAULT_MESSAGES.put("commands.owner.accepted_target", "You accepted {player}'s owner request!");
        DEFAULT_MESSAGES.put("commands.owner.accepted_by_target", "{player} accepted your owner request!");
        DEFAULT_MESSAGES.put("commands.owner.denied_target", "You denied {player}'s owner request!");
        DEFAULT_MESSAGES.put("commands.owner.denied_by_target", "{player} denied your owner request!");
        DEFAULT_MESSAGES.put("commands.owner.remove_owner", "You removed {player} as your owner!");
        DEFAULT_MESSAGES.put("commands.owner.removed_as_owner", "{player} removed you as owner!");
        DEFAULT_MESSAGES.put("commands.owner.remove_master", "You removed {player} from your neko list!");
        DEFAULT_MESSAGES.put("commands.owner.removed_as_master", "{player} removed you from their neko list!");
        DEFAULT_MESSAGES.put("commands.owner.no_owner_relation", "No owner relationship found!");
        DEFAULT_MESSAGES.put("commands.owner.no_owners", "You have no owners!");
        DEFAULT_MESSAGES.put("commands.owner.admin_tip", "Tip: Use /owner forceadd <player> to add owners as admin");
        DEFAULT_MESSAGES.put("commands.owner.owners_list", "Your owners:");
        DEFAULT_MESSAGES.put("commands.owner.no_nekos", "You have no nekos!");
        DEFAULT_MESSAGES.put("commands.owner.nekos_list", "Your nekos:");
        DEFAULT_MESSAGES.put("commands.owner.target_not_neko_admin", "{player} is not a neko!");
        DEFAULT_MESSAGES.put("commands.owner.force_add_success", "Successfully added {player} as owner!");
        DEFAULT_MESSAGES.put("commands.owner.force_add_target", "{player} added you as their owner!");
    }
    
    /**
     * 安全获取消息，防止空指针异常
     * @param languageManager 语言管理器
     * @param key 消息键
     * @param defaultValue 默认值
     * @return 消息内容
     */
    public static String getSafeMessage(LanguageManager languageManager, String key, String defaultValue) {
        if (languageManager == null) {
            return DEFAULT_MESSAGES.getOrDefault(key, defaultValue);
        }
        try {
            String message = languageManager.getMessage(key);
            return message != null ? message : DEFAULT_MESSAGES.getOrDefault(key, defaultValue);
        } catch (Exception e) {
            // 捕获任何异常，返回默认值
            return DEFAULT_MESSAGES.getOrDefault(key, defaultValue);
        }
    }
    
    /**
     * 安全获取消息，使用键名作为默认值
     * @param languageManager 语言管理器
     * @param key 消息键
     * @return 消息内容
     */
    public static String getSafeMessage(LanguageManager languageManager, String key) {
        return getSafeMessage(languageManager, key, DEFAULT_MESSAGES.getOrDefault(key, key));
    }
    
    /**
     * 安全替换占位符
     * @param message 原始消息
     * @param replacements 替换映射
     * @return 替换后的消息
     */
    public static String replacePlaceholdersSafe(String message, Map<String, String> replacements) {
        if (message == null || replacements == null || replacements.isEmpty()) {
            return message != null ? message : "";
        }
        
        String result = message;
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            if (entry.getValue() != null) {
                result = result.replace("{" + entry.getKey() + "}", entry.getValue());
                result = result.replace("%" + entry.getKey() + "%", entry.getValue());
            }
        }
        
        return result;
    }
}