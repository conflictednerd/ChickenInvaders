package Elements.EnemyShots;

import Elements.Enemies.Enemy1;
import Elements.EnemyShot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class EnemyShot1 extends EnemyShot {
    private static Image image;

    public EnemyShot1(int centerX, int centerY) {
        super(centerX, centerY);
        setSpeedX(0);
        setSpeedY(5);
        try {
            if(image == null)
                image = ImageIO.read(Enemy1.class.getResourceAsStream("../../Assets/EnemyShots/EnemyShot1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, centerX - image.getWidth(null)/2, centerY-image.getHeight(null)/2, null);
    }
}
