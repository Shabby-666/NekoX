package org.cneko.nekox.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.NekoManager;
import org.cneko.nekox.utils.SafeMessageUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class OwnerCommand implements CommandExecutor, TabCompleter {
    private final NekoManager nekoManager;
    private final LanguageManager languageManager;
    private final NekoX plugin;

    public OwnerCommand(NekoX plugin) {
        this.plugin = plugin;
        this.nekoManager = plugin.getNekoManager();
        this.languageManager = plugin.getLanguageManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.only_player", "Only players can use this command!"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner", "Usage: /owner <add|accept|deny|remove|list|mylist|forceadd> [player]"));
            return true;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "add":
                handleAddOwner(player, args);
                break;
            case "accept":
                handleAcceptOwner(player, args);
                break;
            case "deny":
                handleDenyOwner(player, args);
                break;
            case "remove":
                handleRemoveOwner(player, args);
                break;
            case "list":
                handleListOwners(player);
                break;
            case "mylist":
                handleMyListOwners(player);
                break;
            case "forceadd":
                if (!player.hasPermission("nekox.admin")) {
                    player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.no_permission", "You don't have permission to execute this command!"));
                    return true;
                }
                handleForceAddOwner(player, args);
                break;
            default:
                player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner", "Usage: /owner <add|accept|deny|remove|list|mylist|forceadd> [player]"));
                break;
        }

        return true;
    }

    private void handleAddOwner(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner_add", "Usage: /owner add <player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.player_not_found", "Player not found!"));
            return;
        }

        if (target == player) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.cant_add_self", "You cannot add yourself as owner!"));
            return;
        }

        if (!nekoManager.isNeko(target)) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.target_not_neko", "Target player is not a neko!"));
            return;
        }

        if (nekoManager.isOwner(player, target)) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.already_owner", "You are already the owner of this neko!"));
            return;
        }

        if (nekoManager.hasOwnerRequest(player, target)) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.request_already_sent", "Owner request already sent!"));
            return;
        }

        try {
            nekoManager.sendOwnerRequest(player, target);
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.request_sent", "Owner request sent successfully!"));
            HashMap<String, String> replacements = new HashMap<>();
            replacements.put("player", player.getName());
            target.sendMessage(SafeMessageUtils.replacePlaceholdersSafe(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.request_received", "{player} wants to be your owner!"), replacements));
            target.sendMessage(SafeMessageUtils.replacePlaceholdersSafe(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.request_help", "Use /owner accept {player} or /owner deny {player}"), replacements));
        } catch (Exception e) {
            player.sendMessage("§c发送主人请求时发生错误，请稍后重试");
            plugin.getLogger().warning("处理主人请求时发生异常: " + e.getMessage());
        }
    }

    private void handleAcceptOwner(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner_accept", "Usage: /owner accept <player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.player_not_found", "Player not found!"));
            return;
        }

        if (!nekoManager.isNeko(player)) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.not_neko", "You are not a neko!"));
            return;
        }

        if (!nekoManager.hasOwnerRequest(target, player)) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.no_pending_request", "No pending owner request!"));
            return;
        }

        try {
            nekoManager.acceptOwnerRequest(target, player);
            HashMap<String, String> replacements1 = new HashMap<>();
            replacements1.put("player", target.getName());
            String message1 = SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.accepted_target", "You accepted {player}'s owner request!");
            player.sendMessage(SafeMessageUtils.replacePlaceholdersSafe(message1, replacements1));
            HashMap<String, String> replacements2 = new HashMap<>();
            replacements2.put("player", player.getName());
            String message2 = SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.accepted_by_target", "{player} accepted your owner request!");
            target.sendMessage(SafeMessageUtils.replacePlaceholdersSafe(message2, replacements2));
        } catch (Exception e) {
            player.sendMessage("§c接受主人请求时发生错误，请稍后重试");
            plugin.getLogger().warning("处理接受主人请求时发生异常: " + e.getMessage());
        }
    }

    private void handleDenyOwner(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner_deny", "Usage: /owner deny <player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.player_not_found", "Player not found!"));
            return;
        }

        if (!nekoManager.isNeko(player)) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.not_neko", "You are not a neko!"));
            return;
        }

        if (!nekoManager.hasOwnerRequest(target, player)) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.no_pending_request", "No pending owner request!"));
            return;
        }

        try {
            nekoManager.denyOwnerRequest(target, player);
            HashMap<String, String> replacements1 = new HashMap<>();
            replacements1.put("player", target.getName());
            String message1 = SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.denied_target", "You denied {player}'s owner request!");
            player.sendMessage(SafeMessageUtils.replacePlaceholdersSafe(message1, replacements1));
            HashMap<String, String> replacements2 = new HashMap<>();
            replacements2.put("player", player.getName());
            String message2 = SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.denied_by_target", "{player} denied your owner request!");
            target.sendMessage(SafeMessageUtils.replacePlaceholdersSafe(message2, replacements2));
        } catch (Exception e) {
            player.sendMessage("§c拒绝主人请求时发生错误，请稍后重试");
            plugin.getLogger().warning("处理拒绝主人请求时发生异常: " + e.getMessage());
        }
    }

    private void handleRemoveOwner(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner_remove", "Usage: /owner remove <player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.player_not_found", "Player not found!"));
            return;
        }

        if (nekoManager.isOwner(player, target)) {
            nekoManager.removeOwner(player, target);
            HashMap<String, String> replacements1 = new HashMap<>();
            replacements1.put("player", target.getName());
            String message1 = SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.remove_owner", "You removed {player} as your owner!");
            player.sendMessage(SafeMessageUtils.replacePlaceholdersSafe(message1, replacements1));
            HashMap<String, String> replacements2 = new HashMap<>();
            replacements2.put("player", player.getName());
            String message2 = SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.removed_as_owner", "You were removed as {player}'s owner!");
            target.sendMessage(SafeMessageUtils.replacePlaceholdersSafe(message2, replacements2));
        } else if (nekoManager.isOwner(target, player)) {
            nekoManager.removeOwner(target, player);
            HashMap<String, String> replacements3 = new HashMap<>();
            replacements3.put("player", target.getName());
            String message3 = SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.remove_master", "You removed {player} as your master!");
            player.sendMessage(SafeMessageUtils.replacePlaceholdersSafe(message3, replacements3));
            HashMap<String, String> replacements4 = new HashMap<>();
            replacements4.put("player", player.getName());
            String message4 = SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.removed_as_master", "You were removed as {player}'s master!");
            target.sendMessage(SafeMessageUtils.replacePlaceholdersSafe(message4, replacements4));
        } else {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.no_owner_relation", "No owner relationship found!"));
        }
    }

    private void handleListOwners(Player player) {
        if (!nekoManager.isNeko(player)) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.not_neko", "You are not a neko!"));
            return;
        }

        Set<Player> owners = nekoManager.getOwners(player);

        if (owners.isEmpty()) {
            StringBuilder message = new StringBuilder(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.no_owners", "You have no owners!"));
            if (player.hasPermission("nekox.admin")) {
                message.append("\n").append(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.admin_tip", "Admin tip: Use /owner forceadd to add owners!"));
            }
            player.sendMessage(message.toString());
            return;
        }

        player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.owners_list", "Your owners:"));
        for (Player owner : owners) {
            player.sendMessage("§e- " + owner.getName());
        }
    }

    private void handleMyListOwners(Player player) {
        Set<Player> nekos = nekoManager.getNekosByOwner(player);

        if (nekos.isEmpty()) {
            StringBuilder message = new StringBuilder(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.no_nekos", "You have no nekos!"));
            if (player.hasPermission("nekox.admin")) {
                message.append("\n").append(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.admin_tip", "Admin tip: Use /owner forceadd to add nekos!"));
            }
            player.sendMessage(message.toString());
            return;
        }

        player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.nekos_list", "Your nekos:"));
        for (Player neko : nekos) {
            player.sendMessage("§e- " + neko.getName());
        }
    }

    private void handleForceAddOwner(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.help.owner_forceadd", "Usage: /owner forceadd <player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.player_not_found", "Player not found!"));
            return;
        }

        if (!nekoManager.isNeko(target)) {
            HashMap<String, String> replacements = new HashMap<>();
            replacements.put("player", target.getName());
            String message = SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.target_not_neko_admin", "{player} is not a neko!");
            player.sendMessage(SafeMessageUtils.replacePlaceholdersSafe(message, replacements));
            return;
        }

        if (nekoManager.isOwner(player, target)) {
            player.sendMessage(SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.already_owner", "You are already the owner of this neko!"));
            return;
        }

        nekoManager.addOwner(player, target);
        HashMap<String, String> replacements1 = new HashMap<>();
        replacements1.put("player", target.getName());
        String message1 = SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.force_add_success", "Successfully added {player} as your neko!");
        player.sendMessage(SafeMessageUtils.replacePlaceholdersSafe(message1, replacements1));
        HashMap<String, String> replacements2 = new HashMap<>();
        replacements2.put("player", player.getName());
        String message2 = SafeMessageUtils.getSafeMessage(languageManager, "commands.owner.force_add_target", "You were forced to become {player}'s neko!");
        target.sendMessage(SafeMessageUtils.replacePlaceholdersSafe(message2, replacements2));
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // 提供子命令补全
            String[] subCommands = {"add", "accept", "deny", "remove", "list", "mylist", "forceadd"};
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(args[0].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
        } else if (args.length == 2) {
            // 提供在线玩家列表补全
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(player.getName());
                }
            }
        }
        
        return completions;
    }
}