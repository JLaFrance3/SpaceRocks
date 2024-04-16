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
    private int offset;

    public SpriteSheet(BufferedImage ss, int width, int height) {
        this.spriteSheet = ss;
        this.sWidth = width;
        this.sHeight = height;
        this.offset = 0;
    }

    public SpriteSheet(BufferedImage ss, int width, int height, int offset) {
        this.spriteSheet = ss;
        this.sWidth = width;
        this.sHeight = height;
        this.offset = offset;
    }

    public BufferedImage getSprite(int col, int row) {
        BufferedImage sprite = spriteSheet.getSubimage(col*sWidth-sWidth, row*sHeight-sHeight+offset, sWidth, sHeight);
        return sprite;
    }

}
