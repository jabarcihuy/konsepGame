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
    
    // Grid animation
    private float gridOffset = 0f;
    
    // Performance Cache for Syntax Highlighting
    private class TextToken {
        String text;
        Color color;
        TextToken(String t, Color c) { text = t; color = c; }
    }
    private java.util.List<java.util.List<TextToken>> cachedQuestionLines = new java.util.ArrayList<>();

    // Typewriter effect
    private float typewriterTimer = 0f;
    private int visibleChars = 0;
    private int totalChars = 0;
    private boolean typewriterFinished = false;

    // Glitch effect
    private float glitchTimer = 0f;
    private float glitchDuration = 0f;
    private int glitchIntensity = 0;

    // Tier Up Notification
    private float tierUpTimer = 0f;

    // Pre-allocated Colors and Fonts for Performance
    private final Color COLOR_BG = new Color(10, 12, 16);
    private final Color COLOR_GRID = new Color(0, 255, 200, 15);
    private final Color COLOR_SCANLINE = new Color(0, 0, 0, 80);
    private final Color COLOR_UI_BOX = new Color(25, 30, 40, 220);
    private final Color COLOR_NEON_CYAN = new Color(0, 255, 200);
    private final Color COLOR_NEON_RED = new Color(255, 50, 100);
    private final Color COLOR_NEON_GREEN = new Color(0, 255, 100);
    private final Color COLOR_PURPLE = new Color(200, 100, 255);
    private final Color COLOR_GLITCH_RED = new Color(255, 0, 0, 50);
    
    private final Font FONT_HEADER = new Font("Consolas", Font.BOLD, 24);
    private final Font FONT_STATS = new Font("Consolas", Font.BOLD, 18);
    private final Font FONT_TIMER = new Font("Consolas", Font.PLAIN, 16);
    private final Font FONT_TIMER_BOLD = new Font("Consolas", Font.BOLD, 16);
    private final Font FONT_QUESTION = new Font("Consolas", Font.PLAIN, 22);
    private final Font FONT_INPUT = new Font("Consolas", Font.BOLD, 28);
    private final Font FONT_FEEDBACK = new Font("Consolas", Font.BOLD, 54);
    private final Font FONT_TIER_UP = new Font("Consolas", Font.BOLD, 72);

    private final BasicStroke STROKE_BORDER = new BasicStroke(2);

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(COLOR_BG);
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
        tierUpTimer = 0f;
        logicPhaseState = new LogicPhaseState();
        startNewPuzzle();
        start();
    }

    private void startNewPuzzle() {
        int oldTier = currentTier;
        if (score >= 1000) {
            currentTier = 3;
        } else if (score >= 500) {
            currentTier = 2;
        } else {
            currentTier = 1;
        }
        
        if (currentTier > oldTier) {
            tierUpTimer = 2.0f;
        }
        
        logicPhaseState.enter(currentTier);
        currentState = State.PLAYING;
        cacheQuestionTokens();
        
        // Reset typewriter
        typewriterTimer = 0f;
        visibleChars = 0;
        typewriterFinished = false;
        
        // Reset glitch
        glitchDuration = 0f;
    }

    private void cacheQuestionTokens() {
        cachedQuestionLines.clear();
        totalChars = 0;
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
                totalChars += token.length();
            }
            cachedQuestionLines.add(lineTokens);
            totalChars++; // newline char
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
            if (wait < 5) wait = 5;

            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    private void update(float delta) {
        cursorBlinkTimer += delta;
        if (cursorBlinkTimer >= 0.5f) {
            showCursor = !showCursor;
            cursorBlinkTimer = 0f;
        }
        
        // Update grid animation
        gridOffset += 15f * delta;
        if (gridOffset > 40f) {
            gridOffset -= 40f;
        }

        if (currentState == State.PLAYING) {
            // Typewriter update
            if (!typewriterFinished) {
                typewriterTimer += delta * 45; // Faster typewriter
                visibleChars = (int) typewriterTimer;
                if (visibleChars >= totalChars) {
                    visibleChars = totalChars;
                    typewriterFinished = true;
                }
            }
            
            // Random glitch if lives are low
            if (lives == 1 && random.nextFloat() < 0.01f) {
                glitchDuration = 0.2f;
                glitchIntensity = 5;
            }
            
            logicPhaseState.update(delta);
            
            if (logicPhaseState.isFinished()) {
                lastAnswerCorrect = logicPhaseState.wasSuccessful();
                if (lastAnswerCorrect) {
                    score += 100 * currentTier; 
                } else {
                    lives--; 
                    glitchDuration = 0.5f;
                    glitchIntensity = 15;
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
        
        if (glitchDuration > 0) {
            glitchDuration -= delta;
        }
        
        if (tierUpTimer > 0) {
            tierUpTimer -= delta;
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
        
        // Drawing Grid (Optimized: No new Color objects)
        g2d.setColor(COLOR_GRID);
        int width = getWidth();
        int height = getHeight();
        for (int i = 0; i < width; i += 40) {
            g2d.drawLine(i, 0, i, height);
        }
        int offset = (int)gridOffset;
        for (int i = -40; i < height; i += 40) {
            int y = i + offset;
            g2d.drawLine(0, y, width, y);
        }

        // Screen shake effect
        int shakeX = 0;
        int shakeY = 0;
        if (currentState == State.PLAYING) {
            float timeLeft = logicPhaseState.getTimeRemaining();
            if (timeLeft < 5.0f && timeLeft > 0) {
                int intensity = (int)(5.0f - timeLeft);
                shakeX = (random.nextInt(intensity * 2 + 1) - intensity);
                shakeY = (random.nextInt(intensity * 2 + 1) - intensity);
            }
        }
        
        if (glitchDuration > 0) {
            shakeX += random.nextInt(glitchIntensity * 2 + 1) - glitchIntensity;
            shakeY += random.nextInt(glitchIntensity * 2 + 1) - glitchIntensity;
            
            if (random.nextFloat() < 0.1f) {
                g2d.setColor(COLOR_GLITCH_RED);
                g2d.fillRect(random.nextInt(width), random.nextInt(height), random.nextInt(200), random.nextInt(20));
            }
        }
        
        g2d.translate(shakeX, shakeY);
        drawLogicPhase(g2d);
        
        if (tierUpTimer > 0) {
            float alpha = Math.min(1.0f, tierUpTimer);
            g2d.setColor(new Color(255, 255, 0, (int)(alpha * 200))); // Dynamic alpha still needs new Color or composite
            g2d.setFont(FONT_TIER_UP);
            String msg = "TIER UP";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(msg, (width - fm.stringWidth(msg)) / 2, (height + fm.getAscent()) / 2);
        }
        
        g2d.translate(-shakeX, -shakeY);
        
        // Scanlines (Simplified for performance)
        g2d.setColor(COLOR_SCANLINE);
        for (int i = 0; i < height; i += 6) { // Increased spacing
            g2d.drawLine(0, i, width, i);
        }
    }

    private void drawLogicPhase(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // UI Box
        g.setColor(COLOR_UI_BOX);
        g.fillRoundRect(40, 40, 720, 520, 15, 15);
        
        // Border
        if (currentState == State.PLAYING && logicPhaseState.getTimeRemaining() < 5.0f) {
            float pulse = (float) (Math.sin(System.currentTimeMillis() / 100.0) * 0.5 + 0.5);
            g.setColor(new Color(255, 50, 50, 150 + (int)(100 * pulse)));
        } else {
            g.setColor(COLOR_NEON_CYAN);
        }
        g.setStroke(STROKE_BORDER);
        g.drawRoundRect(40, 40, 720, 520, 15, 15);

        // Header
        g.setFont(FONT_HEADER);
        g.setColor(COLOR_NEON_CYAN);
        g.drawString("C++ EXECUTION ENVIRONMENT", 60, 80);
        
        // Stats
        g.setFont(FONT_STATS);
        g.setColor(Color.YELLOW);
        g.drawString(String.format("SCORE: %05d", score), 60, 110);
        
        g.setColor(COLOR_NEON_RED);
        g.drawString("LIVES: " + "♥".repeat(Math.max(0, lives)), 300, 110);
        
        g.setColor(COLOR_PURPLE);
        g.drawString("TIER: " + currentTier, 650, 110);

        // Separator
        g.setColor(COLOR_NEON_CYAN);
        g.drawLine(60, 130, 740, 130);

        if (currentState == State.PLAYING) {
            float timeLeft = logicPhaseState.getTimeRemaining();
            float timeRatio = timeLeft / logicPhaseState.getTimeLimit();
            int barWidth = (int)(680 * timeRatio);
            
            // Timer Bar
            g.setColor(timeLeft < 5 ? new Color(255, 50, 100, 100) : new Color(0, 255, 100, 100));
            g.fillRect(60, 150, barWidth, 14);
            g.setColor(timeLeft < 5 ? COLOR_NEON_RED : COLOR_NEON_GREEN);
            g.fillRect(60, 152, barWidth, 10);
            
            g.setFont(timeLeft < 5 ? FONT_TIMER_BOLD : FONT_TIMER);
            g.drawString(String.format("T-MINUS: %.1fs", timeLeft), 60, 185);

            // Question rendering with typewriter
            if (!cachedQuestionLines.isEmpty()) {
                g.setFont(FONT_QUESTION);
                int y = 240;
                int currentCharsDrawn = 0;
                FontMetrics fm = g.getFontMetrics();
                
                outer:
                for (java.util.List<TextToken> lineTokens : cachedQuestionLines) {
                    int currentX = 60;
                    for (TextToken tt : lineTokens) {
                        int charsInToken = tt.text.length();
                        if (currentCharsDrawn + charsInToken <= visibleChars) {
                            g.setColor(tt.color);
                            g.drawString(tt.text, currentX, y);
                            currentX += fm.stringWidth(tt.text);
                            currentCharsDrawn += charsInToken;
                        } else {
                            int charsToDraw = visibleChars - currentCharsDrawn;
                            if (charsToDraw > 0) {
                                g.setColor(tt.color);
                                g.drawString(tt.text.substring(0, charsToDraw), currentX, y);
                            }
                            break outer;
                        }
                    }
                    y += 30;
                    currentCharsDrawn++;
                    if (currentCharsDrawn >= visibleChars) break;
                }
                
                if (visibleChars > totalChars / 2 || typewriterFinished) {
                    g.setColor(COLOR_NEON_CYAN);
                    g.setFont(FONT_INPUT);
                    g.drawString("> " + logicPhaseState.getPlayerInput() + (showCursor ? "█" : ""), 60, y + 40);
                }
            }
            
        } else if (currentState == State.FEEDBACK) {
            g.setFont(FONT_FEEDBACK);
            if (lastAnswerCorrect) {
                g.setColor(new Color(0, 100, 50)); // Shadow
                g.drawString("ACCESS GRANTED", 182, 302);
                g.setColor(COLOR_NEON_GREEN);
                g.drawString("ACCESS GRANTED", 180, 300);
            } else {
                g.setColor(new Color(150, 0, 0)); // Shadow
                g.drawString("ACCESS DENIED", 190 + random.nextInt(5)-2, 300 + random.nextInt(5)-2);
                g.setColor(COLOR_NEON_RED);
                g.drawString("ACCESS DENIED", 190, 300);
            }
        }
    }
}
