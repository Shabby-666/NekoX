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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class OwnerCommand implements CommandExecutor, TabCompleter {
    private final NekoManager nekoManager;
    private final LanguageManager languageManager;

    public OwnerCommand(NekoX plugin) {
        this.nekoManager = plugin.getNekoManager();
        this.languageManager = plugin.getLanguageManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c" + languageManager.getMessage("commands.only_player"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(languageManager.getMessage("commands.help.owner"));
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
                    player.sendMessage(languageManager.getMessage("commands.no_permission"));
                    return true;
                }
                handleForceAddOwner(player, args);
                break;
            default:
                player.sendMessage(languageManager.getMessage("commands.help.owner"));
                break;
        }

        return true;
    }

    private void handleAddOwner(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(languageManager.getMessage("commands.help.owner_add"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(languageManager.getMessage("commands.player_not_found"));
            return;
        }

        if (target == player) {
            player.sendMessage(languageManager.getMessage("commands.owner.cant_add_self"));
            return;
        }

        if (!nekoManager.isNeko(target)) {
            player.sendMessage(languageManager.getMessage("commands.owner.target_not_neko"));
            return;
        }

        if (nekoManager.isOwner(player, target)) {
            player.sendMessage(languageManager.getMessage("commands.owner.already_owner"));
            return;
        }

        if (nekoManager.hasOwnerRequest(player, target)) {
            player.sendMessage(languageManager.getMessage("commands.owner.request_already_sent"));
            return;
        }

        nekoManager.sendOwnerRequest(player, target);
        player.sendMessage(languageManager.getMessage("commands.owner.request_sent"));
        HashMap<String, String> replacements = new HashMap<>();
        replacements.put("player", player.getName());
        target.sendMessage(languageManager.getMessage("commands.owner.request_received", replacements));
        target.sendMessage(languageManager.getMessage("commands.owner.request_help", replacements));
    }

    private void handleAcceptOwner(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(languageManager.getMessage("commands.help.owner_accept"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(languageManager.getMessage("commands.player_not_found"));
            return;
        }

        if (!nekoManager.isNeko(player)) {
            player.sendMessage(languageManager.getMessage("commands.owner.not_neko"));
            return;
        }

        if (!nekoManager.hasOwnerRequest(target, player)) {
            player.sendMessage(languageManager.getMessage("commands.owner.no_pending_request"));
            return;
        }

        nekoManager.acceptOwnerRequest(target, player);
        HashMap<String, String> replacements1 = new HashMap<>();
        replacements1.put("player", target.getName());
        player.sendMessage(languageManager.getMessage("commands.owner.accepted_target", replacements1));
        HashMap<String, String> replacements2 = new HashMap<>();
        replacements2.put("player", player.getName());
        target.sendMessage(languageManager.getMessage("commands.owner.accepted_by_target", replacements2));
    }

    private void handleDenyOwner(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(languageManager.getMessage("commands.help.owner_deny"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(languageManager.getMessage("commands.player_not_found"));
            return;
        }

        if (!nekoManager.isNeko(player)) {
            player.sendMessage(languageManager.getMessage("commands.owner.not_neko"));
            return;
        }

        if (!nekoManager.hasOwnerRequest(target, player)) {
            player.sendMessage(languageManager.getMessage("commands.owner.no_pending_request"));
            return;
        }

        nekoManager.denyOwnerRequest(target, player);
        HashMap<String, String> replacements1 = new HashMap<>();
        replacements1.put("player", target.getName());
        player.sendMessage(languageManager.getMessage("commands.owner.denied_target", replacements1));
        HashMap<String, String> replacements2 = new HashMap<>();
        replacements2.put("player", player.getName());
        target.sendMessage(languageManager.getMessage("commands.owner.denied_by_target", replacements2));
    }

    private void handleRemoveOwner(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(languageManager.getMessage("commands.help.owner_remove"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(languageManager.getMessage("commands.player_not_found"));
            return;
        }

        if (nekoManager.isOwner(player, target)) {
            nekoManager.removeOwner(player, target);
            HashMap<String, String> replacements1 = new HashMap<>();
            replacements1.put("player", target.getName());
            player.sendMessage(languageManager.getMessage("commands.owner.remove_owner", replacements1));
            HashMap<String, String> replacements2 = new HashMap<>();
            replacements2.put("player", player.getName());
            target.sendMessage(languageManager.getMessage("commands.owner.removed_as_owner", replacements2));
        } else if (nekoManager.isOwner(target, player)) {
            nekoManager.removeOwner(target, player);
            HashMap<String, String> replacements3 = new HashMap<>();
            replacements3.put("player", target.getName());
            player.sendMessage(languageManager.getMessage("commands.owner.remove_master", replacements3));
            HashMap<String, String> replacements4 = new HashMap<>();
            replacements4.put("player", player.getName());
            target.sendMessage(languageManager.getMessage("commands.owner.removed_as_master", replacements4));
        } else {
            player.sendMessage(languageManager.getMessage("commands.owner.no_owner_relation"));
        }
    }

    private void handleListOwners(Player player) {
        if (!nekoManager.isNeko(player)) {
            player.sendMessage(languageManager.getMessage("commands.owner.not_neko"));
            return;
        }

        Set<Player> owners = nekoManager.getOwners(player);

        if (owners.isEmpty()) {
            StringBuilder message = new StringBuilder(languageManager.getMessage("commands.owner.no_owners"));
            if (player.hasPermission("nekox.admin")) {
                message.append("\n").append(languageManager.getMessage("commands.owner.admin_tip"));
            }
            player.sendMessage(message.toString());
            return;
        }

        player.sendMessage(languageManager.getMessage("commands.owner.owners_list"));
        for (Player owner : owners) {
            player.sendMessage("§e- " + owner.getName());
        }
    }

    private void handleMyListOwners(Player player) {
        Set<Player> nekos = nekoManager.getNekosByOwner(player);

        if (nekos.isEmpty()) {
            StringBuilder message = new StringBuilder(languageManager.getMessage("commands.owner.no_nekos"));
            if (player.hasPermission("nekox.admin")) {
                message.append("\n").append(languageManager.getMessage("commands.owner.admin_tip"));
            }
            player.sendMessage(message.toString());
            return;
        }

        player.sendMessage(languageManager.getMessage("commands.owner.nekos_list"));
        for (Player neko : nekos) {
            player.sendMessage("§e- " + neko.getName());
        }
    }

    private void handleForceAddOwner(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(languageManager.getMessage("commands.help.owner_forceadd"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(languageManager.getMessage("commands.player_not_found"));
            return;
        }

        if (!nekoManager.isNeko(target)) {
            player.sendMessage(languageManager.getMessage("commands.owner.target_not_neko_admin", target.getName()));
            return;
        }

        if (nekoManager.isOwner(player, target)) {
            player.sendMessage(languageManager.getMessage("commands.owner.already_owner"));
            return;
        }

        nekoManager.addOwner(player, target);
        player.sendMessage(languageManager.getMessage("commands.owner.force_add_success", target.getName()));
        target.sendMessage(languageManager.getMessage("commands.owner.force_add_target", player.getName()));
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