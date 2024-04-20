/* 
 * Jean LaFrance
 * Menu
 * Top layer panel for containing multiple popup menus
 */

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Menu extends JPanel implements ItemListener{
    public static enum STATE {
        NONE,
        STATS,
        UPGRADE,
        MAIN,
        HELP
    }

    private int menuIndex;
    private Rectangle[] menuBounds;
    private STATE state;
    private ControlPanel cPanel;
    private BufferedImage[] menuBackgrounds;
    private ImageIcon[] menuIcons;
    private JButton[] menuButtons;

    private BufferedImage currentBackground;
    private SpriteSheet UI_SS;
    private BufferedImage UI_Sheet;

    public Menu(ControlPanel cp) {
        //Initialize
        this.menuIndex = -1;
        this.menuBounds = new Rectangle[4];
        this.state = STATE.NONE;
        this.cPanel = cp;
        this.menuBackgrounds = new BufferedImage[4];
        this.menuIcons = new ImageIcon[5];
        this.menuButtons = new JButton[5];
        this.currentBackground = null;

        menuBounds[0] = new Rectangle(265, 10, 269, 400);
        menuBounds[1] = new Rectangle(232, 10, 336, 500);
        menuBounds[2] = new Rectangle(215, 10, 370, 500);
        menuBounds[3] = new Rectangle(100, 10, 600, 498);
        
        //Panel preferences
        this.setLayout(null);

        //UI
        for(int i = 0; i < menuButtons.length; i++) {
            menuButtons[i] = new JButton();
            menuButtons[i].setSize(30, 30);
            menuButtons[i].addItemListener(this);
        }

    }

    public void init() {
        BufferedImageLoader loader = new BufferedImageLoader(); 

        try {
            menuBackgrounds[0] = loader.load("res/ShipStats.png");
            menuBackgrounds[1] = loader.load("res/ShipShop.png");
            menuBackgrounds[2] = loader.load("res/Menu.png");
            menuBackgrounds[3] = loader.load("res/Help.png");
            UI_Sheet = loader.load("res/UISheet.png");
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        //Panel icons for use in menu panels
        UI_SS = new SpriteSheet(UI_Sheet, 30, 30, 240, 0);
        for(int k = 0; k < menuIcons.length; k++) {
            menuIcons[k] = new ImageIcon((Image)UI_SS.getSprite(k+8, 1));
        }
    }

    public void setState(STATE s) {
        this.state = s;

        switch (state) {
            case NONE:
                menuIndex = -1;
                this.setVisible(false);
                break;
            case STATS:
                menuIndex = 0;
                this.setBounds(menuBounds[0]);
                this.setVisible(true);
                break;
            case UPGRADE:
                menuIndex = 1;
                this.setBounds(menuBounds[1]);
                this.setVisible(true);
                break;
            case MAIN:
                menuIndex = 2;
                this.setBounds(menuBounds[2]);
                this.setVisible(true);
                break;
            case HELP:
                menuIndex = 3;
                this.setBounds(menuBounds[3]);
                this.setVisible(true);
                break;
        }

        if (menuIndex != -1) {
            currentBackground = menuBackgrounds[menuIndex];
        }
        
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(currentBackground, 0, 0, null);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        //Left
        if (menuButtons[0].isSelected()) {
        }
        //Right
        if (menuButtons[1].isSelected()) {
        }
        //Close
        if (menuButtons[2].isSelected()) {
            //TODO: close menu
        }
        //Return
        if (menuButtons[3].isSelected()) {
        }
        //Confirm
        if (menuButtons[4].isSelected()) {
        }
    }
}
