/* 
 * Jean LaFrance
 * Avatar
 * Player character
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Avatar extends Entity{

    //Player avatar constructor
    public Avatar(BufferedImage sprite, GamePanel gp) {
        super(sprite, gp, -200, 725, -90);
    }

    //Call parent class overloaded method with rotation
    public void paint(Graphics2D brush) {
        super.paint(brush, -90);
    }

    //Coordinate plane is all wonky due to graphics getting rotated
    public void moveUp() {
        setLocation(getX() + getSpeed(), getY());
    }

    public void moveDown() {
        setLocation(getX() - getSpeed(), getY());
    }

    public void shoot() {

    }
}
