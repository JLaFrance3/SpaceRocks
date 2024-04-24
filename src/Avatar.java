/* 
 * Jean LaFrance
 * Avatar
 * Player character
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Avatar extends Ship{

    //Intitial values used in game reset
    private static final double initialFireRate = 0.03;
    private static final int initialHealth = 100;
    private static final int initialShield = 0;
    private static final int initialSpeed = 6;
    private static final int initialDamage = 50;
    private static final int initialX = 725, initialY = 200;

    private InputHolder input;  //Holds control issued by control panel
    private double fireRate;    //Limit fire rate with increment
    private double fireCount;   //Counter

    //Player avatar constructor
    public Avatar(BufferedImage sprite, GamePanel gp, InputHolder input) {
        super(sprite, gp, initialX, initialY, -90);

        this.input = input;
        this.fireRate = initialFireRate;
        this.fireCount = 0;

        setHealth(initialHealth);
        setshield(initialShield);
        setSpeed(initialSpeed);
        setDamage(initialDamage);
    }

    //Game tick
    public void tick() {

        //Adjust velocity based on user input
        switch(input.getInput()) {
            case 'w':
                setDY(-getSpeed());
                break;
            case 's':
                setDY(getSpeed());
                break;
        }

        //Fire projectile if isShooting
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

        //Adjust position
        super.tick();

        //Reset velocity between ticks
        setDY(0);
    }

    //Reset to initial values
    public void reset() {
        this.fireRate = initialFireRate;
        this.fireCount = 0;
        setHealth(initialHealth);
        setshield(initialShield);
        setSpeed(initialSpeed);
        setDamage(initialDamage);
        setDX(0);
        setDY(0);
        setLocation(initialX, initialY);

        super.createMask();
    }

    //Returns rate of fire multiplier
    public double getFireRate() {
        return fireRate;
    }

    //Set rate of fire
    public void setFireRate(double fr) {
        this.fireRate = fr;
    }

    //Fire projectiles
    public void shoot() {
        //Check user input
        if(input.isShooting()) {
            fireCount += fireRate;

            //Limit fire rate
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
