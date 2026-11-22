package com.burpsuiteclone.ui.theme;

import java.awt.Color;

/**
 * Burp Suite Clone theme — replicates Burp Suite's dark color scheme
 * with slightly lighter tones for better readability.
 * Colors based on BurpLaf.properties and DarkTheme.json.
 */
public class BurpTheme {
    // === Brand ===
    public static final Color BRAND_BURP = hex("#FF6633");
    public static final Color BRAND_PRO = hex("#5B4FFF");
    public static final Color BRAND_COMMUNITY = hex("#00A2B9");

    // === Swatch Palettes (slightly lighter) ===
    public static final Color ORANGE_CORE = hex("#FF6633");
    public static final Color ORANGE_1 = hex("#B86430");
    public static final Color ORANGE_2 = hex("#E06835");
    public static final Color ORANGE_3 = hex("#FF8055");
    public static final Color ORANGE_4 = hex("#FF906A");
    public static final Color ORANGE_5 = hex("#FFA080");

    public static final Color BLUE_CORE = hex("#3A7AB5");
    public static final Color BLUE_1 = hex("#3A4250");
    public static final Color BLUE_2 = hex("#355A80");
    public static final Color BLUE_3 = hex("#5A8CC0");
    public static final Color BLUE_4 = hex("#80A8CC");
    public static final Color BLUE_5 = hex("#A5C2DC");
    public static final Color BLUE_6 = hex("#D0E0F5");

    public static final Color RED_CORE = hex("#E03050");
    public static final Color RED_1 = hex("#684548");
    public static final Color RED_2 = hex("#A83548");
    public static final Color RED_3 = hex("#E55570");
    public static final Color RED_4 = hex("#EC7888");
    public static final Color RED_5 = hex("#F29AA5");

    public static final Color PURPLE_CORE = hex("#6555FF");
    public static final Color PURPLE_1 = hex("#3E3E52");
    public static final Color PURPLE_2 = hex("#524BB8");
    public static final Color PURPLE_3 = hex("#8075FF");
    public static final Color PURPLE_4 = hex("#9890FF");
    public static final Color PURPLE_5 = hex("#B0AAFF");

    public static final Color TEAL_CORE = hex("#00B0C5");
    public static final Color TEAL_1 = hex("#385055");
    public static final Color TEAL_2 = hex("#308895");
    public static final Color TEAL_3 = hex("#65C0C8");
    public static final Color TEAL_4 = hex("#88D2D6");

    public static final Color GREEN_CORE = hex("#45AD20");
    public static final Color GREEN_1 = hex("#3A4538");
    public static final Color GREEN_2 = hex("#408030");
    public static final Color GREEN_3 = hex("#65BC48");
    public static final Color GREEN_4 = hex("#83C86A");
    public static final Color GREEN_5 = hex("#A0D58E");

    public static final Color YELLOW_CORE = hex("#E09500");
    public static final Color YELLOW_1 = hex("#524535");
    public static final Color YELLOW_2 = hex("#A07018");
    public static final Color YELLOW_3 = hex("#E2A835");
    public static final Color YELLOW_4 = hex("#E8B85C");
    public static final Color YELLOW_5 = hex("#EEC883");

    // Mono (slightly lighter)
    public static final Color MONO_1 = hex("#323232");
    public static final Color MONO_2 = hex("#383A3C");
    public static final Color MONO_3 = hex("#484A4C");
    public static final Color MONO_4 = hex("#585A5C");
    public static final Color MONO_5 = hex("#707274");
    public static final Color MONO_6 = hex("#8A8C8E");
    public static final Color MONO_7 = hex("#CBCDCE");
    public static final Color MONO_8 = hex("#FFFFFF");

    // === UI Backgrounds ===
    public static final Color BG_1 = MONO_1;
    public static final Color BG_2 = MONO_2;
    public static final Color BG_3 = MONO_3;
    public static final Color BG_4 = MONO_4;
    public static final Color BG_5 = BLUE_1;
    public static final Color BG_6 = BLUE_2;
    public static final Color BG_7 = RED_2;
    public static final Color BG_8 = PURPLE_2;
    public static final Color BG_9 = RED_1;
    public static final Color BG_10 = TEAL_2;

    // === UI Text ===
    public static final Color TEXT_HEADER = MONO_8;
    public static final Color TEXT_BODY = MONO_6;
    public static final Color TEXT_HOVER = MONO_8;
    public static final Color TEXT_DISABLED = MONO_5;
    public static final Color TEXT_LINK = BLUE_3;
    public static final Color TEXT_LINK_HOVER = BLUE_4;
    public static final Color TEXT_AI = PURPLE_5;
    public static final Color TEXT_ERROR = RED_5;
    public static final Color TEXT_INFORMATION = BLUE_5;
    public static final Color TEXT_WHITE = MONO_8;

    // === UI Icons ===
    public static final Color ICON_DEFAULT = MONO_6;
    public static final Color ICON_HOVER = MONO_7;
    public static final Color ICON_DISABLED = MONO_5;
    public static final Color ICON_ERROR = RED_5;
    public static final Color ICON_WARNING = YELLOW_4;
    public static final Color ICON_SUCCESS = GREEN_5;
    public static final Color ICON_INFO = BLUE_5;

    // === HTTP Method Colors ===
    public static final Color METHOD_GET = hex("#C06890");
    public static final Color METHOD_POST = hex("#8075FF");
    public static final Color METHOD_PUT = hex("#65C0C8");
    public static final Color METHOD_DELETE = hex("#65C0C8");
    public static final Color METHOD_PATCH = hex("#65C0C8");
    public static final Color METHOD_HEAD = hex("#65C0C8");
    public static final Color METHOD_OPTIONS = hex("#C06890");
    public static final Color METHOD_CONNECT = hex("#65C0C8");
    public static final Color METHOD_TRACE = hex("#65C0C8");
    public static final Color METHOD_UNKNOWN = MONO_6;

    // === Issue Severity ===
    public static final Color ISSUE_HIGH = RED_CORE;
    public static final Color ISSUE_MEDIUM = YELLOW_CORE;
    public static final Color ISSUE_LOW = BLUE_CORE;
    public static final Color ISSUE_INFO = MONO_6;

    // === Status Colors ===
    public static final Color STATUS_NEW = BLUE_6;
    public static final Color STATUS_IN_PROGRESS = PURPLE_3;
    public static final Color STATUS_POSTPONED = ORANGE_5;
    public static final Color STATUS_IGNORED = RED_5;
    public static final Color STATUS_DONE = GREEN_4;

    // === Editor Colors ===
    public static final Color EDITOR_BG = MONO_1;
    public static final Color EDITOR_TEXT = hex("#D4D4D4");
    public static final Color EDITOR_HEADER_NAME = hex("#D5ECFA");
    public static final Color EDITOR_HEADER_VALUE = hex("#C0C0C0");
    public static final Color EDITOR_PARAM_NAME = hex("#C0D0FF");
    public static final Color EDITOR_PARAM_VALUE = hex("#AAD860");
    public static final Color EDITOR_COOKIE_NAME = hex("#C0D0FF");
    public static final Color EDITOR_COOKIE_VALUE = hex("#AAD860");
    public static final Color EDITOR_COMMENT = hex("#A8C0C0");
    public static final Color EDITOR_SELECTION_BG = BLUE_2;
    public static final Color EDITOR_SELECTION_FG = MONO_8;
    public static final Color EDITOR_CURRENT_LINE = hex("#3A3D40");
    public static final Color EDITOR_LINE_NUMBERS = hex("#A8A8A8");
    public static final Color EDITOR_LOZENGE_BG = hex("#4E5258");
    public static final Color EDITOR_LOZENGE_TEXT = MONO_8;
    public static final Color EDITOR_LITERAL_STRING = hex("#98CC68");
    public static final Color EDITOR_LITERAL_NUMBER = hex("#7EC5F8");
    public static final Color EDITOR_LITERAL_BOOLEAN = hex("#FFA560");
    public static final Color EDITOR_TAG_NAME = hex("#EDC568");
    public static final Color EDITOR_COMPARE_ADDED = GREEN_2;
    public static final Color EDITOR_COMPARE_DELETED = RED_2;
    public static final Color EDITOR_COMPARE_MODIFIED = BLUE_2;

    // === UI Component Colors ===
    public static final Color BUTTON_BG = MONO_3;
    public static final Color BUTTON_FG = MONO_6;
    public static final Color BUTTON_HOVER = MONO_4;
    public static final Color BUTTON_SELECTED = BLUE_CORE;
    public static final Color BUTTON_PRIMARY = ORANGE_2;
    public static final Color BUTTON_PRIMARY_HOVER = ORANGE_3;
    public static final Color BUTTON_DESTRUCTIVE = RED_CORE;
    public static final Color BUTTON_DESTRUCTIVE_HOVER = RED_3;
    public static final Color BUTTON_AI = PURPLE_2;
    public static final Color BUTTON_AI_HOVER = PURPLE_3;
    public static final Color BUTTON_DISABLED = MONO_2;

    public static final Color TAB_BG = MONO_2;
    public static final Color TAB_SELECTED = MONO_1;
    public static final Color TAB_FG = MONO_6;
    public static final Color TAB_SELECTED_FG = MONO_8;
    public static final Color TAB_UNDERLINE = ORANGE_2;

    public static final Color TABLE_BG = MONO_1;
    public static final Color TABLE_ALTERNATE = MONO_2;
    public static final Color TABLE_HEADER = MONO_3;
    public static final Color TABLE_SELECTION = BLUE_2;
    public static final Color TABLE_SELECTION_FG = MONO_8;
    public static final Color TABLE_GRID = MONO_4;
    public static final Color TABLE_ROLLOVER = BLUE_1;

    public static final Color MENU_BG = MONO_2;
    public static final Color MENU_FG = MONO_6;
    public static final Color MENU_SELECTION = BLUE_2;
    public static final Color MENU_SELECTION_FG = MONO_8;
    public static final Color MENU_BAR = MONO_1;

    public static final Color SCROLLBAR_THUMB = MONO_3;
    public static final Color SCROLLBAR_TRACK = MONO_2;
    public static final Color SCROLLBAR_HOVER = MONO_4;

    public static final Color TOOLTIP_BG = MONO_4;
    public static final Color TOOLTIP_FG = MONO_6;
    public static final Color TOOLTIP_BORDER = MONO_4;

    public static final Color POPUP_BG = MONO_2;
    public static final Color POPUP_BORDER = MONO_4;

    public static final Color SEPARATOR = MONO_4;

    public static final Color PROGRESS_FG = ORANGE_CORE;
    public static final Color PROGRESS_BG = MONO_4;

    public static final Color SLIDER_THUMB = BLUE_CORE;
    public static final Color SLIDER_TRACK = MONO_4;
    public static final Color SLIDER_VALUE = BLUE_CORE;

    public static final Color CHECKBOX_ICON = BLUE_CORE;
    public static final Color CHECKBOX_ICON_BORDER = MONO_6;

    public static final Color COMBOBOX_BG = MONO_2;
    public static final Color COMBOBOX_SELECTION = BLUE_2;

    public static final Color TEXTFIELD_BG = MONO_2;
    public static final Color TEXTFIELD_SELECTION = BLUE_2;
    public static final Color TEXTFIELD_SELECTION_FG = MONO_8;

    public static final Color TREE_BG = MONO_2;
    public static final Color TREE_SELECTION = BLUE_2;
    public static final Color TREE_SELECTION_FG = MONO_8;
    public static final Color TREE_HASH = MONO_4;

    public static final Color SPLITPANE_BG = MONO_2;
    public static final Color SPLITPANE_DIVIDER = MONO_3;

    public static final Color TITLEBAR_BG = MONO_1;
    public static final Color TITLEBAR_FG = MONO_6;

    public static final Color STATUSBAR_BG = MONO_1;
    public static final Color STATUSBAR_FG = MONO_6;

    private static Color hex(String hex) {
        return Color.decode(hex);
    }
}
