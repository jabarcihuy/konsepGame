package algorythm.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameOverPanel extends JPanel {
    private JLabel scoreLabel;
    private Timer timer;
    private final int[] drops;
    private final Random random = new Random();
    private final Font matrixFont = new Font("Monospaced", Font.PLAIN, 16);

    private final Color COLOR_BG = new Color(25, 10, 15);
    private final Color COLOR_BG_FADE = new Color(25, 10, 15, 60);
    private final Color COLOR_SCANLINE = new Color(0, 0, 0, 100);
    private final Color COLOR_MATRIX_RED = new Color(255, 50, 50);
    private final Color COLOR_TITLE = new Color(255, 50, 100);
    private final Color COLOR_SCORE = new Color(255, 200, 0);
    
    private final Font FONT_TITLE = new Font("Consolas", Font.BOLD, 64);
    private final Font FONT_SCORE = new Font("Consolas", Font.BOLD, 36);

    public GameOverPanel(ActionListener restartAction, ActionListener menuAction) {
        setPreferredSize(new Dimension(800, 600));
        setBackground(COLOR_BG);
        setLayout(new GridBagLayout());

        // Initialize digital rain drops (one for each column)
        drops = new int[800 / 15 + 1];
        for (int i = 0; i < drops.length; i++) {
            drops[i] = random.nextInt(600 / 15);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("SYSTEM FAILURE");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_TITLE);
        titleLabel.putClientProperty("html.disable", true);
        
        gbc.gridy = 0;
        add(titleLabel, gbc);

        scoreLabel = new JLabel("FINAL SCORE: 0");
        scoreLabel.setFont(FONT_SCORE);
        scoreLabel.setForeground(COLOR_SCORE);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 20, 60, 20);
        add(scoreLabel, gbc);

        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        TerminalButton restartBtn = new TerminalButton("REBOOT SYSTEM", new Color(0, 255, 100));
        restartBtn.addActionListener(restartAction);
        gbc.gridy = 2;
        add(restartBtn, gbc);

        TerminalButton menuBtn = new TerminalButton("RETURN TO MAIN", new Color(200, 200, 200));
        menuBtn.addActionListener(menuAction);
        gbc.gridy = 3;
        add(menuBtn, gbc);

        // Animation timer for red digital rain (70ms for performance)
        timer = new Timer(70, e -> repaint());
        timer.start();
    }

    public void setFinalScore(int score) {
        scoreLabel.setText("FINAL SCORE: " + score);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Fading trails
        g2d.setColor(COLOR_BG_FADE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Matrix Rain (Red)
        g2d.setFont(matrixFont);
        g2d.setColor(COLOR_MATRIX_RED);
        int width = getWidth();
        int height = getHeight();
        
        for (int i = 0; i < drops.length; i++) {
            char c = (char) (0x30A0 + random.nextInt(96));
            int x = i * 15;
            int y = drops[i] * 15;
            
            g2d.drawChars(new char[]{c}, 0, 1, x, y);
            
            if (y > height && random.nextFloat() > 0.95f) {
                drops[i] = 0;
            }
            drops[i]++;
        }

        g2d.setColor(COLOR_SCANLINE);
        for (int i = 0; i < height; i += 6) {
            g2d.drawLine(0, i, width, i);
        }
    }
}
