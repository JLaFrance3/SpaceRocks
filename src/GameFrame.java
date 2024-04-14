/* 
 * Jean LaFrance
 * GameFrame
 * Main frame of game
 */

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class GameFrame extends JFrame{
    
    private InputHolder input;

    public GameFrame() {
        super("Space Rocks");

        //Initialize
        input = new InputHolder();

        //Add Panels
        this.setLayout(new BorderLayout());
        this.add(new GamePanel(input), BorderLayout.CENTER);
        this.add(new ControlPanel(input), BorderLayout.SOUTH);

        //Frame settings
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new java.awt.Dimension(800, 600));
        this.setResizable(false);

        //Show frame
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

}
