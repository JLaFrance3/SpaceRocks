/* 
 * Jean LaFrance
 * ProjectileList
 * List of all current projectiles
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ObjectManager {
    private ArrayList<Mover> friendly;
    private ArrayList<Mover> hostile;
    private ArrayList<Mover> delHostile;
    private ArrayList<Mover> delFriendly;
    private GamePanel gp;
    private ControlPanel cp;
    private Random rand;

    private SpriteSheet smallRocks;
    private SpriteSheet medRocks;
    private SpriteSheet largeRocks;

    private int tickCounter, spawnRate;

    public ObjectManager(GamePanel gp, ControlPanel cp) {
        friendly = new ArrayList<Mover>();
        hostile = new ArrayList<Mover>();
        delHostile = new ArrayList<Mover>();
        delFriendly = new ArrayList<Mover>();
        rand = new Random();
        tickCounter = 0;
        spawnRate = 20;

        this.gp = gp;
        this.cp = cp;
    }

    public void init() {
        BufferedImageLoader loader = new BufferedImageLoader();
        BufferedImage smallRockSS;
        BufferedImage medRockSS;
        BufferedImage largeRockSS;

        try {
            smallRockSS = loader.load("res/SmallRocks.png");
            medRockSS = loader.load("res/MedRocks.png");
            largeRockSS = loader.load("res/BigRocks.png");

            smallRocks = new SpriteSheet(smallRockSS, 64, 64);
            medRocks = new SpriteSheet(medRockSS, 120, 120);
            largeRocks = new SpriteSheet(largeRockSS, 320, 320);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tick() {

        tickCounter++;

        if(tickCounter >= spawnRate) {
            spawnRock(3, 0);
            tickCounter = 0;
        }

        //Check out of bounds
        removeOOB();
        
        for(Mover m : friendly) {
            if (checkFriendlyCollision(m)) {
                delFriendly.add(m);
                //TODO: explosion
                if (m instanceof Avatar) {
                    //TODO: Game over
                    System.out.println("GameOver!");
                }
            }
        }
        for(Mover m : hostile) {
            if (checkHostileCollision(m)) {
                delHostile.add(m);
                //TODO: explosion
            }
        }
    }

    //Spawn rock randomly within spawn range: (-100, 0) -> (-100, 500)
    public void spawnRock(int quantity, int type) {
        int originY, endY, originMaxY, originMinY, endMaxY, endMinY;
        int height;
        double m, dx, dy;

        switch (type) {
            case 2:
                height = 320;
                break;
            case 1:
                height = 120;
                break;
            default:
                height = 64;
                break;
        }

        originMaxY = 500 - height/2;
        originMinY = 0 - height/2;
        endMaxY = 500 - height/2;
        endMinY = 0 - height/2;

        for(int i = 0; i < quantity; i++) {
            originY = rand.nextInt(originMaxY - originMinY + 1) + originMinY;
            endY = rand.nextInt(endMaxY - endMinY + 1) + endMinY;

            //(y-y1)=(x-x1)m
            m = (endY - originY) / ((800.0-height/2) - (-100.0-height/2));

            Rock rock = new Rock(smallRocks.getSprite(1, 1), gp, -100, originY);
            rock.setSpeed(rand.nextInt(9)+2);

            dx = rock.getSpeed();
            dy = m * dx;

            rock.setDX((int)dx);
            rock.setDY((int)dy);

            hostile.add(rock);
        }
    }

    private Boolean checkFriendlyCollision(Mover mover) {
        Boolean collision = false;

        for(Mover m : hostile) {
            if (mover.getMask().intersects(m.getMask())) {
                collision = true;
                break;
            }
        }

        return collision;
    }

    private Boolean checkHostileCollision(Mover mover) {
        Boolean collision = false;

        for(Mover m : friendly) {
            if (mover.getMask().intersects(m.getMask())) {
                collision = true;
                break;
            }
        }

        return collision;
    }

    //Remove all Movers out of bounds. Exclude spawn range.
    private void removeOOB() {
        for(int i = friendly.size() - 1; i >= 0; i--) {
            Mover p = friendly.get(i);
            p.tick();

            //Check bounds
            if (p.getX() < -20 || p.getX() > 820) {
                delFriendly.add(p);
            }
            if(p.getY() < -20 || p.getY() > 520) {
                delFriendly.add(p);
            }
        }

        for(int j = hostile.size() - 1; j >= 0; j--) {
            Mover q = hostile.get(j);
            q.tick();

            //Check bounds
            if (q.getX() < -120 || q.getX() > 850) {
                delHostile.add(q);
            }
            if(q.getY() < -120 || q.getY() > 550) {
                delHostile.add(q);
            }
        }

        friendly.removeAll(delFriendly);
        hostile.removeAll(delHostile);
    }

    public void addHostile(Mover m) {
        hostile.add(m);
    }

    public void removeHostile(Mover m) {
        hostile.remove(m);
    }

    public void addFriendly(Mover m) {
        friendly.add(m);
    }

    public void removeFriendly(Mover m) {
        friendly.remove(m);
    }

    public void paint(Graphics2D brush) {
        for(Mover m : hostile) {
            m.paint(brush);
        }
        for(Mover m : friendly) {
            m.paint(brush);
        }
    }
}
