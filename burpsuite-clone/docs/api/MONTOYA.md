# API Reference

## Montoya API

### MontoyaApi
Main entry point providing access to all services.

```java
MontoyaApi api = ...;
Http http = api.http();
Proxy proxy = api.proxy();
Scanner scanner = api.scanner();
// ... 21 services total
```

### Http Service
- `sendRequest(HttpRequest)` — Send an HTTP request
- `createRequestEngine(RequestEngineOptions)` — Create a request engine for bulk sending
- `cookieJar()` — Access the cookie jar
- `registerHttpHandler(HttpHandler)` — Register an HTTP handler
- `isHttp2Available()` — Check HTTP/2 support

### Proxy Service
- `enableIntercept()` / `disableIntercept()` — Toggle interception
- `history()` — Access proxy history
- `registerRequestHandler(ProxyRequestHandler)` — Register request handler
- `registerResponseHandler(ProxyResponseHandler)` — Register response handler

### Scanner Service
- `registerActiveScanCheck(ActiveScanCheck)` — Register active scan check
- `registerPassiveScanCheck(PassiveScanCheck)` — Register passive scan check
- `startCrawl(String url)` — Start a crawl
- `startAudit(String url)` — Start an audit
- `generateReport(ReportOptions)` — Generate a scan report

### Intruder Service
- `registerPayloadProcessor(PayloadProcessor)` — Register payload processor
- `registerPayloadGeneratorProvider(PayloadGeneratorProvider)` — Register payload generator
- `sendToIntruder(HttpRequestResponse)` — Send to Intruder

### UserInterface Service
- `menuBar()` — Access menu bar
- `registerContextMenuItemsProvider(...)` — Register context menu
- `registerHttpEditorTab(...)` — Register editor tab
- `registerSuiteTab(...)` — Register suite tab
- `registerSettingsPanel(...)` — Register settings panel
