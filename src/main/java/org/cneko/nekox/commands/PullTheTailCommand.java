package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.LanguageManager;
import org.cneko.nekox.utils.NekoManager;

public class PullTheTailCommand implements CommandExecutor {
    private final NekoX plugin;
    private final LanguageManager lang;
    private final NekoManager nekoManager;

    public PullTheTailCommand(NekoX plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLanguageManager();
        this.nekoManager = plugin.getNekoManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 检查命令发送者是否为玩家
        if (!(sender instanceof Player)) {
            sender.sendMessage(lang.getMessage("commands.only_player", (String) null));
            return true;
        }

        Player player = (Player) sender;
        boolean currentStatus = nekoManager.isTailPullEnabled(player);
        boolean newStatus = !currentStatus;

        // 设置新状态
        nekoManager.setTailPullEnabled(player, newStatus);

        // 发送反馈消息
        if (newStatus) {
            player.sendMessage(lang.getMessage("tailpull.command.toggle_enabled", (String) null));
        } else {
            player.sendMessage(lang.getMessage("tailpull.command.toggle_disabled", (String) null));
        }

        return true;
    }
}