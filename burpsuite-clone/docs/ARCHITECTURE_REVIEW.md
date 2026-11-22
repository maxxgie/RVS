# Burp Suite Professional — Deep Architecture Review

## 1. High-Level Architecture

Burp Suite Professional is a Java/Swing-based HTTP proxy & security testing platform with these major subsystems:

```
┌─────────────────────────────────────────────────────────────────────┐
│                         Burp Suite Professional                      │
├─────────────────────────────────────────────────────────────────────┤
│  UI Layer (Swing + FlatLaf)                                         │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │ Dashboard │ Target │ Proxy │ Intruder │ Repeater │ Sequencer  │   │
│  │ Decoder │ Comparer │ Extender │ Logger │ Organizer │ Collab   │   │
│  └──────────────────────────────────────────────────────────────┘   │
├─────────────────────────────────────────────────────────────────────┤
│  Core Engine (Java/Kotlin)                                          │
│  ┌──────────┬──────────┬──────────┬──────────┬──────────┬─────────┐ │
│  │ HTTP     │ Proxy    │ Scanner  │ Intruder │ Repeater │ Sequencer│ │
│  │ Engine   │ Server   │ (Crawl + │ Engine   │ Engine   │ Engine   │ │
│  │          │          │ Audit)   │          │          │          │ │
│  ├──────────┼──────────┼──────────┼──────────┼──────────┼─────────┤ │
│  │ Decoder  │ Comparer │ SiteMap  │ Extender │ Logger   │ Organizer│ │
│  │ Engine   │ Engine   │ Engine   │ Loader   │ Engine   │ Engine   │ │
│  ├──────────┼──────────┼──────────┼──────────┼──────────┼─────────┤ │
│  │ Collabor-│ AI/      │ Persist- │ Scope    │ Session  │ Logging  │ │
│  │ ator     │ Agentic  │ ence     │ Engine   │ Engine   │ Engine   │ │
│  └──────────┴──────────┴──────────┴──────────┴──────────┴─────────┘ │
├─────────────────────────────────────────────────────────────────────┤
│  Native Layer (C/C++ launcher + JNA + Chromium)                     │
│  • Install4j launcher  • JNI/JNA bridges  • Chromium embedded browser│
│  • SQLite native       • OS integration (keyring, file assoc)       │
├─────────────────────────────────────────────────────────────────────┤
│  Persistence Layer (SQLite + .burp project files)                   │
└─────────────────────────────────────────────────────────────────────┘
```

## 2. Professional vs Community Differences

| Feature | Community | Professional |
|---------|-----------|--------------|
| Scanner (Crawl + Audit) | ❌ | ✅ Full automated scanning |
| Intruder (attack types) | Throttled | ✅ Full speed, all attack types |
| Collaborator | ❌ | ✅ OOB interaction server |
| Organizer | ❌ | ✅ Request/response organizer |
| BChecks (custom scan checks) | ❌ | ✅ |
| BApp Store (full) | Limited | ✅ |
| Project files (.burp) | ❌ | ✅ Save/restore full state |
| Task execution engine | ❌ | ✅ Parallel task management |
| Agentic AI tools | ❌ | ✅ AI-assisted testing |
| Reporting (HTML/XML) | ❌ | ✅ |
| Collaborator client restore | ❌ | ✅ |
| Scan configuration profiles | ❌ | ✅ |

## 3. Dashboard (Professional)

The Dashboard is the central command center in Professional:

### 3.1 Task List
- Table columns: Task, Status, Progress, Issues, Time
- Task types: Scan, Crawl, Audit, Intruder attack, Collaborator poll
- Status: Running, Paused, Completed, Failed, Cancelled
- Progress bar per task
- Right-click: Pause, Resume, Cancel, View details

### 3.2 Issue Summary
- Issue count by severity (High/Medium/Low/Info)
- Click to filter site map by issue type
- "New issues" notification badge

### 3.3 Scan Status Card
- Current scan: target, phase (crawling/auditing), requests sent, issues found
- Estimated time remaining
- Pause/Resume/Cancel buttons

### 3.4 Proxy Status Card
- Intercept on/off toggle
- Requests intercepted count
- Proxy listener status (port, running)

### 3.5 Event Log
- Real-time log with filter by level (Info/Warning/Error/Debug)
- Filter by tool (Proxy, Scanner, Intruder, etc.)
- Timestamp, tool, message columns

### 3.6 Quick Launch
- "New scan" button → opens scan wizard
- "New project" / "Open project"
- Recent projects list

## 4. Target Tab (Professional)

### 4.1 Site Map
- Tree view (left): hierarchical URL tree with icons (folder, file, parameter)
- Table view (right): Host, Method, URL, Params, Status, Length, MIME type, Title, Time
- Filter: by scope, by issue, by status code, by MIME type, by text
- Right-click context menu: Send to Repeater/Intruder/Comparer/Decoder, Add to scope, Copy URL, etc.
- Issue highlighting: rows colored by severity

### 4.2 Scope
- Include/Exclude tables with Protocol, Host, Port, Path (regex)
- "Use advanced scope control" checkbox
- Scope change handlers (extensions)

### 4.3 Issue Definitions
- Full list of all scanner issue types with severity, confidence
- Filterable, searchable
- Each issue has description, remediation, references

### 4.4 (Pro) Crawl Paths
- Discovered crawl paths with status
- Manual crawl configuration

## 5. Proxy Tab (Professional)

### 5.1 Intercept
- Master intercept toggle (on/off)
- Forward/Drop/Intercept is on buttons
- Request/Response editors (split view)
- Action dropdown: Send to Repeater/Intruder/Comparer/Decoder/Scanner, etc.
- "Open browser" button (launches Chromium with proxy configured)
- Comment field
- Highlight color picker

### 5.2 HTTP History
- 12 columns: #, Host, Method, URL, Params, Edited, Status, Length, MIME type, Title, IP, Time
- Filter: by host, method, status, MIME, scope, search text
- Right-click: Send to..., Copy as curl, Save item, Add to scope, etc.
- Proxy history filter (ProxyHistoryFilter API)

### 5.3 WebSockets History
- #, Host, URL, Messages, Last message columns
- WebSocket message viewer

### 5.4 Options
- **Proxy listeners**: Add/Edit/Remove, bind address, port, TLS, invisible proxy, redirect
- **Intercept**: Client requests (URL regex, file ext, params, headers), Server responses (content type, status)
- **Match and replace**: Rule table (Enabled, Item, Match, Replace, Comment)
- **TLS pass-through**: Hostname list
- **Client TLS certificates**: Per-host cert config
- **Misc**: HTTP/2, WebSocket, SOCKS, upstream proxy, platform auth

## 6. Intruder Tab (Professional — Full)

### 6.1 Target
- Host, port, HTTPS checkbox
- "Copy from Proxy" button

### 6.2 Positions
- Attack type: Sniper, Battering ram, Pitchfork, Cluster bomb
- Request editor with § markers
- Add/Clear/Auto § buttons
- Payload position highlighting

### 6.3 Payloads
- Payload set selector (1-4)
- Payload type: Simple list, Numbers, Dates, Brute forcer, Character frobber, Case modifier, Recursive grep, Illegal Unicode, Character substitution, Runtime file, Custom iterator, Copy other payload, (Pro) Battering ram payloads
- Payload list editor
- Load/Paste/Remove buttons
- Payload processing rules (add prefix/suffix, encode, hash, etc.)

### 6.4 Options
- **Request headers**: Add/Remove custom headers
- **Grep - Match**: Flag responses containing strings (with regex)
- **Grep - Extract**: Extract items from responses (with regex + template)
- **Grep - Payloads**: Flag payloads found in responses
- **Redirections**: Never/Follow on all/On 3xx (with scope)
- **Resource pool**: Concurrent threads, delay, (Pro) throttle
- **Attack results**: Save results, (Pro) auto-save

### 6.5 Results
- 7 columns: Request, Payload, Status, Error, Time, Length, Comment
- Real-time results as attack runs
- Graph view (response times, lengths)
- Send to Repeater/Intruder/Comparer
- Save results to file

### 6.6 (Pro) Turbo Intruder
- High-speed attack engine (separate extension)
- Python scripting for attacks

## 7. Scanner Tab (Professional Only)

### 7.1 Dashboard
- Scan queue with task list
- New scan wizard (target, scope, configuration)
- Scan status: Crawling / Auditing / Paused / Complete

### 7.2 Crawl
- Crawl configuration: scope, login, crawl depth, max links
- Crawl paths discovered
- Form submission handling
- JavaScript rendering (Chromium)
- Crawl results: URLs, forms, parameters

### 7.3 Audit
- Audit configuration: scan checks (active/passive), insertion points
- Built-in configurations: Crawl and audit, Audit only, Crawl only
- Custom configurations (save/load)
- Audit issues with consolidation
- BChecks integration

### 7.4 Issues
- Issue list with severity, confidence, host, path
- Issue detail: description, remediation, references, evidence
- Consolidation (group duplicate issues)
- Mark as false positive
- Report generation (HTML/XML)

### 7.5 (Pro) Scan Configuration
- Active scan checks selection
- Insertion point types
- Resource pool (threads, delay)
- (Pro) Scan speed: Normal/Fast/Thorough
- (Pro) Application login (session handling)

## 8. Repeater Tab

### 8.1 Tabs (multi-tab support)
- Each tab: Request editor, Response editor, Send/Cancel
- Follow redirections: Never/On all/On 3xx
- Update Content-Length automatically
- Unpack gzip/deflate

### 8.2 (Pro) Repeater Groups
- Group multiple requests
- Send group in sequence/parallel

### 8.3 (Pro) Tab Groups
- Organize tabs into named groups

## 9. Sequencer Tab

### 9.1 Live Capture
- Configure token location (response header/body, cookie)
- Start/Stop capture
- Capture count, speed

### 9.2 Manual Load
- Paste tokens manually

### 9.3 Analysis
- Character-level analysis (bit/byte/char)
- Entropy, correlation, compression
- FIPS 140-2 compliance
- Report generation

## 10. Decoder Tab
- Modes: Decode, Encode, Hash, Smart decode
- Encodings: Plain, Base64, URL, HTML, Hex, ASCII, Octal, Binary, Gzip, Deflate, Raw
- Input/Output split pane
- Auto-detect encoding (Smart decode)

## 11. Comparer Tab
- Compare types: Text, Words, Bytes
- Paste 1/2, Compare button
- Side-by-side diff view with highlighting
- (Pro) Sync scrolling

## 12. Extender Tab (Professional)

### 12.1 Extensions
- Table: Loaded, Type, Name, Filename, State
- Add/Remove/Up/Down buttons
- Extension detail: output, errors, (Pro) unloading

### 12.2 BApp Store
- Filter, table: Name, Author, Version, Rating, Status
- Install/Update/Uninstall
- (Pro) Private BApp Store

### 12.3 APIs
- Montoya API, Legacy Extension API
- (Pro) Custom extension loading

### 12.4 Output/Errors
- Extension stdout/stderr capture

## 13. Logger Tab (Professional)
- #, Time, Tool, Direction, Host, Method, URL, Status, Length
- Filter by tool, level, host, method
- Export to file

## 14. Organizer Tab (Professional Only)
- Items table with status (New, In progress, Postponed, Ignored, Done)
- Filter by status, host, method
- Send to Repeater/Intruder/Comparer
- Add comments, highlight
- (Pro) Bulk operations

## 15. Collaborator Tab (Professional Only)

### 15.1 Client Management
- Create/restore client (secret key)
- Payload generation
- Poll for interactions

### 15.2 Interaction Types
- DNS (with query type, source IP)
- HTTP (with request/response)
- SMTP (with email details)

### 15.3 Payload Options
- Custom subdomain
- (Pro) Collaborator server configuration (self-hosted)

## 16. Project Files (.burp)

### 16.1 Project Structure
- Project name, unique ID
- Configuration (project-level + user-level)
- Site map data
- Proxy history
- Scanner issues
- (Pro) Full state persistence

### 16.2 Save/Load
- Save project to disk
- Open project from disk
- (Pro) Auto-save
- (Pro) Project templates

## 17. Agentic AI (New in 2026)

### 17.1 AI Tools
- Burp AI integration
- MCP (Model Context Protocol) support
- AI-assisted testing workflows
- Chat interface

### 17.2 Browser Integration
- Embedded Chromium browser
- Browser-based testing
- JavaScript rendering for crawler

## 18. Native Layer

### 18.1 Launcher
- Install4j-based native launcher
- JVM selection, memory config
- Splash screen

### 18.2 Chromium
- Embedded browser for crawling
- Browser automation for testing

### 18.3 OS Integration
- Keyring for credentials
- File associations (.burp)
- (Mac) Gatekeeper, (Windows) registry

## 19. UI Theme (Dark)

### 19.1 Color System
- Mono palette (1-8): backgrounds, borders, text
- Swatch palettes: Orange, Blue, Red, Purple, Teal, Green, Yellow
- Method colors: GET (pink), POST (purple), PUT/DELETE (teal), etc.
- Issue severity: High (red), Medium (yellow), Low (blue), Info (gray)
- Status: New (blue), In progress (purple), Postponed (orange), Ignored (red), Done (green)

### 19.2 Typography
- Primary: Segoe UI (Windows), system font
- Code: Consolas / monospace
- Font sizes: 11-12px for body, 12px bold for headers

### 19.3 Component Styling
- FlatLaf base with Burp overrides
- Custom button styles (primary orange, destructive red, AI purple)
- Tabbed panes with underline indicator
- Tables with alternating row colors
- Split panes with themed dividers

## 20. Professional Workflow

### 20.1 Typical Testing Workflow
1. **Setup**: Create project, configure scope, proxy listener
2. **Discovery**: Browse app through proxy → site map populated
3. **Scanning**: Run crawl + audit on target → issues found
4. **Manual Testing**: Send requests to Repeater/Intruder for deep testing
5. **Collaboration**: Use Collaborator for OOB testing
6. **Organization**: Organize findings in Organizer
7. **Reporting**: Generate HTML/XML report
8. **Extension**: Use BApps for specialized testing

### 20.2 Scan Management (Professional)
- Scan queue: multiple concurrent scans
- Scan configuration profiles (save/load)
- Pause/Resume/Cancel
- Scan speed control (Normal/Fast/Thorough)
- Application login (session handling)
- Resource pool management

### 20.3 Task Execution Engine
- Parallel task execution
- Task prioritization
- Resource allocation
- Progress tracking
- Cancellation support

## 21. Key Differences: Our Clone vs Professional

| Area | Our Clone | Professional Burp |
|------|-----------|-------------------|
| HTTP Proxy | Stub | Full HTTP/2, WebSocket, TLS |
| Scanner | Stub | Full crawl + audit engine |
| Intruder | UI only | Full attack engine |
| Repeater | UI only | Full request/response |
| Collaborator | ❌ | ✅ OOB server |
| Organizer | ❌ | ✅ Full organizer |
| Project files | ❌ | ✅ .burp format |
| BApp Store | Stub | Full store |
| AI/Agentic | ❌ | ✅ Burp AI + MCP |
| Browser | ❌ | ✅ Chromium embedded |
| Extensions | Stub | Full Montoya + Legacy API |
| Reporting | ❌ | ✅ HTML/XML/PDF |
| Theme | ✅ Lighter dark | ✅ Dark (exact) |

## 22. Recommended Implementation Order

1. **HTTP Proxy Engine** — Core foundation, all tools depend on it
2. **Site Map Engine** — Data model for discovered URLs
3. **Scanner (Crawl)** — Discovery engine
4. **Scanner (Audit)** — Vulnerability detection
5. **Intruder Engine** — Attack automation
6. **Repeater Engine** — Manual testing
7. **Decoder/Comparer** — Utility tools
8. **Collaborator** — OOB testing
9. **Organizer** — Finding management
10. **Extender** — Plugin system
11. **Persistence** — Project files
12. **AI/Agentic** — AI integration
13. **Browser** — Chromium integration
