# Syntax Blade : C++ Logic Terminal 💻🤖

Syntax Blade is a pure logic programming quiz game built with Java Swing. It features a retro cyberpunk terminal aesthetic and tests your C++ programming knowledge in real-time under pressure!

## 🚀 Features

- **Dynamic C++ Logic Puzzles:** 
  - **Tier 1:** Basic Boolean Logic, Arithmetic, Modulus, Bitwise Operators.
  - **Tier 2:** Pointers, References, and Loops (`for`, `while`).
  - **Tier 3:** Dynamic Memory (`new`/`delete`), Vectors, Structs, Strings, and 2D Arrays.
- **Cyberpunk / Hacker UI:** 
  - Custom `Graphics2D` rendering with retro CRT scanline effects.
  - Custom Syntax Highlighting engine built directly into Java Swing.
  - Blinking terminal cursors and immersive neon color palettes.
- **Multi-Frame Architecture:** 
  - Smooth transitions between Main Menu, Execution Environment (Game), and Game Over screens using `CardLayout`.
- **Lives & Scoring System:** 
  - You start with **3 Lives (♥)**.
  - Answering correctly grants points based on the difficulty tier.
  - Answering incorrectly or letting the timer run out deducts 1 life. 

## 🛠️ Requirements
- Java Development Kit (JDK) 8 or higher.
- Linux / Windows / macOS (Cross-platform Java Swing app).

## 🎮 How to Run

You can compile and run the project easily using the provided bash script (Linux/Mac):

```bash
chmod +x run.sh
./run.sh
```

**Alternatively, you can compile it manually:**
```bash
mkdir -p build/classes
find src -name "*.java" > sources.txt
javac -d build/classes @sources.txt
java -cp build/classes algorythm.swing.MainSwing
```

## 📂 Project Architecture (MVC & Design Patterns)

- **Factory Pattern (`PuzzleFactory.java`):** Dynamically generates polymophic puzzle objects (`BooleanPuzzle`, `LoopPuzzle`, `ArrayPuzzle`) based on your current score.
- **State Pattern (`LogicPhaseState.java`):** Encapsulates the logic phase timer, input buffering, and state validation.
- **Model-View Separation:** The logic core (`engine.logic`) is entirely decoupled from the view and controllers (`algorythm.swing`).

---
*Created for High-Level OOP Coursework.*
