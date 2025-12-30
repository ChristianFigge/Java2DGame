package game.entities;

import java.awt.geom.Rectangle2D;

public class Obstacle extends BaseEntity<Rectangle2D.Double> {
    /**
     * Default height of the obstacles in an obstacle row, in pixels.
     */
    public static final int DEFAULT_HEIGHT = 10;

    public Obstacle(double posX, double posY, double width, double height, int panelWidth, int panelHeight) {
        super(posX, posY, width, height, panelWidth, panelHeight);
    }

    public Obstacle(double posX, double posY, double width, int panelWidth, int panelHeight) {
        this(posX, posY, width, DEFAULT_HEIGHT, panelWidth, panelHeight);
    }

    @Override
    protected Rectangle2D.Double createHitbox(double posX, double posY, double width, double height) {
        return new Rectangle2D.Double(posX, posY, width, height);
    }

    public void moveDown(double speed) {
        hitbox.y += speed;
    }
}