
---

```markdown
# 6s Music Splitter (音频切片工具)

![Java](https://img.shields.io/badge/Java-8+-orange.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

一款极简、高颜值的全格式音频无损切片工具。界面基于 FlatLaf 现代暗黑皮肤打造，免配置双击即用！

---

## 🌟 功能特性

- 🎵 **格式全兼容**：支持 MP3, WAV, FLAC, M4A, AAC, OGG 等几乎所有音频格式的导入。
- 🖱️ **拖拽导入**：支持将电脑中的音频文件直接拖入软件界面的任意位置进行导入。
- 📂 **自动工程化**：根据您输入的“工程名称”，自动创建独立的工程文件夹保存分割后的所有音频片段 [1]。
- ⚙️ **无损/无缝切片**：支持自定义分割秒数，可选择导出为高品质 WAV 格式或保留原始格式 [1]。
- 🛠️ **一体化集成**：内嵌 FFmpeg 核心引擎，运行时自动静默释放，无需用户手动安装任何环境，双击即可直接运行。

---

## 📦 如何下载与使用

### 1. 软件下载
请直接前往本仓库的 [Releases 页面](https://github.com/babababasb/Audio-Splitter-/releases) 下载最新的绿色单文件版 **`AudioSplitter.exe`** [1]。

### 2. 运行说明
- 本软件为绿色免安装版，**双击直接运行**即可。
- 如果是首次运行，软件会自动释放内嵌的音频引擎，请在看到状态栏显示“音频引擎就绪”后开始使用。

### 3. 操作步骤
1. **导入音频**：直接将歌曲拖入软件窗口，或者点击 **“浏览”** 手动选择。
2. **设置路径**：选择分割后的工程文件夹存放位置。
3. **调整参数**：填写工程名称、单段分割秒数，并选择你想要的输出格式 [1]。
4. **开始分割**：点击底部的 **“开始分割音频”**，稍等片刻即可在输出目录中看到自动生成的工程文件夹。

---

## 🛠️ 开发者指南 (如果您想自己编译)

如果您想对本项目进行二次开发或自行通过源码编译，请遵循以下步骤：

### 1. 环境准备
- **Java JDK**: 推荐使用 JDK 8 或更高版本（本项目完全兼容 Java 8）。
- **IDE**: 推荐使用 IntelliJ IDEA。
- **构建工具**: Maven 3.x。

### 2. 获取源码与引擎
1. 克隆本项目到本地：
   ```bash
   git clone https://github.com/babababasb/Audio-Splitter-.git
   ```
2. 下载 Windows 版本的 [FFmpeg 运行时](https://ffmpeg.org/download.html)。
3. 在项目内新建目录，将 `ffmpeg.exe` 放入以下路径：
   ```text
   src/main/resources/bin/ffmpeg.exe
   ```

### 3. 编译打包
1. 打开 IDEA 终端，执行以下 Maven 命令：
   ```bash
   mvn clean package
   ```
2. 编译成功后，在 `target/` 目录下会生成带所有依赖的胖 JAR 包 [1]：
   ```text
   target/6s_music-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```
3. 您可以使用 **Launch4j** 工具，将该 JAR 包转换打包为最终的 `.exe` 可执行文件。

---

## 🤝 贡献与致谢

- **UI 皮肤支持**：感谢 [FlatLaf](https://github.com/JFormDesigner/FlatLaf) 提供的现代化暗黑 Swing 主题。
- **核心音频引擎**：感谢 [FFmpeg](https://ffmpeg.org/) 提供的强大音频处理支持。

---

**Developed By WanAn  2026**
```