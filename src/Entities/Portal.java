package Entities;

import Utils.GameException;
import Utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Portal {
    BufferedImage[] frames;
    private int aniTick,aniSpeed=25,aniIndex;

    Portal(int choice) throws GameException {
        if(choice==2)
            loadPortalLvl2();
        else if(choice==1)
            loadPortalLvl1();
        updateAnimation();

    }
    public void updateAnimation() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= 4) {
                aniIndex = 0;
            }
        }
    }

    public  void loadPortalLvl2() throws GameException {
        BufferedImage temp = LoadSave.getInstance().getAtlas(LoadSave.PORTAL_LVL2);
        this.frames = new BufferedImage[8];
        for (int i = 0; i < frames.length; i++) {
            this.frames[i] = temp.getSubimage(i * 64, 0, 64, 59);
        }
    }

    public  void loadPortalLvl1() throws GameException {
        BufferedImage temp =LoadSave.getInstance().getAtlas(LoadSave.PORTAL);
        this.frames = new BufferedImage[8];
        for (int i = 0; i < frames.length; i++) {
            this.frames[i] = temp.getSubimage(i * 64, 0, 64, 48);
        }
    }

    public void drawPortal(Graphics g, float x, float y, int width, int height){
            g.drawImage(frames[aniIndex], (int) x, (int) y, width,height, null);
    }

}
