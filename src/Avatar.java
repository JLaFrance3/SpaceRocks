/* 
 * Jean LaFrance
 * Avatar
 * Player character
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Avatar extends Ship{

    //Intitial values used in game reset
    private static final double INITIAL_FIRE_RATE = 0.03;
    private static final int INITIAL_HEALTH = 100;
    private static final int INITIAL_SHIELD = 0;
    private static final int INITIAL_SPEED = 5;
    private static final int INITIAL_DAMAGE = 50;
    private static final int INITIAL_X = 725, INITIAL_Y = 200;

    //Stat upgrade values
    private static final double FIRE_RATE_INCREMENT = 0.01;
    private static final int HEALTH_INCREMENT = 20;
    private static final int SHIELD_INCREMENT = 20;
    private static final int SPEED_INCREMENT = 1;
    private static final int DAMAGE_INCREMENT = 20;

    private SpriteSheet ships;      //Holds all ships
    private int[] upgradeCounter;   //Counts individual stat upgrades to upgrade ship at threshold
    private InputHolder input;      //Holds control issued by control panel
    private double fireRate;        //Limit fire rate with increment
    private double fireCount;       //Counter
    private int shipTier;           //Used in determining current ship sprite

    //Player avatar constructor
    public Avatar(SpriteSheet ships, BufferedImage sprite, GamePanel gp, InputHolder input) {
        super(sprite, gp, INITIAL_X, INITIAL_Y, -90);

        this.ships = ships;
        this.upgradeCounter = new int[] {0, 0, 0, 0, 0};
        this.input = input;
        this.fireRate = INITIAL_FIRE_RATE;
        this.fireCount = 0;
        this.shipTier = 1;

        setHealth(INITIAL_HEALTH);
        setshield(INITIAL_SHIELD);
        setSpeed(INITIAL_SPEED);
        setDamage(INITIAL_DAMAGE);
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
        for(int i = 0; i < upgradeCounter.length; i++) upgradeCounter[i] = 0;
        this.fireRate = INITIAL_FIRE_RATE;
        this.fireCount = 0;
        this.shipTier = 1;
        setSprite(ships.getSprite(1, 1));
        setHealth(INITIAL_HEALTH);
        setshield(INITIAL_SHIELD);
        setSpeed(INITIAL_SPEED);
        setDamage(INITIAL_DAMAGE);
        setDX(0);
        setDY(0);
        setLocation(INITIAL_X, INITIAL_Y);

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

    //Upgrade ship/projectile sprite based on the number of stat upgrades bought
    public void checkShipUpgrade() {
        int sum = 0;
        for(int i : upgradeCounter) sum += i;

        //Ship sprite upgrade
        switch (shipTier) {
            case 1:
                //Tier 2 ships
                if (upgradeCounter[0] + upgradeCounter[4] >= 2) {
                    setSprite(ships.getSprite(2, 1));
                    shipTier++;
                }
                else if (upgradeCounter[3] >= 2) {
                    setSprite(ships.getSprite(3, 1));
                    shipTier++;
                }
                else if (upgradeCounter[1] + upgradeCounter[2] >= 2) {
                    setSprite(ships.getSprite(4, 1));
                    shipTier++;
                }
                break;
            case 2:
                //Tier 3 ships
                if (upgradeCounter[0] + upgradeCounter[4] >= 5) {
                    setSprite(ships.getSprite(5, 1));
                    shipTier++;
                }
                else if (upgradeCounter[3] >= 5) {
                    setSprite(ships.getSprite(6, 1));
                    shipTier++;
                }
                else if (upgradeCounter[1] + upgradeCounter[2] >= 5) {
                    setSprite(ships.getSprite(7, 1));
                    shipTier++;
                }
                break;
            case 3:
                //Tier 4 ship
                if (sum >= 12) {
                    setSprite(ships.getSprite(8, 1));
                    shipTier++;
                }
                break;
            case 4:
                //Tier 5 ship
                if (sum >= 16) {
                    setSprite(ships.getSprite(9, 1));
                    shipTier++;
                }
                break;

            //TODO: Projectile sprite upgrades
        } 
    }

    //Ship upgrades selected in menu increase by static increment
    public void upgradeFireRate() {
        this.fireRate += FIRE_RATE_INCREMENT;
        upgradeCounter[0]++;
    }
    public void upgradeHealth() {
        setHealth(getHealth() + HEALTH_INCREMENT);
        upgradeCounter[1]++;
    }
    public void upgradeShield() {
        setshield(getshield() + SHIELD_INCREMENT);
        upgradeCounter[2]++;
    }
    public void upgradeSpeed() {
        setSpeed(getSpeed() + SPEED_INCREMENT);
        upgradeCounter[3]++;
    }
    public void upgradeDamage() {
        setDamage(getDamage() + DAMAGE_INCREMENT);
        upgradeCounter[4]++;
    }

    //Call parent class overloaded method with rotation
    public void paint(Graphics2D brush) {
        super.paint(brush, getRotation());
    }
}
