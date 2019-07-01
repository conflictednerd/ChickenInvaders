package Elements.Shots;

import Elements.Drawable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Shot2 extends Shot {
    public static final int ID = 2;
    public static final int timeBetweenConsecutiveShots = 400;

    private static Image s, l20, l10, r10, r20 = null;
    private int width, height, orientation;

    static{
        try {
            s = ImageIO.read(Shot.class.getResourceAsStream("../../Assets/Shots/Shot2/S.png"));
            l20 = ImageIO.read(Shot.class.getResourceAsStream("../../Assets/Shots/Shot2/L20.png"));
            l10 = ImageIO.read(Shot.class.getResourceAsStream("../../Assets/Shots/Shot2/L10.png"));
            r10 = ImageIO.read(Shot.class.getResourceAsStream("../../Assets/Shots/Shot2/R10.png"));
            r20 = ImageIO.read(Shot.class.getResourceAsStream("../../Assets/Shots/Shot2/R20.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Shot2(int x, int y, int orientation, int shotLevel, String owner) {
        super(x, y);
        this.owner = owner;
        this.orientation = orientation;
        this.shotLevel = shotLevel;
        if(shotLevel>4) {
            damage += shotLevel-4;
        }
//        Shot.shotHeat+=heatIncreaseRate;
//        try {
//            if (s == null) {
//                s = ImageIO.read(Shot.class.getResourceAsStream("../../Assets/Shots/Shot2/S.png"));
//                l20 = ImageIO.read(Shot.class.getResourceAsStream("../../Assets/Shots/Shot2/L20.png"));
//                l10 = ImageIO.read(Shot.class.getResourceAsStream("../../Assets/Shots/Shot2/L10.png"));
//                r10 = ImageIO.read(Shot.class.getResourceAsStream("../../Assets/Shots/Shot2/R10.png"));
//                r20 = ImageIO.read(Shot.class.getResourceAsStream("../../Assets/Shots/Shot2/R20.png"));
//            }
            //set the speed
            int v = -speedY;
            switch (orientation) {
                case 0:
                    width = s.getWidth(null);
                    height = s.getHeight(null);
                    break;
                case 1:
                    width = l20.getWidth(null);
                    height = l20.getHeight(null);
                    speedX = (int) (-v * Math.sin(Math.toRadians(20d)));
                    speedY = (int) (-v * Math.cos(Math.toRadians(20d)));
                    break;
                case 2:
                    width = l10.getWidth(null);
                    height = l10.getHeight(null);
                    speedX = (int) (-v * Math.sin(Math.toRadians(10d)));
                    speedY = (int) (-v * Math.cos(Math.toRadians(10d)));
                    break;
                case 3:
                    width = r10.getWidth(null);
                    height = r10.getHeight(null);
                    speedX = (int) (v * Math.sin(Math.toRadians(10d)));
                    speedY = (int) (-v * Math.cos(Math.toRadians(10d)));
                    break;

                case 4:
                    width = r20.getWidth(null);
                    height = r20.getHeight(null);
                    speedX = (int) (v * Math.sin(Math.toRadians(20d)));
                    speedY = (int) (-v * Math.cos(Math.toRadians(20d)));
                    break;
            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void draw(Graphics2D g2) {
        switch (orientation) {
            case 0:
                g2.drawImage(s, x-s.getWidth(null)/2, y-s.getHeight(null)/2, null);
                break;
            case 1:
                g2.drawImage(l20, x-l20.getWidth(null)/2, y-l20.getHeight(null)/2, null);
                break;
            case 2:
                g2.drawImage(l10, x-l10.getWidth(null)/2, y-l10.getHeight(null)/2, null);
                break;
            case 3:
                g2.drawImage(r10, x-r10.getWidth(null)/2, y-r10.getHeight(null)/2, null);
                break;
            case 4:
                g2.drawImage(r20, x-r20.getWidth(null)/2, y-r20.getHeight(null)/2, null);
                break;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
