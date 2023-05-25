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
import static Utils.Constants.PlayerConstants.BULLET_TURRET;

public class FinalBoss extends Enemy{
    BufferedImage [][] image;
    Player player;
    private boolean alive=true;
    private int lives;

    private ArrayList<Bullet> bullets;

    BufferedImage [] bulletSheet;

    Bullet bullet;

    private int shootCooldown = 200; // Cooldown in ticks (adjust as needed)
    private int shootTimer = 0; // Timer to track cooldown

    private int damage;

    public FinalBoss(float x, float y, int width, int height, int enemyType, BufferedImage[][] image,Player player, int lives, int damage) {
        super(x, y, width, height, enemyType);
        this.image = image;
        loadBulletAnimation();
        this.player = player;
        this.detectionDistance = 600;
        this.lives = lives;
        bullet = new Bullet(this.x , this.y+20,177, 84, bulletSheet,-1 ,BULLET_BOSS_LVL1,false);
        bullet.initHitbox(this.x,this.y +20,177, 84);
        bullets = new ArrayList<Bullet>();
        this.damage = damage;
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
    public void update(Camera camera,Player player,ArrayList<TilesHitBox> walls){
        updateAnimationTick();
        update(camera);
        updateEnemyPos(player,walls,camera);
        //System.out.println("suntem in update-ul de la BOSS");

    }

    @Override
    public void updateEnemyPos(Player player, ArrayList<TilesHitBox> walls,Camera camera){
        this.previouAction = enemyState;
        super.updateEnemyPos(player,walls,camera);
        float playerDistance = Math.abs(player.getX() - getHitbox_X());

        if (playerDistance < detectionDistance) {

            playerDetected = true;
        } else {
            playerDetected = false;
        }

        if (playerDetected){
            enemyState = SHOOTING_BOSS1;
        }
        else{
            enemyState = IDLE_BOSS1;
        }

        if(isBossAlive()) {
            shootTimer++; // Increase shoot timer

            // Check if the player is within detection distance and if enough time has passed since the last shot
            if (playerDetected && shootTimer >= shootCooldown) {
                shootBullet(player);
                shootTimer = 0; // Reset the shoot timer
            }

            // Update bullets
            for (int i = bullets.size() - 1; i >= 0; i--) {
                Bullet bullet = bullets.get(i);
                bullet.update();

                bullet.checkPlayerCollision(player,damage);

                if (!bullet.isActive()) {
                    bullets.remove(i);
                }
            }
        }

        //System.out.println("Player-ul ?: "+ Player.getIsAlive());
        if (hitbox.intersects(player.getHitbox()))  {
            if(!isBossAlive()){
                //System.out.println("Player-ul ?: "+ Player.getIsAlive());

                player.LvlSpawn(1200);

                Gamestate.state = Gamestate.PLAYING2;
            }
            else{
                EnemyCollidedPlayer(player);
                player.setAlive(false);
            }

        }
        if(previouAction != this.enemyState) {
            this.aniIndex = 0;
            this.aniTick =  0;
        }
    }

    private void shootBullet(Player player) {

        float turretX = this.hitbox.x-40 ;
        float turretY = this.hitbox.y + 90;
        Bullet newBullet =  new Bullet(turretX, turretY,177, 84, bulletSheet,-1 ,BULLET_BOSS_LVL1,false);
        newBullet.initHitbox(turretX, turretY,177, 84);
        newBullet.setActive(true);
        newBullet.setPos(turretX, turretY);
        //newBullet.setSpeed(bulletXSpeed);

        bullets.add(newBullet);
    }

    public void drawBullets(Graphics g) {
        for (Bullet bullet : bullets) {
            bullet.drawBullet(g);
        }
    }
    public void loadBulletAnimation() {
        try {
            BufferedImage temp = LoadSave.getInstance().getAtlas(LoadSave.BOSS_LVL1_BULLET);
            this.bulletSheet = new BufferedImage[4];
            for (int i = 0; i < bulletSheet.length; i++) {
                this.bulletSheet[i] = temp.getSubimage(i * 177, 0, 177, 84);
            }
        } catch (GameException e) {
            // Handle the exception here
            System.out.println("GameException occurred:");
            System.out.println("Error Category: " + e.getErrorCategory());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Message: " + e.getMessage());
        }
    }

    public boolean isBossAlive(){
        return alive;
    }


}
