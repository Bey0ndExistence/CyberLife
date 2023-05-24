package gamestates;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;

import Entities.EnemyManager;
import Entities.Player;
import Utils.GameException;
import levels.Camera;
import levels.LevelManager;
import Game.Game;
import Entities.ScoreSubject;

public class Playing extends State implements Statemethods {
    private LevelManager levelManager;

    private EnemyManager enemyManager;
    private Player player;
    private Camera camera;

    private ScoreSubject subject;

    private int score;




    private Menu menu;
    public Playing(Game game,Player player, Menu menu,ScoreSubject subject) throws IOException, GameException, SQLException {
        super(game);
        this.player = player;
        this.menu = menu;
        this.subject = subject;
        initClasses();
    }

    private void initClasses() {

        try {
            camera = new Camera(150, 50, 1920, 768, player);
            levelManager = new LevelManager(this, camera);
            enemyManager = new EnemyManager(subject, this, camera, player, "src/enemy_coordonates_lvl1");
            subject.addObserver(enemyManager);
        }
        catch (GameException e){
            System.out.println(e.toString());
        }
    }

    @Override
    public void update() {

        player.update(levelManager.getHitBoxes(),enemyManager.getEnemies());
        levelManager.update();
        enemyManager.update(levelManager.getHitBoxes());
       // score = enemyManager.getScore();

    }

    @Override
    public void draw(Graphics g, Graphics2D bg) {

        levelManager.drawBG(bg);
        levelManager.drawTiles(g);
        player.render(g);
        enemyManager.drawEnemies(g);
        g.setFont(new Font("Arial", Font.PLAIN,40));
        g.setColor(Color.white);
        g.drawString(  " Score: "+ subject.getScore() +" XP",20,50);
        g.drawString( "Lives: "+ player.getPlayerLives(),30,100);

    }

    public int getScore(){
        return score;
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
                menu.setPrevious(Gamestate.PLAYING);
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
        return enemyManager;
    }

    public Player getPlayer() {
        return player;
    }

}
