package com.burpsuiteclone.core.logging;

import java.io.PrintStream;

/**
 * Logging engine — application-wide logging.
 */
public interface LoggingEngine {
    PrintStream output();
    PrintStream error();
    void logToOutput(String message);
    void logToError(String message);
    void raiseInfoEvent(String message);
    void raiseDebugEvent(String message);
    void raiseErrorEvent(String message);
}
