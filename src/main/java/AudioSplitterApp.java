import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AudioSplitterApp extends JFrame {
    private JTextField txtInputPath;
    private JTextField txtOutputPath;
    private JTextField txtProjName;
    private JTextField txtSeconds;
    private JProgressBar progressBar;
    private JLabel lblStatus;
    private JButton btnStart;
    private JComboBox<String> cbFormat;

    private String extractedFFmpegPath = null;

    public AudioSplitterApp() {
        FlatDarkLaf.setup();
        UIManager.put("defaultFont", new Font("Microsoft YaHei", Font.PLAIN, 13));

        setTitle("Audio Splitter Pro (Embedded Engine)");
        setSize(680, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 【新增】：设置程序窗口和任务栏的图标为“灰色底 + 字母S”
        setIconImage(createAppIcon());

        // --- 标题栏 ---
        JPanel pnlTitle = new JPanel(new GridLayout(2, 1));
        pnlTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        JLabel lblTitle = new JLabel("AUDIO SPLITTER", JLabel.CENTER);
        lblTitle.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        lblTitle.setForeground(new Color(52, 152, 219));

        JLabel lblSub = new JLabel("内嵌集成版 (免安装，支持全格式) • By WanAn  2026", JLabel.CENTER);
        lblSub.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        lblSub.setForeground(Color.GRAY);
        pnlTitle.add(lblTitle);
        pnlTitle.add(lblSub);
        add(pnlTitle, BorderLayout.NORTH);

        // --- 主面板 ---
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // 1. 输入文件
        JPanel row1 = createRowPanel();
        JLabel lblInput = new JLabel("输入音频文件:", JLabel.LEFT);
        lblInput.setPreferredSize(new Dimension(90, 30));
        row1.add(lblInput);
        txtInputPath = new JTextField();
        row1.add(txtInputPath);
        JButton btnBrowseInput = new JButton("浏览");
        btnBrowseInput.addActionListener(e -> selectFile());
        row1.add(btnBrowseInput);
        pnlMain.add(row1);

        // 2. 输出路径
        JPanel row2 = createRowPanel();
        JLabel lblOutput = new JLabel("工程输出路径:", JLabel.LEFT);
        lblOutput.setPreferredSize(new Dimension(90, 30));
        row2.add(lblOutput);
        txtOutputPath = new JTextField();
        row2.add(txtOutputPath);
        JButton btnBrowseOutput = new JButton("浏览");
        btnBrowseOutput.addActionListener(e -> selectDirectory());
        row2.add(btnBrowseOutput);
        pnlMain.add(row2);

        // 3. 参数设置
        JPanel row3 = createRowPanel();
        JLabel lblProj = new JLabel("工程名称:");
        lblProj.setPreferredSize(new Dimension(90, 30));
        row3.add(lblProj);
        txtProjName = new JTextField("My_Split_Project");
        txtProjName.setPreferredSize(new Dimension(150, 30));
        row3.add(txtProjName);

        row3.add(Box.createHorizontalStrut(15));

        row3.add(new JLabel("单段秒数:"));
        txtSeconds = new JTextField("30");
        txtSeconds.setPreferredSize(new Dimension(50, 30));
        row3.add(txtSeconds);

        row3.add(Box.createHorizontalStrut(15));

        row3.add(new JLabel("输出格式:"));
        cbFormat = new JComboBox<>(new String[]{"WAV (推荐无损)", "同输入源格式"});
        cbFormat.setPreferredSize(new Dimension(120, 30));
        row3.add(cbFormat);
        pnlMain.add(row3);

        // 4. 进度条和状态
        JPanel pnlProgress = new JPanel(new BorderLayout());
        pnlProgress.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        lblStatus = new JLabel("正在初始化内嵌音频引擎，请稍候...");
        lblStatus.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        lblStatus.setForeground(Color.LIGHT_GRAY);
        pnlProgress.add(lblStatus, BorderLayout.NORTH);
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        pnlProgress.add(progressBar, BorderLayout.CENTER);
        pnlMain.add(pnlProgress);

        // 5. 开始按钮
        btnStart = new JButton("开始分割音频 (通过内嵌引擎)");
        btnStart.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
        btnStart.setBackground(new Color(41, 128, 185));
        btnStart.setForeground(Color.WHITE);
        btnStart.setEnabled(false);
        btnStart.setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
        btnStart.addActionListener(e -> startSplittingThread());
        pnlMain.add(btnStart);

        add(pnlMain, BorderLayout.CENTER);

        // 拖放文件导入
        FileDropHandler dropHandler = new FileDropHandler();
        txtInputPath.setTransferHandler(dropHandler);
        pnlMain.setTransferHandler(dropHandler);
        pnlTitle.setTransferHandler(dropHandler);

        // 异步初始化 FFmpeg 引擎
        new Thread(this::initializeFFmpeg).start();
    }

    // 【核心绘图技术】：动态生成一个灰色圆角矩形底、蓝色极简科技感“S”的程序图标
    private Image createAppIcon() {
        int size = 128;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();

        // 开启超强抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 1. 绘制灰色底（采用高档炭灰色 #34495E 的扁平色，或中灰色 #4E5154）
        g2.setColor(new Color(58, 63, 68));
        g2.fillRoundRect(0, 0, size, size, 32, 32); // 现代圆角矩形

        // 2. 绘制科技感亮蓝色 “S”
        g2.setColor(new Color(52, 152, 219));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 95)); // 使用饱满的 Segoe UI

        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth("S")) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();

        g2.drawString("S", x, y - 6); // 微调垂直中心对齐
        g2.dispose();
        return image;
    }

    private JPanel createRowPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return panel;
    }

    private class FileDropHandler extends TransferHandler {
        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) return false;
            try {
                Transferable transferable = support.getTransferable();
                java.util.List<File> files = (java.util.List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                if (files != null && !files.isEmpty()) {
                    File file = files.get(0);
                    txtInputPath.setText(file.getAbsolutePath());
                    String nameWithoutExt = file.getName().replaceFirst("[.][^.]+$", "");
                    txtProjName.setText(nameWithoutExt);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private void initializeFFmpeg() {
        try {
            File tempDir = new File(System.getProperty("java.io.tmpdir"), "6s_music_engine");
            if (!tempDir.exists()) tempDir.mkdirs();

            File ffmpegFile = new File(tempDir, "ffmpeg.exe");

            if (!ffmpegFile.exists()) {
                lblStatus.setText("正在释放内嵌核心引擎 (仅首次)...");
                try (InputStream in = getClass().getResourceAsStream("/bin/ffmpeg.exe")) {
                    if (in == null) {
                        throw new FileNotFoundException("未在资源中找到内嵌 ffmpeg.exe！请确认放在了 src/main/resources/bin/ 目录下。");
                    }
                    Files.copy(in, ffmpegFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }

            extractedFFmpegPath = ffmpegFile.getAbsolutePath();

            SwingUtilities.invokeLater(() -> {
                lblStatus.setText("内嵌音频引擎就绪，等待导入音频... (支持文件拖拽到窗口)");
                btnStart.setEnabled(true);
            });

        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                lblStatus.setText("引擎加载失败！" + e.getMessage());
                JOptionPane.showMessageDialog(this, "内嵌引擎加载失败，请联系开发人员！\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            });
        }
    }

    private void selectFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "音频文件 (*.mp3, *.wav, *.flac, *.m4a, *.ogg, *.aac)",
                "mp3", "wav", "flac", "m4a", "ogg", "aac"
        ));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            txtInputPath.setText(file.getAbsolutePath());
            String nameWithoutExt = file.getName().replaceFirst("[.][^.]+$", "");
            txtProjName.setText(nameWithoutExt);
        }
    }

    private void selectDirectory() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtOutputPath.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void startSplittingThread() {
        if (extractedFFmpegPath == null) {
            JOptionPane.showMessageDialog(this, "引擎尚未就绪！", "警告", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            private boolean success = false;
            private String errorMsg = "";

            @Override
            protected Void doInBackground() throws Exception {
                btnStart.setEnabled(false);
                lblStatus.setText("正在解析音频信息...");
                progressBar.setValue(0);

                File srcFile = new File(txtInputPath.getText());
                File outDir = new File(txtOutputPath.getText());
                String projName = txtProjName.getText().trim();
                String secondsStr = txtSeconds.getText().trim();

                File projectDir = new File(outDir, projName);
                if (!projectDir.exists()) projectDir.mkdirs();

                String inputPath = srcFile.getAbsolutePath();
                String ext = "wav";
                if (cbFormat.getSelectedIndex() == 1) {
                    ext = inputPath.substring(inputPath.lastIndexOf(".") + 1);
                }

                double totalDuration = getAudioDuration(extractedFFmpegPath, inputPath);
                if (totalDuration <= 0) totalDuration = 100.0;

                String outputPattern = new File(projectDir, projName + "_%03d." + ext).getAbsolutePath();
                ProcessBuilder pb;

                if (ext.equalsIgnoreCase("wav")) {
                    pb = new ProcessBuilder(
                            extractedFFmpegPath, "-y",
                            "-i", inputPath,
                            "-f", "segment",
                            "-segment_time", secondsStr,
                            "-segment_start_number", "1",
                            outputPattern
                    );
                } else {
                    pb = new ProcessBuilder(
                            extractedFFmpegPath, "-y",
                            "-i", inputPath,
                            "-f", "segment",
                            "-segment_time", secondsStr,
                            "-c", "copy",
                            "-segment_start_number", "1",
                            outputPattern
                    );
                }

                pb.redirectErrorStream(true);
                Process process = pb.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"))) {
                    String line;
                    Pattern timePattern = Pattern.compile("time=(\\d+):(\\d+):(\\d+\\.\\d+)");
                    while ((line = reader.readLine()) != null) {
                        Matcher matcher = timePattern.matcher(line);
                        if (matcher.find()) {
                            int hours = Integer.parseInt(matcher.group(1));
                            int mins = Integer.parseInt(matcher.group(2));
                            double secs = Double.parseDouble(matcher.group(3));
                            double currentSeconds = hours * 3600 + mins * 60 + secs;

                            int percent = (int) Math.min(100, (currentSeconds / totalDuration) * 100);
                            SwingUtilities.invokeLater(() -> {
                                progressBar.setValue(percent);
                                lblStatus.setText("正在分割导出音频文件... 已完成: " + percent + "%");
                            });
                        }
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    success = true;
                } else {
                    errorMsg = "FFmpeg 运行异常，退出码: " + exitCode;
                }
                return null;
            }

            @Override
            protected void done() {
                btnStart.setEnabled(true);
                if (success) {
                    progressBar.setValue(100);
                    lblStatus.setText("音频分割成功！");
                    JOptionPane.showMessageDialog(AudioSplitterApp.this, "分割完成！请前往工程文件夹查看。", "成功", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    lblStatus.setText("处理失败！");
                    JOptionPane.showMessageDialog(AudioSplitterApp.this, "分割失败！\n" + errorMsg, "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private double getAudioDuration(String ffmpegPath, String audioPath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(ffmpegPath, "-i", audioPath);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            Pattern durPattern = Pattern.compile("Duration: (\\d+):(\\d+):(\\d+\\.\\d+)");
            while ((line = r.readLine()) != null) {
                Matcher m = durPattern.matcher(line);
                if (m.find()) {
                    int h = m.group(1) != null ? Integer.parseInt(m.group(1)) : 0;
                    int min = m.group(2) != null ? Integer.parseInt(m.group(2)) : 0;
                    double s = m.group(3) != null ? Double.parseDouble(m.group(3)) : 0.0;
                    p.destroy();
                    return h * 3600 + min * 60 + s;
                }
            }
        } catch (Exception ignored) {}
        return 0.0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AudioSplitterApp().setVisible(true));
    }
}