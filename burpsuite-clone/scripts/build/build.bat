@echo off
REM Build script for Burp Suite Clone
echo Building Burp Suite Clone...

REM Clean previous build
if exist build rmdir /s /q build

REM Compile
gradle build

if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    exit /b 1
)

echo Build successful!
echo Output: build\libs\burpsuite-clone-1.0.0.jar
