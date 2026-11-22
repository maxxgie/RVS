package com.burpsuiteclone.ui.components;

import com.burpsuiteclone.ui.theme.BurpTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Custom menu item matching Burp Suite Pro's styling.
 */
public class BurpMenuItem extends JMenuItem {

    public BurpMenuItem(String text) {
        super(text);
        initialize();
    }

    public BurpMenuItem(String text, int mnemonic) {
        super(text);
        setMnemonic(mnemonic);
        initialize();
    }

    private void initialize() {
        setBackground(BurpTheme.MENU_BG);
        setForeground(BurpTheme.MENU_FG);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        setBorderPainted(false);
        setHorizontalAlignment(SwingConstants.LEFT);
        setFont(new Font("Segoe UI", Font.PLAIN, 11));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(BurpTheme.MENU_SELECTION);
                setForeground(BurpTheme.MENU_SELECTION_FG);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(BurpTheme.MENU_BG);
                setForeground(BurpTheme.MENU_FG);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isRollover() || getModel().isArmed()) {
            g2d.setColor(BurpTheme.MENU_SELECTION);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        g2d.setColor(getForeground());
        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();
        int x = 12;
        int y = (getHeight() + fm.getAscent()) / 2 - 2;
        g2d.drawString(getText(), x, y);

        // Draw accelerator if present
        if (getAccelerator() != null) {
            String accelText = getAccelerator().toString();
            int accelWidth = fm.stringWidth(accelText);
            g2d.setColor(BurpTheme.TEXT_DISABLED);
            g2d.drawString(accelText, getWidth() - accelWidth - 12, y);
        }

        g2d.dispose();
    }
}
