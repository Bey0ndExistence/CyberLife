package Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.io.BufferedReader;

import Utils.GameException;
import gamestates.Playing;
import Utils.LoadSave;
import gamestates.Playing2;
import gamestates.Playing3;
import levels.Camera;

import static Utils.Constants.EnemyConstants.*;

public class EnemyManager implements Observer{
    private Playing playing;
    private Playing2 playing2;

    private Playing3 playing3;
    private BufferedImage[][] cop,turret,boss_one,boss_two,boss_three;
    private BufferedImage portal;
    private ArrayList<Enemy> enemies = new ArrayList<>();

    private Camera camera;

    private Player player;

    private Portal portalLvl3,   portalLvl2,portalLvl1;

    protected int enemiesKilled = 0;
    protected int score  = 0;
    protected int previusSize;

    private ScoreSubject subject;



    private boolean bossScoreUpdated1 = false;
    private boolean bossScoreUpdated2 = false;
    private boolean bossScoreUpdated3 = false;

    public EnemyManager(ScoreSubject subject, Playing playing,Camera camera,Player player,String filePath) throws GameException {
        this.playing = playing;
        this.camera =camera;
        this.player= player;
        this.subject = subject;
        loadCop();
        loadTurret();
        loadBossLevelOne();
        addEnemiesFromFile(filePath);
        previusSize = enemies.size();
        portalLvl1 = new Portal(1);


    }

    public EnemyManager(ScoreSubject subject,Playing2 playing2,Camera camera,Player player,String filePath, int score) throws GameException {
        this.playing2 = playing2;
        this.camera =camera;
        this.player= player;
        this.score = score;
        this.subject = subject;
        loadCop();
        loadTurret();
        loadBossLevelTwo();
        loadBossLevelThree();
        addEnemiesFromFile(filePath);
        previusSize = enemies.size();
        portalLvl2 = new Portal(2);


    }

    public EnemyManager(ScoreSubject subject,Playing3 playing3,Camera camera,Player player,String filePath, int score) throws GameException {
        this.playing3 = playing3;
        this.camera =camera;
        this.player= player;
        this.score = score;
        this.subject = subject;
        loadCop();
        loadTurret();
        loadBossLevelThree();
        addEnemiesFromFile(filePath);
        previusSize = enemies.size();
        portalLvl3 = new Portal(2);

    }

    public int getEnemiesLeft(){
        return enemies.size();
    }


    public void addEnemiesFromFile(String filePath) throws GameException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parameters = line.split(" ");
                int damage = Integer.parseInt(parameters[0]);
                int lives =  Integer.parseInt(parameters[1]);
                int x = Integer.parseInt(parameters[2]);
                int y = Integer.parseInt(parameters[3]);
                int width = Integer.parseInt(parameters[4]);
                int height = Integer.parseInt(parameters[5]);
                String enemyType = parameters[6];


                switch (enemyType) {
                    case "COP":
                        enemies.add(new Cop(x, y, width, height, COP, cop, player,lives,camera));
                        break;
                    case "TURRET":
                        enemies.add(new Turret(x, y, width, height, TURRET, turret,player,lives,damage));
                        break;
                    case "FINALBOSS":
                        enemies.add(new FinalBoss(x , y, width, height, BOSS_1, boss_one, player,lives,damage));
                        break;
                    case "FINALBOSS2":
                        enemies.add(new FinalBoss2(x, y, width, height, BOSS_2, boss_two, player, lives, damage));
                        break;
                    case "FINALBOSS3":
                        enemies.add(new FinalBoss3(x,y,width,height,BOSS_3,boss_three,player,lives,damage,subject));
                        //throw new GameException();
                    default:
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void update(ArrayList<TilesHitBox> walls) {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (enemy instanceof Turret) {
                ((Turret) enemy).update(camera, player, walls);
            } else if(enemy instanceof FinalBoss){
                ((FinalBoss)enemy).update(camera,player,walls);
                if(!((FinalBoss) enemy).isBossAlive() && !bossScoreUpdated1){
                    System.out.println("You just Killed the First Boss!");
                    subject.setScore(score+20);
                    System.out.println("Your score is: " + score + " XP");
                    bossScoreUpdated1 = true;
                }
            }else if(enemy instanceof Cop){
                ((Cop)enemy).update(camera,player,walls);
            }
            else if(enemy instanceof FinalBoss2){
                ((FinalBoss2)enemy).update(camera,player,walls);
                if(!((FinalBoss2) enemy).isBossAlive() && !bossScoreUpdated2){
                    System.out.println("You just Killed the Second Boss!");
                    subject.setScore(score+40);
                    System.out.println("Your score is: " + score + " XP");
                    bossScoreUpdated2 = true;
                }
            }
            else if(enemy instanceof FinalBoss3){
                ((FinalBoss3)enemy).update(camera,player,walls);
                if(!((FinalBoss3) enemy).isBossAlive() && !bossScoreUpdated3){
                    System.out.println("You just Killed the Final Boss!");
                    subject.setScore(score+60);
                    System.out.println("Your score is: " + score + " XP");
                    bossScoreUpdated3 = true;
                }
            }
            else
            {
                enemy.update(camera, player, walls);
            }
        }
        if(enemies.size() < previusSize ){
            System.out.println("You killed an Enemy!");
            subject.setScore(score+10);
            System.out.println("Your score is: " + score + " XP");
            previusSize = enemies.size();
        }
    }


    public void drawEnemies(Graphics g) {
        try {
            for (Enemy c : enemies) {
                if (c instanceof Cop)
                    g.drawImage(cop[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x - 50, (int) c.getHitbox().y, c.getWidth(), c.getHeight(), null);
                else if (c instanceof Turret) {

                    g.drawImage(turret[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x, (int) c.getHitbox().y, c.getWidth(), c.getHeight(), null);
                    ((Turret) c).drawBullets(g);
                } else if (c instanceof FinalBoss) {
                    // System.out.println(((FinalBoss) c).isBossAlive());
                    if (((FinalBoss) c).isBossAlive()) {
                        g.drawImage(boss_one[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x - 100, (int) c.getHitbox().y, c.getWidth(), c.getHeight(), null);
                        ((FinalBoss) c).drawBullets(g);
                    } else {
                        //  g.drawImage(portal,c.getHitbox().x-50 ,c.getHitbox().y,176,196,null);
                        portalLvl1.drawPortal(g, c.getHitbox().x - 100, c.getHitbox().y, c.getWidth() - 100, c.getHeight() - 50);
                        portalLvl1.updateAnimation();
                    }
                } else if (c instanceof FinalBoss2) {
                    if (((FinalBoss2) c).isBossAlive()) {
                        g.drawImage(boss_two[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x - 200, (int) c.getHitbox().y - 200, c.getWidth() * 2, c.getHeight() * 2, null);
                        ((FinalBoss2) c).drawAttack(g);
                    } else {
                        portalLvl2.drawPortal(g, c.getHitbox_X() - 120, c.getHitbox().y, c.getWidth() * 2, c.getHeight());
                        portalLvl2.updateAnimation();
                    }
                } else if (c instanceof FinalBoss3) {
                    if (((FinalBoss3) c).isBossAlive()) {

                        g.drawImage(boss_three[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x - 200, (int) c.getHitbox().y, c.getWidth() * 2, c.getHeight(), null);
                        ((FinalBoss3) c).drawBullets(g);
                    } else {
                        portalLvl3.drawPortal(g, c.getHitbox_X() - 80, c.getHitbox().y, c.getWidth() * 2, c.getHeight());
                        portalLvl3.updateAnimation();
                    }
                }


                c.drawHitbox(g);
            }
        }
        catch (ConcurrentModificationException e){
            e.getStackTrace();
        }
    }

    private void loadCop() throws GameException {
        cop = new BufferedImage[3][10];
        BufferedImage temp = LoadSave.getInstance().getAtlas(LoadSave.COP);
        for (int j = 0; j < cop.length; j++)
            for (int i = 0; i < cop[j].length; i++)
                cop[j][i] = temp.getSubimage(i * 183, j * 192, 183, 192);
    }

    private void loadTurret() throws GameException {
        turret = new BufferedImage[2][4];
        BufferedImage temp = LoadSave.getInstance().getAtlas(LoadSave.TURRET);
        for (int j = 0; j < turret.length; j++)
            for (int i = 0; i < turret[j].length; i++)
                turret[j][i] = temp.getSubimage(i * 132, j * 186, 132, 186);
    }

    private void loadBossLevelOne() throws GameException {
        boss_one = new BufferedImage[4][9];
        BufferedImage temp = LoadSave.getInstance().getAtlas(LoadSave.BOSS_LVL1);
        for (int j = 0; j < boss_one.length; j++)
            for (int i = 0; i < boss_one[j].length; i++)
                boss_one[j][i] = temp.getSubimage(i * 412, j * 264, 412, 264);
    }

    private void loadBossLevelTwo() throws GameException {
        boss_two = new BufferedImage[5][8];
        BufferedImage temp = LoadSave.getInstance().getAtlas(LoadSave.BOSS_LVL2);
        for (int j = 0; j < boss_two.length; j++) {
            for (int i = boss_two[j].length - 1; i >= 0; i--) {
                boss_two[j][boss_two[j].length - 1 - i] = temp.getSubimage(i * 320, j * 192, 320, 192);
            }
        }
    }

    private void loadBossLevelThree() throws GameException {
        boss_three = new BufferedImage[8][13];
        BufferedImage temp = LoadSave.getInstance().getAtlas(LoadSave.LVL3_BOSS);
        for (int j = 0; j < boss_three.length; j++) {
            for (int i = boss_three[j].length - 1; i >= 0; i--) {
                boss_three[j][boss_three[j].length - 1 - i] = temp.getSubimage(i * 250, j * 250, 250, 250);
            }
        }
    }


    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }

    @Override
    public void onScoreUpdate(int score) {
        this.score = score;
        // Perform any necessary actions when the score updates
        System.out.println("Score updated: " + score + " XP");    }
}
