package com.burpsuiteclone.ui.issues;

import com.burpsuiteclone.core.issues.ScanIssue;
import com.burpsuiteclone.ui.theme.BurpTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IssueActivityLog extends JPanel {
    private final DefaultTableModel model;
    private final JTable table;
    private final List<ScanIssue> issues = new ArrayList<>();

    public IssueActivityLog() {
        setLayout(new BorderLayout());
        setBackground(BurpTheme.BG_1);
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BurpTheme.BG_4),
            "Issue activity", 0, 0, null, BurpTheme.TEXT_BODY));

        String[] columns = {"Time", "Severity", "Issue name", "Host", "URL", "Confidence"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setBackground(BurpTheme.BG_1);
        table.setForeground(BurpTheme.TEXT_BODY);
        table.setGridColor(BurpTheme.BG_4);
        table.setSelectionBackground(BurpTheme.TABLE_SELECTION);
        table.setSelectionForeground(BurpTheme.TEXT_BODY);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(22);
        table.getTableHeader().setBackground(BurpTheme.BG_2);
        table.getTableHeader().setForeground(BurpTheme.TEXT_DISABLED);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Severity column coloring
        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, sel, focus, row, col);
                if (!sel) {
                    String sev = value != null ? value.toString() : "";
                    switch (sev) {
                        case "High": c.setForeground(new Color(0xFF, 0x44, 0x44)); break;
                        case "Medium": c.setForeground(new Color(0xFF, 0x88, 0x00)); break;
                        case "Low": c.setForeground(new Color(0xFF, 0xCC, 0x00)); break;
                        case "Information": c.setForeground(BurpTheme.TEXT_DISABLED); break;
                        default: c.setForeground(BurpTheme.TEXT_BODY);
                    }
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(BurpTheme.BG_1);
        scroll.getViewport().setBackground(BurpTheme.BG_1);
        add(scroll, BorderLayout.CENTER);

        // Bottom toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        toolbar.setBackground(BurpTheme.BG_2);
        JButton clearBtn = new JButton("Clear");
        clearBtn.setBackground(BurpTheme.BUTTON_BG);
        clearBtn.setForeground(BurpTheme.TEXT_BODY);
        clearBtn.addActionListener(e -> clearIssues());
        toolbar.add(clearBtn);
        add(toolbar, BorderLayout.SOUTH);
    }

    public void addIssue(ScanIssue issue) {
        issues.add(issue);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        model.addRow(new Object[]{
            sdf.format(new Date(issue.getTimestamp())),
            issue.getSeverity(),
            issue.getIssueName(),
            issue.getHost(),
            issue.getUrl() != null ? issue.getUrl().toString() : "",
            issue.getConfidence()
        });
    }

    public void clearIssues() {
        issues.clear();
        model.setRowCount(0);
    }

    public List<ScanIssue> getIssues() { return new ArrayList<>(issues); }
    public int getIssueCount() { return issues.size(); }
}
