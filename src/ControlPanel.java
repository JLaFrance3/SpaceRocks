/* 
 * Jean LaFrance
 * ControlPanel
 * JPanel used to collect and pass user inputs to game
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

public class ControlPanel extends JPanel{

    private final int WIDTH = 800;
    private final int HEIGHT = 200;

    private InputListener listener;
    private BufferedImage console;
    private BufferedImage UI_SS;
    private SpriteSheet UI;

    private BufferedImage joystickBase;
    private BufferedImage joystick;
    private int stickUp, stickDown, stickPos;
    
    private BufferedImage healthBar;
    private BufferedImage armorBar;
    private BufferedImage healthDot;
    private BufferedImage armorDot;

    public ControlPanel(InputHolder input) {
        //Initialize
        listener = new InputListener(input);

        //Panel settings
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.addKeyListener(listener);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.setFocusable(true);
        this.requestFocus();

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
        armorBar = UI.getSprite(1, 1);

        //Load fill dots for bars
        UI.setPointer(13, 39, 200, 60);
        healthDot = UI.getSprite(2, 1);
        armorDot = UI.getSprite(1, 1);
    }

    public void tick() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
       
        Graphics2D brush = (Graphics2D)g.create();

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
        if (armorBar != null) {
            g.drawImage(armorBar, 30, 65, null);
        }
    }
    

    private class InputListener implements KeyListener, MouseListener, MouseMotionListener {

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
                    input.setInput('e');
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
