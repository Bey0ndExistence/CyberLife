package Entities;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The Guns class extends the TilesHitBox class and includes methods for setting the hitbox to null and
 * drawing the gun on the screen if it is visible.
 */
public class Guns extends TilesHitBox{

    public boolean isVisible = true;

    private static int nrGuns;
    public Guns(float x, float y, int width, int height, BufferedImage image) {
        super(x, y, width, height, image);
    }

    public void setHitboxNull(){
        this.hitbox.x =0;
        this.hitbox.y =-200;
        this.width =0;
        this.height =0;
    }
    public void drawGun(Graphics g) {
        // check if isVisible is true before drawing the Gun instance on the screen
        if (isVisible) {
            g.drawImage(image,(int) getHitbox_X(), (int) y, null);
        }
    }

    public void nrGunsTook() {

        nrGuns+=1;
    }
}
