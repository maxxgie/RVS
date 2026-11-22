package com.burpsuiteclone.core.organizer;


/**
 * Organizer — request/response organization and storage.
 */
public interface OrganizerEngine {
    OrganizerItem addItem(Object requestResponse);
    void removeItem(String itemId);
    java.util.List<OrganizerItem> getItems();
    OrganizerFolder createFolder(String name);
    void moveToFolder(String itemId, String folderId);
}

interface OrganizerItem {
    String id();
    String name();
    Object requestResponse();
    String folderId();
    java.time.Instant createdAt();
}

interface OrganizerFolder {
    String id();
    String name();
    java.util.List<OrganizerItem> items();
}
