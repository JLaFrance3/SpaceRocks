/* 
 * Jean LaFrance
 * Menu
 * Top layer panel for containing multiple popup menus
 */

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Menu extends JPanel implements MouseListener{

    //Menu states
    public static enum STATE {
        NONE,
        STATS,
        UPGRADE,
        UPGRADE2,
        PAUSE,
        HELP,
        MAIN,
        DIFFICULTY,
        GAMEOVER
    }

    private int menuIndex;                      //Used to determine current menu elements
    private Rectangle[] menuBounds;             //Menu sizes
    private Rectangle[] buttonMask;             //Masks to determine user input. No JButtons.
    private STATE state;                        //Current menu state
    private GamePanel gPanel;                   //Main game
    private ControlPanel cPanel;                //Main control panel
    private BufferedImage[] menuBackgrounds;    //Individual popup menu backgrounds
    private BufferedImage currentBackground;    //Current menu background
    private JLabel scoreLabel;                  //Displays score at end of game
    private ButtonListener buttonListener;      //Upgrade button listener
    private JButton[] upgradeButtons;           //Buttons for upgrading ship stats

    public Menu(GamePanel gPanel, ControlPanel cp) {
        //Initialize
        this.menuIndex = -1;
        this.menuBounds = new Rectangle[8];
        this.buttonMask = new Rectangle[10];
        this.state = STATE.NONE;
        this.gPanel = gPanel;
        this.cPanel = cp;
        this.menuBackgrounds = new BufferedImage[8];
        this.currentBackground = null;
        this.scoreLabel = new JLabel("SCORE: 0", SwingConstants.CENTER);
        this.buttonListener = new ButtonListener();
        this.upgradeButtons = new JButton[5];

        //Score label settings
        scoreLabel.setBounds(100, 310, 194, 20);
        scoreLabel.setBackground(new Color(0, 0, 0, 0));
        scoreLabel.setFont(new Font("Symbol-Bold", Font.BOLD, 24));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setVisible(false);
        this.add(scoreLabel);

        //Menu screens bounds for changing size of panel
        menuBounds[0] = new Rectangle(200, 10, 394, 511);   //Pause menu
        menuBounds[1] = new Rectangle(265, 10, 269, 400);   //Ship status menu
        menuBounds[2] = new Rectangle(232, 10, 336, 500);   //Shop page 1
        menuBounds[3] = new Rectangle(232, 10, 336, 500);   //Shop page 2
        menuBounds[4] = new Rectangle(100, 10, 600, 498);   //Help panel
        menuBounds[5] = new Rectangle(0, 0, 800, 600);      //Main menu
        menuBounds[6] = new Rectangle(200, 10, 394, 511);   //Difficulty menu
        menuBounds[7] = new Rectangle(200, 10, 394, 511);   //Gameover panel

        //Button masks for all these are easier than JButtons
        buttonMask[0] = new Rectangle(285, 445, 40, 40);    //Shop Page 1 Forward
        buttonMask[1] = new Rectangle(11, 445, 40, 40);     //Shop Page 2 Back
        buttonMask[2] = new Rectangle(47, 40, 300, 100);    //Pause Menu restart
        buttonMask[3] = new Rectangle(47, 200, 300, 100);   //Pause Menu MainMenu
        buttonMask[4] = new Rectangle(47, 360, 300, 100);   //Pause Menu Exit
        buttonMask[5] = new Rectangle(250, 400, 300, 100); //Main menu start button
        buttonMask[6] = new Rectangle(47, 40, 300, 100);   //Difficulty Menu Easy
        buttonMask[7] = new Rectangle(47, 200, 300, 100);  //Difficulty Menu Medium
        buttonMask[8] = new Rectangle(47, 360, 300, 100);  //Difficulty Menu Hard
        buttonMask[9] = new Rectangle(47, 350, 300, 100);  //Gameover Main Menu

        //JButtons
        for(int i = 0; i < upgradeButtons.length; i++) {
            upgradeButtons[i] = new JButton();
            upgradeButtons[i].addActionListener(buttonListener);
            upgradeButtons[i].setVisible(false);
            upgradeButtons[i].setFocusable(false);
            upgradeButtons[i].setBackground(new Color(0, 0, 0, 0));
            upgradeButtons[i].setOpaque(false);
            upgradeButtons[i].setBorderPainted(false);
            this.add(upgradeButtons[i]);
        }
        upgradeButtons[0].setBounds(285, 110, 30, 30);
        upgradeButtons[1].setBounds(285, 230, 30, 30);
        upgradeButtons[2].setBounds(285, 350, 30, 30);
        upgradeButtons[3].setBounds(285, 110, 30, 30);
        upgradeButtons[4].setBounds(285, 230, 30, 30);
        
        //Panel preferences
        this.setLayout(null);
        this.addMouseListener(this);
    }

    //Initialize
    public void init() {
        BufferedImageLoader loader = new BufferedImageLoader(); 

        try {
            menuBackgrounds[0] = loader.load("res/Menu.png");
            menuBackgrounds[1] = loader.load("res/ShipStats.png");
            menuBackgrounds[2] = loader.load("res/Shop1.png");
            menuBackgrounds[3] = loader.load("res/Shop2.png");
            menuBackgrounds[4] = loader.load("res/Help.png");
            menuBackgrounds[5] = loader.load("res/StartBackground.png");
            menuBackgrounds[6] = loader.load("res/Difficulty.png");
            menuBackgrounds[7] = loader.load("res/GameOver.png");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    //Changes current menu state to display a menu popup
    public void setState(STATE s) {
        this.state = s;

        switch (state) {
            case PAUSE -> menuIndex = 0;
            case STATS -> menuIndex = 1;
            case UPGRADE -> menuIndex = 2;
            case UPGRADE2 -> menuIndex = 3;
            case HELP -> menuIndex = 4;
            case MAIN -> menuIndex = 5;
            case DIFFICULTY -> menuIndex = 6;
            case GAMEOVER -> menuIndex = 7;
            default -> menuIndex = -1;
        }

        //Display menu based on index
        if (menuIndex == -1) {
            this.setVisible(false);
        }
        else {
            this.setBounds(menuBounds[menuIndex]);
            currentBackground = menuBackgrounds[menuIndex];
            this.setVisible(true);

            //Button visibility
            if (menuIndex == 2) {
                upgradeButtons[3].setVisible(false);
                upgradeButtons[4].setVisible(false);
                for(int i = 0; i < 3; i++) upgradeButtons[i].setVisible(true);
            }
            else if (menuIndex == 3) {
                upgradeButtons[3].setVisible(true);
                upgradeButtons[4].setVisible(true);
                for(int i = 0; i < 3; i++) upgradeButtons[i].setVisible(false);
            }
            else {
                for(int i = 0; i < upgradeButtons.length; i++) upgradeButtons[i].setVisible(false);
            }

            //Label visibility
            if (menuIndex == 7) {
                scoreLabel.setVisible(true);
            }
            else {
                scoreLabel.setVisible(false);
            }
        }
    }

    //Get current menu state
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

        //Check if mouse click is within specified menu buttons based on menu state
        switch (state) {
            case MAIN:
                // Start button
                if (buttonMask[5].contains(e.getPoint())) {
                    setState(STATE.DIFFICULTY);
                }
                break;
            case DIFFICULTY:
                //Get difficulty setting
                if (buttonMask[6].contains(e.getPoint())) {
                    //Easy
                    cPanel.setDifficulty(0);
                    setState(STATE.NONE);
                    startGame();
                }
                else if (buttonMask[7].contains(e.getPoint())) {
                    //Medium
                    cPanel.setDifficulty(1);
                    setState(STATE.NONE);
                    startGame();
                }
                else if (buttonMask[8].contains(e.getPoint())) {
                    //Hard
                    cPanel.setDifficulty(2);
                    setState(STATE.NONE);
                    startGame();
                }
                break;
            case GAMEOVER:
                //Main menu
                if (buttonMask[9].contains(e.getPoint())) {
                    cPanel.reset();
                    gPanel.reset();
                    setState(STATE.MAIN);
                }
                break;
            case PAUSE:
                //Restart
                if (buttonMask[2].contains(e.getPoint())) {
                    cPanel.reset();
                    gPanel.reset();
                    startGame();
                }
                else if (buttonMask[3].contains(e.getPoint())) {
                    //Main menu
                    cPanel.reset();
                    gPanel.reset();
                    setState(STATE.MAIN);
                }
                else if (buttonMask[4].contains(e.getPoint())) {
                    //Exit
                    System.exit(0);
                }
                break;
            case UPGRADE:
                //Ship upgrade menu page 1
                if (buttonMask[0].contains(e.getPoint())) {
                    //Next page
                    setState(STATE.UPGRADE2);
                }
                break;
            case UPGRADE2:
                //Ship upgrade menu page 2
                if (buttonMask[1].contains(e.getPoint())) {
                    //Previous page
                    setState((STATE.UPGRADE));
                }
                break;
        }
        repaint();
    }

    public void setButtonIcon(ImageIcon icon) {
        for(int i = 0; i < upgradeButtons.length; i++) {
            upgradeButtons[i].setIcon(icon);
        }
    }

    public void setScore(int score) {
        scoreLabel.setText("SCORE: " + score);
    }

    public void startGame() {
        gPanel.start();
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

    private class ButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == upgradeButtons[0]) {
                gPanel.upgradeFireRate();
            }
            else if (e.getSource() == upgradeButtons[1]) {
                gPanel.upgradeSpeed();
            }
            else if (e.getSource() == upgradeButtons[2]) {
                gPanel.upgradeShield();
            }
            else if (e.getSource() == upgradeButtons[3]) {
                gPanel.upgradeHealth();
            }
            else if (e.getSource() == upgradeButtons[4]) {
                gPanel.upgradeDamage();
            }
        }
    }
}
