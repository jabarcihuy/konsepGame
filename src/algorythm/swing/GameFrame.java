package algorythm.swing;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;
    private GameOverPanel gameOverPanel;

    public GameFrame() {
        setTitle("Syntax Blade - Logic Terminal Engine Prototype");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // 1. Initialize Main Menu
        mainMenuPanel = new MainMenuPanel(
            e -> showGame(), 
            e -> System.exit(0)
        );
        
        // 2. Initialize Game Panel
        gamePanel = new GamePanel();
        gamePanel.setOnGameOver(() -> showGameOver(gamePanel.getScore()));
        
        // 3. Initialize Game Over Panel
        gameOverPanel = new GameOverPanel(
            e -> showGame(),
            e -> showMainMenu()
        );
        
        mainPanel.add(mainMenuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(gameOverPanel, "GAMEOVER");
        
        add(mainPanel);
        pack();
        
        setLocationRelativeTo(null);
        setVisible(true);
        
        showMainMenu();
    }
    
    private void showMainMenu() {
        cardLayout.show(mainPanel, "MENU");
    }
    
    private void showGame() {
        cardLayout.show(mainPanel, "GAME");
        gamePanel.resetGame();
        gamePanel.requestFocusInWindow();
    }
    
    private void showGameOver(int finalScore) {
        gameOverPanel.setFinalScore(finalScore);
        cardLayout.show(mainPanel, "GAMEOVER");
    }
}
