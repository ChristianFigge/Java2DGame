package game.util;

import game.entities.Coin;
import game.entities.Obstacle;

import java.util.ArrayList;
import java.util.List;

public class CoinFactory extends BaseFactory {

    public CoinFactory(int panelWidth, int panelHeight) {
        super(panelWidth, panelHeight);
    }

    /**
     * Creates coins that are placed randomly in a rectangular area
     * with a specified height and the width of the game panel (in pixels).
     * These coins are then added to the provided List of Coin Objects.
     *
     * @param areaHeight height of the area in pixels
     * @param minCoins minimum number of coins
     * @param coins list of coins
     */
    public void createCoinsInArea(int areaHeight, int minCoins, List<Coin> coins) {
        // Randomly define the number of coins to generate
        final int numOfCoins = minCoins + rng.nextInt(4);

        if(numOfCoins > 0) {
            // Define bounds for random coordinates
            int minX = Coin.DEFAULT_COIN_DIAMETER;
            int maxX = panelWidth - (2 * Coin.DEFAULT_COIN_DIAMETER);
            int minY = -(Obstacle.DEFAULT_HEIGHT + areaHeight - Coin.DEFAULT_COIN_DIAMETER);
            int maxY = -(Obstacle.DEFAULT_HEIGHT + 2 * Coin.DEFAULT_COIN_DIAMETER);
            int rangeX = maxX - minX;
            int rangeY = maxY - minY;

            List<Coin> newCoins = new ArrayList<>(numOfCoins);

            // Generate <numOfCoins> new Coins
            for (int i = 0; i < numOfCoins; ++i) {
                Coin potentialCoin = null;
                while(potentialCoin == null) {
                    // Generate random coordinates for next Coin
                    int x = minX + rng.nextInt(rangeX);
                    int y = minY + rng.nextInt(rangeY);
                    potentialCoin = new Coin(x, y, panelWidth, panelHeight);

                    // Check if the generated Coin overlaps with existing Coins
                    // and if it does, discard it and create a new one
                    for(Coin c: newCoins) {
                        if(potentialCoin.collidesWith(c)) {
                            potentialCoin = null;
                            break;
                        }
                    }
                }
                newCoins.add(potentialCoin);
            }
            coins.addAll(newCoins);
        }
    }
}
