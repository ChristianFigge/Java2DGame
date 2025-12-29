package ui.panels;

import javax.swing.*;
import java.awt.*;

public class GameContainer extends JLayeredPane {

    private GamePanel gamePanel;
    //private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;

    public GameContainer(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setLayout(null);

        gameOverOverlay = new GameOverOverlay(this);
        gameOverOverlay.setBounds(0, 0, width, height);
        add(gameOverOverlay, Integer.valueOf(2));

        //pauseOverlay = new PauseOverlay();
        //pauseOverlay.setBounds(0, 0, width, height);
        //add(pauseOverlay, Integer.valueOf(1));

        gamePanel = new GamePanel(width, height, this);
        gamePanel.setBounds(0, 0, width, height);
        add(gamePanel, Integer.valueOf(0));

        gameOverOverlay.setVisible(false);
    }

    public void startNewGame() {
        gameOverOverlay.setVisible(false);
        gamePanel.startNewGame();
    }

    public void showPause(boolean visible) {
        //pauseOverlay.setVisible(visible);
    }

    public void showGameOver(boolean visible) {
        gameOverOverlay.setVisible(visible);
    }
}
