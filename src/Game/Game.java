package Game;

import Entities.Player;
import Entities.ScoreSubject;
import Utils.GameException;
import gamestates.*;
import gamestates.Menu;
import levels.Database;
import levels.LevelManager;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * This is a Java class that represents a game and handles the game loop, updating game objects, and
 * rendering graphics.
 */

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Menu menu;
    private  Playing playing;
    private  Playing2 playing2;
    private  Playing3 playing3;

    private Thread gameThread;

    private final int FPS_SET =144;

    private long lastCheck=0;
    private final int UPS_SET = 200;

    private Player player;

    ScoreSubject subject;

    EndGame endGame;

    public Game()  {
        initClasses();
        gamePanel = new GamePanel(this);
        this.gameWindow= new GameWindow(this.gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        System.out.println("Game running");
        startGameLoop();
    }

    private void initClasses()   {
        menu = new Menu(this);
        subject = new ScoreSubject();
        spawnPlayer();
        playing = new Playing(this,player,menu,subject);
        playing2 = new Playing2(this,player,menu,subject);
        playing3 = new Playing3(this,player,menu,subject);
        endGame = new EndGame(subject);

    }

    private void startGameLoop(){
        this.gameThread= new Thread(this);
        gameThread.start();
    }

    void spawnPlayer()  {
        try {
            File map2 = new File("src/PlayerSpawn");
            Scanner  scanner = new Scanner(map2);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                int x = Integer.parseInt(values[0]);
                int y = Integer.parseInt(values[1]);
                int width = Integer.parseInt(values[2]);
                int height = Integer.parseInt(values[3]);
                player = new Player(x, y, width, height);
            }
        }
        catch (IOException e){
            System.out.println(e.toString());
        }

    }

    public void update() {

        switch(Gamestate.state) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case PLAYING2:
                playing2.update();
                break;
            case PLAYING3:
                playing3.update();
                break;
            case ENDGAME:
                endGame.update();
                break;
            case SAVE: {
                SaveDatabase();
                System.exit(0);
            }
            break;
            case QUIT:
                System.exit(0);
                break;
            default:
                break;
        }
    }

    public void render(Graphics g, Graphics2D bg){
        switch(Gamestate.state) {
            case MENU:
                menu.draw(g,bg);
                break;
            case PLAYING:
                playing.draw(g,bg);
                break;
            case PLAYING2:
                playing2.draw(g,bg);
                break;
            case PLAYING3:
                playing3.draw(g,bg);
                break;
            case ENDGAME:
                endGame.draw(g,bg);
            default:
                break;
        }
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {

            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if(deltaF >= 1){
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                //System.out.println("FPS: " + frames + "| UPS: " + updates);
                frames = 0;
                updates =0;

            }
        }
    }

    public void resetGame()  {
        try {
            File map2 = new File("src/PlayerSpawn");
            Scanner  scanner = new Scanner(map2);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                int x = Integer.parseInt(values[0]);
                int y = Integer.parseInt(values[1]);
                int width = Integer.parseInt(values[2]);
                int height = Integer.parseInt(values[3]);
                player = new Player(x, y, width, height);
            }
            subject.removeObserver(playing.getEnemyManager());
            subject.removeObserver(playing2.getEnemyManager());
            subject.removeObserver(playing3.getEnemyManager());

            playing =  null;
            playing2 = null;
            playing3=  null;

            subject.setScore(0);
            playing = new Playing(this,player,menu,subject);
            playing2 = new Playing2(this,player,menu,subject);
            playing3 = new Playing3(this,player,menu,subject);


            player.resetLives();
        }
      catch (FileNotFoundException e){
            System.out.println("bruh");
      }


    }
    public void windowsFocusLost(){
        if(Gamestate.state == Gamestate.PLAYING);

    }
    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public Playing2 getPlaying2(){
        return  playing2;
    }
    public Playing3 getPlaying3(){
        return  playing3;
    }

    public void SaveDatabase(){
        Database db = Database.getInstance();
        Connection connection = db.getConnection();
        try {
            Statement statement = connection.createStatement();

            // Create the table if it doesn't exist
            String createTableSql = "CREATE TABLE IF NOT EXISTS SAVES_FROM_MENU (Score INT,  EnemiesLvl1 INT,  EnemiesLvl2 INT,  EnemiesLvl3 INT, Lives INT, XPos INT, YPos INT, GameState VARCHAR(255), HasSecondGun INTEGER, HeartsTook INTEGER)";

            statement.execute(createTableSql);

            // Insert the score, lives, player position, and gun type values into the table
            int hasSecondGun = player.isSecondGun() ? 1 : 0; // Convert boolean to 0 or 1
            String insertDataSql = "INSERT INTO SAVES_FROM_MENU (Score, EnemiesLvl1, EnemiesLvl2, EnemiesLvl3, Lives, XPos, YPos, GameState, HasSecondGun, HeartsTook) VALUES (" +
                    subject.getScore() + ", " +
                    playing.getEnemyManager().getEnemiesLeft() + ", " +
                    playing2.getEnemyManager().getEnemiesLeft() + ", " +
                    playing3.getEnemyManager().getEnemiesLeft()+ ", " +
                    player.getPlayerLives() + ", " +
                    player.getCameraHitbox().x + ", " +
                    player.getHitbox().y + ", '" +
                    menu.getGamestate().name() + "', " +
                    hasSecondGun + "," +
                    player.getNrHeartsTook() + " )";
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

    }



}
