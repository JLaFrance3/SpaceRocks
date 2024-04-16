/* 
 * Jean LaFrance
 * ProjectileList
 * List of all current projectiles
 */

import java.awt.Graphics2D;
import java.util.ArrayList;

public class ProjectileList {
    
    private ArrayList<Projectile> projectiles;
    private ArrayList<Projectile> out;

    public ProjectileList() {
        projectiles = new ArrayList<Projectile>();
        out = new ArrayList<Projectile>();
    }

    public void tick() {
        for(Projectile p : projectiles) {
            p.tick();

            //Check bounds
            if (p.getX() < -450 || p.getX() >= 30) {
                out.add(p);
            }
            if(p.getY() > 800 || p.getY() <= 30) {
                out.add(p);
            }
        }

        for(Projectile q : out) {
            projectiles.remove(q);
        }
    }

    public void add(Projectile p) {
        projectiles.add(p);
    }

    public void remove(Projectile p) {
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
