package com.burpsuiteclone.core.session;

/**
 * Session handling engine — cookie jars, session tokens, macro recording.
 */
public interface SessionEngine {
    CookieJar getCookieJar();
    void recordMacro(String name, java.util.List<MacroStep> steps);
    Macro getMacro(String name);
    java.util.List<Macro> getMacros();
}

interface CookieJar {
    java.util.List<Cookie> getCookies(String url);
    void setCookie(Cookie cookie);
    void removeCookie(Cookie cookie);
    void clear();
}

record Cookie(
    String name,
    String value,
    String domain,
    String path,
    boolean secure,
    boolean httpOnly,
    java.time.Instant expiry
) {}

record MacroStep(
    String request,
    String response,
    java.util.Map<String, String> extractedParams
) {}

record Macro(
    String name,
    java.util.List<MacroStep> steps
) {}
