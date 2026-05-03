@echo off
echo Cleaning previous build...
if exist build\classes rmdir /s /q build\classes
mkdir build\classes

echo Compiling Java files...
dir /s /B src\*.java > sources.txt
javac -d build\classes @sources.txt

echo.
echo ==========================================
echo Launching Syntax Blade (JFrame Window)
echo ==========================================
java -cp build\classes algorythm.swing.MainSwing

rem Clean up temporary file
del sources.txt
pause
