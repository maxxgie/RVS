package com.burpsuiteclone.core.scanner;


/**
 * Passive scan engine — analyzes traffic without sending additional requests.
 */
public interface PassiveScanEngine {
    void enable();
    void disable();
    boolean isEnabled();
    void scanRequestResponse(Object requestResponse);
    void registerPassiveScanCheck(PassiveScanCheck check);
}

interface PassiveScanCheck {
    String name();
    void doPassiveScan(Object requestResponse, PassiveScanCallback callback);
}

interface PassiveScanCallback {
    void issueFound(ScanIssue issue);
}
