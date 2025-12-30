package game.entities;

import game.util.ResourceHelper;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class Coin extends BaseEntity<Ellipse2D.Double> {
    /**
     * Default diameter of the coins, in pixels.
     */
    public static final int DEFAULT_COIN_DIAMETER = 32;

    private static final BufferedImage sprite = ResourceHelper.loadAndScaleImage(
            "/images/banana1.png", DEFAULT_COIN_DIAMETER, DEFAULT_COIN_DIAMETER);

    private final int posX; // Remains constant

    public Coin(double posX, double posY, double width, double height, int panelWidth, int panelHeight) {
        super(posX, posY, width, height, panelWidth, panelHeight);
        this.hitboxColor = Color.ORANGE;
        this.posX = (int)Math.round(this.getX());
    }

    public Coin(double posX, double posY, int panelWidth, int panelHeight) {
        this(posX, posY, DEFAULT_COIN_DIAMETER, DEFAULT_COIN_DIAMETER, panelWidth, panelHeight);
    }

    @Override
    protected Ellipse2D.Double createHitbox(double posX, double posY, double width, double height) {
        return new Ellipse2D.Double(posX, posY, width, height);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(sprite, posX, (int)Math.round(hitbox.y), null);
    }

    public void moveDown(double speed) {
        hitbox.y += speed;
    }
}
