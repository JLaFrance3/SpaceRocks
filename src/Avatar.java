/* 
 * Jean LaFrance
 * Avatar
 * Player character
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Avatar extends Ship{

    private InputHolder input;
    private double fireRate;    //Limit fire rate with increment
    private double fireCount;

    //Player avatar constructor
    public Avatar(BufferedImage sprite, GamePanel gp, InputHolder input) {
        super(sprite, gp, 725, 200, -90);

        this.input = input;
        this.fireRate = .05;
        this.fireCount = 0;
    }

    public void tick() {
        //Coordinate plane is all wonky due to graphics getting rotated
        switch(input.getInput()) {
            case 'w':
                setDY(-getSpeed());
                break;
            case 's':
                setDY(getSpeed());
                break;
            case 'e':
                getGP().pause();
                //TODO: menu
                break;
        }

        shoot();
        
        //Wall collision
        if(getY() < 0) {
            if (getDY() < 0) {
                setDY(0);
            }
        }
        if (getY() > getGP().getHeight() - 190) {
            if (getDY() > 0) {
                setDY(0);
            }
        }

        super.tick();

        setDY(0);
    }

    //Returns rate of fire multiplier
    public double getFireRate() {
        return fireRate;
    }

    //Set rate of fire
    public void setFireRate(double fr) {
        this.fireRate = fr;
    }

    public void shoot() {
        if(input.isShooting()) {
            fireCount += fireRate;

            if(fireCount >=1) {
                Projectile projectile = new Projectile(getProjectileSprite(), getGP(), this);
                getGP().getObjectManager().addFriendly(projectile);
                fireCount--;
            }
        }
    }

    //Call parent class overloaded method with rotation
    public void paint(Graphics2D brush) {
        super.paint(brush, getRotation());
    }
}
