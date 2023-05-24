package Entities;

import Utils.GameException;
import Utils.LoadSave;
import gamestates.Gamestate;
import levels.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Utils.Constants.EnemyConstants.*;
import static Utils.Constants.PlayerConstants.BULLET_BOSS_LVL1;

public class FinalBoss2 extends Enemy{
    BufferedImage[][] image;
    Player player;
    private boolean alive=true;
    private int lives;

    private int damage;

    private Rectangle attackingHitbox;

    private boolean canAttack;
    private int attackCooldown;
    private int cooldownTimer;
    private int attackDelay;
    private int attackDelayTimer;

    private int attackDistance;

    private boolean hasJumped; // Flag to track if the boss has already jumped


    public FinalBoss2(float x, float y, int width, int height, int enemyType, BufferedImage[][] image,Player player,int lives,int damage) {
        super(x, y, width, height, enemyType);
        this.image = image;

        this.player = player;
        this.attackDistance = 650;
        this.detectionDistance = 1000;
        this.lives = lives;
        attackingHitbox = new Rectangle((int) getHitbox().x , (int) getHitbox().y , 40, 40);
        this.damage = damage;
        this.attackCooldown = 150;
        this.cooldownTimer = 0;
        this.canAttack = true;
        this.attackDelay = 150; // Adjust the delay value as needed
        this.attackDelayTimer = 0;
    }

    @Override
    protected void updateAnimationTick() {

        super.updateAnimationTick();
    }


    public boolean enemy_got_damaged(){
        if(player.isSecondGun())
            lives-=2;
        else
            lives--;

        if(lives==0){
            alive =false;
        }

        System.out.println("Enemy has only " + lives +"remaining");
        return (lives > 0);
    }

    @Override
    public void update(Camera camera, Player player, ArrayList<TilesHitBox> walls){
        updateAnimationTick();
       // update(camera);
        updateEnemyPos(player,walls,camera);
        //System.out.println("suntem in update-ul de la BOSS");

    }

    @Override
    public void updateEnemyPos(Player player, ArrayList<TilesHitBox> walls, Camera camera) {
        previouAction = this.enemyState;
        super.updateEnemyPos(player, walls, camera);
        float playerDistance = Math.abs(player.getX() - getHitbox_X());



        if (playerDistance < attackDistance) {
            playerDetected = true;
        } else {
            playerDetected = false;
        }

        if (isBossAlive()) {
            if (playerDetected && canAttack && cooldownTimer <= 0) {
                enemyState = ATTACK_BOSS2;

               // Check if the boss has already jumped
                    if (player.getX() +150 < getHitbox_X()) {
                        xspeed = -2; // Adjust the jump speed as needed
                    } else {
                        xspeed = 5; // Adjust the jump speed as needed
                    }
                if (!hasJumped) {
                    yspeed = -2; // Adjust the jump height as needed

                    hitbox.y += yspeed;
                    hasJumped = true; // Set the flag to true after jumping
                }
                hitbox.x += xspeed;

                // Start attack animation with a delay
                if (attackDelayTimer >= attackDelay) {
                    attackingHitbox.setLocation((int) getHitbox().x - 100, (int) getHitbox().y + 170);
                    if (player.getHitbox().intersects(attackingHitbox)) {
                        // Player collided with the attacking hitbox, deal damage to the player
                        player.damage(1);
                        canAttack = false;
                        cooldownTimer = attackCooldown;
                    } else {
                        // Player is within detection range but not in contact with attacking hitbox
                        // Keep the attack animation without dealing damage
                    }
                } else {
                    // Increment the attack delay timer
                    attackDelayTimer++;
                }




            } else if (cooldownTimer > 0) {
                // Cooldown in progress, maintain the attack animation
                enemyState = ATTACK_BOSS2;
                update(camera);
            } else {
                update(camera);
                enemyState = IDLE_BOSS2;
                attackingHitbox.setLocation(getHitbox().x, getHitbox().y);
                attackDelayTimer = 0; // Reset the attack delay timer when the player is outside the detection range
            }
        }
        else{
            update(camera);
        }

        // Update cooldown timer
        if (!canAttack && cooldownTimer > 0) {
            cooldownTimer--;
            if (cooldownTimer == 0) {
                // Cooldown is finished, reset the attack animation
              //  enemyState = IDLE_BOSS2;
                canAttack = true;
                attackDelayTimer = 0; // Reset the attack delay timer
            }
        }

        if (playerDistance < detectionDistance && !(playerDistance< attackDistance)) {
            enemyState = GRUMPY_BOSS2;

        }

        if (hitbox.intersects(player.getHitbox())) {
            if (!isBossAlive()) {
                player.LvlSpawn(1300);
                Gamestate.state = Gamestate.PLAYING3;
            } else {
                EnemyCollidedPlayer(player);
                player.setAlive(false);
            }
        }
        if (previouAction != this.enemyState) {
            this.aniIndex = 0;
            this.aniTick = 0;
        }
    }

    public boolean isBossAlive(){
        return alive;
    }



    public void drawAttack(Graphics g) {
        super.draw(g);

            // Draw the attacking hitbox
            g.setColor(Color.RED);
            g.drawRect(attackingHitbox.x,attackingHitbox.y, attackingHitbox.width, attackingHitbox.height);
        }

}
