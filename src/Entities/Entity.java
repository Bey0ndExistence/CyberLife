package Entities;
import java.awt.*;

/**
 * The Entity class is an abstract class that defines the properties and methods of an entity in a
 * game, including its position, size, hitbox, and methods for drawing and initializing the hitbox.
 */
public abstract class Entity {

    protected float x,y;
    protected int width, height;
    protected Rectangle hitbox;

    public Entity(float x, float y, int width, int height){
        this.x= x;
        this.y =y;
        this.width = width;
        this.height = height;

    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }

    public   void drawHitbox(Graphics g){
        //for debugging the hitbox
        g.setColor(Color.PINK);
        g.drawRect(hitbox.x,hitbox.y,hitbox.width,hitbox.height);

    }

    public void initHitbox(float x_hitbox, float y_hitbox, int width_hitbox, int height_hitbox){
        hitbox = new Rectangle((int) x_hitbox, (int) y_hitbox,width_hitbox,height_hitbox);
    }

    public Rectangle getHitbox(){
        return  hitbox;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight(){
        return height;
    }

}

