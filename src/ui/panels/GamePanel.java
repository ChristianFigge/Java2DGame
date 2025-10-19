package ui.panels;

import java.awt.*;

import javax.swing.JPanel;

import game.entities.Player;
import game.input.KeyboardInput;

/**
 * This is a subclass of JPanel that displays and runs the game.
 */
public class GamePanel extends JPanel implements Runnable {

    public static final long DESIRED_FPS = 60;
    // Floor division, but input feels smoother compared to using Nanoseconds
    public static final long DESIRED_FRAMETIME_MILLIS = 1000 / DESIRED_FPS;

    int width;
    int height;
    Player player;

    Thread gameThread;
    KeyboardInput input = new KeyboardInput();

    boolean drawFPS = true;
    int fps = 0;

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
        // Variables for FPS Counting. Unused if drawFPS == false
        long lastSecondTime = System.currentTimeMillis();
        int frame_count = 0;

        while (this.gameThread != null) {
            // STEP 1: update game information
            update();

            // STEP 2: draw updated game information on the screen
            repaint(); // This calls the paintComponent(...) method defined below

            // Optional: Count & update FPS
            if (drawFPS) {
                frame_count++;
                long currentFrameTime = System.currentTimeMillis();
                if (currentFrameTime - lastSecondTime > 999) {
                    // At least 1 sec passed -> update approx. FPS
                    this.fps = frame_count;
                    lastSecondTime = currentFrameTime;
                    frame_count = 0;
                }
            }

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

    /**
     * Draws an approximate FPS value in the upper left corner of the game panel.
     * @param g2d The Graphics2D object of the game panel
     */
    private void printFPSOnPanel(Graphics2D g2d) {
        g2d.setColor(Color.gray);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(this.fps + " FPS", 0, 12);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Graphics2D will be our "pencil" for drawing stuff on the panel
        Graphics2D g2d = (Graphics2D) g;

        // Give player the pencil to draw h*self
        this.player.draw(g2d);

        if(drawFPS) printFPSOnPanel(g2d);

        // Free resources
        g2d.dispose();
    }
}
