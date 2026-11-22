package com.burpsuiteclone.ui.components;

import com.burpsuiteclone.ui.theme.BurpTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Custom split pane matching Burp Suite Pro's divider style.
 * Features thin dividers with hover effects and snapping.
 */
public class BurpSplitPane extends JSplitPane {

    private static final int DIVIDER_SIZE = 4;
    private static final int DIVIDER_DRAG_SIZE = 8;

    public BurpSplitPane(int orientation, Component left, Component right) {
        super(orientation, left, right);
        initialize();
    }

    public BurpSplitPane(Component left, Component right) {
        this(HORIZONTAL_SPLIT, left, right);
    }

    private void initialize() {
        setBackground(BurpTheme.SPLITPANE_BG);
        setDividerSize(DIVIDER_SIZE);
        setDividerLocation(0.5);
        setOneTouchExpandable(false);
        setContinuousLayout(true);
        setBorder(null);

        // Add hover effect on divider
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(
                        getOrientation() == HORIZONTAL_SPLIT
                                ? Cursor.E_RESIZE_CURSOR
                                : Cursor.N_RESIZE_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(null);
            }
        });
    }

    @Override
    public void setDividerLocation(double proportionalLocation) {
        super.setDividerLocation(proportionalLocation);
    }

}
