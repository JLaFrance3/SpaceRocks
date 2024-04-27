/* 
 * Jean LaFrance
 * ControlPanel
 * JPanel used to collect and pass user inputs to game.
 * Contains menu buttons and display player health and shield.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

public class ControlPanel extends JPanel{
    private final int WIDTH = 800;                  //Panel width
    private final int HEIGHT = 200;                 //Panel height

    private GamePanel gPanel;                       //Game panel
    private InputListener listener;                 //Main listener
    private JToggleButton[] menuButtons;            //Menu buttons displayed on panel
    private Menu menu;                              //Menu responsible for displaying correct screens
    private BufferedImage console;                  //Background image for ControlPanel
    private BufferedImage UI_Sheet;                 //Sheet of UI elements
    private SpriteSheet UI_SS;                      //Spritesheet of UI elements
    private BufferedImage joystickBase;             //Onscreen joystick base
    private BufferedImage joystick;                 //Onscreen joystick
    private int stickUp, stickDown, stickPos;       //Joystick positions
    private BufferedImage hBar, sBar;               //Health/Shield bar images
    private StatusBar healthBar, shieldBar;         //Health/Shield bars
    private BufferedImage hDot, sDot;               //Dots used to fill health/shield bars
    private ImageIcon[] icons;                      //onscreen menu button icons
    private JLabel scoreLabel;                      //Displays score on console    

    public ControlPanel(InputHolder input, GamePanel gp) {
        //Initialize variable
        this.gPanel = gp;
        this.listener = new InputListener(this, input);
        this.icons = new ImageIcon[14];
        this.menuButtons = new JToggleButton[6];
        this.menu = null;
        this.UI_Sheet = null;
        this.UI_SS = null;
        this.joystickBase = null;
        this.joystick = null;
        this.stickUp = 0;
        this.stickDown = 0;
        this.stickPos = 0;
        this.hBar = null;
        this.sBar = null;
        this.healthBar = null;
        this.shieldBar = null;
        this.sDot = null;
        this.hDot = null;
        this.scoreLabel = new JLabel("SCORE: 0", SwingConstants.LEFT);
        
        //Menu buttons
        for(int i = 0; i < menuButtons.length; i++) {
            menuButtons[i] = new JToggleButton();
            menuButtons[i].setBounds(575+(35*i), 128, 30, 30);
            menuButtons[i].addActionListener(listener);
        }

        //Score label settings
        scoreLabel.setBounds(15, 140, 120, 16);
        scoreLabel.setBackground(new Color(0, 0, 0, 0));
        scoreLabel.setFont(new Font("Symbol-Bold", Font.BOLD, 14));
        scoreLabel.setForeground(Color.BLACK);
        this.add(scoreLabel);

        //Panel settings
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(null);
        this.addKeyListener(listener);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.setFocusable(true);
        this.requestFocus();
    }

    //Initialize
    public void init(Menu menu) {
        this.menu = menu;

        //Load images
        BufferedImageLoader loader = new BufferedImageLoader();
        try {
            console = loader.load("res/Console.png");
            UI_Sheet = loader.load("res/UISheet.png");
        } catch(IOException e) {
            e.printStackTrace();
        }

        //Load onscreen joystick
        UI_SS = new SpriteSheet(UI_Sheet, 97, 97, 294, 60);
        joystickBase = UI_SS.getSprite(1, 1);
        UI_SS.setPointer(50, 50, 391, 60);
        joystick = UI_SS.getSprite(1, 1);
        stickPos = HEIGHT / 2 - joystick.getHeight() / 2;
        stickUp = stickPos - 20;
        stickDown = stickPos + 20;

        //Load status bar images
        UI_SS.setPointer(200, 50, 0, 60);
        hBar = UI_SS.getSprite(1, 2);
        sBar = UI_SS.getSprite(1, 1);

        //Load fill dot images for bars
        UI_SS.setPointer(13, 39, 200, 60);
        hDot = UI_SS.getSprite(2, 1);
        sDot = UI_SS.getSprite(1, 1);

        //Create status bars
        healthBar = new StatusBar(hBar, hDot, gPanel, true, 560, 65);
        shieldBar = new StatusBar(sBar, sDot, gPanel, false, 30, 65);

        //Load icons
        UI_SS.setPointer(30, 30, 0, 0);
        for(int i = 0; i < icons.length; i++) {
            if (i <= 6) {
                icons[i] = new ImageIcon((Image)UI_SS.getSprite(i+1, 1));
            }
            else {
                icons[i] = new ImageIcon((Image)UI_SS.getSprite(i-6, 2));
            }
        }

        //Set icons to buttons and display
        for(int j = 0; j < menuButtons.length; j++) {
            menuButtons[j].setBackground(new Color(0, 0, 0, 0));
            menuButtons[j].setBorderPainted(false);
            menuButtons[j].setIcon(icons[j]);
            this.add(menuButtons[j]);
            menuButtons[j].setSelectedIcon(icons[j+7]);
        }

        //Set Menu button icon
        menu.setButtonIcon(new ImageIcon((Image)UI_SS.getSprite(12, 1)));
    }

    //Game tick
    public void tick() {
        scoreLabel.setText("SCORE: " + gPanel.getScore());

        healthBar.tick();
        shieldBar.tick();
    }

    //Set difficulty. Used by menu.
    public void setDifficulty(int difficulty) {
        gPanel.setDifficulty(difficulty);
    }

    //Solved focus issues
    public void focus() {
        this.requestFocus();
    }

    //Update health/shield bar totals
    public void updateStatusBars() {
        healthBar.updateTotalValue();
        shieldBar.updateTotalValue();
    }

    //Reset to initial values
    public void reset() {
        menuButtons[0].setSelected(false);
        menuButtons[1].setSelected(false);
        menuButtons[2].setSelected(false);
        menuButtons[5].setSelected(false);
        stickPos = HEIGHT / 2 - joystick.getHeight() / 2;
        scoreLabel.setText("SCORE: 0");

        //TODO: Health/shield bars
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(console, 0, 0, null);

        if(joystickBase != null) {
            g.drawImage(joystickBase, WIDTH/2-joystickBase.getWidth()/2, HEIGHT/2-joystickBase.getHeight()/2, null);
        }
        if(joystick != null) {
            g.drawImage(joystick, WIDTH/2-joystick.getWidth()/2, stickPos, null);
        }
        if(hBar != null) {
            healthBar.paint(g);
        }
        if (sBar != null) {
            shieldBar.paint(g);
        }
    }
    
    private class InputListener implements KeyListener, MouseListener, MouseMotionListener, ActionListener {

        private ControlPanel panel;
        private InputHolder input;
        private Point clickPosition;

        public InputListener(ControlPanel p, InputHolder input) {
            this.panel = p;
            this.input = input;
            this.clickPosition = new Point(0, 0);
        }

        //Pass commands to input holder based on keypress
        @SuppressWarnings("incomplete-switch")
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                input.setShooting(true);
            }

            switch(e.getKeyCode()) {
                //Up command
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                case KeyEvent.VK_D:
                    input.setInput('w');
                    stickPos = stickUp;
                    break;
                //Down command
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                case KeyEvent.VK_A:
                    input.setInput('s');
                    stickPos = stickDown;
                    break;
                //Pause menu toggle
                case KeyEvent.VK_BACK_SPACE:
                case KeyEvent.VK_ESCAPE:
                case KeyEvent.VK_E:
                    if (!(menu.getState() == Menu.STATE.MAIN)) {
                        switch (menu.getState()) {
                            case STATS -> menuButtons[0].doClick();
                            case PAUSE -> menuButtons[2].doClick();
                            case UPGRADE -> menuButtons[1].doClick();
                            case UPGRADE2 -> menuButtons[1].doClick();
                            case HELP -> menuButtons[5].doClick();
                            case NONE -> menuButtons[2].doClick();
                        }
                    }
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                input.setShooting(false);
            }
            stickPos = HEIGHT / 2 - joystick.getHeight() / 2;
            input.clear();
        }

        //Alternative input using joystick
        @Override
        public void mouseDragged(MouseEvent e) {
            if(e.getY() < clickPosition.getY()) {
                input.setInput('w');
                stickPos = stickUp;
            }
            else if(e.getY() > clickPosition.getY()) {
                input.setInput('s');
                stickPos = stickDown;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            clickPosition = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            stickPos = HEIGHT / 2 - joystick.getHeight() / 2;

            input.clear();
        }

        //Used for toggle buttons
        //Radio buttons were not viable due to inability to unselect all
        @Override
        public void actionPerformed(ActionEvent e) {

            //Determine if all unselected
            boolean selected = false;
            for(int i = 0; i < menuButtons.length; i++) {
                if (i == 3) i += 2;
                if (menuButtons[i].isSelected()) selected = true;
            }

            if (selected) {
                //Pause menu
                if (e.getSource() == menuButtons[2]) {
                    gPanel.pause();
                    menu.setState(Menu.STATE.PAUSE);
                    menuButtons[0].setSelected(false);
                    menuButtons[1].setSelected(false);
                    menuButtons[5].setSelected(false);
                }
                //Upgrades menu
                else if (e.getSource() == menuButtons[1]) {
                    gPanel.pause();
                    menu.setState(Menu.STATE.UPGRADE);
                    menuButtons[0].setSelected(false);
                    menuButtons[2].setSelected(false);
                    menuButtons[5].setSelected(false);
                }
                //Ship stats panel
                else if (e.getSource() == menuButtons[0]) {
                    gPanel.pause();
                    menu.setState(Menu.STATE.STATS);
                    menuButtons[1].setSelected(false);
                    menuButtons[2].setSelected(false);
                    menuButtons[5].setSelected(false);
                }
                //Help panel
                else {
                    gPanel.pause();
                    menu.setState(Menu.STATE.HELP);
                    menuButtons[0].setSelected(false);
                    menuButtons[1].setSelected(false);
                    menuButtons[2].setSelected(false);
                }
            }
            //No menu
            else {
                if (!(menu.getState() == Menu.STATE.MAIN)) {
                    gPanel.unpause();
                    menu.setState(Menu.STATE.NONE);
                }
            }

            //Music toggle
            if (menuButtons[3].isSelected()) {
                //TODO: music toggle
            }
            //Sound toggle
            if (menuButtons[4].isSelected()) {
                //TODO: sounds toggle
            }

            panel.focus();
        }

        //Not implemented
        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }
}
