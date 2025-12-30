package game.entities;

import game.util.ResourceHelper;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class Coin extends BaseEntity<Ellipse2D.Double> {
    /**
     * Default diameter of the coins, in pixels.
     */
    public static final double DEFAULT_COIN_DIAMETER = 32;

    private static BufferedImage sprite = ResourceHelper.loadAndScaleImage("/images/banana1.png", (int)DEFAULT_COIN_DIAMETER, (int)DEFAULT_COIN_DIAMETER);

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

    @Override
    public void draw(Graphics2D g2d) {
        //g2d.setColor(this.hitboxColor);
        //g2d.fill(hitbox);
        g2d.drawImage(sprite, (int)hitbox.x, (int)hitbox.y, null);
    }

    public void moveDown(double speed) {
        hitbox.y += speed;
    }
}
