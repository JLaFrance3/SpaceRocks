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

        animation = new Animation(rocks, 5, 16, 0, randomDirection());
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
