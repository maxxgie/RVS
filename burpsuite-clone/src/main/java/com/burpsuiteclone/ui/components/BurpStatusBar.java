package com.burpsuiteclone.ui.components;

import com.burpsuiteclone.ui.theme.BurpTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Custom status bar matching Burp Suite Pro's status bar.
 * Shows proxy status, scan status, and other indicators.
 */
public class BurpStatusBar extends JPanel {

    private final JLabel statusLabel;
    private final JLabel proxyLabel;
    private final JLabel scanLabel;

    public BurpStatusBar() {
        setBackground(BurpTheme.STATUSBAR_BG);
        setForeground(BurpTheme.STATUSBAR_FG);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BurpTheme.SEPARATOR));
        setLayout(new BorderLayout(0, 0));
        setPreferredSize(new Dimension(0, 24));

        // Left side - main status
        statusLabel = new JLabel("Ready");
        statusLabel.setForeground(BurpTheme.STATUSBAR_FG);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        add(statusLabel, BorderLayout.WEST);

        // Right side - indicators
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.setBackground(BurpTheme.STATUSBAR_BG);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

        proxyLabel = new JLabel("Proxy: 8080");
        proxyLabel.setForeground(BurpTheme.STATUSBAR_FG);
        proxyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        scanLabel = new JLabel("Scan: Idle");
        scanLabel.setForeground(BurpTheme.STATUSBAR_FG);
        scanLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        rightPanel.add(proxyLabel);
        rightPanel.add(scanLabel);

        add(rightPanel, BorderLayout.EAST);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public void setProxyStatus(String status) {
        proxyLabel.setText(status);
    }

    public void setScanStatus(String status) {
        scanLabel.setText(status);
    }
}
