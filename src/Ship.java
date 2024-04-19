/* 
 * Jean LaFrance
 * Entity
 * Parent class for all entities in game
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

abstract class Ship extends Mover{
    
    private int health;         //Health
    private int shield;          //Damage negation

    private BufferedImage projectileSprite;     //Projectile sprite

    //Constructor with position
    public Ship(BufferedImage sprite, GamePanel gp, int x, int y, int rotation) {
        super(sprite, gp, x, y, rotation);
        
        health = 100;
        shield = 0;
    }

    public abstract void shoot();

    public void tick() {
        super.tick();
    }

    //Returns entity health
    public int getHealth() {
        return health;
    }

    //Returns entity shield
    public int getshield() {
        return shield;
    }

    //Gets projectile sprite
    public BufferedImage getProjectileSprite() {
        return projectileSprite;
    }

    //Sets projectile sprite
    public void setProjectileSprite(BufferedImage sprite) {
        this.projectileSprite = sprite;
    }

    //Sets health
    public void setHealth(int health) {
        this.health = health;
    }

    //Sets shield
    public void setshield(int shield) {
        this.shield = shield;
    }

    //Paint method allowing for rotation of sprites
    public void paint(Graphics2D brush, int rotation) {
        super.paint(brush, rotation);
    }
}