package Entities;

import Utils.GameException;
import Utils.LoadSave;
import gamestates.Gamestate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Scanner;

import static Utils.Constants.PlayerConstants.*;


/**
 * The Player class extends the Entity class and contains methods for updating and rendering the
 * player's animations and position.
 */
public class Player extends Entity{

    private BufferedImage[][] animationSheet;
    private BufferedImage[] bulletSheet;
    private BufferedImage img,bullet_img;

    private static int playerLives = 3;
    private int aniTick=0,aniIndex=0;
    private final int aniSpeed=27;
    private int playerAction = IDLE;
    private boolean moving =false;
    private boolean duck = false;

    private boolean shooting = false;
    private static boolean isAlive = true;

    private long cooldownStart = 0;
    private boolean cooldown = false;

    private boolean left, up, right ;

    private boolean inAir = false;

    private boolean goingLeft = false, goingRight = false;

    private float xspeed, yspeed;

    private Rectangle cameraHitbox;

    private boolean secondGun=false;

    private int LastCameraX,cameraXDiff;

   // private ArrayList<Bullet> bullets;

    private int pressed=0,hold=0;
    private boolean okShoot= false;

    public int deathTimer=0;
    public int deathCooldown =100;

    private int nrHeartsTook=0;

    private ArrayList<Bullet> bullets;

    // The above code is defining a Java class called "Player" which extends another class called
    // "Entity". It defines the constructor for the Player class, which takes in x and y coordinates,
    // width, and height as parameters, and initializes the hitbox and cameraHitbox variables. It also
    // defines methods for updating and rendering the player, loading animations, updating the
    // animation based on player actions, and setting player actions such as ducking, shooting, and
    // moving left or right. Additionally, it includes methods for handling collisions with walls and
    // resetting the player's position if they fall off the screen.

    public Player(float x, float y,int width, int height)  {
        super(x, y, width, height);

        try {

            loadAnimations();
            File map2 = new File("src/PlayerHitbox");
            Scanner scanner = new Scanner(map2);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                int x_hitbox = Integer.parseInt(values[0]);
                int y_hitbox = Integer.parseInt(values[1]);
                int width_hitbox = Integer.parseInt(values[2]);
                int height_hitbox = Integer.parseInt(values[3]);
                initHitbox(x_hitbox, y_hitbox, width_hitbox, height_hitbox);
            }
            cameraHitbox = new Rectangle(1200,200,200,200);

            loadBulletAnimation();
            bullets = new ArrayList<Bullet>();
        }
        catch (FileNotFoundException e){

        }


    }

    public Rectangle getCameraHitbox(){
        return cameraHitbox;
    }

    public ArrayList<Bullet> getBullets(){
        return bullets;
    }

    public void update(ArrayList<TilesHitBox> wall, ArrayList<Enemy> enemies) {
        updatePos(wall,enemies);
        UpdateAnimation();
        setAnimation();

            for (int i = bullets.size() - 1; i >= 0; i--) {
                Bullet bullet = bullets.get(i);
                bullet.update();
               for(int j = 0;j < enemies.size();++j )
                if (bullet.getHitbox().getX() < 1920) {
                        if (bullet.getHitbox().intersects(enemies.get(j).getHitbox())) {
                            bullet.setActive(false);
                            if (enemies.get(j) instanceof Turret) {
                                if (!((Turret) enemies.get(j)).enemy_got_damaged()) {
                                    enemies.remove(enemies.get(j));

                                }
                            } else if (enemies.get(j) instanceof Cop) {
                                if (!((Cop) enemies.get(j)).enemy_got_damaged())
                                    enemies.remove(enemies.get(j));
                            } else if (enemies.get(j) instanceof FinalBoss) {
                                if (((FinalBoss) enemies.get(j)).isBossAlive())
                                    ((FinalBoss) enemies.get(j)).enemy_got_damaged();
                            } else if (enemies.get(j) instanceof FinalBoss2) {
                                if (((FinalBoss2) enemies.get(j)).isBossAlive()) {
                                    ((FinalBoss2) enemies.get(j)).enemy_got_damaged();
                                }
                            } else if (enemies.get(j) instanceof FinalBoss3) {
                                if (((FinalBoss3) enemies.get(j)).isBossAlive()) {
                                    ((FinalBoss3) enemies.get(j)).enemy_got_damaged();
                                }
                            }


                        if (!bullet.isActive()) {
                            bullets.remove(bullets.get(i));
                        }
                    }

                }
               else {
                    bullets.remove(bullet);
                }
        }

    }

    public void render(Graphics g){
        try {
            g.drawImage(this.animationSheet[playerAction][this.aniIndex], (int) x - 20, (int) y - 50, width, height, null);
           // drawHitbox(g);
            //g.drawRect(hitbox.x,hitbox.y,width,height);
            drawBullets(g);
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("ceva outofbounds");
        }

    }

    public void drawBullets(Graphics g) {
        try {
            for (Bullet bullet : bullets) {
                bullet.drawBullet(g);
            }
        }
        catch (ConcurrentModificationException e){
            System.out.println("bullets drawn bad from player");
        }

    }

    public void resetLives(){
        playerLives =3;
    }




    public void loadAnimations() {
        try {
            this.img = LoadSave.getInstance().getAtlas(LoadSave.PLAYER_ATLAS);

            this.animationSheet = new BufferedImage[14][8];
            for (int i = 0; i < animationSheet.length; i++)
                for (int j = 0; j < animationSheet[i].length; j++) {
                    this.animationSheet[i][j] = this.img.getSubimage(j * 240, i * 240, 240, 240);

                }
        }
        catch (GameException e){
            System.out.println(e.toString());
        }
    }
    public void loadBulletAnimation()  {

        try {
            this.bullet_img =LoadSave.getInstance().getAtlas(LoadSave.BULLETS);
            this.bulletSheet= new BufferedImage[4];
            for(int i=0; i< bulletSheet.length;i++) {
                this.bulletSheet[i] = this.bullet_img.getSubimage(i * 60, 0, 60,15);

            }
        }
        catch (GameException e){
            System.out.println(e.toString());
        }

    }

    public void shoot() {
        float xShoot = this.hitbox.x+200-20;
        float yShoot = this.hitbox.y+100-50;
        Bullet newBullet = new Bullet(xShoot, yShoot,60,15, bulletSheet,1,BULLET_PLAYER,true);
        newBullet.initHitbox(xShoot,yShoot,60,15);
        newBullet.setActive(true);
        newBullet.setPos(xShoot, yShoot);
        bullets.add(newBullet);
    }

    public static void damage(int damage) {
        playerLives-=damage;
        if(!(playerLives<0))
            System.out.println("You got only " + playerLives + " left");
        if (playerLives <= 0) {
            Gamestate.state = Gamestate.MENU;
            isAlive = false;
        }
    }

    public static boolean getIsAlive(){
        return isAlive;
    }


    public void UpdateAnimation() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            if (!duck) {
                aniIndex++;
                if (aniIndex >= GetSpriteAmount(playerAction)) {
                    aniIndex = 0;
                }
            } else {

                if (cooldown) {
                    // pause the animation on the last frame

                    if (aniIndex >= GetSpriteAmount(DUCK) - 1) {
                        aniIndex = GetSpriteAmount(DUCK) - 1;
                    }
                    else{
                        aniIndex++;
                    }
                    // check if cooldown is over
                    if (System.currentTimeMillis() - cooldownStart >= 500) {
                        cooldown = false;
                    }
                } else {
                    aniIndex++;
                    if (aniIndex >= GetSpriteAmount(DUCK)) {
                        aniIndex = 0;
                        this.duck = false;
                    }
                }
            }
        }
    }

    public int getLastCameraX() {
        return LastCameraX;
    }

    public int getNrHeartsTook(){
        return nrHeartsTook;
    }
    public void setDucking(){
        if (!cooldown) {
            this.duck = true;
            this.shooting = false;
            this.moving=false;
            this.cooldown = true;
            this.cooldownStart = System.currentTimeMillis();
        }
    }

    public void setShooting(){
        this.shooting =true;
    }

    public void setNonShooting(){
        this.shooting =false;
    }

    private void updatePos(ArrayList<TilesHitBox> walls, ArrayList<Enemy> enemies){
        this.moving = false;
        TilesHitBox wall= null;

        if(left && right || !left && !right) xspeed *= 0.8;
        else if (left && !right && !inAir) {
            this.moving = true;
            xspeed--;
        }
        else if (right && !left && !inAir) {
            this.moving = true;
            xspeed++;

        }

        if(xspeed > 0 && xspeed < 0.25) xspeed =0;
        if(xspeed < 0 && xspeed > -0.25) xspeed =0;

        if(xspeed > 1) xspeed = 1;
        if(xspeed < -1) xspeed = -1;


        Iterator<TilesHitBox> iter;
        if(up){
            this.moving = false;
            this.inAir = true;
            hitbox.y++;
            iter = walls.iterator();
            while (iter.hasNext()) {
                wall = iter.next();
                if (wall.getHitbox().intersects(getHitbox())) {
                    yspeed = -3;

                }
            }
            hitbox.y--;
        }

        yspeed += 0.035;

        LastCameraX = cameraHitbox.x;
        // Horizontal Collision

        boolean ok = true;
        if (xspeed > 0) { // move to the right
            float tilesDirection = - xspeed;
            iter = walls.iterator();
            while (iter.hasNext()) {
                wall = iter.next();
                for (Enemy enemy : enemies) {
                    // Check if the player's next position will collide with the enemy's hitbox
                    if (tilesDirection + wall.getHitbox().width >= enemy.getHitbox().x  &&
                            wall.getHitbox().y < enemy.getHitbox().y + enemy.getHitbox().height &&
                            wall.getHitbox().y + wall.getHitbox().height > enemy.getHitbox().y) {

                        // Calculate the new position for both the player and the enemy based on the direction of the tiles
                        int newEnemyX = enemy.getHitbox().x + (int) tilesDirection;

                        // Move the player and the enemy to their respective new positions

                        enemy.setHitbox_X(newEnemyX);
                    }
                }

                if (hitbox.x + hitbox.width + xspeed >= wall.getHitbox().x &&
                        hitbox.x + hitbox.width <= wall.getHitbox().x &&
                        hitbox.y < wall.getHitbox().y + wall.getHitbox().height &&
                        hitbox.y + hitbox.height > wall.getHitbox().y) {
                    if (wall instanceof Guns) {
                        ((Guns) wall).isVisible = false;
                        ((Guns) wall).nrGunsTook();
                        secondGun = true;
                        iter.remove(); // remove element using iterator

                    }
                    else if(wall instanceof Heart){
                        ((Heart) wall).isVisible = false;
                        nrHeartsTook++;
                        playerLives++;
                        iter.remove();
                    }
                    else {
                        ok = false;
                        break;
                    }
                }
            }
            if (ok) {
                cameraHitbox.x += xspeed;
            }
        } else if (xspeed < 0) { // move to the left
            float tilesDirection = - xspeed;
            iter= walls.iterator();
            while (iter.hasNext()) {
                wall = iter.next();

                for (Enemy enemy : enemies) {

                    if (tilesDirection + wall.getHitbox().width >= enemy.getHitbox().x  &&
                            wall.getHitbox().y < enemy.getHitbox().y + enemy.getHitbox().height &&
                            wall.getHitbox().y + wall.getHitbox().height > enemy.getHitbox().y) {

                        // Calculate the new position for both the player and the enemy based on the direction of the tiles
                        int newEnemyX =  enemy.getHitbox().x + (int) tilesDirection;
                        if(enemy instanceof Cop)
                            ((Cop) enemy).setMovingRight();

                        // Move the player and the enemy to their respective new positions

                        enemy.setHitbox_X(newEnemyX);
                    }
                }


                if (hitbox.x + xspeed <= wall.getHitbox().x + wall.getHitbox().width &&
                        hitbox.x >= wall.getHitbox().x + wall.getHitbox().width &&
                        hitbox.y < wall.getHitbox().y + wall.getHitbox().height &&
                        hitbox.y + hitbox.height > wall.getHitbox().y)  {

                                    if (wall instanceof Guns) {
                                        ((Guns) wall).isVisible = false;
                                        ((Guns) wall).nrGunsTook();
                                        secondGun = true;
                                        iter.remove(); // remove element using iterator
                                    }
                                    else if(wall instanceof Heart){
                                        ((Heart) wall).isVisible = false;
                                        ((Heart) wall).nrHearsTook();
                                        playerLives++;
                                        iter.remove();
                                    }
                                    else {
                                        ok = false;
                                        break;
                                    }
                }
            }
            if (ok) {
                cameraHitbox.x += xspeed;
            }
        }

        hitbox.y += yspeed;
        iter = walls.iterator();
        while (iter.hasNext()){
            wall = iter.next();
            if (getHitbox().intersects(wall.getHitbox())) {
                if(wall instanceof Guns ){
                    ((Guns) wall).isVisible = false;
                    ((Guns) wall).nrGunsTook();
                    secondGun =true;
                    iter.remove();
                }
                else if(wall instanceof Heart){
                    ((Heart) wall).isVisible = false;
                    ((Heart) wall).nrHearsTook();
                    playerLives++;
                    iter.remove();
                }
                else{
                    hitbox.y -= yspeed;
                    while (!wall.getHitbox().intersects(getHitbox()))
                        hitbox.y += Math.signum(yspeed);
                    hitbox.y -= Math.signum(yspeed);
                    okShoot = true;
                    yspeed = 0;
                    inAir = false;
                }
            }
            else{
                okShoot= false;
            }
            y = hitbox.y;

        }

        y += yspeed;
        hitbox.y = (int) y ;

      //  System.out.println("y din updatePos "+ y);
        if(y>800){
            playerReset();
        }

      //  System.out.println(cameraHitbox.x);
    }

    private void playerReset(){
        cameraHitbox.x = 1200;
        hitbox.y = 0;
        y = hitbox.y;
        xspeed=0;
        yspeed=0;

    }
    public boolean isSecondGun(){
        return secondGun;
    }

    public void setRight(boolean right) {
        this.right = right;
    }


    public void setUp(boolean up) {
        this.up = up;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }


    public void setGoingLeft(){
        goingLeft= true;
        goingRight =false;
    }

    public void setGoingRight(){
        goingRight = true;
        goingLeft = false;
    }


    private void setAnimation() {
        int previousPlayerAction = this.playerAction;
        pressed=0;

        if(moving) {
            if(goingRight)
                this.playerAction = RUN;
            else
                this.playerAction = RUN_LEFT;
        }
        else if(this.duck) {
            this.playerAction = DUCK;
        }
        else if(this.inAir){
            if((cameraHitbox.x - LastCameraX) > 0)
                this.playerAction = JUMP;
            else if((cameraHitbox.x - LastCameraX) <0)
                this.playerAction = JUMP_LEFT;
            else
                this.playerAction =JUMP;
        }
        else if(this.shooting){
            pressed =1;
            if(hold==0) {
                shoot();
            }

            if(!secondGun)
                this.playerAction = SHOOT1;
            else
                this.playerAction = SHOOTcombuster;
        }
        else if(!isAlive){

            this.playerAction = HURT;
            y -= 2;
            hitbox.y = (int) y ;

            if(deathTimer > deathCooldown){
                this.setAlive(true);
                deathTimer=0;
            }
            deathTimer++;
        }
        else {
            this.playerAction = IDLE;
        }

        if(previousPlayerAction != this.playerAction) {
            this.aniIndex = 0;
            this.aniTick =  0;
        }

        hold = pressed;

    }


        public int getPlayerLives(){
            return playerLives;
        }
      public void LvlSpawn(int value){
        cameraHitbox.x = value;
      }

    void setAlive(boolean alive){
        this.isAlive = alive;
    }
}
