/* 
 * Jean LaFrance
 * StatusBar
 * Manages the health/shield bars in game
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class StatusBar {

    private BufferedImage bar;      //Status bar for health or shield
    private BufferedImage dot;      //Dot that fills bar
    private GamePanel gp;           //Main game panel
    private boolean isHealth;       //Either health or shield
    private boolean fromLeft;       //Dots filled from left or right
    private int x, y;               //Bar position on panel
    private double totalValue;      //Total health/shield value
    private double currentValue;    //Current health/shield

    public StatusBar(BufferedImage bar, BufferedImage dot, GamePanel gp, boolean isHealth, int x, int y) {
        this.bar = bar;
        this.dot = dot;
        this.gp = gp;
        this.isHealth = isHealth;
        this.fromLeft = true;
        this.x = x;
        this.y = y;
        this.totalValue = 0;
        this.currentValue = 0;
    }

    public void init() {
        totalValue = getTotalValue();
        currentValue = totalValue;
    }

    //Game tick
    public void tick() {
        if (totalValue <= 0) {
            init();
        }

        //Get updated health/shield value
        if (isHealth) {
            currentValue = gp.getPlayerHealth();
        }
        else {
            currentValue = gp.getPlayerShield();
        }

    }

    //TODO: Menu needs to call this on upgrade
    //Gets health/shield upon
    public double getTotalValue() {
        double value = 0;

        if (isHealth) {
            value = gp.getPlayerMaxHealth();
        }
        else {
            value = gp.getPlayerMaxShield();
        }

        return value;
    }

    //Reverse dot fill direction
    public void setDirection(boolean fromLeft) {
        this.fromLeft = fromLeft;
    }

    public void paint(Graphics g) {
        //Paint bar at x, y
        g.drawImage(bar, x, y, null);

        //Shield bar
        if (!isHealth) {
            for(int i = 0; i < 11; i++) {
                //paint dot at ...
                //x+5, y+65
                //iter: x + 14
                if (currentValue >= totalValue / 11 * i) {
                    g.drawImage(dot, x + 5 + (14 * i), y + 5, null);
                }
            }
        }
        //Health bar reverse direction
        else {
            for(int i = 11; i > 0; i--) {
                if (currentValue >= totalValue / 11 * i) {
                    g.drawImage(dot, x + 28 + (14 * i), y + 5, null);
                }
            }
        }
    }
    
}
