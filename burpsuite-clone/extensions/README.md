# Extension SDK

## Overview
Extensions (BApps) are JAR files that implement the Montoya API or Legacy Extension API.

## Creating an Extension

### 1. Implement the Extension Interface

```java
package com.example;

import com.burpsuiteclone.api.montoya.MontoyaApi;
import com.burpsuiteclone.api.montoya.extension.Extension;

public class MyExtension implements Extension {
    private MontoyaApi api;

    @Override
    public void initialize(MontoyaApi api) {
        this.api = api;
        api.logging().logToOutput("Extension loaded!");
        
        // Register handlers
        api.http().registerHttpHandler(new MyHttpHandler(api));
        api.proxy().registerRequestHandler(new MyProxyHandler(api));
    }

    @Override
    public String name() { return "My Extension"; }

    @Override
    public void unload() {
        api.logging().logToOutput("Extension unloaded!");
    }
}
```

### 2. Package as JAR

```bash
jar cvf my-extension.jar -C build/classes .
```

### 3. Load in Burp Suite Clone

Extensions → Add → Select JAR file

## Extension Points

| Interface | Purpose |
|-----------|---------|
| HttpHandler | Intercept/modify HTTP requests and responses |
| ProxyRequestHandler | Intercept proxy requests |
| ProxyResponseHandler | Intercept proxy responses |
| WebSocketHandler | Intercept WebSocket messages |
| ActiveScanCheck | Custom active scan checks |
| PassiveScanCheck | Custom passive scan checks |
| PayloadGeneratorProvider | Custom Intruder payloads |
| PayloadProcessor | Process Intruder payloads |
| ContextMenuItemsProvider | Custom context menus |
| MessageEditorTabFactory | Custom message editor tabs |
