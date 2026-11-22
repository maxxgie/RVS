package com.burpsuiteclone.ui.components;

import com.burpsuiteclone.ui.theme.BurpTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Custom toolbar matching Burp Suite Pro's toolbar style.
 * Flat buttons with hover effects and proper spacing.
 */
public class BurpToolbar extends JToolBar {

    public BurpToolbar() {
        setBackground(BurpTheme.BG_1);
        setForeground(BurpTheme.TEXT_BODY);
        setFloatable(false);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BurpTheme.SEPARATOR));
        setBorderPainted(true);
        setRollover(true);

        UIManager.put("ToolBar.background", BurpTheme.BG_1);
        UIManager.put("ToolBar.foreground", BurpTheme.TEXT_BODY);
        UIManager.put("ToolBar.borderColor", BurpTheme.SEPARATOR);
        UIManager.put("ToolBar.buttonBackground", BurpTheme.BUTTON_BG);
        UIManager.put("ToolBar.buttonForeground", BurpTheme.BUTTON_FG);
    }

    @Override
    public void addSeparator() {
        super.addSeparator();
        // Style the separator
        for (Component c : getComponents()) {
            if (c instanceof JToolBar.Separator) {
                c.setBackground(BurpTheme.SEPARATOR);
                c.setPreferredSize(new Dimension(1, 24));
            }
        }
    }

    /**
     * Adds a toolbar button with icon text.
     */
    public void addButton(String text, boolean primary) {
        JButton button = new JButton(text);
        styleButton(button, primary);
        add(button);
    }

    public void addButton(JButton button, boolean primary) {
        styleButton(button, primary);
        add(button);
    }

    private void styleButton(JButton button, boolean primary) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        button.setMargin(new Insets(2, 8, 2, 8));

        if (primary) {
            button.setBackground(BurpTheme.BUTTON_PRIMARY);
            button.setForeground(BurpTheme.TEXT_WHITE);
        } else {
            button.setBackground(BurpTheme.BUTTON_BG);
            button.setForeground(BurpTheme.BUTTON_FG);
        }

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (primary) {
                    button.setBackground(BurpTheme.BUTTON_PRIMARY_HOVER);
                } else {
                    button.setBackground(BurpTheme.BUTTON_HOVER);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (primary) {
                    button.setBackground(BurpTheme.BUTTON_PRIMARY);
                } else {
                    button.setBackground(BurpTheme.BUTTON_BG);
                }
            }
        });
    }
}
