package gamestates;

import Utils.GameException;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;

public interface Statemethods {
    public void update();
    public void draw(Graphics g, Graphics2D bg);
    public void mouseClicked(MouseEvent e);
    public void mousePressed(MouseEvent e);
    public void mouseReleased(MouseEvent e) throws SQLException, IOException, GameException;

    public void mouseMoved(MouseEvent e);

    public void keyPressed(KeyEvent e);

    public void keyReleased(KeyEvent e);

}
