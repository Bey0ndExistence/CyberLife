package Entities;

import Utils.GameException;
import Utils.LoadSave;
import gamestates.Gamestate;
import levels.Camera;
import levels.Database;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import static Utils.Constants.EnemyConstants.*;
import static Utils.Constants.PlayerConstants.*;

public class FinalBoss3 extends Enemy{
    BufferedImage [][] image;
    Player player;
    private boolean alive=true;
    private int lives = 25;

    private ArrayList<Bullet> bullets;

    BufferedImage [] bulletSheet,bulletSheetSecondGun;

    Bullet bullet;

    private int shootCooldown = 200; // Cooldown in ticks (adjust as needed)
    private int shootTimer = 0; // Timer to track cooldown
    ScoreSubject subject;
    private int damage;

    private int attackDistance;

    private boolean secondGun=false;


    private boolean moveLeftCooldownActive = false;
    private int moveLeftCooldownTimer = 0;
    private int moveLeftCooldownDuration = 100;

    public FinalBoss3(float x, float y, int width, int height, int enemyType, BufferedImage[][] image,Player player, int lives, int damage, ScoreSubject subject) {
        super(x, y, width, height, enemyType);
        this.image = image;
        loadBulletAnimation();
        loadBulletAnimationSecondGun();
        this.player = player;
        this.detectionDistance = 850;
        this.lives = lives;
        bullet = new Bullet(this.x , this.y+20,145, 25, bulletSheet,-1 ,BULLET_BOSS_LVL1,false);
        bullet.initHitbox(this.x,this.y +20,145, 25);
        bullets = new ArrayList<Bullet>();
        this.damage = damage;
        this.subject = subject;

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
        //update(camera);
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
          if(lives>15)
            enemyState = ATTACK_BOSS3_FIRSTGUN;
          else{
              secondGun = true;
              this.damage = 2;
              enemyState =ATTACK_BOSS3_SECONDGUN;
          }
        }
        else{
            enemyState = IDLE_BOSS3;
        }

        if(isBossAlive()) {
            update(camera);

            shootTimer++; // Increase shoot timer

            // Check if the player is within detection distance and if enough time has passed since the last shot
            if (playerDetected && shootTimer >= shootCooldown) {
                if(!secondGun)
                    shootBullet();
                else
                    shootBulletSecondGun();

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
        else{
            update(camera);
        }

        //System.out.println("Player-ul ?: "+ Player.getIsAlive());
        if (hitbox.intersects(player.getHitbox()))  {
            if(!isBossAlive()){
                createDatabase();
            }
            else{
                EnemyCollidedPlayer(player);
                player.setAlive(false);
            }

        }
        TilesHitBox wall= null;
        Iterator<TilesHitBox> iter;

        for (int i = player.getBullets().size() - 1; i >= 0; i--) {
            Bullet bullet = player.getBullets().get(i);
            System.out.println("size " + player.getBullets().size() );
            if (bullet.getHitbox().x > getHitbox().getX() - 300) {

                enemyState = JUMPING_BOSS3;
                hitbox.y++;
                iter = walls.iterator();
                while (iter.hasNext()) {
                    wall = iter.next();
                    if (wall.getHitbox().intersects(getHitbox())) {
                        yspeed = -4;
                    }
                }
                hitbox.y--;
            }
        }

        yspeed += 0.035;


        if(previouAction != this.enemyState) {
            this.aniIndex = 0;
            this.aniTick =  0;
        }
    }

    private void shootBullet() {

        float xShoot = this.hitbox.x-180 ;
        float yShoot = this.hitbox.y + 113;
        Bullet newBullet =  new Bullet(xShoot, yShoot,145, 25, bulletSheet,-1 ,BULET_BOSS_LVL2,false);
        newBullet.initHitbox(xShoot, yShoot,145, 25);
        newBullet.setActive(true);
        newBullet.setPos(xShoot, yShoot);
        bullets.add(newBullet);
    }

    private void shootBulletSecondGun() {

        float xShoot = this.hitbox.x-180 ;
        float yShoot = this.hitbox.y + 100;
        Bullet newBullet =  new Bullet(xShoot, yShoot,64, 64, bulletSheetSecondGun,-1 ,BULET_BOSS_LVL2_SECOND,false);
        newBullet.initHitbox(xShoot, yShoot,64, 64);
        newBullet.setActive(true);
        newBullet.setPos(xShoot, yShoot);

        bullets.add(newBullet);
    }

    public void drawBullets(Graphics g) {
        for (Bullet bullet : bullets) {
            bullet.drawBullet(g);
        }
    }
    public void loadBulletAnimation() {
        try {
            BufferedImage temp = LoadSave.getInstance().getAtlas(LoadSave.BULLET_LVL3);
            this.bulletSheet = new BufferedImage[2];
            for (int i = 0; i < bulletSheet.length; i++) {
                this.bulletSheet[i] = temp.getSubimage(i * 145, 0, 145, 25);
            }
        } catch (GameException e) {
            // Handle the exception here
            System.out.println("GameException occurred:");
            System.out.println("Error Category: " + e.getErrorCategory());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Message: " + e.getMessage());
        }
    }

    public void loadBulletAnimationSecondGun() {
        try {
            BufferedImage temp = LoadSave.getInstance().getAtlas(LoadSave.BULLET_LVL3_SECONDGUN);
            this.bulletSheetSecondGun = new BufferedImage[6];
            for (int i = 0; i < bulletSheetSecondGun.length; i++) {
                this.bulletSheetSecondGun[i] = temp.getSubimage(i * 64, 0, 64, 64);
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

    public void createDatabase(){
        Database db = Database.getInstance();
        Connection connection = db.getConnection();
        try {
            Statement statement = connection.createStatement();

            // Create the table if it doesn't exist
            String createTableSql = "CREATE TABLE IF NOT EXISTS SAVES (Score INT, Lives INT, XPos INT, YPos INT)";
            statement.execute(createTableSql);

            // Insert the score, lives, and player position values into the table
            String insertDataSql = "INSERT INTO SAVES (Score, Lives, XPos, YPos) VALUES (" +
                    subject.getScore() + ", " +
                    player.getPlayerLives() + ", " +
                    player.getCameraHitbox().getX() + ", " +
                    player.getHitbox().getY()+ ")";
            try {
                statement.execute(insertDataSql);
            } catch (SQLException e) {
                // Handle the constraint violation appropriately
                System.err.println("Failed to insert score: " + e.getMessage());
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            // Handle the exception appropriately
            e.printStackTrace();
        }
        Gamestate.state = Gamestate.ENDGAME;
    }


}