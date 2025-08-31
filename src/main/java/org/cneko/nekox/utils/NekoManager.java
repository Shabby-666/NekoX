package org.cneko.nekox.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NekoManager {
    private final Set<UUID> nekoPlayers = new HashSet<>();
    private final NekoX plugin;
    
    public NekoManager(NekoX plugin) {
        this.plugin = plugin;
        loadNekoPlayers();
    }
    
    /**
     * 设置玩家为猫娘
     */
    public boolean setNeko(Player player, boolean isNeko) {
        if (isNeko) {
            if (nekoPlayers.add(player.getUniqueId())) {
                saveNekoPlayers();
                return true;
            }
        } else {
            if (nekoPlayers.remove(player.getUniqueId())) {
                saveNekoPlayers();
                return true;
            }
        }
        return false;
    }
    
    /**
     * 检查玩家是否是猫娘
     */
    public boolean isNeko(Player player) {
        return nekoPlayers.contains(player.getUniqueId());
    }
    
    /**
     * 从nekos.json文件加载猫娘玩家列表
     */
    private void loadNekoPlayers() {
        File file = getNekosFile();
        if (!file.exists()) {
            return;
        }
        
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type setType = new TypeToken<Set<String>>(){}.getType();
            Set<String> uuidStrings = gson.fromJson(reader, setType);
            
            if (uuidStrings != null) {
                for (String uuidString : uuidStrings) {
                    try {
                        UUID uuid = UUID.fromString(uuidString);
                        nekoPlayers.add(uuid);
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("无效的UUID格式: " + uuidString);
                    }
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("加载nekos.json文件失败: " + e.getMessage());
        }
    }
    
    /**
     * 保存猫娘玩家列表到nekos.json文件
     */
    private void saveNekoPlayers() {
        File file = getNekosFile();
        
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new Gson();
            Set<String> uuidStrings = new HashSet<>();
            for (UUID uuid : nekoPlayers) {
                uuidStrings.add(uuid.toString());
            }
            gson.toJson(uuidStrings, writer);
        } catch (IOException e) {
            plugin.getLogger().severe("保存nekos.json文件失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取nekos.json文件的引用
     */
    private File getNekosFile() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        return new File(dataFolder, "nekos.json");
    }
    
    /**
     * 获取所有猫娘玩家
     */
    public Set<Player> getNekoPlayers() {
        Set<Player> players = new HashSet<>();
        for (UUID uuid : nekoPlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                players.add(player);
            }
        }
        return players;
    }
}