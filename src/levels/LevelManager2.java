package levels;

import Entities.Guns;
import Entities.Heart;
import Entities.TilesHitBox;
import Entities.TilesHitBoxAnimated;
import Game.Game;
import Utils.GameException;
import Utils.LoadSave;
import gamestates.Playing;
import gamestates.Playing2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static Utils.Constants.PlayerConstants.*;
import static Utils.Constants.PlayerConstants.BANNER_SUSHI;

public class LevelManager2 {

    private Game game;
    Playing2 playing2;
    private BufferedImage miami_car,miami_highway, palm_tree, car_bmw,car_tesla, BG_FAR, BG_MIDDLE, BG_CLOSE;
    private Camera camera;


    private ArrayList<TilesHitBox> hitBoxes;
    ArrayList<BackgroundLayer> backgroundLayers;
    private BufferedImage heart;

    LoadSave singleton;
    public LevelManager2(Playing2 playing2, Camera camera)  {
        this.playing2= playing2;
        this.camera = camera;
        importTileSprites();
        LoadTileHitboxes();

    }

    public ArrayList<TilesHitBox> getHitBoxes(){
        return hitBoxes;

    }
    private void importTileSprites() {
        try {
            miami_highway = LoadSave.getInstance().getAtlas(LoadSave.MIAMI_HIGHWAY);
            miami_car = LoadSave.getInstance().getAtlas(LoadSave.MASINA);
            miami_car = LoadSave.getInstance().getAtlas(LoadSave.MASINA);
            palm_tree = LoadSave.getInstance().getAtlas(LoadSave.PALMIER);
            car_bmw = LoadSave.getInstance().getAtlas(LoadSave.CAR_BMW);

            //Background layers
            BG_FAR = LoadSave.getInstance().getAtlas(LoadSave.BG_FAR_LVL2);
            BG_MIDDLE = LoadSave.getInstance().getAtlas(LoadSave.BG_MIDDLE_LVL2);
            BG_CLOSE = LoadSave.getInstance().getAtlas(LoadSave.BG_CLOSE_LVL2);

            //HEART
            BufferedImage hearts = LoadSave.getInstance().getAtlas(LoadSave.HEARTS);
            heart = hearts.getSubimage(2, 0, 30, 28);
        }
        catch (GameException e){
            System.out.println(e);

        }


    }

    public void LoadTileHitboxes()  {
        try {
            File backgroundLayerFile = new File("src/backgroundLayer1");
            Scanner scanner = new Scanner(backgroundLayerFile);
            backgroundLayers = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                int type = Integer.parseInt(values[0]);
                float parallaxFactor = Float.parseFloat(values[1]);
                int x = Integer.parseInt(values[2]);
                int y = Integer.parseInt(values[3]);

                if (type == 0) {
                    backgroundLayers.add(new BackgroundLayer(BG_FAR, parallaxFactor, x, y));
                } else if (type == 1) {
                    backgroundLayers.add(new BackgroundLayer(BG_MIDDLE, parallaxFactor, x, y));
                } else if (type == 2) {
                    backgroundLayers.add(new BackgroundLayer(BG_CLOSE, parallaxFactor, x, y));
                }
            }

            this.hitBoxes = new ArrayList<>();


            File map2 = new File("src/MapLvl2");
            scanner = new Scanner(map2);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                int type = Integer.parseInt(values[0]);
                int x = Integer.parseInt(values[1]);
                int y = Integer.parseInt(values[2]);
                int width = Integer.parseInt(values[3]);
                int height = Integer.parseInt(values[4]);

                if (type == 0) {
                    hitBoxes.add(new TilesHitBox(x, y, width, height, miami_highway));
                } else if (type == 1) {
                    hitBoxes.add(new TilesHitBox(x, y, width, height, miami_car));
                } else if (type == 2) {
                    hitBoxes.add(new TilesHitBox(x, y, width, height, palm_tree));
                } else if (type == 3) {
                    hitBoxes.add(new Heart(x, y, width, height, heart));
                }

            }


            File mapHitbox = new File("src/MapHitboxLvl2");
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
            System.out.println(" nu s-a gasit backgroundLayer1/MapLvl2/MapHitboxLvl2");
        }
    }


    public  void drawTiles(Graphics g){
        for( TilesHitBox hitBox : hitBoxes){

            if (hitBox instanceof TilesHitBoxAnimated) {
                ((TilesHitBoxAnimated) hitBox).drawAnimated(g);

            }
            else if(hitBox instanceof Guns) {
                ((Guns) hitBox).drawGun(g);
            }
            else {
                hitBox.draw(g);
            }
            hitBox.drawHitbox(g);

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
