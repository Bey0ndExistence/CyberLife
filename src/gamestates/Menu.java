package gamestates;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;

import Game.Game;
import UI.MenuButton;
import Utils.GameException;
import Utils.LoadSave;

import static Utils.Constants.Buttons.B_HEIGHT_DEFAULT;
import static Utils.Constants.Buttons.B_WIDTH_DEFAULT;

public class Menu extends State implements Statemethods {

    private MenuButton[] buttons = new MenuButton[3];
    private MenuButton deathPlayAgain ;
    private BufferedImage[] backgroundImg;
    private BufferedImage deathScreen;

    private boolean isDeathScreen=false;

    private Gamestate gamestate= Gamestate.PLAYING;
    public Menu(Game game) throws GameException {
        super(game);
        loadButtons();
        loadMenuBg();
        loadDeathScreen();
    }


    private void loadButtons() throws GameException {
        buttons[0]= new MenuButton(1920/2, 50*2 ,0,Gamestate.PLAYING,game);
        buttons[1]= new MenuButton(1920/2, 120*2 ,1,Gamestate.SAVE,game);
        buttons[2]= new MenuButton(1920/2, 190*2 ,2,Gamestate.QUIT,game);
        deathPlayAgain = new MenuButton(1920/2,200*3,0,Gamestate.PLAYING,game);
    }

    private void loadMenuBg() throws GameException {
        backgroundImg = new BufferedImage[2];
        BufferedImage temp = LoadSave.getInstance().getAtlas(LoadSave.MENU_BACKGROUNDS);
        for (int i = 0; i < backgroundImg.length; i++)
            backgroundImg[i] = temp.getSubimage(i * 1920, 0, 1920,1120);

    }

    private void loadDeathScreen() throws GameException{
        deathScreen = LoadSave.getInstance().getAtlas(LoadSave.DEATH_SCREEN);
    }

    @Override
    public void update() {
        for(MenuButton mb: buttons)
            mb.update();

        deathPlayAgain.update();
    }

    @Override
    public void draw(Graphics g, Graphics2D g2d) {
        for (BufferedImage menuBg : backgroundImg) {
            g2d.drawImage(menuBg, 0, -200, 1920, 1120, null);
        }

        if (getGame().getPlaying().getPlayer().getIsAlive()) {
            for (MenuButton mb : buttons)
                mb.draw(g, g2d);
        }
        else{
            g.drawImage(deathScreen,700,0,600,600,null);
            deathPlayAgain.draw(g,g2d);
            isDeathScreen = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(MenuButton mb: buttons){
            if(isIn(e,mb)){
                mb.setMousePressed(true);
            }
        }
        if(isIn(e,deathPlayAgain)){
            deathPlayAgain.setMousePressed(true);
        }

    }


    @Override
    public void mouseReleased(MouseEvent e) throws SQLException, IOException, GameException {
        for(MenuButton mb: buttons){
                if (isIn(e, mb)) {
                    if (mb.isMousePressed())
                        mb.applyGamestate();
                    break;

            }

        }

        if(isIn(e,deathPlayAgain)){
            if(deathPlayAgain.isMousePressed()){
                deathPlayAgain.applyGamestate();
                isDeathScreen = false;
            }
        }

        resetButtons();
    }

    private void resetButtons(){
        for( MenuButton mb: buttons)
            mb.resetBools();
        deathPlayAgain.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mb: buttons)
            mb.setMouseOver(false);

        if(isIn(e,deathPlayAgain)){
            deathPlayAgain.setMouseOver(true);
        }

        for(MenuButton mb: buttons)
            if(isIn(e,mb)){
                mb.setMouseOver(true);
                break;
            }

        if(isIn(e,deathPlayAgain)){
            deathPlayAgain.setMouseOver(true);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && !isDeathScreen)
            Gamestate.state = Gamestate.PLAYING3;

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public void setPrevious(Gamestate gamestate){
        this.gamestate = gamestate;
    }

    public Gamestate getGamestate() {
        return gamestate;
    }
}