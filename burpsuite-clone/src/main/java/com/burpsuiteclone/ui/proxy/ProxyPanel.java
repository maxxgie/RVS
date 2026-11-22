package com.burpsuiteclone.ui.proxy;

import com.burpsuiteclone.core.proxy.HttpProxyEngine;
import com.burpsuiteclone.core.proxy.ProxyHistoryEntry;
import com.burpsuiteclone.core.proxy.ProxyOptions;
import com.burpsuiteclone.ui.theme.BurpTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProxyPanel extends JPanel {
    private final HttpProxyEngine engine;
    private DefaultTableModel historyModel;
    private JTable historyTable;
    private JTextArea interceptRequestEditor;
    private JTextArea interceptResponseEditor;
    private JButton interceptToggleBtn;
    private JButton forwardBtn;
    private JButton dropBtn;
    private volatile boolean interceptOn = true;
    private int currentInterceptId = -1;

    public ProxyPanel() {
        this(new HttpProxyEngine());
    }

    public ProxyPanel(HttpProxyEngine engine) {
        this.engine = engine;
        setLayout(new BorderLayout());
        setBackground(BurpTheme.BG_2);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BurpTheme.TAB_BG);
        tabs.addTab("Intercept", createInterceptPanel());
        tabs.addTab("HTTP history", createHistoryPanel());
        tabs.addTab("WebSockets history", createWsPanel());
        tabs.addTab("Options", createOptionsPanel());
        add(tabs, BorderLayout.CENTER);

        // Wire engine listener
        engine.addListener(new HttpProxyEngine.ProxyListener() {
            @Override
            public void onRequest(ProxyHistoryEntry entry, String method, String url, String headers, String body) {
                SwingUtilities.invokeLater(() -> {
                    if (!interceptOn) {
                        addHistoryRow(entry);
                    }
                });
            }
            @Override
            public void onResponse(ProxyHistoryEntry entry, int statusCode, String headers, String body) {
                SwingUtilities.invokeLater(() -> updateHistoryRow(entry));
            }
            @Override
            public void onIntercept(HttpProxyEngine.InterceptedRequest req) {
                SwingUtilities.invokeLater(() -> showIntercepted(req));
            }
            @Override
            public void onError(String error) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(ProxyPanel.this, error, "Proxy Error", JOptionPane.ERROR_MESSAGE));
            }
            @Override
            public void onStateChange(boolean running) {
                SwingUtilities.invokeLater(() -> {
                    if (running) {
                        interceptToggleBtn.setText("Intercept is on");
                    }
                });
            }
        });
        engine.setOnHistoryUpdate(entry -> SwingUtilities.invokeLater(() -> addHistoryRow(entry)));
        try { engine.start(8081); } catch (Exception e) { JOptionPane.showMessageDialog(this, "Failed to start proxy: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private JPanel createInterceptPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        controlPanel.setBackground(BurpTheme.BG_2);
        controlPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BurpTheme.SEPARATOR));

        interceptToggleBtn = new JButton("Intercept is on");
        interceptToggleBtn.setBackground(BurpTheme.BUTTON_PRIMARY);
        interceptToggleBtn.setForeground(BurpTheme.TEXT_WHITE);
        interceptToggleBtn.addActionListener(e -> toggleIntercept());
        controlPanel.add(interceptToggleBtn);

        forwardBtn = new JButton("Forward");
        forwardBtn.setBackground(BurpTheme.BUTTON_BG);
        forwardBtn.setForeground(BurpTheme.BUTTON_FG);
        forwardBtn.setEnabled(false);
        forwardBtn.addActionListener(e -> forwardCurrent());
        controlPanel.add(forwardBtn);

        dropBtn = new JButton("Drop");
        dropBtn.setBackground(BurpTheme.BUTTON_DESTRUCTIVE);
        dropBtn.setForeground(BurpTheme.TEXT_WHITE);
        dropBtn.setEnabled(false);
        dropBtn.addActionListener(e -> dropCurrent());
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

        panel.add(controlPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBackground(BurpTheme.SPLITPANE_BG);
        splitPane.setDividerSize(4);
        splitPane.setDividerLocation(600);

        interceptRequestEditor = new JTextArea("No request intercepted");
        interceptRequestEditor.setBackground(BurpTheme.EDITOR_BG);
        interceptRequestEditor.setForeground(BurpTheme.EDITOR_TEXT);
        interceptRequestEditor.setFont(new Font("Consolas", Font.PLAIN, 12));
        interceptRequestEditor.setCaretColor(BurpTheme.TEXT_BODY);
        interceptRequestEditor.setEditable(false);
        splitPane.setLeftComponent(new JScrollPane(interceptRequestEditor));

        interceptResponseEditor = new JTextArea("No response");
        interceptResponseEditor.setBackground(BurpTheme.EDITOR_BG);
        interceptResponseEditor.setForeground(BurpTheme.EDITOR_TEXT);
        interceptResponseEditor.setFont(new Font("Consolas", Font.PLAIN, 12));
        interceptResponseEditor.setCaretColor(BurpTheme.TEXT_BODY);
        interceptResponseEditor.setEditable(false);
        splitPane.setRightComponent(new JScrollPane(interceptResponseEditor));

        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(BurpTheme.BG_2);
        toolbar.add(new JLabel("Filter:"));
        JTextField filterField = new JTextField(25);
        filterField.setBackground(BurpTheme.TEXTFIELD_BG);
        filterField.setForeground(BurpTheme.TEXT_BODY);
        toolbar.add(filterField);
        panel.add(toolbar, BorderLayout.NORTH);

        String[] cols = {"#", "Host", "Method", "URL", "Params", "Edited", "Status", "Length", "MIME type", "Title", "IP", "Time"};
        historyModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        historyTable = new JTable(historyModel);
        historyTable.setBackground(BurpTheme.TABLE_BG);
        historyTable.setForeground(BurpTheme.TEXT_BODY);
        historyTable.setGridColor(BurpTheme.TABLE_GRID);
        historyTable.setSelectionBackground(BurpTheme.TABLE_SELECTION);
        historyTable.setSelectionForeground(BurpTheme.TABLE_SELECTION_FG);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        historyTable.setRowHeight(18);
        historyTable.setIntercellSpacing(new Dimension(0, 0));
        historyTable.getTableHeader().setBackground(BurpTheme.BG_2);
        historyTable.getTableHeader().setForeground(BurpTheme.TEXT_DISABLED);
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(historyTable);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BurpTheme.TABLE_BG);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createWsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);
        String[] cols = {"#", "Host", "URL", "Messages", "Last message"};
        Object[][] data = {};
        JTable table = createStyledTable(data, cols);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BurpTheme.BG_2);
        JTabbedPane optTabs = new JTabbedPane();
        optTabs.setBackground(BurpTheme.TAB_BG);

        // Proxy listeners
        JPanel listenersPanel = new JPanel(new BorderLayout());
        listenersPanel.setBackground(BurpTheme.BG_2);
        String[] listenCols = {"Running", "Interface", "Invisible", "Redirect", "TLS", "Support invisible"};
        Object[][] listenData = {{"true", "127.0.0.1:8080", "false", "", "false", "false"}};
        JTable listenTable = createStyledTable(listenData, listenCols);
        listenersPanel.add(new JScrollPane(listenTable), BorderLayout.CENTER);
        JPanel listenBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        listenBtns.setBackground(BurpTheme.BG_2);
        JButton addBtn = new JButton("Add");
        addBtn.setBackground(BurpTheme.BUTTON_BG);
        addBtn.setForeground(BurpTheme.BUTTON_FG);
        listenBtns.add(addBtn);
        JButton editBtn = new JButton("Edit");
        editBtn.setBackground(BurpTheme.BUTTON_BG);
        editBtn.setForeground(BurpTheme.BUTTON_FG);
        listenBtns.add(editBtn);
        JButton removeBtn = new JButton("Remove");
        removeBtn.setBackground(BurpTheme.BUTTON_BG);
        removeBtn.setForeground(BurpTheme.BUTTON_FG);
        listenBtns.add(removeBtn);
        listenersPanel.add(listenBtns, BorderLayout.SOUTH);
        optTabs.addTab("Proxy listeners", listenersPanel);

        // Intercept options
        JPanel interceptOpts = new JPanel(new BorderLayout());
        interceptOpts.setBackground(BurpTheme.BG_2);
        JPanel interceptOptsPanel = new JPanel();
        interceptOptsPanel.setLayout(new BoxLayout(interceptOptsPanel, BoxLayout.Y_AXIS));
        interceptOptsPanel.setBackground(BurpTheme.BG_2);
        interceptOptsPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        interceptOptsPanel.add(new JCheckBox("Intercept requests based on the following rules:"));
        interceptOptsPanel.add(new JCheckBox("  URL (regex match)"));
        interceptOptsPanel.add(new JCheckBox("  File extension"));
        interceptOptsPanel.add(new JCheckBox("  HTTP method"));
        interceptOptsPanel.add(new JCheckBox("Intercept responses based on the following rules:"));
        interceptOptsPanel.add(new JCheckBox("  Content type"));
        interceptOptsPanel.add(new JCheckBox("  Status code"));
        interceptOpts.add(new JScrollPane(interceptOptsPanel), BorderLayout.CENTER);
        optTabs.addTab("Intercept", interceptOpts);

        // Match and replace
        JPanel matchReplace = new JPanel(new BorderLayout());
        matchReplace.setBackground(BurpTheme.BG_2);
        String[] mrCols = {"Enabled", "Item", "Match", "Replace", "Comment"};
        Object[][] mrData = {};
        JTable mrTable = createStyledTable(mrData, mrCols);
        matchReplace.add(new JScrollPane(mrTable), BorderLayout.CENTER);
        JPanel mrBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mrBtns.setBackground(BurpTheme.BG_2);
        JButton mrAdd = new JButton("Add");
        mrAdd.setBackground(BurpTheme.BUTTON_BG); mrAdd.setForeground(BurpTheme.BUTTON_FG);
        mrBtns.add(mrAdd);
        JButton mrEdit = new JButton("Edit");
        mrEdit.setBackground(BurpTheme.BUTTON_BG); mrEdit.setForeground(BurpTheme.BUTTON_FG);
        mrBtns.add(mrEdit);
        JButton mrRemove = new JButton("Remove");
        mrRemove.setBackground(BurpTheme.BUTTON_BG); mrRemove.setForeground(BurpTheme.BUTTON_FG);
        mrBtns.add(mrRemove);
        JButton mrUp = new JButton("Up");
        mrUp.setBackground(BurpTheme.BUTTON_BG); mrUp.setForeground(BurpTheme.BUTTON_FG);
        mrBtns.add(mrUp);
        JButton mrDown = new JButton("Down");
        mrDown.setBackground(BurpTheme.BUTTON_BG); mrDown.setForeground(BurpTheme.BUTTON_FG);
        mrBtns.add(mrDown);
        matchReplace.add(mrBtns, BorderLayout.SOUTH);
        optTabs.addTab("Match and replace", matchReplace);

        // TLS
        JPanel tlsPanel = new JPanel(new BorderLayout());
        tlsPanel.setBackground(BurpTheme.BG_2);
        tlsPanel.add(new JLabel("TLS pass-through and client certificates"), BorderLayout.CENTER);
        optTabs.addTab("TLS", tlsPanel);

        // Misc
        JPanel miscPanel = new JPanel(new BorderLayout());
        miscPanel.setBackground(BurpTheme.BG_2);
        miscPanel.add(new JLabel("Miscellaneous proxy settings"), BorderLayout.CENTER);
        optTabs.addTab("Misc", miscPanel);

        panel.add(optTabs, BorderLayout.CENTER);
        return panel;
    }

    private void toggleIntercept() {
        interceptOn = !interceptOn;
        engine.getOptions().setInterceptRequests(interceptOn);
        if (interceptOn) {
            interceptToggleBtn.setText("Intercept is on");
            interceptToggleBtn.setBackground(BurpTheme.BUTTON_PRIMARY);
        } else {
            interceptToggleBtn.setText("Intercept is off");
            interceptToggleBtn.setBackground(BurpTheme.BUTTON_BG);
            // Forward any pending
            if (currentInterceptId >= 0) {
                engine.forwardIntercepted(currentInterceptId);
                currentInterceptId = -1;
            }
        }
    }

    private void forwardCurrent() {
        if (currentInterceptId >= 0) {
            String headers = interceptRequestEditor.getText();
            // Split headers and body
            int bodyStart = headers.indexOf("\n\n");
            String hdr = bodyStart >= 0 ? headers.substring(0, bodyStart) : headers;
            String body = bodyStart >= 0 ? headers.substring(bodyStart + 2) : "";
            engine.updateInterceptedRequest(currentInterceptId, hdr, body);
            engine.forwardIntercepted(currentInterceptId);
            currentInterceptId = -1;
            interceptRequestEditor.setText("No request intercepted");
            interceptRequestEditor.setEditable(false);
            forwardBtn.setEnabled(false);
            dropBtn.setEnabled(false);
        }
    }

    private void dropCurrent() {
        if (currentInterceptId >= 0) {
            engine.dropIntercepted(currentInterceptId);
            currentInterceptId = -1;
            interceptRequestEditor.setText("No request intercepted");
            interceptRequestEditor.setEditable(false);
            forwardBtn.setEnabled(false);
            dropBtn.setEnabled(false);
        }
    }

    private void showIntercepted(HttpProxyEngine.InterceptedRequest req) {
        currentInterceptId = req.id;
        interceptRequestEditor.setText(req.requestHeaders + (req.requestBody.isEmpty() ? "" : "\n\n" + req.requestBody));
        interceptRequestEditor.setEditable(true);
        forwardBtn.setEnabled(true);
        dropBtn.setEnabled(true);
    }

    private void addHistoryRow(ProxyHistoryEntry entry) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        historyModel.addRow(new Object[]{
            entry.getId(), entry.getHost(), entry.getMethod(), entry.getUrl(),
            entry.getParams(), entry.isEdited() ? "Yes" : "", entry.getStatusCode(),
            entry.getResponseLength(), entry.getMimeType(), entry.getTitle(),
            entry.getIp(), sdf.format(entry.getTime())
        });
    }

    private void updateHistoryRow(ProxyHistoryEntry entry) {
        for (int i = 0; i < historyModel.getRowCount(); i++) {
            if (historyModel.getValueAt(i, 0).equals(entry.getId())) {
                historyModel.setValueAt(entry.getStatusCode(), i, 6);
                historyModel.setValueAt(entry.getResponseLength(), i, 7);
                historyModel.setValueAt(entry.getMimeType(), i, 8);
                break;
            }
        }
    }

    private JTable createStyledTable(Object[][] data, String[] cols) {
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        table.setBackground(BurpTheme.TABLE_BG);
        table.setForeground(BurpTheme.TEXT_BODY);
        table.setGridColor(BurpTheme.TABLE_GRID);
        table.setSelectionBackground(BurpTheme.TABLE_SELECTION);
        table.setSelectionForeground(BurpTheme.TABLE_SELECTION_FG);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(18);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setBackground(BurpTheme.BG_2);
        table.getTableHeader().setForeground(BurpTheme.TEXT_DISABLED);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        return table;
    }

    public HttpProxyEngine getEngine() { return engine; }
}
