package com.burpsuiteclone;

import com.burpsuiteclone.ui.theme.BurpTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Main window replicating Burp Suite's professional layout.
 * All sections implemented to match real Burp Suite.
 */
public class BurpSuiteClone {
    public static void main(String[] args) {
        com.burpsuiteclone.ui.theme.ThemeManager.applyDarkTheme();

        SwingUtilities.invokeLater(() -> {
            createMainWindow();
        });
    }

    private static void createMainWindow() {
        JFrame frame = new JFrame("Burp Suite Clone");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 950);
        frame.setLocationRelativeTo(null);

        // === Menu Bar ===
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(BurpTheme.MENU_BAR);
        menuBar.setForeground(BurpTheme.TEXT_BODY);

        String[] menuNames = {"Burp", "Project", "Intruder", "Repeater", "Window", "Help"};
        for (String name : menuNames) {
            JMenu menu = new JMenu(name);
            menu.setForeground(BurpTheme.TEXT_BODY);
            menuBar.add(menu);
        }

        // Burp menu
        JMenu burpMenu = menuBar.getMenu(0);
        burpMenu.add(new JMenuItem("New project on disk"));
        burpMenu.add(new JMenuItem("Open project"));
        burpMenu.add(new JMenuItem("Save project"));
        burpMenu.addSeparator();
        burpMenu.add(new JMenuItem("User options"));
        burpMenu.add(new JMenuItem("Project options"));
        burpMenu.addSeparator();
        burpMenu.add(new JMenuItem("Exit"));

        // Project menu
        JMenu projMenu = menuBar.getMenu(1);
        projMenu.add(new JMenuItem("New scan"));
        projMenu.add(new JMenuItem("Scan queue"));
        projMenu.addSeparator();
        projMenu.add(new JMenuItem("Site map"));
        projMenu.add(new JMenuItem("Scope"));

        // Intruder menu
        JMenu intruderMenu = menuBar.getMenu(2);
        intruderMenu.add(new JMenuItem("New attack"));
        intruderMenu.add(new JMenuItem("Open saved attack"));

        // Repeater menu
        JMenu repeaterMenu = menuBar.getMenu(3);
        repeaterMenu.add(new JMenuItem("New tab"));
        repeaterMenu.add(new JMenuItem("Close tab"));

        frame.setJMenuBar(menuBar);

        // === Main Content ===
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BurpTheme.BG_2);

        // Top toolbar
        JToolBar toolbar = new JToolBar();
        toolbar.setBackground(BurpTheme.BG_1);
        toolbar.setFloatable(false);
        toolbar.setBorder(new EmptyBorder(4, 8, 4, 8));

        String[] toolButtons = {"New scan", "Scan queue", "Site map", "Proxy history", "Intruder", "Repeater", "Sequencer", "Decoder"};
        for (String btn : toolButtons) {
            JButton button = new JButton(btn);
            button.setBackground(BurpTheme.BUTTON_BG);
            button.setForeground(BurpTheme.BUTTON_FG);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            toolbar.add(button);
            toolbar.addSeparator(new Dimension(4, 0));
        }
        mainPanel.add(toolbar, BorderLayout.NORTH);

        // === Main Tabbed Pane ===
        JTabbedPane suiteTabs = new JTabbedPane(JTabbedPane.TOP);
        suiteTabs.setBackground(BurpTheme.TAB_BG);
        suiteTabs.setForeground(BurpTheme.TAB_FG);

        suiteTabs.addTab("Dashboard", createDashboardPanel());
        suiteTabs.addTab("Target", createTargetPanel());
        suiteTabs.addTab("Proxy", createProxyPanel());
        suiteTabs.addTab("Intruder", createIntruderPanel());
        suiteTabs.addTab("Repeater", createRepeaterPanel());
        suiteTabs.addTab("Sequencer", createSequencerPanel());
        suiteTabs.addTab("Decoder", createDecoderPanel());
        suiteTabs.addTab("Comparer", createComparerPanel());
        suiteTabs.addTab("Extender", createExtenderPanel());
        suiteTabs.addTab("Logger", createLoggerPanel());
        suiteTabs.addTab("Scanner", createScannerPanel());
        suiteTabs.addTab("Organizer", createOrganizerPanel());
        suiteTabs.addTab("Collaborator", createCollaboratorPanel());

        mainPanel.add(suiteTabs, BorderLayout.CENTER);

        // === Status Bar ===
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(BurpTheme.STATUSBAR_BG);
        statusBar.setBorder(new EmptyBorder(2, 8, 2, 8));
        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setForeground(BurpTheme.STATUSBAR_FG);
        statusBar.add(statusLabel, BorderLayout.WEST);

        JPanel rightStatus = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightStatus.setBackground(BurpTheme.STATUSBAR_BG);
        JLabel portLabel = new JLabel("Proxy: 8080");
        portLabel.setForeground(BurpTheme.STATUSBAR_FG);
        rightStatus.add(portLabel);
        JLabel scanLabel = new JLabel("Scan: Idle");
        scanLabel.setForeground(BurpTheme.STATUSBAR_FG);
        rightStatus.add(scanLabel);
        statusBar.add(rightStatus, BorderLayout.EAST);

        mainPanel.add(statusBar, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // === Dashboard Panel (exact Burp Suite replica with all buttons/panels) ===
    private static JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BurpTheme.BG_2);
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Top: 4 status cards in a row
        JPanel topRow = new JPanel(new GridLayout(1, 4, 1, 1));
        topRow.setBackground(BurpTheme.SEPARATOR);
        topRow.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BurpTheme.SEPARATOR));
        topRow.setPreferredSize(new Dimension(0, 72));
        topRow.add(createDashCard("Tasks", "No tasks running", "", BurpTheme.BLUE_CORE));
        topRow.add(createDashCard("Issues", "0 issues found", "High: 0  Medium: 0  Low: 0  Information: 0", BurpTheme.RED_CORE));
        topRow.add(createDashCard("Scan status", "Idle", "No scan running", BurpTheme.GREEN_CORE));
        topRow.add(createDashCard("Proxy", "Intercept off", "Proxy running on 127.0.0.1:8080", BurpTheme.ORANGE_CORE));
        panel.add(topRow, BorderLayout.NORTH);

        // Center: task list (top) + event log (bottom)
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setBackground(BurpTheme.SPLITPANE_BG);
        split.setDividerSize(3);
        split.setDividerLocation(280);
        split.setBorder(null);

        // Task list
        JPanel taskPanel = new JPanel(new BorderLayout(0, 0));
        taskPanel.setBackground(BurpTheme.BG_2);

        JPanel taskToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        taskToolbar.setBackground(BurpTheme.BG_2);
        taskToolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BurpTheme.SEPARATOR));
        JButton newScanBtn = new JButton("New scan");
        styleButton(newScanBtn, true);
        newScanBtn.addActionListener(e -> showNewScanDialog());
        taskToolbar.add(newScanBtn);
        JButton newTaskBtn = new JButton("New task");
        styleButton(newTaskBtn, false);
        newTaskBtn.addActionListener(e -> showNewTaskDialog());
        taskToolbar.add(newTaskBtn);
        taskToolbar.add(Box.createHorizontalStrut(8));
        JButton pauseBtn = new JButton("Pause");
        styleButton(pauseBtn, false);
        pauseBtn.addActionListener(e -> showTaskActionDialog("Pause", "Select a task to pause."));
        taskToolbar.add(pauseBtn);
        JButton resumeBtn = new JButton("Resume");
        styleButton(resumeBtn, false);
        resumeBtn.addActionListener(e -> showTaskActionDialog("Resume", "Select a task to resume."));
        taskToolbar.add(resumeBtn);
        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn, false);
        cancelBtn.addActionListener(e -> showTaskActionDialog("Cancel", "Select a task to cancel."));
        taskToolbar.add(cancelBtn);
        taskPanel.add(taskToolbar, BorderLayout.NORTH);

        String[] taskCols = {"Task", "Status", "Progress", "Issues", "Time started", "Time elapsed"};
        Object[][] taskData = {};
        JTable taskTable = createStyledTable(taskData, taskCols);
        taskTable.setRowHeight(20);
        taskTable.setShowGrid(true);
        taskTable.setGridColor(BurpTheme.TABLE_GRID);
        taskTable.setIntercellSpacing(new Dimension(0, 0));
        JScrollPane taskScroll = new JScrollPane(taskTable);
        taskScroll.setBorder(null);
        taskScroll.getViewport().setBackground(BurpTheme.TABLE_BG);
        taskPanel.add(taskScroll, BorderLayout.CENTER);
        split.setTopComponent(taskPanel);

        // Event log
        JPanel logPanel = new JPanel(new BorderLayout(0, 0));
        logPanel.setBackground(BurpTheme.BG_2);

        JPanel logToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        logToolbar.setBackground(BurpTheme.BG_2);
        logToolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BurpTheme.SEPARATOR));
        logToolbar.add(new JLabel("Filter:"));
        JTextField filterField = new JTextField(22);
        filterField.setBackground(BurpTheme.TEXTFIELD_BG);
        filterField.setForeground(BurpTheme.TEXT_BODY);
        filterField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BurpTheme.SEPARATOR),
            new EmptyBorder(2, 4, 2, 4)));
        logToolbar.add(filterField);
        logToolbar.add(new JLabel("Level:"));
        JComboBox<String> levelCombo = new JComboBox<>(new String[]{"All", "Info", "Warning", "Error", "Debug"});
        levelCombo.setBackground(BurpTheme.COMBOBOX_BG);
        levelCombo.setForeground(BurpTheme.TEXT_BODY);
        levelCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        logToolbar.add(levelCombo);
        logToolbar.add(new JLabel("Tool:"));
        JComboBox<String> toolCombo = new JComboBox<>(new String[]{"All", "Proxy", "Scanner", "Intruder", "Repeater", "Sequencer", "Extender", "Target"});
        toolCombo.setBackground(BurpTheme.COMBOBOX_BG);
        toolCombo.setForeground(BurpTheme.TEXT_BODY);
        toolCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        logToolbar.add(toolCombo);
        logPanel.add(logToolbar, BorderLayout.NORTH);

        String[] logCols = {"Time", "Tool", "Level", "Message"};
        Object[][] logData = {
            {"12:00:01", "Proxy", "Info", "Proxy listening on port 8080"},
            {"12:00:02", "Scanner", "Info", "Scanner ready"},
            {"12:00:03", "System", "Info", "Burp Suite Clone started successfully"}
        };
        JTable logTable = createStyledTable(logData, logCols);
        logTable.setRowHeight(18);
        logTable.setShowGrid(true);
        logTable.setGridColor(BurpTheme.TABLE_GRID);
        logTable.setIntercellSpacing(new Dimension(0, 0));
        JScrollPane logScroll = new JScrollPane(logTable);
        logScroll.setBorder(null);
        logScroll.getViewport().setBackground(BurpTheme.TABLE_BG);
        logPanel.add(logScroll, BorderLayout.CENTER);

        // Issue activity panel (bottom-right dock)
        com.burpsuiteclone.ui.issues.IssueActivityLog issueLog = new com.burpsuiteclone.ui.issues.IssueActivityLog();

        // Bottom dock: tabbed Event log + Issue activity
        JTabbedPane bottomDock = new JTabbedPane();
        bottomDock.setBackground(BurpTheme.BG_2);
        bottomDock.setForeground(BurpTheme.TEXT_BODY);
        bottomDock.addTab("Event log", logPanel);
        bottomDock.addTab("Issue activity", issueLog);
        split.setBottomComponent(bottomDock);

        panel.add(split, BorderLayout.CENTER);

        // Bottom bar
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        bottomBar.setBackground(BurpTheme.BG_1);
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BurpTheme.SEPARATOR));
        bottomBar.add(new JLabel("Quick:"));
        JButton scanBtn = new JButton("New scan");
        styleButton(scanBtn, true);
        scanBtn.addActionListener(e -> showNewScanDialog());
        bottomBar.add(scanBtn);
        JButton openProjBtn = new JButton("Open project");
        styleButton(openProjBtn, false);
        openProjBtn.addActionListener(e -> showOpenProjectDialog());
        bottomBar.add(openProjBtn);
        JButton userOptsBtn = new JButton("User options");
        styleButton(userOptsBtn, false);
        userOptsBtn.addActionListener(e -> showUserOptionsDialog());
        bottomBar.add(userOptsBtn);
        JButton projOptsBtn = new JButton("Project options");
        styleButton(projOptsBtn, false);
        projOptsBtn.addActionListener(e -> showProjectOptionsDialog());
        bottomBar.add(projOptsBtn);
        panel.add(bottomBar, BorderLayout.SOUTH);

        return panel;
    }

    private static void styleButton(JButton btn, boolean primary) {
        if (primary) {
            btn.setBackground(BurpTheme.BUTTON_PRIMARY);
            btn.setForeground(BurpTheme.TEXT_WHITE);
        } else {
            btn.setBackground(BurpTheme.BUTTON_BG);
            btn.setForeground(BurpTheme.BUTTON_FG);
        }
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    private static void showNewScanDialog() {
        JDialog dialog = new JDialog((Frame) null, "New Scan", true);
        dialog.setSize(750, 600);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(BurpTheme.BG_2);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(BurpTheme.BG_2);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Left: scan type selection
        JPanel leftPanel = new JPanel(new BorderLayout(4, 4));
        leftPanel.setBackground(BurpTheme.BG_2);
        leftPanel.setPreferredSize(new Dimension(170, 0));
        leftPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BurpTheme.SEPARATOR), "Scan type",
            0, 0, null, BurpTheme.TEXT_BODY));

        ButtonGroup scanTypeGroup = new ButtonGroup();
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        radioPanel.setBackground(BurpTheme.BG_2);
        radioPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JRadioButton crawlAndAudit = new JRadioButton("Crawl and audit", true);
        crawlAndAudit.setBackground(BurpTheme.BG_2);
        crawlAndAudit.setForeground(BurpTheme.TEXT_BODY);
        crawlAndAudit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        scanTypeGroup.add(crawlAndAudit);
        radioPanel.add(crawlAndAudit);
        radioPanel.add(Box.createVerticalStrut(4));

        JRadioButton crawlOnly = new JRadioButton("Crawl only");
        crawlOnly.setBackground(BurpTheme.BG_2);
        crawlOnly.setForeground(BurpTheme.TEXT_BODY);
        crawlOnly.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        scanTypeGroup.add(crawlOnly);
        radioPanel.add(crawlOnly);
        radioPanel.add(Box.createVerticalStrut(4));

        JRadioButton auditOnly = new JRadioButton("Audit only");
        auditOnly.setBackground(BurpTheme.BG_2);
        auditOnly.setForeground(BurpTheme.TEXT_BODY);
        auditOnly.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        scanTypeGroup.add(auditOnly);
        radioPanel.add(auditOnly);

        leftPanel.add(radioPanel, BorderLayout.CENTER);
        panel.add(leftPanel, BorderLayout.WEST);

        // Right: tabbed configuration
        JTabbedPane configTabs = new JTabbedPane();
        configTabs.setBackground(BurpTheme.TAB_BG);

        // === Scan details tab ===
        JPanel detailsPanel = new JPanel(new BorderLayout(8, 8));
        detailsPanel.setBackground(BurpTheme.BG_2);
        detailsPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel detailsForm = new JPanel(new GridBagLayout());
        detailsForm.setBackground(BurpTheme.BG_2);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        detailsForm.add(new JLabel("URLs to scan:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1; gbc.weighty = 1;
        JTextArea urlsArea = new JTextArea("https://example.com");
        urlsArea.setBackground(BurpTheme.EDITOR_BG);
        urlsArea.setForeground(BurpTheme.EDITOR_TEXT);
        urlsArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        urlsArea.setCaretColor(BurpTheme.TEXT_BODY);
        JScrollPane urlsScroll = new JScrollPane(urlsArea);
        urlsScroll.setBorder(BorderFactory.createLineBorder(BurpTheme.SEPARATOR));
        detailsForm.add(urlsScroll, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; gbc.weighty = 0;
        detailsForm.add(new JLabel("Scope:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JComboBox<String> scopeCombo = new JComboBox<>(new String[]{"Use suite scope", "Custom scope", "Scan everything (not recommended)"});
        scopeCombo.setBackground(BurpTheme.COMBOBOX_BG);
        scopeCombo.setForeground(BurpTheme.TEXT_BODY);
        detailsForm.add(scopeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        detailsForm.add(new JLabel("Protocol:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JComboBox<String> protocolCombo = new JComboBox<>(new String[]{"HTTP and HTTPS", "HTTP only", "HTTPS only"});
        protocolCombo.setBackground(BurpTheme.COMBOBOX_BG);
        protocolCombo.setForeground(BurpTheme.TEXT_BODY);
        detailsForm.add(protocolCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        detailsForm.add(new JLabel("Port (optional):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JTextField portField = new JTextField("");
        portField.setBackground(BurpTheme.TEXTFIELD_BG);
        portField.setForeground(BurpTheme.TEXT_BODY);
        detailsForm.add(portField, gbc);

        detailsPanel.add(detailsForm, BorderLayout.CENTER);

        // Info panel at bottom
        JPanel detailsInfo = new JPanel(new BorderLayout());
        detailsInfo.setBackground(BurpTheme.BG_3);
        detailsInfo.setBorder(new EmptyBorder(8, 8, 8, 8));
        JLabel infoLabel = new JLabel("<html><b>Note:</b> URLs will be added to the site map. Only in-scope URLs will be scanned.</html>");
        infoLabel.setForeground(BurpTheme.TEXT_DISABLED);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        detailsInfo.add(infoLabel, BorderLayout.CENTER);
        detailsPanel.add(detailsInfo, BorderLayout.SOUTH);

        configTabs.addTab("Scan details", detailsPanel);

        // === Scan configuration tab ===
        JPanel scanConfigPanel = new JPanel(new BorderLayout(8, 8));
        scanConfigPanel.setBackground(BurpTheme.BG_2);
        scanConfigPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel scTop = new JPanel(new BorderLayout(8, 4));
        scTop.setBackground(BurpTheme.BG_2);
        JPanel scLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        scLeft.setBackground(BurpTheme.BG_2);
        scLeft.add(new JLabel("Scan configuration:"));
        JComboBox<String> configCombo = new JComboBox<>(new String[]{"Crawl strategy - fastest", "Crawl strategy - thorough", "Audit checks - all", "Audit checks - (lightweight)", "Custom configuration"});
        configCombo.setBackground(BurpTheme.COMBOBOX_BG);
        configCombo.setForeground(BurpTheme.TEXT_BODY);
        scLeft.add(configCombo);
        scTop.add(scLeft, BorderLayout.WEST);
        JPanel scRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        scRight.setBackground(BurpTheme.BG_2);
        JButton editConfigBtn = new JButton("Edit...");
        styleButton(editConfigBtn, false);
        editConfigBtn.addActionListener(e -> showScanConfigEditor());
        scRight.add(editConfigBtn);
        JButton newConfigBtn = new JButton("New...");
        styleButton(newConfigBtn, false);
        newConfigBtn.addActionListener(e -> showScanConfigEditor());
        scRight.add(newConfigBtn);
        scTop.add(scRight, BorderLayout.EAST);
        scanConfigPanel.add(scTop, BorderLayout.NORTH);

        // Configuration categories
        JPanel configCategories = new JPanel(new BorderLayout(4, 4));
        configCategories.setBackground(BurpTheme.BG_2);
        configCategories.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BurpTheme.SEPARATOR), "Configuration details",
            0, 0, null, BurpTheme.TEXT_BODY));

        String[] configCols = {"Category", "Setting", "Value"};
        Object[][] configData = {
            {"Crawl", "Crawl depth", "Unlimited"},
            {"Crawl", "Max links", "5000"},
            {"Crawl", "Handle cookies", "Yes"},
            {"Crawl", "Follow redirects", "In-scope only"},
            {"Audit", "Active checks", "All"},
            {"Audit", "Passive checks", "All"},
            {"Audit", "Insertion points", "URL, body, cookies, headers"},
            {"Audit", "Issue consolidation", "Smart"},
            {"Resource", "Threads", "5"},
            {"Resource", "Delay (ms)", "0"},
            {"Resource", "Scan speed", "Normal"}
        };
        JTable configTable = createStyledTable(configData, configCols);
        configTable.setRowHeight(18);
        configCategories.add(new JScrollPane(configTable), BorderLayout.CENTER);
        scanConfigPanel.add(configCategories, BorderLayout.CENTER);

        // Built-in configs list
        JPanel builtinPanel = new JPanel(new BorderLayout(4, 4));
        builtinPanel.setBackground(BurpTheme.BG_2);
        builtinPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BurpTheme.SEPARATOR), "Built-in configurations",
            0, 0, null, BurpTheme.TEXT_BODY));
        String[] builtinCols = {"Name", "Type"};
        Object[][] builtinData = {
            {"Crawl strategy - fastest", "Crawl"},
            {"Crawl strategy - thorough", "Crawl"},
            {"Audit checks - all", "Audit"},
            {"Audit checks - (lightweight)", "Audit"},
            {"Crawl and audit - (default)", "Combined"}
        };
        JTable builtinTable = createStyledTable(builtinData, builtinCols);
        builtinTable.setRowHeight(18);
        builtinPanel.add(new JScrollPane(builtinTable), BorderLayout.CENTER);
        builtinPanel.setPreferredSize(new Dimension(0, 120));
        scanConfigPanel.add(builtinPanel, BorderLayout.SOUTH);

        configTabs.addTab("Scan configuration", scanConfigPanel);

        // === Application login tab ===
        JPanel loginPanel = new JPanel(new BorderLayout(8, 8));
        loginPanel.setBackground(BurpTheme.BG_2);
        loginPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel loginForm = new JPanel(new GridBagLayout());
        loginForm.setBackground(BurpTheme.BG_2);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(4, 4, 4, 4);
        gbc2.anchor = GridBagConstraints.WEST;

        gbc2.gridx = 0; gbc2.gridy = 0;
        loginForm.add(new JLabel("Login type:"), gbc2);
        gbc2.gridx = 1; gbc2.fill = GridBagConstraints.HORIZONTAL; gbc2.weightx = 1;
        JComboBox<String> loginType = new JComboBox<>(new String[]{"No login", "Form-based authentication", "HTTP basic authentication", "NTLM authentication", "Custom (session handling rules)"});
        loginType.setBackground(BurpTheme.COMBOBOX_BG);
        loginType.setForeground(BurpTheme.TEXT_BODY);
        loginForm.add(loginType, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 1; gbc2.fill = GridBagConstraints.NONE; gbc2.weightx = 0;
        loginForm.add(new JLabel("Username:"), gbc2);
        gbc2.gridx = 1; gbc2.fill = GridBagConstraints.HORIZONTAL; gbc2.weightx = 1;
        JTextField userField = new JTextField();
        userField.setBackground(BurpTheme.TEXTFIELD_BG);
        userField.setForeground(BurpTheme.TEXT_BODY);
        loginForm.add(userField, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 2; gbc2.fill = GridBagConstraints.NONE; gbc2.weightx = 0;
        loginForm.add(new JLabel("Password:"), gbc2);
        gbc2.gridx = 1; gbc2.fill = GridBagConstraints.HORIZONTAL; gbc2.weightx = 1;
        JPasswordField passField = new JPasswordField();
        passField.setBackground(BurpTheme.TEXTFIELD_BG);
        passField.setForeground(BurpTheme.TEXT_BODY);
        loginForm.add(passField, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 3; gbc2.fill = GridBagConstraints.NONE; gbc2.weightx = 0;
        loginForm.add(new JLabel("Login URL:"), gbc2);
        gbc2.gridx = 1; gbc2.fill = GridBagConstraints.HORIZONTAL; gbc2.weightx = 1;
        JTextField loginUrlField = new JTextField();
        loginUrlField.setBackground(BurpTheme.TEXTFIELD_BG);
        loginUrlField.setForeground(BurpTheme.TEXT_BODY);
        loginForm.add(loginUrlField, gbc2);

        loginPanel.add(loginForm, BorderLayout.NORTH);

        JPanel sessionPanel = new JPanel(new BorderLayout(4, 4));
        sessionPanel.setBackground(BurpTheme.BG_2);
        sessionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BurpTheme.SEPARATOR), "Session handling",
            0, 0, null, BurpTheme.TEXT_BODY));
        sessionPanel.add(new JLabel("Session handling rules and macros configured in Project options"), BorderLayout.CENTER);
        loginPanel.add(sessionPanel, BorderLayout.CENTER);

        configTabs.addTab("Application login", loginPanel);

        // === Resource pool tab ===
        JPanel resourcePanel = new JPanel(new BorderLayout(8, 8));
        resourcePanel.setBackground(BurpTheme.BG_2);
        resourcePanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel resourceForm = new JPanel(new GridBagLayout());
        resourceForm.setBackground(BurpTheme.BG_2);
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(4, 4, 4, 4);
        gbc3.anchor = GridBagConstraints.WEST;

        gbc3.gridx = 0; gbc3.gridy = 0;
        resourceForm.add(new JLabel("Resource pool:"), gbc3);
        gbc3.gridx = 1; gbc3.fill = GridBagConstraints.HORIZONTAL; gbc3.weightx = 1;
        JComboBox<String> poolCombo = new JComboBox<>(new String[]{"Default pool", "Custom pool"});
        poolCombo.setBackground(BurpTheme.COMBOBOX_BG);
        poolCombo.setForeground(BurpTheme.TEXT_BODY);
        resourceForm.add(poolCombo, gbc3);

        gbc3.gridx = 0; gbc3.gridy = 1; gbc3.fill = GridBagConstraints.NONE; gbc3.weightx = 0;
        resourceForm.add(new JLabel("Concurrent requests:"), gbc3);
        gbc3.gridx = 1; gbc3.fill = GridBagConstraints.HORIZONTAL; gbc3.weightx = 1;
        JTextField threadsField = new JTextField("5");
        threadsField.setBackground(BurpTheme.TEXTFIELD_BG);
        threadsField.setForeground(BurpTheme.TEXT_BODY);
        resourceForm.add(threadsField, gbc3);

        gbc3.gridx = 0; gbc3.gridy = 2; gbc3.fill = GridBagConstraints.NONE; gbc3.weightx = 0;
        resourceForm.add(new JLabel("Delay between requests (ms):"), gbc3);
        gbc3.gridx = 1; gbc3.fill = GridBagConstraints.HORIZONTAL; gbc3.weightx = 1;
        JTextField delayField = new JTextField("0");
        delayField.setBackground(BurpTheme.TEXTFIELD_BG);
        delayField.setForeground(BurpTheme.TEXT_BODY);
        resourceForm.add(delayField, gbc3);

        gbc3.gridx = 0; gbc3.gridy = 3; gbc3.fill = GridBagConstraints.NONE; gbc3.weightx = 0;
        resourceForm.add(new JLabel("Scan speed:"), gbc3);
        gbc3.gridx = 1; gbc3.fill = GridBagConstraints.HORIZONTAL; gbc3.weightx = 1;
        JComboBox<String> speedCombo = new JComboBox<>(new String[]{"Normal", "Fast (Pro)", "Thorough (Pro)"});
        speedCombo.setBackground(BurpTheme.COMBOBOX_BG);
        speedCombo.setForeground(BurpTheme.TEXT_BODY);
        resourceForm.add(speedCombo, gbc3);

        gbc3.gridx = 0; gbc3.gridy = 4; gbc3.fill = GridBagConstraints.NONE; gbc3.weightx = 0;
        resourceForm.add(new JLabel("Max retries:"), gbc3);
        gbc3.gridx = 1; gbc3.fill = GridBagConstraints.HORIZONTAL; gbc3.weightx = 1;
        JTextField retriesField = new JTextField("3");
        retriesField.setBackground(BurpTheme.TEXTFIELD_BG);
        retriesField.setForeground(BurpTheme.TEXT_BODY);
        resourceForm.add(retriesField, gbc3);

        gbc3.gridx = 0; gbc3.gridy = 5; gbc3.fill = GridBagConstraints.NONE; gbc3.weightx = 0;
        resourceForm.add(new JLabel("Throttle (req/s):"), gbc3);
        gbc3.gridx = 1; gbc3.fill = GridBagConstraints.HORIZONTAL; gbc3.weightx = 1;
        JTextField throttleField = new JTextField("0 (unlimited)");
        throttleField.setBackground(BurpTheme.TEXTFIELD_BG);
        throttleField.setForeground(BurpTheme.TEXT_BODY);
        resourceForm.add(throttleField, gbc3);

        resourcePanel.add(resourceForm, BorderLayout.NORTH);

        // Resource pool info
        JPanel poolInfo = new JPanel(new BorderLayout());
        poolInfo.setBackground(BurpTheme.BG_3);
        poolInfo.setBorder(new EmptyBorder(8, 8, 8, 8));
        JLabel poolLabel = new JLabel("<html><b>Default pool:</b> 5 concurrent requests, no delay, normal speed.<br>Custom pools can be created in Project options → Resource pools.</html>");
        poolLabel.setForeground(BurpTheme.TEXT_DISABLED);
        poolLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        poolInfo.add(poolLabel, BorderLayout.CENTER);
        resourcePanel.add(poolInfo, BorderLayout.CENTER);

        configTabs.addTab("Resource pool", resourcePanel);

        panel.add(configTabs, BorderLayout.CENTER);

        // Bottom buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(BurpTheme.BG_2);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BurpTheme.SEPARATOR));
        JButton okBtn = new JButton("Start scan");
        styleButton(okBtn, true);
        okBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(okBtn);
        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn, false);
        cancelBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(cancelBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private static void showScanConfigEditor() {
        JDialog dialog = new JDialog((Frame) null, "Scan Configuration", true);
        dialog.setSize(600, 450);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(BurpTheme.BG_2);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(BurpTheme.BG_2);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BurpTheme.TAB_BG);

        // Crawl settings
        JPanel crawlPanel = new JPanel(new GridBagLayout());
        crawlPanel.setBackground(BurpTheme.BG_2);
        crawlPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;
        crawlPanel.add(new JLabel("Crawl depth:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JComboBox<String> depthCombo = new JComboBox<>(new String[]{"Unlimited", "1", "2", "3", "5", "10"});
        depthCombo.setBackground(BurpTheme.COMBOBOX_BG);
        depthCombo.setForeground(BurpTheme.TEXT_BODY);
        crawlPanel.add(depthCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        crawlPanel.add(new JLabel("Max links to crawl:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JTextField maxLinks = new JTextField("5000");
        maxLinks.setBackground(BurpTheme.TEXTFIELD_BG);
        maxLinks.setForeground(BurpTheme.TEXT_BODY);
        crawlPanel.add(maxLinks, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        crawlPanel.add(new JLabel("Handle cookies:"), gbc);
        gbc.gridx = 1;
        crawlPanel.add(new JCheckBox("Yes", true), gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        crawlPanel.add(new JLabel("Follow redirects:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JComboBox<String> redirectCombo = new JComboBox<>(new String[]{"In-scope only", "All", "None"});
        redirectCombo.setBackground(BurpTheme.COMBOBOX_BG);
        redirectCombo.setForeground(BurpTheme.TEXT_BODY);
        crawlPanel.add(redirectCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        crawlPanel.add(new JLabel("Crawl JavaScript:"), gbc);
        gbc.gridx = 1;
        crawlPanel.add(new JCheckBox("Yes", true), gbc);
        tabs.addTab("Crawl", crawlPanel);

        // Audit settings
        JPanel auditPanel = new JPanel(new GridBagLayout());
        auditPanel.setBackground(BurpTheme.BG_2);
        auditPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(4, 4, 4, 4);
        gbc2.anchor = GridBagConstraints.WEST;
        gbc2.gridx = 0; gbc2.gridy = 0;
        auditPanel.add(new JLabel("Active scan checks:"), gbc2);
        gbc2.gridx = 1; gbc2.fill = GridBagConstraints.HORIZONTAL; gbc2.weightx = 1;
        JComboBox<String> activeChecks = new JComboBox<>(new String[]{"All", "Custom selection"});
        activeChecks.setBackground(BurpTheme.COMBOBOX_BG);
        activeChecks.setForeground(BurpTheme.TEXT_BODY);
        auditPanel.add(activeChecks, gbc2);
        gbc2.gridx = 0; gbc2.gridy = 1; gbc2.fill = GridBagConstraints.NONE; gbc2.weightx = 0;
        auditPanel.add(new JLabel("Passive scan checks:"), gbc2);
        gbc2.gridx = 1; gbc2.fill = GridBagConstraints.HORIZONTAL; gbc2.weightx = 1;
        JComboBox<String> passiveChecks = new JComboBox<>(new String[]{"All", "Custom selection"});
        passiveChecks.setBackground(BurpTheme.COMBOBOX_BG);
        passiveChecks.setForeground(BurpTheme.TEXT_BODY);
        auditPanel.add(passiveChecks, gbc2);
        gbc2.gridx = 0; gbc2.gridy = 2; gbc2.fill = GridBagConstraints.NONE; gbc2.weightx = 0;
        auditPanel.add(new JLabel("Insertion points:"), gbc2);
        gbc2.gridx = 1; gbc2.fill = GridBagConstraints.HORIZONTAL; gbc2.weightx = 1;
        JComboBox<String> insertPoints = new JComboBox<>(new String[]{"URL, body, cookies, headers", "URL only", "Body only", "Custom"});
        insertPoints.setBackground(BurpTheme.COMBOBOX_BG);
        insertPoints.setForeground(BurpTheme.TEXT_BODY);
        auditPanel.add(insertPoints, gbc2);
        gbc2.gridx = 0; gbc2.gridy = 3; gbc2.fill = GridBagConstraints.NONE; gbc2.weightx = 0;
        auditPanel.add(new JLabel("Issue consolidation:"), gbc2);
        gbc2.gridx = 1; gbc2.fill = GridBagConstraints.HORIZONTAL; gbc2.weightx = 1;
        JComboBox<String> consolidation = new JComboBox<>(new String[]{"Smart", "Per host and path", "Per host", "Per issue type"});
        consolidation.setBackground(BurpTheme.COMBOBOX_BG);
        consolidation.setForeground(BurpTheme.TEXT_BODY);
        auditPanel.add(consolidation, gbc2);
        tabs.addTab("Audit", auditPanel);

        panel.add(tabs, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(BurpTheme.BG_2);
        JButton okBtn = new JButton("OK");
        styleButton(okBtn, true);
        okBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(okBtn);
        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn, false);
        cancelBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(cancelBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }
    private static void showNewTaskDialog() {
        JDialog dialog = new JDialog((Frame) null, "New Task", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(BurpTheme.BG_2);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(BurpTheme.BG_2);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BurpTheme.BG_2);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Task type:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JComboBox<String> taskType = new JComboBox<>(new String[]{"Scan", "Crawl", "Audit", "Intruder attack"});
        taskType.setBackground(BurpTheme.COMBOBOX_BG);
        taskType.setForeground(BurpTheme.TEXT_BODY);
        form.add(taskType, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        form.add(new JLabel("Target:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JTextField targetField = new JTextField("https://example.com");
        targetField.setBackground(BurpTheme.TEXTFIELD_BG);
        targetField.setForeground(BurpTheme.TEXT_BODY);
        form.add(targetField, gbc);

        panel.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(BurpTheme.BG_2);
        JButton okBtn = new JButton("Create");
        styleButton(okBtn, true);
        okBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(okBtn);
        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn, false);
        cancelBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(cancelBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private static void showTaskActionDialog(String action, String message) {
        JOptionPane.showMessageDialog(null, message, action, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showOpenProjectDialog() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open Project");
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "Opened: " + chooser.getSelectedFile().getName(), "Project", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void showUserOptionsDialog() {
        JDialog dialog = new JDialog((Frame) null, "User Options", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(BurpTheme.BG_2);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(BurpTheme.BG_2);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BurpTheme.TAB_BG);
        tabs.addTab("Connections", new JLabel("Connection settings"));
        tabs.addTab("TLS", new JLabel("TLS settings"));
        tabs.addTab("Display", new JLabel("Display settings"));
        tabs.addTab("Misc", new JLabel("Miscellaneous settings"));
        panel.add(tabs, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(BurpTheme.BG_2);
        JButton okBtn = new JButton("OK");
        styleButton(okBtn, true);
        okBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(okBtn);
        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn, false);
        cancelBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(cancelBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private static void showProjectOptionsDialog() {
        JDialog dialog = new JDialog((Frame) null, "Project Options", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(BurpTheme.BG_2);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(BurpTheme.BG_2);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BurpTheme.TAB_BG);
        tabs.addTab("Connections", new JLabel("Connection settings"));
        tabs.addTab("Scanner", new JLabel("Scanner settings"));
        tabs.addTab("Intruder", new JLabel("Intruder settings"));
        tabs.addTab("Misc", new JLabel("Miscellaneous settings"));
        panel.add(tabs, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(BurpTheme.BG_2);
        JButton okBtn = new JButton("OK");
        styleButton(okBtn, true);
        okBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(okBtn);
        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn, false);
        cancelBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(cancelBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private static JPanel createDashCard(String title, String mainText, String subText, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 2));
        card.setBackground(BurpTheme.BG_3);
        card.setBorder(new EmptyBorder(8, 12, 8, 12));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(accent);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        card.add(titleLabel, BorderLayout.NORTH);
        JLabel mainLabel = new JLabel(mainText);
        mainLabel.setForeground(BurpTheme.TEXT_HEADER);
        mainLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(mainLabel, BorderLayout.CENTER);
        if (!subText.isEmpty()) {
            JLabel subLabel = new JLabel(subText);
            subLabel.setForeground(BurpTheme.TEXT_DISABLED);
            subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            card.add(subLabel, BorderLayout.SOUTH);
        }
        return card;
    }
    private static JPanel createTargetPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);
        JTabbedPane subTabs = new JTabbedPane();
        subTabs.setBackground(BurpTheme.TAB_BG);
        JPanel siteMapPanel = new JPanel(new BorderLayout());
        siteMapPanel.setBackground(BurpTheme.BG_2);
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(BurpTheme.BG_2);
        leftPanel.setPreferredSize(new Dimension(320, 0));
        JTree siteTree = new JTree();
        siteTree.setBackground(BurpTheme.TREE_BG);
        siteTree.setForeground(BurpTheme.TEXT_BODY);
        siteTree.setRootVisible(true);
        leftPanel.add(new JScrollPane(siteTree), BorderLayout.CENTER);
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(BurpTheme.BG_2);
        filterPanel.add(new JLabel("Filter:"));
        JTextField filterField = new JTextField(15);
        filterField.setBackground(BurpTheme.TEXTFIELD_BG);
        filterField.setForeground(BurpTheme.TEXT_BODY);
        filterPanel.add(filterField);
        leftPanel.add(filterPanel, BorderLayout.SOUTH);
        siteMapPanel.add(leftPanel, BorderLayout.WEST);
        String[] columns = {"Host", "Method", "URL", "Params", "Status", "Length", "MIME type", "Title", "Time"};
        Object[][] data = {};
        JTable siteTable = createStyledTable(data, columns);
        siteMapPanel.add(new JScrollPane(siteTable), BorderLayout.CENTER);
        subTabs.addTab("Site map", siteMapPanel);
        JPanel scopePanel = new JPanel(new BorderLayout());
        scopePanel.setBackground(BurpTheme.BG_2);
        JPanel scopeTop = new JPanel(new BorderLayout());
        scopeTop.setBackground(BurpTheme.BG_2);
        scopeTop.setBorder(new EmptyBorder(8, 8, 8, 8));
        JLabel scopeLabel = new JLabel("Target scope defines which hosts/URLs are in scope.");
        scopeLabel.setForeground(BurpTheme.TEXT_BODY);
        scopeTop.add(scopeLabel, BorderLayout.NORTH);
        JPanel scopeControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        scopeControls.setBackground(BurpTheme.BG_2);
        scopeControls.add(new JButton("Add"));
        scopeControls.add(new JButton("Remove"));
        scopeControls.add(new JButton("Edit"));
        scopeTop.add(scopeControls, BorderLayout.SOUTH);
        scopePanel.add(scopeTop, BorderLayout.NORTH);
        JSplitPane scopeSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        scopeSplit.setBackground(BurpTheme.SPLITPANE_BG);
        String[] incCols = {"Enabled", "Protocol", "Host", "Port", "Path"};
        Object[][] incData = {};
        JTable incTable = createStyledTable(incData, incCols);
        JScrollPane incScroll = new JScrollPane(incTable);
        incScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BurpTheme.SEPARATOR), "Include in scope", 0, 0, null, BurpTheme.TEXT_BODY));
        scopeSplit.setTopComponent(incScroll);
        String[] excCols = {"Enabled", "Protocol", "Host", "Port", "Path"};
        Object[][] excData = {};
        JTable excTable = createStyledTable(excData, excCols);
        JScrollPane excScroll = new JScrollPane(excTable);
        excScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BurpTheme.SEPARATOR), "Exclude from scope", 0, 0, null, BurpTheme.TEXT_BODY));
        scopeSplit.setBottomComponent(excScroll);
        scopeSplit.setDividerLocation(200);
        scopePanel.add(scopeSplit, BorderLayout.CENTER);
        subTabs.addTab("Scope", scopePanel);
        JPanel issuesPanel = new JPanel(new BorderLayout());
        issuesPanel.setBackground(BurpTheme.BG_2);
        String[] issueCols = {"Issue type", "Severity", "Confidence"};
        Object[][] issueData = {};
        JTable issueTable = createStyledTable(issueData, issueCols);
        issuesPanel.add(new JScrollPane(issueTable), BorderLayout.CENTER);
        subTabs.addTab("Issue definitions", issuesPanel);
        panel.add(subTabs, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createProxyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);
        JTabbedPane subTabs = new JTabbedPane();
        subTabs.setBackground(BurpTheme.TAB_BG);
        JPanel interceptPanel = new JPanel(new BorderLayout());
        interceptPanel.setBackground(BurpTheme.BG_2);
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        controlPanel.setBackground(BurpTheme.BG_2);
        JButton interceptBtn = new JButton("Intercept is on");
        interceptBtn.setBackground(BurpTheme.BUTTON_PRIMARY);
        interceptBtn.setForeground(BurpTheme.TEXT_WHITE);
        controlPanel.add(interceptBtn);
        JButton forwardBtn = new JButton("Forward");
        forwardBtn.setBackground(BurpTheme.BUTTON_BG);
        forwardBtn.setForeground(BurpTheme.BUTTON_FG);
        controlPanel.add(forwardBtn);
        JButton dropBtn = new JButton("Drop");
        dropBtn.setBackground(BurpTheme.BUTTON_DESTRUCTIVE);
        dropBtn.setForeground(BurpTheme.TEXT_WHITE);
        controlPanel.add(dropBtn);
        JButton actionBtn = new JButton("Action");
        actionBtn.setBackground(BurpTheme.BUTTON_BG);
        actionBtn.setForeground(BurpTheme.BUTTON_FG);
        controlPanel.add(actionBtn);
        controlPanel.add(new JSeparator(SwingConstants.VERTICAL));
        JButton openBrowserBtn = new JButton("Open browser");
        openBrowserBtn.setBackground(BurpTheme.BUTTON_BG);
        openBrowserBtn.setForeground(BurpTheme.BUTTON_FG);
        controlPanel.add(openBrowserBtn);
        interceptPanel.add(controlPanel, BorderLayout.NORTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBackground(BurpTheme.SPLITPANE_BG);
        JTextArea requestEditor = new JTextArea("GET / HTTP/1.1\nHost: example.com\nUser-Agent: Mozilla/5.0\n\n");
        requestEditor.setBackground(BurpTheme.EDITOR_BG);
        requestEditor.setForeground(BurpTheme.EDITOR_TEXT);
        requestEditor.setFont(new Font("Consolas", Font.PLAIN, 12));
        requestEditor.setCaretColor(BurpTheme.TEXT_BODY);
        splitPane.setLeftComponent(new JScrollPane(requestEditor));
        JTextArea responseEditor = new JTextArea("HTTP/1.1 200 OK\nContent-Type: text/html\nContent-Length: 1234\n\n<html>...</html>");
        responseEditor.setBackground(BurpTheme.EDITOR_BG);
        responseEditor.setForeground(BurpTheme.EDITOR_TEXT);
        responseEditor.setFont(new Font("Consolas", Font.PLAIN, 12));
        responseEditor.setCaretColor(BurpTheme.TEXT_BODY);
        splitPane.setRightComponent(new JScrollPane(responseEditor));
        splitPane.setDividerLocation(600);
        interceptPanel.add(splitPane, BorderLayout.CENTER);
        subTabs.addTab("Intercept", interceptPanel);
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(BurpTheme.BG_2);
        JPanel histToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        histToolbar.setBackground(BurpTheme.BG_2);
        histToolbar.add(new JLabel("Filter:"));
        JTextField histFilter = new JTextField(25);
        histFilter.setBackground(BurpTheme.TEXTFIELD_BG);
        histFilter.setForeground(BurpTheme.TEXT_BODY);
        histToolbar.add(histFilter);
        historyPanel.add(histToolbar, BorderLayout.NORTH);
        String[] histColumns = {"#", "Host", "Method", "URL", "Params", "Edited", "Status", "Length", "MIME type", "Title", "IP", "Time"};
        Object[][] histData = {};
        JTable histTable = createStyledTable(histData, histColumns);
        historyPanel.add(new JScrollPane(histTable), BorderLayout.CENTER);
        subTabs.addTab("HTTP history", historyPanel);
        JPanel wsPanel = new JPanel(new BorderLayout());
        wsPanel.setBackground(BurpTheme.BG_2);
        String[] wsColumns = {"#", "Host", "URL", "Messages", "Last message"};
        Object[][] wsData = {};
        JTable wsTable = createStyledTable(wsData, wsColumns);
        wsPanel.add(new JScrollPane(wsTable), BorderLayout.CENTER);
        subTabs.addTab("WebSockets history", wsPanel);
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setBackground(BurpTheme.BG_2);
        JTabbedPane optTabs = new JTabbedPane();
        optTabs.setBackground(BurpTheme.TAB_BG);
        JPanel listenersPanel = new JPanel(new BorderLayout());
        listenersPanel.setBackground(BurpTheme.BG_2);
        String[] listenCols = {"Running", "Interface", "Invisible", "Redirect", "TLS", "Support invisible"};
        Object[][] listenData = {{"true", "127.0.0.1:8080", "false", "", "false", "false"}};
        JTable listenTable = createStyledTable(listenData, listenCols);
        listenersPanel.add(new JScrollPane(listenTable), BorderLayout.CENTER);
        JPanel listenBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        listenBtns.setBackground(BurpTheme.BG_2);
        listenBtns.add(new JButton("Add"));
        listenBtns.add(new JButton("Edit"));
        listenBtns.add(new JButton("Remove"));
        listenersPanel.add(listenBtns, BorderLayout.SOUTH);
        optTabs.addTab("Proxy listeners", listenersPanel);
        JPanel interceptOpts = new JPanel(new BorderLayout());
        interceptOpts.setBackground(BurpTheme.BG_2);
        JPanel interceptOptsPanel = new JPanel();
        interceptOptsPanel.setLayout(new BoxLayout(interceptOptsPanel, BoxLayout.Y_AXIS));
        interceptOptsPanel.setBackground(BurpTheme.BG_2);
        interceptOptsPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        interceptOptsPanel.add(new JCheckBox("Intercept requests based on:"));
        interceptOptsPanel.add(new JCheckBox("  URL (regex)"));
        interceptOptsPanel.add(new JCheckBox("  File extension"));
        interceptOptsPanel.add(new JCheckBox("  HTTP method"));
        interceptOptsPanel.add(new JCheckBox("Intercept responses based on:"));
        interceptOptsPanel.add(new JCheckBox("  Content type"));
        interceptOptsPanel.add(new JCheckBox("  Status code"));
        interceptOpts.add(new JScrollPane(interceptOptsPanel), BorderLayout.CENTER);
        optTabs.addTab("Intercept", interceptOpts);
        JPanel matchReplace = new JPanel(new BorderLayout());
        matchReplace.setBackground(BurpTheme.BG_2);
        String[] mrCols = {"Enabled", "Item", "Match", "Replace", "Comment"};
        Object[][] mrData = {};
        JTable mrTable = createStyledTable(mrData, mrCols);
        matchReplace.add(new JScrollPane(mrTable), BorderLayout.CENTER);
        JPanel mrBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mrBtns.setBackground(BurpTheme.BG_2);
        mrBtns.add(new JButton("Add"));
        mrBtns.add(new JButton("Edit"));
        mrBtns.add(new JButton("Remove"));
        mrBtns.add(new JButton("Up"));
        mrBtns.add(new JButton("Down"));
        matchReplace.add(mrBtns, BorderLayout.SOUTH);
        optTabs.addTab("Match and replace", matchReplace);
        JPanel tlsPanel = new JPanel(new BorderLayout());
        tlsPanel.setBackground(BurpTheme.BG_2);
        tlsPanel.add(new JLabel("TLS pass-through and client certificates"), BorderLayout.CENTER);
        optTabs.addTab("TLS", tlsPanel);
        JPanel miscPanel = new JPanel(new BorderLayout());
        miscPanel.setBackground(BurpTheme.BG_2);
        miscPanel.add(new JLabel("Miscellaneous proxy settings"), BorderLayout.CENTER);
        optTabs.addTab("Misc", miscPanel);
        optionsPanel.add(optTabs, BorderLayout.CENTER);
        subTabs.addTab("Options", optionsPanel);
        panel.add(subTabs, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createIntruderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);
        JTabbedPane subTabs = new JTabbedPane();
        subTabs.setBackground(BurpTheme.TAB_BG);
        JPanel targetPanel = new JPanel(new BorderLayout());
        targetPanel.setBackground(BurpTheme.BG_2);
        JPanel targetForm = new JPanel(new GridBagLayout());
        targetForm.setBackground(BurpTheme.BG_2);
        targetForm.setBorder(new EmptyBorder(8, 8, 8, 8));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;
        targetForm.add(new JLabel("Target (host and port):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JTextField hostField = new JTextField("example.com");
        hostField.setBackground(BurpTheme.TEXTFIELD_BG);
        hostField.setForeground(BurpTheme.TEXT_BODY);
        targetForm.add(hostField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        targetForm.add(new JLabel("Port:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JTextField portField = new JTextField("443");
        portField.setBackground(BurpTheme.TEXTFIELD_BG);
        portField.setForeground(BurpTheme.TEXT_BODY);
        targetForm.add(portField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        targetForm.add(new JLabel("Use HTTPS:"), gbc);
        gbc.gridx = 1;
        targetForm.add(new JCheckBox(), gbc);
        targetPanel.add(targetForm, BorderLayout.NORTH);
        subTabs.addTab("Target", targetPanel);
        JPanel positionsPanel = new JPanel(new BorderLayout());
        positionsPanel.setBackground(BurpTheme.BG_2);
        JPanel posToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        posToolbar.setBackground(BurpTheme.BG_2);
        posToolbar.add(new JLabel("Attack type:"));
        JComboBox<String> attackType = new JComboBox<>(new String[]{"Sniper", "Battering ram", "Pitchfork", "Cluster bomb"});
        attackType.setBackground(BurpTheme.COMBOBOX_BG);
        attackType.setForeground(BurpTheme.TEXT_BODY);
        posToolbar.add(attackType);
        positionsPanel.add(posToolbar, BorderLayout.NORTH);
        JTextArea posEditor = new JTextArea("POST /login HTTP/1.1\nHost: example.com\nContent-Type: application/x-www-form-urlencoded\n\nusername=§admin§&password=§password§");
        posEditor.setBackground(BurpTheme.EDITOR_BG);
        posEditor.setForeground(BurpTheme.EDITOR_TEXT);
        posEditor.setFont(new Font("Consolas", Font.PLAIN, 12));
        posEditor.setCaretColor(BurpTheme.TEXT_BODY);
        positionsPanel.add(new JScrollPane(posEditor), BorderLayout.CENTER);
        JPanel posBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        posBtns.setBackground(BurpTheme.BG_2);
        posBtns.add(new JButton("Add §"));
        posBtns.add(new JButton("Clear §"));
        posBtns.add(new JButton("Auto §"));
        positionsPanel.add(posBtns, BorderLayout.SOUTH);
        subTabs.addTab("Positions", positionsPanel);
        JPanel payloadsPanel = new JPanel(new BorderLayout());
        payloadsPanel.setBackground(BurpTheme.BG_2);
        JPanel payloadTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        payloadTop.setBackground(BurpTheme.BG_2);
        payloadTop.add(new JLabel("Payload set:"));
        JComboBox<String> payloadSet = new JComboBox<>(new String[]{"1", "2", "3", "4"});
        payloadSet.setBackground(BurpTheme.COMBOBOX_BG);
        payloadSet.setForeground(BurpTheme.TEXT_BODY);
        payloadTop.add(payloadSet);
        payloadTop.add(new JLabel("Payload type:"));
        JComboBox<String> payloadType = new JComboBox<>(new String[]{"Simple list", "Numbers", "Dates", "Brute forcer", "Character frobber", "Case modifier", "Recursive grep", "Illegal Unicode", "Character substitution", "Runtime file", "Custom iterator", "Copy other payload"});
        payloadType.setBackground(BurpTheme.COMBOBOX_BG);
        payloadType.setForeground(BurpTheme.TEXT_BODY);
        payloadTop.add(payloadType);
        payloadsPanel.add(payloadTop, BorderLayout.NORTH);
        JTextArea payloadArea = new JTextArea("admin\nroot\ntest\nuser\nguest\nadministrator");
        payloadArea.setBackground(BurpTheme.EDITOR_BG);
        payloadArea.setForeground(BurpTheme.EDITOR_TEXT);
        payloadArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        payloadsPanel.add(new JScrollPane(payloadArea), BorderLayout.CENTER);
        JPanel payloadBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        payloadBtns.setBackground(BurpTheme.BG_2);
        payloadBtns.add(new JButton("Load..."));
        payloadBtns.add(new JButton("Paste"));
        payloadBtns.add(new JButton("Remove"));
        payloadsPanel.add(payloadBtns, BorderLayout.SOUTH);
        subTabs.addTab("Payloads", payloadsPanel);
        JPanel intruderOptions = new JPanel(new BorderLayout());
        intruderOptions.setBackground(BurpTheme.BG_2);
        JTabbedPane optTabs = new JTabbedPane();
        optTabs.setBackground(BurpTheme.TAB_BG);
        JPanel reqHeaders = new JPanel(new BorderLayout());
        reqHeaders.setBackground(BurpTheme.BG_2);
        reqHeaders.add(new JLabel("Request headers"), BorderLayout.CENTER);
        optTabs.addTab("Request headers", reqHeaders);
        JPanel grepMatch = new JPanel(new BorderLayout());
        grepMatch.setBackground(BurpTheme.BG_2);
        grepMatch.add(new JLabel("Grep - Match: flag responses containing these strings"), BorderLayout.NORTH);
        JTextArea grepArea = new JTextArea("Invalid password\nError\nException");
        grepArea.setBackground(BurpTheme.EDITOR_BG);
        grepArea.setForeground(BurpTheme.EDITOR_TEXT);
        grepMatch.add(new JScrollPane(grepArea), BorderLayout.CENTER);
        optTabs.addTab("Grep - Match", grepMatch);
        JPanel grepExtract = new JPanel(new BorderLayout());
        grepExtract.setBackground(BurpTheme.BG_2);
        grepExtract.add(new JLabel("Grep - Extract: extract items from responses"), BorderLayout.CENTER);
        optTabs.addTab("Grep - Extract", grepExtract);
        JPanel grepPayloads = new JPanel(new BorderLayout());
        grepPayloads.setBackground(BurpTheme.BG_2);
        grepPayloads.add(new JLabel("Grep - Payloads: flag payloads found in responses"), BorderLayout.CENTER);
        optTabs.addTab("Grep - Payloads", grepPayloads);
        JPanel redirPanel = new JPanel(new BorderLayout());
        redirPanel.setBackground(BurpTheme.BG_2);
        redirPanel.add(new JLabel("Redirections: follow redirects"), BorderLayout.CENTER);
        optTabs.addTab("Redirections", redirPanel);
        intruderOptions.add(optTabs, BorderLayout.CENTER);
        subTabs.addTab("Options", intruderOptions);
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(BurpTheme.BG_2);
        String[] resultCols = {"Request", "Payload", "Status", "Error", "Time", "Length", "Comment"};
        Object[][] resultData = {};
        JTable resultTable = createStyledTable(resultData, resultCols);
        resultsPanel.add(new JScrollPane(resultTable), BorderLayout.CENTER);
        JPanel resultToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resultToolbar.setBackground(BurpTheme.BG_2);
        resultToolbar.add(new JButton("Send to Repeater"));
        resultToolbar.add(new JButton("Send to Intruder"));
        resultToolbar.add(new JButton("Show response in browser"));
        resultsPanel.add(resultToolbar, BorderLayout.SOUTH);
        subTabs.addTab("Results", resultsPanel);
        panel.add(subTabs, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(BurpTheme.BG_2);
        JButton startBtn = new JButton("Start attack");
        startBtn.setBackground(BurpTheme.BUTTON_PRIMARY);
        startBtn.setForeground(BurpTheme.TEXT_WHITE);
        bottomPanel.add(startBtn);
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(BurpTheme.BUTTON_BG);
        saveBtn.setForeground(BurpTheme.BUTTON_FG);
        bottomPanel.add(saveBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    private static JPanel createRepeaterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);
        JTabbedPane repeaterTabs = new JTabbedPane();
        repeaterTabs.setBackground(BurpTheme.TAB_BG);
        JPanel tab1 = new JPanel(new BorderLayout());
        tab1.setBackground(BurpTheme.BG_2);
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(BurpTheme.BG_2);
        JButton sendBtn = new JButton("Send");
        sendBtn.setBackground(BurpTheme.BUTTON_PRIMARY);
        sendBtn.setForeground(BurpTheme.TEXT_WHITE);
        toolbar.add(sendBtn);
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(BurpTheme.BUTTON_BG);
        cancelBtn.setForeground(BurpTheme.BUTTON_FG);
        toolbar.add(cancelBtn);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        JButton followBtn = new JButton("Follow redirections: Never");
        followBtn.setBackground(BurpTheme.BUTTON_BG);
        followBtn.setForeground(BurpTheme.BUTTON_FG);
        toolbar.add(followBtn);
        tab1.add(toolbar, BorderLayout.NORTH);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setBackground(BurpTheme.SPLITPANE_BG);
        JTextArea reqEditor = new JTextArea("GET / HTTP/1.1\nHost: example.com\nUser-Agent: Mozilla/5.0\nAccept: */*\n\n");
        reqEditor.setBackground(BurpTheme.EDITOR_BG);
        reqEditor.setForeground(BurpTheme.EDITOR_TEXT);
        reqEditor.setFont(new Font("Consolas", Font.PLAIN, 12));
        reqEditor.setCaretColor(BurpTheme.TEXT_BODY);
        split.setLeftComponent(new JScrollPane(reqEditor));
        JTextArea respEditor = new JTextArea("HTTP/1.1 200 OK\nContent-Type: text/html\nContent-Length: 1234\n\n<html><body>Hello</body></html>");
        respEditor.setBackground(BurpTheme.EDITOR_BG);
        respEditor.setForeground(BurpTheme.EDITOR_TEXT);
        respEditor.setFont(new Font("Consolas", Font.PLAIN, 12));
        respEditor.setCaretColor(BurpTheme.TEXT_BODY);
        split.setRightComponent(new JScrollPane(respEditor));
        split.setDividerLocation(600);
        tab1.add(split, BorderLayout.CENTER);
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(BurpTheme.BG_1);
        statusPanel.add(new JLabel("Status: 200 OK | Length: 1234 | Time: 45ms"));
        tab1.add(statusPanel, BorderLayout.SOUTH);
        repeaterTabs.addTab("Repeater 1", tab1);
        panel.add(repeaterTabs, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(BurpTheme.BG_2);
        JButton newTabBtn = new JButton("+ New tab");
        newTabBtn.setBackground(BurpTheme.BUTTON_BG);
        newTabBtn.setForeground(BurpTheme.BUTTON_FG);
        bottomPanel.add(newTabBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    private static JPanel createSequencerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);
        JTabbedPane subTabs = new JTabbedPane();
        subTabs.setBackground(BurpTheme.TAB_BG);
        JPanel livePanel = new JPanel(new BorderLayout());
        livePanel.setBackground(BurpTheme.BG_2);
        livePanel.add(new JLabel("Live capture: configure token location and start capture"), BorderLayout.CENTER);
        subTabs.addTab("Live capture", livePanel);
        JPanel manualPanel = new JPanel(new BorderLayout());
        manualPanel.setBackground(BurpTheme.BG_2);
        manualPanel.add(new JLabel("Manual load: paste tokens for analysis"), BorderLayout.CENTER);
        subTabs.addTab("Manual load", manualPanel);
        JPanel analysisPanel = new JPanel(new BorderLayout());
        analysisPanel.setBackground(BurpTheme.BG_2);
        analysisPanel.add(new JLabel("Analysis results: entropy, correlation, etc."), BorderLayout.CENTER);
        subTabs.addTab("Analysis", analysisPanel);
        panel.add(subTabs, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createDecoderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(BurpTheme.BG_2);
        topPanel.add(new JLabel("Mode:"));
        JComboBox<String> modeCombo = new JComboBox<>(new String[]{"Decode", "Encode", "Hash", "Smart decode"});
        modeCombo.setBackground(BurpTheme.COMBOBOX_BG);
        modeCombo.setForeground(BurpTheme.TEXT_BODY);
        topPanel.add(modeCombo);
        topPanel.add(new JLabel("Encoding:"));
        JComboBox<String> encCombo = new JComboBox<>(new String[]{"Plain", "Base64", "URL", "HTML", "Hex", "ASCII", "Octal", "Binary", "Gzip", "Deflate", "Raw"});
        encCombo.setBackground(BurpTheme.COMBOBOX_BG);
        encCombo.setForeground(BurpTheme.TEXT_BODY);
        topPanel.add(encCombo);
        JButton decodeBtn = new JButton("Decode");
        decodeBtn.setBackground(BurpTheme.BUTTON_PRIMARY);
        decodeBtn.setForeground(BurpTheme.TEXT_WHITE);
        topPanel.add(decodeBtn);
        JButton encodeBtn = new JButton("Encode");
        encodeBtn.setBackground(BurpTheme.BUTTON_BG);
        encodeBtn.setForeground(BurpTheme.BUTTON_FG);
        topPanel.add(encodeBtn);
        panel.add(topPanel, BorderLayout.NORTH);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setBackground(BurpTheme.SPLITPANE_BG);
        JTextArea inputArea = new JTextArea("Enter text here...");
        inputArea.setBackground(BurpTheme.EDITOR_BG);
        inputArea.setForeground(BurpTheme.EDITOR_TEXT);
        inputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        inputArea.setCaretColor(BurpTheme.TEXT_BODY);
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BurpTheme.SEPARATOR), "Input", 0, 0, null, BurpTheme.TEXT_BODY));
        split.setTopComponent(inputScroll);
        JTextArea outputArea = new JTextArea("Output appears here...");
        outputArea.setBackground(BurpTheme.EDITOR_BG);
        outputArea.setForeground(BurpTheme.EDITOR_TEXT);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        outputArea.setCaretColor(BurpTheme.TEXT_BODY);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BurpTheme.SEPARATOR), "Output", 0, 0, null, BurpTheme.TEXT_BODY));
        split.setBottomComponent(outputScroll);
        split.setDividerLocation(250);
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createComparerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(BurpTheme.BG_2);
        topPanel.add(new JLabel("Compare:"));
        JComboBox<String> compareType = new JComboBox<>(new String[]{"Text", "Words", "Bytes"});
        compareType.setBackground(BurpTheme.COMBOBOX_BG);
        compareType.setForeground(BurpTheme.TEXT_BODY);
        topPanel.add(compareType);
        JButton paste1 = new JButton("Paste 1");
        paste1.setBackground(BurpTheme.BUTTON_BG);
        paste1.setForeground(BurpTheme.BUTTON_FG);
        topPanel.add(paste1);
        JButton paste2 = new JButton("Paste 2");
        paste2.setBackground(BurpTheme.BUTTON_BG);
        paste2.setForeground(BurpTheme.BUTTON_FG);
        topPanel.add(paste2);
        JButton compareBtn = new JButton("Compare");
        compareBtn.setBackground(BurpTheme.BUTTON_PRIMARY);
        compareBtn.setForeground(BurpTheme.TEXT_WHITE);
        topPanel.add(compareBtn);
        panel.add(topPanel, BorderLayout.NORTH);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setBackground(BurpTheme.SPLITPANE_BG);
        JTextArea leftArea = new JTextArea("Paste or type text 1...");
        leftArea.setBackground(BurpTheme.EDITOR_BG);
        leftArea.setForeground(BurpTheme.EDITOR_TEXT);
        leftArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        leftArea.setCaretColor(BurpTheme.TEXT_BODY);
        JScrollPane leftScroll = new JScrollPane(leftArea);
        leftScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BurpTheme.SEPARATOR), "Text 1", 0, 0, null, BurpTheme.TEXT_BODY));
        split.setLeftComponent(leftScroll);
        JTextArea rightArea = new JTextArea("Paste or type text 2...");
        rightArea.setBackground(BurpTheme.EDITOR_BG);
        rightArea.setForeground(BurpTheme.EDITOR_TEXT);
        rightArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        rightArea.setCaretColor(BurpTheme.TEXT_BODY);
        JScrollPane rightScroll = new JScrollPane(rightArea);
        rightScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BurpTheme.SEPARATOR), "Text 2", 0, 0, null, BurpTheme.TEXT_BODY));
        split.setRightComponent(rightScroll);
        split.setDividerLocation(600);
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createExtenderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);
        JTabbedPane subTabs = new JTabbedPane();
        subTabs.setBackground(BurpTheme.TAB_BG);
        JPanel extensionsPanel = new JPanel(new BorderLayout());
        extensionsPanel.setBackground(BurpTheme.BG_2);
        String[] extColumns = {"Loaded", "Type", "Name", "Filename", "State"};
        Object[][] extData = {};
        JTable extTable = createStyledTable(extData, extColumns);
        extensionsPanel.add(new JScrollPane(extTable), BorderLayout.CENTER);
        JPanel extBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        extBtnPanel.setBackground(BurpTheme.BG_2);
        JButton addBtn = new JButton("Add");
        addBtn.setBackground(BurpTheme.BUTTON_PRIMARY);
        addBtn.setForeground(BurpTheme.TEXT_WHITE);
        extBtnPanel.add(addBtn);
        JButton removeBtn = new JButton("Remove");
        removeBtn.setBackground(BurpTheme.BUTTON_DESTRUCTIVE);
        removeBtn.setForeground(BurpTheme.TEXT_WHITE);
        extBtnPanel.add(removeBtn);
        JButton upBtn = new JButton("Up");
        upBtn.setBackground(BurpTheme.BUTTON_BG);
        upBtn.setForeground(BurpTheme.BUTTON_FG);
        extBtnPanel.add(upBtn);
        JButton downBtn = new JButton("Down");
        downBtn.setBackground(BurpTheme.BUTTON_BG);
        downBtn.setForeground(BurpTheme.BUTTON_FG);
        extBtnPanel.add(downBtn);
        extensionsPanel.add(extBtnPanel, BorderLayout.SOUTH);
        subTabs.addTab("Extensions", extensionsPanel);
        JPanel bappPanel = new JPanel(new BorderLayout());
        bappPanel.setBackground(BurpTheme.BG_2);
        JPanel bappTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bappTop.setBackground(BurpTheme.BG_2);
        bappTop.add(new JLabel("Filter:"));
        JTextField bappFilter = new JTextField(20);
        bappFilter.setBackground(BurpTheme.TEXTFIELD_BG);
        bappFilter.setForeground(BurpTheme.TEXT_BODY);
        bappTop.add(bappFilter);
        bappPanel.add(bappTop, BorderLayout.NORTH);
        String[] bappCols = {"Name", "Author", "Version", "Rating", "Status"};
        Object[][] bappData = {};
        JTable bappTable = createStyledTable(bappData, bappCols);
        bappPanel.add(new JScrollPane(bappTable), BorderLayout.CENTER);
        subTabs.addTab("BApp Store", bappPanel);
        JPanel apisPanel = new JPanel(new BorderLayout());
        apisPanel.setBackground(BurpTheme.BG_2);
        String[] apiCols = {"API", "Version", "Status"};
        Object[][] apiData = {{"Montoya API", "2026.1", "Available"}, {"Legacy Extension API", "1.0", "Available"}};
        JTable apiTable = createStyledTable(apiData, apiCols);
        apisPanel.add(new JScrollPane(apiTable), BorderLayout.CENTER);
        subTabs.addTab("APIs", apisPanel);
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBackground(BurpTheme.BG_2);
        JTextArea outputArea = new JTextArea();
        outputArea.setBackground(BurpTheme.EDITOR_BG);
        outputArea.setForeground(BurpTheme.EDITOR_TEXT);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        outputArea.setEditable(false);
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        subTabs.addTab("Output", outputPanel);
        JPanel errorsPanel = new JPanel(new BorderLayout());
        errorsPanel.setBackground(BurpTheme.BG_2);
        JTextArea errorsArea = new JTextArea();
        errorsArea.setBackground(BurpTheme.EDITOR_BG);
        errorsArea.setForeground(BurpTheme.EDITOR_TEXT);
        errorsArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        errorsArea.setEditable(false);
        errorsPanel.add(new JScrollPane(errorsArea), BorderLayout.CENTER);
        subTabs.addTab("Errors", errorsPanel);
        panel.add(subTabs, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createLoggerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(BurpTheme.BG_2);
        topPanel.add(new JLabel("Filter:"));
        JTextField filterField = new JTextField(25);
        filterField.setBackground(BurpTheme.TEXTFIELD_BG);
        filterField.setForeground(BurpTheme.TEXT_BODY);
        topPanel.add(filterField);
        topPanel.add(new JLabel("Level:"));
        JComboBox<String> levelCombo = new JComboBox<>(new String[]{"All", "Info", "Debug", "Warning", "Error"});
        levelCombo.setBackground(BurpTheme.COMBOBOX_BG);
        levelCombo.setForeground(BurpTheme.TEXT_BODY);
        topPanel.add(levelCombo);
        panel.add(topPanel, BorderLayout.NORTH);
        String[] logCols = {"#", "Time", "Tool", "Direction", "Host", "Method", "URL", "Status", "Length"};
        Object[][] logData = {};
        JTable logTable = createStyledTable(logData, logCols);
        panel.add(new JScrollPane(logTable), BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createScannerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);
        JTabbedPane subTabs = new JTabbedPane();
        subTabs.setBackground(BurpTheme.TAB_BG);
        JPanel dashPanel = new JPanel(new BorderLayout());
        dashPanel.setBackground(BurpTheme.BG_2);
        JPanel scanCards = new JPanel(new GridLayout(1, 3, 8, 0));
        scanCards.setBackground(BurpTheme.BG_2);
        scanCards.add(createDashCard("Scan status", "Idle", "No scan running", BurpTheme.GREEN_CORE));
        scanCards.add(createDashCard("Issues found", "0", "High:0 Med:0 Low:0", BurpTheme.RED_CORE));
        scanCards.add(createDashCard("Requests sent", "0", "0 requests", BurpTheme.BLUE_CORE));
        dashPanel.add(scanCards, BorderLayout.NORTH);
        String[] scanCols = {"Task", "Target", "Status", "Progress", "Issues", "Time"};
        Object[][] scanData = {};
        JTable scanTable = createStyledTable(scanData, scanCols);
        dashPanel.add(new JScrollPane(scanTable), BorderLayout.CENTER);
        JPanel scanBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        scanBtns.setBackground(BurpTheme.BG_2);
        JButton newScanBtn = new JButton("New scan");
        newScanBtn.setBackground(BurpTheme.BUTTON_PRIMARY);
        newScanBtn.setForeground(BurpTheme.TEXT_WHITE);
        scanBtns.add(newScanBtn);
        scanBtns.add(new JButton("Pause"));
        scanBtns.add(new JButton("Resume"));
        scanBtns.add(new JButton("Cancel"));
        dashPanel.add(scanBtns, BorderLayout.SOUTH);
        subTabs.addTab("Dashboard", dashPanel);
        JPanel crawlPanel = new JPanel(new BorderLayout());
        crawlPanel.setBackground(BurpTheme.BG_2);
        JPanel crawlConfig = new JPanel(new GridBagLayout());
        crawlConfig.setBackground(BurpTheme.BG_2);
        crawlConfig.setBorder(new EmptyBorder(8, 8, 8, 8));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;
        crawlConfig.add(new JLabel("Target URL:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JTextField crawlUrl = new JTextField("https://example.com");
        crawlUrl.setBackground(BurpTheme.TEXTFIELD_BG);
        crawlUrl.setForeground(BurpTheme.TEXT_BODY);
        crawlConfig.add(crawlUrl, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        crawlConfig.add(new JLabel("Max depth:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JTextField depthField = new JTextField("5");
        depthField.setBackground(BurpTheme.TEXTFIELD_BG);
        depthField.setForeground(BurpTheme.TEXT_BODY);
        crawlConfig.add(depthField, gbc);
        crawlPanel.add(crawlConfig, BorderLayout.NORTH);
        String[] crawlCols = {"URL", "Status", "Links", "Forms", "Parameters"};
        Object[][] crawlData = {};
        JTable crawlTable = createStyledTable(crawlData, crawlCols);
        crawlPanel.add(new JScrollPane(crawlTable), BorderLayout.CENTER);
        subTabs.addTab("Crawl", crawlPanel);
        JPanel auditPanel = new JPanel(new BorderLayout());
        auditPanel.setBackground(BurpTheme.BG_2);
        JPanel auditConfig = new JPanel(new FlowLayout(FlowLayout.LEFT));
        auditConfig.setBackground(BurpTheme.BG_2);
        auditConfig.add(new JLabel("Configuration:"));
        JComboBox<String> auditConfigCombo = new JComboBox<>(new String[]{"Crawl and audit", "Audit only", "Crawl only", "Custom"});
        auditConfigCombo.setBackground(BurpTheme.COMBOBOX_BG);
        auditConfigCombo.setForeground(BurpTheme.TEXT_BODY);
        auditConfig.add(auditConfigCombo);
        auditConfig.add(new JButton("Edit config"));
        auditPanel.add(auditConfig, BorderLayout.NORTH);
        String[] issueCols = {"Severity", "Confidence", "Host", "Path", "Issue type"};
        Object[][] issueData = {};
        JTable issueTable = createStyledTable(issueData, issueCols);
        auditPanel.add(new JScrollPane(issueTable), BorderLayout.CENTER);
        JPanel auditBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        auditBtns.setBackground(BurpTheme.BG_2);
        auditBtns.add(new JButton("View issue"));
        auditBtns.add(new JButton("Mark false positive"));
        auditBtns.add(new JButton("Generate report"));
        auditPanel.add(auditBtns, BorderLayout.SOUTH);
        subTabs.addTab("Audit", auditPanel);
        JPanel issuesPanel = new JPanel(new BorderLayout());
        issuesPanel.setBackground(BurpTheme.BG_2);
        String[] allIssueCols = {"#", "Severity", "Confidence", "Host", "Path", "Issue type", "Status"};
        Object[][] allIssueData = {};
        JTable allIssueTable = createStyledTable(allIssueData, allIssueCols);
        issuesPanel.add(new JScrollPane(allIssueTable), BorderLayout.CENTER);
        subTabs.addTab("Issues", issuesPanel);
        panel.add(subTabs, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createOrganizerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(BurpTheme.BG_2);
        topPanel.add(new JLabel("Filter:"));
        JTextField filterField = new JTextField(20);
        filterField.setBackground(BurpTheme.TEXTFIELD_BG);
        filterField.setForeground(BurpTheme.TEXT_BODY);
        topPanel.add(filterField);
        topPanel.add(new JLabel("Status:"));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"All", "New", "In progress", "Postponed", "Ignored", "Done"});
        statusCombo.setBackground(BurpTheme.COMBOBOX_BG);
        statusCombo.setForeground(BurpTheme.TEXT_BODY);
        topPanel.add(statusCombo);
        panel.add(topPanel, BorderLayout.NORTH);
        String[] orgCols = {"#", "Status", "Host", "Method", "URL", "Comment", "Time"};
        Object[][] orgData = {};
        JTable orgTable = createStyledTable(orgData, orgCols);
        panel.add(new JScrollPane(orgTable), BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(BurpTheme.BG_2);
        btnPanel.add(new JButton("Send to Repeater"));
        btnPanel.add(new JButton("Send to Intruder"));
        btnPanel.add(new JButton("Send to Comparer"));
        btnPanel.add(new JButton("Comment"));
        btnPanel.add(new JButton("Highlight"));
        btnPanel.add(new JButton("Delete"));
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private static JPanel createCollaboratorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);
        JTabbedPane subTabs = new JTabbedPane();
        subTabs.setBackground(BurpTheme.TAB_BG);
        JPanel clientPanel = new JPanel(new BorderLayout());
        clientPanel.setBackground(BurpTheme.BG_2);
        JPanel clientTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        clientTop.setBackground(BurpTheme.BG_2);
        JButton createClientBtn = new JButton("Create client");
        createClientBtn.setBackground(BurpTheme.BUTTON_PRIMARY);
        createClientBtn.setForeground(BurpTheme.TEXT_WHITE);
        clientTop.add(createClientBtn);
        clientTop.add(new JButton("Restore client"));
        clientTop.add(new JLabel("Secret key:"));
        JTextField keyField = new JTextField(30);
        keyField.setBackground(BurpTheme.TEXTFIELD_BG);
        keyField.setForeground(BurpTheme.TEXT_BODY);
        clientTop.add(keyField);
        clientPanel.add(clientTop, BorderLayout.NORTH);
        JPanel payloadPanel = new JPanel(new BorderLayout());
        payloadPanel.setBackground(BurpTheme.BG_2);
        payloadPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        payloadPanel.add(new JLabel("Generated payloads appear here"), BorderLayout.NORTH);
        JTextArea payloadArea = new JTextArea("http://abc123.burpcollaborator.net\n");
        payloadArea.setBackground(BurpTheme.EDITOR_BG);
        payloadArea.setForeground(BurpTheme.EDITOR_TEXT);
        payloadArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        payloadPanel.add(new JScrollPane(payloadArea), BorderLayout.CENTER);
        clientPanel.add(payloadPanel, BorderLayout.CENTER);
        subTabs.addTab("Client", clientPanel);
        JPanel interactionsPanel = new JPanel(new BorderLayout());
        interactionsPanel.setBackground(BurpTheme.BG_2);
        JPanel intTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        intTop.setBackground(BurpTheme.BG_2);
        intTop.add(new JButton("Poll now"));
        intTop.add(new JLabel("Filter:"));
        JComboBox<String> intFilter = new JComboBox<>(new String[]{"All", "DNS", "HTTP", "SMTP"});
        intFilter.setBackground(BurpTheme.COMBOBOX_BG);
        intFilter.setForeground(BurpTheme.TEXT_BODY);
        intTop.add(intFilter);
        interactionsPanel.add(intTop, BorderLayout.NORTH);
        String[] intCols = {"Time", "Type", "Interaction ID", "Source IP", "Details"};
        Object[][] intData = {};
        JTable intTable = createStyledTable(intData, intCols);
        interactionsPanel.add(new JScrollPane(intTable), BorderLayout.CENTER);
        subTabs.addTab("Interactions", interactionsPanel);
        JPanel genPanel = new JPanel(new BorderLayout());
        genPanel.setBackground(BurpTheme.BG_2);
        genPanel.add(new JLabel("Default payload generator configuration"), BorderLayout.CENTER);
        subTabs.addTab("Payload generator", genPanel);
        panel.add(subTabs, BorderLayout.CENTER);
        return panel;
    }


    private static JTable createStyledTable(Object[][] data, String[] columns) {
        JTable table = new JTable(data, columns);
        table.setBackground(BurpTheme.TABLE_BG);
        table.setForeground(BurpTheme.TEXT_BODY);
        table.setGridColor(BurpTheme.TABLE_GRID);
        table.setSelectionBackground(BurpTheme.TABLE_SELECTION);
        table.setSelectionForeground(BurpTheme.TABLE_SELECTION_FG);
        table.setRowHeight(20);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setBackground(BurpTheme.TABLE_HEADER);
        table.getTableHeader().setForeground(BurpTheme.TEXT_BODY);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        return table;
    }
}
