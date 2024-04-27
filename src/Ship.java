/* 
 * Jean LaFrance
 * Ship
 * Parent class for all ships in game
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

abstract class Ship extends Mover{
    
    
    private double shield;          //Damage negation

    private BufferedImage projectileSprite;     //Projectile sprite

    //Constructor with position
    public Ship(BufferedImage sprite, GamePanel gp, int x, int y, int rotation) {
        super(sprite, gp, x, y, rotation);
        
        this.shield = 0;
        this.projectileSprite = null;
    }

    //Abstract methods
    public abstract void shoot();

    //Game tick
    @Override
    public void tick() {
        super.tick();
    }

    //Returns entity shield
    public double getShield() {
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

    //Set ship sprite
    @Override
    public void setSprite(BufferedImage sprite) {
        super.setSprite(sprite);

        createMask();
    }

    //Sets shield
    public void setShield(double shield) {
        this.shield = shield;
    }

    //Paint method allowing for rotation of sprites
    @Override
    public void paint(Graphics2D brush, int rotation) {
        super.paint(brush, rotation);
    }
}
