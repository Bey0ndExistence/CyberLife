package levels;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
/**
 * The BackgroundLayer class represents a layer of background image with a parallax effect that can be
 * updated and drawn on a graphics context.
 */
public class BackgroundLayer {
    private BufferedImage image;
    private double parallaxFactor;
    private int x;
    private int y;

    public BackgroundLayer(BufferedImage image, double parallaxFactor, float x, float y) {
        this.image = image;
        this.parallaxFactor = parallaxFactor;
        this.x = (int) x;
        this.y = (int) y;
    }

    public void update(Camera camera) {
        x = (int) (-camera.getX() * parallaxFactor);

    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(image, x, y, null);
    }
}
