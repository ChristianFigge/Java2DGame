package ui.panels;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JPanel;

import game.entities.Coin;
import game.entities.Obstacle;
import game.entities.Player;
import game.input.KeyboardInput;
import game.input.MouseInput;
import game.util.CoinFactory;
import game.util.ObstacleRowFactory;
import game.util.ResourceHelper;

/**
 * This is a subclass of JPanel that displays and runs the game.
 */
public class GamePanel extends JPanel implements Runnable {
    /*+++++++++++++++++++++++++ ATTRIBUTES +++++++++++++++++++++++++*/
    public static final long DESIRED_FPS = 60;
    // Floor division, but input feels smoother compared to using Nanoseconds
    public static final long DESIRED_FRAMETIME_MILLIS = 1000 / DESIRED_FPS;

    int width;
    int height;
    Player player;
    int playerScore = 0;

    BufferedImage imgBackground;
    private double bgY = 0.0;

    double gameDifficulty = 1.0;
    ObstacleRowFactory obsRowFactory;
    LinkedList<Obstacle> obstacles = new LinkedList<>();
    CoinFactory coinFactory;
    LinkedList<Coin> coins = new LinkedList<>();

    Thread gameThread;
    KeyboardInput keyboard = new KeyboardInput();
    MouseInput mouse = new MouseInput();

    boolean drawFPS = true;
    long fps = 0;
    /*------------------------- ATTRIBUTES -------------------------*/

    /*++++++++++++++++++++ CONSTRUCTORS / INIT +++++++++++++++++++++*/
    public GamePanel(int width, int height) {
        this.width = width;
        this.height = height;
        // Spawn player entity in the center of the panel (approx)
        this.player = new Player(width / 2.0, height / 2.0, width, height);

        // Create Factories
        this.obsRowFactory = new ObstacleRowFactory(gameDifficulty, width, height);
        this.coinFactory = new CoinFactory(gameDifficulty, width, height);

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.black);

        setDoubleBuffered(true); // Use drawing buffer
        setFocusable(true); // Needed for KeyListener to work

        addKeyListener(this.keyboard); // Add listener for keyboard events

        // Init background image (assumes square image!)
        int size = Math.max(this.width, this.height);
        imgBackground = ResourceHelper.loadAndScaleImage("/images/bg_space1.png", size, size);

        /*
        // Make mouse cursor invisible (only while it's inside the panel)
        setCursor(
                Toolkit.getDefaultToolkit().createCustomCursor(
                        new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                        new Point(),
                        "blank"
                )
        );

        // There are different interfaces for mouse motion and button/wheel inputs.
        // Our MouseListener combines them, though
        addMouseMotionListener(this.mouse);
        addMouseListener(this.mouse);
        */
    }

    /**
     * Creates a GamePanel with default width and height (480x360).
     */
    public GamePanel() {
        this(800, 600);
    }

    public void startGame() {
        this.gameThread = new Thread(this);
        this.gameThread.start(); // executes run()
    }
    /*-------------------- CONSTRUCTORS / INIT ---------------------*/

    /*++++++++++++++++++++++++++ METHODS +++++++++++++++++++++++++++*/

    /**
     * Runs the game loop, ideally in a separate Thread.
     */
    @Override
    public void run() {
        // Variables for FPS Counting. Unused if drawFPS == false
        long lastSecondTime = System.currentTimeMillis();
        int frame_count = 0;

        while (this.gameThread != null) {
            // While the game grows more complex, execution of update() and repaint()
            // might take a few milliseconds on slower machines.
            // To offset this delay for the sleep time per frame, we measure the
            // execution time and subtract it from the desired frame time.
            long lastFrameTime = System.currentTimeMillis();

            // STEP 1: Update game information
            update();

            // STEP 2: Draw updated game information on the screen
            repaint(); // This calls the paintComponent(...) method defined below

            long currentFrameTime = System.currentTimeMillis();

            // Optional: Count & update FPS
            if (drawFPS) {
                frame_count++;
                double millisecsElapsed = currentFrameTime - lastSecondTime;
                if (millisecsElapsed > 999) {
                    // At least 1 sec passed -> update approx. FPS
                    this.fps = Math.round(frame_count / (millisecsElapsed / 1000.0));
                    // (Re)Set variables for the next second
                    lastSecondTime = currentFrameTime;
                    frame_count = 0;
                }
            }

            // STEP 3: Wait a bit for the CPU to cool down :)
            long frametime = currentFrameTime - lastFrameTime;
            long sleeptime = DESIRED_FRAMETIME_MILLIS - frametime;
            if (sleeptime < 1) sleeptime = 1;
            try {
                Thread.sleep(sleeptime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Updates the game state. Should be called once every frame
     * from within the game loop.
     */
    public void update() {
        // For testing purposes, we either use mouse or keyboard
        // for changing the player's position. Subject to change.
        if (mouse.isInPanel)
            handleMouseInput();
        else
            handleKeyboardInput();

        // COLLISION CHECKS:
        // Obstacle collision
        if(player.isHitByObstacle(obstacles)) {
            // TODO Punish player, e.g. lose score points or life
            player.setHitboxColor(Color.red);
            playerScore -= 10;
        } else {
            player.setHitboxColor(Color.green);
        }

        // Coin collision
        if(player.isHitByCoin(coins)) {
            // TODO Reward player, e.g. increase score points
            playerScore += 100;
        }

        // Move Coins & Obstacles down the panel
        descendCoinsAndObstacles();

        // Create new Obstacles & Coins, if needed
        int obsDistance = 200; // TODO use gameDifficulty to calculate obstacle distance
        if (obstacles.isEmpty() || obstacles.peekLast().getY() > obsDistance) {
            obsRowFactory.createObstacleRow(obstacles);
            coinFactory.createCoinsInArea(obsDistance, coins);
        }

        // TODO DRY
        // Remove obstacles that have already left the panel, so we don't run out of RAM
        while (!(obstacles.isEmpty()) && obstacles.peekFirst().getY() > this.width) {
            obstacles.removeFirst();
        }

        // Remove coins that have already left the panel, so we don't run out of RAM
        while (!(coins.isEmpty()) && coins.peekFirst().getY() > this.width) {
            coins.removeFirst();
        }
    }

    /**
     * Reads keyboard input and changes the game state accordingly.
     */
    private void handleKeyboardInput() {
        // Player class handles player entity controls:
        this.player.handleKeyboardInput(keyboard);

        // Maybe do other stuff here later
    }

    /**
     * Reads mouse input and changes the game state accordingly.
     */
    private void handleMouseInput() {
        player.setPos(mouse.cursorPosX, mouse.cursorPosY);
    }

    /**
     * Draws an approximate FPS value in the upper left corner of the game panel.
     *
     * @param g2d The Graphics2D object of the game panel
     */
    private void printFPSOnPanel(Graphics2D g2d) {
        g2d.setColor(Color.gray);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(this.fps + " FPS", 0, 12);
    }

    private void printScoreOnPanel(Graphics2D g2d) {
        if(player.isHit())
            g2d.setColor(Color.red);
        else
            g2d.setColor(Color.white);

        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Score: " + this.playerScore, 0, height - 12);
    }

    private void descendCoinsAndObstacles() {
        for (Obstacle obs : obstacles) {
            obs.moveDown();
        }

        for (Coin c: coins) {
            c.moveDown();
        }
    }

    /**
     * Paints a frame on the game panel based on the current game state.
     * Should not be called directly, but via JPanel.repaint() from within the game loop.
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Graphics2D will be our "pencil" for drawing stuff on the panel
        Graphics2D g2d = (Graphics2D) g;

        drawBackground(g2d);

        // Draw obstacles
        for (Obstacle obs : obstacles)
            obs.draw(g2d);

        // Draw coins
        for(Coin c: coins)
            c.draw(g2d);

        // Give player the pencil to draw h*self
        this.player.draw(g2d);

        if (drawFPS)
            printFPSOnPanel(g2d);

        printScoreOnPanel(g2d);

        // Free resources
        g2d.dispose();
    }

    /**
     * Uses a seamlessly tileable image (think of textures)
     * that is drawn twice (one below the other) and pushed along
     * the y-Axis to create a scrolling background on the panel.
     * @param g2d the Graphics2D object of the panel
     */
    private void drawBackground(Graphics2D g2d) {
        // Increment Y-Position of the first image:
        // Scrolls background down by 1 pixel every 3 frames
        bgY += 1.0 / 3.0;
        // Y-Position of the second instance of the same image
        // that will be drawn above the first
        double y2 = bgY - imgBackground.getHeight();

        // If the lower image A leaves the panel, first switch its position
        // with upper image B and then put B above that
        if (bgY >= this.height) {
            bgY = y2;
            y2 = y2 - imgBackground.getHeight();
        }

        // Render image twice at different Y-coordinates
        g2d.drawImage(imgBackground, 0, (int)Math.round(bgY), this);
        g2d.drawImage(imgBackground, 0, (int)Math.round(y2), this);
    }
}
