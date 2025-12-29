package game.entities;

import game.input.KeyboardInput;
import game.util.ResourceHelper;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Player extends BaseEntity<Ellipse2D.Double> {

    /*+++++++++++++++++++++++++ ATTRIBUTES +++++++++++++++++++++++++*/
    public static final double DEFAULT_WIDTH = 49.0;
    public static final double DEFAULT_HEIGHT = 49.0;
    public static final double DEFAULT_SPEED = 2.0;
    public static final double BOOST_SPEED = 5.0;

    private boolean isHit = false;

    // TODO create Sprite class
    private final BufferedImage sprite = ResourceHelper.loadImage("/images/spaceship_player.png");
    private double spriteRotation = 0.0;
    private static final double ROTATE_DEGREES = 25.0; // Degrees to rotate left or right based on input
    private static final double BOOST_ROTATE_FACTOR = 1.5; // Multiplier for rotation while speed boosting / sprinting

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
    @Override
    public void draw(Graphics2D g2d) {

        // Feedback if player hits an obstacle: Draw red hitbox outline.
        if(this.isHit) {
            g2d.setColor(Color.red);
            //g2d.fill(hitbox);
            g2d.setStroke(new BasicStroke(3f));
            g2d.draw(hitbox);
        }

        // Use affine transform for positioning & scaling of the spaceship image
        // TODO create Sprite class
        AffineTransform at = new AffineTransform();
        at.translate(hitbox.x, hitbox.y);
        at.scale(DEFAULT_WIDTH / sprite.getWidth(), DEFAULT_HEIGHT / sprite.getHeight());

        if(spriteRotation != 0.0)
            at.rotate(Math.toRadians(spriteRotation), sprite.getWidth() / 2.0, sprite.getHeight() / 2.0);

        g2d.drawImage(sprite, at, null);
    }

    public void setHitboxColor(Color color) {
        this.hitboxColor = color;
    }

    public void moveLeft() {
        hitbox.x -= speed;
        spriteRotation = -ROTATE_DEGREES;
        // Prevent entity from leaving the game panel
        if (hitbox.x < 0.0)
            hitbox.x = 0.0;
    }

    public void moveRight() {
        hitbox.x += speed;
        spriteRotation = ROTATE_DEGREES;
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

    public void handleKeyboardInput(KeyboardInput keyboard) {
        // Sprite rotation is movement-dependent.
        // So we reset it to 0.0 und update based on input.
        spriteRotation = 0.0;

        if (keyboard.left)
            moveLeft();
        if (keyboard.right)
            moveRight();
        if (keyboard.up)
            moveUp();
        if (keyboard.down)
            moveDown();
        if (keyboard.sprint) {
            spriteRotation *= BOOST_ROTATE_FACTOR;
            setSpeed(Player.BOOST_SPEED);
        }
        else
            setSpeed(Player.DEFAULT_SPEED);
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
    public <T extends BaseEntity<?>> boolean isHitByObstacle(List<T> entities) {
        this.isHit = false;
        for(T e: entities) {
            if(this.collidesWith(e)) {
                this.isHit = true;
                break;
            }
        }
        return this.isHit;
    }

    public <T extends BaseEntity<?>> boolean isHitByCoin(List<T> entities) {
        for(T e: entities) {
            if(this.collidesWith(e)) {
                entities.remove(e);
                return true;
            }
        }
        return false;
    }
}
