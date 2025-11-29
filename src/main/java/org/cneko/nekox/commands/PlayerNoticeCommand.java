package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.PlayerConfigManagerSafe;
import org.cneko.nekox.utils.SafeMessageUtils;

public class PlayerNoticeCommand implements CommandExecutor {
    private final NekoX plugin;
    private final PlayerConfigManagerSafe configManager;
    private final LanguageManager languageManager;

    public PlayerNoticeCommand(NekoX plugin) {
        this.plugin = plugin;
        this.configManager = (PlayerConfigManagerSafe) plugin.getPlayerConfigManager();
        this.languageManager = plugin.getLanguageManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.only_player", "Only players can use this command!"));
            return true;
        }

        Player player = (Player) sender;

        if (!plugin.getNekoManager().isNeko(player)) {
            player.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.health.notneko", "You are not a neko!"));
            return true;
        }

        if (args.length == 0) {
            boolean enabled = configManager.isNoticeEnabled(player);
            if (enabled) {
                player.sendMessage("§a" + SafeMessageUtils.getSafeMessage(languageManager, "commands.playernotice.enabled", "Player notices are enabled!"));
            } else {
                player.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.playernotice.disabled", "Player notices are disabled!"));
            }
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "on":
            case "enable":
                configManager.setNoticeEnabled(player, true);
                player.sendMessage("§a" + SafeMessageUtils.getSafeMessage(languageManager, "commands.playernotice.enabled_success", "Player notices enabled successfully!"));
                return true;

            case "off":
            case "disable":
                configManager.setNoticeEnabled(player, false);
                player.sendMessage("§a" + SafeMessageUtils.getSafeMessage(languageManager, "commands.playernotice.disabled_success", "Player notices disabled successfully!"));
                return true;

            default:
                player.sendMessage("§c" + SafeMessageUtils.getSafeMessage(languageManager, "commands.playernotice.usage", "Usage: /playernotice <on|off>"));
                return true;
        }
    }
}