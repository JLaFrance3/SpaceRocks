/* 
 * Jean LaFrance
 * Avatar
 * Player character
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Avatar extends Entity{

    private InputHolder input;

    //Player avatar constructor
    public Avatar(BufferedImage sprite, GamePanel gp, InputHolder input) {
        super(sprite, gp, -200, 725, -90);

        this.input = input;
    }

    public void tick() {
        //Coordinate plane is all wonky due to graphics getting rotated
        switch(input.getInput()) {
            case 'w':
                setDX(getSpeed());
                break;
            case 's':
                setDX(-getSpeed());
                break;
            case 'e':
                //TODO: Menu
                break;
        }

        shoot();
        
        //Wall collision
        if(getX() > 0) {
            setLocation(0, getY());
        }
        if (getX() < -getGP().getHeight() + 190) {
            setLocation(-getGP().getHeight() + 190, getY());
        }

        super.tick();

        setDX(0);
    }

    @Override
    public void shoot() {
        if(input.isShooting()) {
            super.shoot();
        }
    }

    //Call parent class overloaded method with rotation
    public void paint(Graphics2D brush) {
        super.paint(brush, getRotation());
    }
}
