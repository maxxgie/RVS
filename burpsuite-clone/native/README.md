# Native Launcher Architecture

## Overview
The native launcher is a C/C++ executable that bootstraps the JVM and launches the Java application. Based on reverse engineering of Burp Suite's install4j launcher.

## Components

### 1. JVM Discovery
- Search order: pref_jre.cfg → inst_jre.cfg → env vars → PATH
- Validates JVM by checking for jvm.dll existence
- Supports bundled JRE and system-installed JRE

### 2. JVM Loading
- LoadLibrary("jvm.dll")
- GetProcAddress("JNI_CreateJavaVM")
- Create JVM with configured options (heap size, classpath, etc.)

### 3. Application Launch
- Find main class: com.burpsuiteclone.BurpSuiteClone
- Invoke main(String[] args) via JNI
- Pass configuration and arguments

### 4. Native Bridge (JNA)
- JNA library for Java-to-native calls
- Provides OS API access, crypto, file system operations
- Used for: certificate handling, process management, UI integration

### 5. Browser Integration
- Chromium Embedded Framework (CEF) or CDP client
- Browser automation for DOM XSS scanning
- HTTP/2 interception via browser proxy

## Build Instructions

### Windows
```bash
cd native/windows/launcher
cl /Fe:BurpSuiteClone.exe launcher.c
```

### Linux
```bash
cd native/linux/launcher
gcc -o BurpSuiteClone launcher.c
```

### macOS
```bash
cd native/macos/launcher
clang -o BurpSuiteClone launcher.c
```
