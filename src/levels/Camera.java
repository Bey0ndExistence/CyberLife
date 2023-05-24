package levels;
import Entities.Enemy;
import Entities.Player;

/**
 * The Camera class defines a camera object that follows a player object and updates its position
 * accordingly.
 */
public class Camera {
    private int x;
    private int y;
    private int width;
    private int height;
    private Player target;

    private Enemy enemy;

    public Camera(int x, int y, int width, int height, Player player) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.target = player;

    }

    public void update() {
        if (target != null) {
                x = (target.getCameraHitbox().x - width / 2);
        }
    }


    public void setEnemy(Enemy enemy){
        this.enemy = enemy;
    }

    public int getX() {
        return x;
    }

}