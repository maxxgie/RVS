# Burp Suite v2026.11.2 — Reverse Engineering Findings & Architecture Blueprint

## 1. Overview

Burp Suite is a comprehensive web application security testing platform built as a Java application with native launchers, an embedded Chromium browser, and an extensible plugin architecture. This document captures the findings from reverse engineering the Windows x64 installer (v2026.11.2) and provides the architectural blueprint for building a functionally equivalent system.

---

## 2. Native Layer

### 2.1 BurpSuite.exe — install4j Launcher

The native entry point is a standard **install4j** (ej-technologies) PE32+ executable that serves as a JVM bootstrapper.

**JVM Loading Flow:**

```
Entry Point (FUN_140015fa0)
  → Validate executable, get version info, 32/64-bit check
  → JVM Discovery (FUN_14001d780)
      ├── Strategy 1: pref_jre.cfg (preferred JRE config)
      ├── Strategy 2: .install4j/inst_jre.cfg (bundled JRE)
      ├── Strategy 3: Environment variables (EXE4J_JAVA_HOME, INSTALL4J_JAVA_HOME)
      └── Strategy 4: System PATH search
  → JVM Loading (FUN_140019cb0 → FUN_140019ab0)
      └── LoadLibraryA("jvm.dll")
  → JNI Initialization (FUN_14001d680)
      └── GetProcAddress("JNI_CreateJavaVM") → stored at DAT_14007e178
  → Main Class Launch (FUN_14000f970)
      └── FindClass("com.exe4j.runtime.WinLauncher") → invoke main(String[] args)
```

**Key Functions:**

| Address | Name (inferred) | Size | Purpose |
|---------|-----------------|------|---------|
| 0x140015fa0 | validateAndInit | ~2KB | Executable validation, version extraction |
| 0x14001d780 | discoverJVM | ~1KB | Multi-strategy JVM discovery |
| 0x140019cb0 | loadJvmModule | ~500B | Orchestrates jvm.dll loading |
| 0x140019ab0 | loadLibraryWrapper | ~500B | LoadLibraryA wrapper |
| 0x14001d680 | getJniCreateJavaVM | ~300B | GetProcAddress for JNI_CreateJavaVM |
| 0x14001d1a0 | fullJvmLoader | ~8.5KB | Complete JVM loading logic |
| 0x1400173b0 | jvmSearchAndLoad | ~1.8KB | JVM search/load orchestration |
| 0x140012610 | initJVM | ~1KB | Prints "Creating JVM", calls JNI_CreateJavaVM, prints "JVM created" |
| 0x140012a30 | loadI4jInst | ~500B | Loads i4jinst.dll, calls registerNativesEx |
| 0x14000f970 | callMain | ~1KB | Finds main class/method, prepares args, invokes via JNI |

**Key Data Addresses:**
- `DAT_14007e178`: JNI_CreateJavaVM function pointer
- `DAT_14007e1a0`: Loaded JVM module handle (HMODULE)

### 2.2 jnidispatch.dll — JNA Bridge

This is the **Java Native Access (JNA)** library (standard, not Burp-specific). It provides 76 exported functions for Java-to-native interop.

**Core Functions:**

| Function | Purpose |
|----------|---------|
| `Java_com_sun_jna_Native_initIDs` | Caches 50+ JNI field/method IDs for Native class (pointers, strings, booleans) |
| `Java_com_sun_jna_Native_registerMethod` | Registers native methods for dynamic dispatch |
| `Java_com_sun_jna_Native_ffi_1call` | Executes FFI calls via libffi |
| `Java_com_sun_jna_Native_ffi_1prep_1cif` | Prepares FFI call interface (argument types, return type) |
| `Java_com_sun_jna_Native_ffi_1prep_1closure` | Prepares FFI closure for callbacks |
| `Java_com_sun_jna_Native_invoke*` | Various invoke variants (int, long, double, float, pointer, string, structure) |
| `Java_com_sun_jna_Native_get*` / `set*` | Memory read/write primitives |
| `Java_com_sun_jna_Native_malloc` / `free` | Native memory allocation |
| `Java_com_sun_jna_Native_loadLibrary` | Dynamic library loading |

**Architecture Role:** JNA enables Burp's Java code to call native libraries (OS APIs, crypto, browser automation) without writing JNI C code.

### 2.3 Embedded Chromium (burpbrowser/)

- **Version:** 157.0.8103.51 (full Chromium build)
- **chrome.dll:** ~308MB
- **Purpose:** Browser-based scanning (DOM XSS, client-side vulnerabilities), HTTP/2 interception, JavaScript analysis
- **Integration:** Via CDP (Chrome DevTools Protocol) for automation

---

## 3. Java Application Layer

### 3.1 JAR Structure

```
burpsuite.jar (173MB, multi-release, Java 21/class version 65)
├── burp/                          # Legacy Extension API (clean interfaces)
│   ├── IBurpExtender.java
│   ├── IBurpExtenderCallbacks.java  (97 methods)
│   ├── IExtensionHelpers.java       (29 methods)
│   ├── IHttpListener.java
│   ├── IProxyListener.java
│   ├── IScannerListener.java
│   ├── IScannerCheck.java
│   ├── IScannerInsertionPoint.java   (5 methods, 18 fields)
│   ├── IHttpRequestResponse.java     (18 methods)
│   ├── IInterceptedProxyMessage.java (6 methods, 7 fields)
│   ├── IRequestInfo.java            (6 methods, 7 fields)
│   ├── ... (27 total interfaces)
│
├── burp/api/montoya/               # Modern Extension API (282 classes)
│   ├── MontoyaApi.java              (main entry point, 21 services)
│   ├── http/                        # HTTP service (13 methods)
│   ├── proxy/                       # Proxy service (10 methods)
│   ├── scanner/                     # Scanner service (9 methods)
│   ├── intruder/                    # Intruder service (6 methods)
│   ├── ui/                          # UI service (20 methods)
│   ├── utilities/                   # Utilities (12 methods)
│   ├── logging/                     # Logging (11 methods)
│   ├── persistence/                 # Persistence (2 methods)
│   ├── project/                     # Project (2 methods)
│   ├── collaborator/                # Collaborator (3 methods)
│   ├── ai/                          # AI (2 methods)
│   ├── bambda/                      # Bambda (1 method)
│   ├── burpsuite/                   # BurpSuite (8 methods)
│   ├── decoder/                     # Decoder (1 method)
│   ├── comparer/                    # Comparer (1 method)
│   ├── organizer/                   # Organizer (4 methods)
│   ├── repeater/                    # Repeater (2 methods)
│   ├── scope/                       # Scope (4 methods)
│   ├── sitemap/                     # SiteMap (6 methods)
│   ├── websockets/                  # WebSockets (3 methods)
│   └── extension/                   # Extension (5 methods)
│
├── com/                            # Obfuscated implementation (316 classes)
│   ├── Zk7.java                     (140 methods — HTTP config)
│   ├── Zts.java                     (94 methods — proxy engine)
│   ├── Zdq.java                     (75 methods — scan config)
│   ├── Zcv.java                     (61 methods — scanner)
│   ├── Zcw.java                     (61 methods — logging/telemetry)
│   ├── Za2.java                     (58 methods — HTTP service)
│   ├── Zco.java                     (56 methods — HTTP proxy)
│   ├── Zzi.java                     (55 methods — logging impl)
│   ├── Zct.java                     (49 methods — scan config)
│   ├── Ztw.java                     (45 methods — UI/tab)
│   ├── ... (306 more, mostly data classes, enums, Gson adapters)
│
├── net/portswigger/                # Protobuf + internal (58,303 classes)
│   └── Heavily obfuscated (Z_*, Za*, Zb* patterns)
│       with protobuf message classes (Builder/OrBuilder)
│
├── META-INF/
│   ├── MANIFEST.MF
│   ├── native-image/               # GraalVM native-image metadata
│   ├── versions/                   # Multi-release (Java 9-25)
│   └── substrate/                  # Substrate VM config
│
└── Resources
    ├── DomInvader (DOM XSS scanner)
    ├── AdBlock lists
    ├── UI assets (icons, themes)
    └── Browser automation scripts
```

### 3.2 Montoya API — Complete Service Map

| Service | Methods | Purpose |
|---------|---------|---------|
| **Http** | 13 | sendRequest, createRequestEngine, cookieJar, registerHttpHandler, etc. |
| **Proxy** | 10 | enable/disable intercept, history, registerRequest/Response/WebSocket handlers |
| **Scanner** | 9 | registerActive/PassiveScanCheck, startCrawl, startAudit, generateReport |
| **Intruder** | 6 | registerPayloadProcessor/GeneratorProvider, sendToIntruder |
| **UserInterface** | 20 | menuBar, register*Tab/ContextMenu/Editor/HotKey/SettingsPanel |
| **Utilities** | 12 | base64Utils, byteUtils, cryptoUtils, jsonUtils, shellUtils, etc. |
| **Logging** | 11 | output/error streams, logToOutput/Error, raise*Event |
| **Persistence** | 2 | extensionData, preferences |
| **Project** | 2 | name, id |
| **Collaborator** | 3 | createClient, restoreClient, defaultPayloadGenerator |
| **Ai** | 2 | isEnabled, prompt |
| **Bambda** | 1 | importBambda |
| **BurpSuite** | 8 | version, export/import options, shutdown, taskExecutionEngine |
| **Decoder** | 1 | sendToDecoder |
| **Comparer** | 1 | sendToComparer |
| **Organizer** | 4 | sendToOrganizer, items |
| **Repeater** | 2 | sendToRepeater |
| **Scope** | 4 | isInScope, include/exclude, registerScopeChangeHandler |
| **SiteMap** | 6 | requestResponses, issues, add |
| **WebSockets** | 3 | registerWebSocketCreatedHandler, createWebSocket |
| **Extension** | 5 | setName, filename, isBapp, unload, registerUnloadingHandler |

### 3.3 Legacy Extension API

27 interfaces parsed:

| Interface | Methods | Fields | Purpose |
|-----------|---------|--------|---------|
| IBurpExtenderCallbacks | 97 | — | Main callback interface for extensions |
| IExtensionHelpers | 29 | — | Request/response parsing helpers |
| IHttpRequestResponse | 18 | — | HTTP message container |
| IScannerInsertionPoint | 5 | 18 | Scan insertion point definition |
| IInterceptedProxyMessage | 6 | 7 | Intercepted proxy message |
| IRequestInfo | 6 | 7 | Parsed request info |
| IResponseInfo | 6 | — | Parsed response info |
| IParameter | 7 | 7 | HTTP parameter |
| ITextEditor | 8 | — | Text editor component |
| IMessageEditorTab | 7 | — | Message editor tab |
| IHttpService | 3 | — | HTTP service (host/port/protocol) |
| ICookie | 5 | — | Cookie representation |
| IHttpHeader | 2 | — | HTTP header |
| IScanIssue | 14 | — | Scan finding |
| IScannerCheck | 3 | — | Active/passive scan check |
| IIntruderAttack | 2 | — | Intruder attack config |
| IIntruderPayloadGenerator | 3 | — | Payload generation |
| IIntruderPayloadProcessor | 2 | — | Payload processing |
| IContextMenuFactory | 1 | — | Context menu factory |
| ITab | 2 | — | Tab component |
| ISessionHandlingAction | 2 | — | Session handling |
| IScopeChangeListener | 1 | — | Scope change listener |
| ITempFile | 2 | — | Temp file |
| IHttpListener | 1 | — | HTTP listener |
| IProxyListener | 1 | — | Proxy listener |
| IScannerListener | 1 | — | Scanner listener |

### 3.4 Obfuscated Implementation Classes

316 classes in `com/` package. Key ones by complexity:

| Class | Methods | Fields | Inferred Purpose |
|-------|---------|--------|-----------------|
| Zk7 | 140 | 71 | HTTP request configuration (headers, cookies, proxy, auth, etc.) |
| Zts | 94 | 44 | Proxy interception engine |
| Zdq | 75 | 35 | Scan/intruder configuration (URLs, payloads, rules) |
| Zcv | 61 | 19 | Scanner core |
| Zcw | 61 | 2 | Logging/telemetry interface (com/Zap) |
| Za2 | 58 | 27 | HTTP service implementation |
| Zco | 56 | 19 | HTTP proxy handler |
| Zzi | 55 | 2 | Logging implementation (implements Zcw) |
| Zct | 49 | 20 | Scan configuration data |
| Ztw | 45 | 20 | UI tab management |
| Zdp | 37 | 17 | HTTP message data |
| Zz9 | 36 | 2 | File I/O (serialization) |
| Zab | 33 | 11 | Scan issue data |
| Zkj | 31 | 14 | Key-value store |
| Zzo | 28 | 10 | Survey/targeting config |
| Zna | 27 | 10 | Scan results |
| Zdl | 24 | 12 | Thread/uncaught exception handler |
| Znd | 24 | 9 | Text/string utilities |
| Zz7 | 24 | 9 | UI survey/popup config |
| Zcs | 23 | 9 | Feature flags (extends Zcj) |

**Technology Stack:**
- **Kotlin** — primary implementation language (coroutines, data classes, sealed classes)
- **Gson** — JSON serialization (type adapters, reflect)
- **Protobuf** — internal communication (net.portswigger/)
- **Coroutines** — async operations (RestrictedSuspendLambda, SequenceScope)
- **Enum-heavy** — configuration options (Zzw: 33 match operators, Zz2: ONCE/RECURRING/ALWAYS, etc.)

---

## 4. Architecture Blueprint

### 4.1 System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Native Launcher                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐   │
│  │ BurpSuite.exe │  │  jnidispatch  │  │  burpbrowser/        │   │
│  │ (install4j)   │  │  (JNA bridge) │  │  (Chromium 157)      │   │
│  └──────┬───────┘  └──────┬───────┘  └──────────┬───────────┘   │
│         │                 │                      │               │
│         └─────────────────┼──────────────────────┘               │
│                           │                                      │
├───────────────────────────┼──────────────────────────────────────┤
│                    JVM (Java 21+)                                │
│  ┌────────────────────────┴──────────────────────────────────┐   │
│  │                    Core Engine                              │   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────────┐  │   │
│  │  │  HTTP    │ │  Proxy   │ │  Scanner │ │   Intruder   │  │   │
│  │  │  Engine  │ │  Engine  │ │  Engine  │ │    Engine    │  │   │
│  │  └────┬─────┘ └────┬─────┘ └────┬─────┘ └──────┬───────┘  │   │
│  │       │             │            │               │          │   │
│  │  ┌────┴─────────────┴────────────┴───────────────┴──────┐  │   │
│  │  │              Message/Session Manager                  │  │   │
│  │  └────────────────────────┬─────────────────────────────┘  │   │
│  │                           │                               │   │
│  │  ┌────────────────────────┴─────────────────────────────┐  │   │
│  │  │              Extension API (Montoya + Legacy)          │  │   │
│  │  └────────────────────────┬─────────────────────────────┘  │   │
│  │                           │                               │   │
│  │  ┌────────────────────────┴─────────────────────────────┐  │   │
│  │  │                   UI Layer (Swing/JavaFX)            │  │   │
│  │  └──────────────────────────────────────────────────────┘  │   │
│  └────────────────────────────────────────────────────────────┘   │
│                                                                   │
│  ┌────────────────────────────────────────────────────────────┐   │
│  │              Persistence (SQLite/Preferences)              │   │
│  └────────────────────────────────────────────────────────────┘   │
└───────────────────────────────────────────────────────────────────┘
```

### 4.2 Module Breakdown

#### Core Modules

| Module | Description | Key Components |
|--------|-------------|----------------|
| **http** | HTTP/1.1, HTTP/2, WebSocket protocol handling | HttpRequest, HttpResponse, HttpService, CookieJar, HttpEngine |
| **proxy** | Intercepting proxy | ProxyServer, InterceptHandler, ProxyHistory, WebSocket proxy |
| **scanner** | Active/passive vulnerability scanning | ActiveScanEngine, PassiveScanEngine, ScanCheck, InsertionPoint, ScanIssue |
| **intruder** | Automated request fuzzing | IntruderAttack, PayloadGenerator, PayloadProcessor, IntruderEngine |
| **repeater** | Manual request replay | RepeaterTab, RequestEditor, ResponseViewer |
| **decoder** | Encoding/decoding utilities | Base64, URL, HTML, Hex, Gzip, etc. |
| **comparer** | Diff comparison | Text diff, byte diff |
| **organizer** | Request organization | OrganizerItems, folders |
| **sitemap** | Site map tree | SiteMapNode, SiteMapTree |
| **collaborator** | Out-of-band testing | CollaboratorClient, PayloadGenerator, InteractionPoller |
| **extensions** | Plugin system | ExtensionLoader, ExtensionContext, BApp store |
| **ai** | AI-assisted testing | AIPrompt, AIResponse |

#### UI Modules

| Module | Description |
|--------|-------------|
| **main** | Main window, menu bar, tab management |
| **proxy** | Proxy intercept UI, history table, WebSocket view |
| **scanner** | Scan queue, issues tree, scan config dialog |
| **intruder** | Intruder attack UI, payload config, results table |
| **repeater** | Repeater tabs, request/response editors |
| **decoder** | Decoder input/output panels |
| **comparer** | Diff view panels |
| **organizer** | Organizer tree view |
| **sitemap** | Site map tree view |
| **extensions** | Extensions tab, BApp store UI |
| **settings** | Preferences dialog, project options |
| **components** | Shared UI components (editors, tables, trees) |

#### Native Modules

| Module | Description |
|--------|-------------|
| **jni** | JNI bridge for native calls |
| **browser** | Chromium CDP automation |

### 4.3 Data Flow

```
HTTP Request Flow:
  Browser/Client → Proxy Listener → Intercept Handler → (modify) → Target Server
  Target Server → Proxy → (modify) → Browser/Client
  ↓ (passive scan)
  Passive Scanner → Scan Issues
  ↓ (active scan)
  Active Scanner → Insertion Points → Payloads → Target Server → Analysis → Scan Issues

Extension Flow:
  Extension JAR → ExtensionLoader → MontoyaApi/IBurpExtenderCallbacks → Register handlers
  HTTP/Proxy/Scanner events → Extension handlers → Modified data

Intruder Flow:
  Base Request → Insertion Points → Payload Generator → Payload Processor → Target Server → Response Analysis
```

### 4.4 Persistence

- **Project files:** Serialized session state (requests, responses, issues, config)
- **Preferences:** User settings (proxy port, scope, display options)
- **Extension data:** Per-extension persistent storage
- **Temp files:** Large response bodies, scan data

---

## 5. Technology Recommendations

For building a Burp Suite equivalent:

| Component | Recommended Technology |
|-----------|----------------------|
| **Language** | Java 21+ (Kotlin for implementation, matching Burp's approach) |
| **UI** | JavaFX or Swing (Burp uses Swing) |
| **HTTP Client** | Java HttpClient (HTTP/2 support) or Netty |
| **Proxy Server** | Netty (async, high-performance) |
| **Database** | SQLite (project persistence) |
| **JSON** | Gson or Jackson |
| **Protobuf** | Protocol Buffers (internal IPC) |
| **Browser** | Playwright or CDP (Chromium automation) |
| **Native** | JNA (Java Native Access) |
| **Build** | Gradle or Maven |
| **Packaging** | jpackage (native installer) |

---

## 6. Key Design Patterns Identified

1. **API/Implementation Separation:** Clean public interfaces in `burp/` and `burp/api/montoya/`, obfuscated implementations in `com/`
2. **Event-Driven Architecture:** Listeners (IHttpListener, IProxyListener, IScannerListener) for extension hooks
3. **Builder Pattern:** Extensive use of builders for complex objects (HttpRequest, ScanConfig, etc.)
4. **Factory Pattern:** MontoyaApi as central factory for all services
5. **Observer Pattern:** Scope change listeners, scan issue listeners
6. **Strategy Pattern:** Scanner checks, payload generators, payload processors
7. **Template Method:** Scan insertion points with customizable payload generation

---

## 7. Extension Points (for Plugin Development)

The Montoya API provides these extension hooks:

- **HttpHandler:** Intercept/modify HTTP requests and responses
- **ProxyRequestHandler/ProxyResponseHandler:** Intercept proxy traffic
- **WebSocketHandler:** Intercept WebSocket messages
- **ActiveScanCheck/PassiveScanCheck:** Custom vulnerability checks
- **PayloadGeneratorProvider/PayloadProcessor:** Custom Intruder payloads
- **ContextMenuItemsProvider:** Custom context menu items
- **MessageEditorTabFactory:** Custom message editor tabs
- **HttpHandler:** HTTP handler registration
- **ScopeChangeHandler:** Scope change notifications
- **ExtensionUnloadingHandler:** Cleanup on unload

---

## 8. File Reference

| File | Description |
|------|-------------|
| `C:\Users\maxwe\Downloads\burpsuite_windows-x64_v2026_11_2.exe` | Original installer |
| `C:\Users\maxwe\Downloads\burp_extracted\burpsuite.jar` | Main application (173MB) |
| `C:\Users\maxwe\Downloads\burp_extracted\BurpSuite.exe` | Native launcher (install4j) |
| `C:\Users\maxwe\Downloads\burp_extracted\jre\` | Bundled JRE 25 |
| `C:\Users\maxwe\Downloads\burp_extracted\native\jnidispatch.dll` | JNA native bridge |
| `C:\Users\maxwe\Downloads\burp_extracted\burpbrowser\` | Embedded Chromium |
| `C:\Users\maxwe\Downloads\parse_class.py` | Python class file parser |
| `C:\Users\maxwe\Downloads\analyze_obfuscated.py` | Obfuscated class analyzer |
| `C:\Users\maxwe\Downloads\obfuscated_analysis.txt` | Full obfuscated class analysis |
