/* 
 * Jean LaFrance
 * Rock
 * Collision object player must avoid
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Rock extends Mover {

    private Animation animation;    //Sprite animation
    private int type;
    
    public Rock(BufferedImage[] rocks, GamePanel gp, int type) {
        super(rocks[0], gp, 100, 100);

        this.animation = new Animation(rocks, 5, 16, 0, randomDirection());
        this.type = type;
    }

    public Rock(BufferedImage[] rocks, GamePanel gp, int x, int y, int type) {
        super(rocks[0], gp, x, y);

        //Randomize rock animation by generating random start index
        int startIndex = (int)(Math.random() * rocks.length / 16) * 16;

        //Randomize animation speed 2-5
        int animSpeed = (int)(Math.random() * 4) + 2;

        //Create new animation
        animation = new Animation(rocks, animSpeed, 16, startIndex, randomDirection());
        this.type = type;

        //Health/damage based on type
        switch (type) {
            case 0:
                setDamage(30);
                setHealth(30);
                break;
            case 1:
                setDamage(100);
                setHealth(100);
                break;
            case 2:
                setDamage(400);
                setHealth(400);

                //Needs a smaller mask
                createMask(120, 120);

                break;
        }
        
    }

    //Will determine animation rotation
    public Boolean randomDirection() {
        Boolean reverse;
        if (Math.random() < 0.5) {
            reverse = true;
        }
        else {
            reverse = false;
        }

        return reverse;
    }

    //Game tick for animations
    @Override
    public void tick() {
        super.tick();
        animation.tick();
        setSprite(animation.getSprite());
    }

    @Override
    public void paint(Graphics2D brush) {
        super.paint(brush, getRotation());
    }

    public int getType() {
        return type;
    }
}
