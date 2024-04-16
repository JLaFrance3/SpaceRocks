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
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
       
        Graphics2D brush = (Graphics2D)g.create();

        if(console != null) {
            g.drawImage(console, 0, 0, null);
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
                    input.setInput('w');
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    input.setInput('s');
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
            if(input.getInput() != 'e') {
                input.clear();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(e.getY() < clickPosition.getY()) {
                input.setInput('w');
            }
            else if(e.getY() > clickPosition.getY()) {
                input.setInput('s');
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            clickPosition = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
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
