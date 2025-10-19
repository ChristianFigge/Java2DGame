package ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import game.entities.Player;
import game.input.KeyboardInput;

public class GamePanel extends JPanel implements Runnable {
    /**
     * This is a sub-class of JPanel that displays and runs the game.
     */
    public static final long DESIRED_FPS = 60;
    // Floor division, but input feels smoother compared to using Nanoseconds
    public static final long DESIRED_FRAMETIME_MILLIS = 1000 / DESIRED_FPS;

    int width;
    int height;
    Player player;

    Thread gameThread;
    KeyboardInput input = new KeyboardInput();

    public GamePanel(int width, int height) {
        this.width = width;
        this.height = height;
        this.player = new Player(100, 100, width, height);

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.black);

        setDoubleBuffered(true); // Use drawing buffer
        setFocusable(true); // Needed for KeyListener to work

        addKeyListener(this.input); // Add listener for keyboard events
    }

    /**
     * Creates a GamePanel with default width and height (480x360).
     */
    public GamePanel() {
        this(480, 360);
    }

    public void startGame() {
        this.gameThread = new Thread(this);
        this.gameThread.start(); // executes run()
    }

    @Override
    public void run() {
        while (this.gameThread != null) {
            // STEP 1: update game information
            update();

            // STEP 2: draw updated game information on the screen
            repaint(); // This calls the paintComponent(...) method defined below

            // STEP 3: Wait a bit for the CPU to cool down :)
            try {
                Thread.sleep(DESIRED_FRAMETIME_MILLIS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void update() {
        handleKeyboardInput();
    }

    private void handleKeyboardInput() {
        // Read input and change game information accordingly
        if (input.left)
            this.player.moveLeft();
        if (input.right)
            this.player.moveRight();
        if (input.up)
            this.player.moveUp();
        if (input.down)
            this.player.moveDown();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Graphics2D will be our "pencil" for drawing stuff on the panel
        Graphics2D g2d = (Graphics2D) g;

        // Give player the pencil to draw h*self
        this.player.draw(g2d);

        // Free resources
        g2d.dispose();
    }
}
