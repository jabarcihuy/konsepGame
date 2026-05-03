package syntaxblade.engine.logic;

import java.util.Random;

/**
 * Factory pattern implementation for generating Logic Puzzles dynamically.
 * Upgraded to use C++ syntax for all questions.
 */
public class PuzzleFactory {
    private static final Random random = new Random();

    public static LogicPuzzle createPuzzle(int tier) {
        switch (tier) {
            case 1:
                return generateTier1Puzzle();
            case 2:
                return generateTier2Puzzle();
            case 3:
                return generateTier3Puzzle();
            default:
                throw new IllegalArgumentException("Unknown puzzle tier: " + tier);
        }
    }

    private static LogicPuzzle generateTier1Puzzle() {
        int type = random.nextInt(5);
        switch(type) {
            case 0: 
                return new BooleanPuzzle("int a = 10, b = 3;\nstd::cout << (a + b);\n\nWhat is the output?", "13", 15.0f);
            case 1: 
                return new BooleanPuzzle("int a = 15, b = 4;\nstd::cout << (a % b);\n\nWhat is the output?", "3", 15.0f);
            case 2: 
                return new BooleanPuzzle("bool x = true, y = false;\nstd::cout << (x && y);\n\nWhat is the output (1 or 0)?", "0", 15.0f);
            case 3: 
                return new BooleanPuzzle("int x = 16;\nstd::cout << (x >> 2);\n\nWhat is the output?", "4", 15.0f);
            case 4:
                return new BooleanPuzzle("int a = 7, b = 2;\nstd::cout << (a / b);\n\nWhat is the output?", "3", 15.0f);
            default:
                return new BooleanPuzzle("std::cout << (5 + 3);\n\nWhat is the output?", "8", 15.0f);
        }
    }

    private static LogicPuzzle generateTier2Puzzle() {
        int type = random.nextInt(5);
        switch(type) {
            case 0: 
                return new LoopPuzzle("int x = 10;\nint& ref = x;\nref = 25;\nstd::cout << x;\n\nWhat is the output?", "25", 20.0f);
            case 1: 
                return new LoopPuzzle("int a = 5;\nint* ptr = &a;\n*ptr = *ptr + 10;\nstd::cout << a;\n\nWhat is the output?", "15", 20.0f);
            case 2: 
                return new LoopPuzzle("int sum = 0;\nfor(int i=0; i<4; ++i) {\n  sum += i;\n}\nstd::cout << sum;\n\nWhat is the output?", "6", 20.0f);
            case 3: 
                return new LoopPuzzle("int arr[3] = {10, 20, 30};\nstd::cout << arr[1];\n\nWhat is the output?", "20", 20.0f);
            case 4:
                return new LoopPuzzle("int x = 0;\nwhile(x < 10) x += 3;\nstd::cout << x;\n\nWhat is the output?", "12", 20.0f);
            default:
                return new LoopPuzzle("int x = 10;\nstd::cout << x;", "10", 20.0f);
        }
    }

    private static LogicPuzzle generateTier3Puzzle() {
        int type = random.nextInt(5);
        switch(type) {
            case 0: 
                return new ArrayPuzzle("std::vector<int> v;\nv.push_back(10);\nv.push_back(20);\nstd::cout << v.size();\n\nWhat is the output?", "2", 25.0f);
            case 1: 
                return new ArrayPuzzle("int* p = new int(50);\nint v = *p / 2;\ndelete p;\nstd::cout << v;\n\nWhat is the output?", "25", 25.0f);
            case 2: 
                return new ArrayPuzzle("struct Box { int w; };\nBox b; b.w = 99;\nBox* p = &b;\nstd::cout << p->w;\n\nWhat is the output?", "99", 25.0f);
            case 3: 
                return new ArrayPuzzle("std::string s = \"ALGO\";\nstd::cout << s[2];\n\nWhat is the output?", "G", 25.0f);
            case 4:
                return new ArrayPuzzle("int a[2][2] = {{1,2},{3,4}};\nstd::cout << a[0][1];\n\nWhat is the output?", "2", 25.0f);
            default:
                return new ArrayPuzzle("std::cout << \"OK\";", "OK", 25.0f);
        }
    }

}
