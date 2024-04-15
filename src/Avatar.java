/* 
 * Jean LaFrance
 * Avatar
 * Player character
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Avatar extends Entity{

    private InputHolder input;

    //Player avatar constructor
    public Avatar(BufferedImage sprite, GamePanel gp, InputHolder input) {
        super(sprite, gp, -200, 725, -90);

        this.input = input;
    }

    public void tick() {
        //Coordinate plane is all wonky due to graphics getting rotated
        switch(input.getInput()) {
            case 'w':
                setDX(getSpeed());
                break;
            case 's':
                setDX(-getSpeed());
                break;
        }

        super.tick();

        setDX(0);
    }

    //Shoot projectile
    public void shoot() {

    }

    //Call parent class overloaded method with rotation
    public void paint(Graphics2D brush) {
        super.paint(brush, getRotation());
    }
}
