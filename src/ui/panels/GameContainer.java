package ui.panels;

import javax.swing.*;
import java.awt.*;

public class GameContainer extends JLayeredPane {

    private final GamePanel gamePanel;
    private final GameOverOverlay gameOverOverlay;

    public GameContainer(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setLayout(null);

        gameOverOverlay = new GameOverOverlay(this);
        gameOverOverlay.setBounds(0, 0, width, height);
        add(gameOverOverlay, Integer.valueOf(2));

        gamePanel = new GamePanel(width, height, this);
        gamePanel.setBounds(0, 0, width, height);
        add(gamePanel, Integer.valueOf(0));

        gameOverOverlay.setVisible(false);
    }

    public void startNewGame() {
        gameOverOverlay.setVisible(false);
        gamePanel.startNewGame();
    }

    public void showGameOver(boolean visible) {
        gameOverOverlay.setVisible(visible);
    }
}
