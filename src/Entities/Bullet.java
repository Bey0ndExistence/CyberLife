package Entities;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Utils.Constants.PlayerConstants.getSpriteAmountTiles;

public class Bullet extends TilesHitBox{

    private boolean isPlayerBullet;
    BufferedImage[] frames;
    private int dir;
    private float speed;
    private boolean active = false;
    private int aniTick,aniSpeed=35,aniIndex, bullet_type;

    public Bullet(float x, float y, int width, int height, BufferedImage[] image,int dir,int bullet_type,boolean isPlayerBullet  ) {
        super(x, y, width, height, null);
        this.dir =dir;
        frames = image;
        this.bullet_type = bullet_type;
        this.speed = 5;
        this.isPlayerBullet = false;
    }

    public void updateBullet(){
        hitbox.x += dir * speed;
        x += dir * speed;
    }

    public void setPos(float x, float y) {

        hitbox.x =(int) x ;
        hitbox.y =(int)  y;
        this.x = x;
        this.y = y;
    }

    public boolean isActive(){
        return active;
    }
    public void checkPlayerCollision(Player player,int damage) {
        if (hitbox.intersects(player.getHitbox())) {
            setActive(false);
            player.damage(damage);
            player.setAlive(false);

        }
    }
    public void updateAnimation() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmountTiles(bullet_type)) {
                aniIndex = 0;
            }
        }
    }

    public void setActive(boolean active){
        this.active= active;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void update() {
        // Update player position and other variables


        // Update the bullet
        if (isActive()) {
            updateBullet();
            updateAnimation();
        }
    }
    public void drawBullet(Graphics g) {
        try {
            if (isActive())
                g.drawImage(frames[aniIndex], (int) this.x, (int) this.y, width, height, null);
        }
        catch (IndexOutOfBoundsException e){
            e.getStackTrace();
        }
    }

}
