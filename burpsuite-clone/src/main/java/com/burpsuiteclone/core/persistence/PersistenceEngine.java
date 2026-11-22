package com.burpsuiteclone.core.persistence;

/**
 * Persistence engine — project and preference storage.
 */
public interface PersistenceEngine {
    ProjectData loadProject(String path);
    void saveProject(String path, ProjectData data);
    Preferences loadPreferences();
    void savePreferences(Preferences prefs);
    ExtensionData loadExtensionData(String extensionName);
    void saveExtensionData(String extensionName, ExtensionData data);
}

record ProjectData(
    String name,
    String id,
    java.util.List<String> requestHistory,
    java.util.List<String> scanIssues,
    java.util.Map<String, String> config
) {}

record Preferences(
    java.util.Map<String, Object> settings
) {}

record ExtensionData(
    String extensionName,
    java.util.Map<String, String> data
) {}
