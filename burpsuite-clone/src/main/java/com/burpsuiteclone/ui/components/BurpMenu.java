package com.burpsuiteclone.ui.components;

import com.burpsuiteclone.ui.theme.BurpTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Custom menu matching Burp Suite Pro's menu style.
 */
public class BurpMenu extends JMenu {

    public BurpMenu(String text) {
        super(text);
        initialize();
    }

    public BurpMenu(String text, int mnemonic) {
        super(text);
        setMnemonic(mnemonic);
        initialize();
    }

    private void initialize() {
        setBackground(BurpTheme.MENU_BG);
        setForeground(BurpTheme.MENU_FG);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        setBorderPainted(false);
        setFont(new Font("Segoe UI", Font.PLAIN, 11));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(BurpTheme.MENU_SELECTION);
                setForeground(BurpTheme.MENU_SELECTION_FG);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(isArmed() ? BurpTheme.MENU_SELECTION : BurpTheme.MENU_BG);
                setForeground(isArmed() ? BurpTheme.MENU_SELECTION_FG : BurpTheme.MENU_FG);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isRollover() || getModel().isArmed() || isSelected()) {
            g2d.setColor(BurpTheme.MENU_SELECTION);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        g2d.setColor(getForeground());
        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();
        int x = 12;
        int y = (getHeight() + fm.getAscent()) / 2 - 2;
        g2d.drawString(getText(), x, y);

        g2d.dispose();
    }

    @Override
    public JMenuItem add(JMenuItem menuItem) {
        if (menuItem instanceof BurpMenuItem) {
            return super.add(menuItem);
        }
        // Wrap plain JMenuItem in BurpMenuItem
        BurpMenuItem wrapped = new BurpMenuItem(menuItem.getText());
        if (menuItem.getAccelerator() != null) {
            wrapped.setAccelerator(menuItem.getAccelerator());
        }
        wrapped.addActionListener(menuItem.getActionListeners()[0]);
        return super.add(wrapped);
    }
}
