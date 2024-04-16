/* 
 * Jean LaFrance
 * Entity
 * Parent class for all entities in game
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

abstract class Entity extends Mover{
    
    private int health;     //Health
    private int armor;      //Damage negation

    private BufferedImage projectileSprite;     //Projectile sprite

    //Constructor
    public Entity(BufferedImage sprite, GamePanel gp) {
        super(sprite, gp);
        
        health = 0;
        armor = 0;
    }

    //Constructor with rotation
    public Entity(BufferedImage sprite, GamePanel gp, int rotation) {
        super(sprite, gp, rotation);
        
        health = 100;
        armor = 0;
    }

    //Constructor with position
    public Entity(BufferedImage sprite, GamePanel gp, int x, int y, int rotation) {
        super(sprite, gp, x, y, rotation);
        
        health = 100;
        armor = 0;
    }

    public void tick() {
        super.tick();
    }

    //Returns entity health
    public int getHealth() {
        return health;
    }

    //Returns entity armor
    public int getArmor() {
        return armor;
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

    //Sets armor
    public void setArmor(int armor) {
        this.armor = armor;
    }

    //Paint method allowing for rotation of sprites
    public void paint(Graphics2D brush, int rotation) {
        super.paint(brush, rotation);
    }
}
