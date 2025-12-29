package ui.panels;

import javax.swing.*;
import java.awt.*;

public class GameOverOverlay extends JPanel {

    public GameOverOverlay(GameContainer gameContainer) {

        setOpaque(false); // wichtig!
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel lblGameOver = new JLabel("GAME OVER");
        lblGameOver.setFont(new Font("Arial", Font.BOLD, 48));
        lblGameOver.setForeground(Color.WHITE);
        lblGameOver.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnRestart = new JButton("Restart");
        btnRestart.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRestart.addActionListener(e -> {
            gameContainer.startNewGame();
        });

        add(Box.createVerticalGlue());
        add(lblGameOver);
        add(Box.createVerticalStrut(20));
        add(btnRestart);
        add(Box.createVerticalGlue());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.6f)); // Transparence
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}
