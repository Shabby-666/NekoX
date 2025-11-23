# NekoX 完整API使用示例

本文档展示了如何使用NekoX插件提供的完整API来操作猫娘系统的所有功能。

## 1. 获取API实例

```java
import org.cneko.nekox.api.NekoXAPI;
import org.bukkit.plugin.Plugin;

// 在你的插件中获取NekoX API实例
NekoXAPI api = NekoXAPI.getInstance(yourPlugin);
```

## 2. 猫娘状态管理

### 2.1 检查玩家是否为猫娘

```java
// 通过玩家对象检查
boolean isNeko = api.isNeko(player);

// 通过玩家名检查
boolean isNeko = api.isNeko("playerName");
```

### 2.2 设置玩家为猫娘

```java
// 通过玩家对象设置
api.setNeko(player, true);

// 通过玩家名设置
api.setNeko("playerName", true);

// 取消玩家的猫娘身份
api.setNeko("playerName", false);
```

### 2.3 获取猫娘列表

```java
// 获取所有猫娘玩家名（包括离线玩家）
Set<String> allNekoNames = api.getAllNekoNames();

// 获取所有在线猫娘玩家
Set<Player> onlineNekos = api.getNekoPlayers();
```

## 3. 通知状态管理

### 3.1 设置通知状态

```java
// 通过玩家对象设置
api.setNoticeEnabled(player, true);

// 通过玩家名设置
api.setNoticeEnabled("playerName", true);
```

### 3.2 检查通知状态

```java
// 检查玩家的通知状态
boolean isNoticeEnabled = api.isNoticeEnabled(player);
```

## 4. 主人关系管理

### 4.1 添加主人关系

```java
// 通过玩家对象添加
api.addOwner(nekoPlayer, ownerPlayer);

// 通过玩家名添加
api.addOwner("nekoName", "ownerName");
```

### 4.2 移除主人关系

```java
// 通过玩家对象移除
api.removeOwner(nekoPlayer, ownerPlayer);

// 通过玩家名移除
api.removeOwner("nekoName", "ownerName");
```

### 4.3 查询主人关系

```java
// 获取指定猫娘的所有主人名
Set<String> ownerNames = api.getOwnerNames("nekoName");

// 获取指定猫娘的所有主人对象（在线玩家）
Set<Player> owners = api.getOwners(nekoPlayer);

// 检查玩家是否有主人
boolean hasOwner = api.hasOwner("playerName");

// 检查玩家是否是某只猫娘的主人
boolean isOwner = api.isOwnerOf("ownerName", "nekoName");

// 通过玩家对象检查
boolean isOwner = api.isOwner(ownerPlayer, nekoPlayer);
```

### 4.4 获取主人的猫娘

```java
// 获取某个主人的所有猫娘
Set<Player> nekos = api.getNekosByOwner(ownerPlayer);
```

## 5. 主人申请管理

### 5.1 发送主人申请

```java
// 发送主人申请
api.sendOwnerRequest(requesterPlayer, nekoPlayer);
```

### 5.2 检查申请状态

```java
// 检查是否有主人申请
boolean hasRequest = api.hasOwnerRequest(requesterPlayer, nekoPlayer);
```

### 5.3 获取申请列表

```java
// 获取猫娘收到的所有申请
Set<Player> requests = api.getOwnerRequests(nekoPlayer);
```

### 5.4 处理申请

```java
// 接受主人申请
api.acceptOwnerRequest(requesterPlayer, nekoPlayer);

// 拒绝主人申请
api.denyOwnerRequest(requesterPlayer, nekoPlayer);
```

## 6. 直接数据库操作（不触发事件）

这些方法适用于需要批量操作或同步数据的场景，不会触发相关事件。

### 6.1 直接添加主人关系

```java
// 直接向数据库添加主人关系（不触发事件）
api.addOwnerDirect("nekoName", "ownerName");
```

### 6.2 直接移除主人关系

```java
// 直接从数据库移除主人关系（不触发事件）
api.removeOwnerDirect("nekoName", "ownerName");
```

### 6.3 直接设置猫娘状态

```java
// 直接设置玩家为猫娘（不触发事件）
api.setNekoDirect("playerName", true);
```

### 6.4 直接设置通知状态

```java
// 直接设置玩家的通知状态（不触发事件）
api.setNoticeEnabledDirect("playerName", true);
```

## 7. 事件监听

NekoX插件提供了以下事件，您可以在您的插件中监听这些事件：

### 7.1 猫娘状态变更事件

```java
import org.cneko.nekox.api.events.NekoStatusChangeEvent;

@EventHandler
public void onNekoStatusChange(NekoStatusChangeEvent event) {
    Player player = event.getPlayer();
    boolean isNeko = event.isNeko();
    boolean isOnline = event.isOnline();
    
    // 处理猫娘状态变更
}
```

### 7.2 主人关系变更事件

```java
import org.cneko.nekox.api.events.OwnerRelationshipEvent;

@EventHandler
public void onOwnerRelationshipChange(OwnerRelationshipEvent event) {
    String nekoName = event.getNekoName();
    String ownerName = event.getOwnerName();
    OwnerRelationshipEvent.RelationshipAction action = event.getAction();
    
    // 处理主人关系变更
    switch (action) {
        case ADD:
            // 添加了主人关系
            break;
        case REMOVE:
            // 移除了主人关系
            break;
        case REQUEST:
            // 发送了主人申请
            break;
    }
}
```