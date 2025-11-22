# NekoX 多语言功能使用指南

## 目录
- [English](#english)
- [中文](#中文)

---

## English

### How Language System Works
NekoX plugin supports multiple languages through YAML configuration files. By default, it comes with English and Simplified Chinese language files. You can easily add new languages by following the instructions below.

### Language File Structure
- Language files are stored in the `Languages` folder inside the plugin directory
- Each language has its own YAML file named after the language (e.g., `English.yml`, `简体中文.yml`)
- The main configuration file `Language.yml` in the plugin directory stores the current language and the list of available languages

### Available Languages
By default, NekoX comes with these languages:
- English
- Simplified Chinese

### How to Switch Languages
You can change the plugin language using the in-game command:
```
/nekox language <language>
```
For example:
```
/nekox language English
/nekox language 简体中文
```

### How to Add a New Custom Language
Follow these steps to add a new language to the plugin:

#### Step 1: Create a New Language File
1. Go to the `Languages` folder in the NekoX plugin directory
2. Create a new YAML file with the language name (e.g., `Español.yml` for Spanish, `日本語.yml` for Japanese)

#### Step 2: Copy and Translate Language Keys
1. Open an existing language file (like `English.yml`) as a reference
2. Copy all the key-value pairs into your new language file
3. Translate the values to your target language while keeping the keys unchanged

#### Step 3: Update Language Configuration
1. Open `Language.yml` in the plugin directory
2. Add your new language name to the `Languages` list
3. Save the file

#### Step 4: Reload the Plugin
1. Use the command `/nekox reload` to apply the changes
2. Your new language should now be available

### Language File Example Structure
Here's a small example of what a language file looks like:
```
# Basic plugin messages
plugin.enabled: "NekoX plugin has been successfully enabled!"
plugin.disabled: "NekoX plugin has been successfully disabled!"
plugin.name: "NekoX"
plugin.version: "Version: 5.0-ProMax+++"

# Command messages
commands.pat.success: "You petted %player% affectionately!"
commands.lovebite.success: "You gave %player% a love bite!"
commands.earscratch.success: "You scratched %player%'s ears!"
# ... and many more keys
```

### Placeholder Variables
Some language strings contain placeholder variables (e.g., `%player%`, `%time%`). These will be replaced with actual values when the message is displayed. When translating, make sure to keep these placeholders unchanged.

### Tips for Creating Language Files
- Use a text editor that supports UTF-8 encoding to ensure proper character display
- Keep the original key names intact, only translate the values
- Test your language file in-game to check for any issues
- Share your translations with the community to help others!

---

## 中文

### 语言系统工作原理
NekoX插件通过YAML配置文件支持多种语言。默认情况下，它包含英语和简体中文语言文件。您可以按照以下说明轻松添加新的语言。

### 语言文件结构
- 语言文件存储在插件目录下的`Languages`文件夹中
- 每种语言都有一个以语言名称命名的YAML文件（例如`English.yml`、`简体中文.yml`）
- 插件目录下的主配置文件`Language.yml`存储当前使用的语言和可用语言列表

### 可用语言
默认情况下，NekoX提供以下语言：
- 英语 (English)
- 简体中文

### 如何切换语言
您可以使用游戏内命令更改插件语言：
```
/nekox language <语言>
```
例如：
```
/nekox language English
/nekox language 简体中文
```

### 如何添加新的自定义语言
按照以下步骤向插件添加新语言：

#### 步骤1：创建新的语言文件
1. 进入NekoX插件目录下的`Languages`文件夹
2. 创建一个新的YAML文件，以语言名称命名（例如`Español.yml`用于西班牙语，`日本語.yml`用于日语）

#### 步骤2：复制并翻译语言键
1. 打开现有语言文件（如`English.yml`）作为参考
2. 将所有键值对复制到新的语言文件中
3. 将值翻译成目标语言，同时保持键不变

#### 步骤3：更新语言配置
1. 打开插件目录下的`Language.yml`文件
2. 将新语言名称添加到`Languages`列表中
3. 保存文件

#### 步骤4：重新加载插件
1. 使用命令`/nekox reload`应用更改
2. 新语言现在应该可用

### 语言文件示例结构
以下是语言文件的小示例：
```
# 基本插件消息
plugin.enabled: "NekoX插件已成功启用！"
plugin.disabled: "NekoX插件已成功禁用！"
plugin.name: "NekoX"
plugin.version: "版本: 5.0-ProMax+++"

# 命令消息
commands.pat.success: "你温柔地抚摸了%player%！"
commands.lovebite.success: "你给了%player%一个爱的咬痕！"
commands.earscratch.success: "你挠了挠%player%的耳朵！"
# ... 还有更多键
```

### 占位符变量
一些语言字符串包含占位符变量（如`%player%`、`%time%`）。这些变量在显示消息时会被实际值替换。翻译时，请确保保持这些占位符不变。

### 创建语言文件的提示
- 使用支持UTF-8编码的文本编辑器，以确保正确显示字符
- 保持原始键名不变，只翻译值
- 在游戏中测试语言文件，检查是否有任何问题
- 与社区分享您的翻译，以帮助他人！