package game.entities;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Coin extends BaseEntity<Ellipse2D.Double> {

    public Coin(double posX, double posY, double width, double height, int panelWidth, int panelHeight) {
        super(posX, posY, width, height, panelWidth, panelHeight);
        this.hitboxColor = Color.ORANGE;
    }

    public Coin(double posX, double posY, double diameter, int panelWidth, int panelHeight) {
        this(posX, posY, diameter, diameter, panelWidth, panelHeight);
    }

    @Override
    protected Ellipse2D.Double createHitbox(double posX, double posY, double width, double height) {
        return new Ellipse2D.Double(posX, posY, width, height);
    }

    public void moveDown(double speed) {
        hitbox.y += speed;
    }
}
