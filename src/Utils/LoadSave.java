package Utils;

import Game.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * The LoadSave class is a singleton that provides methods for loading images from files.
 */

public class LoadSave {
    private static LoadSave instance;

    public static final String PLAYER_ATLAS = "V_SpriteSheetFinal2.0.png";
    public static final String LEVEL1_TILESET1 = "Gata.png";
    public static final String LEVEL1_TILESET2 = "yessir.png";
    public static final String BRIDGE_TILE = "bridgeFinal.png";
    public static final String BG_FAR = "Untitled-1.png";
    public static final String BG_MIDDLE = "Untitled-2.png";
    public static final String BG_CLOSE = "Untitled-3.png";
    public static final String BANNER_START = "banner-final.png";
    public static final String BANNER_R = "banner-R.png";
    public static final String BANNER_FLASH = "banner-flash.png";
    public static final String BANNER_LIFE = "banner-Life.png";
    public static final String BANNER_HOTEL = "banner-hotel.png";
    public static final String BANNER_JAPONEZA_VERDE = "banner-japoneza-verde.png";
    public static final String BANNER_COKE = "banner-coke.png";
    public static final String BANNER_JAPONEZA_MOV = "banner-japoneza-mov.png";
    public static final String BANNER_SUSHI = "banner-sushi.png";
    public static final String GUN_RED = "gunRed.png";
    public static final String COMBUSTER_TEXT = "combuster.png";
    public static final String LEVEL_END= "level_end.png";
    public static final String BULLETS= "bullets.png";
    public static final String BUTTONS= "button_atlas.png";
    public static final String MENU_BACKGROUNDS= "MenuBg-1.png";
    public static final String COP= "cops.png";
    public static final String TURRET= "turret.png";
    public static final String BOSS_LVL1= "boss_lvl1.png";
    public static final String TURRET_BULLET= "turret-bullet.png";
    public static final String DEATH_SCREEN= "deathScreen.png";
    public static final String BOSS_LVL1_BULLET= "boss_lvl1_bullet.png";

    public static final String PORTAL= "green_portal.png";

    public static final String HEARTS = "hearts.png";

    //LVL2
    public static final String MIAMI_HIGHWAY= "miami_highway_final.png";
    public static final String BG_FAR_LVL2= "miami_sky.png";
    public static final String BG_MIDDLE_LVL2= "miami_buildings.png";
    public static final String BG_CLOSE_LVL2= "palmieri.png";
    public static final String MASINA= "miami_masina.png";
    public static final String PALMIER= "palm-tree.png";
    public static final String CAR_BMW= "car_bmw.png";
    public static final String CAR_TESLA= "car-tesla.png";
    public static final String PORTAL_LVL2= "purple_portal.png";
    public static final String BOSS_LVL2= "boss_lvl2_final.png";
    public static final String BLANK= "blank.png";

    //LVL3
    public static final String LVL3_MAP= "lvl3map_final.png";
    public static final String LVL3_BG= "lvl3_bg.png";
    public static final String LVL3_TERMINAL= "lvl3_small-terminal.png";
    public static final String LVL3_ELEVATOR= "lv3_elevator.png";
    public static final String LVL3_CRYO_POD= "lvl3_cryo-pod.png";
    public static final String LVL3_BIG_COMPUTER= "lvL3_big-computer.png";
    public static final String LVL3_CABINS= "lvl3_server-gabinetes.png";
    public static final String LVL3_BOSS= "finalboss3_final.png";
    public static final String BULLET_LVL3= "bullet_boss3.png";
    public static final String BULLET_LVL3_SECONDGUN= "bullet_bosslvl2_second.png";

    //public static final String LVL3_BOSS= "download_1.png";



    private LoadSave() {
        // private constructor to prevent instantiation from outside
    }

    public static LoadSave getInstance() {
        if (instance == null) {
            instance = new LoadSave();
        }
        return instance;
    }

    public BufferedImage getAtlas(String fileName) throws GameException {
        BufferedImage img = null;
        InputStream is=null;

        try {
            is = LoadSave.class.getResourceAsStream("/" + fileName);
            if(is==null){
                throw new GameException("Failed to read atlas: " + fileName,1002, "IO Error");
            }
            img = ImageIO.read(is);
        } catch (IOException e) {
            throw new GameException("Failed to read atlas: " + fileName, e, 1002, "IO Error");
        }

        finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                throw new GameException("Failed to close input stream", e, 1003, "IO Error");
            }
        }
        return img;
    }
}
