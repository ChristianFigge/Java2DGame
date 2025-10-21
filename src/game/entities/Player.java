package game.entities;

import java.awt.Color;
import java.awt.geom.Ellipse2D;

public class Player extends BaseEntity<Ellipse2D.Double> {

    /*+++++++++++++++++++++++++ ATTRIBUTES +++++++++++++++++++++++++*/
    public static final double DEFAULT_WIDTH = 21.0;
    public static final double DEFAULT_HEIGHT = 21.0;
    /*------------------------- ATTRIBUTES -------------------------*/

    /*++++++++++++++++++++ CONSTRUCTORS / INIT +++++++++++++++++++++*/
    public Player(double posX, double posY, int panelWidth, int panelHeight) {
        // Call parent class constructor and set some attributes
        super(posX, posY, DEFAULT_WIDTH, DEFAULT_HEIGHT, panelWidth, panelHeight);
        hitboxColor = Color.green;
        speed = 2.0;
    }

    @Override
    protected Ellipse2D.Double createHitbox(double posX, double posY, double width, double height) {
        // For now, we use a simple Ellipse/Circle as hitbox
        return new Ellipse2D.Double(posX, posY, width, height);
    }
    /*-------------------- CONSTRUCTORS / INIT ---------------------*/

    /*++++++++++++++++++++++++++ METHODS +++++++++++++++++++++++++++*/
    public void moveLeft() {
        hitbox.x -= speed;
        // Prevent entity from leaving the game panel
        if (hitbox.x < 0.0)
            hitbox.x = 0.0;
    }

    public void moveRight() {
        hitbox.x += speed;
        // Prevent entity from leaving the game panel
        double right_bound = panelWidth - hitbox.width - 1;
        if (hitbox.x > right_bound)
            hitbox.x = right_bound;
    }

    public void moveUp() {
        hitbox.y -= speed;
        // Prevent entity from leaving the game panel
        if (hitbox.y < 0.0)
            hitbox.y = 0.0;
    }

    public void moveDown() {
        hitbox.y += speed;
        // Prevent entity from leaving the game panel
        double bottom_bound = panelHeight - hitbox.height - 1;
        if (hitbox.y > bottom_bound)
            hitbox.y = bottom_bound;
    }

    public void setPos(int posX, int posY) {
        hitbox.x = posX;
        hitbox.y = posY;
    }
}
