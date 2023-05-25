package Entities;

import Utils.GameException;
import Utils.LoadSave;
import levels.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Utils.Constants.EnemyConstants.*;
import static Utils.Constants.PlayerConstants.BULLET_TURRET;

public class Turret extends Enemy{

    BufferedImage[][] image;
    BufferedImage[] bulletSheet;

    private ArrayList<Bullet> bullets;

    private int life;

    private int shootCooldown = 230; // Cooldown in ticks (adjust as needed)
    private int shootTimer = 0; // Timer to track cooldown

    private int damage;

    public int deathTimer=0;
    public int deathCooldown = 100;

    Player player;
    Bullet bullet;
    public Turret(float x, float y, int width, int height, int enemyType, BufferedImage[][] image, Player player, int lives,int damage) throws GameException {
        super(x, y, width, height, enemyType);
        this.image = image;
        this.player = player;
        loadBulletAnimation();
        this.aniSpeed = 45;
        this.detectionDistance = 700;

        bullets = new ArrayList<Bullet>();
        this.life = lives;
        this.damage = damage;
    }

    public void loadBulletAnimation() throws GameException {

      BufferedImage temp  = LoadSave.getInstance().getAtlas(LoadSave.TURRET_BULLET);
        this.bulletSheet= new BufferedImage[3];
        for(int i=0; i< bulletSheet.length;i++) {
            this.bulletSheet[i] = temp.getSubimage(i * 32, 0, 32,32);

        }
    }

    public boolean enemy_got_damaged(){
        if(player.isSecondGun())
            life-=2;
        else
            life--;



        System.out.println("Enemy has only " + life +"remaining");
        return (life > 0);
    }



    @Override
    public void update(Camera camera,Player player,ArrayList<TilesHitBox> walls) {
        updateAnimationTick();
        updateEnemyPos(player,walls,camera);
        super.update(camera);
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

        if (playerDetected) {
            enemyState = RUNNING;
        }
        else{
            enemyState = 1;
        }

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
        if(previouAction != this.enemyState) {
            this.aniIndex = 0;
            this.aniTick =  0;
        }
    }


    private void shootBullet(Player player) {

        float turretX = this.hitbox.x ;
        float turretY = this.hitbox.y + 90;
        Bullet newBullet =  new Bullet(turretX, turretY,64,64, bulletSheet,-1 ,BULLET_TURRET,false);
        newBullet.initHitbox(turretX, turretY,64,64);
        newBullet.setActive(true);
        newBullet.setPos(turretX, turretY);

        bullets.add(newBullet);
    }
    public void drawBullets(Graphics g) {
        for (Bullet bullet : bullets) {
            bullet.drawBullet(g);
        }
    }



}
