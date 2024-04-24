/* 
 * Jean LaFrance
 * Animation
 * Used for animating sprite sheets
 */

 import java.awt.image.BufferedImage;

public class Animation {
    int speed, speedIndex, count, frames, startIndex;   //Used for determining current sprite and speed of animation
    Boolean reverse;                                    //Used for reversing animation
    BufferedImage[] images;                             //Animation images
    BufferedImage currentImage;                         //Current frame

    public Animation(BufferedImage[] images) {
        this.speed = 6;
        this.speedIndex = 0;
        this.count = startIndex;
        this.frames = 16;
        this.startIndex = 0;
        this.reverse = false;
        this.images = images;
    }

    public Animation(BufferedImage[] images, int speed, int frames, int startIndex, Boolean reverse) {
        this.speed = speed;
        this.speedIndex = 0;
        this.count = startIndex;
        this.frames = frames;
        this.startIndex = startIndex;
        this.reverse = reverse;
        this.images = images;

        if (reverse) {
            count += frames - 1;
        }
    }

    //Get current frame
    public BufferedImage getSprite() {
        return currentImage;
    }

    //Next frame controlled by game clock and speed variable
    public void tick() {
        speedIndex++;
        if(speedIndex > speed) {
            speedIndex = 0;
            nextFrame();
        }
    }

    //Get next frame
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
