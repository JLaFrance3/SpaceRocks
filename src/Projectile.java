/* 
 * Jean LaFrance
 * Projectile
 * Moves across screen to collide with other entities
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Projectile extends Mover{

    public Projectile(BufferedImage sprite, GamePanel gp) {
        super(sprite, gp);

        setDX(-getSpeed());
    }

    public Projectile(BufferedImage sprite, GamePanel gp, Ship ent, Boolean isBeam) {
        super(sprite, gp, ent.getX() - 18, 
            isBeam ? (ent.getY() - 10) : (ent.getY() - 20 + (int)(Math.random() * 17) - 8));

        setRotation(ent.getRotation());
        setSpeed(20);

        setDX(-getSpeed());
    }

    public void paint(Graphics2D brush) {
        super.paint(brush, getRotation());
    }
}
