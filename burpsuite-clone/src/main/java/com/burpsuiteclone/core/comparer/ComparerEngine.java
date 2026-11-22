package com.burpsuiteclone.core.comparer;

/**
 * Comparer — diff comparison between two texts or bytes.
 */
public interface ComparerEngine {
    DiffResult compareText(String left, String right);
    DiffResult compareBytes(byte[] left, byte[] right);
    DiffResult compareWords(String left, String right);
}

record DiffResult(
    java.util.List<DiffLine> lines,
    int additions,
    int deletions,
    int changes
) {}

record DiffLine(
    DiffType type,
    String content,
    int lineNumber
) {}

enum DiffType { UNCHANGED, ADDED, REMOVED, MODIFIED }
