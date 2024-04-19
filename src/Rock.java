/* 
 * Jean LaFrance
 * Rock
 * Collision object player must avoid
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Rock extends Mover {

    Animation animation;    //Sprite animation
    
    public Rock(BufferedImage[] rocks, GamePanel gp) {
        super(rocks[0], gp, 100, 100);

        animation = new Animation(rocks, 5, 16, 0, randomDirection());
    }

    public Rock(BufferedImage[] rocks, GamePanel gp, int x, int y) {
        super(rocks[0], gp, x, y);

        //Randomize rock type by generating random start index for animation
        int startIndex = (int)(Math.random() * rocks.length / 16) * 16;

        //Randomize animation speed 2-5
        int animSpeed = (int)(Math.random() * 4) + 2;

        //Create new animation
        animation = new Animation(rocks, animSpeed, 16, startIndex, randomDirection());
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

    @Override
    public void tick() {
        super.tick();
        animation.runAnimation();
        setSprite(animation.getSprite());
    }

    public void paint(Graphics2D brush) {
        super.paint(brush, getRotation());
    }

}
