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
    private JLayeredPane layerPane;
    private GamePanel gPanel;
    private ControlPanel cPanel;
    private Menu mPanel;

    public GameFrame() {
        super("Space Rocks");

        //Initialize
        input = new InputHolder();
        layerPane = new JLayeredPane();

        //Layered Pane
        this.setLayout(new BorderLayout());
        this.add(layerPane, BorderLayout.CENTER);
        layerPane.setBounds(0, 0, 800, 600);

        //Intialize main panels
        gPanel = new GamePanel(input);
        cPanel = new ControlPanel(input, gPanel);
        mPanel = new Menu(this, cPanel);

        //Add Panels to layered pane
        gPanel.setBounds(0, 0, 800, 600);
        cPanel.setBounds(0, 400, 800, 600);
        mPanel.setBounds(0, 0, 800, 600);
        cPanel.setOpaque(false);
        mPanel.setOpaque(false);
        layerPane.add(gPanel, JLayeredPane.DEFAULT_LAYER);
        layerPane.add(cPanel, JLayeredPane.PALETTE_LAYER);
        layerPane.add(mPanel, JLayeredPane.POPUP_LAYER);

        //Frame settings
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new java.awt.Dimension(800, 600));
        this.setResizable(false);

        //Initialize panels
        gPanel.init();
        mPanel.init();
        cPanel.init(mPanel);

        //Show frame
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        //Main Menu
        mPanel.setState(Menu.STATE.MAIN);
    }

    public void startGame() {
        //Start game
        gPanel.start();
    }
}
