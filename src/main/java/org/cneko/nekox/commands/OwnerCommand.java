package org.cneko.nekox.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.utils.NekoManager;

import java.util.Set;

public class OwnerCommand implements CommandExecutor {
    private final NekoX plugin;
    private final NekoManager nekoManager;
    
    public OwnerCommand(NekoX plugin) {
        this.plugin = plugin;
        this.nekoManager = plugin.getNekoManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 确保命令发送者是玩家
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家可以使用此命令！");
            return true;
        }
        
        Player player = (Player)sender;
        
        // 检查参数数量
        if (args.length < 1) {
            sendHelp(player);
            return true;
        }
        
        // 处理不同的子命令
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "add":
                handleAddOwner(player, args);
                break;
            case "remove":
                handleRemoveOwner(player, args);
                break;
            case "list":
                handleListOwners(player);
                break;
            case "mylist":
                handleMyNekos(player);
                break;
            case "accept":
                handleAcceptOwner(player, args);
                break;
            case "deny":
                handleDenyOwner(player, args);
                break;
            case "forceadd":
                handleForceAddOwner(player, args);
                break;
            case "help":
                sendHelp(player);
                break;
            default:
                player.sendMessage("§c未知的子命令！输入 /owner help 查看帮助");
        }
        
        return true;
    }
    
    private void handleAddOwner(Player sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§c用法: /owner add <猫娘玩家>");
            return;
        }
        
        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§c找不到玩家: " + args[1]);
            return;
        }
        
        if (sender.equals(target)) {
            sender.sendMessage("§c你不能成为自己的主人！");
            return;
        }
        
        if (!nekoManager.isNeko(target)) {
            sender.sendMessage("§c玩家 " + target.getName() + " 不是猫娘！");
            return;
        }
        
        if (nekoManager.isOwner(sender, target)) {
            sender.sendMessage("§e你已经是玩家 " + target.getName() + " 的主人了！");
            return;
        }
        
        if (nekoManager.hasOwnerRequest(sender, target)) {
            sender.sendMessage("§e你已经向玩家 " + target.getName() + " 发送过主人申请了，请等待对方回应！");
            return;
        }
        
        boolean sent = nekoManager.sendOwnerRequest(sender, target);
        
        if (sent) {
            sender.sendMessage("§a主人申请已发送给玩家 " + target.getName() + " ，请等待对方回应！");
            target.sendMessage("§e玩家 " + sender.getName() + " 向你发送了主人申请！使用 /owner accept " + sender.getName() + " 接受，或使用 /owner deny " + sender.getName() + " 拒绝。");
        } else {
            sender.sendMessage("§c发送主人申请失败！");
        }
    }
    
    private void handleAcceptOwner(Player sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§c用法: /owner accept <申请者>");
            return;
        }
        
        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§c找不到玩家: " + args[1]);
            return;
        }
        
        if (!nekoManager.isNeko(sender)) {
            sender.sendMessage("§c只有猫娘可以接受主人申请！");
            return;
        }
        
        if (!nekoManager.hasOwnerRequest(target, sender)) {
            sender.sendMessage("§c玩家 " + target.getName() + " 没有向你发送过主人申请！");
            return;
        }
        
        boolean accepted = nekoManager.acceptOwnerRequest(target, sender);
        
        if (accepted) {
            sender.sendMessage("§a你已接受玩家 " + target.getName() + " 成为你的主人！喵~♪");
            target.sendMessage("§a玩家 " + sender.getName() + " 已接受你的主人申请！");
        } else {
            sender.sendMessage("§c接受主人申请失败！");
        }
    }
    
    private void handleDenyOwner(Player sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§c用法: /owner deny <申请者>");
            return;
        }
        
        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§c找不到玩家: " + args[1]);
            return;
        }
        
        if (!nekoManager.isNeko(sender)) {
            sender.sendMessage("§c只有猫娘可以拒绝主人申请！");
            return;
        }
        
        if (!nekoManager.hasOwnerRequest(target, sender)) {
            sender.sendMessage("§c玩家 " + target.getName() + " 没有向你发送过主人申请！");
            return;
        }
        
        boolean denied = nekoManager.denyOwnerRequest(target, sender);
        
        if (denied) {
            sender.sendMessage("§a你已拒绝玩家 " + target.getName() + " 的主人申请！");
            target.sendMessage("§e玩家 " + sender.getName() + " 已拒绝你的主人申请！");
        } else {
            sender.sendMessage("§c拒绝主人申请失败！");
        }
    }
    
    private void handleForceAddOwner(Player sender, String[] args) {
        if (!sender.hasPermission("nekox.admin")) {
            sender.sendMessage("§c你没有权限使用此命令！");
            return;
        }
        
        if (args.length < 2) {
            sender.sendMessage("§c用法: /owner forceadd <猫娘玩家>");
            return;
        }
        
        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§c找不到玩家: " + args[1]);
            return;
        }
        
        if (sender.equals(target)) {
            sender.sendMessage("§c你不能成为自己的主人！");
            return;
        }
        
        if (!nekoManager.isNeko(target)) {
            sender.sendMessage("§c玩家 " + target.getName() + " 不是猫娘！");
            return;
        }
        
        boolean added = nekoManager.addOwner(target, sender);
        
        if (added) {
            sender.sendMessage("§a已强制成为玩家 " + target.getName() + " 的主人！");
            target.sendMessage("§e管理员 " + sender.getName() + " 已强制成为你的主人！喵~♪");
        } else {
            sender.sendMessage("§e你已经是玩家 " + target.getName() + " 的主人了！");
        }
    }
    
    private void handleRemoveOwner(Player sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§c用法: /owner remove <玩家>");
            return;
        }

        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§c找不到玩家: " + args[1]);
            return;
        }

        if (sender.equals(target)) {
            sender.sendMessage("§c你不能移除与自己的主人关系！");
            return;
        }

        // 检查sender是否是target的主人，或者target是否是sender的主人
        boolean isOwnerAndNeko = nekoManager.isOwner(sender, target);
        boolean isNekoAndOwner = nekoManager.isOwner(target, sender);
        
        if (!isOwnerAndNeko && !isNekoAndOwner) {
            sender.sendMessage("§c你和玩家 " + target.getName() + " 之间没有主人关系！");
            return;
        }

        // 确定谁是主人，谁是猫娘
        Player owner, neko;
        if (isOwnerAndNeko) {
            owner = sender;
            neko = target;
        } else {
            owner = target;
            neko = sender;
        }

        boolean removed = nekoManager.removeOwner(neko, owner);

        if (removed) {
            sender.sendMessage("§a成功移除与玩家 " + target.getName() + " 的主人关系！");
            target.sendMessage("§e玩家 " + sender.getName() + " 已移除与你的主人关系！");
        }
    }
    
    private void handleListOwners(Player sender) {
        if (!nekoManager.isNeko(sender)) {
            sender.sendMessage("§c只有猫娘可以查看自己的主人！");
            return;
        }
        
        Set<Player> owners = nekoManager.getOwners(sender);
        
        if (owners.isEmpty()) {
            sender.sendMessage("§e你还没有主人呢！喵~♪");
        } else {
            StringBuilder message = new StringBuilder("§a你的主人列表：");
            for (Player owner : owners) {
                message.append(" ").append(owner.getName());
            }
            sender.sendMessage(message.toString());
        }
    }
    
    private void handleMyNekos(Player sender) {
        Set<Player> nekos = nekoManager.getNekosByOwner(sender);
        
        if (nekos.isEmpty()) {
            sender.sendMessage("§e你还没有猫娘呢！");
        } else {
            StringBuilder message = new StringBuilder("§a你拥有的猫娘：");
            for (Player neko : nekos) {
                message.append(" ").append(neko.getName());
            }
            sender.sendMessage(message.toString());
        }
    }
    
    private void sendHelp(Player player) {
        player.sendMessage("§6===== NekoX 主人系统帮助 =====");
        player.sendMessage("§e/owner add <玩家> - 向指定猫娘玩家发送主人申请");
        player.sendMessage("§e/owner accept <玩家> - 接受指定玩家的主人申请");
        player.sendMessage("§e/owner deny <玩家> - 拒绝指定玩家的主人申请");
        player.sendMessage("§e/owner forceadd <玩家> - 无需猫娘同意直接成为其主人(管理员专用)");
        player.sendMessage("§e/owner remove <玩家> - 移除与指定玩家的主人关系(猫娘和主人都可以使用)");
        player.sendMessage("§e/owner list - 查看你(作为猫娘)的所有主人");
        player.sendMessage("§e/owner mylist - 查看你拥有的所有猫娘");
        player.sendMessage("§e/owner help - 查看此帮助信息");
    }
}