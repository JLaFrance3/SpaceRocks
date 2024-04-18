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
    
    private ArrayList<Mover> movers;
    private ArrayList<Mover> delMovers;
    private GamePanel gp;
    private ControlPanel cp;
    private Random rand;

    private SpriteSheet smallRocks;
    private SpriteSheet medRocks;
    private SpriteSheet largeRocks;

    private int tickCounter;

    public ObjectManager(GamePanel gp, ControlPanel cp) {
        movers = new ArrayList<Mover>();
        delMovers = new ArrayList<Mover>();
        rand = new Random();
        tickCounter = 0;

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

        if(tickCounter >= 20) {
            spawnRock(4, 0);
            tickCounter = 0;
        }

        //Check out of bounds
        removeOOB();
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

            movers.add(rock);
        }
    }

    //Remove all Movers out of bounds. Exclude spawn range.
    private void removeOOB() {
        for(Mover p : movers) {
            p.tick();

            //Check bounds
            if (p.getX() < -100 || p.getX() > 820) {
                delMovers.add(p);
            }
            if(p.getY() < -100 || p.getY() > 550) {
                delMovers.add(p);
            }
        }

        for(Mover q : delMovers) {
            movers.remove(q);
        }
    }

    public void addMover(Projectile p) {
        movers.add(p);
    }

    public void removeMover(Projectile p) {
        movers.remove(p);
    }

    public void paint(Graphics2D brush) {
        for(Mover p : movers) {
            p.paint(brush);
        }
    }

    //Delete
    public void debug() {
        for(Mover p : movers) {
            System.out.printf("(%d, %d)",p.getX(), p.getY());
        }
    }

}
