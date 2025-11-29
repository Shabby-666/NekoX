package org.cneko.nekox.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.cneko.nekox.NekoX;
import org.cneko.nekox.api.events.OwnerRelationshipEvent;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class PlayerConfigManager {
    private final NekoX plugin;
    private Connection connection;
    private final Map<String, Boolean> noticeEnabledCache = new HashMap<>();
    private final Map<String, Boolean> isNekoCache = new HashMap<>();
    private final Map<String, Set<String>> nekoOwnersCache = new HashMap<>(); // 猫娘与其主人的映射
    private final Map<String, Set<String>> ownerRequestsCache = new HashMap<>(); // 主人申请的映射（申请者 -> 被申请的猫娘）

    public PlayerConfigManager(NekoX plugin) {
        this.plugin = plugin;
        this.connection = null; // 初始化为null
        
        try {
            initDatabase();
            if (connection != null) {
                loadAllConfigs();
                loadAllNekoData();
                plugin.getLogger().info("玩家配置管理器初始化成功");
            } else {
                plugin.getLogger().warning("数据库连接失败，将使用内存模式运行");
                initializeMemoryMode();
            }
        } catch (Exception e) {
            plugin.getLogger().severe("初始化玩家配置管理器失败: " + e.getMessage());
            e.printStackTrace();
            plugin.getLogger().warning("将使用内存模式运行");
            initializeMemoryMode();
        }
    }

    private void initDatabase() {
        try {
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }

            File dbFile = new File(dataFolder, "PlayerConfigs.db");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());

            if (connection != null && !connection.isClosed()) {
                plugin.getLogger().info("数据库连接成功");

                // 创建玩家配置表
                String createTableSQL = "CREATE TABLE IF NOT EXISTS player_configs (" +
                        "player_name TEXT PRIMARY KEY, " +
                        "notice_enabled INTEGER DEFAULT 1, " +
                        "is_neko INTEGER DEFAULT 0);";
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(createTableSQL);
                }

                // 创建猫娘主人关系表
                String createOwnersTableSQL = "CREATE TABLE IF NOT EXISTS neko_owners (" +
                        "neko_name TEXT, " +
                        "owner_name TEXT, " +
                        "PRIMARY KEY (neko_name, owner_name));";
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(createOwnersTableSQL);
                }

                // 创建主人申请关系表
                String createRequestsTableSQL = "CREATE TABLE IF NOT EXISTS owner_requests (" +
                        "requester_name TEXT, " +
                        "neko_name TEXT, " +
                        "PRIMARY KEY (requester_name, neko_name));";
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(createRequestsTableSQL);
                }
            } else {
                plugin.getLogger().warning("数据库连接失败");
                connection = null;
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("初始化数据库失败: " + e.getMessage());
            connection = null;
        } catch (Exception e) {
            plugin.getLogger().severe("数据库初始化异常: " + e.getMessage());
            connection = null;
        }
    }
    
    /**
     * 初始化内存模式，当数据库不可用时使用
     */
    private void initializeMemoryMode() {
        plugin.getLogger().info("初始化内存模式，玩家数据将在插件重启后丢失");
        // 清空缓存，使用内存存储
        noticeEnabledCache.clear();
        isNekoCache.clear();
        nekoOwnersCache.clear();
        ownerRequestsCache.clear();
    }

    public void setNoticeEnabled(Player player, boolean enabled) {
        // 添加空指针检查
        if (player == null) {
            return;
        }
        
        final String finalPlayerName = player.getName().toLowerCase();
        noticeEnabledCache.put(finalPlayerName, enabled);

        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            // 添加连接检查
            if (connection == null) {
                plugin.getLogger().severe("数据库连接为空，无法设置玩家通知状态");
                return;
            }
            
            try {
                String sql = "INSERT OR REPLACE INTO player_configs (player_name, notice_enabled, is_neko) VALUES (?, ?, " +
                        "(SELECT is_neko FROM player_configs WHERE player_name = ? UNION SELECT 0 WHERE NOT EXISTS (SELECT 1 FROM player_configs WHERE player_name = ?)));";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalPlayerName);
                    pstmt.setInt(2, enabled ? 1 : 0);
                    pstmt.setString(3, finalPlayerName);
                    pstmt.setString(4, finalPlayerName);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("设置玩家通知状态失败: " + e.getMessage());
            }
        });
    }

    // 新增直接设置玩家通知状态的方法（不触发事件）
    public void setNoticeEnabledDirect(String playerName, boolean enabled) {
        final String finalPlayerName = playerName.toLowerCase();
        noticeEnabledCache.put(finalPlayerName, enabled);

        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String sql = "INSERT OR REPLACE INTO player_configs (player_name, notice_enabled, is_neko) VALUES (?, ?, " +
                        "(SELECT is_neko FROM player_configs WHERE player_name = ? UNION SELECT 0 WHERE NOT EXISTS (SELECT 1 FROM player_configs WHERE player_name = ?)));";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalPlayerName);
                    pstmt.setInt(2, enabled ? 1 : 0);
                    pstmt.setString(3, finalPlayerName);
                    pstmt.setString(4, finalPlayerName);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("设置玩家通知状态失败: " + e.getMessage());
            }
        });
    }

    public boolean isNoticeEnabled(Player player) {
        // 添加空指针检查
        if (player == null) {
            return true; // 默认启用
        }
        
        String playerName = player.getName().toLowerCase();
        if (noticeEnabledCache.containsKey(playerName)) {
            return noticeEnabledCache.get(playerName);
        }

        // 异步加载数据，但立即返回缓存值或默认值
        loadNoticeEnabledFromDatabase(playerName);
        return true; // 默认启用
    }

    private void loadNoticeEnabledFromDatabase(String playerName) {
        final String finalPlayerName = playerName.toLowerCase();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // 检查连接是否可用
                if (connection == null || connection.isClosed()) {
                    plugin.getLogger().warning("数据库连接不可用，跳过加载通知状态");
                    return;
                }
                
                String sql = "SELECT notice_enabled FROM player_configs WHERE player_name = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalPlayerName);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        boolean enabled = rs.getInt("notice_enabled") == 1;
                        noticeEnabledCache.put(finalPlayerName, enabled);
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("查询玩家通知状态失败: " + e.getMessage());
            } catch (Exception e) {
                plugin.getLogger().warning("加载通知状态时发生异常: " + e.getMessage());
            }
        });
    }

    /**
     * 设置玩家为猫娘（通过玩家对象）
     */
    public void setNeko(Player player, boolean isNeko) {
        // 添加空指针检查
        if (player == null) {
            return;
        }
        
        final String finalPlayerName = player.getName().toLowerCase();
        isNekoCache.put(finalPlayerName, isNeko);

        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            // 添加连接检查
            if (connection == null) {
                plugin.getLogger().severe("数据库连接为空，无法设置玩家猫娘状态");
                return;
            }
            
            try {
                String sql = "INSERT OR REPLACE INTO player_configs (player_name, notice_enabled, is_neko) VALUES (?, " +
                        "(SELECT notice_enabled FROM player_configs WHERE player_name = ? UNION SELECT 1 WHERE NOT EXISTS (SELECT 1 FROM player_configs WHERE player_name = ?)), ?);";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalPlayerName);
                    pstmt.setString(2, finalPlayerName);
                    pstmt.setString(3, finalPlayerName);
                    pstmt.setInt(4, isNeko ? 1 : 0);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("设置玩家猫娘状态失败: " + e.getMessage());
            }
        });
    }

    // 新增直接设置玩家为猫娘的方法（不触发事件）
    public void setNekoDirect(String playerName, boolean isNeko) {
        final String finalPlayerName = playerName.toLowerCase();
        isNekoCache.put(finalPlayerName, isNeko);

        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String sql = "INSERT OR REPLACE INTO player_configs (player_name, notice_enabled, is_neko) VALUES (?, " +
                        "(SELECT notice_enabled FROM player_configs WHERE player_name = ? UNION SELECT 1 WHERE NOT EXISTS (SELECT 1 FROM player_configs WHERE player_name = ?)), ?);";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalPlayerName);
                    pstmt.setString(2, finalPlayerName);
                    pstmt.setString(3, finalPlayerName);
                    pstmt.setInt(4, isNeko ? 1 : 0);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("设置玩家猫娘状态失败: " + e.getMessage());
            }
        });
    }

    /**
     * 设置玩家为猫娘（通过玩家名）
     */
    public void setNekoByName(String playerName, boolean isNeko) {
        final String finalPlayerName = playerName.toLowerCase();
        isNekoCache.put(finalPlayerName, isNeko);

        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String sql = "INSERT OR REPLACE INTO player_configs (player_name, notice_enabled, is_neko) VALUES (?, " +
                        "(SELECT notice_enabled FROM player_configs WHERE player_name = ? UNION SELECT 1 WHERE NOT EXISTS (SELECT 1 FROM player_configs WHERE player_name = ?)), ?);";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalPlayerName);
                    pstmt.setString(2, finalPlayerName);
                    pstmt.setString(3, finalPlayerName);
                    pstmt.setInt(4, isNeko ? 1 : 0);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("设置玩家猫娘状态失败: " + e.getMessage());
            }
        });
    }

    /**
     * 检查玩家是否是猫娘（通过玩家对象）
     */
    public boolean isNeko(Player player) {
        // 添加空指针检查
        if (player == null) {
            return false; // 默认不是猫娘
        }
        
        String playerName = player.getName().toLowerCase();
        if (isNekoCache.containsKey(playerName)) {
            return isNekoCache.get(playerName);
        }

        // 异步加载数据，但立即返回缓存值或默认值
        loadNekoStatusFromDatabase(playerName);
        return false; // 默认不是猫娘
    }

    private void loadNekoStatusFromDatabase(String playerName) {
        final String finalPlayerName = playerName.toLowerCase();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String sql = "SELECT is_neko FROM player_configs WHERE player_name = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalPlayerName);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        boolean isNeko = rs.getInt("is_neko") == 1;
                        isNekoCache.put(finalPlayerName, isNeko);
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("查询玩家猫娘状态失败: " + e.getMessage());
            }
        });
    }

    /**
     * 检查玩家是否是猫娘（通过玩家名）
     */
    public boolean isNeko(String playerName) {
        playerName = playerName.toLowerCase();
        if (isNekoCache.containsKey(playerName)) {
            return isNekoCache.get(playerName);
        }

        // 异步加载数据，但立即返回缓存值或默认值
        loadNekoStatusFromDatabase(playerName);
        return false; // 默认不是猫娘
    }

    /**
     * 为主人添加猫娘（通过玩家对象）
     */
    public void addOwner(Player neko, Player owner) {
        // 添加空指针检查
        if (neko == null || owner == null) {
            return;
        }
        
        if (!isNeko(neko)) {
            return; // 不是猫娘
        }

        final String finalNekoName = neko.getName().toLowerCase();
        final String finalOwnerName = owner.getName().toLowerCase();

        // 更新缓存
        nekoOwnersCache.putIfAbsent(finalNekoName, new HashSet<>());
        nekoOwnersCache.get(finalNekoName).add(finalOwnerName);

        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            // 添加连接检查
            if (connection == null) {
                plugin.getLogger().severe("数据库连接为空，无法添加主人关系");
                return;
            }
            
            try {
                String sql = "INSERT OR IGNORE INTO neko_owners (neko_name, owner_name) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalNekoName);
                    pstmt.setString(2, finalOwnerName);
                    pstmt.executeUpdate();
                }
                
                // 触发主人关系变更事件
                Bukkit.getScheduler().runTask(plugin, () -> {
                    OwnerRelationshipEvent event = new OwnerRelationshipEvent(finalNekoName, finalOwnerName, OwnerRelationshipEvent.RelationshipAction.ADD);
                    Bukkit.getPluginManager().callEvent(event);
                });
            } catch (SQLException e) {
                plugin.getLogger().severe("添加主人关系失败: " + e.getMessage());
            }
        });
    }

    // 新增直接添加主人关系的方法（不触发事件）
    public void addOwnerDirect(String nekoName, String ownerName) {
        final String finalNekoName = nekoName.toLowerCase();
        final String finalOwnerName = ownerName.toLowerCase();

        // 检查猫娘是否存在且是猫娘
        if (!isNeko(finalNekoName)) {
            return; // 不是猫娘
        }

        // 更新缓存
        nekoOwnersCache.putIfAbsent(finalNekoName, new HashSet<>());
        nekoOwnersCache.get(finalNekoName).add(finalOwnerName);

        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String sql = "INSERT OR IGNORE INTO neko_owners (neko_name, owner_name) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalNekoName);
                    pstmt.setString(2, finalOwnerName);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("添加主人关系失败: " + e.getMessage());
            }
        });
    }

    /**
     * 为主人添加猫娘（通过玩家名）
     */
    public void addOwnerByName(String nekoName, String ownerName) {
        final String finalNekoName = nekoName.toLowerCase();
        final String finalOwnerName = ownerName.toLowerCase();

        // 检查猫娘是否存在且是猫娘
        if (!isNeko(finalNekoName)) {
            return; // 不是猫娘
        }

        // 更新缓存
        nekoOwnersCache.putIfAbsent(finalNekoName, new HashSet<>());
        nekoOwnersCache.get(finalNekoName).add(finalOwnerName);

        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String sql = "INSERT OR IGNORE INTO neko_owners (neko_name, owner_name) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalNekoName);
                    pstmt.setString(2, finalOwnerName);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("添加主人关系失败: " + e.getMessage());
            }
        });
    }

    /**
     * 移除主人与猫娘的关系（通过玩家对象）
     */
    public void removeOwner(Player neko, Player owner) {
        // 添加空指针检查
        if (neko == null || owner == null) {
            return;
        }
        
        final String finalNekoName = neko.getName().toLowerCase();
        final String finalOwnerName = owner.getName().toLowerCase();

        // 更新缓存
        if (!nekoOwnersCache.containsKey(finalNekoName)) {
            return;
        }

        nekoOwnersCache.get(finalNekoName).remove(finalOwnerName);

        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            // 添加连接检查
            if (connection == null) {
                plugin.getLogger().severe("数据库连接为空，无法移除主人关系");
                return;
            }
            
            try {
                String sql = "DELETE FROM neko_owners WHERE neko_name = ? AND owner_name = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalNekoName);
                    pstmt.setString(2, finalOwnerName);
                    pstmt.executeUpdate();
                }
                
                // 触发主人关系变更事件
                Bukkit.getScheduler().runTask(plugin, () -> {
                    OwnerRelationshipEvent event = new OwnerRelationshipEvent(finalNekoName, finalOwnerName, OwnerRelationshipEvent.RelationshipAction.REMOVE);
                    Bukkit.getPluginManager().callEvent(event);
                });
            } catch (SQLException e) {
                plugin.getLogger().severe("移除主人关系失败: " + e.getMessage());
            }
        });
    }

    // 新增直接移除主人关系的方法（不触发事件）
    public void removeOwnerDirect(String nekoName, String ownerName) {
        final String finalNekoName = nekoName.toLowerCase();
        final String finalOwnerName = ownerName.toLowerCase();

        // 更新缓存
        if (!nekoOwnersCache.containsKey(finalNekoName)) {
            return;
        }

        nekoOwnersCache.get(finalNekoName).remove(finalOwnerName);

        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String sql = "DELETE FROM neko_owners WHERE neko_name = ? AND owner_name = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalNekoName);
                    pstmt.setString(2, finalOwnerName);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("移除主人关系失败: " + e.getMessage());
            }
        });
    }

    /**
     * 移除主人与猫娘的关系（通过玩家名）
     */
    public void removeOwnerByName(String nekoName, String ownerName) {
        final String finalNekoName = nekoName.toLowerCase();
        final String finalOwnerName = ownerName.toLowerCase();

        // 更新缓存
        if (!nekoOwnersCache.containsKey(finalNekoName)) {
            return;
        }

        nekoOwnersCache.get(finalNekoName).remove(finalOwnerName);

        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String sql = "DELETE FROM neko_owners WHERE neko_name = ? AND owner_name = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalNekoName);
                    pstmt.setString(2, finalOwnerName);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("移除主人关系失败: " + e.getMessage());
            }
        });
    }

    /**
     * 获取某只猫娘的所有主人
     */
    public Set<Player> getOwners(Player neko) {
        Set<Player> owners = new HashSet<>();
        final String finalNekoName = neko.getName().toLowerCase();

        Set<String> ownerNames = nekoOwnersCache.get(finalNekoName);
        if (ownerNames != null) {
            for (String ownerName : ownerNames) {
                Player owner = Bukkit.getPlayerExact(ownerName);
                if (owner != null && owner.isOnline()) {
                    owners.add(owner);
                }
            }
        }

        return owners;
    }

    /**
     * 获取某只猫娘的所有主人名（包括离线玩家）
     */
    public Set<String> getOwnerNames(String nekoName) {
        nekoName = nekoName.toLowerCase();
        Set<String> ownerNames = nekoOwnersCache.get(nekoName);
        return ownerNames != null ? new HashSet<>(ownerNames) : new HashSet<>();
    }

    /**
     * 检查某个玩家是否是某只猫娘的主人
     */
    public boolean isOwner(Player owner, Player neko) {
        final String finalNekoName = neko.getName().toLowerCase();
        final String finalOwnerName = owner.getName().toLowerCase();

        Set<String> ownerNames = nekoOwnersCache.get(finalNekoName);
        return ownerNames != null && ownerNames.contains(finalOwnerName);
    }

    /**
     * 检查某个玩家是否是某只猫娘的主人（通过玩家名）
     */
    public boolean isOwnerOf(String ownerName, String nekoName) {
        ownerName = ownerName.toLowerCase();
        nekoName = nekoName.toLowerCase();

        Set<String> ownerNames = nekoOwnersCache.get(nekoName);
        return ownerNames != null && ownerNames.contains(ownerName);
    }

    /**
     * 检查玩家是否有主人
     */
    public boolean hasOwner(String playerName) {
        playerName = playerName.toLowerCase();
        
        // 检查是否有任何猫娘将此玩家作为主人
        for (Set<String> ownerNames : nekoOwnersCache.values()) {
            if (ownerNames.contains(playerName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取某个主人的所有猫娘
     */
    public Set<Player> getNekosByOwner(Player owner) {
        Set<Player> nekos = new HashSet<>();
        final String finalOwnerName = owner.getName().toLowerCase();

        for (Map.Entry<String, Set<String>> entry : nekoOwnersCache.entrySet()) {
            if (entry.getValue().contains(finalOwnerName)) {
                Player neko = Bukkit.getPlayerExact(entry.getKey());
                // 检查玩家是否是猫娘（包括离线玩家）
                if (neko != null && neko.isOnline() && isNeko(neko)) {
                    nekos.add(neko);
                } else if (neko == null && isNeko(entry.getKey())) {
                    // 离线猫娘也包含在内
                    // 注意：这里我们只添加在线玩家到返回集合中
                }
            }
        }

        // 添加在线猫娘
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isNeko(player) && isOwner(owner, player)) {
                nekos.add(player);
            }
        }

        return nekos;
    }

    /**
     * 发送主人申请
     */
    public void sendOwnerRequest(Player requester, Player neko) {
        if (!isNeko(neko)) {
            return; // 不是猫娘
        }

        if (isOwner(requester, neko)) {
            return; // 已经是主人了
        }

        final String finalRequesterName = requester.getName().toLowerCase();
        final String finalNekoName = neko.getName().toLowerCase();

        // 更新缓存
        ownerRequestsCache.putIfAbsent(finalRequesterName, new HashSet<>());
        ownerRequestsCache.get(finalRequesterName).add(finalNekoName);

        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String sql = "INSERT OR IGNORE INTO owner_requests (requester_name, neko_name) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalRequesterName);
                    pstmt.setString(2, finalNekoName);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("发送主人申请失败: " + e.getMessage());
            }
        });
    }

    /**
     * 检查是否有主人申请
     */
    public boolean hasOwnerRequest(Player requester, Player neko) {
        final String finalRequesterName = requester.getName().toLowerCase();
        final String finalNekoName = neko.getName().toLowerCase();

        Set<String> requests = ownerRequestsCache.get(finalRequesterName);
        return requests != null && requests.contains(finalNekoName);
    }

    /**
     * 获取猫娘收到的所有申请
     */
    public Set<Player> getOwnerRequests(Player neko) {
        Set<Player> requesters = new HashSet<>();
        final String finalNekoName = neko.getName().toLowerCase();

        for (Map.Entry<String, Set<String>> entry : ownerRequestsCache.entrySet()) {
            if (entry.getValue().contains(finalNekoName)) {
                Player requester = Bukkit.getPlayerExact(entry.getKey());
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
    public void acceptOwnerRequest(Player requester, Player neko) {
        if (!hasOwnerRequest(requester, neko)) {
            return; // 没有申请
        }

        // 添加主人关系
        addOwner(neko, requester);

        // 移除申请
        removeOwnerRequest(requester, neko);
    }

    /**
     * 拒绝主人申请
     */
    public void denyOwnerRequest(Player requester, Player neko) {
        if (!hasOwnerRequest(requester, neko)) {
            return; // 没有申请
        }

        removeOwnerRequest(requester, neko);
    }

    /**
     * 移除主人申请
     */
    private void removeOwnerRequest(Player requester, Player neko) {
        String requesterName = requester.getName().toLowerCase();
        String nekoName = neko.getName().toLowerCase();

        Set<String> requests = ownerRequestsCache.get(requesterName);
        if (requests == null) {
            return;
        }

        requests.remove(nekoName);

        // 如果该申请者没有其他申请，则移除整个条目
        if (requests.isEmpty()) {
            ownerRequestsCache.remove(requesterName);
        }

        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String sql = "DELETE FROM owner_requests WHERE requester_name = ? AND neko_name = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, requesterName);
                    pstmt.setString(2, nekoName);
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("移除主人申请失败: " + e.getMessage());
            }
        });
    }

    private void loadAllConfigs() {
        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String sql = "SELECT player_name, notice_enabled, is_neko FROM player_configs";
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        String playerName = rs.getString("player_name").toLowerCase();
                        boolean enabled = rs.getInt("notice_enabled") == 1;
                        boolean isNeko = rs.getInt("is_neko") == 1;
                        noticeEnabledCache.put(playerName, enabled);
                        isNekoCache.put(playerName, isNeko);
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("加载所有配置失败: " + e.getMessage());
            }
        });
    }

    private void loadAllNekoData() {
        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            // 加载猫娘主人关系
            try {
                String sql = "SELECT neko_name, owner_name FROM neko_owners";
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        String nekoName = rs.getString("neko_name").toLowerCase();
                        String ownerName = rs.getString("owner_name").toLowerCase();
                        nekoOwnersCache.putIfAbsent(nekoName, new HashSet<>());
                        nekoOwnersCache.get(nekoName).add(ownerName);
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("加载猫娘主人关系失败: " + e.getMessage());
            }

            // 加载主人申请关系
            try {
                String sql = "SELECT requester_name, neko_name FROM owner_requests";
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        String requesterName = rs.getString("requester_name").toLowerCase();
                        String nekoName = rs.getString("neko_name").toLowerCase();
                        ownerRequestsCache.putIfAbsent(requesterName, new HashSet<>());
                        ownerRequestsCache.get(requesterName).add(nekoName);
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("加载主人申请关系失败: " + e.getMessage());
            }
        });
    }

    /**
     * 获取所有猫娘玩家名（包括离线玩家）
     */
    public Set<String> getAllNekoNames() {
        return new HashSet<>(isNekoCache.keySet());
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("关闭数据库连接失败: " + e.getMessage());
        }
    }
}