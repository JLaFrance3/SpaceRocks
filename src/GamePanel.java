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
    private ControlPanel cPanel;
    private Timer timer;
    private Random random;
    private InputHolder input;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private BufferedImage background;
    private BufferedImage shipSS;
    private SpriteSheet ships;
    private BufferedImage projectileSS;
    private SpriteSheet lasers;
    private Avatar player;

    private ObjectManager objects;

    public GamePanel(InputHolder input, ControlPanel panel) {
        //Initialize
        this.input = input;
        running = false;
        this.cPanel = panel;
        timer = new Timer(DELAY, new ClockListener(this));
        random = new Random();
        objects = new ObjectManager();

        //Panel settings
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public void init() {
        BufferedImageLoader loader = new BufferedImageLoader();

        try {
            background = loader.load("res/BlueBackground1.png");
            shipSS = loader.load("res/ShipSheet.png");
            projectileSS = loader.load("res/ProjectileSheet.png");
        } catch(IOException e) {
            e.printStackTrace();
        }

        ships = new SpriteSheet(shipSS, 59, 47);
        player = new Avatar(ships.getSprite(1, 1), this, input);

        lasers = new SpriteSheet(projectileSS, 42, 68, 0, 90);
        player.setProjectileSprite(lasers.getSprite(1, 1));
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
        objects.tick();
        cPanel.tick();

        repaint();

        //Delete
        objects.debug();
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

    public ObjectManager getObjectManager() {
        return objects;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
       
        Graphics2D brush = (Graphics2D)g.create();

        if(background != null) {
            g.drawImage(background, 0, 0, null);
        }
        if (player != null) {
            player.paint(brush);
        }
        if(objects != null) {
            objects.paint(brush);
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
