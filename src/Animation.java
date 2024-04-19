/* 
 * Jean LaFrance
 * Animation
 * Used for animating sprite sheets
 */

 import java.awt.image.BufferedImage;

public class Animation {
    
    int speed, index, count, frames, startIndex;
    Boolean reverse;
    BufferedImage[] images;
    BufferedImage currentImage;

    public Animation(BufferedImage[] images) {
        this.images = images;
        this.reverse = false;
        this.speed = 6;
        this.index = 0;
        this.frames = 16;
        this.startIndex = 0;
        this.count = startIndex + 1;
    }

    public Animation(BufferedImage[] images, int speed, int frames, int startIndex, Boolean reverse) {
        this.images = images;
        this.reverse = reverse;
        this.speed = speed;
        this.index = 0;
        this.startIndex = startIndex;
        this.count = startIndex + 1;
        this.frames = frames;
        
        if (reverse) {
            count += frames;
        }
    }

    public BufferedImage getSprite() {
        return currentImage;
    }

    public void runAnimation() {
        index++;
        if(index > speed) {
            index = 0;
            nextFrame();
        }
    }

    private void nextFrame() {
        currentImage = images[count-1];

        if(reverse) {
            count--;

            if (count < 1) {
                count += frames;
            }
        }
        else {
            count++;

            if (count > frames) {
                count = 1;
            }
        }

        
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
