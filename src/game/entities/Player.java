package game.entities;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;

public class Player extends BaseEntity<Ellipse2D.Double> {

    /*+++++++++++++++++++++++++ ATTRIBUTES +++++++++++++++++++++++++*/
    public static final double DEFAULT_WIDTH = 21.0;
    public static final double DEFAULT_HEIGHT = 21.0;
    public static final double DEFAULT_SPEED = 2.0;
    public static final double BOOST_SPEED = 5.0;

    private boolean isHit = false;
    /*------------------------- ATTRIBUTES -------------------------*/

    /*++++++++++++++++++++ CONSTRUCTORS / INIT +++++++++++++++++++++*/
    public Player(double posX, double posY, int panelWidth, int panelHeight) {
        // Call parent class constructor and set some attributes
        super(posX, posY, DEFAULT_WIDTH, DEFAULT_HEIGHT, panelWidth, panelHeight);
        hitboxColor = Color.green;
        speed = DEFAULT_SPEED;
    }

    @Override
    protected Ellipse2D.Double createHitbox(double posX, double posY, double width, double height) {
        // For now, we use a simple Ellipse/Circle as hitbox
        return new Ellipse2D.Double(posX, posY, width, height);
    }
    /*-------------------- CONSTRUCTORS / INIT ---------------------*/

    /*++++++++++++++++++++++++++ METHODS +++++++++++++++++++++++++++*/
    public void setHitboxColor(Color color) {
        this.hitboxColor = color;
    }

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

    public boolean isHit() { return this.isHit; }

    /**
     * Checks if the Player collides with any Entity in the given List
     * and sets the isHit Attribute accordingly.
     * @param entities A List of BaseEntities (e.g. Obstacles)
     */
    public <T extends BaseEntity<?>> boolean isHitBy(List<T> entities) {
        for(T e: entities) {
            if(this.collidesWith(e)) {
                this.isHit = true;
                return true;
            }
        }
        this.isHit = false;
        return false;
    }
}
