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
        super(sprite, gp, 725, 200, -90);
    }

    //Stuff gets wonky when you rotate the panel around the image
    public void setLocation(int x, int y) {
        super.setLocation(-y, x);
    }

    //Call parent class overloaded method with rotation
    public void paint(Graphics2D brush) {
        super.paint(brush, -90);
    }
}
