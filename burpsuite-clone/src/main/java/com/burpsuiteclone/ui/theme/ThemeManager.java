package com.burpsuiteclone.ui.theme;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

/**
 * Applies Burp Suite's dark theme to the application.
 * Uses FlatLaf as the base with Burp's color overrides.
 */
public class ThemeManager {
    public static void applyDarkTheme() {
        try {
            // Configure FlatLaf dark theme
            FlatDarkLaf.setup();

            // Override with Burp colors
            UIManager.put("Panel.background", BurpTheme.BG_2);
            UIManager.put("Panel.foreground", BurpTheme.TEXT_BODY);

            UIManager.put("Button.background", BurpTheme.BUTTON_BG);
            UIManager.put("Button.foreground", BurpTheme.BUTTON_FG);
            UIManager.put("Button.hoverBackground", BurpTheme.BUTTON_HOVER);
            UIManager.put("Button.selectedBackground", BurpTheme.BUTTON_SELECTED);

            UIManager.put("TabbedPane.background", BurpTheme.TAB_BG);
            UIManager.put("TabbedPane.foreground", BurpTheme.TAB_FG);
            UIManager.put("TabbedPane.selectedBackground", BurpTheme.TAB_SELECTED);
            UIManager.put("TabbedPane.selectedForeground", BurpTheme.TAB_SELECTED_FG);
            UIManager.put("TabbedPane.underlineColor", BurpTheme.TAB_UNDERLINE);

            UIManager.put("Table.background", BurpTheme.TABLE_BG);
            UIManager.put("Table.foreground", BurpTheme.TEXT_BODY);
            UIManager.put("Table.selectionBackground", BurpTheme.TABLE_SELECTION);
            UIManager.put("Table.selectionForeground", BurpTheme.TABLE_SELECTION_FG);
            UIManager.put("Table.gridColor", BurpTheme.TABLE_GRID);
            UIManager.put("TableHeader.background", BurpTheme.TABLE_HEADER);
            UIManager.put("TableHeader.foreground", BurpTheme.TEXT_BODY);

            UIManager.put("Tree.background", BurpTheme.TREE_BG);
            UIManager.put("Tree.foreground", BurpTheme.TEXT_BODY);
            UIManager.put("Tree.selectionBackground", BurpTheme.TREE_SELECTION);
            UIManager.put("Tree.selectionForeground", BurpTheme.TREE_SELECTION_FG);

            UIManager.put("TextField.background", BurpTheme.TEXTFIELD_BG);
            UIManager.put("TextField.foreground", BurpTheme.TEXT_BODY);
            UIManager.put("TextField.selectionBackground", BurpTheme.TEXTFIELD_SELECTION);
            UIManager.put("TextField.selectionForeground", BurpTheme.TEXTFIELD_SELECTION_FG);

            UIManager.put("TextArea.background", BurpTheme.EDITOR_BG);
            UIManager.put("TextArea.foreground", BurpTheme.EDITOR_TEXT);

            UIManager.put("ScrollPane.background", BurpTheme.BG_2);
            UIManager.put("Viewport.background", BurpTheme.BG_2);

            UIManager.put("MenuBar.background", BurpTheme.MENU_BAR);
            UIManager.put("MenuBar.foreground", BurpTheme.TEXT_BODY);

            UIManager.put("Menu.background", BurpTheme.MENU_BG);
            UIManager.put("Menu.foreground", BurpTheme.MENU_FG);
            UIManager.put("Menu.selectionBackground", BurpTheme.MENU_SELECTION);
            UIManager.put("Menu.selectionForeground", BurpTheme.MENU_SELECTION_FG);

            UIManager.put("MenuItem.background", BurpTheme.MENU_BG);
            UIManager.put("MenuItem.foreground", BurpTheme.MENU_FG);
            UIManager.put("MenuItem.selectionBackground", BurpTheme.MENU_SELECTION);
            UIManager.put("MenuItem.selectionForeground", BurpTheme.MENU_SELECTION_FG);

            UIManager.put("PopupMenu.background", BurpTheme.POPUP_BG);
            UIManager.put("PopupMenu.foreground", BurpTheme.TEXT_BODY);
            UIManager.put("PopupMenu.borderColor", BurpTheme.POPUP_BORDER);

            UIManager.put("ToolTip.background", BurpTheme.TOOLTIP_BG);
            UIManager.put("ToolTip.foreground", BurpTheme.TOOLTIP_FG);
            UIManager.put("ToolTip.border", BorderFactory.createLineBorder(BurpTheme.TOOLTIP_BORDER));

            UIManager.put("SplitPane.background", BurpTheme.SPLITPANE_BG);
            UIManager.put("SplitPaneDivider.draggingColor", BurpTheme.SPLITPANE_DIVIDER);

            UIManager.put("ScrollBar.thumb", BurpTheme.SCROLLBAR_THUMB);
            UIManager.put("ScrollBar.track", BurpTheme.SCROLLBAR_TRACK);

            UIManager.put("ProgressBar.foreground", BurpTheme.PROGRESS_FG);
            UIManager.put("ProgressBar.background", BurpTheme.PROGRESS_BG);

            UIManager.put("ComboBox.background", BurpTheme.COMBOBOX_BG);
            UIManager.put("ComboBox.selectionBackground", BurpTheme.COMBOBOX_SELECTION);

            UIManager.put("CheckBox.background", BurpTheme.BG_2);
            UIManager.put("CheckBox.foreground", BurpTheme.TEXT_BODY);

            UIManager.put("RadioButton.background", BurpTheme.BG_2);
            UIManager.put("RadioButton.foreground", BurpTheme.TEXT_BODY);

            UIManager.put("Label.background", BurpTheme.BG_2);
            UIManager.put("Label.foreground", BurpTheme.TEXT_BODY);

            UIManager.put("Separator.foreground", BurpTheme.SEPARATOR);

            UIManager.put("OptionPane.background", BurpTheme.BG_1);
            UIManager.put("OptionPane.foreground", BurpTheme.TEXT_BODY);

        } catch (Exception e) {
            System.err.println("Failed to apply theme: " + e.getMessage());
        }
    }

    public static void applyLightTheme() {
        // Light theme variant
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to apply light theme: " + e.getMessage());
        }
    }
}
