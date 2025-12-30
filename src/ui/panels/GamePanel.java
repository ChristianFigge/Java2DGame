package ui.panels;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JPanel;

import game.entities.BaseEntity;
import game.entities.Coin;
import game.entities.Obstacle;
import game.entities.Player;
import game.input.KeyboardInput;
import game.util.CoinFactory;
import game.util.GameDifficulty;
import game.util.ObstacleRowFactory;
import game.util.ResourceHelper;

/**
 * This is a subclass of JPanel that displays and runs the game.
 */
public class GamePanel extends JPanel implements Runnable {
    /*+++++++++++++++++++++++++ ATTRIBUTES +++++++++++++++++++++++++*/
    public static final long DESIRED_FPS = 60;
    // Rough floor division, but input feels smoother compared to using Nanoseconds
    public static final long DESIRED_FRAMETIME_MILLIS = 1000 / DESIRED_FPS;

    private final GameContainer gameContainer;
    private final int panelWidth;
    private final int panelHeight;

    private final KeyboardInput keyboard = new KeyboardInput();

    private final BufferedImage imgBackground;
    private double backgroundScrollPos;

    private Player player;
    private int playerScore;
    private GameDifficulty gameDifficulty;

    private final ObstacleRowFactory obsRowFactory;
    private final CoinFactory coinFactory;

    private LinkedList<Obstacle> obstacles;
    private LinkedList<Coin> coins;

    private Thread gameThread;
    private boolean gameIsRunning = false;

    private final boolean drawFPS = true;
    private long fps = 0;
    /*------------------------- ATTRIBUTES -------------------------*/

    /*++++++++++++++++++++ CONSTRUCTORS / INIT +++++++++++++++++++++*/
    public GamePanel(int width, int height, GameContainer gameContainer) {
        this.gameContainer = gameContainer;
        this.panelWidth = width;
        this.panelHeight = height;

        // Swing Component settings
        setPreferredSize(new Dimension(width, height));
        setDoubleBuffered(true); // Use drawing buffer
        setFocusable(true); // Needed for KeyListener to work
        addKeyListener(this.keyboard); // Add listener for keyboard events

        // Init Game Entity Factories
        obsRowFactory = new ObstacleRowFactory(width, height);
        coinFactory = new CoinFactory(width, height);

        // Init background image (NOTE: assumes a square image!)
        int size = Math.max(this.panelWidth, this.panelHeight);
        imgBackground = ResourceHelper.loadAndScaleImage("/images/bg_space1.png", size, size);
    }

    /**
     * (Re-)Initializes all game-relevant objects and variables.
     */
    private void initGame() {
        // Spawn player entity in the center of the panel (approx)
        player = new Player(panelWidth / 2.0, panelHeight / 2.0, panelWidth, panelHeight);

        // Create empty lists for Coins & Obstacles
        obstacles = new LinkedList<Obstacle>();
        coins = new LinkedList<Coin>();

        // Init game vars
        backgroundScrollPos = 0.0;
        playerScore = 0;
        gameDifficulty = new GameDifficulty();
        gameIsRunning = true;
    }

    /**
     * (Re-)starts the game thread.
     */
    public void startNewGame() {
        // Shut down the current gameThread, if any
        if(gameThread != null) {
            gameIsRunning = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // (Re-)init Game
        initGame();

        // Create & start new game Thread
        gameThread = new Thread(this);
        gameThread.start(); // executes run()
    }
    /*--------------------- CONSTRUCTORS / INIT ----------------------*/

    /*++++++++++++++++++++++++++ GAME LOOP +++++++++++++++++++++++++++*/

    /**
     * Runs the GAME LOOP, ideally in a separate Thread.
     */
    @Override
    public void run() {
        // Variables for FPS Counting. Unused if drawFPS == false
        long lastSecondTime = System.currentTimeMillis();
        int frame_count = 0;

        while (gameThread != null && gameIsRunning) {
            // While the game grows more complex, execution of update() and repaint()
            // might take a few milliseconds on slower machines.
            // To offset this delay for the sleep time per frame, we measure the
            // execution time and subtract it from the desired frame time.
            long lastFrameTime = System.currentTimeMillis();

            // STEP 1: Update game information
            update();

            // STEP 2: Draw updated game information on the screen
            repaint(); // Calls the paintComponent(...) method defined below

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
        player.handleKeyboardInput(keyboard);

        // Obstacle collision
        int oldScore = playerScore;
        if(player.isHitByObstacle(obstacles)) {
            playerScore -= 10; // Punish player
        }
        // Coin collision
        if(player.isHitByCoin(coins)) {
            playerScore += 100; // Reward player
        }

        if(gameIsOver()) {
            this.gameIsRunning = false;
            gameContainer.showGameOver(true);
            return;
        }

        // Adjust game difficulty based on Score
        if(playerScore != oldScore) {
            gameDifficulty.setDifficultyFromScore(playerScore);
        }

        // Move Coins & Obstacles down the panel
        for (Obstacle obs : obstacles) { obs.moveDown(gameDifficulty.getEntitySpeed()); }
        for (Coin c: coins) { c.moveDown(gameDifficulty.getEntitySpeed()); }

        // Create new Obstacles & Coins, if needed
        double obsDistance = gameDifficulty.getObstacleDistance();
        if (obstacles.isEmpty() || obstacles.peekLast().getY() > obsDistance) {
            obsRowFactory.createObstacleRow(gameDifficulty.getObstacleProbability(), obstacles);
            coinFactory.createCoinsInArea((int)obsDistance, gameDifficulty.getMinCoins(), coins);
        }

        // Remove descending entities that have already left the panel, so we don't run out of RAM
        clearEntityList(obstacles);
        clearEntityList(coins);
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
        Graphics2D g2d = (Graphics2D) g.create();

        drawBackground(g2d);

        // Draw coins & obstacles
        for (Obstacle o : obstacles) { o.draw(g2d); }
        for(Coin c: coins) { c.draw(g2d); }

        // Draw Player & Score
        player.draw(g2d);
        printScoreOnPanel(g2d);

        if (drawFPS) printFPSOnPanel(g2d);

        // Free resources
        g2d.dispose();
    }

    /*+++++++++++++++++++++++++++ HELPER FNCS +++++++++++++++++++++++++++++*/
    /**
     * Defines/Checks losing conditions and returns result.
     *
     * @return boolean, if the current game is lost or not
     */
    private boolean gameIsOver() {
        return this.playerScore < 0 || player.getY() > this.panelHeight;
    }

    /**
     * Removes descending BaseEntities from the specified List that have
     * left the GamePanel through its bottom.
     *
     * @param entities List of BaseEntities with LIFO-Queue ordering
     */
    private <T extends Shape> void clearEntityList(LinkedList<? extends BaseEntity<T>> entities) {
        while (!(entities.isEmpty()) && entities.peekFirst().getY() > this.panelHeight) {
            entities.removeFirst();
        }
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
        g2d.setColor(player.isHit() ? Color.red : Color.white);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Score: " + this.playerScore, 2, panelHeight - 12);
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
        backgroundScrollPos += 1.0 / 3.0;
        // Y-Position of the second instance of the same image
        // that will be drawn above the first
        double y2 = backgroundScrollPos - imgBackground.getHeight();

        // If the lower image A leaves the panel, switch its position
        // with upper image B and then put B above that
        if (backgroundScrollPos >= this.panelHeight) {
            backgroundScrollPos = y2;
            y2 = y2 - imgBackground.getHeight();
        }

        // Render image twice at different Y-coordinates
        g2d.drawImage(imgBackground, 0, (int)Math.round(backgroundScrollPos), this);
        g2d.drawImage(imgBackground, 0, (int)Math.round(y2), this);
    }
}
