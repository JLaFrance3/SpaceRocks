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

        //Delete later
        this.setBackground(java.awt.Color.white);

    }

    private class InputListener implements KeyListener, MouseListener, MouseMotionListener {

        private InputHolder input;
        private Point clickPosition;

        public InputListener(InputHolder input) {
            this.input = input;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    input.setInput('w');
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    input.setInput('s');
                    break;
                case KeyEvent.VK_SPACE:
                    input.setInput(' ');
                    break;
                case KeyEvent.VK_BACK_SPACE:
                case KeyEvent.VK_ESCAPE:
                    input.setInput('e');
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
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
