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

    private SpriteSheet smallRockSS;
    private SpriteSheet medRockSS;
    private SpriteSheet largeRockSS;

    private BufferedImage[] smallRocks;
    private BufferedImage[] medRocks;
    private BufferedImage[] largeRocks;

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
        BufferedImage small;
        BufferedImage medium;
        BufferedImage large;

        try {
            small = loader.load("res/SmallRocks.png");
            medium = loader.load("res/MedRocks.png");
            large = loader.load("res/BigRocks.png");

            smallRockSS = new SpriteSheet(small, 64, 64);
            medRockSS = new SpriteSheet(medium, 120, 120);
            largeRockSS = new SpriteSheet(large, 320, 240);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //Load rock images for animation
        smallRocks = new BufferedImage[96];
        medRocks = new BufferedImage[96];
        for (int i = 0; i < 6; i++) {
            for(int j = 1; j <= 16; j++) {
                smallRocks[(i*16)+j-1] = smallRockSS.getSprite(j, i+1);
                medRocks[(i*16)+j-1] = medRockSS.getSprite(j, i+1);
            }
        }
        largeRocks = new BufferedImage[64];
        for (int i = 0; i < 4; i++) {
            for(int j = 1; j <= 16; j++) {
                largeRocks[(i*16)+j-1] = largeRockSS.getSprite(j, i+1);
            }
        }
    }

    public void tick() {

        tickCounter++;

        if(tickCounter >= spawnRate) {
            spawnRock(1, 0);
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
    public void spawnRock(int quantity, int size) {
        int originY, endY, originMaxY, originMinY, endMaxY, endMinY;
        int height, width;
        double m, dx, dy;

        switch (size) {
            case 2:
                height = 240;
                width = 320;
                break;
            case 1:
                height = 120;
                width = 120;
                break;
            default:
                height = 64;
                width = 64;
                break;
        }

        originMaxY = 480 - height/2;
        originMinY = -30 - height/2;
        endMaxY = 450 - height/2;
        endMinY = 0 - height/2;

        for(int i = 0; i < quantity; i++) {
            //Get random origin and end Y values for trajectory calculation
            originY = rand.nextInt(originMaxY - originMinY + 1) + originMinY;
            endY = rand.nextInt(endMaxY - endMinY + 1) + endMinY;

            //m = (y-y1) / (x-x1)
            m = (endY - originY) / ((800.0-width/2) - (-300.0-width/2));

            //Rock size
            switch (size) {
                case 2:
                    dx = rand.nextInt(2)+1;   //1-2 speed
                    break;
                case 1:
                    dx = rand.nextInt(4)+2;   //2-5 speed
                    break;
                default:
                    dx = rand.nextInt(5)+3;   //3-7 speed
                    break;
            }

            dy = m * dx;

            Rock rock;
            switch (size) {
                case 2:
                    rock = new Rock(largeRocks, gp, -300, originY);
                    break;
                case 1:
                    rock = new Rock(medRocks, gp, -300, originY);
                    break;
                default:
                    rock = new Rock(smallRocks, gp, -300, originY);
                    break;
            }

            hostile.add(rock);

            rock.setDX((int)dx);
            rock.setDY((int)dy);
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

            //Check bounds. Allow for some hostile to spawn off screen
            if (q.getX() < -320 || q.getX() > 850) {
                delHostile.add(q);
            }
            if(q.getY() < -320 || q.getY() > 770) {
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
