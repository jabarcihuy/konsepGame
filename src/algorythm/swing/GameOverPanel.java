package algorythm.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameOverPanel extends JPanel {
    private JLabel scoreLabel;

    public GameOverPanel(ActionListener restartAction, ActionListener menuAction) {
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(25, 10, 15));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("SYSTEM FAILURE");
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 64));
        titleLabel.setForeground(new Color(255, 50, 100));
        
        gbc.gridy = 0;
        add(titleLabel, gbc);

        scoreLabel = new JLabel("FINAL SCORE: 0");
        scoreLabel.setFont(new Font("Consolas", Font.BOLD, 36));
        scoreLabel.setForeground(new Color(255, 200, 0));
        
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
    }

    public void setFinalScore(int score) {
        scoreLabel.setText("FINAL SCORE: " + score);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 0, 0, 100));
        for (int i = 0; i < getHeight(); i += 4) {
            g2d.drawLine(0, i, getWidth(), i);
        }
    }
}
