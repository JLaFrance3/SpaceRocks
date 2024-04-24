/* 
 * Jean LaFrance
 * Mover
 * Parent class for all moving objects in game
 */

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.Shape;

abstract class Mover {

    private final boolean displayHitbox = false;    //Used for debugging

    private int speed;              //Pixels/second?
    private int x, y;               //Position
    private int dX, dY;             //Velocity
    private int rotation;           //Rotation of sprite
    private int damage;             //Damage
    private Rectangle mask;         //Collision mask

    private BufferedImage sprite;   //sprite image
    private GamePanel gp;           //Panel

    public Mover(BufferedImage sprite, GamePanel gp) {
        this.speed = 20;
        this.x = 100;
        this.y = 100;
        this.dX = 0;
        this.dY = 0;
        this.rotation = 0;
        this.damage = 10;
        this.mask = null;

        this.sprite = sprite;
        this.gp = gp;

        createMask();
    }

    //Constructor with position
    public Mover(BufferedImage sprite, GamePanel gp, int x, int y) {
        this.speed = 5;
        this.x = x;
        this.y = y;
        this.dX = 0;
        this.dY = 0;
        this.rotation = 0;
        this.damage = 50;
        this.mask = null;

        this.sprite = sprite;
        this.gp = gp;

        createMask();
    }

    //Constructor with position/rotation
    public Mover(BufferedImage sprite, GamePanel gp, int x, int y, int rotation) {
        this.speed = 6;
        this.x = x;
        this.y = y;
        this.dX = 0;
        this.dY = 0;
        this.damage = 50;
        this.mask = null;

        this.rotation = rotation;
        this.sprite = sprite;
        this.gp = gp;

        createMask();
    }

    //Abstract methods
    abstract void paint(Graphics2D brush);

    //Creates rectangle based on RGB values for mask
    public void createMask() {
        Shape s;
        int width = sprite.getWidth();
        int height = sprite.getHeight();
        int top = height / 2;
        int bottom = top;
        int left = width / 2;
        int right = left;
        
        for(int i = 0; i < sprite.getWidth(); i++) {
            for(int j = 0; j < sprite.getHeight(); j++) {
                if (sprite.getRGB(i, j) != 0) {
                    top = Math.min(top, j);
                    bottom = Math.max(bottom, j);
                    left = Math.min(left, i);
                    right = Math.max(right, i);
                }
            }
        }
        
        Rectangle r = new Rectangle(left+x, top+y, right-left, bottom-top);
        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(rotation), r.getCenterX(), r.getCenterY());
        s = at.createTransformedShape(r);
        r = s.getBounds();

        this.mask = r;
    }

    //Game tick
    public void tick() {
        x+=dX;
        y+=dY;
        mask.translate(dX, dY);
    }

    //Returns entity speed
    public int getSpeed() {
        return speed;
    }
    
    //Returns entity damage
    public int getDamage() {
        return damage;
    }

    //Returns entity x coordinate
    public int getX() {
        return x;
    }

    //Returns entity y coordinate
    public int getY() {
        return y;
    }

    //Return collision mask
    public Rectangle getMask() {
        return mask;
    }

    //Returns entity x velocity
    public int getDX() {
        return dX;
    }

    //Returns entity y velocity
    public int getDY() {
        return dY;
    }

    //Returns entity rotation value
    public int getRotation() {
        return rotation;
    }

    //Returns entity sprite image
    public BufferedImage getSprite() {
        return sprite;
    }

    //Returns the game panel
    public GamePanel getGP() {
        return gp;
    }

    //Sets entity x and y coordinate
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //Sets entity rotation value
    public void setRotation(int rotation) {
        this.rotation = rotation;
        createMask();
    }

    //Sets entity sprite image
    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    //Sets speed
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    //Sets damage
    public void setDamage(int damage) {
        this.damage = damage;
    }

    //Sets x velocity
    public void setDX(int dX) {
        this.dX = dX;
    }

    //Sets y velocity
    public void setDY(int dY) {
        this.dY = dY;
    }

    //Paint method allowing for rotation of sprites
    public void paint(Graphics2D brush, int rotation) {

        if(rotation == 0) {
            brush.drawImage(sprite, x, y, null);
        }
        else {
            double rx = sprite.getWidth() / 2;
            double ry = sprite.getHeight() / 2;

            AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(rotation), rx, ry);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
            
            brush.drawImage(op.filter(sprite, null), x, y, null);
        }

        if(displayHitbox) {
            brush.setColor(java.awt.Color.RED);
            brush.draw(mask);
        }
    }
}
