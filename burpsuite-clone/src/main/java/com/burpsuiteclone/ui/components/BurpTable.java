package com.burpsuiteclone.ui.components;

import com.burpsuiteclone.ui.theme.BurpTheme;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom JTable styled to match Burp Suite Pro exactly.
 * Features alternating row colors, custom headers, hover effects,
 * and pixel-perfect cell rendering.
 */
public class BurpTable extends JTable {

    private final List<TableRowSorter<?>> sorters = new ArrayList<>();

    public BurpTable(Object[][] data, String[] columnNames) {
        super(data, columnNames);
        initialize();
    }

    public BurpTable(TableModel model) {
        super(model);
        initialize();
    }

    private void initialize() {
        setBackground(BurpTheme.TABLE_BG);
        setForeground(BurpTheme.TEXT_BODY);
        setGridColor(BurpTheme.TABLE_GRID);
        setSelectionBackground(BurpTheme.TABLE_SELECTION);
        setSelectionForeground(BurpTheme.TABLE_SELECTION_FG);
        setRowHeight(20);
        setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        setShowGrid(true);
        setIntercellSpacing(new Dimension(0, 0));
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setCellSelectionEnabled(false);
        setRowSelectionAllowed(true);
        setColumnSelectionAllowed(false);

        // Table header styling
        getTableHeader().setBackground(BurpTheme.TABLE_HEADER);
        getTableHeader().setForeground(BurpTheme.TEXT_BODY);
        getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setResizingAllowed(true);

        setDefaultRenderer(Object.class, new BurpTableCellRenderer());
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);

        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;

            // Alternating row colors
            if (!isRowSelected(row)) {
                if (row % 2 == 0) {
                    jc.setBackground(BurpTheme.TABLE_BG);
                } else {
                    jc.setBackground(BurpTheme.TABLE_ALTERNATE);
                }
            }

            // Hover effect
            if (row == getMouseRow() && column == getMouseColumn() && !isRowSelected(row)) {
                jc.setBackground(BurpTheme.TABLE_ROLLOVER);
            }

            jc.setForeground(BurpTheme.TEXT_BODY);
            jc.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        }

        return c;
    }

    private int getMouseRow() {
        return rowAtPoint(getMousePosition());
    }

    private int getMouseColumn() {
        return columnAtPoint(getMousePosition());
    }

    /**
     * Adds a column with a specific preferred width.
     */
    public void addColumn(String columnName, int preferredWidth) {
        TableColumn column = new TableColumn(getColumnCount());
        column.setHeaderValue(columnName);
        column.setPreferredWidth(preferredWidth);
        column.setMinWidth(preferredWidth / 2);
        addColumn(column);
    }

    /**
     * Sets column widths after all columns are added.
     */
    public void setColumnWidths(int... widths) {
        TableColumnModel model = getColumnModel();
        for (int i = 0; i < Math.min(widths.length, model.getColumnCount()); i++) {
            model.getColumn(i).setPreferredWidth(widths[i]);
            model.getColumn(i).setMinWidth(widths[i] / 2);
        }
    }

    /**
     * Custom cell renderer for Burp Suite tables.
     */
    public static class BurpTableCellRenderer implements TableCellRenderer {

        private final DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                        boolean isSelected, boolean hasFocus,
                                                        int row, int column) {
            Component c = defaultRenderer.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                label.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4));
                label.setVerticalAlignment(SwingConstants.CENTER);

                if (isSelected) {
                    label.setBackground(BurpTheme.TABLE_SELECTION);
                    label.setForeground(BurpTheme.TABLE_SELECTION_FG);
                } else {
                    if (row % 2 == 0) {
                        label.setBackground(BurpTheme.TABLE_BG);
                    } else {
                        label.setBackground(BurpTheme.TABLE_ALTERNATE);
                    }
                    label.setForeground(BurpTheme.TEXT_BODY);
                }

                // Right-align numeric columns (heuristic: last few columns)
                if (column >= table.getColumnCount() - 3) {
                    label.setHorizontalAlignment(SwingConstants.TRAILING);
                } else {
                    label.setHorizontalAlignment(SwingConstants.LEFT);
                }
            }

            return c;
        }
    }
}
