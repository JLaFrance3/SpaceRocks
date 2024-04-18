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
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel{
    
    private static final int DELAY = 17;
    private final int WIDTH = 800;
    private final int HEIGHT = 450;

    private boolean running = false;
    private ControlPanel cPanel;
    private Timer timer;
    private InputHolder input;

    private BufferedImage background;
    private SpriteSheet ships;
    private SpriteSheet lasers;
    private Avatar player;

    private ObjectManager manager;

    public GamePanel(InputHolder input, ControlPanel panel) {
        //Initialize
        this.input = input;
        running = false;
        this.cPanel = panel;
        timer = new Timer(DELAY, new ClockListener());
        manager = new ObjectManager(this, cPanel);

        //Panel settings
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void init() {
        BufferedImageLoader loader = new BufferedImageLoader();
        BufferedImage shipSS;
        BufferedImage projectileSS;

        manager.init();

        try {
            background = loader.load("res/BlueBackground1.png");
            shipSS = loader.load("res/ShipSheet.png");
            projectileSS = loader.load("res/ProjectileSheet.png");

            ships = new SpriteSheet(shipSS, 59, 47);
            player = new Avatar(ships.getSprite(1, 1), this, input);

            lasers = new SpriteSheet(projectileSS, 42, 68, 0, 90);
            player.setProjectileSprite(lasers.getSprite(1, 1));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if(running) {
            return;
        }
        
        running = true;
        timer.start();
    }

    public void tick() {
        player.tick();
        manager.tick();
        cPanel.tick();

        repaint();

        //Delete
        // manager.debug();
    }

    public void end() {
        if(!running) {
            return;
        }

        running = false;
        timer.stop();
    }

    public void pause() {
        timer.stop();
    }

    public void unpause() {
        
    }

    public ObjectManager getObjectManager() {
        return manager;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
       
        Graphics2D brush = (Graphics2D)g;

        if(background != null) {
            g.drawImage(background, 0, 0, null);
        }
        if (player != null) {
            player.paint(brush);
        }
        if(manager != null) {
            manager.paint(brush);
        }
    }

    private class ClockListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if(running) {
                tick();
            }
        }
    }
}
