/* 
 * Jean LaFrance
 * Player Proxy
 * Class used control player avatar between frames
 * Input types:

 */

public class PlayerProxy {
    
    private Avatar player;
    
    public void setPlayer(Avatar player) {
        this.player = player;
    }

    public void moveDown() {
        player.moveDown();
    }

    public void moveUp() {
        player.moveUp();
    }

    public void shoot() {
        player.shoot();
    }

}
