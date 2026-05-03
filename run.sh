#!/bin/bash

echo "Cleaning previous build..."
rm -rf build/classes
mkdir -p build/classes

echo "Compiling Java files..."
find src -name "*.java" > sources.txt
javac -d build/classes @sources.txt

echo ""
echo "=========================================="
echo "Launching Syntax Blade (JFrame Window)"
echo "=========================================="
java -cp build/classes algorythm.swing.MainSwing

# Clean up temporary file
rm sources.txt
