/* 
 * Jean LaFrance
 * Avatar
 * Player character
 */

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Avatar extends Ship{

    //Intitial values used in game reset
    private static final double INITIAL_FIRE_RATE = 0.03;
    private static final double INITIAL_HEALTH = 100;
    private static final double INITIAL_SHIELD = 60;
    private static final int INITIAL_SPEED = 5;
    private static final double INITIAL_DAMAGE = 30;
    private static final int INITIAL_X = 725, INITIAL_Y = 200;

    //Stat upgrade values
    private static final double FIRE_RATE_INCREMENT = 0.01;
    private static final int HEALTH_INCREMENT = 20;
    private static final int SHIELD_INCREMENT = 20;
    private static final int SPEED_INCREMENT = 1;
    private static final double DAMAGE_INCREMENT = 20;
    private static final double PROJECTILE_CONVERSION = 8.0;

    private SpriteSheet ships;              //Holds all ships
    private SpriteSheet lasers;             //Holds all lasers
    private BufferedImage shieldSprite;     //Shield animation on hit
    private float shieldAlpha;              //Used to fade out shield
    private int[] upgradeCounter;           //Counts individual stat upgrades to upgrade ship at threshold
    private InputHolder input;              //Holds control issued by control panel
    private double fireRate;                //Limit fire rate with increment
    private double fireCount;               //Counter for fire rate
    private int shieldCounter;              //Counter for shield animation
    private boolean shieldUp;               //Used for painting shield
    private int shipTier;                   //Used in determining current ship sprite
    private boolean isBeamProjectile;       //Projectile type: beam or turret. Based on stat upgrades
    private double maxHealth, maxShield;    //Max values used by health/shield status bars

    //Player avatar constructor
    public Avatar(SpriteSheet ships, SpriteSheet lasers, BufferedImage shieldSprite, GamePanel gp, InputHolder input) {
        super(ships.getSprite(1, 1), gp, INITIAL_X, INITIAL_Y, -90);

        this.ships = ships;
        this.lasers = lasers;
        this.shieldSprite = shieldSprite;
        this.shieldAlpha = 1.0f;
        this.upgradeCounter = new int[] {0, 0, 0, 0, 0};
        this.input = input;
        this.fireRate = INITIAL_FIRE_RATE;
        this.fireCount = 0;
        this.shieldCounter = 0;
        this.shipTier = 1;
        this.isBeamProjectile = true;
        this.maxHealth = INITIAL_HEALTH;
        this.maxShield = INITIAL_SHIELD;

        setHealth(maxHealth);
        setShield(maxShield);
        setSpeed(INITIAL_SPEED);
        setDamage(INITIAL_DAMAGE);
        setProjectileSprite(lasers.getSprite(1, 1));
    }

    //Game tick
    @Override
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

        //Shield animation
        if (shieldUp) {
            if (shieldCounter < 9) {
                shieldCounter++;
                shieldAlpha -= 0.1f;
            }
            else {
                shieldCounter = 0;
                shieldAlpha = 1.0f;
                shieldAnim(false);
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
        this.shieldAlpha = 1.0f;
        this.fireRate = INITIAL_FIRE_RATE;
        this.fireCount = 0;
        this.shieldCounter = 0;
        this.shipTier = 1;
        this.isBeamProjectile = true;
        this.maxHealth = INITIAL_HEALTH;
        this.maxShield = INITIAL_SHIELD;
        
        setHealth(maxHealth);
        setShield(maxShield);
        setSpeed(INITIAL_SPEED);
        setDamage(INITIAL_DAMAGE);
        setDX(0);
        setDY(0);
        setLocation(INITIAL_X, INITIAL_Y);

        lasers.setPointer(42, 68, 0, 90);
        setProjectileSprite(lasers.getSprite(1, 1));
        setSprite(ships.getSprite(1, 1));

        createMask();
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
    @Override
    public void shoot() {
        //Check user input
        if(input.isShooting()) {
            fireCount += fireRate;

            //Limit fire rate
            if(fireCount >=1) {
                Projectile projectile = new Projectile(getProjectileSprite(), getGP(), this, isBeamProjectile);
                projectile.setHealth(getDamage());
                projectile.setDamage(getDamage());
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

                    createMask(36, 50);
                }
                break;
        } 

        //Projectile sprite upgrade
        if (shipTier == 5) {
            lasers.setPointer(42, 68, 0, 90);
            setProjectileSprite(lasers.getSprite(5, 1));

            //Decrease fire rate and increase damage if changing projectile type
            if (!isBeamProjectile) {
                fireRate /= PROJECTILE_CONVERSION;
                setDamage(getDamage() * PROJECTILE_CONVERSION);
                isBeamProjectile = true;
            }
        }
        else {
            //Turret style projectile
            if (upgradeCounter[0] > upgradeCounter[4]) {
                //Increase fire rate and decrease damage if changing to turret projectiles
                if (isBeamProjectile) {
                    fireRate *= PROJECTILE_CONVERSION;
                    setDamage(getDamage() / PROJECTILE_CONVERSION);
                    isBeamProjectile = false;
                }

                lasers.setPointer(50, 90, 0, 0);
                switch (upgradeCounter[0]) {
                    case 1 -> setProjectileSprite(lasers.getSprite(1, 1));
                    case 2 -> setProjectileSprite(lasers.getSprite(2, 1));
                    case 3 -> setProjectileSprite(lasers.getSprite(3, 1));
                    case 4 -> setProjectileSprite(lasers.getSprite(4, 1));
                    case 5 -> setProjectileSprite(lasers.getSprite(6, 1));
                    case 6 -> setProjectileSprite(lasers.getSprite(5, 1));
                }
            }
            //Beam style projectile
            else {
                //Decrease fire rate and increase damage if changing to beam projectiles
                if (!isBeamProjectile) {
                    fireRate /= PROJECTILE_CONVERSION;
                    setDamage(getDamage() * PROJECTILE_CONVERSION);
                    isBeamProjectile = true;
                }

                lasers.setPointer(42, 68, 0, 90);
                switch (upgradeCounter[4]) {
                    case 1 -> setProjectileSprite(lasers.getSprite(2, 1));
                    case 2 -> setProjectileSprite(lasers.getSprite(2, 1));
                    case 3 -> setProjectileSprite(lasers.getSprite(3, 1));
                    case 4 -> setProjectileSprite(lasers.getSprite(4, 1));
                    case 5 -> setProjectileSprite(lasers.getSprite(6, 1));
                    case 6 -> setProjectileSprite(lasers.getSprite(7, 1));
                }
            }
        }
    }

    //Shield animation
    public void shieldAnim(Boolean b) {
        shieldUp = b;
    }

    //Ship upgrades selected in menu increase by static increment
    //Upgrade fire rate
    public void upgradeFireRate() {
        if (isBeamProjectile) {
            this.fireRate += FIRE_RATE_INCREMENT;
        }
        else {
            this.fireRate += FIRE_RATE_INCREMENT * PROJECTILE_CONVERSION;
        }
        upgradeCounter[0]++;
    }

    //Upgrade health
    public void upgradeHealth() {
        this.maxHealth += HEALTH_INCREMENT;

        setHealth(getHealth() + HEALTH_INCREMENT);
        upgradeCounter[1]++;
    }

    //Upgrade shield
    public void upgradeShield() {
        this.maxShield += SHIELD_INCREMENT;

        setShield(getShield() + SHIELD_INCREMENT);
        upgradeCounter[2]++;
    }

    //Upgrade speed
    public void upgradeSpeed() {
        setSpeed(getSpeed() + SPEED_INCREMENT);
        upgradeCounter[3]++;
    }

    //Upgrade damage
    public void upgradeDamage() {
        if (isBeamProjectile) {
            setDamage(getDamage() + DAMAGE_INCREMENT);
        }
        else {
            setDamage(getDamage() + DAMAGE_INCREMENT / PROJECTILE_CONVERSION);
        }
        upgradeCounter[4]++;
    }

    //Get max health
    public double getMaxHealth() {
        return maxHealth;
    }

    //Get max shield
    public double getMaxShield() {
        return maxShield;
    }

    //Call parent class overloaded method with rotation
    @Override
    public void paint(Graphics2D brush) {
        super.paint(brush, getRotation());

        if (shieldUp) {
            //Change graphics2d alpha, draw shield, reset alpha to full
            brush.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, shieldAlpha));
            brush.drawImage(shieldSprite, getX(), getY() - 10, null);
            brush.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }
}
