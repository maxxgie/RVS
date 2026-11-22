# Browser Integration (Chromium/CDP)

## Overview
Embedded Chromium browser for:
- DOM-based XSS scanning
- Client-side vulnerability detection
- JavaScript analysis
- HTTP/2 interception
- WebSocket monitoring

## Architecture

```
Java Application ←→ CDP (Chrome DevTools Protocol) ←→ Chromium Browser
```

### CDP Connection
- Launch Chromium with `--remote-debugging-port=9222`
- Connect via WebSocket to `ws://localhost:9222/devtools/browser`
- Send CDP commands (Page.navigate, Runtime.evaluate, Network.enable, etc.)

### Automation Flow
1. Launch browser instance
2. Navigate to target URL
3. Enable network monitoring
4. Inject scanning scripts
5. Collect results (DOM, console, network)

### Key CDP Domains
- **Page**: Navigation, screenshots
- **Runtime**: JavaScript evaluation
- **Network**: Request/response interception
- **DOM**: DOM tree inspection
- **Console**: Console message capture
- **Target**: Multi-target management
