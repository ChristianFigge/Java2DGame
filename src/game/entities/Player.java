package game.entities;

import game.input.KeyboardInput;
import game.util.ResourceHelper;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Player extends BaseEntity<Ellipse2D.Double> {

    /*+++++++++++++++++++++++++ ATTRIBUTES +++++++++++++++++++++++++*/
    public static final double DEFAULT_WIDTH = 49.0;
    public static final double DEFAULT_HEIGHT = 49.0;
    public static final double DEFAULT_SPEED = 2.0;
    public static final double BOOST_SPEED = 5.0;

    private boolean isHit = false;

    // TODO create Sprite class?
    private final BufferedImage sprite = ResourceHelper.loadImage("/images/spaceship_with_ape.png");
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
        // TODO create Sprite class?
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
        // Prevent entity from leaving the game panel
        if (hitbox.x > 0.0)
            hitbox.x -= speed;
    }

    public void moveRight() {
        // Prevent entity from leaving the game panel
        if (hitbox.x < panelWidth - hitbox.width)
            hitbox.x += speed;
    }

    public void moveUp() {
        // Prevent entity from leaving the game panel
        if (hitbox.y > 0.0)
            hitbox.y -= speed;
    }

    public void moveDown() {
        // Prevent entity from leaving the game panel
        if(hitbox.y < panelHeight - hitbox.height)
            hitbox.y += speed;
    }

    public void handleKeyboardInput(KeyboardInput keyboard) {
        // Sprite rotation is movement-dependent.
        // So we reset it to 0.0 und update based on input.
        spriteRotation = 0.0;

        if (keyboard.up) moveUp();
        if (keyboard.down) moveDown();

        if (keyboard.left) {
            spriteRotation -= ROTATE_DEGREES;
            moveLeft();
        }
        if (keyboard.right) {
            spriteRotation += ROTATE_DEGREES;
            moveRight();
        }

        if (keyboard.sprint) {
            spriteRotation *= BOOST_ROTATE_FACTOR;
            setSpeed(Player.BOOST_SPEED);
        }
        else
            setSpeed(Player.DEFAULT_SPEED);
    }

    public void setX(int posX) {
        hitbox.x = posX;
    }

    public void setY(int posY) {
        hitbox.y = posY;
    }

    public void setPos(int posX, int posY) {
        setX(posX);
        setY(posY);
    }

    public boolean isHit() { return this.isHit; }

    /**
     * Checks if the Player collides with any Obstacle in the given List,
     * blocks Player movement and sets the isHit attribute accordingly.
     * @param obstacles A List of BaseEntities (e.g. Obstacles)
     */
    public boolean isHitByObstacle(List<Obstacle> obstacles) {
        this.isHit = false;
        for(Obstacle obs: obstacles) {
            while(this.collidesWith(obs)) {
                if(!this.isHit) this.isHit = true;

                // Get info on which side of the Obstacle the Player is (left, top, right...)
                int outcode = obs.hitbox.outcode(getX(), getY())
                        | obs.hitbox.outcode(getX() + hitbox.width, getY() + hitbox.height);

                // Check collisions on Y axis
                // Ignore this part if outcode suggests BOTH Top & Bottom (possible depending on obstacle height)
                if(((outcode & Rectangle2D.OUT_TOP) != 0) ^ ((outcode & Rectangle2D.OUT_BOTTOM) != 0)) {
                    if ((outcode & Rectangle2D.OUT_TOP) != 0) {
                        hitbox.y -= 1; // Push player upward
                    } else if ((outcode & Rectangle2D.OUT_BOTTOM) != 0) {
                        hitbox.y += 1; // Push player downward
                    }
                    continue; // Recheck collision
                }

                // Check collisions on X axis
                if((outcode & Rectangle2D.OUT_LEFT) != 0) {
                    hitbox.x -= 1; // Push player leftward
                }
                else if ((outcode & Rectangle2D.OUT_RIGHT) != 0) {
                    hitbox.x += 1; // Push player rightward
                }
            }

            if(this.isHit) break;
        }
        return this.isHit;
    }

    /**
     * Checks if the Player collides with any Coin in the given List.
     * If true, it removes that Coin object from the List.
     * @param coins List of Coin objects
     * @return true/false
     */
    public boolean isHitByCoin(List<Coin> coins) {
        for(Coin e: coins) {
            if(this.collidesWith(e)) {
                coins.remove(e);
                return true;
            }
        }
        return false;
    }
}
