/* 
 * Jean LaFrance
 * Avatar
 * Player character
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Avatar {
    
    private BufferedImage sprite;
    private GamePanel gp;
    private int x, y;

    public Avatar(BufferedImage sprite, GamePanel gp) {
        this.sprite = sprite;
        this.gp = gp;

        setLocation(700, 200);
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public void setLocation(int x, int y) {
        this.x = -y;
        this.y = x;
    }

    public void paint(Graphics2D brush) {
        brush.rotate(Math.toRadians(-90), sprite.getWidth()/2, sprite.getHeight()/2);

        System.out.println(brush.drawImage(sprite, x, y, gp));

        brush.rotate(Math.toRadians(90), sprite.getWidth()/2, sprite.getHeight()/2);
    }
}
