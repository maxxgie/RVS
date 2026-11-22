# Burp Suite Clone — Architecture Blueprint

This project is a functionally equivalent reimplementation of Burp Suite, based on reverse engineering findings documented in `docs/ARCHITECTURE.md`.

## Project Structure

```
burpsuite-clone/
├── docs/
│   └── ARCHITECTURE.md          # Complete reverse engineering findings
├── src/
│   ├── main/
│   │   ├── java/com/burpsuiteclone/
│   │   │   ├── api/montoya/     # Modern Extension API (21 services)
│   │   │   ├── core/            # Core engine modules
│   │   │   ├── ui/              # UI layer
│   │   │   ├── native/          # JNI/JNA bridge
│   │   │   ├── proto/           # Protobuf definitions
│   │   │   └── util/            # Utilities
│   │   └── resources/           # Icons, templates
│   └── test/                    # Test suite
├── native/                      # Native launcher (C/C++)
├── browser/                     # Chromium integration
├── extensions/                  # Extension examples
├── config/                      # Default configuration
└── scripts/                     # Build/deploy scripts
```

## Modules

### Core Engine
- **http** — HTTP/1.1, HTTP/2, WebSocket protocol handling
- **proxy** — Intercepting proxy server
- **scanner** — Active/passive vulnerability scanning
- **intruder** — Automated request fuzzing
- **repeater** — Manual request replay
- **decoder** — Encoding/decoding utilities
- **comparer** — Diff comparison
- **organizer** — Request organization
- **sitemap** — Site map tree
- **collaborator** — Out-of-band testing
- **extensions** — Plugin system
- **ai** — AI-assisted testing

### Extension API (Montoya)
21 service interfaces matching Burp's Montoya API:
Http, Proxy, Scanner, Intruder, UserInterface, Utilities, Logging, Persistence, Project, Collaborator, Ai, Bambda, BurpSuite, Decoder, Comparer, Organizer, Repeater, Scope, SiteMap, WebSockets, Extension

### Legacy Extension API
27 interfaces matching Burp's original extension API (IBurpExtenderCallbacks, IExtensionHelpers, etc.)

## Getting Started

```bash
# Build
gradle build

# Run
java -jar burpsuite-clone.jar
```

## Documentation

See `docs/ARCHITECTURE.md` for complete reverse engineering findings including:
- Native layer analysis (BurpSuite.exe, jnidispatch.dll, Chromium)
- Java API mapping (Montoya + Legacy)
- Obfuscated implementation class analysis
- Architecture blueprint
- Technology recommendations
