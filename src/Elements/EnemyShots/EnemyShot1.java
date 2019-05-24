package Elements.EnemyShots;

import Elements.Enemies.Enemy1;
import Elements.EnemyShot;

import javax.imageio.ImageIO;
import java.io.IOException;

public class EnemyShot1 extends EnemyShot {

    public EnemyShot1(int centerX, int centerY) {
        super(centerX, centerY);
        setSpeedX(0);
        setSpeedY(5);

        try {
            image = ImageIO.read(Enemy1.class.getResourceAsStream("../../Assets/EnemyShots/EnemyShot1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
