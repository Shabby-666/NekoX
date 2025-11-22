package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.PlayerConfigManager;

public class PlayerNoticeCommand implements CommandExecutor {
    private final NekoX plugin;
    private final PlayerConfigManager configManager;
    private final LanguageManager languageManager;

    public PlayerNoticeCommand(NekoX plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getPlayerConfigManager();
        this.languageManager = plugin.getLanguageManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c" + languageManager.getMessage("commands.only_player"));
            return true;
        }

        Player player = (Player) sender;

        if (!plugin.getNekoManager().isNeko(player)) {
            player.sendMessage("§c" + languageManager.getMessage("commands.health.notneko"));
            return true;
        }

        if (args.length == 0) {
            boolean enabled = configManager.isNoticeEnabled(player);
            if (enabled) {
                player.sendMessage("§a" + languageManager.getMessage("commands.playernotice.enabled"));
            } else {
                player.sendMessage("§c" + languageManager.getMessage("commands.playernotice.disabled"));
            }
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "on":
            case "enable":
                configManager.setNoticeEnabled(player, true);
                player.sendMessage("§a" + languageManager.getMessage("commands.playernotice.enabled_success"));
                return true;

            case "off":
            case "disable":
                configManager.setNoticeEnabled(player, false);
                player.sendMessage("§a" + languageManager.getMessage("commands.playernotice.disabled_success"));
                return true;

            default:
                player.sendMessage("§c" + languageManager.getMessage("commands.playernotice.usage"));
                return true;
        }
    }
}