/* 
 * Jean LaFrance
 * ObjectManager
 * Handles waves, spawns, collisions, and out of bounds
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ObjectManager {
    private static final int waveDelay = 350;       //Tick delay between spawn waves

    private ArrayList<Projectile> friendly;         //Friendly projectile
    private ArrayList<Rock> hostile;                //Hostile rocks
    private ArrayList<Rock> delHostile;             //Hostiles to be deleted from arraylist
    private ArrayList<Projectile> delFriendly;      //Friendlies to be deleted from arraylist

    //Tracks collisions to prevent repeated collisions on same objects
    private HashMap<Mover, ArrayList<Rock>> collisions;   

    private Avatar player;                //Player avatar
    private GamePanel gp;                 //Game Panel
    private Random rand;                  //Random

    private SpriteSheet smallRockSS;      //Spritesheet for small rocks
    private SpriteSheet medRockSS;        //Spritesheet for medium rocks
    private SpriteSheet largeRockSS;      //Spritesheet for large rocks
    private BufferedImage[] smallRocks;   //Image array passed to rock object for animation
    private BufferedImage[] medRocks;     //Image array passed to rock object for animation
    private BufferedImage[] largeRocks;   //Image array passed to rock object for animation

    private int tickCounter, spawnRate;   //Used to calculate rate of spawn/waves
    private int[] rockValue;              //Used in wave spawn and scoring
    private double[] rockWeight;          //Probabilistic weight of rock spawn
    private int waveMaxValue, waveValue;  //Used to calculate length of enemy spawn wave
    private double[] weightIncrements;    //Spawn wave difficulty increase at linear rate based on game difficulty
    private int waveValueIncrement;       //Spawn wave difficulty increase at linear rate based on game difficulty
    private int difficulty;               //Difficulty level
    private int score;                    //Score

    public ObjectManager(GamePanel gp) {
        this.friendly = new ArrayList<Projectile>();
        this.hostile = new ArrayList<Rock>();
        this.delHostile = new ArrayList<Rock>();
        this.delFriendly = new ArrayList<Projectile>();
        this.collisions = new HashMap<Mover, ArrayList<Rock>>();

        this.player = null;
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
        this.score = 0;
    }

    //Load images
    public void init(Avatar player) {
        BufferedImageLoader loader = new BufferedImageLoader();
        BufferedImage small;
        BufferedImage medium;
        BufferedImage large;

        //Get player and map it to collision hashmap
        this.player = player;
        this.collisions.put(player, new ArrayList<Rock>());

        //Load SpriteSheets to be passed to rocks
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

    //Game tick
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

        //Player tick
        player.tick();

        //Tick all and check out of bounds
        checkOOB();
        
        //Check for any collisions and calculate damage
        checkCollision();

        //Add to score based on rock size value
        for (Rock r : delHostile) {
            score += rockValue[r.getType()];
        }

        //Unmap projectiles to be deleted
        for(Projectile p : delFriendly) {
            collisions.remove(p);
        }

        //Unmap player mapped rocks to be deleted
        for(Rock r : delHostile) {
            if (collisions.get(player).contains(r)) {
                collisions.get(player).remove(r);
            }
        }

        //Remove OOB and collision objects from lists
        friendly.removeAll(delFriendly);
        hostile.removeAll(delHostile);

        //Clear lists
        delFriendly.clear();
        delHostile.clear();
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

    //Set values based on passed difficulty
    public void setDifficulty(int difficulty) {
        double[] weight = new double[3];
        this.difficulty = difficulty;

        //Set values based on difficulty
        switch (difficulty) {
            case 2:
                //Hard
                weight[2] = 0.02;
                weight[1] = 0.08;
                weight[0] = 1.0 - weight[1] - weight[2];
                spawnRate = 5;
                waveMaxValue = 220;
                waveValueIncrement = 50;
                weightIncrements[2] = 0.002;
                weightIncrements[1] = 0.004;
                weightIncrements[0] = -weightIncrements[1] - weightIncrements[2];
                break;
            case 1:
                //Medium
                weight[2] = 0.01;
                weight[1] = 0.07;
                weight[0] = 1.0 - weight[1] - weight[2];
                spawnRate = 10;
                waveMaxValue = 160;
                waveValueIncrement = 40;
                weightIncrements[2] = 0.0015;
                weightIncrements[1] = 0.003;
                weightIncrements[0] = -weightIncrements[1] - weightIncrements[2];
                break;
            default:
                //Easy
                weight[2] = 0.00;
                weight[1] = 0.05;
                weight[0] = 1.0 - weight[1] - weight[2];
                spawnRate = 15;
                waveMaxValue = 60;
                waveValueIncrement = 30;
                weightIncrements[2] = 0.001;
                weightIncrements[1] = 0.002;
                weightIncrements[0] = -weightIncrements[1] - weightIncrements[2];
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
                    rock = new Rock(largeRocks, gp, -300, originY, size);
                    break;
                case 1:
                    //Medium
                    dx = rand.nextInt(4)+2;   //2-5 speed
                    dy = m * dx;
                    rock = new Rock(medRocks, gp, -300, originY, size);
                    break;
                default:
                    //Small
                    dx = rand.nextInt(5)+3;   //3-7 speed
                    dy = m * dx;
                    rock = new Rock(smallRocks, gp, -300, originY, size);
                    break;
            }

            hostile.add(rock);

            rock.setDX((int)dx);
            rock.setDY((int)dy);
        }
    }

    //Check collsion between friendly and hostile objects
    private void checkCollision() {

        //Friendly object collision detection
        for(Rock h : hostile) {

            for(Projectile f : friendly) {
                if (f.getMask().intersects(h.getMask())) {
                    //Add arraylist to map if it does not already contain one
                    collisions.computeIfAbsent(f, v -> new ArrayList<Rock>());
                    
                    //Check if projectile previously mapped rock
                    if (!collisions.get(f).contains(h)) {

                        //Map collision to prevent repeated collision on same objects
                        collisions.get(f).add(h);

                        calculateDamage(f, h);
                        //TODO: Explosion
                    }
                }
            }

            if (player.getMask().intersects(h.getMask())) {

                //Check if player previously mapped rock
                if (!collisions.get(player).contains(h)) {

                    //Map collision to prevent repeated collision on same objects
                    collisions.get(player).add(h);

                    calculateDamage(h);
                }
            }
        }
    }

    //Calculate damage between projectiles and rocks
    private void calculateDamage(Projectile f, Rock h) {
        f.setHealth(f.getHealth() - h.getDamage());
        h.setHealth(h.getHealth() - f.getDamage());

        if (f.getHealth() <= 0) {
            delFriendly.add(f);
        }
        if (h.getHealth() <= 0) {
            delHostile.add(h);
        }
    }

    //Calculate damage to player
    private void calculateDamage(Rock h) {

        //Check if no shield left
        if (player.getShield() <= 0) {

            //Adjust health value
            player.setHealth(player.getHealth() - h.getDamage());

        }
        else {

            //Shield animation
            player.shieldAnim(true);

            //Adjust shield value
            player.setShield(player.getShield() - h.getDamage());

            //If shield out, damage carries over
            if (player.getShield() < 0) {
                //Adjust health value
                player.setHealth(player.getHealth() + player.getShield());
                player.setShield(0);
            }

        }

        //Rock damage
        h.setHealth(h.getHealth() - player.getDamage());

        //Rock death
        if (h.getHealth() <= 0) {
            //TODO: Explosion?
            delHostile.add(h);
        }

        //Player death
        if (player.getHealth() <= 0) {
            //TODO: Bigger explosion?
            gp.gameover();
        }
    }

    //Check for movers out of bounds. Exclude spawn range.
    private void checkOOB() {
        //Friendly
        for(int i = friendly.size() - 1; i >= 0; i--) {
            Projectile p = friendly.get(i);
            p.tick();

            //Check bounds
            if (p.getX() < -20 || p.getX() > 820) {
                delFriendly.add(p);
            }
            if(p.getY() < -40 || p.getY() > 540) {
                delFriendly.add(p);
            }
        }

        //Hostile
        for(int j = hostile.size() - 1; j >= 0; j--) {
            Rock q = hostile.get(j);
            q.tick();

            //Check bounds. Allow for some hostile to spawn off screen
            if (q.getX() < -320 || q.getX() > 850) {
                delHostile.add(q);
            }
            if(q.getY() < -320 || q.getY() > 770) {
                delHostile.add(q);
            }
        }
    }

    //Reset to initial values
    public void reset() {
        friendly.clear();
        hostile.clear();
        delHostile.clear();
        delFriendly.clear();
        collisions.clear();
        collisions.put(player, new ArrayList<Rock>());
        tickCounter = 0;
        waveValue = 0;
        score = 0;
        setDifficulty(difficulty);
    }

    //Get current score
    public int getScore() {
        return score;
    }

    //Add or remove Movers from Arraylists
    public void addHostile(Rock r) {
        hostile.add(r);
    }
    public void removeHostile(Rock r) {
        hostile.remove(r);
    }
    public void addFriendly(Projectile p) {
        friendly.add(p);
    }
    public void removeFriendly(Projectile p) {
        friendly.remove(p);
    }

    //Paint
    public void paint(Graphics2D brush) {
        player.paint(brush);
        for(Rock m : hostile) {
            m.paint(brush);
        }
        for(Projectile p : friendly) {
            p.paint(brush);
        }
    }
}
