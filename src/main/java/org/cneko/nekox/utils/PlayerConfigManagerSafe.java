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

/**
 * 安全的玩家配置管理器，包含完整的错误处理和内存模式支持
 */
public class PlayerConfigManagerSafe {
    private final NekoX plugin;
    private Connection connection;
    private final Map<String, Boolean> noticeEnabledCache = new HashMap<>();
    private final Map<String, Boolean> isNekoCache = new HashMap<>();
    private final Map<String, Set<String>> nekoOwnersCache = new HashMap<>(); // 猫娘与其主人的映射
    private final Map<String, Set<String>> ownerRequestsCache = new HashMap<>(); // 主人申请的映射（申请者 -> 被申请的猫娘）
    private boolean memoryMode = false;

    public PlayerConfigManagerSafe(NekoX plugin) {
        this.plugin = plugin;
        this.connection = null; // 初始化为null
        
        try {
            initDatabase();
            if (connection != null && !connection.isClosed()) {
                loadAllConfigs();
                loadAllNekoData();
                plugin.getLogger().info("玩家配置管理器初始化成功");
                memoryMode = false;
            } else {
                plugin.getLogger().warning("数据库连接失败，将使用内存模式运行");
                initializeMemoryMode();
                memoryMode = true;
            }
        } catch (Exception e) {
            plugin.getLogger().severe("初始化玩家配置管理器失败: " + e.getMessage());
            e.printStackTrace();
            plugin.getLogger().warning("将使用内存模式运行");
            initializeMemoryMode();
            memoryMode = true;
        }
    }

    private void initDatabase() {
        try {
            // 检查SQLite驱动是否可用
            try {
                Class.forName("org.sqlite.JDBC");
                plugin.getLogger().info("SQLite JDBC驱动已加载");
            } catch (ClassNotFoundException e) {
                plugin.getLogger().severe("SQLite JDBC驱动未找到！请确保已添加sqlite-jdbc依赖");
                connection = null;
                return;
            }

            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
                plugin.getLogger().info("创建插件数据文件夹: " + dataFolder.getAbsolutePath());
            }

            File dbFile = new File(dataFolder, "PlayerConfigs.db");
            plugin.getLogger().info("数据库文件路径: " + dbFile.getAbsolutePath());
            
            // 检查数据库文件是否可写
            if (dbFile.exists() && !dbFile.canWrite()) {
                plugin.getLogger().severe("数据库文件不可写: " + dbFile.getAbsolutePath());
                connection = null;
                return;
            }

            String dbUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            plugin.getLogger().info("尝试连接数据库: " + dbUrl);
            
            connection = DriverManager.getConnection(dbUrl);

            if (connection != null && !connection.isClosed()) {
                plugin.getLogger().info("数据库连接成功！");

                // 启用外键约束
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON");
                    plugin.getLogger().info("启用外键约束");
                }

                // 创建玩家配置表
                String createTableSQL = "CREATE TABLE IF NOT EXISTS player_configs (" +
                        "player_name TEXT PRIMARY KEY, " +
                        "notice_enabled INTEGER DEFAULT 1, " +
                        "is_neko INTEGER DEFAULT 0);";
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(createTableSQL);
                    plugin.getLogger().info("创建/验证 player_configs 表");
                }

                // 创建猫娘主人关系表
                String createOwnersTableSQL = "CREATE TABLE IF NOT EXISTS neko_owners (" +
                        "neko_name TEXT, " +
                        "owner_name TEXT, " +
                        "PRIMARY KEY (neko_name, owner_name));";
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(createOwnersTableSQL);
                    plugin.getLogger().info("创建/验证 neko_owners 表");
                }

                // 创建主人申请关系表
                String createRequestsTableSQL = "CREATE TABLE IF NOT EXISTS owner_requests (" +
                        "requester_name TEXT, " +
                        "neko_name TEXT, " +
                        "PRIMARY KEY (requester_name, neko_name));";
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(createRequestsTableSQL);
                    plugin.getLogger().info("创建/验证 owner_requests 表");
                }
                
                plugin.getLogger().info("数据库初始化完成");
            } else {
                plugin.getLogger().severe("数据库连接失败：连接对象为null或已关闭");
                connection = null;
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("数据库SQL错误: " + e.getMessage());
            plugin.getLogger().severe("错误代码: " + e.getErrorCode());
            plugin.getLogger().severe("SQL状态: " + e.getSQLState());
            connection = null;
        } catch (Exception e) {
            plugin.getLogger().severe("数据库初始化异常: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
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
        memoryMode = true;
    }

    /**
     * 加载所有玩家配置到缓存
     */
    private void loadAllConfigs() {
        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (memoryMode || connection == null) {
                plugin.getLogger().info("内存模式或数据库不可用，跳过加载配置");
                return;
            }
            
            safeDatabaseOperation(() -> {
                String sql = "SELECT player_name, notice_enabled, is_neko FROM player_configs";
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    int count = 0;
                    while (rs.next()) {
                        String playerName = rs.getString("player_name").toLowerCase();
                        boolean enabled = rs.getInt("notice_enabled") == 1;
                        boolean isNeko = rs.getInt("is_neko") == 1;
                        noticeEnabledCache.put(playerName, enabled);
                        isNekoCache.put(playerName, isNeko);
                        count++;
                    }
                    plugin.getLogger().info("加载了 " + count + " 个玩家配置");
                }
                return true;
            }, "加载所有配置失败");
        });
    }

    /**
     * 加载所有猫娘关系到缓存
     */
    private void loadAllNekoData() {
        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (memoryMode || connection == null) {
                plugin.getLogger().info("内存模式或数据库不可用，跳过加载猫娘关系");
                return;
            }
            
            // 加载猫娘主人关系
            safeDatabaseOperation(() -> {
                String sql = "SELECT neko_name, owner_name FROM neko_owners";
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    int ownerCount = 0;
                    while (rs.next()) {
                        String nekoName = rs.getString("neko_name").toLowerCase();
                        String ownerName = rs.getString("owner_name").toLowerCase();
                        nekoOwnersCache.putIfAbsent(nekoName, new HashSet<>());
                        nekoOwnersCache.get(nekoName).add(ownerName);
                        ownerCount++;
                    }
                    plugin.getLogger().info("加载了 " + ownerCount + " 个主人关系");
                }
                return true;
            }, "加载猫娘主人关系失败");

            // 加载主人申请关系
            safeDatabaseOperation(() -> {
                String sql = "SELECT requester_name, neko_name FROM owner_requests";
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    int requestCount = 0;
                    while (rs.next()) {
                        String requesterName = rs.getString("requester_name").toLowerCase();
                        String nekoName = rs.getString("neko_name").toLowerCase();
                        ownerRequestsCache.putIfAbsent(requesterName, new HashSet<>());
                        ownerRequestsCache.get(requesterName).add(nekoName);
                        requestCount++;
                    }
                    plugin.getLogger().info("加载了 " + requestCount + " 个主人申请");
                }
                return true;
            }, "加载主人申请关系失败");
        });
    }

    public void setNoticeEnabled(Player player, boolean enabled) {
        if (player == null) {
            return;
        }
        
        try {
            final String finalPlayerName = player.getName().toLowerCase();
            noticeEnabledCache.put(finalPlayerName, enabled);

            // 异步执行数据库操作
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (!safeDatabaseOperation(() -> {
                    String sql = "INSERT OR REPLACE INTO player_configs (player_name, notice_enabled, is_neko) VALUES (?, ?, " +
                            "(SELECT is_neko FROM player_configs WHERE player_name = ? UNION SELECT 0 WHERE NOT EXISTS (SELECT 1 FROM player_configs WHERE player_name = ?)));";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setString(1, finalPlayerName);
                        pstmt.setInt(2, enabled ? 1 : 0);
                        pstmt.setString(3, finalPlayerName);
                        pstmt.setString(4, finalPlayerName);
                        pstmt.executeUpdate();
                    }
                    return true;
                }, "设置玩家通知状态失败")) {
                    // 数据库操作失败，数据仍保存在内存中
                    plugin.getLogger().info("通知状态已保存到内存");
                }
            });
        } catch (Exception e) {
            plugin.getLogger().warning("设置通知状态时发生异常: " + e.getMessage());
        }
    }

    public boolean isNoticeEnabled(Player player) {
        if (player == null) {
            return true; // 默认启用
        }
        
        try {
            String playerName = player.getName().toLowerCase();
            if (noticeEnabledCache.containsKey(playerName)) {
                return noticeEnabledCache.get(playerName);
            }

            // 异步加载数据，但立即返回缓存值或默认值
            loadNoticeEnabledFromDatabase(playerName);
            return true; // 默认启用
        } catch (Exception e) {
            plugin.getLogger().warning("检查通知状态时发生异常: " + e.getMessage());
            return true;
        }
    }

    private void loadNoticeEnabledFromDatabase(String playerName) {
        final String finalPlayerName = playerName.toLowerCase();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            safeDatabaseOperation(() -> {
                String sql = "SELECT notice_enabled FROM player_configs WHERE player_name = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalPlayerName);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        boolean enabled = rs.getInt("notice_enabled") == 1;
                        noticeEnabledCache.put(finalPlayerName, enabled);
                    }
                }
                return true;
            }, "查询玩家通知状态失败");
        });
    }

    public void setNeko(Player player, boolean isNeko) {
        if (player == null) {
            return;
        }
        
        setNekoByName(player.getName(), isNeko);
    }

    public void setNekoByName(String playerName, boolean isNeko) {
        if (playerName == null || playerName.trim().isEmpty()) {
            return;
        }
        
        try {
            final String finalPlayerName = playerName.toLowerCase();
            isNekoCache.put(finalPlayerName, isNeko);

            // 异步执行数据库操作
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (!safeDatabaseOperation(() -> {
                    String sql = "INSERT OR REPLACE INTO player_configs (player_name, notice_enabled, is_neko) VALUES (?, " +
                            "(SELECT notice_enabled FROM player_configs WHERE player_name = ? UNION SELECT 1 WHERE NOT EXISTS (SELECT 1 FROM player_configs WHERE player_name = ?)), ?);";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setString(1, finalPlayerName);
                        pstmt.setString(2, finalPlayerName);
                        pstmt.setString(3, finalPlayerName);
                        pstmt.setInt(4, isNeko ? 1 : 0);
                        pstmt.executeUpdate();
                    }
                    return true;
                }, "设置玩家猫娘状态失败")) {
                    // 数据库操作失败，数据仍保存在内存中
                    plugin.getLogger().info("猫娘状态已保存到内存");
                }
            });
        } catch (Exception e) {
            plugin.getLogger().warning("设置猫娘状态时发生异常: " + e.getMessage());
        }
    }

    public boolean isNeko(Player player) {
        if (player == null) {
            return false;
        }
        
        try {
            String playerName = player.getName().toLowerCase();
            
            // 首先检查缓存
            if (isNekoCache.containsKey(playerName)) {
                return isNekoCache.get(playerName);
            }
            
            // 如果是内存模式，直接返回false
            if (memoryMode) {
                isNekoCache.put(playerName, false);
                return false;
            }
            
            // 如果数据库连接可用，从数据库加载
            if (connection != null) {
                try {
                    loadPlayerConfig(playerName);
                } catch (Exception e) {
                    plugin.getLogger().warning("加载玩家配置失败: " + e.getMessage());
                    isNekoCache.put(playerName, false);
                }
            }
            
            // 返回缓存中的值，如果没有则默认为false
            return isNekoCache.getOrDefault(playerName, false);
        } catch (Exception e) {
            plugin.getLogger().warning("检查玩家猫娘状态失败: " + e.getMessage());
            return false;
        }
    }
    
    public boolean isNeko(String playerName) {
        if (playerName == null || playerName.trim().isEmpty()) {
            return false;
        }
        
        try {
            String normalizedName = playerName.toLowerCase();
            
            // 首先检查缓存
            if (isNekoCache.containsKey(normalizedName)) {
                return isNekoCache.get(normalizedName);
            }
            
            // 如果是内存模式，直接返回false
            if (memoryMode) {
                isNekoCache.put(normalizedName, false);
                return false;
            }
            
            // 如果数据库连接可用，从数据库加载
            if (connection != null) {
                try {
                    loadPlayerConfig(normalizedName);
                } catch (Exception e) {
                    plugin.getLogger().warning("加载玩家配置失败: " + e.getMessage());
                    isNekoCache.put(normalizedName, false);
                }
            }
            
            // 返回缓存中的值，如果没有则默认为false
            return isNekoCache.getOrDefault(normalizedName, false);
        } catch (Exception e) {
            plugin.getLogger().warning("检查玩家猫娘状态失败: " + e.getMessage());
            return false;
        }
    }

    private void loadPlayerConfig(String playerName) {
        safeDatabaseOperation(() -> {
            String sql = "SELECT is_neko FROM player_configs WHERE player_name = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, playerName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    boolean isNeko = rs.getInt("is_neko") == 1;
                    isNekoCache.put(playerName, isNeko);
                } else {
                    isNekoCache.put(playerName, false);
                }
            }
            return true;
        }, "加载玩家配置失败");
    }

    public void addOwner(Player neko, Player owner) {
        if (neko == null || owner == null) {
            return;
        }
        
        addOwnerByName(neko.getName(), owner.getName());
        
        // 触发主人关系变更事件
        try {
            OwnerRelationshipEvent event = new OwnerRelationshipEvent(neko.getName(), owner.getName(), OwnerRelationshipEvent.RelationshipAction.ADD);
            Bukkit.getPluginManager().callEvent(event);
        } catch (Exception e) {
            plugin.getLogger().warning("触发主人关系变更事件失败: " + e.getMessage());
        }
    }

    public void addOwnerByName(String nekoName, String ownerName) {
        if (nekoName == null || ownerName == null) {
            return;
        }
        
        try {
            String finalNekoName = nekoName.toLowerCase();
            String finalOwnerName = ownerName.toLowerCase();
            
            // 更新缓存
            nekoOwnersCache.computeIfAbsent(finalNekoName, k -> new HashSet<>()).add(finalOwnerName);

            // 异步执行数据库操作
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (!safeDatabaseOperation(() -> {
                    String sql = "INSERT OR IGNORE INTO neko_owners (neko_name, owner_name) VALUES (?, ?)";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setString(1, finalNekoName);
                        pstmt.setString(2, finalOwnerName);
                        pstmt.executeUpdate();
                    }
                    return true;
                }, "添加主人关系失败")) {
                    // 数据库操作失败，关系仍保存在内存中
                    plugin.getLogger().info("主人关系已保存到内存");
                }
            });
        } catch (Exception e) {
            plugin.getLogger().warning("添加主人关系时发生异常: " + e.getMessage());
        }
    }

    public Set<Player> getOwners(Player neko) {
        if (neko == null) {
            return new HashSet<>();
        }
        
        try {
            String nekoName = neko.getName().toLowerCase();
            Set<String> ownerNames = nekoOwnersCache.getOrDefault(nekoName, new HashSet<>());
            
            // 转换为Player对象
            Set<Player> owners = new HashSet<>();
            for (String ownerName : ownerNames) {
                Player owner = Bukkit.getPlayer(ownerName);
                if (owner != null) {
                    owners.add(owner);
                }
            }
            
            return owners;
        } catch (Exception e) {
            plugin.getLogger().warning("获取主人列表时发生异常: " + e.getMessage());
            return new HashSet<>();
        }
    }

    public boolean isOwner(Player owner, Player neko) {
        if (owner == null || neko == null) {
            return false;
        }
        
        try {
            Set<Player> owners = getOwners(neko);
            return owners.contains(owner);
        } catch (Exception e) {
            plugin.getLogger().warning("检查主人关系时发生异常: " + e.getMessage());
            return false;
        }
    }

    public void sendOwnerRequest(Player requester, Player neko) {
        if (requester == null || neko == null) {
            return;
        }
        
        try {
            String finalRequesterName = requester.getName().toLowerCase();
            String finalNekoName = neko.getName().toLowerCase();
            
            // 更新缓存
            ownerRequestsCache.computeIfAbsent(finalRequesterName, k -> new HashSet<>()).add(finalNekoName);

            // 触发主人关系变更事件
            try {
                OwnerRelationshipEvent event = new OwnerRelationshipEvent(neko.getName(), requester.getName(), OwnerRelationshipEvent.RelationshipAction.REQUEST);
                Bukkit.getPluginManager().callEvent(event);
            } catch (Exception e) {
                plugin.getLogger().warning("触发主人申请事件失败: " + e.getMessage());
            }

            // 异步执行数据库操作
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (!safeDatabaseOperation(() -> {
                    String sql = "INSERT OR IGNORE INTO owner_requests (requester_name, neko_name) VALUES (?, ?)";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setString(1, finalRequesterName);
                        pstmt.setString(2, finalNekoName);
                        pstmt.executeUpdate();
                    }
                    return true;
                }, "发送主人申请失败")) {
                    // 数据库操作失败，申请仍保存在内存中
                    plugin.getLogger().info("主人申请已保存到内存");
                }
            });
        } catch (Exception e) {
            plugin.getLogger().warning("发送主人申请时发生异常: " + e.getMessage());
        }
    }

    public boolean hasOwnerRequest(Player requester, Player neko) {
        if (requester == null || neko == null) {
            return false;
        }
        
        try {
            String requesterName = requester.getName().toLowerCase();
            String nekoName = neko.getName().toLowerCase();
            
            Set<String> requests = ownerRequestsCache.getOrDefault(requesterName, new HashSet<>());
            return requests.contains(nekoName);
        } catch (Exception e) {
            plugin.getLogger().warning("检查主人申请时发生异常: " + e.getMessage());
            return false;
        }
    }

    public void acceptOwnerRequest(Player requester, Player neko) {
        if (requester == null || neko == null) {
            return;
        }
        
        try {
            // 添加主人关系
            addOwner(neko, requester);
            
            // 移除申请
            denyOwnerRequest(requester, neko);
            
            // 触发主人关系变更事件
            try {
                OwnerRelationshipEvent event = new OwnerRelationshipEvent(neko.getName(), requester.getName(), OwnerRelationshipEvent.RelationshipAction.ADD);
                Bukkit.getPluginManager().callEvent(event);
            } catch (Exception e) {
                plugin.getLogger().warning("触发接受主人申请事件失败: " + e.getMessage());
            }
        } catch (Exception e) {
            plugin.getLogger().warning("接受主人申请时发生异常: " + e.getMessage());
        }
    }

    public void denyOwnerRequest(Player requester, Player neko) {
        if (requester == null || neko == null) {
            return;
        }
        
        try {
            String finalRequesterName = requester.getName().toLowerCase();
            String finalNekoName = neko.getName().toLowerCase();
            
            // 更新缓存
            Set<String> requests = ownerRequestsCache.get(finalRequesterName);
            if (requests != null) {
                requests.remove(finalNekoName);
            }

            // 异步执行数据库操作
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (!safeDatabaseOperation(() -> {
                    String sql = "DELETE FROM owner_requests WHERE requester_name = ? AND neko_name = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setString(1, finalRequesterName);
                        pstmt.setString(2, finalNekoName);
                        pstmt.executeUpdate();
                    }
                    return true;
                }, "删除主人申请失败")) {
                    // 数据库操作失败，申请仍从内存中移除
                    plugin.getLogger().info("主人申请已从内存中删除");
                }
            });
        } catch (Exception e) {
            plugin.getLogger().warning("拒绝主人申请时发生异常: " + e.getMessage());
        }
    }

    public void removeOwner(Player neko, Player owner) {
        if (neko == null || owner == null) {
            return;
        }
        
        try {
            String finalNekoName = neko.getName().toLowerCase();
            String finalOwnerName = owner.getName().toLowerCase();
            
            // 更新缓存
            Set<String> owners = nekoOwnersCache.get(finalNekoName);
            if (owners != null) {
                owners.remove(finalOwnerName);
            }

            // 触发主人关系变更事件
            try {
                OwnerRelationshipEvent event = new OwnerRelationshipEvent(neko.getName(), owner.getName(), OwnerRelationshipEvent.RelationshipAction.REMOVE);
                Bukkit.getPluginManager().callEvent(event);
            } catch (Exception e) {
                plugin.getLogger().warning("触发删除主人关系事件失败: " + e.getMessage());
            }

            // 异步执行数据库操作
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (!safeDatabaseOperation(() -> {
                    String sql = "DELETE FROM neko_owners WHERE neko_name = ? AND owner_name = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setString(1, finalNekoName);
                        pstmt.setString(2, finalOwnerName);
                        pstmt.executeUpdate();
                    }
                    return true;
                }, "删除主人关系失败")) {
                    // 数据库操作失败，关系仍从内存中移除
                    plugin.getLogger().info("主人关系已从内存中删除");
                }
            });
        } catch (Exception e) {
            plugin.getLogger().warning("删除主人关系时发生异常: " + e.getMessage());
        }
    }

    private boolean safeDatabaseOperation(DatabaseOperation operation, String errorMessage) {
        if (memoryMode || connection == null) {
            return false;
        }
        
        try {
            return operation.execute();
        } catch (SQLException e) {
            plugin.getLogger().severe(errorMessage + ": " + e.getMessage());
            return false;
        } catch (Exception e) {
            plugin.getLogger().warning(errorMessage + "（异常）: " + e.getMessage());
            return false;
        }
    }

    @FunctionalInterface
    private interface DatabaseOperation {
        boolean execute() throws SQLException;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                plugin.getLogger().info("数据库连接已关闭");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("关闭数据库连接失败: " + e.getMessage());
        }
    }

    // 以下是为了兼容原版本的API方法
    public Set<Player> getNekosByOwner(Player owner) {
        if (owner == null) {
            return new HashSet<>();
        }
        
        try {
            String ownerName = owner.getName().toLowerCase();
            Set<Player> nekos = new HashSet<>();
            
            // 遍历所有缓存，找到属于该主人的猫娘
            for (Map.Entry<String, Set<String>> entry : nekoOwnersCache.entrySet()) {
                if (entry.getValue().contains(ownerName)) {
                    Player neko = Bukkit.getPlayer(entry.getKey());
                    if (neko != null) {
                        nekos.add(neko);
                    }
                }
            }
            
            return nekos;
        } catch (Exception e) {
            plugin.getLogger().warning("获取猫娘列表时发生异常: " + e.getMessage());
            return new HashSet<>();
        }
    }

    public Set<String> getAllNekoNames() {
        try {
            Set<String> nekoNames = new HashSet<>();
            for (Map.Entry<String, Boolean> entry : isNekoCache.entrySet()) {
                if (entry.getValue()) {
                    nekoNames.add(entry.getKey());
                }
            }
            return nekoNames;
        } catch (Exception e) {
            plugin.getLogger().warning("获取所有猫娘名时发生异常: " + e.getMessage());
            return new HashSet<>();
        }
    }

    public boolean hasOwner(String playerName) {
        if (playerName == null) {
            return false;
        }
        
        try {
            String normalizedName = playerName.toLowerCase();
            Set<String> owners = nekoOwnersCache.get(normalizedName);
            return owners != null && !owners.isEmpty();
        } catch (Exception e) {
            plugin.getLogger().warning("检查是否有主人时发生异常: " + e.getMessage());
            return false;
        }
    }

    public Set<String> getOwnerNames(String nekoName) {
        if (nekoName == null) {
            return new HashSet<>();
        }
        
        try {
            String normalizedName = nekoName.toLowerCase();
            Set<String> owners = nekoOwnersCache.get(normalizedName);
            return owners != null ? new HashSet<>(owners) : new HashSet<>();
        } catch (Exception e) {
            plugin.getLogger().warning("获取主人名列表时发生异常: " + e.getMessage());
            return new HashSet<>();
        }
    }

    public Set<Player> getOwnerRequests(Player neko) {
        if (neko == null) {
            return new HashSet<>();
        }
        
        try {
            String nekoName = neko.getName().toLowerCase();
            Set<Player> requesters = new HashSet<>();
            
            // 遍历所有申请，找到申请该猫娘的玩家
            for (Map.Entry<String, Set<String>> entry : ownerRequestsCache.entrySet()) {
                if (entry.getValue().contains(nekoName)) {
                    Player requester = Bukkit.getPlayer(entry.getKey());
                    if (requester != null) {
                        requesters.add(requester);
                    }
                }
            }
            
            return requesters;
        } catch (Exception e) {
            plugin.getLogger().warning("获取主人申请列表时发生异常: " + e.getMessage());
            return new HashSet<>();
        }
    }
    
    // 新增直接设置玩家通知状态的方法（不触发事件）
    public void setNoticeEnabledDirect(String playerName, boolean enabled) {
        final String finalPlayerName = playerName.toLowerCase();
        noticeEnabledCache.put(finalPlayerName, enabled);

        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (!safeDatabaseOperation(() -> {
                String sql = "INSERT OR REPLACE INTO player_configs (player_name, notice_enabled, is_neko) VALUES (?, ?, " +
                        "(SELECT is_neko FROM player_configs WHERE player_name = ? UNION SELECT 0 WHERE NOT EXISTS (SELECT 1 FROM player_configs WHERE player_name = ?)));";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalPlayerName);
                    pstmt.setInt(2, enabled ? 1 : 0);
                    pstmt.setString(3, finalPlayerName);
                    pstmt.setString(4, finalPlayerName);
                    pstmt.executeUpdate();
                }
                return true;
            }, "设置玩家通知状态失败")) {
                // 数据库操作失败，数据仍保存在内存中
                plugin.getLogger().info("通知状态已保存到内存");
            }
        });
    }
    
    // 新增直接设置玩家为猫娘的方法（不触发事件）
    public void setNekoDirect(String playerName, boolean isNeko) {
        final String finalPlayerName = playerName.toLowerCase();
        isNekoCache.put(finalPlayerName, isNeko);

        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (!safeDatabaseOperation(() -> {
                String sql = "INSERT OR REPLACE INTO player_configs (player_name, notice_enabled, is_neko) VALUES (?, " +
                        "(SELECT notice_enabled FROM player_configs WHERE player_name = ? UNION SELECT 1 WHERE NOT EXISTS (SELECT 1 FROM player_configs WHERE player_name = ?)), ?);";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalPlayerName);
                    pstmt.setString(2, finalPlayerName);
                    pstmt.setString(3, finalPlayerName);
                    pstmt.setInt(4, isNeko ? 1 : 0);
                    pstmt.executeUpdate();
                }
                return true;
            }, "设置玩家猫娘状态失败")) {
                // 数据库操作失败，数据仍保存在内存中
                plugin.getLogger().info("猫娘状态已保存到内存");
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
            if (!safeDatabaseOperation(() -> {
                String sql = "INSERT OR IGNORE INTO neko_owners (neko_name, owner_name) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalNekoName);
                    pstmt.setString(2, finalOwnerName);
                    pstmt.executeUpdate();
                }
                return true;
            }, "添加主人关系失败")) {
                // 数据库操作失败，关系仍保存在内存中
                plugin.getLogger().info("主人关系已保存到内存");
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
            if (!safeDatabaseOperation(() -> {
                String sql = "DELETE FROM neko_owners WHERE neko_name = ? AND owner_name = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalNekoName);
                    pstmt.setString(2, finalOwnerName);
                    pstmt.executeUpdate();
                }
                return true;
            }, "移除主人关系失败")) {
                // 数据库操作失败，关系仍从内存中移除
                plugin.getLogger().info("主人关系已从内存中删除");
            }
        });
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
}