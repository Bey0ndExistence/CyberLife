package Entities;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Utils.Constants.PlayerConstants.getSpriteAmountTiles;

/**
 * The Bullet class represents a bullet object in a game, with properties such as direction, speed,
 * animation, and collision detection.
 */
public class Bullet extends TilesHitBox{

    private boolean isPlayerBullet;
    BufferedImage[] frames;
    private int dir;
    private float speed;
    private boolean active = false;
    private int aniTick,aniSpeed=35,aniIndex, bullet_type;

   // This is the constructor for the Bullet class. It takes in several parameters including the x and
   // y position, width and height of the bullet, an array of BufferedImages for the animation frames,
   // the direction of the bullet, the type of bullet, and a boolean indicating whether the bullet
   // belongs to the player or not. It sets the direction, animation frames, bullet type, and speed of
   // the bullet, and initializes the active state of the bullet to false.
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
    /**
     * The function checks for collision between an enemy's bullet and the player, and if there is a
     * collision, it deactivates the hitbox, damages the player, and sets the player as not alive.
     * 
     * @param player The player parameter is an instance of the Player class, which represents the
     * player character in the game.
     * @param damage The amount of damage that will be inflicted on the player if a collision occurs.
     */
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
