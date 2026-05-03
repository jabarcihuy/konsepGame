package algorythm.swing;

import syntaxblade.engine.state.LogicPhaseState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    private Thread thread;
    private boolean running = false;
    private final int FPS = 60;
    private final long targetTime = 1000 / FPS;

    // Engine Core
    private LogicPhaseState logicPhaseState;
    private int score = 0;
    private int lives = 3;
    private int currentTier = 1;
    private float cursorBlinkTimer = 0f;
    private boolean showCursor = true;

    // Callbacks
    private Runnable onGameOver;

    // Feedback State
    private enum State {
        PLAYING,
        FEEDBACK
    }
    private State currentState = State.PLAYING;
    private float feedbackTimer = 0f;
    private boolean lastAnswerCorrect = false;
    private Random random = new Random();
    
    // Performance Cache for Syntax Highlighting
    private class TextToken {
        String text;
        Color color;
        TextToken(String t, Color c) { text = t; color = c; }
    }
    private java.util.List<java.util.List<TextToken>> cachedQuestionLines = new java.util.ArrayList<>();

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(15, 15, 20));
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleInput(e, true);
            }
        });
    }

    public void setOnGameOver(Runnable callback) {
        this.onGameOver = callback;
    }

    public int getScore() {
        return score;
    }

    public void resetGame() {
        score = 0;
        lives = 3;
        currentTier = 1;
        logicPhaseState = new LogicPhaseState();
        startNewPuzzle();
        start();
    }

    private void startNewPuzzle() {
        if (score > 600) {
            currentTier = random.nextInt(3) + 1; // 1-3
        } else if (score > 300) {
            currentTier = random.nextInt(2) + 1; // 1-2
        } else {
            currentTier = 1; // 1
        }
        
        logicPhaseState.enter(currentTier);
        currentState = State.PLAYING;
        cacheQuestionTokens();
    }

    private void cacheQuestionTokens() {
        cachedQuestionLines.clear();
        String question = logicPhaseState.getPuzzleQuestion();
        if (question == null) return;
        
        String[] lines = question.split("\n");
        for (String line : lines) {
            java.util.List<TextToken> lineTokens = new java.util.ArrayList<>();
            String[] tokens = line.split("(?<=[\\s\\(\\)\\[\\]\\{\\}<>;=+\\-*/%&|!,?])|(?=[\\s\\(\\)\\[\\]\\{\\}<>;=+\\-*/%&|!,?])");
            for (String token : tokens) {
                Color c = Color.WHITE;
                if (token.equals("int") || token.equals("bool") || token.equals("for") || token.equals("while") || 
                    token.equals("struct") || token.equals("new") || token.equals("delete") || token.equals("return") || 
                    token.equals("true") || token.equals("false")) {
                    c = new Color(255, 100, 200); // Pink
                } else if (token.equals("std::cout") || token.equals("std::vector") || token.equals("std::string") || 
                           token.equals("push_back") || token.equals("size")) {
                    c = new Color(100, 200, 255); // Light Blue
                } else if (token.matches("\\d+")) {
                    c = new Color(0, 255, 150); // Bright Green
                } else if (token.matches("[=+\\-*/%&|!<>?]+")) {
                    c = new Color(255, 200, 0); // Yellow
                } else if (token.matches("[\\[\\]\\{\\}\\(\\);,]+")) {
                    c = new Color(180, 180, 180); // Gray
                } else if (token.startsWith("\"") && token.endsWith("\"")) {
                    c = new Color(150, 255, 100); // Green string
                } else if (token.equals("What") || token.equals("is") || token.equals("the") || token.equals("output") || 
                           token.equals("output?") || token.equals("value") || token.equals("of") || token.equals("Type") || 
                           token.equals("or")) {
                    c = new Color(200, 200, 200); // Light Gray
                }
                lineTokens.add(new TextToken(token, c));
            }
            cachedQuestionLines.add(lineTokens);
        }
    }

    public void start() {
        if (thread == null || !running) {
            thread = new Thread(this);
            running = true;
            thread.start();
        }
    }

    @Override
    public void run() {
        long start, elapsed, wait;
        while (running) {
            start = System.nanoTime();

            update(1.0f / FPS);
            repaint();

            elapsed = System.nanoTime() - start;
            wait = targetTime - elapsed / 1000000;
            if (wait < 0) wait = 5;

            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void update(float delta) {
        cursorBlinkTimer += delta;
        if (cursorBlinkTimer >= 0.5f) {
            showCursor = !showCursor;
            cursorBlinkTimer = 0f;
        }

        if (currentState == State.PLAYING) {
            logicPhaseState.update(delta);
            
            if (logicPhaseState.isFinished()) {
                lastAnswerCorrect = logicPhaseState.wasSuccessful();
                if (lastAnswerCorrect) {
                    score += 100 * currentTier; 
                } else {
                    lives--; 
                }
                
                if (lives <= 0) {
                    if (onGameOver != null) {
                        running = false; 
                        SwingUtilities.invokeLater(onGameOver);
                    }
                } else {
                    currentState = State.FEEDBACK;
                    feedbackTimer = 2.0f;
                }
            }
        } else if (currentState == State.FEEDBACK) {
            feedbackTimer -= delta;
            if (feedbackTimer <= 0) {
                startNewPuzzle();
            }
        }
    }

    private void handleInput(KeyEvent e, boolean isPressed) {
        if (currentState == State.PLAYING && isPressed) {
            logicPhaseState.handleInput(e.getKeyChar());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawLogicPhase(g2d);
        
        // Scanlines
        g2d.setColor(new Color(0, 0, 0, 80));
        for (int i = 0; i < getHeight(); i += 4) {
            g2d.drawLine(0, i, getWidth(), i);
        }
    }

    private void drawLogicPhase(Graphics2D g) {
        // UI Box
        g.setColor(new Color(25, 30, 40));
        g.fillRoundRect(40, 40, 720, 520, 15, 15);
        g.setColor(new Color(0, 255, 200));
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(40, 40, 720, 520, 15, 15);

        // Header
        g.setFont(new Font("Consolas", Font.BOLD, 24));
        g.setColor(new Color(0, 255, 200));
        g.drawString("C++ EXECUTION ENVIRONMENT", 60, 80);
        
        // Stats
        g.setFont(new Font("Consolas", Font.BOLD, 18));
        g.setColor(Color.YELLOW);
        g.drawString(String.format("SCORE: %05d", score), 60, 110);
        
        g.setColor(new Color(255, 50, 100));
        g.drawString("LIVES: " + "♥".repeat(Math.max(0, lives)), 300, 110);
        
        g.setColor(new Color(200, 100, 255));
        g.drawString("TIER: " + currentTier, 650, 110);

        // Separator
        g.setColor(new Color(0, 255, 200, 100));
        g.drawLine(60, 130, 740, 130);

        if (currentState == State.PLAYING) {
            // Timer Bar
            float timeLeft = logicPhaseState.getTimeRemaining();
            float timeRatio = timeLeft / logicPhaseState.getTimeLimit();
            g.setColor(timeLeft < 5 ? new Color(255, 50, 100) : new Color(0, 255, 100));
            g.fillRect(60, 150, (int)(680 * timeRatio), 10);
            
            g.setFont(new Font("Consolas", Font.PLAIN, 16));
            g.drawString(String.format("T-MINUS: %.1fs", timeLeft), 60, 180);

            // Question
            g.setFont(new Font("Consolas", Font.PLAIN, 22));
            
            if (!cachedQuestionLines.isEmpty()) {
                int y = 230;
                FontMetrics fm = g.getFontMetrics();
                for (java.util.List<TextToken> lineTokens : cachedQuestionLines) {
                    int currentX = 60;
                    for (TextToken tt : lineTokens) {
                        g.setColor(tt.color);
                        g.drawString(tt.text, currentX, y);
                        currentX += fm.stringWidth(tt.text);
                    }
                    y += 30;
                }
                // Input Field
                g.setColor(new Color(0, 255, 200));
                g.setFont(new Font("Consolas", Font.BOLD, 28));
                String cursor = showCursor ? "█" : "";
                g.drawString("> " + logicPhaseState.getPlayerInput() + cursor, 60, y + 40);
            }
            
        } else if (currentState == State.FEEDBACK) {
            g.setFont(new Font("Consolas", Font.BOLD, 54));
            if (lastAnswerCorrect) {
                g.setColor(new Color(0, 255, 100));
                g.drawString("ACCESS GRANTED", 180, 300);
                g.setFont(new Font("Consolas", Font.PLAIN, 28));
                g.drawString("+" + (100 * currentTier) + " BYTES", 310, 360);
            } else {
                g.setColor(new Color(255, 50, 100));
                g.drawString("ACCESS DENIED", 190, 300);
                g.setFont(new Font("Consolas", Font.PLAIN, 28));
                g.drawString("SYSTEM CORRUPTION DETECTED", 200, 360);
            }
        }
    }
}
