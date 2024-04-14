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
    private InputHolder input;

    public ControlPanel(InputHolder input) {
        //Initialize
        this.input = input;
        listener = new InputListener(input);

        //Panel settings
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        //Delete later
        this.setBackground(java.awt.Color.green);

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
                    System.out.println("Up");
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    input.setInput('s');
                    System.out.println("Down");
                    break;
                case KeyEvent.VK_BACK_SPACE:
                case KeyEvent.VK_ESCAPE:
                    input.setInput('e');
                    System.out.println("Esc");
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            //Make sure escape key goes through
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

            clickPosition = e.getPoint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            clickPosition = e.getPoint();
        }

        //Not implemented
        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        
    }
}
