package com.burpsuiteclone.core.extensions;

import com.burpsuiteclone.api.montoya.MontoyaApi;

/**
 * Extension loader — loads and manages Burp extensions (BApps).
 */
public interface ExtensionLoader {
    Extension loadExtension(String jarPath, MontoyaApi api);
    void unloadExtension(Extension extension);
    java.util.List<Extension> getLoadedExtensions();
}

interface Extension {
    String name();
    String filename();
    boolean isBapp();
    void unload();
    ExtensionStatus status();
}

enum ExtensionStatus { LOADED, UNLOADED, ERROR }
