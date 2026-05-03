package algorythm.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TerminalButton extends JButton {
    public TerminalButton(String text, Color fgColor) {
        super(text);
        setFont(new Font("Consolas", Font.BOLD, 22));
        setForeground(fgColor);
        setBackground(new Color(15, 15, 20));
        setFocusPainted(false);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fgColor, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        setContentAreaFilled(false);
        setOpaque(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(fgColor);
                setForeground(new Color(15, 15, 20));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(new Color(15, 15, 20));
                setForeground(fgColor);
            }
        });
    }
}
