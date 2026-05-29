# 6s Music Splitter (音频切片工具)

解析度高、体验流畅的音频处理利器。

[![Java](https://img.shields.io/badge/Java-8+-orange.svg?style=flat-square)](https://www.oracle.com/java/)
[![Platform](https://img.shields.io/badge/Platform-Windows-blue.svg?style=flat-square)](https://github.com/babababasb/Audio-Splitter-)
[![License](https://img.shields.io/badge/License-MIT-green.svg?style=flat-square)](LICENSE)

一款极简、高颜值的全格式音频无损切片工具。界面基于 **FlatLaf** 现代暗黑皮肤打造，免配置双击即用！

---

## 🌟 功能特性

* 🎵 **格式全兼容**：支持 MP3, WAV, FLAC, M4A, AAC, OGG 等几乎所有常见音频格式的导入。
* 🖱️ **拖拽导入**：支持将电脑中的音频文件直接拖入软件界面的任意位置，实现一键导入。
* 📂 **自动工程化**：根据您输入的“工程名称”，自动创建独立的工程文件夹保存分割后的所有音频片段。
* ⚙️ **无损/无缝切片**：支持自定义分割秒数，可选择导出为高品质 WAV 格式或保留原始音频格式。
* 🛠️ **一体化集成**：内嵌 FFmpeg 核心引擎，运行时自动静默释放，无需用户手动安装任何环境变量，双击即可直接运行。

---

## 📦 如何下载与使用

### 1. 软件下载
请直接前往本仓库的 [Releases 页面](https://github.com/babababasb/Audio-Splitter-/releases) 下载最新的绿色单文件版 **`AudioSplitter.exe`**。

### 2. 运行说明
* 本软件为**绿色免安装版**，双击直接运行即可。
* 首次运行时，软件会自动在后台释放内嵌的音频引擎，请在看到状态栏显示 **“音频引擎就绪”** 后开始使用。

### 3. 操作步骤
1. **导入音频**：直接将歌曲拖入软件窗口，或者点击 **“浏览”** 按钮手动选择音频文件。
2. **设置路径**：选择分割后的工程文件夹存放位置（输出路径）。
3. **调整参数**：填写工程名称、单段分割秒数，并选择您需要的输出格式（如 WAV 或 保持原格式）。
4. **开始分割**：点击底部的 **“开始分割音频”**，稍等片刻即可在输出目录中看到自动生成的工程文件夹及切片文件。

---

## 🛠️ 开发者指南

如果您想对本项目进行二次开发或自行通过源码编译，请遵循以下步骤：
