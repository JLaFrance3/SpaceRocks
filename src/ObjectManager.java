/* 
 * Jean LaFrance
 * ObjectManager
 * Handles waves, spawns, collisions, and out of bounds
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ObjectManager {
    private static final int waveDelay = 350;       //Tick delay between spawn waves

    private ArrayList<Mover> friendly;              //Friendly movers(playeravatar, projectiles)
    private ArrayList<Mover> hostile;               //Hostile movers(rocks, enemies, projectiles)
    private ArrayList<Mover> delHostile;            //Hostiles to be deleted from arraylist
    private ArrayList<Mover> delFriendly;           //Friendlies to be deleted from arraylist
    private GamePanel gp;                           //Game Panel
    private Random rand;                            //Random

    private SpriteSheet smallRockSS;                //Spritesheet for small rocks
    private SpriteSheet medRockSS;                  //Spritesheet for medium rocks
    private SpriteSheet largeRockSS;                //Spritesheet for large rocks

    private BufferedImage[] smallRocks;             //Image array passed to rock object for animation
    private BufferedImage[] medRocks;               //Image array passed to rock object for animation
    private BufferedImage[] largeRocks;             //Image array passed to rock object for animation

    private int tickCounter, spawnRate;             //Used to calculate rate of spawn/waves
    private int[] rockValue;                        //Used in wave spawn and scoring
    private double[] rockWeight;                    //Probabilistic weight of rock spawn
    private int waveMaxValue, waveValue;            //Used to calculate length of enemy spawn wave
    private double[] weightIncrements;              //Spawn wave difficulty increase at linear rate based on game difficulty
    private int waveValueIncrement;                 //Spawn wave difficulty increase at linear rate based on game difficulty
    private int difficulty;

    public ObjectManager(GamePanel gp) {
        this.friendly = new ArrayList<Mover>();
        this.hostile = new ArrayList<Mover>();
        this.delHostile = new ArrayList<Mover>();
        this.delFriendly = new ArrayList<Mover>();
        this.gp = gp;
        this.rand = new Random();
        this.tickCounter = 0;
        this.spawnRate = 0;
        this.rockValue = new int[]{1, 2, 5};
        this.rockWeight = new double[3];
        this.waveMaxValue = 0;
        this.waveValue = 0;
        this.weightIncrements = new double[3];
        this.waveValueIncrement = 0;
        this.difficulty = 0;
    }

    //Load images
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

        //Check wave completion
        if (waveValue < waveMaxValue) {

            //Limit spawn rate
            if(tickCounter >= spawnRate) {
                int rockIndex = 0;

                tickCounter = 0;

                //Random rock size determined by weighted values
                for(double r = Math.random(); rockIndex < rockWeight.length; rockIndex++) {
                    r -= rockWeight[rockIndex];
                    if (r <= 0) {
                        break;
                    }
                }
                
                //Make rock
                spawnRock(1, rockIndex);

                //Add rock value to current wave total value
                waveValue += rockValue[rockIndex];
            }
        }
        else {
            //Wave over, delay for next wave
            if (tickCounter > waveDelay) {
                nextWave();
                tickCounter = 0;
            }
        }

        //Check out of bounds
        removeOOB();
        
        //Friendly object collision detection
        for(Mover m : friendly) {
            //Arraylist of all collisions this tick
            ArrayList<Mover> collisions = new ArrayList<Mover>();
            collisions.addAll(checkCollision(m));

            if (!collisions.isEmpty()) {
                delFriendly.add(m);
                for(Mover h : collisions) {
                    delHostile.add(h);
                }
                //TODO: explosion
                if (m instanceof Avatar) {
                    gp.gameover();
                }
            }
        }
    }

    //Increase difficulty, start next wave
    public void nextWave() {
        //Increase rock weights
        for(int i = 0; i < rockWeight.length; i++) {
            rockWeight[i] += weightIncrements[i];
        }

        //Increase wave length
        waveMaxValue += waveValueIncrement;
        waveValue = 0;  //Reset wave counter
    }

    //Set difficulty
    public void setDifficulty(int difficulty) {
        double[] weight = new double[3];
        this.difficulty = difficulty;

        //Set values based on difficulty
        switch (difficulty) {
            case 2:
                //Hard
                weight[0] = 0.9;
                weight[1] = 0.08;
                weight[2] = 0.02;
                spawnRate = 5;
                waveMaxValue = 220;
                waveValueIncrement = 50;
                weightIncrements[0] = -0.006;
                weightIncrements[1] = 0.004;
                weightIncrements[2] = 0.002;
                break;
            case 1:
                //Medium
                weight[0] = 0.92;
                weight[1] = 0.07;
                weight[2] = 0.01;
                spawnRate = 10;
                waveMaxValue = 160;
                waveValueIncrement = 40;
                weightIncrements[0] = -0.0045;
                weightIncrements[1] = 0.003;
                weightIncrements[2] = 0.0015;
                break;
            default:
                //Easy
                weight[0] = 0.95;
                weight[1] = 0.05;
                weight[2] = 0.00;
                spawnRate = 15;
                waveMaxValue = 60;
                waveValueIncrement = 30;
                weightIncrements[0] = -0.003;
                weightIncrements[1] = 0.002;
                weightIncrements[2] = 0.001;
                break;
        }

        rockWeight[0] = weight[0];
        rockWeight[1] = weight[1];
        rockWeight[2] = weight[2];
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
                    //Large
                    dx = rand.nextInt(2)+1;   //1-2 speed
                    dy = m * dx;
                    rock = new Rock(largeRocks, gp, -300, originY);
                    break;
                case 1:
                    //Medium
                    dx = rand.nextInt(4)+2;   //2-5 speed
                    dy = m * dx;
                    rock = new Rock(medRocks, gp, -300, originY);
                    break;
                default:
                    //Small
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

    //Return array of entities in collision
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
        //Friendly
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

        //Hostile
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

    //Reset to initial values
    public void reset() {
        friendly.clear();
        hostile.clear();
        delHostile.clear();
        delFriendly.clear();
        tickCounter = 0;
        waveValue = 0;
        setDifficulty(difficulty);
    }

    //Add or remove Movers from Arraylists
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

    //Paint
    public void paint(Graphics2D brush) {
        for(Mover m : hostile) {
            m.paint(brush);
        }
        for(Mover m : friendly) {
            m.paint(brush);
        }
    }
}
