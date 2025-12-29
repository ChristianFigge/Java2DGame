package game.util;

import game.entities.Obstacle;
import game.entities.Player;

import java.util.List;
import java.util.Random;

public class ObstacleRowFactory {
    /**
     * Default height of the obstacles in an obstacle row, in pixels.
     */
    public static final double DEFAULT_ROW_HEIGHT = 10;

    private final Random rng = new Random();
    private int panelWidth, panelHeight;
    private double gameDifficulty = 1.0;

    public ObstacleRowFactory(double gameDifficulty, int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        setDifficulty(gameDifficulty);
    }

    public void setDifficulty(double gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
        // Maybe later do some stuff here
    }

    public void setRngSeed(long seed) {
        rng.setSeed(seed);
    }

    /**
     * @param true_probability The probability of the function to return true in [0.0f, 1.0f]
     * @return true or false based on the given probability
     */
    private boolean coinflip(float true_probability) {
        if (true_probability < 1.0f) // Skips the number generation if unnecessary
            return rng.nextFloat() < true_probability;
        return true;
    }

    /**
     * Creates random obstacles in a horizontal row and adds them to the given List of Obstacles.
     * The row has at least one gap for the player to fit through. The minimum width of these
     * gaps is determined by the current game difficulty (TODO).
     * The obstacles have equal height and are placed just outside the upper bound of the
     * game panel, with their Y-coordinates defined as the negative default_row_height value.
     *
     * @param obstacles A List of Obstacle objects
     */
    public void createObstacleRow(List<Obstacle> obstacles) {
        // This function divides the panelWidth in N parts which the player is guaranteed to fit through.
        // It then "coinflips" for each part to determine if it's a gap or an obstacle, but the heads/tails
        // (true/false) probabilities are derived from the game difficulty, so that gaps might be less
        // likely to occur on harder levels.
        double dPanelWidth = panelWidth; // cast to double
        final int N = (int) (dPanelWidth / ((Player.DEFAULT_WIDTH * 2.0 + 2.0))); // TODO use gameDiff
        float obstacleProbability = 0.75f; // TODO use gameDifficulty

        // Randomly determine the type of each part (obstacle or gap?)
        boolean noGap = true; // Monitors the gap creation (we need at least 1!)
        boolean[] partIsObstacle = new boolean[N]; // All false by default
        for (int i = 0; i < N; ++i) {
            if (coinflip(obstacleProbability))
                partIsObstacle[i] = true;
            else if (noGap)
                noGap = false;
        }

        // If somehow no gap has been created randomly, we force one into existence:
        if(noGap)
            partIsObstacle[rng.nextInt(N)] = false;

        // Create obstacle rectangles based on the coinflips
        int iSuccessiveObs = 0; // Counts successive obstacle parts
        double partWidth = dPanelWidth / N; // The width of each part in pixels
        for(int i = 0; i < N + 1; ++i) {
            if(i < N && partIsObstacle[i]) {
                ++iSuccessiveObs;
            }
            else if (iSuccessiveObs > 0) {
                double x = (i - iSuccessiveObs) * partWidth; // X-coordinate of the upper left corner of the Obstacle
                double width = iSuccessiveObs * partWidth; // Width of the Rectangle
                obstacles.add(new Obstacle(x, -DEFAULT_ROW_HEIGHT, width, DEFAULT_ROW_HEIGHT, panelWidth, panelHeight));
                iSuccessiveObs = 0; // Reset to 0
            }
        }
    }
}
