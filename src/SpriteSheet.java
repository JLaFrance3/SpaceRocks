/* 
 * Jean LaFrance
 * SpriteSheet
 * Allows easier access to individual sprites on sheet
 */

import java.awt.image.BufferedImage;

public class SpriteSheet {
    
    private BufferedImage spriteSheet;  //Image of sprites
    private int sWidth;                 //Sprite width
    private int sHeight;                //Sprite height
    private int xOffset;                //Offset to get to desired column
    private int yOffset;                //Offset to get to desired row

    public SpriteSheet(BufferedImage ss, int width, int height) {
        this.spriteSheet = ss;
        this.sWidth = width;
        this.sHeight = height;
        this.xOffset = 0;
        this.yOffset = 0;
    }

    public SpriteSheet(BufferedImage ss, int width, int height, int xOffset, int yOffset) {
        this.spriteSheet = ss;
        this.sWidth = width;
        this.sHeight = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    //Set spritesheet position
    public void setPointer(int width, int height, int x, int y) {
        this.sWidth = width;
        this.sHeight = height;
        this.xOffset = x;
        this.yOffset = y;
    }

    //Returns selected sprite
    public BufferedImage getSprite(int col, int row) {
        BufferedImage sprite = spriteSheet.getSubimage(col*sWidth-sWidth+xOffset, row*sHeight-sHeight+yOffset, sWidth, sHeight);
        return sprite;
    }

}
