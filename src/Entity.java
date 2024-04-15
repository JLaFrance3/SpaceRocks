/* 
 * Jean LaFrance
 * Entity
 * Parent class for all entities in game
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

abstract class Entity {
    
    private int health;     //Health
    private int armor;      //Damage negation
    private int damage;     //Damage
    private int speed;      //Pixels/second?
    private int x, y;       //Position
    private int dX, dY;     //Velovity
    private int rotation;   //Rotation of sprite

    private BufferedImage sprite;   //sprite image
    private GamePanel gp;           //Image observer

    //Constructor
    public Entity(BufferedImage sprite, GamePanel gp) {
        health = 100;
        armor = 0;
        damage = 50;
        speed = 10;
        rotation = 0;

        dX = 0;
        dY = 0;

        this.sprite = sprite;
        this.gp = gp;

        setLocation(100, 100);
    }

    //Constructor with rotation
    public Entity(BufferedImage sprite, GamePanel gp, int rotation) {
        health = 100;
        armor = 0;
        damage = 50;
        speed = 10;

        dX = 0;
        dY = 0;

        this.rotation = rotation;
        this.sprite = sprite;
        this.gp = gp;

        setLocation(100, 100);
    }

    //Constructor with position
    public Entity(BufferedImage sprite, GamePanel gp, int x, int y, int rotation) {
        health = 100;
        armor = 0;
        damage = 50;
        speed = 10;

        dX = 0;
        dY = 0;

        this.rotation = rotation;
        this.sprite = sprite;
        this.gp = gp;

        this.x = x;
        this.y = y;
        setLocation(x, y);
    }

    public void tick() {
        x+=dX;
        y+=dY;
    }

    //Returns entity health
    public int getHealth() {
        return health;
    }

    //Returns entity armor
    public int getArmor() {
        return armor;
    }

    //Returns entity damage
    public int getDamage() {
        return damage;
    }

    //Returns entity speed
    public int getSpeed() {
        return speed;
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

    //Sets health
    public void setHealth(int health) {
        this.health = health;
    }

    //Sets armor
    public void setArmor(int armor) {
        this.armor = armor;
    }

    //Sets damage
    public void setDamage(int damage) {
        this.damage = damage;
    }

    //Sets speed
    public void setSpeed(int speed) {
        this.speed = speed;
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
