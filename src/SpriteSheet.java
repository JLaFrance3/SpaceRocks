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
    private int xOffset;
    private int yOffset;

    public SpriteSheet(BufferedImage ss, int width, int height) {
        this.spriteSheet = ss;
        this.sWidth = width;
        this.sHeight = height;
        this.xOffset = 0;
        this.yOffset = 0;
    }

    public SpriteSheet(BufferedImage ss, int width, int height, int yOffset) {
        this.spriteSheet = ss;
        this.sWidth = width;
        this.sHeight = height;
        this.xOffset = 0;
        this.yOffset = yOffset;
    }

    public SpriteSheet(BufferedImage ss, int width, int height, int xOffset, int yOffset) {
        this.spriteSheet = ss;
        this.sWidth = width;
        this.sHeight = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public BufferedImage getSprite(int col, int row) {
        BufferedImage sprite = spriteSheet.getSubimage(col*sWidth-sWidth+xOffset, row*sHeight-sHeight+yOffset, sWidth, sHeight);
        return sprite;
    }

}
