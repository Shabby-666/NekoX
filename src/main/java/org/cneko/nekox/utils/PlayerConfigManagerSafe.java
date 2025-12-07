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
    private final LanguageManager languageManager;
    private Connection connection;
    private final Map<String, Boolean> noticeEnabledCache = new HashMap<>();
    private final Map<String, Boolean> isNekoCache = new HashMap<>();
    private final Map<String, Set<String>> nekoOwnersCache = new HashMap<>(); // 猫娘与其主人的映射
    private final Map<String, Set<String>> ownerRequestsCache = new HashMap<>(); // 主人申请的映射（申请者 -> 被申请的猫娘）
    private final Map<String, Boolean> tailPullEnabledCache = new HashMap<>(); // 尾巴拉扯功能开关状态
    private boolean memoryMode = false;

    public PlayerConfigManagerSafe(NekoX plugin) {
        this.plugin = plugin;
        this.languageManager = plugin.getLanguageManager(); // 从plugin获取已初始化的语言管理器
        this.connection = null; // 初始化为null
        
        try {
            initDatabase();
            if (connection != null && !connection.isClosed()) {
                loadAllConfigs();
                loadAllNekoData();
                memoryMode = false;
            } else {
                plugin.getLogger().warning(languageManager.getMessage("database.connection_failed_memory"));
                initializeMemoryMode();
                memoryMode = true;
            }
        } catch (Exception e) {
            plugin.getLogger().severe(languageManager.getMessage("database.init_manager_failed") + e.getMessage());
            e.printStackTrace();
            plugin.getLogger().warning(languageManager.getMessage("database.using_memory_mode"));
            initializeMemoryMode();
            memoryMode = true;
        }
    }

    private void initDatabase() {
        try {
            // 检查SQLite驱动是否可用
            try {
                Class.forName("org.sqlite.JDBC");
                plugin.getLogger().info(languageManager.getMessage("database.sqlite_driver_loaded"));
            } catch (ClassNotFoundException e) {
                plugin.getLogger().severe(languageManager.getMessage("database.sqlite_driver_not_found"));
                connection = null;
                return;
            }

            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("path", dataFolder.getAbsolutePath());
                plugin.getLogger().info(languageManager.getMessage("database.creating_data_folder", placeholders));
            }

            File dbFile = new File(dataFolder, "PlayerConfigs.db");
            Map<String, String> pathPlaceholders = new HashMap<>();
            pathPlaceholders.put("path", dbFile.getAbsolutePath());
            plugin.getLogger().info(languageManager.getMessage("database.file_path", pathPlaceholders));
            
            // 检查数据库文件是否可写
            if (dbFile.exists() && !dbFile.canWrite()) {
                plugin.getLogger().severe(languageManager.getMessage("database.file_not_writable", pathPlaceholders));
                connection = null;
                return;
            }

            String dbUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            Map<String, String> urlPlaceholders = new HashMap<>();
            urlPlaceholders.put("url", dbUrl);
            plugin.getLogger().info(languageManager.getMessage("database.connecting", urlPlaceholders));
            
            connection = DriverManager.getConnection(dbUrl);

            if (connection != null && !connection.isClosed()) {
                plugin.getLogger().info(languageManager.getMessage("database.connected"));

                // 启用外键约束
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON");
                    plugin.getLogger().info(languageManager.getMessage("database.foreign_keys_enabled"));
                }

                // 创建玩家配置表
                String createTableSQL = "CREATE TABLE IF NOT EXISTS player_configs (" +
                        "player_name TEXT PRIMARY KEY, " +
                        "notice_enabled INTEGER DEFAULT 1, " +
                        "is_neko INTEGER DEFAULT 0, " +
                        "tail_pull_enabled INTEGER DEFAULT 1);";
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(createTableSQL);
                    Map<String, String> tablePlaceholders = new HashMap<>();
                    tablePlaceholders.put("table", "player_configs");
                    plugin.getLogger().info(languageManager.getMessage("database.table_created", tablePlaceholders));
                }

                // 创建猫娘主人关系表
                String createOwnersTableSQL = "CREATE TABLE IF NOT EXISTS neko_owners (" +
                        "neko_name TEXT, " +
                        "owner_name TEXT, " +
                        "PRIMARY KEY (neko_name, owner_name));";
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(createOwnersTableSQL);
                    Map<String, String> ownersTablePlaceholders = new HashMap<>();
                    ownersTablePlaceholders.put("table", "neko_owners");
                    plugin.getLogger().info(languageManager.getMessage("database.table_created", ownersTablePlaceholders));
                }

                // 创建主人申请关系表
                String createRequestsTableSQL = "CREATE TABLE IF NOT EXISTS owner_requests (" +
                        "requester_name TEXT, " +
                        "neko_name TEXT, " +
                        "PRIMARY KEY (requester_name, neko_name));";
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(createRequestsTableSQL);
                    Map<String, String> requestsTablePlaceholders = new HashMap<>();
                    requestsTablePlaceholders.put("table", "owner_requests");
                    plugin.getLogger().info(languageManager.getMessage("database.table_created", requestsTablePlaceholders));
                }
                
                // 检查并修复 player_configs 表的列结构
                checkAndFixPlayerConfigsTable();
                
                plugin.getLogger().info(languageManager.getMessage("database.initialized"));
            } else {
                plugin.getLogger().severe(languageManager.getMessage("database.connection_failed"));
                connection = null;
            }
        } catch (SQLException e) {
            plugin.getLogger().severe(languageManager.getMessage("database.sql_error") + e.getMessage());
            Map<String, String> codePlaceholders = new HashMap<>();
            codePlaceholders.put("code", String.valueOf(e.getErrorCode()));
            plugin.getLogger().severe(languageManager.getMessage("database.error_code", codePlaceholders));
            Map<String, String> statePlaceholders = new HashMap<>();
            statePlaceholders.put("state", e.getSQLState());
            plugin.getLogger().severe(languageManager.getMessage("database.sql_state", statePlaceholders));
            connection = null;
        } catch (Exception e) {
            Map<String, String> exceptionPlaceholders = new HashMap<>();
            exceptionPlaceholders.put("exception", e.getClass().getSimpleName());
            exceptionPlaceholders.put("message", e.getMessage());
            plugin.getLogger().severe(languageManager.getMessage("database.init_exception", exceptionPlaceholders));
            e.printStackTrace();
            connection = null;
        }
    }
    
    /**
     * 检查并修复 player_configs 表的列结构
     */
    private void checkAndFixPlayerConfigsTable() {
        try {
            // 定义需要检查的列及其默认值
            Map<String, String> requiredColumns = new HashMap<>();
            requiredColumns.put("notice_enabled", "INTEGER DEFAULT 1");
            requiredColumns.put("is_neko", "INTEGER DEFAULT 0");
            requiredColumns.put("tail_pull_enabled", "INTEGER DEFAULT 1");
            
            // 获取表中已存在的列
            Set<String> existingColumns = new HashSet<>();
            try (ResultSet rs = connection.getMetaData().getColumns(null, null, "player_configs", null)) {
                while (rs.next()) {
                    existingColumns.add(rs.getString("COLUMN_NAME").toLowerCase());
                }
            }
            
            // 检查并添加缺失的列
            for (Map.Entry<String, String> entry : requiredColumns.entrySet()) {
                String columnName = entry.getKey();
                String columnDefinition = entry.getValue();
                
                if (!existingColumns.contains(columnName.toLowerCase())) {
                    // 添加缺失的列
                    String alterSQL = "ALTER TABLE player_configs ADD COLUMN " + columnName + " " + columnDefinition;
                    try (Statement stmt = connection.createStatement()) {
                        stmt.execute(alterSQL);
                        Map<String, String> columnPlaceholders = new HashMap<>();
                        columnPlaceholders.put("column", columnName);
                        plugin.getLogger().info(languageManager.getMessage("database.column_added", columnPlaceholders));
                    }
                }
            }
            
        } catch (SQLException e) {
            Map<String, String> errorPlaceholders = new HashMap<>();
            errorPlaceholders.put("message", e.getMessage());
            plugin.getLogger().warning(languageManager.getMessage("database.table_check_error", errorPlaceholders));
        }
    }
    
    /**
     * 初始化内存模式，当数据库不可用时使用
     */
    private void initializeMemoryMode() {
        plugin.getLogger().info(languageManager.getMessage("database.memory_mode_initialized"));
        // 清空缓存，使用内存存储
        noticeEnabledCache.clear();
        isNekoCache.clear();
        nekoOwnersCache.clear();
        ownerRequestsCache.clear();
        tailPullEnabledCache.clear();
        memoryMode = true;
    }

    /**
     * 加载所有玩家配置到缓存
     */
    private void loadAllConfigs() {
        // 异步执行数据库操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (memoryMode || connection == null) {
                plugin.getLogger().info(languageManager.getMessage("database.skip_loading"));
                return;
            }
            
            safeDatabaseOperation(() -> {
                String sql = "SELECT player_name, notice_enabled, is_neko, tail_pull_enabled FROM player_configs";
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    int count = 0;
                    while (rs.next()) {
                        String playerName = rs.getString("player_name").toLowerCase();
                        boolean enabled = rs.getInt("notice_enabled") == 1;
                        boolean isNeko = rs.getInt("is_neko") == 1;
                        boolean tailPullEnabled = rs.getInt("tail_pull_enabled") == 1;
                        noticeEnabledCache.put(playerName, enabled);
                        isNekoCache.put(playerName, isNeko);
                        tailPullEnabledCache.put(playerName, tailPullEnabled);
                        count++;
                    }
                    Map<String, String> countPlaceholders = new HashMap<>();
                    countPlaceholders.put("count", String.valueOf(count));
                    plugin.getLogger().info(languageManager.getMessage("database.loaded_configs", countPlaceholders));
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
                plugin.getLogger().info(languageManager.getMessage("database.skip_loading"));
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
                    Map<String, String> ownerCountPlaceholders = new HashMap<>();
                    ownerCountPlaceholders.put("count", String.valueOf(ownerCount));
                    plugin.getLogger().info(languageManager.getMessage("database.loaded_owner_relations", ownerCountPlaceholders));
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
                    Map<String, String> requestCountPlaceholders = new HashMap<>();
                    requestCountPlaceholders.put("count", String.valueOf(requestCount));
                    plugin.getLogger().info(languageManager.getMessage("database.loaded_requests", requestCountPlaceholders));
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
                    plugin.getLogger().info(languageManager.getMessage("database.operation_failed"));
                }
            });
        } catch (Exception e) {
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "设置通知状态时发生异常: " + e.getMessage());
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
                plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "检查通知状态时发生异常: " + e.getMessage());
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
                    plugin.getLogger().info(languageManager.getMessage("database.operation_failed"));
                }
            });
        } catch (Exception e) {
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "设置猫娘状态时发生异常: " + e.getMessage());
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
                    plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "加载玩家配置失败: " + e.getMessage());
                    isNekoCache.put(playerName, false);
                }
            }
            
            // 返回缓存中的值，如果没有则默认为false
            return isNekoCache.getOrDefault(playerName, false);
        } catch (Exception e) {
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "检查玩家猫娘状态失败: " + e.getMessage());
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
                    plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "加载玩家配置失败: " + e.getMessage());
                    isNekoCache.put(normalizedName, false);
                }
            }
            
            // 返回缓存中的值，如果没有则默认为false
            return isNekoCache.getOrDefault(normalizedName, false);
        } catch (Exception e) {
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "检查玩家猫娘状态失败: " + e.getMessage());
            return false;
        }
    }

    private void loadPlayerConfig(String playerName) {
        safeDatabaseOperation(() -> {
            String sql = "SELECT is_neko, tail_pull_enabled FROM player_configs WHERE player_name = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, playerName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    boolean isNeko = rs.getInt("is_neko") == 1;
                    boolean tailPullEnabled = rs.getInt("tail_pull_enabled") == 1;
                    isNekoCache.put(playerName, isNeko);
                    tailPullEnabledCache.put(playerName, tailPullEnabled);
                } else {
                    isNekoCache.put(playerName, false);
                    tailPullEnabledCache.put(playerName, true); // 默认开启
                }
            }
            return true;
        }, "加载玩家配置失败");
    }
    
    /**
     * 设置玩家的尾巴拉扯功能开关状态
     */
    public void setTailPullEnabled(Player player, boolean enabled) {
        if (player == null) {
            return;
        }
        
        try {
            final String finalPlayerName = player.getName().toLowerCase();
            tailPullEnabledCache.put(finalPlayerName, enabled);

            // 异步执行数据库操作
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (!safeDatabaseOperation(() -> {
                    String sql = "INSERT OR REPLACE INTO player_configs (player_name, tail_pull_enabled, notice_enabled, is_neko) VALUES (?, ?, " +
                            "(SELECT notice_enabled FROM player_configs WHERE player_name = ? UNION SELECT 1 WHERE NOT EXISTS (SELECT 1 FROM player_configs WHERE player_name = ?)), " +
                            "(SELECT is_neko FROM player_configs WHERE player_name = ? UNION SELECT 0 WHERE NOT EXISTS (SELECT 1 FROM player_configs WHERE player_name = ?)))";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setString(1, finalPlayerName);
                        pstmt.setInt(2, enabled ? 1 : 0);
                        pstmt.setString(3, finalPlayerName);
                        pstmt.setString(4, finalPlayerName);
                        pstmt.setString(5, finalPlayerName);
                        pstmt.setString(6, finalPlayerName);
                        pstmt.executeUpdate();
                    }
                    return true;
                }, "设置玩家尾巴拉扯功能开关状态失败")) {
                    // 数据库操作失败，数据仍保存在内存中
                    plugin.getLogger().info(languageManager.getMessage("database.operation_failed"));
                }
            });
        } catch (Exception e) {
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "设置尾巴拉扯功能开关状态时发生异常: " + e.getMessage());
        }
    }
    
    /**
     * 检查玩家的尾巴拉扯功能是否开启
     */
    public boolean isTailPullEnabled(Player player) {
        if (player == null) {
            return true; // 默认开启
        }
        
        try {
            String playerName = player.getName().toLowerCase();
            
            // 首先检查缓存
            if (tailPullEnabledCache.containsKey(playerName)) {
                return tailPullEnabledCache.get(playerName);
            }
            
            // 如果是内存模式，直接返回默认值
            if (memoryMode) {
                tailPullEnabledCache.put(playerName, true); // 默认开启
                return true;
            }
            
            // 如果数据库连接可用，从数据库加载
            if (connection != null) {
                try {
                    loadPlayerTailPullConfig(playerName);
                } catch (Exception e) {
                    plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "加载玩家尾巴拉扯配置失败: " + e.getMessage());
                    tailPullEnabledCache.put(playerName, true); // 默认开启
                }
            }
            
            // 返回缓存中的值，如果没有则默认为true
            return tailPullEnabledCache.getOrDefault(playerName, true);
        } catch (Exception e) {
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "检查玩家尾巴拉扯功能开关状态失败: " + e.getMessage());
            return true; // 默认开启
        }
    }
    
    private void loadPlayerTailPullConfig(String playerName) {
        safeDatabaseOperation(() -> {
            String sql = "SELECT tail_pull_enabled FROM player_configs WHERE player_name = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, playerName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    boolean enabled = rs.getInt("tail_pull_enabled") == 1;
                    tailPullEnabledCache.put(playerName, enabled);
                } else {
                    tailPullEnabledCache.put(playerName, true); // 默认开启
                }
            }
            return true;
        }, "加载玩家尾巴拉扯配置失败");
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
                plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "触发主人关系变更事件失败: " + e.getMessage());
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
                    plugin.getLogger().info(languageManager.getMessage("database.operation_failed"));
                }
            });
        } catch (Exception e) {
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "添加主人关系时发生异常: " + e.getMessage());
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
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "获取主人列表时发生异常: " + e.getMessage());
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
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "检查主人关系时发生异常: " + e.getMessage());
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
                plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "触发主人申请事件失败: " + e.getMessage());
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
                    plugin.getLogger().info(languageManager.getMessage("database.operation_failed"));
                }
            });
        } catch (Exception e) {
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "发送主人申请时发生异常: " + e.getMessage());
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
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "检查主人申请时发生异常: " + e.getMessage());
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
                plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "触发接受主人申请事件失败: " + e.getMessage());
            }
        } catch (Exception e) {
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "接受主人申请时发生异常: " + e.getMessage());
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
                    plugin.getLogger().info(languageManager.getMessage("database.operation_failed"));
                }
            });
        } catch (Exception e) {
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "拒绝主人申请时发生异常: " + e.getMessage());
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
                plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "触发删除主人关系事件失败: " + e.getMessage());
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
                    plugin.getLogger().info(languageManager.getMessage("database.operation_failed"));
                }
            });
        } catch (Exception e) {
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "删除主人关系时发生异常: " + e.getMessage());
        }
    }

    private boolean safeDatabaseOperation(DatabaseOperation operation, String errorMessage) {
        if (memoryMode || connection == null) {
            return false;
        }
        
        try {
            return operation.execute();
        } catch (SQLException e) {
            plugin.getLogger().severe("§dNekoX §e>> " + errorMessage + ": " + e.getMessage());
            return false;
        } catch (Exception e) {
            plugin.getLogger().warning("§dNekoX §e>> " + errorMessage + "（异常）: " + e.getMessage());
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
                plugin.getLogger().info(languageManager.getMessage("database.connection_closed"));
            }
        } catch (SQLException e) {
            Map<String, String> errorPlaceholders = new HashMap<>();
            errorPlaceholders.put("message", e.getMessage());
            plugin.getLogger().severe(languageManager.getMessage("database.close_failed", errorPlaceholders));
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
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "Failed to get nekos by owner: " + e.getMessage());
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
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "Failed to get all neko names: " + e.getMessage());
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
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "Failed to check if player has owner: " + e.getMessage());
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
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "Failed to get owner names: " + e.getMessage());
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
            plugin.getLogger().warning(languageManager.getMessage("plugin.startup_error") + "Failed to get owner requests: " + e.getMessage());
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
            }, "Failed to set player notice status")) {
                // 数据库操作失败，数据仍保存在内存中
                plugin.getLogger().info(languageManager.getMessage("database.operation_failed"));
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
                String sql = "INSERT OR REPLACE INTO player_configs (player_name, notice_enabled, is_neko, tail_pull_enabled) VALUES (?, " +
                        "(SELECT notice_enabled FROM player_configs WHERE player_name = ? UNION SELECT 1 WHERE NOT EXISTS (SELECT 1 FROM player_configs WHERE player_name = ?)), ?, " +
                        "(SELECT tail_pull_enabled FROM player_configs WHERE player_name = ? UNION SELECT 1 WHERE NOT EXISTS (SELECT 1 FROM player_configs WHERE player_name = ?)));";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, finalPlayerName);
                    pstmt.setString(2, finalPlayerName);
                    pstmt.setString(3, finalPlayerName);
                    pstmt.setInt(4, isNeko ? 1 : 0);
                    pstmt.setString(5, finalPlayerName);
                    pstmt.setString(6, finalPlayerName);
                    pstmt.executeUpdate();
                }
                return true;
            }, "Failed to set player neko status")) {
                // 数据库操作失败，数据仍保存在内存中
                plugin.getLogger().info(languageManager.getMessage("database.operation_failed"));
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
            }, "Failed to add owner relationship")) {
                // 数据库操作失败，关系仍保存在内存中
                plugin.getLogger().info(languageManager.getMessage("database.operation_failed"));
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
                    plugin.getLogger().info(languageManager.getMessage("database.operation_failed"));
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