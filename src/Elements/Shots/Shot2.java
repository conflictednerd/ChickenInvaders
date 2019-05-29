package Elements.Shots;

import Elements.Shot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Shot2 extends Shot {
    public static final int ID = 2;
    public static final int timeBetweenConsecutiveShots = 400;

    private static Image s, l20, l10, r10, r20 = null;
    private Image image = null;
    private int width, height;

    public Shot2(int x, int y, int orientation, int shotLevel) {
        super(x, y);
        this.shotLevel = shotLevel;
        if(shotLevel>4) {
            damage += shotLevel-4;
            heatIncreaseRate /= 4;
        }
        else{
            heatIncreaseRate /= shotLevel;
        }
        Shot.shotHeat+=heatIncreaseRate;
        try {
            if (s == null) {
                s = ImageIO.read(Shot.class.getResourceAsStream("../Assets/Shots/Shot2/S.png"));
                l20 = ImageIO.read(Shot.class.getResourceAsStream("../Assets/Shots/Shot2/L20.png"));
                l10 = ImageIO.read(Shot.class.getResourceAsStream("../Assets/Shots/Shot2/L10.png"));
                r10 = ImageIO.read(Shot.class.getResourceAsStream("../Assets/Shots/Shot2/R10.png"));
                r20 = ImageIO.read(Shot.class.getResourceAsStream("../Assets/Shots/Shot2/R20.png"));
            }
            //set the speed
            int v = -speedY;
            switch (orientation) {
                case 0:
                    image = s;
                    break;
                case 1:
                    image = l20;
                    speedX = (int) (-v * Math.sin(Math.toRadians(20d)));
                    speedY = (int) (-v * Math.cos(Math.toRadians(20d)));
                    break;
                case 2:
                    image = l10;
                    speedX = (int) (-v * Math.sin(Math.toRadians(10d)));
                    speedY = (int) (-v * Math.cos(Math.toRadians(10d)));
                    break;
                case 3:
                    image = r10;
                    speedX = (int) (v * Math.sin(Math.toRadians(10d)));
                    speedY = (int) (-v * Math.cos(Math.toRadians(10d)));
                    break;

                case 4:
                    image = r20;
                    speedX = (int) (v * Math.sin(Math.toRadians(20d)));
                    speedY = (int) (-v * Math.cos(Math.toRadians(20d)));
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
