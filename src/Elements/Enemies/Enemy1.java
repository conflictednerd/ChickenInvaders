package Elements.Enemies;

import Elements.Enemy;
import Elements.EnemyShot;
import Elements.EnemyShots.EnemyShot1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class Enemy1 extends Enemy {

    private int minX = 0, minY, maxX = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()-20);
    private int speedX = 6, speedY = 0;
    private static int width, height;
    private static Random random = new Random();

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public Enemy1(){
        super();

        minX = 0; minY = 0;

        try {
            image = ImageIO.read(Enemy1.class.getResourceAsStream("../../Assets/Enemies/A/image1.png"));
            width = image.getWidth(null);
            height = image.getHeight(null);
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

    /** Note that shooting must be handled and called from LE.
     *
     * @return null if it doesn't shoot or an EnemyShot object.
     */
    @Override
    public EnemyShot shoot() {
        // 5 percent chance of shooting each second -> 1/1000 chance of shooting every 20mS(LEs refresh rate).
        if (random.nextInt(1000)==0)
            return new EnemyShot1(getCenterX(),getCenterY());
        return null;
    }
}
