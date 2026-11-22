package com.burpsuiteclone.core.proxy;

public class ProxyMatchReplaceRule {
    public enum RuleType {
        REQUEST_HEADER, REQUEST_BODY, REQUEST_FIRST_LINE,
        RESPONSE_HEADER, RESPONSE_BODY, RESPONSE_FIRST_LINE
    }

    private String description;
    private RuleType type;
    private String match;
    private String replace;
    private boolean enabled;
    private boolean regex;

    public ProxyMatchReplaceRule() {
        this.enabled = true;
        this.regex = false;
        this.type = RuleType.REQUEST_HEADER;
    }

    public ProxyMatchReplaceRule(String description, RuleType type, String match, String replace, boolean regex) {
        this.description = description;
        this.type = type;
        this.match = match;
        this.replace = replace;
        this.regex = regex;
        this.enabled = true;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public RuleType getType() { return type; }
    public void setType(RuleType type) { this.type = type; }
    public String getMatch() { return match; }
    public void setMatch(String match) { this.match = match; }
    public String getReplace() { return replace; }
    public void setReplace(String replace) { this.replace = replace; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isRegex() { return regex; }
    public void setRegex(boolean regex) { this.regex = regex; }

    public String apply(String input, RuleType targetType) {
        if (!enabled || type != targetType || match == null || match.isEmpty()) return input;
        if (regex) {
            return input.replaceAll(match, replace);
        } else {
            return input.replace(match, replace);
        }
    }
}
