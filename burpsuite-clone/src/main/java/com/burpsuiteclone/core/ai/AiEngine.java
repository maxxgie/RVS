package com.burpsuiteclone.core.ai;

/**
 * AI-assisted testing engine.
 */
public interface AiEngine {
    boolean isEnabled();
    AiResponse prompt(String prompt);
    AiResponse prompt(String prompt, AiContext context);
}

record AiResponse(
    String text,
    boolean success,
    String error
) {}

record AiContext(
    String requestUrl,
    String requestMethod,
    String requestBody,
    String responseBody
) {}
