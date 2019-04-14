package Elements;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Shot implements Drawable, Movable {

    private int x, y;

    /**
     * If shotHeat reaches maxHeat, no shots will be fired until shotHeat reaches 0 and we're in 'CoolDown mode'.
     * While in cool down mode (or not shooting in general), shotHeat will be reduced by heatReductionRate every coolDownTimeMillis.
     */
    public static long coolDownTimeMillis = 200, timeBetweenConsecutiveShots = 200;
    public static volatile int shotHeat = 0, maxHeat = 100, heatReductionRate = 5, heatIncreaseRate = 3;

    public static void reduceHeat() {
        shotHeat -= heatReductionRate;
        if(shotHeat < 0) shotHeat = 0;
    }

    private void increaseHeat() {
        shotHeat += heatIncreaseRate;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    int speedX = 0, speedY = -10;
    private static Image image;

    public Shot(int x, int y){
        this.x = x;
        this.y = y;
        try {
//            image = ImageIO.read(Shot.class.getResourceAsStream("../Assets/spaceMissiles_003.png"));
            image = ImageIO.read(Shot.class.getResourceAsStream("../Assets/Shots/MegaLaser.png"));
//            image = ImageIO.read(Shot.class.getResourceAsStream("../Assets/Shots/PlayProjectile.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        increaseHeat();
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, x-image.getWidth(null)/2, y-image.getHeight(null)/2, null);
    }

    @Override
    public void move() {
        x += speedX;
        y += speedY;
    }
}
