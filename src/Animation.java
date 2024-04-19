/* 
 * Jean LaFrance
 * Animation
 * Used for animating sprite sheets
 */

 import java.awt.image.BufferedImage;

public class Animation {
    
    int speed, speedIndex, count, frames, startIndex;
    Boolean reverse;
    BufferedImage[] images;
    BufferedImage currentImage;

    public Animation(BufferedImage[] images) {
        this.images = images;
        this.reverse = false;
        this.speed = 6;
        this.speedIndex = 0;
        this.frames = 16;
        this.startIndex = 0;
        this.count = startIndex;
    }

    public Animation(BufferedImage[] images, int speed, int frames, int startIndex, Boolean reverse) {
        this.images = images;
        this.reverse = reverse;
        this.speed = speed;
        this.speedIndex = 0;
        this.startIndex = startIndex;
        this.count = startIndex;
        this.frames = frames;
        
        if (reverse) {
            count += frames - 1;
        }
    }

    public BufferedImage getSprite() {
        return currentImage;
    }

    public void runAnimation() {
        speedIndex++;
        if(speedIndex > speed) {
            speedIndex = 0;
            nextFrame();
        }
    }

    private void nextFrame() {
        currentImage = images[count];

        if(reverse) {
            count--;

            if (count <= startIndex) {
                count += frames - 1;
            }
        }
        else {
            count++;

            if (count >= frames + startIndex) {
                count = startIndex;
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
