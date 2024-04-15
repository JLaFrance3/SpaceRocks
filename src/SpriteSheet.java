/* 
 * Jean LaFrance
 * SpriteSheet
 * Holds sprites
 */

import java.awt.image.BufferedImage;

public class SpriteSheet {
    
    private BufferedImage spriteSheet;
    private int sWidth;
    private int sHeight;

    public SpriteSheet(BufferedImage ss, int width, int height) {
        this.spriteSheet = ss;
        this.sWidth = width;
        this.sHeight = height;
    }

    public BufferedImage getSprite(int col, int row) {
        BufferedImage sprite = spriteSheet.getSubimage(col*sWidth-sWidth, row*sHeight-sHeight, sWidth, sHeight);
        return sprite;
    }

}
