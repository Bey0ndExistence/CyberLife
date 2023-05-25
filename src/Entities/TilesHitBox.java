package Entities;

import levels.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The TilesHitBox class extends the Entity class and represents the hitbox for tiles on the screen with
 * an image.
 */
public  class TilesHitBox extends Entity{

    // X position of the entity on the screen
    protected BufferedImage image;
    public TilesHitBox(float x, float y, int width, int height, BufferedImage image) {
        super(x, y, width, height);
        this.image = image;
    }


    public float getHitbox_X() {
        return hitbox.x;
    }

    public void setHitbox_X(float screenX) {
        this.hitbox.x = (int) screenX;
    }

    public void update(Camera camera) {
        setHitbox_X(getX() - camera.getX());

    }

    public final void draw(Graphics g) {
        g.drawImage(image, (int) getHitbox_X(), (int) this.y, null);
    }



}
