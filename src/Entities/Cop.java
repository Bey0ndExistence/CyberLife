package Entities;

import levels.Camera;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Utils.Constants.EnemyConstants.*;

public class Cop extends  Enemy{
    BufferedImage[][] image;

    Player player;
    private int lives = 2;



    public Cop(float x, float y, int width, int height,  int enemyType,BufferedImage[][] image,Player player, int lives,Camera camera) {
        super(x, y, width, height, enemyType);
        this.image = image;
        this.player = player;
        this.lives = lives;
        this.detectionDistance = 400;


    }

   /**
    * This function reduces the lives of an enemy in a game and returns a boolean indicating if the
    * enemy is still alive.
    * 
    * @return The method is returning a boolean value that indicates whether the enemy still has lives
    * remaining or not. If the enemy has lives remaining, the method returns true, otherwise it returns
    * false.
    */
    public boolean enemy_got_damaged(){
        if(player.isSecondGun())
            lives-=2;
        else
            lives--;

        System.out.println("Enemy has only " + lives +"remaining");
        return (lives > 0);
    }

    @Override
    public void update(Camera camera,Player player,ArrayList<TilesHitBox> walls){
        updateAnimationTick();
        updateEnemyPos(player,walls,camera);
    }


   /**
    * This function updates the position and state of an enemy character in a game based on the
    * player's position and other game elements.
    * 
    * @param player The player object that the enemy is trying to detect and chase.
    * @param walls An ArrayList of TilesHitBox objects representing the walls in the game level.
    * @param camera The camera object used to display the game world. It is likely used to determine
    * the position of the enemy on the screen.
    */
    @Override
    public void updateEnemyPos(Player player, ArrayList<TilesHitBox> walls, Camera camera) {
        previouAction = this.enemyState;
        super.updateEnemyPos(player, walls, camera);
        float playerDistance = Math.abs(player.getX() - getHitbox_X());

        if (!playerDetected && playerDistance < detectionDistance) {
            playerDetected = true;
        }
        if (playerDistance < pickUpGun) {
            enemyState = IDLE_GUN;
        } else {
            enemyState = IDLE;
        }

        if (playerDetected) {
            float distanceToPlayer = player.getX() - getHitbox_X();
            enemyState = RUNNING;
            if (distanceToPlayer < -300) {
                // Player is to the left of the enemy by more than 200 pixels
                movingLeft = true;
                movingRight = false;

            } else if (distanceToPlayer > 300) {
                // Player is to the right of the enemy by more than 200 pixels
                movingLeft = false;
                movingRight = true;
            }
        }


       if(!playerDetected){
           xspeed = 0;
           update(camera);
       }

       if(movingRight){

           if(player.getCameraHitbox().x - player.getLastCameraX() > 0) // playerul se misca la dreapta
                xspeed = 1;
           if(player.getCameraHitbox().x - player.getLastCameraX() < 0) // playerul se misca la stanga
                   xspeed = 2;

       }
       else if(movingLeft){
           xspeed = -2;
       }

        if (previouAction != this.enemyState) {
            this.aniIndex = 0;
            this.aniTick = 0;
        }


        //System.out.println(player.getCameraHitbox().x - getHitbox_X());
    }

    public void setMovingLeft(){
        movingLeft = true;
    }
    public void setMovingRight(){
        movingRight = true;
    }




}
