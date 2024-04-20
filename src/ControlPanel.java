/* 
 * Jean LaFrance
 * ControlPanel
 * JPanel used to collect and pass user inputs to game
 */

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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ControlPanel extends JPanel{

    private final int WIDTH = 800;
    private final int HEIGHT = 200;

    private GamePanel gPanel;
    private Boolean isPaused;
    private InputListener listener;
    private JToggleButton[] menuButtons;
    private BufferedImage console;
    private BufferedImage UI_SS;
    private SpriteSheet UI;

    private BufferedImage joystickBase;
    private BufferedImage joystick;
    private int stickUp, stickDown, stickPos;
    
    private BufferedImage healthBar;
    private BufferedImage shieldBar;
    private BufferedImage healthDot;
    private BufferedImage shieldDot;
    private ImageIcon[] icons;


    public ControlPanel(InputHolder input, GamePanel gp) {
        //Initialize
        this.gPanel = gp;
        this.listener = new InputListener(input);
        this.icons = new ImageIcon[13];
        this.isPaused = false;

        //Panel settings
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(null);
        this.addKeyListener(listener);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.setFocusable(true);
        this.requestFocus();

        //UI
        menuButtons = new JToggleButton[6];
        for(int i = 0; i < menuButtons.length; i++) {
            menuButtons[i] = new JToggleButton();
            menuButtons[i].setBounds(575+(35*i), 128, 30, 30);
            menuButtons[i].addItemListener(listener);
        }
    }

    public void init() {
        BufferedImageLoader loader = new BufferedImageLoader();

        try {
            console = loader.load("res/Console.png");
            UI_SS = loader.load("res/UISheet.png");
        } catch(IOException e) {
            e.printStackTrace();
        }

        //Load onscreen joystick
        UI = new SpriteSheet(UI_SS, 97, 97, 294, 60);
        joystickBase = UI.getSprite(1, 1);
        UI.setPointer(50, 50, 391, 60);
        joystick = UI.getSprite(1, 1);
        stickPos = HEIGHT / 2 - joystick.getHeight() / 2;
        stickUp = stickPos - 20;
        stickDown = stickPos + 20;

        //Load bars
        UI.setPointer(200, 50, 0, 60);
        healthBar = UI.getSprite(1, 2);
        shieldBar = UI.getSprite(1, 1);

        //Load fill dots for bars
        UI.setPointer(13, 39, 200, 60);
        healthDot = UI.getSprite(2, 1);
        shieldDot = UI.getSprite(1, 1);

        //Load icons
        UI.setPointer(30, 30, 0, 0);
        for(int i = 0; i < icons.length; i++) {
            if (i <= 6) {
                icons[i] = new ImageIcon((Image)UI.getSprite(i+1, 1));
            }
            else {
                icons[i] = new ImageIcon((Image)UI.getSprite(i-6, 2));
            }
        }

        //Set icons to buttons and display
        for(int j = 0; j < menuButtons.length; j++) {
            //The last button doesn't have a 'selected' icon
            if(j == menuButtons.length - 1) {
                menuButtons[j].setIcon(icons[j+1]);
                this.add(menuButtons[j]);
                break;
            }

            //Set icons and 'selected' icons for the first 5 buttons
            menuButtons[j].setIcon(icons[j]);
            this.add(menuButtons[j]);
            menuButtons[j].setSelectedIcon(icons[j+7]);
        }
    }

    public void tick() {

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

        private InputHolder input;
        private Point clickPosition;

        public InputListener(InputHolder input) {
            this.input = input;
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
                    if (isPaused) {
                        isPaused = false;
                        gPanel.unpause();
                    }
                    else {
                        isPaused = true;
                        gPanel.pause();
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
            if (menuButtons[0].isSelected()) {
                //TODO: ship stats
            }
            if (menuButtons[1].isSelected()) {
                //TODO: upgrade menu
            }
            if (menuButtons[2].isSelected()) {
                //TODO: menu
            }
            if (menuButtons[3].isSelected()) {
                //TODO: music off
            }
            if (menuButtons[4].isSelected()) {
                //TODO: sounds off
            }
            if (menuButtons[5].isSelected()) {
                //TODO: help
            }
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
