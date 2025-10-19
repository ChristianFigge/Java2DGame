import ui.panels.GamePanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame appWindow = new JFrame("2D Game Test");
        appWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Quit the app when closing the window
        appWindow.setResizable(false); // The window has a fixed size
        appWindow.setLocationRelativeTo(null); // Parameter "null" places the window in the center of the screen

        GamePanel gamePanel = new GamePanel();
        appWindow.add(gamePanel);

        // Add Panels here

        appWindow.pack(); // Make the window's size fit the preferred size of its subcomponents.
        appWindow.setVisible(true); // Make the window actually appear on screen

        gamePanel.startGame(); // Start the game loop
    }
}