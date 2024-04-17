/* 
 * Jean LaFrance
 * Rock
 * Collision object player must avoid
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Rock extends Mover {
    
    public Rock(BufferedImage sprite, GamePanel gp) {
        super(sprite, gp);
    }

    public Rock(BufferedImage sprite, GamePanel gp, int x, int y, int dx, int dy) {
        super(sprite, gp, x, y, dx, dy);
    }

    public void paint(Graphics2D brush) {
        super.paint(brush, getRotation());
    }

}
