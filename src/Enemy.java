/* 
 * Jean LaFrance
 * Enemy
 * Parent class for enemies in game
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Enemy extends Ship{
    
    private double fireRate;    //Limit fire rate with increment
    private double fireCount;

    public Enemy(BufferedImage sprite, GamePanel gp, int x, int y) {
        super(sprite, gp, 725, 200, 90);

        fireRate = .1;
        fireCount = 0;
    }

    //Returns rate of fire multiplier
    public double getFireRate() {
        return fireRate;
    }

    //Set rate of fire
    public void setFireRate(double fr) {
        this.fireRate = fr;
    }

    //Shoot projectile
    public void shoot() {
        if(fireCount >=1) {
            fireCount += fireRate;

            Projectile projectile = new Projectile(getProjectileSprite(), getGP(), this);
            getGP().getObjectManager().addHostile(projectile);

            fireCount--;
        }
    }

    @Override
    void paint(Graphics2D brush) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'paint'");
    }
}
