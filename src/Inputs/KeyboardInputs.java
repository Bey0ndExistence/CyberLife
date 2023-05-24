package Inputs;

import Game.Game;
import Game.GamePanel;
import gamestates.Gamestate;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * The KeyboardInputs class implements KeyListener to handle keyboard inputs for a game panel in Java.
 */
public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;
    public KeyboardInputs(GamePanel gamePanel){
        this.gamePanel=gamePanel;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (Gamestate.state){
            case MENU:
                gamePanel.getGame().getMenu().keyPressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyPressed(e);
                break;
            case PLAYING2:
                gamePanel.getGame().getPlaying2().keyPressed(e);
                break;
            case PLAYING3:
                gamePanel.getGame().getPlaying3().keyPressed(e);
                break;
            default:
                break;
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (Gamestate.state){
            case MENU:
                gamePanel.getGame().getMenu().keyReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyReleased(e);
                break;
            case PLAYING2:
                gamePanel.getGame().getPlaying2().keyReleased(e);
            case PLAYING3:
                gamePanel.getGame().getPlaying3().keyReleased(e);
                break;
            default:
                break;
        }
    }
}