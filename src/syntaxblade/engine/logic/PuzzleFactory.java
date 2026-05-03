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
        int r = random.nextInt(5);
        switch(r) {
            case 0: return new BooleanPuzzle("int a = 5, b = 2;\nstd::cout << (a / b);\n\nWhat is the output?", "2", 15.0f);
            case 1: return new BooleanPuzzle("bool x = false, y = true;\nstd::cout << (!x && y);\n\nWhat is the output (1 or 0)?", "1", 15.0f);
            case 2: return new BooleanPuzzle("int x = 10;\nstd::cout << (x++ + 5);\n\nWhat is the output?", "15", 15.0f);
            case 3: return new BooleanPuzzle("int a = 8;\nint b = a >> 1;\nstd::cout << b;\n\nWhat is the output?", "4", 15.0f);
            case 4: return new BooleanPuzzle("int x = 7;\nstd::cout << (x % 3);\n\nWhat is the output?", "1", 15.0f);
            default: return new BooleanPuzzle("std::cout << (1 == 1);\nOutput?", "1", 15.0f);
        }
    }

    private static LogicPuzzle generateTier2Puzzle() {
        int r = random.nextInt(5);
        switch(r) {
            case 0: return new LoopPuzzle("int x = 5;\nint& ref = x;\nref = 10;\nstd::cout << x;\n\nWhat is the output?", "10", 20.0f);
            case 1: return new LoopPuzzle("int a = 3;\nint* ptr = &a;\n*ptr = *ptr * 2;\nstd::cout << a;\n\nWhat is the output?", "6", 20.0f);
            case 2: return new LoopPuzzle("int sum = 0;\nfor(int i=0; i<3; ++i) {\n  sum += i;\n}\nstd::cout << sum;\n\nWhat is the output?", "3", 20.0f);
            case 3: return new LoopPuzzle("int x = 0;\nwhile(x < 5) x += 2;\nstd::cout << x;\n\nWhat is the output?", "6", 20.0f);
            case 4: return new LoopPuzzle("int arr[3] = {1, 2, 3};\nstd::cout << *(arr + 1);\n\nWhat is the output?", "2", 20.0f);
            default: return new LoopPuzzle("int x=0;\nstd::cout << x;", "0", 20.0f);
        }
    }

    private static LogicPuzzle generateTier3Puzzle() {
        int r = random.nextInt(5);
        switch(r) {
            case 0: return new ArrayPuzzle("std::vector<int> v = {1, 2};\nv.push_back(3);\nstd::cout << v.size();\n\nWhat is the output?", "3", 25.0f);
            case 1: return new ArrayPuzzle("int* p = new int(4);\nint val = *p + 2;\ndelete p;\nstd::cout << val;\n\nWhat is the output?", "6", 25.0f);
            case 2: return new ArrayPuzzle("struct Node { int val; };\nNode n; n.val = 5;\nNode* ptr = &n;\nstd::cout << ptr->val;\n\nWhat is the output?", "5", 25.0f);
            case 3: return new ArrayPuzzle("std::string s = \"C++\";\nstd::cout << s[1];\n\nWhat is the output?", "+", 25.0f);
            case 4: return new ArrayPuzzle("int a[2][2] = {{1,2},{3,4}};\nstd::cout << a[1][0];\n\nWhat is the output?", "3", 25.0f);
            default: return new ArrayPuzzle("std::cout << 1;", "1", 25.0f);
        }
    }
}
