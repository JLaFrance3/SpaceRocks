/* 
 * Jean LaFrance
 * ProjectileList
 * List of all current projectiles
 */

import java.awt.Graphics2D;
import java.util.ArrayList;

public class ObjectManager {
    
    private ArrayList<Mover> movers;
    private ArrayList<Mover> delMovers;

    public ObjectManager() {
        movers = new ArrayList<Mover>();
        delMovers = new ArrayList<Mover>();
    }

    public void tick() {
        for(Mover p : movers) {
            p.tick();

            //Check bounds
            if (p.getX() < -20 || p.getX() > 820) {
                delMovers.add(p);
            }
            if(p.getY() < -20 || p.getY() > 430) {
                delMovers.add(p);
            }
        }

        for(Mover q : delMovers) {
            movers.remove(q);
        }
    }

    public void addMover(Projectile p) {
        movers.add(p);
    }

    public void removeMover(Projectile p) {
        movers.remove(p);
    }

    public void paint(Graphics2D brush) {
        for(Mover p : movers) {
            p.paint(brush);
        }
    }

    //Delete
    public void debug() {
        for(Mover p : movers) {
            System.out.printf("(%d, %d)",p.getX(), p.getY());
        }
        if(movers.size() > 0)
        System.out.println();
    }

}
