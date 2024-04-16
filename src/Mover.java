/* 
 * Jean LaFrance
 * Mover
 * Parent class for all moving objects in game
 */

import java.awt.Graphics2D;
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

    //Constructor with rotation
    public Mover(BufferedImage sprite, GamePanel gp, int rotation) {
        speed = 10;
        damage = 50;

        dX = 0;
        dY = 0;

        this.rotation = rotation;
        this.sprite = sprite;
        this.gp = gp;

        setLocation(100, 100);
    }

    //Constructor with position
    public Mover(BufferedImage sprite, GamePanel gp, int x, int y, int rotation) {
        speed = 10;
        damage = 50;

        dX = 0;
        dY = 0;

        this.rotation = rotation;
        this.sprite = sprite;
        this.gp = gp;

        this.x = x;
        this.y = y;
        setLocation(x, y);
    }

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
            double rotateX_Axis = sprite.getWidth() / 2;
            double rotateY_Axis = sprite.getHeight() / 2;

            brush.rotate(Math.toRadians(rotation), rotateX_Axis, rotateY_Axis);

            brush.drawImage(sprite, x, y, gp);

            brush.rotate(Math.toRadians(-rotation), rotateX_Axis, rotateY_Axis);
        }
    }
}
