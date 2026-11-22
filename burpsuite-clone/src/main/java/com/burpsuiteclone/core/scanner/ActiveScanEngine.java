package com.burpsuiteclone.core.scanner;


/**
 * Active scan engine — sends payloads to insertion points and analyzes responses.
 */
public interface ActiveScanEngine {
    void startScan(String targetUrl);
    void pauseScan();
    void resumeScan();
    void cancelScan();
    ScanStatus getStatus();
    void registerScanCheck(ScanCheck check);
}

enum ScanStatus {
    IDLE, RUNNING, PAUSED, COMPLETED, CANCELLED, ERROR
}

interface ScanCheck {
    String name();
    void doScan(Object baseRequest, InsertionPoint insertionPoint, ScanCallback callback);
}

interface InsertionPoint {
    String name();
    Object buildRequest(String payload);
    int getPayloadPosition();
}

interface ScanCallback {
    void issueFound(ScanIssue issue);
    void scanProgress(int completed, int total);
}

record ScanIssue(
    String name,
    String detail,
    String remediation,
    Severity severity,
    Confidence confidence,
    Object requestResponse
) {
    enum Severity { INFORMATION, LOW, MEDIUM, HIGH, CRITICAL }
    enum Confidence { CERTAIN, FIRM, TENTATIVE }
}
