/* 
 * Jean LaFrance
 * GameFrame
 * Main frame of game
 */

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

public class GameFrame extends JFrame{
    
    private InputHolder input;
    private JLayeredPane layer;
    private GamePanel gPanel;
    private ControlPanel cPanel;

    public GameFrame() {
        super("Space Rocks");

        //Initialize
        input = new InputHolder();
        gPanel = new GamePanel(input);
        cPanel = new ControlPanel(input);

        //Layered Pane
        layer = new JLayeredPane();
        this.setLayout(new BorderLayout());
        this.add(layer, BorderLayout.CENTER);
        layer.setBounds(0, 0, 800, 600);

        //Add Panels to layered pane
        gPanel.setBounds(0, 0, 800, 600);
        cPanel.setBounds(0, 400, 800, 600);
        cPanel.setOpaque(false);
        layer.add(gPanel, 0, 0);
        layer.add(cPanel, 1, 0);

        //Frame settings
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new java.awt.Dimension(800, 600));
        this.setResizable(false);

        //Show frame
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        //Start Game
        gPanel.init();
        cPanel.init();
        gPanel.start();
    }

}
