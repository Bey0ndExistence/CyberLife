package Game;

import Inputs.KeyboardInputs;
import Inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;




/**
 * The GamePanel class extends JPanel and sets the panel size, adds keyboard inputs, and renders the
 * game.
 */
public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private BufferedImage[][] animationSheet;
    private BufferedImage img;

    private Game game;
    public GamePanel(Game game) throws IOException {
        mouseInputs = new MouseInputs(this);
        this.game=game;
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
        setPanelSize();

    }

    private void setPanelSize() {
        Dimension size = new Dimension(1920,768);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D bg = (Graphics2D) g;
        game.render(g,bg);
    }

    public Game getGame(){
        return this.game;
    }

}
