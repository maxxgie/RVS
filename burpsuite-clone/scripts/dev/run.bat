@echo off
REM Development run script
echo Starting Burp Suite Clone in development mode...

java -cp "build\classes\java\main;build\resources\main" com.burpsuiteclone.BurpSuiteClone %*
