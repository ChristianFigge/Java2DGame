package game.util;

import game.entities.Coin;
import game.entities.Obstacle;
import game.entities.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CoinFactory {
    /**
     * Default diameter of the coins, in pixels.
     */
    public static final double DEFAULT_COIN_DIAMETER = 20;

    /**
     * A random number generator used to create the coins.
     */
    private final Random rng = new Random();

    /**
     * Information about the game panel dimensions, needed for constructing Coin objects.
     */
    private int panelWidth, panelHeight;
    private double gameDifficulty = 1.0;

    public CoinFactory(double gameDifficulty, int panelWidth, int panelHeight) {
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

    /*
     * Creates 1 to 3 coins that are placed randomly in a rectangular area
     * with a specified height and the width of the game panel (in pixels).
     * These coins are then added to the provided List of Coin Objects.
     *
     */
    public void createCoinsInArea(int areaHeight, List<Coin> coins) {
        // Randomly define the number of coins to generate
        final int numOfCoins = rng.nextInt(4);

        System.out.println("About to create" + numOfCoins + " coins!");

        if(numOfCoins > 0) {
            // Define bounds for random coordinates
            // TODO explain
            int maxY = -(int) (ObstacleRowFactory.DEFAULT_ROW_HEIGHT + Player.DEFAULT_HEIGHT + DEFAULT_COIN_DIAMETER);
            int minY = -(areaHeight - (int)Player.DEFAULT_HEIGHT);
            int rangeY = maxY - minY;
            int maxX = panelWidth - (int)(Player.DEFAULT_WIDTH + DEFAULT_COIN_DIAMETER);
            int minX = (int)Player.DEFAULT_WIDTH;
            int rangeX = maxX - minX;

            // Collection for accepted Coins, so we can easily check for overlaps
            // and deny generated Coins, if necessary
            List<Coin> newCoins = new ArrayList<>(numOfCoins);

            // Generate <numOfCoins> new Coins
            for (int i = 0; i < numOfCoins; ++i) {
                Coin potentialCoin = null;
                while(potentialCoin == null) {
                    // Generate random coordinates for next Coin
                    int x = minX + rng.nextInt(rangeX);
                    int y = minY + rng.nextInt(rangeY);
                    potentialCoin = new Coin(x, y, DEFAULT_COIN_DIAMETER, panelWidth, panelHeight);

                    // Check if new Coin overlaps with existing Coins
                    // and if it does, create new coin
                    for(Coin c: newCoins) {
                        if(potentialCoin.collidesWith(c)) {
                            potentialCoin = null;
                            break;
                        }
                    }
                }
                newCoins.add(potentialCoin);
            }
            System.out.println("Created " + newCoins.size() + " random Coins!");
            coins.addAll(newCoins);
        }


    }
}
