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

    public Projectile(BufferedImage sprite, GamePanel gp, Ship ent) {
        super(sprite, gp, ent.getX(), ent.getY() - 10);

        setRotation(ent.getRotation());
        setSpeed(20);

        setDX(-getSpeed());
    }

    public void paint(Graphics2D brush) {
        super.paint(brush, getRotation());
    }
}
