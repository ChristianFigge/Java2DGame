package game.util;

import java.util.Random;

abstract class BaseFactory {
    protected final Random rng = new Random();
    protected int panelWidth, panelHeight;

    protected BaseFactory(int panelWidth, int panelHeight) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    }

    public void setRngSeed(long seed) {
        rng.setSeed(seed);
    }
}
