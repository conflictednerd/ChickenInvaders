package Elements.Enemies;

import Elements.Enemy;
import Elements.EnemyShot;
import Elements.EnemyShots.EnemyShot1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 * These enemies have a simple lateral left-to-right movement from minX to maxX with constant speedX.
 */
public class Enemy1 extends Enemy {

    private static Image image = null;
    private int minX = 0, minY, maxX = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()-20);
//    private int speedX = 6, speedY = 0;
    private static int width, height;
    private static Random random = new Random();

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public Enemy1(){
        super();

        minX = 0; minY = 0;
        speedX = 6;

        try {
            if(image == null){
            image = ImageIO.read(Enemy1.class.getResourceAsStream("../../Assets/Enemies/128/0.png"));
            width = image.getWidth(null);
            height = image.getHeight(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Enemy1(int centerX, int centerY,int minX, int maxX){
        this();
        this.maxX = maxX;
        this.minX = minX;
        setCenterX(centerX);
        setCenterY(centerY);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, getCenterX()-image.getWidth(null)/2, getCenterY()- image.getHeight(null)/2, null);
    }

    @Override
    public void move() {
        if(getCenterX() + width/2 >= maxX || getCenterX() - width/2 <= minX){
            speedX *= -1;
        }
        setCenterX(getCenterX()+speedX);
        setCenterY(getCenterY()+speedY);
    }


    public void setMinX(int minX) {
        this.minX = minX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }
}
