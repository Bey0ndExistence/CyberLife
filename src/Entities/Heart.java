package Entities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Heart extends TilesHitBox{
    public boolean isVisible = true;
    private static int nrHearts;
    public Heart(float x, float y, int width, int height, BufferedImage image) {
        super(x, y, width, height, image);
    }

    public void drawHeart(Graphics g) {
        // check if isVisible is true before drawing the Gun instance on the screen
        if (isVisible) {
            g.drawImage(image,(int) getHitbox_X(), (int) y, null);
        }
    }

    public void nrHearsTook() {
        nrHearts+=1;
    }
}
