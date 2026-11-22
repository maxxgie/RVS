package com.burpsuiteclone.core.intruder;


/**
 * Intruder engine — automated request fuzzing with payload generation.
 */
public interface IntruderEngine {
    IntruderAttack createAttack(Object baseRequest, AttackConfig config);
    void startAttack(IntruderAttack attack);
    void pauseAttack(IntruderAttack attack);
    void resumeAttack(IntruderAttack attack);
    void cancelAttack(IntruderAttack attack);
    AttackStatus getStatus(IntruderAttack attack);
}

interface IntruderAttack {
    String id();
    AttackConfig config();
    AttackResults results();
    void addResult(AttackResult result);
}

record AttackConfig(
    String name,
    PayloadType payloadType,
    int threads,
    int delayMs,
    boolean followRedirects,
    boolean grepMatch,
    String grepPattern
) {}

enum PayloadType {
    SIMPLE_LIST, RUNTIME_FILE, CUSTOM, NUMBERS, DATES, BRUTE_FORCER, CHARACTER_FROBBER, CASE_MODIFIER, RECURSIVE_GREP, ILLEGAL_UNICODE, CHARACTER_SUBSTITUTION
}

enum AttackStatus { IDLE, RUNNING, PAUSED, COMPLETED, CANCELLED }

interface AttackResults {
    int totalRequests();
    int completedRequests();
    java.util.List<AttackResult> results();
}

record AttackResult(
    int index,
    Object requestResponse,
    int statusCode,
    int responseLength,
    long responseTime,
    String extractedValue
) {}
