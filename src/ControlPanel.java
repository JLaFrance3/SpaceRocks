/* 
 * Jean LaFrance
 * ControlPanel
 * JPanel used to collect and pass user inputs to game
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ControlPanel extends JPanel{
    private final int WIDTH = 800;
    private final int HEIGHT = 200;

    private GamePanel gPanel;
    private InputListener listener;
    private JToggleButton[] menuButtons;

    private Menu menu;
    private BufferedImage console;
    private BufferedImage UI_Sheet;
    private SpriteSheet UI_SS;
    private BufferedImage joystickBase;
    private BufferedImage joystick;
    private int stickUp, stickDown, stickPos;
    private BufferedImage healthBar;
    private BufferedImage shieldBar;
    private BufferedImage healthDot;
    private BufferedImage shieldDot;
    private ImageIcon[] icons;              //onscreen menu icons

    public ControlPanel(InputHolder input, GamePanel gp) {
        //Initialize
        this.gPanel = gp;
        this.listener = new InputListener(this, input);
        this.icons = new ImageIcon[14];
        this.menuButtons = new JToggleButton[6];
        this.menu = null;

        //Panel settings
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(null);
        this.addKeyListener(listener);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.setFocusable(true);
        this.requestFocus();
        
        //UI
        for(int i = 0; i < menuButtons.length; i++) {
            menuButtons[i] = new JToggleButton();
            menuButtons[i].setBounds(575+(35*i), 128, 30, 30);
            menuButtons[i].addItemListener(listener);
        }
    }

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

        //Load bars
        UI_SS.setPointer(200, 50, 0, 60);
        healthBar = UI_SS.getSprite(1, 2);
        shieldBar = UI_SS.getSprite(1, 1);

        //Load fill dots for bars
        UI_SS.setPointer(13, 39, 200, 60);
        healthDot = UI_SS.getSprite(2, 1);
        shieldDot = UI_SS.getSprite(1, 1);

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
    }

    //Set difficulty
    public void setDifficulty(int difficulty) {
        gPanel.setDifficulty(difficulty);
    }

    public void focus() {
        this.requestFocus();
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
        if(healthBar != null) {
            g.drawImage(healthBar, 560, 65, null);
        }
        if (shieldBar != null) {
            g.drawImage(shieldBar, 30, 65, null);
        }
    }
    
    private class InputListener implements KeyListener, MouseListener, MouseMotionListener, ItemListener {

        private ControlPanel panel;
        private InputHolder input;
        private Point clickPosition;

        public InputListener(ControlPanel p, InputHolder input) {
            this.panel = p;
            this.input = input;
            this.clickPosition = new Point(0, 0);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                input.setShooting(true);
            }

            switch(e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                case KeyEvent.VK_D:
                    input.setInput('w');
                    stickPos = stickUp;
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                case KeyEvent.VK_A:
                    input.setInput('s');
                    stickPos = stickDown;
                    break;
                case KeyEvent.VK_BACK_SPACE:
                case KeyEvent.VK_ESCAPE:
                case KeyEvent.VK_E:
                    if (!(menu.getState() == Menu.STATE.MAIN)) {
                        if(menu.getState() == Menu.STATE.NONE) {
                            menuButtons[2].setSelected(true);
                        }
                        else {
                            menuButtons[0].setSelected(false);
                            menuButtons[1].setSelected(false);
                            menuButtons[2].setSelected(false);
                            menuButtons[5].setSelected(false);
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
            //Ensure escape key goes through
            else if(input.getInput() != 'e') {
                if(input.getInput() == 'w' || input.getInput() == 's') {
                    stickPos = HEIGHT / 2 - joystick.getHeight() / 2;
                }

                input.clear();
            }
        }

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

        @Override
        public void itemStateChanged(ItemEvent e) {

            //Pause menu
            if (menuButtons[2].isSelected()) {
                gPanel.pause();
                menu.setState(Menu.STATE.PAUSE);
            }
            //Upgrades menu
            else if (menuButtons[1].isSelected()) {
                gPanel.pause();
                menu.setState(Menu.STATE.UPGRADE);
            }
            //Ship stats panel
            else if (menuButtons[0].isSelected()) {
                gPanel.pause();
                menu.setState(Menu.STATE.STATS);
            }
            //Help panel
            else if (menuButtons[5].isSelected()) {
                gPanel.pause();
                menu.setState(Menu.STATE.HELP);
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
