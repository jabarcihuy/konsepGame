package algorythm.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(ActionListener startAction, ActionListener exitAction) {
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(15, 15, 20));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("SYNTAX BLADE");
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 64));
        titleLabel.setForeground(new Color(0, 255, 200)); // Neon Cyan
        
        gbc.gridy = 0;
        add(titleLabel, gbc);
        
        JLabel subtitleLabel = new JLabel("C++ LOGIC TERMINAL");
        subtitleLabel.setFont(new Font("Consolas", Font.ITALIC, 24));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 20, 60, 20);
        add(subtitleLabel, gbc);

        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        TerminalButton startBtn = new TerminalButton("INITIALIZE CONNECTION", new Color(0, 255, 100));
        startBtn.addActionListener(startAction);
        gbc.gridy = 2;
        add(startBtn, gbc);

        TerminalButton exitBtn = new TerminalButton("TERMINATE SESSION", new Color(255, 50, 100));
        exitBtn.addActionListener(exitAction);
        gbc.gridy = 3;
        add(exitBtn, gbc);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Draw retro scanlines
        g2d.setColor(new Color(0, 0, 0, 80));
        for (int i = 0; i < getHeight(); i += 4) {
            g2d.drawLine(0, i, getWidth(), i);
        }
    }
}
