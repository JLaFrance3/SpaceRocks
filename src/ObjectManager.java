/* 
 * Jean LaFrance
 * ProjectileList
 * List of all current projectiles
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ObjectManager {
    private ArrayList<Mover> friendly;              //Friendly movers(playeravatar, projectiles)
    private ArrayList<Mover> hostile;               //Hostile movers(rocks, enemies, projectiles)
    private ArrayList<Mover> delHostile;
    private ArrayList<Mover> delFriendly;
    private GamePanel gp;
    private Random rand;

    private SpriteSheet smallRockSS;
    private SpriteSheet medRockSS;
    private SpriteSheet largeRockSS;

    private BufferedImage[] smallRocks;
    private BufferedImage[] medRocks;
    private BufferedImage[] largeRocks;

    private int tickCounter, spawnRate, difficulty;
    private int[] rockValue;                        //Used in wave spawn and scoring
    private double[] rockWeight;                    //probabalistic weight of rock spawn
    private int waveMaxValue, waveValue;

    public ObjectManager(GamePanel gp) {
        this.friendly = new ArrayList<Mover>();
        this.hostile = new ArrayList<Mover>();
        this.delHostile = new ArrayList<Mover>();
        this.delFriendly = new ArrayList<Mover>();
        this.gp = gp;
        this.rand = new Random();
        this.tickCounter = 0;
        this.spawnRate = 0;
        this.difficulty = 0;
        this.rockValue = new int[]{1, 2, 5};
        this.rockWeight = new double[3];
        this.waveMaxValue = 0;
        this.waveValue = 0;
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

        if (waveValue < waveMaxValue) {
            tickCounter++;

            if(tickCounter >= spawnRate) {
                int rockIndex = 0;

                for(double r = Math.random(); rockIndex < rockWeight.length; rockIndex++) {
                    r -= rockWeight[rockIndex];
                    if (r <= 0) {
                        break;
                    }
                }
                
                spawnRock(1, rockIndex);

                tickCounter = 0;
                waveValue += rockValue[rockIndex];
                System.out.println(waveValue);
            }
        }

        //Check out of bounds
        removeOOB();
        
        //Friendly object collision detection
        for(Mover m : friendly) {
            ArrayList<Mover> collisions = new ArrayList<Mover>();
            collisions.addAll(checkCollision(m));

            if (!collisions.isEmpty()) {
                delFriendly.add(m);
                for(Mover h : collisions) {
                    delHostile.add(h);
                }
                //TODO: explosion
                if (m instanceof Avatar) {
                    //TODO: Game over
                    System.out.println("GameOver!");
                }
            }
        }
    }

    //Spawn rock randomly within spawn range: (-100, 0) -> (-100, 500)
    public void spawnRock(int quantity, int size) {
        int originY, endY, originMaxY, originMinY, endMaxY, endMinY;
        int height, width;
        double m, dx, dy;

        //Rock size used in origin and end point randomization
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

        //Maximum origin and end Y values
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

            //Randomize speed based on size and create rock
            Rock rock;
            switch (size) {
                case 2:
                    dx = rand.nextInt(2)+1;   //1-2 speed
                    dy = m * dx;
                    rock = new Rock(largeRocks, gp, -300, originY);
                    break;
                case 1:
                    dx = rand.nextInt(4)+2;   //2-5 speed
                    dy = m * dx;
                    rock = new Rock(medRocks, gp, -300, originY);
                    break;
                default:
                    dx = rand.nextInt(5)+3;   //3-7 speed
                    dy = m * dx;
                    rock = new Rock(smallRocks, gp, -300, originY);
                    break;
            }

            hostile.add(rock);

            rock.setDX((int)dx);
            rock.setDY((int)dy);
        }
    }

    private ArrayList<Mover> checkCollision(Mover mover) {
        ArrayList<Mover> collision = new ArrayList<Mover>();

        for(Mover m : hostile) {
            if (mover.getMask().intersects(m.getMask())) {
                collision.add(m);
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

    //Increase difficulty, start next wave
    public void nextWave(double[] weightIncrease) {
        for(int i = 0; i < rockWeight.length; i++) {
            rockWeight[i] += weightIncrease[i];
        }

        waveValue = 0;
    }
    
    //Increase difficulty, start next wave
    public void nextWave(double[] weightIncrease, int waveIncrease) {
        for(int i = 0; i < rockWeight.length; i++) {
            rockWeight[i] += weightIncrease[i];
        }

        waveMaxValue += waveIncrease;
        waveValue = 0;
    }

    //Set difficulty
    public void setDifficulty(int difficulty) {
        double[] weight = new double[3];
        this.difficulty = difficulty;

        //Set probabalistic weights for rocks spawns based on difficulty
        switch (difficulty) {
            case 2:
                //Hard
                weight[0] = 0.88;
                weight[1] = 0.09;
                weight[2] = 0.03;
                spawnRate = 5;
                waveMaxValue = 260;
                break;
            case 1:
                //Medium
                weight[0] = 0.92;
                weight[1] = 0.07;
                weight[2] = 0.01;
                spawnRate = 10;
                waveMaxValue = 160;
                break;
            default:
                //Easy
                weight[0] = 0.95;
                weight[1] = 0.05;
                weight[2] = 0.00;
                spawnRate = 15;
                waveMaxValue = 60;
                break;
        }

        rockWeight[0] = weight[0];
        rockWeight[1] = weight[1];
        rockWeight[2] = weight[2];
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
