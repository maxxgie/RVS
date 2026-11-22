package com.burpsuiteclone.ui.components;

import com.burpsuiteclone.ui.theme.BurpTheme;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom tabbed pane matching Burp Suite Pro's tab style.
 * Features close buttons on tabs, underline indicators,
 * and proper hover/selected states.
 */
public class BurpTabbedPane extends JTabbedPane {

    private final Map<Integer, Runnable> closeActions = new HashMap<>();

    public BurpTabbedPane() {
        super(JTabbedPane.TOP);
        initialize();
    }

    public BurpTabbedPane(int tabPlacement) {
        super(tabPlacement);
        initialize();
    }

    private void initialize() {
        setBackground(BurpTheme.TAB_BG);
        setForeground(BurpTheme.TAB_FG);
        setFont(new Font("Segoe UI", Font.PLAIN, 11));
        setFocusable(false);

        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.selected", BurpTheme.TAB_SELECTED);
        UIManager.put("TabbedPane.unselectedBackground", BurpTheme.TAB_BG);
        UIManager.put("TabbedPane.unselectedForeground", BurpTheme.TAB_FG);
        UIManager.put("TabbedPane.selectedForeground", BurpTheme.TAB_SELECTED_FG);
        UIManager.put("TabbedPane.focus", BurpTheme.TAB_SELECTED);
        UIManager.put("TabbedPane.light", BurpTheme.TAB_BG);
        UIManager.put("TabbedPane.dark", BurpTheme.TAB_BG);
        UIManager.put("TabbedPane.tabInsets", new Insets(2, 12, 2, 12));
        UIManager.put("TabbedPane.selectedTabPadInsets", new Insets(0, 0, -1, 0));
        UIManager.put("TabbedPane.tabAreaBackground", BurpTheme.TAB_BG);
        UIManager.put("TabbedPane.borderHightlightColor", BurpTheme.TAB_BG);
        UIManager.put("TabbedPane.darkShadow", BurpTheme.TAB_BG);

        addChangeListener(e -> repaint());
    }

    public void addTab(String title, Component component, Runnable closeAction) {
        int index = getTabCount();
        addTab(title, component);
        closeActions.put(index, closeAction);
        setTabComponentAt(index, createTabComponent(title, index));
    }

    @Override
    public void addTab(String title, Component component) {
        super.addTab(title, component);
        int index = getTabCount() - 1;
        setTabComponentAt(index, createTabComponent(title, index));
    }

    private Component createTabComponent(String title, int index) {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));

        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        label.setForeground(BurpTheme.TAB_FG);
        label.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 4));
        panel.add(label, BorderLayout.CENTER);

        JButton closeBtn = new JButton("×");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeBtn.setForeground(BurpTheme.TAB_FG);
        closeBtn.setBackground(BurpTheme.TAB_BG);
        closeBtn.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.setPreferredSize(new Dimension(16, 16));
        closeBtn.setMinimumSize(new Dimension(16, 16));
        closeBtn.setMaximumSize(new Dimension(16, 16));

        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeBtn.setBackground(BurpTheme.SEPARATOR);
                closeBtn.setForeground(BurpTheme.BUTTON_DESTRUCTIVE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeBtn.setBackground(BurpTheme.TAB_BG);
                closeBtn.setForeground(BurpTheme.TAB_FG);
            }
        });

        closeBtn.addActionListener(e -> {
            Runnable action = closeActions.get(index);
            if (action != null) {
                action.run();
            }
            removeTabAt(index);
            closeActions.remove(index);
            rebuildTabComponents();
        });

        panel.add(closeBtn, BorderLayout.EAST);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (getSelectedIndex() != index) {
                    label.setForeground(BurpTheme.TEXT_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (getSelectedIndex() != index) {
                    label.setForeground(BurpTheme.TAB_FG);
                }
            }
        });

        return panel;
    }

    private void rebuildTabComponents() {
        for (int i = 0; i < getTabCount(); i++) {
            setTabComponentAt(i, createTabComponent(getTitleAt(i), i));
        }
    }
}
