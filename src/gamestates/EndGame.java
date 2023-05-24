package gamestates;

import javax.security.auth.Subject;
import java.awt.*;
import java.awt.image.BufferedImage;

import Entities.ScoreSubject;
import Utils.GameException;
import Utils.LoadSave;

public class EndGame {

    ScoreSubject subject;

    BufferedImage[] backgroundImg;
    public EndGame(ScoreSubject subject) throws GameException {
            this.subject = subject;
            loadMenuBg();
    }

    public void update() {


    }

    public void draw(Graphics g,Graphics2D g2d){

        for (BufferedImage menuBg : backgroundImg) {
            g2d.drawImage(menuBg, 0, -200, 1920, 1120, null);
        }

        g.setFont(new Font("Arial", Font.PLAIN,200));
        g.setColor(Color.white);
        g.drawString("VICTORY " ,550,400);
        g.setFont(new Font("Arial", Font.PLAIN,100));
        g.drawString("Score: " + subject.getScore() + " XP" ,700,600);

    }

    private void loadMenuBg() throws GameException {
        backgroundImg = new BufferedImage[2];
        BufferedImage temp = LoadSave.getInstance().getAtlas(LoadSave.MENU_BACKGROUNDS);
        for (int i = 0; i < backgroundImg.length; i++)
            backgroundImg[i] = temp.getSubimage(i * 1920, 0, 1920,1120);

    }
}
