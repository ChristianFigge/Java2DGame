package game.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

public abstract class BaseEntity<S extends Shape> {

    /*+++++++++++++++++++++++++ ATTRIBUTES +++++++++++++++++++++++++*/
    /**
     * For collision detection, we use the build-in methods of the Shape-Interface.
     * The classes that implement Shape also store x/y-coordinates and
     * width/height-sizes.
     */
    protected S hitbox;
    protected Color hitboxColor = Color.gray;

    /**
     *  The speed at which the entity moves through the game world, in pixels per frame.
     */
    protected double speed = 1.0;

    /**
     * Information about the game panel dimensions, needed for out-of-bounds checks.
     */
    protected double panelWidth, panelHeight;
    /*------------------------- ATTRIBUTES -------------------------*/

    /*++++++++++++++++++++ CONSTRUCTORS / INIT +++++++++++++++++++++*/
    public BaseEntity(double posX, double posY, double width, double height, int panelWidth, int panelHeight) {
        this.hitbox = createHitbox(posX, posY, width, height);
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    }

    /***
     * Called during object construction. Creates an object that implements the Shape interface.
     *
     * @param posX   x-coordinate of the entity on the game panel
     * @param posY   y-coordinate of the entity on the game panel
     * @param width  width of the entity in pixels
     * @param height  height of the entity in pixels
     */
    protected abstract S createHitbox(double posX, double posY, double width, double height);
    /*-------------------- CONSTRUCTORS / INIT ---------------------*/

    /*++++++++++++++++++++++++++ METHODS +++++++++++++++++++++++++++*/
    /**
     * Draws the entity on the game panel. By default, it simply draws the hitbox of
     * the object. Overwrite the method to change that.
     *
     * @param g2d The Graphics class of the game panel
     */
    public void draw(Graphics2D g2d) {
        g2d.setColor(this.hitboxColor);
        g2d.fill(hitbox);
    }

    /**
     * Returns the X coordinate of the upper-left corner of the entity's bounding box.
     * The bounding box is the smallest Rectangle that the hitbox completely fits in.
     * @return X coordinate of the upper-left corner of the bounding box
     */
    public double getX() {
        return hitbox.getBounds2D().getX();
    }

    /**
     * Returns the Y coordinate of the upper-left corner of the entity's bounding box.
     * The bounding box is the smallest Rectangle that the hitbox completely fits in.
     * @return Y coordinate of the upper-left corner of the bounding box
     */
    public double getY() {
        return hitbox.getBounds2D().getY();
    }

    /**
     * Returns the width of the entity's bounding box in pixels.
     * The bounding box is the smallest Rectangle that the hitbox completely fits in.
     * @return width of the upper-left corner of the bounding box in pixels
     */
    public double getWidth() {
        return hitbox.getBounds2D().getWidth();
    }

    /**
     * Returns the height of the entity's bounding box in pixels.
     * The bounding box is the smallest Rectangle that the hitbox completely fits in.
     * @return height of the upper-left corner of the bounding box in pixels
     */
    public double getHeight() {
        return hitbox.getBounds2D().getHeight();
    }

    /**
     * Returns the speed of this entity in pixels per frame.
     * @return the speed of this entity in pixels per frame
     */
    public double getSpeed() { return this.speed; }

    /**
     * Sets the speed of this entity in pixels per frame.
     * @param newSpeed new speed of the entity
     */
    public void setSpeed(double newSpeed) {
        this.speed = newSpeed;
    }

    /**
     * Checks if every part of the entity is within the bounds of the game panel.
     */
    public boolean isCompletelyInPanel() {
        double x = getX();
        double y = getY();
        return
                x >= 0 &&
                x + getWidth() < panelWidth &&
                y >= 0 &&
                y + getHeight() < panelHeight;
    }

    public boolean collidesWith(BaseEntity<? extends Shape> other) {
        return this.hitbox.intersects(other.hitbox.getBounds2D());
    }
}
