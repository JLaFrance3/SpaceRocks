/* 
 * Jean LaFrance
 * ProjectileList
 * List of all current projectiles
 */

import java.awt.Graphics2D;
import java.util.ArrayList;

public class ObjectManager {
    
    private ArrayList<Projectile> projectiles;
    private ArrayList<Projectile> delProjectiles;
    private ArrayList<Rock> rocks;
    private ArrayList<Rock> delRocks;

    public ObjectManager() {
        projectiles = new ArrayList<Projectile>();
        delProjectiles = new ArrayList<Projectile>();
    }

    public void tick() {
        for(Projectile p : projectiles) {
            p.tick();

            //Check bounds
            if (p.getX() < -20 || p.getX() > 820) {
                delProjectiles.add(p);
            }
            if(p.getY() < -20 || p.getY() > 430) {
                delProjectiles.add(p);
            }
        }

        for(Rock r : rocks) {
            r.tick();

            //Check bounds
            if (r.getX() < -20 || r.getX() > 820) {
                delRocks.add(r);
            }
            if(r.getY() < -20 || r.getY() > 430) {
                delRocks.add(r);
            }
        }

        //Remove out of bounds objects
        for(Projectile q : delProjectiles) {
            projectiles.remove(q);
        }
        for(Rock n : delRocks) {
            rocks.remove(n);
        }
    }

    public void addProjectile(Projectile p) {
        projectiles.add(p);
    }

    public void removeProjectile(Projectile p) {
        projectiles.remove(p);
    }

    public void paint(Graphics2D brush) {
        for(Projectile p : projectiles) {
            p.paint(brush);
        }
    }

    //Delete
    public void debug() {
        for(Projectile p : projectiles) {
            System.out.printf("(%d, %d)",p.getX(), p.getY());
        }
        if(projectiles.size() > 0)
        System.out.println();
    }

}
