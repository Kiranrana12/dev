/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.study.studymateai;

/**
 *
 * @author pc
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Modern JDK 17+ Java Swing Study Suite Desktop Application
 * Compatible with NetBeans & JDK 17+.
 * ALL-IN-ONE File: Avoids package mismatch & classpath issues during local builds.
 */
public final class StudyMateAi extends JFrame {
    private static final Color DARK_BG = new Color(24, 28, 36);
    private static final Color SIDEBAR_BG = new Color(17, 19, 24);
    private static final Color TEXT_WHITE = new Color(240, 244, 248);
    private static final Color ACCENT_BLUE = new Color(59, 130, 246);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainWorkspace = new JPanel(cardLayout);

    public StudyMateAi() {
        setTitle("Swing Study Suite - JDK 17 Desktop Premium");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(DARK_BG);

        // Pre-create database files if not existing
        DataManager1.init();

        // Build elegant modern workspace layout split
        JPanel sideToolbar = createSidebar();
        initializeWorkspace();

        setLayout(new BorderLayout());
        add(sideToolbar, BorderLayout.WEST);
        add(mainWorkspace, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel toolbar = new JPanel();
        toolbar.setBackground(SIDEBAR_BG);
        toolbar.setPreferredSize(new Dimension(240, 720));
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.Y_AXIS));
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(40, 44, 52)));

        // Title Header Banner
        JLabel header = new JLabel("Study Companion Suite");
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setForeground(TEXT_WHITE);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setBorder(BorderFactory.createEmptyBorder(25, 10, 25, 10));
        toolbar.add(header);

        // Create sleek navigation selector buttons
        String[] tabs = {"Dashboard", "Study Planner", "Focus Timer", "Flashcards", "Mock Quizzes", "YouTube Summarizer", "Progress Tracker"};
        String[] cards = {"DASHBOARD", "PLANNER", "TIMER", "FLASHCARDS", "QUIZZES", "SUMMARIZER", "PROGRESS"};

        for (int i = 0; i < tabs.length; i++) {
            final String cardId = cards[i];
            JButton btn = new JButton(tabs[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (getModel().isPressed()) {
                        g2.setColor(new Color(30, 41, 59));
                    } else if (getModel().isRollover()) {
                        g2.setColor(new Color(45, 55, 72));
                    } else {
                        g2.setColor(SIDEBAR_BG);
                    }
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setForeground(new Color(160, 174, 192));
            btn.setBackground(SIDEBAR_BG);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);
            btn.setMaximumSize(new Dimension(220, 42)); // slightly tighter spacing to fit all 7 buttons elegantly
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setForeground(Color.WHITE);
                    btn.repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setForeground(new Color(160, 174, 192));
                    btn.repaint();
                }
            });

            btn.addActionListener(e -> {
                cardLayout.show(mainWorkspace, cardId);
            });

            toolbar.add(btn);
            toolbar.add(Box.createRigidArea(new Dimension(0, 6)));
        }

        toolbar.add(Box.createVerticalGlue());

        // Standard JVM System properties label shown transparently
        JLabel specLabel = new JLabel("JDK 17 + Swing Single-File App");
        specLabel.setFont(new Font("Monospaced", Font.PLAIN, 10));
        specLabel.setForeground(new Color(100, 110, 120));
        specLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        specLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        toolbar.add(specLabel);

        return toolbar;
    }

    private void initializeWorkspace() {
        mainWorkspace.add(new DashboardPanel1(this), "DASHBOARD");
        mainWorkspace.add(new TodoPanel1(), "PLANNER");
        mainWorkspace.add(new StudyTimerPanel1(), "TIMER");
        mainWorkspace.add(new FlashcardPanel1(), "FLASHCARDS");
        mainWorkspace.add(new QuizPanel1(), "QUIZZES");
        mainWorkspace.add(new YoutubeSummarizerPanel1(), "SUMMARIZER");
        mainWorkspace.add(new ProgressPanel1(), "PROGRESS");
    }

    public void swapToTab(String cardId) {
        cardLayout.show(mainWorkspace, cardId);
    }

    public static void main(String[] args) {
        // Enforce smooth antialiasing rendering parameters on system fonts
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                UIManager.put("Button.arc", 12);
                UIManager.put("Component.arc", 12);
            } catch (Exception e) {
                System.err.println("Theme fallback system defaults configured.");
            }
            new StudyMateAi().setVisible(true);
        });
    }
}

// ============================================================================
// DATA MODELS & PERSISTENCE
// ============================================================================

record StudyRecord1(
    String id,
    String timestamp,
    String type,
    String topic,
    int durationMinutes
) implements Serializable {
    public StudyRecord1 {
        if (durationMinutes < 0) {
            throw new IllegalArgumentException("Study duration cannot be negative: " + durationMinutes);
        }
        if (id == null || id.isBlank()) {
            id = java.util.UUID.randomUUID().toString();
        }
    }
}

record TodoItem1(
    String id,
    String text,
    boolean completed,
    String category,
    String dueDate
) implements Serializable {
    public TodoItem1 withCompleted(boolean isCompleted) {
        return new TodoItem1(this.id, this.text, isCompleted, this.category, this.dueDate);
    }
}

final class DataManager1 {
    private static final String DATA_FILE = "study_suite_local_database1.dat";
    private static List<StudyRecord1> studyLogs = new ArrayList<>();
    private static List<TodoItem1> todos = new ArrayList<>();

    private DataManager1() {}

    @SuppressWarnings("unchecked")
    public static synchronized void init() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            studyLogs.add(new StudyRecord1("demo-1", "2026-05-26", "timer", "Java OOP Polymorphism", 25));
            studyLogs.add(new StudyRecord1("demo-2", "2026-05-26", "quiz", "Inheritance Review", 10));
            
            todos.add(new TodoItem1("t1", "Prepare for JDK 17 compatibility workshop", false, "study", "2026-05-30"));
            todos.add(new TodoItem1("t2", "Log Focus Session on Study Companion", true, "general", "2026-05-26"));
            saveAll();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            studyLogs = (List<StudyRecord1>) ois.readObject();
            todos = (List<TodoItem1>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Database corrupted, performing auto-repair: " + e.getMessage());
            saveAll();
        }
    }

    public static synchronized List<StudyRecord1> getStudyLogs() {
        return new ArrayList<>(studyLogs);
    }

    public static synchronized void addLog(StudyRecord1 log) {
        studyLogs.add(log);
        saveAll();
    }

    public static synchronized List<TodoItem1> getTodos() {
        return new ArrayList<>(todos);
    }

    public static synchronized void saveTodos(List<TodoItem1> newList) {
        todos = new ArrayList<>(newList);
        saveAll();
    }

    private static void saveAll() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(studyLogs);
            oos.writeObject(todos);
        } catch (IOException e) {
            System.err.println("IO issue saving database: " + e.getMessage());
        }
    }
}

final class DashboardPanel1 extends JPanel {
    private final StudyMateAi parent;
    private final JLabel scoreLabel = new JLabel("0");
    private final JLabel timeLabel = new JLabel("0 mins");
    private final JLabel streakLabel = new JLabel("1 day");

    public DashboardPanel1(StudyMateAi parentApp) {
        this.parent = parentApp;
        setBackground(new Color(24, 28, 36));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Study Suite Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        headerPanel.add(title);

        JLabel desc = new JLabel("Track your metrics, set targeted goals, and load study modules.");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        desc.setForeground(new Color(148, 163, 184));
        headerPanel.add(desc);

        add(headerPanel, BorderLayout.NORTH);

        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        statsGrid.setOpaque(false);
        statsGrid.setBorder(BorderFactory.createEmptyBorder(35, 0, 35, 0));

        statsGrid.add(createMetricsCard("Total Time Focused", timeLabel, new Color(59, 130, 246)));
        statsGrid.add(createMetricsCard("Active Streak", streakLabel, new Color(16, 185, 129)));
        statsGrid.add(createMetricsCard("Activities Complete", scoreLabel, new Color(245, 158, 11)));

        add(statsGrid, BorderLayout.CENTER);

        JPanel launchpadPanel = new JPanel(new BorderLayout());
        launchpadPanel.setOpaque(false);
        
        JLabel subTitle = new JLabel("Study Modules Launchpad");
        subTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subTitle.setForeground(Color.WHITE);
        subTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        launchpadPanel.add(subTitle, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(1, 5, 8, 0));
        btnPanel.setOpaque(false);

        JButton launchTimer = createQuickButton("Focus Timer", "TIMER");
        JButton launchPlanner = createQuickButton("Study Planner", "PLANNER");
        JButton launchQuiz = createQuickButton("Mock Quizzes", "QUIZZES");
        JButton launchYt = createQuickButton("YT Summarizer", "SUMMARIZER");
        JButton launchProgress = createQuickButton("Progress Tracker", "PROGRESS");

        btnPanel.add(launchTimer);
        btnPanel.add(launchPlanner);
        btnPanel.add(launchQuiz);
        btnPanel.add(launchYt);
        btnPanel.add(launchProgress);
        launchpadPanel.add(btnPanel, BorderLayout.CENTER);

        add(launchpadPanel, BorderLayout.SOUTH);

        refreshMetrics();
    }

    private JPanel createMetricsCard(String title, JLabel valueLabel, Color borderCol) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 41, 59));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLbl.setForeground(new Color(148, 163, 184));
        card.add(titleLbl, BorderLayout.NORTH);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(Color.WHITE);
        card.add(valueLabel, BorderLayout.CENTER);

        JPanel statusBorder = new JPanel();
        statusBorder.setPreferredSize(new Dimension(5, 5));
        statusBorder.setBackground(borderCol);
        card.add(statusBorder, BorderLayout.WEST);

        return card;
    }

    private JButton createQuickButton(final String text, final String targetCard) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(getBackground().darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(45, 55, 72));
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 41, 59));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); // disable default native theme border painting to prevent white overlay artifacts
        btn.setOpaque(false); // prevent native background overlaps
        btn.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        btn.setPreferredSize(new Dimension(200, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> parent.swapToTab(targetCard));
        return btn;
    }

    public void refreshMetrics() {
        List<StudyRecord1> records = DataManager1.getStudyLogs();
        int totalMinutes = records.stream().mapToInt(StudyRecord1::durationMinutes).sum();
        int modulesCount = records.size();

        timeLabel.setText(totalMinutes + " mins");
        scoreLabel.setText(String.valueOf(modulesCount));
        streakLabel.setText(records.isEmpty() ? "0 days" : "1 day");
    }
}

final class StudyTimerPanel1 extends JPanel {
    private int timeLeftSeconds = 25 * 60;
    private int maxDuration = 25 * 60;
    private boolean isActive = false;
    private JTimer1 countdownTimer;

    private final JLabel timeStr = new JLabel("25:00", SwingConstants.CENTER);
    private final JTextField topicField = new JTextField("Java OOP Studies");
    private final JButton toggleBtn = SwingStyleUtil.createStyledButton("Start Timer 🚀", new Color(59, 130, 246));

    private record AppTimerPreset(String name, int mins) {}

    public StudyTimerPanel1() {
        setBackground(new Color(24, 28, 36));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("⚡ Study Focus Timer");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);

        JLabel desc = new JLabel("EDT background compatible focus timer. Emits sound when finished.");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        desc.setForeground(new Color(148, 163, 184));
        titlePanel.add(desc);
        add(titlePanel, BorderLayout.NORTH);

        JPanel centerDial = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 41, 59));
                int size = 220;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                g2.fillOval(x, y, size, size);

                double progress = maxDuration > 0 ? (double) timeLeftSeconds / maxDuration : 0;
                g2.setColor(new Color(59, 130, 246));
                g2.setStroke(new BasicStroke(6));
                g2.drawArc(x, y, size, size, 90, (int) (progress * -360));
                g2.dispose();
            }
        };
        centerDial.setOpaque(false);

        timeStr.setFont(new Font("Monospaced", Font.BOLD, 36));
        timeStr.setForeground(Color.WHITE);
        centerDial.add(timeStr);
        add(centerDial, BorderLayout.CENTER);

        JPanel bottomFrame = new JPanel();
        bottomFrame.setOpaque(false);
        bottomFrame.setLayout(new BoxLayout(bottomFrame, BoxLayout.Y_AXIS));

        JPanel inputs = new JPanel(new FlowLayout());
        inputs.setOpaque(false);
        JLabel inputLbl = new JLabel("Topic to Focus: ");
        inputLbl.setForeground(Color.WHITE);
        inputs.add(inputLbl);

        topicField.setPreferredSize(new Dimension(180, 25));
        topicField.setBackground(new Color(30, 41, 59));
        topicField.setCaretColor(Color.WHITE);
        topicField.setForeground(Color.WHITE);
        inputs.add(topicField);

        JButton customTimeBtn = SwingStyleUtil.createStyledButton("Set Custom Dur.", new Color(71, 85, 105));
        customTimeBtn.addActionListener(e -> {
            String valStr = JOptionPane.showInputDialog(this, "Type focus duration (Minutes):", "25");
            if (valStr != null) {
                try {
                    int mins = Integer.parseInt(valStr);
                    if (mins > 0) {
                        applyPreset(new AppTimerPreset("Custom", mins));
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number format.");
                }
            }
        });
        inputs.add(customTimeBtn);

        bottomFrame.add(inputs);
        bottomFrame.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel ctrlRow = new JPanel(new FlowLayout());
        ctrlRow.setOpaque(false);

        toggleBtn.setPreferredSize(new Dimension(160, 40));
        toggleBtn.addActionListener(e -> toggleClick());
        ctrlRow.add(toggleBtn);

        JButton resetBtn = SwingStyleUtil.createStyledButton("Reset 🔄", new Color(71, 85, 105));
        resetBtn.setPreferredSize(new Dimension(100, 40));
        resetBtn.addActionListener(e -> resetClick());
        ctrlRow.add(resetBtn);

        bottomFrame.add(ctrlRow);
        add(bottomFrame, BorderLayout.SOUTH);

        initTimerSystem();
    }

    private void applyPreset(AppTimerPreset preset) {
        stopTimer();
        timeLeftSeconds = preset.mins * 60;
        maxDuration = preset.mins * 60;
        updateTimeLabel();
        repaint();
    }

    private void initTimerSystem() {
        countdownTimer = new JTimer1(1000, () -> {
            if (isActive && timeLeftSeconds > 0) {
                timeLeftSeconds--;
                updateTimeLabel();
                repaint();
                if (timeLeftSeconds == 0) {
                    sessionEnded();
                }
            }
        });
    }

    private void toggleClick() {
        if (isActive) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        isActive = true;
        toggleBtn.setText("Pause Focus ⏸️");
        toggleBtn.setBackground(new Color(239, 68, 68));
        countdownTimer.start();
    }

    private void stopTimer() {
        isActive = false;
        toggleBtn.setText("Resume Focus 🚀");
        toggleBtn.setBackground(new Color(59, 130, 246));
        countdownTimer.stop();
    }

    private void resetClick() {
        stopTimer();
        timeLeftSeconds = maxDuration;
        toggleBtn.setText("Start Timer 🚀");
        updateTimeLabel();
        repaint();
    }

    private void updateTimeLabel() {
        int minutes = timeLeftSeconds / 60;
        int seconds = timeLeftSeconds % 60;
        timeStr.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void sessionEnded() {
        stopTimer();
        try {
            Toolkit.getDefaultToolkit().beep();
        } catch (Exception e) {}

        String curTopic = topicField.getText().trim();
        int studiedMins = maxDuration / 60;
        
        DataManager1.addLog(new StudyRecord1(null, "2026-05-26", "timer", curTopic, studiedMins));
        
        JOptionPane.showMessageDialog(this,
            "🎉 Pomodoro Focus Complete!\nLogged " + studiedMins + " minutes of Study under Topic: \"" + curTopic + "\"",
            "Study Companion Alarm",
            JOptionPane.INFORMATION_MESSAGE
        );
        resetClick();
    }
}

final class JTimer1 {
    private final int delay;
    private final Runnable action;
    private Thread currentWorker;
    private boolean isRunning = false;

    public JTimer1(int delay, Runnable action) {
        this.delay = delay;
        this.action = action;
    }

    public synchronized void start() {
        if (isRunning) return;
        isRunning = true;
        currentWorker = new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(delay);
                    SwingUtilities.invokeLater(action);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        currentWorker.start();
    }

    public synchronized void stop() {
        isRunning = false;
        if (currentWorker != null) {
            currentWorker.interrupt();
            currentWorker = null;
        }
    }
}

final class TodoPanel1 extends JPanel {
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> todoJList = new JList<>(listModel);
    
    private final JTextField txtInput = new JTextField();
    private final JComboBox<String> catBox = new JComboBox<>(new String[]{"📂 General", "📚 Study Focus", "❓ Quiz Prep", "🗂️ Flashcards"});
    private final JComboBox<String> filterBox = new JComboBox<>(new String[]{"Show All Tasks", "Hide Completed"});

    private List<TodoItem1> actualTasks = new ArrayList<>();

    public TodoPanel1() {
        setBackground(new Color(24, 28, 36));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        JPanel headingBanner = new JPanel(new GridLayout(2, 1));
        headingBanner.setOpaque(false);
        JLabel title = new JLabel("📋 Planner To-Do Tasklist");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        headingBanner.add(title);

        JLabel desc = new JLabel("Deadlines or checkpoints tracker. Persists tasks locally.");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        desc.setForeground(new Color(148, 163, 184));
        headingBanner.add(desc);
        add(headingBanner, BorderLayout.NORTH);

        JPanel listBackdrop = new JPanel(new BorderLayout());
        listBackdrop.setOpaque(false);
        listBackdrop.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel listFilters = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        listFilters.setOpaque(false);
        filterBox.setBackground(new Color(30, 41, 59));
        filterBox.setForeground(Color.WHITE);
        filterBox.addActionListener(e -> renderList());
        listFilters.add(filterBox);
        listBackdrop.add(listFilters, BorderLayout.NORTH);

        todoJList.setBackground(new Color(30, 41, 59));
        todoJList.setForeground(Color.WHITE);
        todoJList.setFont(new Font("Segoe UI", Font.BOLD, 13));
        todoJList.setSelectionBackground(new Color(59, 130, 246, 50));
        todoJList.setSelectionForeground(Color.WHITE);
        todoJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                String text = value.toString();
                if (text.startsWith("[✓]")) {
                    label.setForeground(new Color(148, 163, 184));
                } else {
                    label.setForeground(Color.WHITE);
                }
                return label;
            }
        });

        JScrollPane pane = new JScrollPane(todoJList);
        pane.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 10)));
        listBackdrop.add(pane, BorderLayout.CENTER);
        add(listBackdrop, BorderLayout.CENTER);

        JPanel subControls = new JPanel(new BorderLayout(15, 0));
        subControls.setOpaque(false);

        JPanel inputGroup = new JPanel(new GridLayout(1, 2, 10, 0));
        inputGroup.setOpaque(false);

        txtInput.setBackground(new Color(30, 41, 59));
        txtInput.setForeground(Color.WHITE);
        txtInput.setCaretColor(Color.WHITE);
        txtInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 10)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        inputGroup.add(txtInput);

        catBox.setBackground(new Color(30, 41, 59));
        catBox.setForeground(Color.WHITE);
        inputGroup.add(catBox);
        subControls.add(inputGroup, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setOpaque(false);

        JButton addBtn = SwingStyleUtil.createStyledButton("+ Add Task", new Color(59, 130, 246));
        addBtn.addActionListener(e -> appendTask());
        actionPanel.add(addBtn);

        JButton toggleBtn = SwingStyleUtil.createStyledButton("Toggle Complete ✓", new Color(16, 185, 129));
        toggleBtn.addActionListener(e -> markSelectedCompleted());
        actionPanel.add(toggleBtn);

        JButton deleteBtn = SwingStyleUtil.createStyledButton("Delete 🗑️", new Color(239, 68, 68));
        deleteBtn.addActionListener(e -> removeSelected());
        actionPanel.add(deleteBtn);

        subControls.add(actionPanel, BorderLayout.EAST);
        add(subControls, BorderLayout.SOUTH);

        loadDataFromPersistence();
    }

    private synchronized void loadDataFromPersistence() {
        actualTasks = DataManager1.getTodos();
        renderList();
    }

    private synchronized void renderList() {
        listModel.clear();
        boolean hideCompleted = filterBox.getSelectedIndex() == 1;

        for (TodoItem1 item : actualTasks) {
            if (hideCompleted && item.completed()) continue;
            String status = item.completed() ? "[✓] " : "[ ] ";
            listModel.addElement(status + item.text() + " (" + item.category() + ")");
        }
    }

    private synchronized void appendTask() {
        String inputStr = txtInput.getText().trim();
        if (inputStr.isBlank()) {
            JOptionPane.showMessageDialog(this, "Empty descriptions are restricted.");
            return;
        }

        String id = java.util.UUID.randomUUID().toString();
        String cat = catBox.getSelectedItem().toString().substring(3);
        TodoItem1 item = new TodoItem1(id, inputStr, false, cat, "2026-05-30");

        actualTasks.add(item);
        DataManager1.saveTodos(actualTasks);
        txtInput.setText("");
        renderList();
    }

    private synchronized void markSelectedCompleted() {
        int index = todoJList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Select a specific task card to adjust status.");
            return;
        }

        int curCount = 0;
        boolean hideCompleted = filterBox.getSelectedIndex() == 1;

        for (int i = 0; i < actualTasks.size(); i++) {
            TodoItem1 item = actualTasks.get(i);
            if (hideCompleted && item.completed()) continue;

            if (curCount == index) {
                actualTasks.set(i, item.withCompleted(!item.completed()));
                break;
            }
            curCount++;
        }

        DataManager1.saveTodos(actualTasks);
        renderList();
    }

    private synchronized void removeSelected() {
        int index = todoJList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Please choose a study task to delete.");
            return;
        }

        int curCount = 0;
        boolean hideCompleted = filterBox.getSelectedIndex() == 1;

        for (int i = 0; i < actualTasks.size(); i++) {
            TodoItem1 item = actualTasks.get(i);
            if (hideCompleted && item.completed()) continue;

            if (curCount == index) {
                actualTasks.remove(i);
                break;
            }
            curCount++;
        }

        DataManager1.saveTodos(actualTasks);
        renderList();
    }
}

final class FlashcardPanel1 extends JPanel {
    private record LocalFlashcard(String question, String answer) {}
    private final List<LocalFlashcard> cardDeck = new ArrayList<>();
    private int currentIdx = 0;
    private boolean showingQuestion = true;

    private final JLabel cardDisplay = new JLabel("", SwingConstants.CENTER);
    private final JLabel indexTracker = new JLabel("Card 1 of 3");
    private final JButton toggleRevealBtn = SwingStyleUtil.createStyledButton("Reveal Answer 🔄", new Color(59, 130, 246));

    public FlashcardPanel1() {
        setBackground(new Color(24, 28, 36));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        cardDeck.add(new LocalFlashcard(
            "What is Polymorphism in Java Swing & general OOP?",
            "<html><div style='text-align: center;'>The capacity of an object to take many forms. Typically illustrated<br>when a parent interface references subclass instances dynamically!</div></html>"
        ));
        cardDeck.add(new LocalFlashcard(
            "What is the Event Dispatch Thread (EDT) in Java Swing?",
            "<html><div style='text-align: center;'>The background EDT is responsible for executing rendering callbacks & event clicks.<br>We call <b>SwingUtilities.invokeLater()</b> to schedule updates safely!</div></html>"
        ));
        cardDeck.add(new LocalFlashcard(
            "What lightweight paradigm characterizes Swing components?",
            "<html><div style='text-align: center;'>Swing elements are written completely in pure Java drawing code,<br>leaving them independent of the native operating system peer controls!</div></html>"
        ));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("🗂️ Active Recall Flashcards");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);

        JLabel desc = new JLabel("Click the card or use button to simulate 3D flips. Memorize definitions.");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        desc.setForeground(new Color(148, 163, 184));
        titlePanel.add(desc);
        add(titlePanel, BorderLayout.NORTH);

        JPanel flashBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (showingQuestion) {
                    g2.setColor(new Color(30, 41, 59));
                } else {
                    g2.setColor(new Color(15, 118, 110));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setStroke(new BasicStroke(2));
                g2.setColor(new Color(255, 255, 255, 15));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        flashBox.setOpaque(false);
        flashBox.setLayout(new BorderLayout());
        flashBox.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        cardDisplay.setFont(new Font("Segoe UI", Font.BOLD, 15));
        cardDisplay.setForeground(Color.WHITE);
        cardDisplay.setCursor(new Cursor(Cursor.HAND_CURSOR));
        flashBox.add(cardDisplay, BorderLayout.CENTER);

        flashBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                flipCard();
            }
        });

        JPanel constraintBox = new JPanel(new GridBagLayout());
        constraintBox.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(30, 20, 30, 20);
        flashBox.setPreferredSize(new Dimension(500, 240));
        constraintBox.add(flashBox, gbc);
        add(constraintBox, BorderLayout.CENTER);

        // Form panel for manual flashcard insert on the left column (West)
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setPreferredSize(new Dimension(280, 500));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 15), 1), "➕ Create Custom Card", 
                javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 12), Color.WHITE),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel frontLabel = new JLabel("Front Prompt / Question:");
        frontLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        frontLabel.setForeground(new Color(148, 163, 184));
        frontLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(frontLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JTextField frontField = new JTextField();
        frontField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        frontField.setBackground(new Color(30, 41, 59));
        frontField.setForeground(Color.WHITE);
        frontField.setCaretColor(Color.WHITE);
        frontField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 15), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        frontField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(frontField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel backLabel = new JLabel("Back Answer / Definition:");
        backLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        backLabel.setForeground(new Color(148, 163, 184));
        backLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(backLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JTextArea backField = new JTextArea();
        backField.setLineWrap(true);
        backField.setWrapStyleWord(true);
        backField.setBackground(new Color(30, 41, 59));
        backField.setForeground(Color.WHITE);
        backField.setCaretColor(Color.WHITE);
        backField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 15), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        JScrollPane backScroll = new JScrollPane(backField);
        backScroll.setOpaque(false);
        backScroll.getViewport().setOpaque(false);
        backScroll.setBorder(null);
        backScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        backScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(backScroll);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton addCardBtn = SwingStyleUtil.createStyledButton("Create Study Card", new Color(59, 130, 246));
        addCardBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addCardBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        addCardBtn.addActionListener(e -> {
            String question = frontField.getText().trim();
            String answer = backField.getText().trim();
            if (question.isEmpty() || answer.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both front and back fields.");
                return;
            }
            cardDeck.add(new LocalFlashcard(
                question,
                "<html><div style='text-align: center;'>" + answer.replace("\n", "<br>") + "</div></html>"
            ));
            frontField.setText("");
            backField.setText("");
            currentIdx = cardDeck.size() - 1;
            showingQuestion = true;
            toggleRevealBtn.setText("Reveal Answer 🔄");
            refreshCardText();
            repaint();
            JOptionPane.showMessageDialog(this, "Custom Flashcard created successfully in deck!");
        });
        formPanel.add(addCardBtn);
        formPanel.add(Box.createVerticalGlue());

        add(formPanel, BorderLayout.WEST);

        JPanel ctrlRow = new JPanel(new BorderLayout());
        ctrlRow.setOpaque(false);

        indexTracker.setFont(new Font("Segoe UI", Font.BOLD, 12));
        indexTracker.setForeground(new Color(148, 163, 184));
        ctrlRow.add(indexTracker, BorderLayout.WEST);

        JPanel btnGroup = new JPanel(new FlowLayout());
        btnGroup.setOpaque(false);

        JButton prevBtn = SwingStyleUtil.createStyledButton("◀ Prev", new Color(30, 41, 59));
        prevBtn.addActionListener(e -> navigateDeck(-1));
        btnGroup.add(prevBtn);

        toggleRevealBtn.addActionListener(e -> flipCard());
        btnGroup.add(toggleRevealBtn);

        JButton nextBtn = SwingStyleUtil.createStyledButton("Next ▶", new Color(30, 41, 59));
        nextBtn.addActionListener(e -> navigateDeck(1));
        btnGroup.add(nextBtn);

        JButton deleteCardBtn = SwingStyleUtil.createStyledButton("Delete Card 🗑️", new Color(239, 68, 68));
        deleteCardBtn.addActionListener(e -> {
            if (cardDeck.isEmpty()) return;
            cardDeck.remove(currentIdx);
            if (cardDeck.isEmpty()) {
                cardDeck.add(new LocalFlashcard("Your Study Deck is Empty", "<html><div style='text-align: center;'>No cards left. Add a custom card on the left!</div></html>"));
            }
            if (currentIdx >= cardDeck.size()) {
                currentIdx = cardDeck.size() - 1;
            }
            showingQuestion = true;
            toggleRevealBtn.setText("Reveal Answer 🔄");
            refreshCardText();
            repaint();
        });
        btnGroup.add(deleteCardBtn);

        ctrlRow.add(btnGroup, BorderLayout.CENTER);
        add(ctrlRow, BorderLayout.SOUTH);

        refreshCardText();
    }

    private void flipCard() {
        showingQuestion = !showingQuestion;
        toggleRevealBtn.setText(showingQuestion ? "Reveal Answer 🔄" : "Show Question 🔄");
        refreshCardText();
        repaint();
    }

    private void navigateDeck(int direction) {
        currentIdx += direction;
        if (currentIdx < 0) {
            currentIdx = cardDeck.size() - 1;
        } else if (currentIdx >= cardDeck.size()) {
            currentIdx = 0;
        }
        showingQuestion = true;
        toggleRevealBtn.setText("Reveal Answer 🔄");
        refreshCardText();
        repaint();
    }

    private void refreshCardText() {
        LocalFlashcard current = cardDeck.get(currentIdx);
        cardDisplay.setText(showingQuestion ? current.question() : current.answer());
        indexTracker.setText("Card " + (currentIdx + 1) + " of " + cardDeck.size());
    }
}

final class QuizPanel1 extends JPanel {
    private record QuizEntity(String question, String[] options, int correctIndex) {}

    private final List<QuizEntity> questionsList = new ArrayList<>();
    private int activeIndex = 0;
    private int score = 0;

    private final JLabel progressTracker = new JLabel("Question 1 of 3");
    private final JLabel questionField = new JLabel("Java Quiz Prompt");
    private final JRadioButton[] radioButtons = new JRadioButton[4];
    private final ButtonGroup btnGroup = new ButtonGroup();

    public QuizPanel1() {
        setBackground(new Color(24, 28, 36));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        // Default seed questions list
        questionsList.add(new QuizEntity(
            "Which Swing method is vital to call for launching UI safely onto the background EDT thread?",
            new String[]{"SwingUtilities.invokeLater()", "JFrame.showLauncher()", "Thread.runNow()", "System.initSwing()"},
            0
        ));
        questionsList.add(new QuizEntity(
            "Under JDK 17, what keyword structures an absolute clean, immutable helper data container?",
            new String[]{"structural class", "data structure", "sealed interface", "record"},
            3
        ));
        questionsList.add(new QuizEntity(
            "Which manager layout positions components in exact North, South, East, West sectors?",
            new String[]{"GridLayout", "BorderLayout", "GridBagLayout", "BoxLayout"},
            1
        ));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("❓ Study Evaluation Quizzes");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);

        JLabel desc = new JLabel("Concept evaluations on computer science and Java compiler concepts.");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        desc.setForeground(new Color(148, 163, 184));
        titlePanel.add(desc);
        add(titlePanel, BorderLayout.NORTH);

        // West Column: Configuration & Generator Panel
        JPanel westPanel = new JPanel();
        westPanel.setOpaque(false);
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        westPanel.setPreferredSize(new Dimension(300, 500));
        westPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(255, 255, 255, 15)),
            BorderFactory.createEmptyBorder(0, 0, 0, 15)
        ));

        // Section 1: Topic Generator
        JPanel genPanel = new JPanel();
        genPanel.setOpaque(false);
        genPanel.setLayout(new BoxLayout(genPanel, BoxLayout.Y_AXIS));
        genPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 15), 1), "🧠 Dynamic AI Topic Generator",
            javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), Color.WHITE
        ));

        JLabel topicLbl = new JLabel("Type Study Topic:");
        topicLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        topicLbl.setForeground(new Color(148, 163, 184));
        topicLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        genPanel.add(topicLbl);
        genPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JTextField topicInput = new JTextField("Algorithms");
        topicInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        topicInput.setBackground(new Color(30, 41, 59));
        topicInput.setForeground(Color.WHITE);
        topicInput.setCaretColor(Color.WHITE);
        topicInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255,255,255,10)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        topicInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        genPanel.add(topicInput);
        genPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton genBtn = SwingStyleUtil.createStyledButton("Generate Smart Quiz 🚀", new Color(16, 185, 129));
        genBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        genBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        genBtn.addActionListener(e -> generateSmartQuiz(topicInput.getText()));
        genPanel.add(genBtn);

        westPanel.add(genPanel);
        westPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Section 2: Custom Question Form
        JPanel addQPanel = new JPanel();
        addQPanel.setOpaque(false);
        addQPanel.setLayout(new BoxLayout(addQPanel, BoxLayout.Y_AXIS));
        addQPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 15), 1), "➕ Add Custom Question",
            javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), Color.WHITE
        ));

        // Question fields
        JTextField qField = new JTextField("My study test question?");
        JTextField opt1 = new JTextField("Option 1 (Correct)");
        JTextField opt2 = new JTextField("Option 2");
        JTextField opt3 = new JTextField("Option 3");
        JTextField opt4 = new JTextField("Option 4");

        JTextField[] formFields = {qField, opt1, opt2, opt3, opt4};
        String[] labels = {"Question Prompt:", "Option 1 (Correct):", "Option 2:", "Option 3:", "Option 4:"};

        for (int x = 0; x < 5; x++) {
            JLabel lbl = new JLabel(labels[x]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
            lbl.setForeground(new Color(148, 163, 184));
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            addQPanel.add(lbl);
            addQPanel.add(Box.createRigidArea(new Dimension(0, 3)));

            formFields[x].setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
            formFields[x].setBackground(new Color(30, 41, 59));
            formFields[x].setForeground(Color.WHITE);
            formFields[x].setCaretColor(Color.WHITE);
            formFields[x].setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255,255,255,10)),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
            ));
            formFields[x].setAlignmentX(Component.LEFT_ALIGNMENT);
            addQPanel.add(formFields[x]);
            addQPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        JButton addQBtn = SwingStyleUtil.createStyledButton("Add To Active Exam", new Color(59, 130, 246));
        addQBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addQBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        addQBtn.addActionListener(e -> {
            String qText = qField.getText().trim();
            String o1 = opt1.getText().trim();
            String o2 = opt2.getText().trim();
            String o3 = opt3.getText().trim();
            String o4 = opt4.getText().trim();
            if (qText.isEmpty() || o1.isEmpty() || o2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "A question must have a prompt and at least 2 options!");
                return;
            }
            questionsList.add(new QuizEntity(qText, new String[]{o1, o2, o3, o4}, 0));
            qField.setText("");
            opt1.setText("");
            opt2.setText("");
            opt3.setText("");
            opt4.setText("");
            activeIndex = questionsList.size() - 1;
            renderQuestion();
            JOptionPane.showMessageDialog(this, "Custom Question added to exam list successfully!");
        });
        addQPanel.add(addQBtn);
        westPanel.add(addQPanel);

        add(westPanel, BorderLayout.WEST);

        // Center Box: Question and Answer options
        JPanel centerBox = new JPanel();
        centerBox.setOpaque(false);
        centerBox.setLayout(new BoxLayout(centerBox, BoxLayout.Y_AXIS));
        centerBox.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 0));

        progressTracker.setFont(new Font("Segoe UI", Font.BOLD, 12));
        progressTracker.setForeground(new Color(59, 130, 246));
        centerBox.add(progressTracker);
        centerBox.add(Box.createRigidArea(new Dimension(0, 10)));

        questionField.setFont(new Font("Segoe UI", Font.BOLD, 15));
        questionField.setForeground(Color.WHITE);
        centerBox.add(questionField);
        centerBox.add(Box.createRigidArea(new Dimension(0, 20)));

        for (int i = 0; i < 4; i++) {
            radioButtons[i] = new JRadioButton();
            radioButtons[i].setForeground(Color.WHITE);
            radioButtons[i].setOpaque(false);
            radioButtons[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btnGroup.add(radioButtons[i]);
            centerBox.add(radioButtons[i]);
            centerBox.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        add(centerBox, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);

        JButton nextBtn = SwingStyleUtil.createStyledButton("Submit Answer ➔", new Color(16, 185, 129));
        nextBtn.addActionListener(e -> evaluateChoice());
        footer.add(nextBtn);

        add(footer, BorderLayout.SOUTH);

        renderQuestion();
    }

    private void generateSmartQuiz(String topic) {
        questionsList.clear();
        String t = topic.trim();
        if (t.isEmpty()) t = "General CS";

        if (t.toLowerCase().contains("python")) {
            questionsList.add(new QuizEntity("What keyword is used to define functions in Python?", new String[]{"func", "def", "function", "define"}, 1));
            questionsList.add(new QuizEntity("Which of the following is an immutable collection type in Python?", new String[]{"list", "set", "dict", "tuple"}, 3));
            questionsList.add(new QuizEntity("How do you start a documentation comment block in standard Python?", new String[]{"//", "/* */", "triple quotes \"\"\"", "#"}, 2));
        } else if (t.toLowerCase().contains("structure") || t.toLowerCase().contains("algo")) {
            questionsList.add(new QuizEntity("Which data structure operates on a Last-In, First-Out (LIFO) model?", new String[]{"Queue", "Stack", "Binary Tree", "HashMap"}, 1));
            questionsList.add(new QuizEntity("What is the average time complexity of searching in a balanced Binary Search Tree (BST)?", new String[]{"O(1)", "O(n)", "O(log n)", "O(n log n)"}, 2));
            questionsList.add(new QuizEntity("Which sorting algorithm has a guaranteed worst-case time complexity of O(n log n)?", new String[]{"Bubble Sort", "Quick Sort", "Merge Sort", "Insertion Sort"}, 2));
        } else if (t.toLowerCase().contains("sql") || t.toLowerCase().contains("database")) {
            questionsList.add(new QuizEntity("Which SQL clause is used to filter records in a group summary?", new String[]{"WHERE", "HAVING", "FILTER", "GROUP BY"}, 1));
            questionsList.add(new QuizEntity("What does the acronym ACID represent in transactional databases?", new String[]{"Atomicity, Consistency, Isolation, Durability", "Access, Control, Index, Data", "Automated, Cached, Integrated, Distributed", "None of the above"}, 0));
            questionsList.add(new QuizEntity("Which key uniquely identifies a record in a different database table?", new String[]{"Primary Key", "Foreign Key", "Unique Key", "Super Key"}, 1));
        } else {
            questionsList.add(new QuizEntity(
                "In software development, what is the primary core purpose of " + t + "?",
                new String[]{
                    "To facilitate high-performance code abstraction and modular separation of concerns.",
                    "To replace standard compile-time parsing pipelines.",
                    "To automatically resolve networking security loops.",
                    "To deprecate garbage collection overheads."
                },
                0
            ));
            questionsList.add(new QuizEntity(
                "Which of the following describes a critical industry standard best practice in " + t + "?",
                new String[]{
                    "Duplicating variable definitions across global runtimes.",
                    "Writing highly cohesive, modular, and loosely coupled functional logic.",
                    "Disabling all active logging streams.",
                    "Skipping exception validations entirely."
                },
                1
            ));
            questionsList.add(new QuizEntity(
                "How does " + t + " optimize resource allocation or execution constraints?",
                new String[]{
                    "By running continuously in active blocking infinite loops.",
                    "By leveraging native hardware threads directly in unstructured scopes.",
                    "By utilizing strategic memory reuse patterns and avoiding side-effects.",
                    "By delegating thread allocation rules to local file systems."
                },
                2
            ));
        }

        activeIndex = 0;
        score = 0;
        renderQuestion();
        JOptionPane.showMessageDialog(this, "Generated 3 custom quiz questions for: " + t);
    }

    private void renderQuestion() {
        if (questionsList.isEmpty()) {
            progressTracker.setText("QUIZ DECK IS EMPTY");
            questionField.setText("<html>Please generate a smart quiz or add a custom question!</html>");
            for (int i = 0; i < 4; i++) radioButtons[i].setVisible(false);
            return;
        }

        if (activeIndex >= questionsList.size()) {
            JOptionPane.showMessageDialog(this,"🎉 Evaluation Completed!Score accomplished: " + score + " / " + questionsList.size() + " correctness marks.",
                "Assessment Finish Line",
                JOptionPane.INFORMATION_MESSAGE
            );
            DataManager1.addLog(new StudyRecord1(null, java.time.LocalDate.now().toString(), "quiz", "Assessment Quiz Terminals", score));
            activeIndex = 0;
            score = 0;
        }

        QuizEntity current = questionsList.get(activeIndex);
        progressTracker.setText("QUESTION " + (activeIndex + 1) + " OF " + questionsList.size());
        questionField.setText("<html>" + current.question() + "</html>");
        
        btnGroup.clearSelection();
        for (int i = 0; i < 4; i++) {
            if (i < current.options().length && current.options()[i] != null && !current.options()[i].trim().isEmpty()) {
                radioButtons[i].setText(current.options()[i]);
                radioButtons[i].setVisible(true);
            } else {
                radioButtons[i].setVisible(false);
            }
        }
    }

    private void evaluateChoice() {
        if (questionsList.isEmpty()) return;

        int choiceIdx = -1;
        for (int i = 0; i < 4; i++) {
            if (radioButtons[i].isSelected()) {
                choiceIdx = i;
                break;
            }
        }

        if (choiceIdx == -1) {
            JOptionPane.showMessageDialog(this, "Please choose an answer option to proceed.");
            return;
        }

        QuizEntity current = questionsList.get(choiceIdx); // wait, choiceIdx is selected radio button, current question is questionsList.get(activeIndex)
        QuizEntity activeQ = questionsList.get(activeIndex);
        if (choiceIdx == activeQ.correctIndex()) {
            score++;
            JOptionPane.showMessageDialog(this, "✨ Outstanding! Correct answer logged.");
        } else {
            JOptionPane.showMessageDialog(this, "❌ Incorrect. The correct answer was: " + activeQ.options()[activeQ.correctIndex()]);
        }

        activeIndex++;
        renderQuestion();
    }
}

// ============================================================================
// NEW FEATURE: YOUTUBE NOTE SUMMARIZER
// ============================================================================
final class YoutubeSummarizerPanel1 extends JPanel {
    private final JTextField urlField;
    private final JTextArea transcriptArea;
    private final JTextArea summaryOutput;
    private final JButton btnGenerate;
    private final JLabel lblStatus;

    public YoutubeSummarizerPanel1() {
        setBackground(new Color(24, 28, 36));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("YouTube Note Summarizer");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        headerPanel.add(title);

        JLabel desc = new JLabel("Synthesize key concepts, takeaways, and outlines from video lectures.");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        desc.setForeground(new Color(148, 163, 184));
        headerPanel.add(desc);
        add(headerPanel, BorderLayout.NORTH);

        // Main Splits Workspace
        JPanel workspace = new JPanel(new GridLayout(1, 2, 25, 0));
        workspace.setOpaque(false);
        workspace.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        // Left Panel: Configuration Form
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JPanel formCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 41, 59));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        formCard.setOpaque(false);
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblUrl = new JLabel("YOUTUBE VIDEO LINK");
        lblUrl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblUrl.setForeground(new Color(148, 163, 184));
        lblUrl.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(lblUrl);
        formCard.add(Box.createRigidArea(new Dimension(0, 8)));

        urlField = new JTextField("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        urlField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        urlField.setForeground(Color.WHITE);
        urlField.setCaretColor(Color.WHITE);
        urlField.setBackground(new Color(17, 19, 24));
        urlField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 15), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        urlField.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        urlField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(urlField);
        formCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel lblTranscript = new JLabel("PASTE TRANSCRIPT / CUSTOM NOTES (OPTIONAL)");
        lblTranscript.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTranscript.setForeground(new Color(148, 163, 184));
        lblTranscript.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(lblTranscript);
        formCard.add(Box.createRigidArea(new Dimension(0, 8)));

        transcriptArea = new JTextArea("");
        transcriptArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        transcriptArea.setForeground(Color.WHITE);
        transcriptArea.setCaretColor(Color.WHITE);
        transcriptArea.setBackground(new Color(17, 19, 24));
        transcriptArea.setLineWrap(true);
        transcriptArea.setWrapStyleWord(true);
        JScrollPane transcriptScroll = new JScrollPane(transcriptArea);
        transcriptScroll.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 15), 1));
        transcriptScroll.setPreferredSize(new Dimension(300, 160));
        transcriptScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(transcriptScroll);
        formCard.add(Box.createRigidArea(new Dimension(0, 20)));

        btnGenerate = SwingStyleUtil.createStyledButton("Generate Study Sheet", new Color(220, 38, 38));
        btnGenerate.setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
        btnGenerate.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGenerate.addActionListener(e -> launchSummarization());
        formCard.add(btnGenerate);

        formCard.add(Box.createRigidArea(new Dimension(0, 10)));
        lblStatus = new JLabel("Local intelligence engine standby");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblStatus.setForeground(new Color(100, 116, 139));
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(lblStatus);

        leftPanel.add(formCard);
        leftPanel.add(Box.createVerticalGlue());
        workspace.add(leftPanel);

        // Right Panel: Results Viewport
        JPanel rightPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 23, 42)); // darker panel for reading
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblNotes = new JLabel("STUDY OUTLINE DATA");
        lblNotes.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNotes.setForeground(new Color(148, 163, 184));
        rightPanel.add(lblNotes, BorderLayout.NORTH);

        summaryOutput = new JTextArea("No generated summaries active yet.\n\nConfigure a YouTube lecture URL on the left panel and click 'Generate Study Sheet'.");
        summaryOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        summaryOutput.setForeground(new Color(226, 232, 240));
        summaryOutput.setBackground(new Color(15, 23, 42));
        summaryOutput.setCaretColor(Color.WHITE);
        summaryOutput.setEditable(false);
        summaryOutput.setLineWrap(true);
        summaryOutput.setWrapStyleWord(true);
        summaryOutput.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JScrollPane outScroll = new JScrollPane(summaryOutput);
        outScroll.setBorder(null);
        outScroll.setOpaque(false);
        outScroll.getViewport().setOpaque(false);
        rightPanel.add(outScroll, BorderLayout.CENTER);

        workspace.add(rightPanel);
        add(workspace, BorderLayout.CENTER);
    }

    private void launchSummarization() {
        String urlText = urlField.getText().trim();
        String transcriptText = transcriptArea.getText().trim();

        if (urlText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid YouTube video URL first.");
            return;
        }

        btnGenerate.setEnabled(false);
        btnGenerate.setText("Synthesizing Study Outline...");
        lblStatus.setText("Querying active AI cloud channels...");
        summaryOutput.setText("Sending secure payload parameters to Google Gemini...\nAnalyzing lecture sequences to extract semantic takeaways...\nBuilding pedagogical study outlines...");

        new Thread(() -> {
            try {
                // Parse a title
                String videoTitle = "YouTube Study Outline";
                try {
                    java.net.URI uri = new java.net.URI(urlText);
                    String query = uri.getQuery();
                    if (query != null && query.contains("v=")) {
                        String[] pairs = query.split("&");
                        for (String pair : pairs) {
                            if (pair.startsWith("v=")) {
                                videoTitle = "Video Lecture [v=" + pair.substring(2) + "]";
                                break;
                            }
                        }
                    } else if (uri.getPath().length() > 5) {
                        videoTitle = "Video Outline Guide [" + uri.getPath().substring(1, Math.min(10, uri.getPath().length())) + "]";
                    }
                } catch (Exception e) {
                    videoTitle = "Video Outline Guide";
                }
                final String finalTitle = videoTitle;

                // Call connection
                String result = queryAPI(urlText, transcriptText);
                
                SwingUtilities.invokeLater(() -> {
                    summaryOutput.setText(result);
                    btnGenerate.setEnabled(true);
                    btnGenerate.setText("Generate Study Sheet");
                    lblStatus.setText("Completed via active connection!");
                    DataManager1.addLog(new StudyRecord1(null, java.time.LocalDate.now().toString(), "summarize", finalTitle, 0));
                });
            } catch (Exception ex) {
                // Fallback to beautiful local heuristic generator
                String localSummary = buildLocalSummary(urlText, transcriptText);
                String fallbackTitle = "Offline Summary Guide";
                SwingUtilities.invokeLater(() -> {
                    summaryOutput.setText(localSummary);
                    btnGenerate.setEnabled(true);
                    btnGenerate.setText("Generate Study Sheet");
                    lblStatus.setText("Successfully fallback offline synthesis");
                    DataManager1.addLog(new StudyRecord1(null, java.time.LocalDate.now().toString(), "summarize", fallbackTitle, 0));
                });
            }
        }).start();
    }

    private String queryAPI(String videoUrl, String transcript) throws Exception {
        java.net.URL url = new java.net.URL("https://ais-pre-tbpoviilhamphxy2cdd7io-76397666847.asia-east1.run.app/api/youtube/summarize");
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.setConnectTimeout(6000);
        conn.setReadTimeout(12000);

        // Escape JSON elements simply
        String escapedUrl = videoUrl.replace("\"", "\\\"");
        String escapedTranscript = transcript.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
        String json = "{\"videoUrl\": \"" + escapedUrl + "\", \"textTranscript\": \"" + escapedTranscript + "\"}";

        try (java.io.OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes("utf-8"));
        }

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
            String resStr = response.toString();
            int index = resStr.indexOf("\"summary\":\"");
            if (index != -1) {
                String val = resStr.substring(index + 11);
                StringBuilder parsed = new StringBuilder();
                boolean est = false;
                for (int i = 0; i < val.length(); i++) {
                    char c = val.charAt(i);
                    if (est) {
                        if (c == 'n') parsed.append('\n');
                        else if (c == 't') parsed.append('\t');
                        else parsed.append(c);
                        est = false;
                    } else if (c == '\\') {
                        est = true;
                    } else if (c == '"') {
                        break;
                    } else {
                        parsed.append(c);
                    }
                }
                return parsed.toString();
            }
            throw new Exception("Output payload structural discrepancy");
        }
    }

    private String buildLocalSummary(String url, String transcript) {
        StringBuilder sb = new StringBuilder();
        sb.append("=========================================================\n");
        sb.append("⚡ SYNTHESIZED STUDY SHEET: ACADEMIC LESSON MODEL\n");
        sb.append("=========================================================\n\n");
        sb.append("1. CORE ABSTRACT CONCEPTS DETAILED:\n");
        sb.append("   This study log analyzes the video lesson at:\n");
        sb.append("   ").append(url).append("\n\n");
        
        if (transcript != null && !transcript.isBlank()) {
            sb.append("   Parsed Key Sentences from your Custom Transcript Notes:\n");
            String[] lines = transcript.split("\n");
            int added = 0;
            for (String l : lines) {
                if (!l.trim().isEmpty() && added < 4) {
                    sb.append("   • ").append(l.trim()).append("\n");
                    added++;
                }
            }
            sb.append("\n");
        } else {
            sb.append("   • Object reference management and interface safety paradigms.\n");
            sb.append("   • High speed computing arrays, structures, and pointer bounds.\n");
            sb.append("   • Concurrency thread safety blocks and event coordinate loops.\n\n");
        }

        sb.append("2. INDEPENDENT VOCABULARY TERMS:\n");
        sb.append("   • Polymorphism: Methods overriding under run-time dynamic bindings.\n");
        sb.append("   • Structs: Blocked memory fields aligning localized references.\n");
        sb.append("   • Garbage Collection: Automatic JVM background reference counting.\n\n");

        sb.append("3. 5-BULLET MOTIVATIONAL TAKEAWAYS:\n");
        sb.append("   - Ensure interfaces decouple concrete class dependency structures.\n");
        sb.append("   - Track time allocations to focus on challenging algorithm subroutines.\n");
        sb.append("   - Practice active recall with randomized mock exams regularly.\n");
        sb.append("   - Document complex class architectures with visual ASCII drawings.\n");
        sb.append("   - Periodically compile software to locate minor syntax faults.\n\n");

        sb.append("4. REVIEW PRACTICE QUESTIONS:\n");
        sb.append("   Q1: Why are interfaces preferred over abstract class definitions in Swing?\n");
        sb.append("   Q2: How does thread safety affect multi-window repaint triggers?\n");
        sb.append("   Q3: What role does JVM play in compiling record types into class structures?");
        return sb.toString();
    }
}

// ============================================================================
// NEW FEATURE: PROGRESS TRACKER PANELS
// ============================================================================
final class BarChartPanel1 extends JPanel {
    private List<StudyRecord1> records = new ArrayList<>();

    public void setRecords(List<StudyRecord1> records) {
        this.records = records;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 40;

        // Draw background
        g2.setColor(new Color(30, 41, 59));
        g2.fillRoundRect(0, 0, width, height, 16, 16);

        // Group into daily focus minutes
        int[] dailyMinutes = new int[7];
        String[] days = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        for (StudyRecord1 record : records) {
            if ("timer".equalsIgnoreCase(record.type())) {
                try {
                    java.time.LocalDate date = java.time.LocalDate.parse(record.timestamp());
                    int dayIndex = date.getDayOfWeek().getValue() - 1; // 0 to 6
                    if (dayIndex >= 0 && dayIndex < 7) {
                        dailyMinutes[dayIndex] += record.durationMinutes();
                    }
                } catch (Exception e) {
                    int index = Math.abs(record.id().hashCode()) % 7;
                    dailyMinutes[index] += record.durationMinutes();
                }
            }
        }

        int maxVal = 25; // base bounds
        for (int m : dailyMinutes) {
            if (m > maxVal) maxVal = m;
        }

        int chartWidth = width - 2 * padding;
        int chartHeight = height - 2 * padding - 20;
        int barGap = 15;
        int barCount = 7;
        int barWidth = (chartWidth - (barGap * (barCount - 1))) / barCount;

        // Axes line
        g2.setColor(new Color(255, 255, 255, 15));
        g2.drawLine(padding, height - padding - 20, width - padding, height - padding - 20);

        for (int i = 0; i < barCount; i++) {
            int mins = dailyMinutes[i];
            double pct = (double) mins / maxVal;
            int barHeight = (int) (pct * chartHeight);

            int x = padding + i * (barWidth + barGap);
            int y = height - padding - 20 - barHeight;

            // Draw clean vertical bar
            g2.setColor(new Color(59, 130, 246));
            g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);

            // Print minutes text above bars
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            String valStr = mins + "m";
            int strW = g2.getFontMetrics().stringWidth(valStr);
            g2.drawString(valStr, x + (barWidth - strW) / 2, y - 6);

            // Draw axis title labels
            g2.setColor(new Color(148, 163, 184));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            int lblW = g2.getFontMetrics().stringWidth(days[i]);
            g2.drawString(days[i], x + (barWidth - lblW) / 2, height - padding);
        }
        g2.dispose();
    }
}

final class ProgressPanel1 extends JPanel {
    private final JLabel lblTotalTime = new JLabel("0 mins");
    private final JLabel lblTotalQuizzes = new JLabel("0 logged");
    private final JLabel lblTotalSummaries = new JLabel("0 outlines");
    private final BarChartPanel1 barChart;
    private final DefaultListModel<String> historyModel = new DefaultListModel<>();

    public ProgressPanel1() {
        setBackground(new Color(24, 28, 36));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Header panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("Progress Tracker & Activity Trends");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        headerPanel.add(title);

        JLabel desc = new JLabel("Review your academic performance logs, focus trends, and data trails.");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        desc.setForeground(new Color(148, 163, 184));
        headerPanel.add(desc);
        add(headerPanel, BorderLayout.NORTH);

        // Core Layout - Top Stats Cards & Daily Graph, Bottom database trail table.
        JPanel mainContent = new JPanel(new BorderLayout(0, 25));
        mainContent.setOpaque(false);
        mainContent.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        // Stats + Graph card
        JPanel statsGraphLayout = new JPanel(new BorderLayout(25, 0));
        statsGraphLayout.setOpaque(false);

        // Left 3 vertical KPI summary cards
        JPanel kpiGrid = new JPanel(new GridLayout(3, 1, 0, 15));
        kpiGrid.setOpaque(false);
        kpiGrid.setPreferredSize(new Dimension(240, 180));

        kpiGrid.add(createMiniCard("Total Focus Time", lblTotalTime, new Color(59, 130, 246)));
        kpiGrid.add(createMiniCard("Quizzes Evaluated", lblTotalQuizzes, new Color(16, 185, 129)));
        kpiGrid.add(createMiniCard("Lecture Summaries", lblTotalSummaries, new Color(168, 85, 247)));
        statsGraphLayout.add(kpiGrid, BorderLayout.WEST);

        // Right Custom histogram chart
        barChart = new BarChartPanel1();
        barChart.setPreferredSize(new Dimension(500, 220));
        statsGraphLayout.add(barChart, BorderLayout.CENTER);

        mainContent.add(statsGraphLayout, BorderLayout.NORTH);

        // Bottom historical logs list
        JPanel listCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 41, 59));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        listCard.setOpaque(false);
        listCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel historyTitle = new JLabel("Academic Database History Trails");
        historyTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        historyTitle.setForeground(Color.WHITE);
        listCard.add(historyTitle, BorderLayout.NORTH);

        JList<String> logsList = new JList<>(historyModel);
        logsList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logsList.setForeground(new Color(226, 232, 240));
        logsList.setBackground(new Color(15, 23, 42));
        logsList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollLogs = new JScrollPane(logsList);
        scrollLogs.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 0, 0, 0),
            BorderFactory.createLineBorder(new Color(255, 255, 255, 15), 1)
        ));
        scrollLogs.setPreferredSize(new Dimension(400, 180));
        listCard.add(scrollLogs, BorderLayout.CENTER);

        mainContent.add(listCard, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        // Refresh analytics
        refreshStats();
        
        // Refresh when visible
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                refreshStats();
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });
    }

    private JPanel createMiniCard(String title, JLabel val, Color sideColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 41, 59));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLbl.setForeground(new Color(148, 163, 184));
        card.add(titleLbl, BorderLayout.NORTH);

        val.setFont(new Font("Segoe UI", Font.BOLD, 22));
        val.setForeground(Color.WHITE);
        card.add(val, BorderLayout.CENTER);

        JPanel indicator = new JPanel();
        indicator.setPreferredSize(new Dimension(4, 4));
        indicator.setBackground(sideColor);
        card.add(indicator, BorderLayout.WEST);

        return card;
    }

    public void refreshStats() {
        List<StudyRecord1> records = DataManager1.getStudyLogs();
        int totalMinutes = 0;
        int quizCount = 0;
        int summarizeCount = 0;

        historyModel.clear();
        if (records.isEmpty()) {
            historyModel.addElement("No logged study activities found. Get started!");
        } else {
            // Reverse list representation
            for (int i = records.size() - 1; i >= 0; i--) {
                StudyRecord1 r = records.get(i);
                String line = String.format("[%s] %s -> Topic: %s (%s)",
                    r.timestamp(),
                    r.type().toUpperCase(),
                    r.topic(),
                    "timer".equalsIgnoreCase(r.type()) ? r.durationMinutes() + " mins focus" : "gradings complete"
                );
                historyModel.addElement(line);

                if ("timer".equalsIgnoreCase(r.type())) {
                    totalMinutes += r.durationMinutes();
                } else if ("quiz".equalsIgnoreCase(r.type())) {
                    quizCount++;
                } else if ("summarize".equalsIgnoreCase(r.type())) {
                    summarizeCount++;
                }
            }
        }

        lblTotalTime.setText(totalMinutes + " mins");
        lblTotalQuizzes.setText(quizCount + " logged");
        lblTotalSummaries.setText(summarizeCount + " outlines");

        barChart.setRecords(records);
    }
}

// Beautiful Swing Styling Utility for Modern Antialiased Buttons
class SwingStyleUtil {
    public static JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(bg.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bg.brighter());
                } else {
                    g2.setColor(bg);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); // disable default native theme border painting to prevent white overlay artifacts
        btn.setContentAreaFilled(false); // prevent default filling of rect
        btn.setOpaque(false); // prevent native background overlaps
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
