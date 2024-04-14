/* 
 * Jean LaFrance
 * GamePanel
 * Main panel of game used to paint player avatar and enemies
 */

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel{
    
    private static final int DELAY = 100;
    private final int WIDTH = 800;
    private final int HEIGHT = 450;

    private boolean running = false;
    private Timer timer;
    private Random random;
    private InputHolder input;

    public GamePanel(InputHolder in) {
        //Initialize
        this.input = in;
        running = true;
        timer = new Timer(DELAY, new ClockListener(this));
        random = new Random();

        //Panel settings
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        //Delete later
        this.setBackground(java.awt.Color.black);
    }

    public void start() {

    }

    public void update() {

    }

    public void end() {

    }

    public void pause() {

    }

    public void unpause() {

    }

    public void paintComponent() {

    }

    private class ClockListener implements ActionListener {

        public JPanel panel;
        
        public ClockListener(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(running) {
                update();
            }
        }
    }
}
