package Utils;

/**
 * The Constants class contains static variables and methods for player actions and sprite amounts in a
 * Java game.
 */
public class Constants {

    public static class EnemyConstants{
        public static final int COP = 0;
        public static final int IDLE = 1;
        public static final int IDLE_GUN = 2;
        public static final int RUNNING = 0;
        public static final int TURRET = 2;
        public static final int BOSS_1 = 3;
        public static final int BOSS_2 = 4;
        public static final int BOSS_3 = 5;



//FINAL BOSS
public static final int IDLE_BOSS1 = 3;
public static final int DEATH_BOSS1 = 0;
public static final int RUNNING_BOSS1 = 1;
public static final int SHOOTING_BOSS1 = 2;

//LVL2

public static final int IDLE_BOSS2 = 0;
public static final int RUNNING_BOSS2 = 1;
public static final int ATTACK_BOSS2 = 2;
public static final int DEATH_BOSS2 = 4;
public static final int GRUMPY_BOSS2 = 3;

//LVL3
public static final int  RUNNING_BOSS3 = 2;

        public static final int ATTACK_BOSS3_FIRSTGUN = 6;
        public static final int DEATH_BOSS3 = 4;
        public static final int IDLE_BOSS3 = 4;
        public static final int ATTACK_BOSS3_SECONDGUN = 5;
        public static final int JUMPING_BOSS3 = 0;

        public static int GetSpriteAmount(int enemy_type, int enemy_state) {

            switch (enemy_type) {
                case COP:
                    switch (enemy_state) {
                        case IDLE:
                            return 1;
                        case IDLE_GUN:
                            return 1;
                        case RUNNING:
                            return 10;
                    }
                case TURRET:
                    switch (enemy_state){
                        case IDLE :
                            return 2;
                        case RUNNING:
                            return 4;
                    }
                case BOSS_1:
                    switch (enemy_state){
                        case IDLE_BOSS1:
                            return 2;
                        case DEATH_BOSS1:
                            return 9;
                        case RUNNING_BOSS1:
                            return 4;
                        case SHOOTING_BOSS1 :
                            return 5;
                    }
                case BOSS_2:
                    switch (enemy_state){
                        case IDLE_BOSS2:
                            return 2;
                        case RUNNING_BOSS2:
                            return 8;
                        case ATTACK_BOSS2:
                            return 7;
                        case DEATH_BOSS2 :
                            return 6;
                        case GRUMPY_BOSS2:
                            return 4;
                    }
                case BOSS_3:
                    switch (enemy_state){
                        case IDLE_BOSS3:
                            return 8 ;
                        case RUNNING_BOSS3:
                            return 8;
                        case ATTACK_BOSS3_FIRSTGUN:
                            return 6;
                        case ATTACK_BOSS3_SECONDGUN:
                            return 6;
                        case JUMPING_BOSS3:
                            return 13;
                    }


            }

            return 0;

        }

    }
    public static  class Buttons{
        public static final int B_WIDTH_DEFAULT = 140;
        public static final int B_HEIGHT_DEFAULT = 56;
        public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * 1);
        public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * 1);

    }
    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUN = 1;
        public static final int HURT = 2;
        public static final int DUCK = 3;
        public static final int SHOOT1 = 4;
        public static final int SHOOTcombuster = 5;
        public static final int JUMP = 6;

        public static final int IDLE_LEFT = 7;
        public static final int RUN_LEFT = 8;
        public static final int HURT_LEFT = 9;
        public static final int DUCK_LEFT = 10;
        public static final int SHOOT1_LEFT = 11;
        public static final int SHOOTcombuster_LEFT = 12;
        public static final int JUMP_LEFT = 13;

        public static final int BANNER_START = 14;

        public static final int BANNER_R = 15;
        public static final int BANNER_COKE = 16;
        public static final int BANNER_LIFE = 17;
        public static final int BANNER_FLASH = 18;
        public static final int BANNER_JAPONEZA_VERDE = 19;
        public static final int BANNER_JAPONEZA_MOV = 20;
        public static final int BANNER_SUSHI = 21;
        public static final int BULLET_PLAYER = 22;

        public static final int BULLET_TURRET = 23;
        public static final int BULLET_BOSS_LVL1 = 24;
        public static final int BULET_BOSS_LVL2 = 25;
        public static final int BULET_BOSS_LVL2_SECOND = 26;

        public static int GetSpriteAmount(int player_action) {

            switch (player_action) {
                case IDLE:
                    return 4;
                case RUN:
                    return 8;
                case HURT:
                    return 1;
                case DUCK:
                    return 5;
                case SHOOT1:
                    return 3;
                case SHOOTcombuster:
                    return 3;
                case JUMP:
                    return 7;
                case IDLE_LEFT:
                    return 4;
                case RUN_LEFT:
                    return 8;
                case HURT_LEFT:
                    return 1;
                case DUCK_LEFT:
                    return 5;
                case SHOOT1_LEFT:
                    return 3;
                case SHOOTcombuster_LEFT:
                    return 3;
                case JUMP_LEFT:
                    return 7;

                default:
                    return 4;
            }
        }

        public  static int getSpriteAmountTiles(int tile) {
            switch (tile) {
                case BANNER_START:
                    return 4;
                case BANNER_R:
                    return 4;
                case BANNER_COKE:
                    return 3;
                case BANNER_JAPONEZA_MOV:
                    return 4;
                case BANNER_SUSHI:
                    return 3;
                case BANNER_LIFE:
                    return 4;
                case BULLET_PLAYER:
                    return 4;
                case BULLET_TURRET:
                    return 3;
                case BULLET_BOSS_LVL1:
                    return 4;
                case BULET_BOSS_LVL2:
                    return 2;
                case BULET_BOSS_LVL2_SECOND:
                    return 6;
                default:
                    return 4;
            }
        }
    }
}
