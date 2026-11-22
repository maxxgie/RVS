package com.burpsuiteclone.ui.components;

import com.burpsuiteclone.ui.theme.BurpTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Custom menu bar matching Burp Suite Pro's menu style.
 */
public class BurpMenuBar extends JMenuBar {

    public BurpMenuBar() {
        setBackground(BurpTheme.MENU_BAR);
        setForeground(BurpTheme.TEXT_BODY);
        setBorder(null);
        setBorderPainted(false);
    }

    public BurpMenu addMenu(String name) {
        BurpMenu menu = new BurpMenu(name);
        add(menu);
        return menu;
    }

    public BurpMenu addMenu(String name, int mnemonic) {
        BurpMenu menu = new BurpMenu(name);
        menu.setMnemonic(mnemonic);
        add(menu);
        return menu;
    }
}
