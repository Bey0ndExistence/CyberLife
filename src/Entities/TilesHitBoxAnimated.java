package Entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import static Utils.Constants.PlayerConstants.getSpriteAmountTiles;

/**
 * This is a Java class for an animated image with hitbox with a specified position, size, and image frames.
 */
public class TilesHitBoxAnimated extends TilesHitBox{
    BufferedImage[] frames;
    private int aniTick,aniSpeed=26,aniIndex, bannerAction;
    public TilesHitBoxAnimated(float x, float y, int width, int height, BufferedImage[] image,int state) {
        super(x, y, width, height, null);
        bannerAction = state;
        frames = image;
    }

    public void updateAnimation() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmountTiles(bannerAction)) {
                aniIndex = 0;
            }
        }
    }

    public void drawAnimated(Graphics g) {
        try {
            g.drawImage(frames[aniIndex], (int) getHitbox_X(), (int) this.y, null);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
