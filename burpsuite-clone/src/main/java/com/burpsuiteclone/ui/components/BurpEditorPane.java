package com.burpsuiteclone.ui.components;

import com.burpsuiteclone.ui.theme.BurpTheme;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Custom editor pane with syntax highlighting for HTTP requests/responses.
 * Matches Burp Suite Pro's message editor appearance exactly.
 */
public class BurpEditorPane extends JTextPane {

    private static final Pattern HEADER_NAME = Pattern.compile("^[A-Za-z][A-Za-z0-9!#$%&'*+.^_`|~-]*:");
    private static final Pattern HTTP_STATUS = Pattern.compile("^HTTP/[0-9.]+\\s+[0-9]+");
    private static final Pattern HTTP_METHOD = Pattern.compile("^(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS|CONNECT|TRACE)\\s");
    private static final Pattern QUERY_STRING = Pattern.compile("[?&][A-Za-z0-9_.-]+=");
    private static final Pattern COOKIE = Pattern.compile("(?:Cookie|Set-Cookie):\\s*[A-Za-z0-9_.-]+=");
    private static final Pattern CONTENT_TYPE = Pattern.compile("Content-Type:\\s*[^\\r\\n]+");
    private static final String CONTENT = "content";

    public BurpEditorPane() {
        setContentType("text/plain");
        setBackground(BurpTheme.EDITOR_BG);
        setForeground(BurpTheme.EDITOR_TEXT);
        setCaretColor(BurpTheme.TEXT_BODY);
        setFont(new Font("Consolas", Font.PLAIN, 12));
        setMargin(new Insets(4, 4, 4, 4));
        setSelectionColor(BurpTheme.EDITOR_SELECTION_BG);
        setSelectedTextColor(BurpTheme.EDITOR_SELECTION_FG);
        setDragEnabled(true);
        setDropMode(DropMode.INSERT_ROWS);

        // Enable undo support
    }

    public void undo() { /* not implemented */ }
    public void redo() { /* not implemented */ }

    /**
     * Sets HTTP syntax highlighting on the document content.
     * Call this after setting text to apply highlighting.
     */
    public void applySyntaxHighlighting() {
        StyledDocument doc = getStyledDocument();
        String text = getText();

        // Remove all existing styles
        doc.setCharacterAttributes(0, text.length(), getStyle("default"), true);

        // Highlight header names
        highlightPattern(doc, HEADER_NAME, BurpTheme.EDITOR_HEADER_NAME);

        // Highlight HTTP status line
        highlightPattern(doc, HTTP_STATUS, BurpTheme.EDITOR_HEADER_NAME);

        // Highlight HTTP methods
        highlightPattern(doc, HTTP_METHOD, BurpTheme.EDITOR_LITERAL_STRING);

        // Highlight query string parameters
        highlightPattern(doc, QUERY_STRING, BurpTheme.EDITOR_PARAM_NAME);

        // Highlight cookies
        highlightPattern(doc, COOKIE, BurpTheme.EDITOR_COOKIE_NAME);

        // Highlight content type
        highlightPattern(doc, CONTENT_TYPE, BurpTheme.EDITOR_COMMENT);
    }

    private void highlightPattern(StyledDocument doc, Pattern pattern, Color color) {
        try {
            Matcher matcher = pattern.matcher(getText());
            Style style = doc.addStyle(CONTENT, null);
            StyleConstants.setForeground(style, color);
            StyleConstants.setBold(style, true);

            while (matcher.find()) {
                doc.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), style, false);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * Sets the editor to read-only mode.
     */
    public void setReadOnly(boolean readOnly) {
        setEditable(!readOnly);
        if (readOnly) {
            setForeground(BurpTheme.TEXT_DISABLED);
        } else {
            setForeground(BurpTheme.EDITOR_TEXT);
        }
    }

    /**
     * Clears all content.
     */
    public void clear() {
        setText("");
    }
}
