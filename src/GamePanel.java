/* 
 * Jean LaFrance
 * GamePanel
 * Main panel of game used to paint player avatar and enemies
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel{
    
    private static final int DELAY = 17;
    private final int WIDTH = 800;
    private final int HEIGHT = 450;

    private boolean running = false;
    private Timer timer;
    private Random random;
    private InputHolder input;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private BufferedImage shipSS;
    private BufferedImage projectileSS;
    private SpriteSheet ships;
    private SpriteSheet lasers;
    private BufferedImage playerAvatar;
    private BufferedImage beam;
    private Avatar player;

    private ProjectileList projectiles;

    public GamePanel(InputHolder input) {
        //Initialize
        this.input = input;
        running = false;
        timer = new Timer(DELAY, new ClockListener(this));
        random = new Random();
        projectiles = new ProjectileList();

        //Panel settings
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        //Delete later
        this.setBackground(java.awt.Color.black);
    }

    public void init() {
        BufferedImageLoader loader = new BufferedImageLoader();

        try {
            shipSS = loader.load("res/ShipSheet.png");
        } catch(IOException e) {
            e.printStackTrace();
        }

        ships = new SpriteSheet(shipSS, 59, 47);
        playerAvatar = ships.getSprite(1, 1);
        player = new Avatar(playerAvatar, this, input);

        try {
            projectileSS = loader.load("res/ProjectileSheet.png");
        } catch(IOException e) {
            e.printStackTrace();
        }

        lasers = new SpriteSheet(projectileSS, 42, 68, 90);
        beam = lasers.getSprite(1, 1);
        player.setProjectileSprite(beam);
    }

    public void start() {
        if(running) {
            return;
        }
        
        running = true;
        timer.start();
    }

    public void tick() {

        if(player != null) {
            player.tick();
        }
        if(projectiles != null) {
            projectiles.tick();
        }

        repaint();

        //Delete
        projectiles.debug();
    }

    public void end() {
        if(!running) {
            return;
        }

        running = false;
        timer.stop();
    }

    public void pause() {
        
    }

    public void unpause() {
        
    }

    public ProjectileList getProjectileList() {
        return projectiles;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
       
        Graphics2D brush = (Graphics2D)g.create();

        if (player != null) {
            player.paint(brush);
        }
        if(projectiles != null) {
            projectiles.paint(brush);
        }
        brush.dispose();
    }

    private class ClockListener implements ActionListener {

        public JPanel panel;
        
        public ClockListener(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(running) {
                tick();
            }
        }
    }
}
