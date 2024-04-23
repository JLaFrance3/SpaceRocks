/* 
 * Jean LaFrance
 * Menu
 * Top layer panel for containing multiple popup menus
 */

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JPanel;

public class Menu extends JPanel implements MouseListener{
    public static enum STATE {
        NONE,
        STATS,
        UPGRADE,
        UPGRADE2,
        PAUSE,
        HELP,
        MAIN,
        DIFFICULTY
    }

    private int menuIndex;
    private Rectangle[] menuBounds;
    private Rectangle[] buttonMask;
    private STATE state;
    private GameFrame game;
    private ControlPanel cPanel;
    private BufferedImage[] menuBackgrounds;

    private BufferedImage currentBackground;

    public Menu(GameFrame game, ControlPanel cp) {
        //Initialize
        this.menuIndex = -1;
        this.menuBounds = new Rectangle[7];
        this.buttonMask = new Rectangle[14];
        this.state = STATE.NONE;
        this.game = game;
        this.cPanel = cp;
        this.menuBackgrounds = new BufferedImage[7];
        this.currentBackground = null;

        //Menu screens bounds for changing size of panel
        menuBounds[0] = new Rectangle(200, 10, 394, 511);   //Pause menu
        menuBounds[1] = new Rectangle(265, 10, 269, 400);   //Ship status menu
        menuBounds[2] = new Rectangle(232, 10, 336, 500);   //Shop page 1
        menuBounds[3] = new Rectangle(232, 10, 336, 500);   //Shop page 2
        menuBounds[4] = new Rectangle(100, 10, 600, 498);   //Help panel
        menuBounds[5] = new Rectangle(0, 0, 800, 600);      //Main menu
        menuBounds[6] = new Rectangle(200, 10, 394, 511);   //Difficulty menu

        //Button masks for now because they are a bit easier than JButtons
        buttonMask[0] = new Rectangle(285, 110, 40, 40);    //Shop Page 1 selector 1
        buttonMask[1] = new Rectangle(285, 230, 40, 40);    //Shop Page 1 selector 2
        buttonMask[2] = new Rectangle(285, 350, 40, 40);    //Shop Page 1 selector 3
        buttonMask[3] = new Rectangle(285, 445, 40, 40);    //Shop Page 1 Forward
        buttonMask[4] = new Rectangle(285, 110, 40, 40);    //Shop Page 2 Selector 1
        buttonMask[5] = new Rectangle(285, 230, 40, 40);    //Shop Page 2 Selector 2
        buttonMask[6] = new Rectangle(11, 445, 40, 40);     //Shop Page 2 Back
        buttonMask[7] = new Rectangle(47, 40, 300, 100);    //Pause Menu Restart
        buttonMask[8] = new Rectangle(47, 200, 300, 100);   //Pause Menu MainMenu
        buttonMask[9] = new Rectangle(47, 360, 300, 100);   //Pause Menu Exit
        buttonMask[10] = new Rectangle(250, 400, 300, 100); //Main menu start button
        buttonMask[11] = new Rectangle(47, 40, 300, 100);   //Difficulty Menu Easy
        buttonMask[12] = new Rectangle(47, 200, 300, 100);  //Difficulty Menu Medium
        buttonMask[13] = new Rectangle(47, 360, 300, 100);  //Difficulty Menu Hard
        
        //Panel preferences
        this.setLayout(null);
        this.addMouseListener(this);
    }

    public void init() {
        BufferedImageLoader loader = new BufferedImageLoader(); 

        try {
            menuBackgrounds[0] = loader.load("res/Menu.png");
            menuBackgrounds[1] = loader.load("res/ShipStats.png");
            menuBackgrounds[2] = loader.load("res/Shop1.png");
            menuBackgrounds[3] = loader.load("res/shop2.png");
            menuBackgrounds[4] = loader.load("res/Help.png");
            menuBackgrounds[5] = loader.load("res/StartBackground.png");
            menuBackgrounds[6] = loader.load("res/Difficulty.png");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setState(STATE s) {
        this.state = s;

        switch (state) {
            case PAUSE:
                menuIndex = 0;
                break;
            case STATS:
                menuIndex = 1;
                break;
            case UPGRADE:
                menuIndex = 2;
                break;
            case UPGRADE2:
                menuIndex = 3;
                break;
            case HELP:
                menuIndex = 4;
                break;
            case MAIN:
                menuIndex = 5;
                break;
            case DIFFICULTY:
                menuIndex = 6;
                break;
            default:
                menuIndex = -1;
                this.setVisible(false);
                break;
        }

        if (menuIndex != -1) {
            this.setBounds(menuBounds[menuIndex]);
            currentBackground = menuBackgrounds[menuIndex];
            this.setVisible(true);
        }
    }

    public STATE getState() {
        return state;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(currentBackground, 0, 0, null);
    }

    @Override
    public void mousePressed(MouseEvent e) {

        //Check if mouse click is within specified menu buttons
        switch (state) {
            case MAIN:
                // Start button
                if (buttonMask[10].contains(e.getPoint())) {
                    setState(STATE.DIFFICULTY);
                }
                break;
            case DIFFICULTY:
                //Get difficulty setting
                if (buttonMask[11].contains(e.getPoint())) {
                    //Easy
                    cPanel.setDifficulty(0);
                    setState(STATE.NONE);
                    game.startGame();
                }
                else if (buttonMask[12].contains(e.getPoint())) {
                    //Medium
                    cPanel.setDifficulty(1);
                    setState(STATE.NONE);
                    game.startGame();
                }
                else if (buttonMask[13].contains(e.getPoint())) {
                    //Hard
                    cPanel.setDifficulty(2);
                    setState(STATE.NONE);
                    game.startGame();
                }
                break;
            case PAUSE:
                //Get pause menu option
                if (buttonMask[7].contains(e.getPoint())) {
                    //TODO: Restart game
                }
                else if (buttonMask[8].contains(e.getPoint())) {
                    //Main menu
                    setState(STATE.MAIN);
                }
                else if (buttonMask[9].contains(e.getPoint())) {
                    //Exit
                    System.exit(0);
                }
                break;
            case UPGRADE:
                //Ship upgrade menu page 1
                if (buttonMask[0].contains(e.getPoint())) {
                    //TODO: Upgrade fire rate
                }
                else if (buttonMask[1].contains(e.getPoint())) {
                    //TODO: Upgrade speed
                }
                else if (buttonMask[2].contains(e.getPoint())) {
                    //TODO: Upgrade shield
                }
                else if (buttonMask[3].contains(e.getPoint())) {
                    //Next page
                    setState(STATE.UPGRADE2);
                    repaint();
                }
                break;
            case UPGRADE2:
                //Ship upgrade menu page 2
                if (buttonMask[4].contains(e.getPoint())) {
                    //TODO: Upgrade health
                }
                else if (buttonMask[5].contains(e.getPoint())) {
                    //TODO: Upgrade damage
                }
                else if (buttonMask[6].contains(e.getPoint())) {
                    //Previous page
                    setState((STATE.UPGRADE));
                    repaint();
                }
                break;
        }
    }

    //Unimplemented
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
