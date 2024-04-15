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
    
    private static final int DELAY = 100;
    private final int WIDTH = 800;
    private final int HEIGHT = 450;

    private boolean running = false;
    private Timer timer;
    private Random random;
    private InputHolder input;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private BufferedImage shipSS;
    private SpriteSheet ships;

    private BufferedImage playerAvatar;
    private Avatar player;

    public GamePanel(InputHolder input) {
        //Initialize
        this.input = input;
        running = false;
        timer = new Timer(DELAY, new ClockListener(this));
        random = new Random();

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

        SpriteSheet ss = new SpriteSheet(shipSS, 59, 47);
        playerAvatar = ss.getSprite(1, 1);
        player = new Avatar(playerAvatar, this);
    }

    public void start() {
        if(running) {
            return;
        }
        System.out.println("Started");
        running = true;
        timer.start();
        init();
        run();
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(75);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            switch(input.getInput()) {
                case 'w':
                    player.moveUp();
                    break;
                case 's':
                    player.moveDown();
                    break;
            }
            repaint();
        }
        
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

    public void tick() {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
       
        if (player != null) {
            Graphics2D brush = (Graphics2D)g.create();
            player.paint(brush);
            brush.dispose();
        }
    }

    private class ClockListener implements ActionListener {

        public JPanel panel;
        
        public ClockListener(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(running) {
                
            }
        }
    }
}
