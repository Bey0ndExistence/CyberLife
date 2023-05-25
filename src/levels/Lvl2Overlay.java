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
import java.util.ConcurrentModificationException;
import java.util.Scanner;

public class Lvl2Overlay {
    private ArrayList<TilesHitBox> palmTrees;
    private BufferedImage palmTreeImage;

    public Lvl2Overlay() {
        try {
            palmTrees = new ArrayList<>();
            // Add palm trees to the list
            palmTreeImage = LoadSave.getInstance().getAtlas(LoadSave.PALMIER);

            File Map = new File("src/Lvl2OverlayMap");
            Scanner scanner = new Scanner(Map);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                int x = Integer.parseInt(values[0]);
                int y = Integer.parseInt(values[1]);
                int width = Integer.parseInt(values[2]);
                int height = Integer.parseInt(values[3]);
                palmTrees.add(new TilesHitBox(x, y, width, height, palmTreeImage));
            }

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
        try {
            for (TilesHitBox palmTree : palmTrees) {
                palmTree.draw(g);
            }
        }
        catch (ConcurrentModificationException e){
            System.out.println("ceva e not okei la Lvl2Overlay draw");
        }
    }
}