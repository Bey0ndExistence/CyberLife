package levels;

import Entities.TilesHitBox;
import Utils.GameException;
import Utils.LoadSave;
import levels.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Lvl2Overlay {
    private ArrayList<TilesHitBox> palmTrees;
    private BufferedImage palmTreeImage;

    public Lvl2Overlay() {
        try {
            palmTrees = new ArrayList<>();
            // Add palm trees to the list
            palmTreeImage = LoadSave.getInstance().getAtlas(LoadSave.PALMIER);
            palmTrees.add(new TilesHitBox(600,250,399,624,palmTreeImage));
            //palmTrees.add(new TilesHitBox(750,150,399,624, palmTreeImage));
            palmTrees.add(new TilesHitBox(1800,220,399,624, palmTreeImage));
            palmTrees.add(new TilesHitBox(3500,145,399,624, palmTreeImage));
            // Add more palm trees as needed

            File mapHitbox = new File("src/Lvl2OverlayHitboxes");
            Scanner scannerHitbox = new Scanner(mapHitbox);
            int index = 0;
            while (scannerHitbox.hasNextLine() && index < palmTrees.size()) {
                String line = scannerHitbox.nextLine();
                String[] values = line.split(",");
                int x = Integer.parseInt(values[0]);
                int y = Integer.parseInt(values[1]);
                int width = Integer.parseInt(values[2]);
                int height = Integer.parseInt(values[3]);
                palmTrees.get(index).initHitbox(x, y, width, height);
                index++;
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Nu s-a gasit Lvl2OverlayHitboxes");
        }
        catch (GameException e){
            System.out.println(e.toString());
        }

    }

    public void update(Camera camera) {
        for (TilesHitBox palmTree : palmTrees) {
            palmTree.update(camera);
        }
    }

    public void draw(Graphics g) {
        for (TilesHitBox palmTree : palmTrees) {
            palmTree.draw(g);
        }
    }
}