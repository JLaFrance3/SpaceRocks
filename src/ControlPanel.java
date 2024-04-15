/* 
 * Jean LaFrance
 * ControlPanel
 * JPanel used to collect and pass user inputs to game
 */

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class ControlPanel extends JPanel{

    private final int WIDTH = 800;
    private final int HEIGHT = 150;

    private InputListener listener;
    private PlayerProxy proxy;

    public ControlPanel(PlayerProxy proxy) {
        //Initialize
        this.proxy = proxy;
        listener = new InputListener(proxy);

        //Panel settings
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.addKeyListener(listener);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        this.setFocusable(true);
        this.requestFocus();

        //Delete later
        this.setBackground(java.awt.Color.white);

    }

    private class InputListener implements KeyListener, MouseListener, MouseMotionListener {

        private PlayerProxy input;
        private Point clickPosition;

        public InputListener(PlayerProxy input) {
            this.input = input;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            /*     
            *      '-' - No input
            *      'w' - Up
            *      's' - Down
            *      'e' - Pause
            *      ' ' - Shoot
            */

            switch(e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    proxy.moveUp();
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    proxy.moveDown();
                    break;
                case KeyEvent.VK_SPACE:
                    proxy.shoot();
                    break;
                case KeyEvent.VK_BACK_SPACE:
                case KeyEvent.VK_ESCAPE:
                    //TODO: Pause Menu
                    break;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(e.getY() < clickPosition.getY()) {
                proxy.moveUp();
            }
            else if(e.getY() > clickPosition.getY()) {
                proxy.moveDown();
            }

            clickPosition = e.getPoint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            clickPosition = e.getPoint();
        }

        //Not implemented
        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}

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
