/* 
 * Jean LaFrance
 * Mover
 * Parent class for all moving objects in game
 */

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

abstract class Mover {
    private int speed;              //Pixels/second?
    private int x, y;               //Position
    private int dX, dY;             //Velocity
    private int rotation;           //Rotation of sprite
    private int damage;             //Damage

    private BufferedImage sprite;   //sprite image
    private GamePanel gp;           //Panel

    public Mover(BufferedImage sprite, GamePanel gp) {
        speed = 20;
        damage = 10;
        rotation = 0;

        dX = 0;
        dY = 0;

        this.sprite = sprite;
        this.gp = gp;

        setLocation(100, 100);
    }

    //Constructor with position/rotation
    public Mover(BufferedImage sprite, GamePanel gp, int x, int y, int rotation) {
        speed = 10;
        damage = 50;

        dX = 0;
        dY = 0;

        this.rotation = rotation;
        this.sprite = sprite;
        this.gp = gp;

        setLocation(x, y);
    }

    //Constructor with position and delta values
    public Mover(BufferedImage sprite, GamePanel gp, int x, int y, int dx, int dy) {
        rotation = 0;
        speed = 0;
        damage = 50;

        this.dX = dx;
        this.dY = dy;

        this.sprite = sprite;
        this.gp = gp;

        setLocation(x, y);
    }

    //Abstract methods
    abstract void paint(Graphics2D brush);

    //Game tick
    public void tick() {
        x+=dX;
        y+=dY;
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
            brush.drawImage(sprite, x, y, gp);
        }
        else {
            double rx = sprite.getWidth() / 2;
            double ry = sprite.getHeight() / 2;

            AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(rotation), rx, ry);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

            brush.drawImage(op.filter(sprite, null), x, y, null);
        }
    }
}
