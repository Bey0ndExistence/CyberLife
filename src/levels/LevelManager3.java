package levels;



import Entities.Guns;
import Entities.Heart;
import Entities.TilesHitBox;
import Entities.TilesHitBoxAnimated;
import Game.Game;
import Utils.GameException;
import Utils.LoadSave;

import gamestates.Playing3;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Scanner;

public class LevelManager3 {

    Playing3 playing3;
    private BufferedImage map, big_computer,small_terminal,elevator,cryo_pod,server_cabinetes,  BG_CLOSE;
    private Camera camera;
    ArrayList<BackgroundLayer> backgroundLayers;


    private ArrayList<TilesHitBox> hitBoxes;

    private BufferedImage heart;

    LoadSave singleton;
    public LevelManager3(Playing3 playing3, Camera camera)  {
        this.playing3= playing3;
        this.camera = camera;
        importTileSprites();
        LoadTileHitboxes();

    }

    public ArrayList<TilesHitBox> getHitBoxes(){
        return hitBoxes;

    }
    private void importTileSprites() {

        try {
            map = LoadSave.getInstance().getAtlas(LoadSave.LVL3_MAP);

            big_computer = LoadSave.getInstance().getAtlas(LoadSave.LVL3_BIG_COMPUTER);
            small_terminal = LoadSave.getInstance().getAtlas(LoadSave.LVL3_TERMINAL);
            elevator = LoadSave.getInstance().getAtlas(LoadSave.LVL3_ELEVATOR);
            cryo_pod = LoadSave.getInstance().getAtlas(LoadSave.LVL3_CRYO_POD);
            server_cabinetes = LoadSave.getInstance().getAtlas(LoadSave.LVL3_CABINS);


            BG_CLOSE = LoadSave.getInstance().getAtlas(LoadSave.LVL3_BG);

            BufferedImage hearts = LoadSave.getInstance().getAtlas(LoadSave.HEARTS);
            heart = hearts.getSubimage(2, 0, 30, 28);
        }
        catch (GameException e){
            System.out.println(e.toString());
        }

    }

    public void LoadTileHitboxes() {

        try {
            File backgroundLayerFile = new File("src/backgroundLayer3");
            Scanner scanner = new Scanner(backgroundLayerFile);
            backgroundLayers = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                float parallaxFactor = Float.parseFloat(values[0]);
                int x = Integer.parseInt(values[1]);
                int y = Integer.parseInt(values[2]);
                backgroundLayers.add( new BackgroundLayer(BG_CLOSE, parallaxFactor,x,y));

            }


            this.hitBoxes = new ArrayList<>();

            File map3 = new File("src/MapLvl3");
            scanner = new Scanner(map3);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                int type = Integer.parseInt(values[0]);
                int x = Integer.parseInt(values[1]);
                int y = Integer.parseInt(values[2]);
                int width = Integer.parseInt(values[3]);
                int height = Integer.parseInt(values[4]);

                if (type == 1) {
                    hitBoxes.add(new TilesHitBox(x, y, width, height, map));
                } else if (type == 2) {
                    hitBoxes.add(new TilesHitBox(x, y, width, height, big_computer));
                } else if (type == 3) {
                    hitBoxes.add(new TilesHitBox(x, y, width, height,small_terminal ));
                } else if (type == 4) {
                    hitBoxes.add(new TilesHitBox(x, y, width, height, elevator));
                } else if (type == 5) {
                    hitBoxes.add(new TilesHitBox(x, y, width, height, cryo_pod));
                } else if (type == 6) {
                    hitBoxes.add(new TilesHitBox(x, y, width, height, server_cabinetes));
                }
                else if(type == 7){
                    hitBoxes.add(new Heart(x,y, width,height,heart));
                }
            }


            File mapHitbox = new File("src/MapHitboxLvl3");
            Scanner scannerHitbox = new Scanner(mapHitbox);
            int index = 0;
            while (scannerHitbox.hasNextLine() && index < hitBoxes.size()) {
                String line = scannerHitbox.nextLine();
                String[] values = line.split(",");
                int x = Integer.parseInt(values[0]);
                int y = Integer.parseInt(values[1]);
                int width = Integer.parseInt(values[2]);
                int height = Integer.parseInt(values[3]);
                hitBoxes.get(index).initHitbox(x, y, width, height);
                index++;
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Nu s-a gasit backgroundLayer3/Map3/MapHtiboxLvl3 ");
        }


    }


    public  void drawTiles(Graphics g){
        try {
            for (TilesHitBox hitBox : hitBoxes) {

                if (hitBox instanceof TilesHitBoxAnimated) {
                    ((TilesHitBoxAnimated) hitBox).drawAnimated(g);

                } else if (hitBox instanceof Guns) {
                    ((Guns) hitBox).drawGun(g);
                } else {
                    hitBox.draw(g);
                }
                hitBox.drawHitbox(g);

            }
        }
        catch (ConcurrentModificationException e){
            System.out.println("ceva sussy la draw");
        }
    }
    public void drawBG(Graphics2D g2d) {

        for (BackgroundLayer layer : backgroundLayers) {
            layer.draw(g2d);
        }

    }

    public void update(){
        camera.update();
        for (BackgroundLayer layer : backgroundLayers) {
            layer.update(camera);
        }
        for( TilesHitBox hitBox: hitBoxes) {
            hitBox.update(camera);
            if (hitBox instanceof TilesHitBoxAnimated) {
                ((TilesHitBoxAnimated) hitBox).updateAnimation();
            }
        }
    }
}
