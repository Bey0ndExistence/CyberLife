package Game;

import javax.swing.*;

/**
 * The GameWindow class creates a JFrame window to display a GamePanel object.
 */
public class GameWindow {
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel){
        jframe = new JFrame();
        jframe.add(gamePanel);
        jframe.pack();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        jframe.setResizable(false);
    }
}
