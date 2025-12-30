package game.util;

import game.entities.Player;

/**
 * Provides game variables derived from the current difficulty.
 */
public class GameDifficulty {
    /** Defines at which score you reach maximum difficulty, if
     * difficulty is derived from the score. */
    private static final double SCORE_LIMIT = 10000;

    private static final double MIN_ENTITY_SPEED = 1;
    private static final double MAX_ENTITY_SPEED = 5;
    private static final double ENTITY_SPEED_RANGE = MAX_ENTITY_SPEED - MIN_ENTITY_SPEED;
    private static final double MIN_OBS_DISTANCE = 150;
    private static final double MAX_OBS_DISTANCE = 500;
    private static final double OBS_DISTANCE_RANGE = MAX_OBS_DISTANCE - MIN_OBS_DISTANCE;

    private double difficulty;
    private double obstacleDistance;
    private double entitySpeed;
    private int minCoins;
    private float obstacleProbability;
    private double minGapWidth;

    public GameDifficulty() {
        setDifficulty(0);
    }

    public void setDifficulty(double difficulty) {
        // Ensure that 0 <= difficulty <= 1.0
        this.difficulty = Math.max(0.0, Math.min(difficulty, 1.0));

        // Set game variables
        entitySpeed = MIN_ENTITY_SPEED + ENTITY_SPEED_RANGE * difficulty;
        obstacleDistance = MAX_OBS_DISTANCE - OBS_DISTANCE_RANGE * difficulty;
        minCoins = (int)Math.round(4.0 * (1.0 - difficulty));
        obstacleProbability = (float)(0.25 + 0.5 * difficulty);
        minGapWidth = Player.DEFAULT_WIDTH * (1.0 + (2.0 * (1.0 - difficulty))) + 2.0;
    }

    public void setDifficultyFromScore(double score) {
        setDifficulty(Math.min(score, SCORE_LIMIT) / SCORE_LIMIT);
    }

    public double getObstacleDistance() {
        //System.out.println("OBS DISTANCE: " + obstacleDistance);
        return obstacleDistance;
    }

    public double getEntitySpeed() {
        //System.out.println("ENTITY SPEED: " + entitySpeed);
        return entitySpeed;
    }

    public int getMinCoins() {
        return minCoins;
    }

    public float getObstacleProbability() {
        return obstacleProbability;
    }

    public double getMinGapWidth() {
        return minGapWidth;
    }
}
