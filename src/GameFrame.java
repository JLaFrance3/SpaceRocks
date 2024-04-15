/* 
 * Jean LaFrance
 * GameFrame
 * Main frame of game
 */

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class GameFrame extends JFrame{
    
    private PlayerProxy proxy;
    private GamePanel gPanel;
    private ControlPanel cPanel;

    public GameFrame() {
        super("Space Rocks");

        //Initialize
        proxy = new PlayerProxy();
        gPanel = new GamePanel(proxy);
        cPanel = new ControlPanel(proxy);

        //Add Panels
        this.setLayout(new BorderLayout());
        this.add(gPanel, BorderLayout.CENTER);
        this.add(cPanel, BorderLayout.SOUTH);

        //Frame settings
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new java.awt.Dimension(800, 600));
        this.setResizable(false);

        //Show frame
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        //Start Game
        gPanel.start();
    }

}
