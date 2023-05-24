package Entities;

import gamestates.Gamestate;
import levels.Camera;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import static Utils.Constants.EnemyConstants.*;

public abstract class Enemy extends TilesHitBox {

    protected int aniIndex, enemyState, enemyType;
    protected int aniTick, aniSpeed = 35;
    protected boolean playerDetected = false;
   protected boolean movingLeft = false;
   protected boolean movingRight = false;
    protected float detectionDistance = 300; // change to set the distance at which the enemy detects the player
    protected float pickUpGun = 500f;
   protected float xspeed,yspeed;

   protected int previouAction;

    boolean ok;

    public Enemy(float x, float y, int width, int height,int enemyType) {
        super(x, y, width, height, null);
        this.enemyType = enemyType;
        initHitbox(x, y, width/2, height);
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;
            }

        }
    }

    public void update(Camera camera,Player player,ArrayList<TilesHitBox> walls) {
        updateAnimationTick();
        updateEnemyPos(player,walls,camera);
        if(enemyType == TURRET || enemyType == BOSS_1 )
            super.update(camera);
       // System.out.println("update robot");
        //System.out.println(hitbox.x);
    }

    public void updateEnemyPos(Player player, ArrayList<TilesHitBox> walls,Camera camera) {
        Iterator<TilesHitBox> iter;
        TilesHitBox wall = null;

        ok = true;
        // move to the left

        if (xspeed < 0) {
            iter = walls.iterator();
            while (iter.hasNext()) {
                wall = iter.next();
                if (hitbox.x + xspeed <= wall.getHitbox().x + wall.getHitbox().width &&
                        hitbox.x >= wall.getHitbox().x + wall.getHitbox().width &&
                        hitbox.y < wall.getHitbox().y + wall.getHitbox().height &&
                        hitbox.y + hitbox.height > wall.getHitbox().y) {
                    if (wall instanceof Guns) {
                        // ... (handle Guns collision)
                    } else if (wall instanceof Heart) {

                    } else {

                        ok = false;
                        break;
                    }
                }
            }
        }
        else if( xspeed > 0){
            iter = walls.iterator();
            while (iter.hasNext()) {
                wall = iter.next();
                if (hitbox.x + hitbox.width + xspeed >= wall.getHitbox().x &&
                        hitbox.x + hitbox.width <= wall.getHitbox().x &&
                        hitbox.y < wall.getHitbox().y + wall.getHitbox().height &&
                        hitbox.y + hitbox.height > wall.getHitbox().y) {
                    if (wall instanceof Guns) {
                        // ... (handle Guns collision)
                    } else if (wall instanceof Heart) {

                    } else {

                        ok = false;
                        break;
                    }
                }
            }
        }

        // Check player collision
        if (hitbox.intersects(player.getHitbox()))  {

           if(enemyType != BOSS_1 && enemyType != BOSS_2 && enemyType !=BOSS_3) {
               player.setAlive(false);
               EnemyCollidedPlayer(player);
           }
            ok =false;


        }
        if (ok) {
            hitbox.x += xspeed;
        }



        yspeed += 0.035;
        hitbox.y += yspeed;
        iter = walls.iterator();
        while (iter.hasNext()) {
            wall = iter.next();
            if (getHitbox().intersects(wall.getHitbox())) {
                if (wall instanceof Guns) {
                    // ... (handle Guns collision)
                } else {
                    hitbox.y -= yspeed;
                    while (!wall.getHitbox().intersects(getHitbox()))
                        hitbox.y += Math.signum(yspeed);
                    hitbox.y -= Math.signum(yspeed);
                    yspeed = 0;
                }
            }
            if (getHitbox().intersects(player.getHitbox())) {

                    if(enemyType != BOSS_1 && enemyType != BOSS_2 && enemyType != BOSS_3) {
                        player.setAlive(false);
                        System.out.println("sper ca nu intru aici");
                        EnemyCollidedPlayer(player);
                }


            }
            y = hitbox.y;
        }

        y += yspeed;
        hitbox.y = (int) y;



    }

    public void EnemyCollidedPlayer(Player player) {
            player.damage(3); // insta death
    }
    public int getAniIndex() {
        return aniIndex;
    }

    public float getSpeed(){
        return xspeed;
    }
    public int getEnemyState() {
        return enemyState;
    }

}
