package Elements.Shots;

import Elements.Shot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * TODO! !!! when shooting with shot in a level higher than one, two or three or four shots are created
 * when we fire. Thus, in a single firing shotHeat will increase up to 4 times!! this can be solved by passing shot level
 * to Shoti constructor so that it increases the heat by a fraction of increaseShotHeat amount depending
 * on the number of shots fired in each firing. but then shotHeat
 * and increaseShotHeat must be doubles and handling it shouldnt be in abstract Shot class.
 */

public class Shot1 extends Shot {
    public static final int ID = 1;
    public static final int timeBetweenConsecutiveShots = 200;

    private static Image s, l20, l10, r10, r20 = null;
    private Image image = null;
    private int width, height;

    /**
     * @param x
     * @param y
     * @param orientation 0 for S, 1 for L20, 2 for L10, 3 for R10, 4 for R20
     */
    public Shot1(int x, int y, int orientation, int shotLevel) {
        super(x, y);
        this.shotLevel = shotLevel;
        heatIncreaseRate = 4;
        if(shotLevel>4) {
            damage += shotLevel-4;
            heatIncreaseRate /= 4;
        }
        else{
            heatIncreaseRate /= shotLevel;
        }
        Shot.shotHeat+=heatIncreaseRate;
        try {
            if(s == null){
                s = ImageIO.read(Shot.class.getResourceAsStream("../Assets/Shots/Shot1/S.png"));
                l20 = ImageIO.read(Shot.class.getResourceAsStream("../Assets/Shots/Shot1/L20.png"));
                l10 = ImageIO.read(Shot.class.getResourceAsStream("../Assets/Shots/Shot1/L10.png"));
                r10 = ImageIO.read(Shot.class.getResourceAsStream("../Assets/Shots/Shot1/R10.png"));
                r20 = ImageIO.read(Shot.class.getResourceAsStream("../Assets/Shots/Shot1/R20.png"));
            }
            //set the speed
            int v = -speedY;
            switch (orientation){
                case 0: image = s; break;
                case 1:
                    image = l20;
                    speedX = (int) (-v*Math.sin(Math.toRadians(20d)));
                    speedY = (int) (-v*Math.cos(Math.toRadians(20d)));
                    break;
                case 2:
                    image = l10;
                    speedX = (int) (-v*Math.sin(Math.toRadians(10d)));
                    speedY = (int) (-v*Math.cos(Math.toRadians(10d)));
                    break;
                case 3:
                    image = r10;
                    speedX = (int) (v*Math.sin(Math.toRadians(10d)));
                    speedY = (int) (-v*Math.cos(Math.toRadians(10d)));
                    break;

                case 4:
                    image = r20;
                    speedX = (int) (v*Math.sin(Math.toRadians(20d)));
                    speedY = (int) (-v*Math.cos(Math.toRadians(20d)));
                    break;
            }
            width = image.getWidth(null);
            height = image.getHeight(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, x-image.getWidth(null)/2, y-image.getHeight(null)/2, null);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
