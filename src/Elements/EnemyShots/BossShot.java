package Elements.EnemyShots;

import Elements.EnemyShot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class BossShot extends EnemyShot {
    private static Image image = null;
    private static int V = 5;

    static {
        try {
            image = ImageIO.read(BossShot.class.getResourceAsStream("../../Assets/BossShots/3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param centerX
     * @param centerY
     * @param orientation 0 is straight to left and it turns 45deg.
     */
    public BossShot(int centerX, int centerY, int orientation) {
        super(centerX, centerY);

        switch (orientation){
            case 0:
                speedX = 0;
                speedY = -V;
                break;

            case 1:
                speedX = -V;
                speedY = -V;
                break;

            case 2:
                speedX = -V;
                speedY = 0;
                break;

            case 3:
                speedX = -V;
                speedY = V;
                break;

            case 4:
                speedX = 0;
                speedY = V;
                break;

            case 5:
                speedX = V;
                speedY = V;
                break;

            case 6:
                speedX = V;
                speedY = 0;
                break;

            default:
                speedX = V;
                speedY = -V;
                break;
        }


    }


    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, centerX - image.getWidth(null)/2, centerY-image.getHeight(null)/2, null);
    }
}
