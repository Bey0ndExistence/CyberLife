package gamestates;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import Entities.EnemyManager;
import Entities.Player;
import Entities.ScoreSubject;
import Utils.GameException;
import Utils.LoadSave;
import levels.Camera;
import levels.*;
import Game.Game;

public class Playing2 extends State implements Statemethods {
    private LevelManager2 levelManager2;

    private EnemyManager enemyManager2;

    private Lvl2Overlay lvl2Overlay;
    private Player player;

    private Camera camera;

    private int score;

    private ScoreSubject subject;
    BufferedImage heart;

    private Menu menu;
    public Playing2(Game game,Player player,Menu menu,ScoreSubject subject) {
        super(game);
        this.player = player;
        this.subject = subject;
        this.menu = menu;
        initClasses();

    }

    private void initClasses()  {

        try {
            camera = new Camera(150, 50, 1920, 768, player);
            levelManager2 = new LevelManager2(this, camera);
            enemyManager2 = new EnemyManager(subject, this, camera, player, "src/enemy_coordonates_lvl2", score);
            lvl2Overlay = new Lvl2Overlay();
            subject.addObserver(enemyManager2);
            BufferedImage hearts = LoadSave.getInstance().getAtlas(LoadSave.HEARTS);
            heart = hearts.getSubimage(2, 0, 30, 28);
        }
        catch (GameException e){
            System.out.println(e);
        }

    }

    @Override
    public void update() {

        player.update(levelManager2.getHitBoxes(),enemyManager2.getEnemies());
        levelManager2.update();
        enemyManager2.update(levelManager2.getHitBoxes());
        lvl2Overlay.update(camera);

    }

    @Override
    public void draw(Graphics g, Graphics2D bg) {
        levelManager2.drawBG(bg);
        levelManager2.drawTiles(g);
        player.render(g);
        enemyManager2.drawEnemies(g);
        lvl2Overlay.draw(g);
        g.setFont(new Font("Arial", Font.PLAIN,40));
        g.setColor(Color.white);
        g.drawString(  " Score: "+ subject.getScore() +" XP",20,50);
        g.drawString( "Lives: ",30,100);

        int heartWidth = 30 * 2; // Width of each heart image
        int heartSpacing = 10; // Adjust the spacing between hearts
        int startX = 150; // Starting X position for the first heart

        for (int i = 1; i <= player.getPlayerLives(); i++) {
            int heartX = startX + (i - 1) * (heartWidth + heartSpacing); // Calculate the X position for each heart
            g.drawImage(heart, heartX, 55, heartWidth, 28 * 2, null);
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                player.setUp(true);
                break;
            case KeyEvent.VK_A:
                player.setLeft(true);
                player.setGoingLeft();
                break;
            case KeyEvent.VK_S:
                player.setDucking();
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                player.setGoingRight();
                break;
            case KeyEvent.VK_SPACE:
                player.setShooting();
                break;
            case KeyEvent.VK_BACK_SPACE:
                Gamestate.state= Gamestate.MENU;
                menu.setPrevious(Gamestate.PLAYING2);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                player.setUp(false);
                break;
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_S:
                // gamePanel.getGame().getPlayer().setNonDucking();
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setNonShooting();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }
    public EnemyManager getEnemyManager(){
        return enemyManager2;
    }

    public Player getPlayer() {
        return player;
    }

}
