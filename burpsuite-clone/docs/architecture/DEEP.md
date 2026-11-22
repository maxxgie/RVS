# Deep Architecture

## Layer 1: Native (C/C++)

### Launcher (BurpSuite.exe equivalent)
- JVM discovery and loading
- JNI initialization
- Application bootstrap

### JNA Bridge (jnidispatch.dll equivalent)
- Native method registration
- FFI call execution
- Memory management
- Dynamic library loading

### Browser (Chromium)
- CDP client
- Process management
- Screenshot capture
- DOM manipulation

## Layer 2: Core Engine (Java/Kotlin)

### HTTP Engine
- Protocol: HTTP/1.1, HTTP/2, WebSocket
- Client: Java HttpClient / Netty
- Features: Cookie jar, redirect handling, proxy support, TLS

### Proxy Engine
- Server: Netty NIO
- Intercept: Request/response modification
- History: Persistent storage with search
- WebSocket: Full duplex message handling
- HTTP/2: Stream multiplexing

### Scanner Engine
- Active: Payload injection at insertion points
- Passive: Traffic analysis without additional requests
- Checks: SQLi, XSS, CSRF, SSRF, etc.
- Reporting: HTML, XML, JSON

### Intruder Engine
- Attack types: Sniper, Battering ram, Pitchfork, Cluster bomb
- Payload types: Simple list, numbers, dates, brute forcer, etc.
- Results: Status code, length, time, extracted values

### Repeater
- Manual request editing
- Response viewing
- History navigation

### Decoder
- Encodings: Base64, URL, HTML, Hex, ASCII, Octal, Binary, Gzip

### Comparer
- Text diff, byte diff, word diff
- Visual highlighting

### Organizer
- Folder structure
- Request grouping
- Notes and annotations

### Site Map
- Tree structure
- Scope filtering
- Search

### Collaborator
- Out-of-band payload generation
- Interaction polling
- DNS/HTTP/SMTP callbacks

### Extensions
- JAR loading
- API registration
- Lifecycle management

### AI
- Prompt/response handling
- Context-aware suggestions

## Layer 3: Extension API

### Montoya API (Modern)
- 21 service interfaces
- Fluent builder pattern
- Type-safe

### Legacy API
- 27 interfaces
- Callback-based
- Backward compatible

## Layer 4: UI (Swing/JavaFX)

### Main Window
- Tabbed interface
- Menu bar
- Status bar
- Docking panels

### Module UIs
- Proxy: Intercept table, history, WebSocket
- Scanner: Queue, issues, config
- Intruder: Attack config, results
- Repeater: Request/response editors
- Decoder: Input/output panels
- Comparer: Diff view
- Organizer: Tree view
- Site Map: Tree view
- Extensions: List, BApp store
- Settings: Preferences dialog

## Layer 5: Persistence

### Project Files
- SQLite database
- Request/response bodies (files)
- Scan issues
- Configuration

### Preferences
- User settings
- Window state
- Recent projects

### Temp Files
- Large response bodies
- Scan data
- Cache
