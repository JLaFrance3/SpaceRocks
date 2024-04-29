/* 
 * Jean LaFrance
 * GamePanel
 * Main panel of game used to paint player avatar and enemies
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel{
    
    private static final int DELAY = 17;        //Timer delay for ticks

    private Timer timer;                        //Game timer
    private InputHolder input;                  //Holds user input to control player avatar
    private Menu menu;                          //Menu responsible for displaying correct screens
    private ControlPanel cp;                    //Main control panel

    private BufferedImage background;           //Current background image
    private SpriteSheet ships;                  //Image containg all ships
    private SpriteSheet lasers;                 //Image containing all projectiles
    private BufferedImage shieldSprite;         //Image used for player shield animation
    private Avatar player;                      //Player avatar
    private ObjectManager manager;              //Game manager
    private SpriteSheet UI_SS;                  //Spritesheet of UI elements
    private JLabel crystalIcon, crystalLabel;   //Used to display players reward amount
    private int crystals;                       //Player reward crystal amount

    //Limit game speed variance
    private long lastTime;
    private final double NS_TICK = 1000000000 / 30.0; //30fps
    private double delta;
    private long nowTime;

    public GamePanel(InputHolder input) {
        //Initialize variables
        this.timer = new Timer(DELAY, new ClockListener());
        this.input = input;
        this.menu = null;
        this.cp = null;
        this.background = null;
        this.ships = null;
        this.lasers = null;
        this.shieldSprite = null;
        this.player = null;
        this.manager = new ObjectManager(this);
        this.UI_SS = null;
        this.crystalIcon = new JLabel();
        this.crystalLabel = new JLabel();
        this.crystals = 0;

        this.lastTime = System.nanoTime();
        this.delta = 0;
        this.nowTime = 0;

        //Label settings
        this.setLayout(null);
        crystalIcon.setBounds(15, 15, 22, 35);
        crystalIcon.setBackground(new Color(0, 0, 0, 0));
        this.add(crystalIcon);
        crystalLabel.setBounds(42, 22, 120, 16);
        crystalLabel.setBackground(new Color(0, 0, 0, 0));
        crystalLabel.setFont(new Font("Symbol-Bold", Font.BOLD, 14));
        crystalLabel.setForeground(Color.WHITE);
        this.add(crystalLabel);
    }

    //Initialize
    public void init(Menu menu, ControlPanel cp) {
        BufferedImageLoader loader = new BufferedImageLoader();
        BufferedImage shipSS;
        BufferedImage projectileSS;
        BufferedImage UI_Sheet;

        this.menu = menu;
        this.cp = cp;

        try {
            background = loader.load("res/BlueBackground1.png");
            shipSS = loader.load("res/ShipSheet.png");
            projectileSS = loader.load("res/ProjectileSheet.png");
            shieldSprite = loader.load("res/Shield.png");
            UI_Sheet = loader.load("res/UISheet.png");

            ships = new SpriteSheet(shipSS, 59, 47);
            lasers = new SpriteSheet(projectileSS, 42, 68, 0, 90);
            UI_SS = new SpriteSheet(UI_Sheet, 34, 54, 226, 60);

            player = new Avatar(ships, lasers, shieldSprite, this, input);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        manager.init(player);

        Image labelIcon = UI_SS.getSprite(1, 1).getScaledInstance(22, 35, Image.SCALE_SMOOTH);
        crystalIcon.setIcon(new ImageIcon((labelIcon)));
    }

    //Start game
    public void start() {
        timer.start();
    }

    //Game clock
    public void tick() {
        nowTime = System.nanoTime();
        delta += (nowTime - lastTime) / NS_TICK;
        lastTime = nowTime;

        if (delta >= 1) {
            manager.tick();
            cp.tick();

            delta--;
        }

        //Update reward label for number of crystals
        if (crystals > 0) {
            crystalLabel.setText("" + crystals);
        }

        repaint();
    }

    //Game over screen
    public void gameover() {
        pause();
        menu.setScore(getScore());

        menu.setState(Menu.STATE.GAMEOVER);
    }

    //Reset all game values to default
    public void reset() {
        input.clear();
        player.reset();
        manager.reset();

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

    //Ship upgrades selected in menu increase by static increment
    public void upgradeFireRate() {
        player.upgradeFireRate();
        player.checkShipUpgrade();
    }
    public void upgradeHealth() {
        player.upgradeHealth();
        player.checkShipUpgrade();
    }
    public void upgradeShield() {
        player.upgradeShield();
        player.checkShipUpgrade();
    }
    public void upgradeSpeed() {
        player.upgradeSpeed();
        player.checkShipUpgrade();
    }
    public void upgradeDamage() {
        player.upgradeDamage();
        player.checkShipUpgrade();
    }

    //Add crystals
    public void addCrystals(int c) {
        this.crystals += c;
    }

    //Get object manager
    public ObjectManager getObjectManager() {
        return manager;
    }

    //Get current score
    public int getScore() {
        return manager.getScore();
    }

    //Get current player health
    public double getPlayerHealth() {
        return player.getHealth();
    }

    //Get current player shield
    public double getPlayerShield() {
        return player.getShield();
    }

    //Get player maximum health
    public double getPlayerMaxHealth() {
        return player.getMaxHealth();
    }

    //Get player maximum shield
    public double getPlayerMaxShield() {
        return player.getMaxShield();
    }

    //Get player crystals
    public int getCrystals() {
        return crystals;
    }

    //Timer stop
    public void pause() {
        timer.stop();
    }
    //Timer start
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
