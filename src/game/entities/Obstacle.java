package game.entities;

import java.awt.geom.Rectangle2D;

public class Obstacle extends BaseEntity<Rectangle2D.Double> {
    public Obstacle(double posX, double posY, double width, double height, int panelWidth, int panelHeight) {
        super(posX, posY, width, height, panelWidth, panelHeight);
    }

    @Override
    protected Rectangle2D.Double createHitbox(double posX, double posY, double width, double height) {
        return new Rectangle2D.Double(posX, posY, width, height);
    }

    public void moveDown() {
        hitbox.y += speed;
    }
}