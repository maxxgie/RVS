package com.burpsuiteclone.core.config;

/**
 * Scope engine — target scope management.
 */
public interface ScopeEngine {
    boolean isInScope(String url);
    void includeInScope(String urlPattern);
    void excludeFromScope(String urlPattern);
    java.util.List<String> getIncludedUrls();
    java.util.List<String> getExcludedUrls();
    void registerScopeChangeHandler(ScopeChangeHandler handler);
}

interface ScopeChangeHandler {
    void scopeChanged(String url, boolean included);
}
