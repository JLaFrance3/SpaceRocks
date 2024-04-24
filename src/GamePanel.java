/* 
 * Jean LaFrance
 * GamePanel
 * Main panel of game used to paint player avatar and enemies
 */

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel{
    
    private static final int DELAY = 17;        //Timer delay for ticks

    private Timer timer;                        //Game timer
    private InputHolder input;                  //Holds user input to control player avatar
    private Menu menu;                          //Menu responsible for displaying correct screens

    private BufferedImage background;           //Current background image
    private SpriteSheet ships;                  //Image containg all ships
    private SpriteSheet lasers;                 //Image containing all projectiles
    private Avatar player;                      //Player avatar

    private ObjectManager manager;

    public GamePanel(InputHolder input) {
        //Initialize variables
        this.timer = new Timer(DELAY, new ClockListener());
        this.input = input;
        this.menu = null;
        this.background = null;
        this.ships = null;
        this.lasers = null;
        this.player = null;
        this.manager = new ObjectManager(this);
    }

    //Initialize
    public void init(Menu menu) {
        BufferedImageLoader loader = new BufferedImageLoader();
        BufferedImage shipSS;
        BufferedImage projectileSS;

        this.menu = menu;

        manager.init();

        try {
            background = loader.load("res/BlueBackground1.png");
            shipSS = loader.load("res/ShipSheet.png");
            projectileSS = loader.load("res/ProjectileSheet.png");

            ships = new SpriteSheet(shipSS, 59, 47);
            player = new Avatar(ships.getSprite(1, 1), this, input);
            manager.addFriendly(player);

            lasers = new SpriteSheet(projectileSS, 42, 68, 0, 90);
            player.setProjectileSprite(lasers.getSprite(1, 1));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Start game
    public void start() {
        timer.start();
    }

    //Game clock
    public void tick() {
        manager.tick();

        repaint();
    }

    //Game over screen
    public void gameover() {
        pause();

        menu.setState(Menu.STATE.GAMEOVER);
    }

    //Reset all game values to default
    public void reset() {
        player.setSprite(ships.getSprite(1, 1));
        input.clear();
        player.reset();
        manager.reset();
        manager.addFriendly(player);

        if (menu.getState() == Menu.STATE.MAIN) {
            pause();
        }
        else {
            menu.setState(Menu.STATE.NONE);
        }
        repaint();
    }

    //Set difficulty
    public void setDifficulty(int difficulty) {
        manager.setDifficulty(difficulty);
    }

    public ObjectManager getObjectManager() {
        return manager;
    }

    public void pause() {
        timer.stop();
    }

    public void unpause() {
        timer.start();
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
            tick();
        }
    }
}
