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
import java.util.*;

public class NekoManager {
    private final Set<UUID> nekoPlayers = new HashSet<>();
    private final Map<UUID, Set<UUID>> nekoOwners = new HashMap<>(); // 猫娘与其主人的映射
    private final Map<UUID, Set<UUID>> ownerRequests = new HashMap<>(); // 主人申请的映射（申请者 -> 被申请的猫娘）
    private final NekoX plugin;
    
    public NekoManager(NekoX plugin) {
        this.plugin = plugin;
        loadNekoPlayers();
        loadNekoOwners();
        loadOwnerRequests();
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
    
    /**
     * 从nekos_owners.json文件加载猫娘与主人的关系
     */
    private void loadNekoOwners() {
        File file = getNekoOwnersFile();
        if (!file.exists()) {
            return;
        }
        
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, Set<String>>>(){}.getType();
            Map<String, Set<String>> ownerMap = gson.fromJson(reader, mapType);
            
            if (ownerMap != null) {
                for (Map.Entry<String, Set<String>> entry : ownerMap.entrySet()) {
                    try {
                        UUID nekoUUID = UUID.fromString(entry.getKey());
                        Set<UUID> ownerUUIDs = new HashSet<>();
                        
                        for (String ownerUUIDString : entry.getValue()) {
                            try {
                                ownerUUIDs.add(UUID.fromString(ownerUUIDString));
                            } catch (IllegalArgumentException e) {
                                plugin.getLogger().warning("无效的主人UUID格式: " + ownerUUIDString);
                            }
                        }
                        
                        nekoOwners.put(nekoUUID, ownerUUIDs);
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("无效的猫娘UUID格式: " + entry.getKey());
                    }
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("加载nekos_owners.json文件失败: " + e.getMessage());
        }
    }
    
    /**
     * 保存猫娘与主人的关系到nekos_owners.json文件
     */
    private void saveNekoOwners() {
        File file = getNekoOwnersFile();
        
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new Gson();
            Map<String, Set<String>> ownerMap = new HashMap<>();
            
            for (Map.Entry<UUID, Set<UUID>> entry : nekoOwners.entrySet()) {
                String nekoUUIDString = entry.getKey().toString();
                Set<String> ownerUUIDStrings = new HashSet<>();
                
                for (UUID ownerUUID : entry.getValue()) {
                    ownerUUIDStrings.add(ownerUUID.toString());
                }
                
                ownerMap.put(nekoUUIDString, ownerUUIDStrings);
            }
            
            gson.toJson(ownerMap, writer);
        } catch (IOException e) {
            plugin.getLogger().severe("保存nekos_owners.json文件失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取nekos_owners.json文件的引用
     */
    private File getNekoOwnersFile() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        return new File(dataFolder, "nekos_owners.json");
    }
    
    /**
     * 为主人添加猫娘
     */
    public boolean addOwner(Player neko, Player owner) {
        if (!isNeko(neko)) {
            return false; // 不是猫娘
        }
        
        UUID nekoUUID = neko.getUniqueId();
        UUID ownerUUID = owner.getUniqueId();
        
        nekoOwners.putIfAbsent(nekoUUID, new HashSet<>());
        boolean added = nekoOwners.get(nekoUUID).add(ownerUUID);
        
        if (added) {
            saveNekoOwners();
        }
        
        return added;
    }
    
    /**
     * 移除主人与猫娘的关系
     */
    public boolean removeOwner(Player neko, Player owner) {
        UUID nekoUUID = neko.getUniqueId();
        UUID ownerUUID = owner.getUniqueId();
        
        if (!nekoOwners.containsKey(nekoUUID)) {
            return false;
        }
        
        boolean removed = nekoOwners.get(nekoUUID).remove(ownerUUID);
        
        if (removed) {
            saveNekoOwners();
        }
        
        return removed;
    }
    
    /**
     * 获取某只猫娘的所有主人
     */
    public Set<Player> getOwners(Player neko) {
        Set<Player> owners = new HashSet<>();
        UUID nekoUUID = neko.getUniqueId();
        
        if (nekoOwners.containsKey(nekoUUID)) {
            for (UUID ownerUUID : nekoOwners.get(nekoUUID)) {
                Player owner = Bukkit.getPlayer(ownerUUID);
                if (owner != null && owner.isOnline()) {
                    owners.add(owner);
                }
            }
        }
        
        return owners;
    }
    
    /**
     * 检查某个玩家是否是某只猫娘的主人
     */
    public boolean isOwner(Player owner, Player neko) {
        UUID nekoUUID = neko.getUniqueId();
        UUID ownerUUID = owner.getUniqueId();
        
        return nekoOwners.containsKey(nekoUUID) && nekoOwners.get(nekoUUID).contains(ownerUUID);
    }
    
    /**
     * 获取某个主人的所有猫娘
     */
    public Set<Player> getNekosByOwner(Player owner) {
        Set<Player> nekos = new HashSet<>();
        UUID ownerUUID = owner.getUniqueId();
        
        for (Map.Entry<UUID, Set<UUID>> entry : nekoOwners.entrySet()) {
            if (entry.getValue().contains(ownerUUID)) {
                Player neko = Bukkit.getPlayer(entry.getKey());
                if (neko != null && neko.isOnline() && isNeko(neko)) {
                    nekos.add(neko);
                }
            }
        }
        
        return nekos;
    }
    
    // 主人申请相关方法
    
    /**
     * 发送主人申请
     */
    public boolean sendOwnerRequest(Player requester, Player neko) {
        if (!isNeko(neko)) {
            return false; // 不是猫娘
        }
        
        if (isOwner(requester, neko)) {
            return false; // 已经是主人了
        }
        
        UUID requesterUUID = requester.getUniqueId();
        UUID nekoUUID = neko.getUniqueId();
        
        ownerRequests.putIfAbsent(requesterUUID, new HashSet<>());
        boolean added = ownerRequests.get(requesterUUID).add(nekoUUID);
        
        if (added) {
            saveOwnerRequests();
        }
        
        return added;
    }
    
    /**
     * 检查是否有主人申请
     */
    public boolean hasOwnerRequest(Player requester, Player neko) {
        UUID requesterUUID = requester.getUniqueId();
        UUID nekoUUID = neko.getUniqueId();
        
        return ownerRequests.containsKey(requesterUUID) && ownerRequests.get(requesterUUID).contains(nekoUUID);
    }
    
    /**
     * 获取猫娘收到的所有申请
     */
    public Set<Player> getOwnerRequests(Player neko) {
        Set<Player> requesters = new HashSet<>();
        UUID nekoUUID = neko.getUniqueId();
        
        for (Map.Entry<UUID, Set<UUID>> entry : ownerRequests.entrySet()) {
            if (entry.getValue().contains(nekoUUID)) {
                Player requester = Bukkit.getPlayer(entry.getKey());
                if (requester != null && requester.isOnline()) {
                    requesters.add(requester);
                }
            }
        }
        
        return requesters;
    }
    
    /**
     * 接受主人申请
     */
    public boolean acceptOwnerRequest(Player requester, Player neko) {
        if (!hasOwnerRequest(requester, neko)) {
            return false; // 没有申请
        }
        
        // 添加主人关系
        boolean added = addOwner(neko, requester);
        
        if (added) {
            // 移除申请
            removeOwnerRequest(requester, neko);
        }
        
        return added;
    }
    
    /**
     * 拒绝主人申请
     */
    public boolean denyOwnerRequest(Player requester, Player neko) {
        if (!hasOwnerRequest(requester, neko)) {
            return false; // 没有申请
        }
        
        return removeOwnerRequest(requester, neko);
    }
    
    /**
     * 移除主人申请
     */
    private boolean removeOwnerRequest(Player requester, Player neko) {
        UUID requesterUUID = requester.getUniqueId();
        UUID nekoUUID = neko.getUniqueId();
        
        if (!ownerRequests.containsKey(requesterUUID)) {
            return false;
        }
        
        boolean removed = ownerRequests.get(requesterUUID).remove(nekoUUID);
        
        if (removed) {
            // 如果该申请者没有其他申请，则移除整个条目
            if (ownerRequests.get(requesterUUID).isEmpty()) {
                ownerRequests.remove(requesterUUID);
            }
            saveOwnerRequests();
        }
        
        return removed;
    }
    
    /**
     * 从nekos_owner_requests.json文件加载主人申请
     */
    private void loadOwnerRequests() {
        File file = getOwnerRequestsFile();
        if (!file.exists()) {
            return;
        }
        
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, Set<String>>>(){}.getType();
            Map<String, Set<String>> requestMap = gson.fromJson(reader, mapType);
            
            if (requestMap != null) {
                for (Map.Entry<String, Set<String>> entry : requestMap.entrySet()) {
                    try {
                        UUID requesterUUID = UUID.fromString(entry.getKey());
                        Set<UUID> nekoUUIDs = new HashSet<>();
                        
                        for (String nekoUUIDString : entry.getValue()) {
                            try {
                                nekoUUIDs.add(UUID.fromString(nekoUUIDString));
                            } catch (IllegalArgumentException e) {
                                plugin.getLogger().warning("无效的猫娘UUID格式: " + nekoUUIDString);
                            }
                        }
                        
                        ownerRequests.put(requesterUUID, nekoUUIDs);
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("无效的申请者UUID格式: " + entry.getKey());
                    }
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("加载nekos_owner_requests.json文件失败: " + e.getMessage());
        }
    }
    
    /**
     * 保存主人申请到nekos_owner_requests.json文件
     */
    private void saveOwnerRequests() {
        File file = getOwnerRequestsFile();
        
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new Gson();
            Map<String, Set<String>> requestMap = new HashMap<>();
            
            for (Map.Entry<UUID, Set<UUID>> entry : ownerRequests.entrySet()) {
                String requesterUUIDString = entry.getKey().toString();
                Set<String> nekoUUIDStrings = new HashSet<>();
                
                for (UUID nekoUUID : entry.getValue()) {
                    nekoUUIDStrings.add(nekoUUID.toString());
                }
                
                requestMap.put(requesterUUIDString, nekoUUIDStrings);
            }
            
            gson.toJson(requestMap, writer);
        } catch (IOException e) {
            plugin.getLogger().severe("保存nekos_owner_requests.json文件失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取nekos_owner_requests.json文件的引用
     */
    private File getOwnerRequestsFile() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        return new File(dataFolder, "nekos_owner_requests.json");
    }
}