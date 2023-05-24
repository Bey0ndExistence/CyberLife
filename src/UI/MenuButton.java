package UI;


import gamestates.Gamestate;
import Game.Game;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;

import  Utils.*;
import static Utils.Constants.Buttons.*;

public class MenuButton {

    private Gamestate state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;
    private int xPos, yPos, rowIndex, index;

    private int xOffsetCenter = B_WIDTH / 2;
    private Game game;
    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state,Game game) throws GameException {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
        this.game = game;
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH*2, B_HEIGHT*2);

    }

    public Gamestate getState(){
        return state;
    }

    private void loadImgs() throws GameException {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.getInstance().getAtlas(LoadSave.BUTTONS);
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
    }

    public void draw(Graphics g,Graphics2D g2d) {
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH*2, B_HEIGHT*2, null);
    }

    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
    }
    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void applyGamestate() throws SQLException, IOException, GameException {
        if(state== Gamestate.PLAYING)
            game.resetGame();
        Gamestate.state = state;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }


}
