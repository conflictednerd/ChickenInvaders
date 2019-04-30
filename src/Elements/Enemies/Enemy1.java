package Elements.Enemies;

import Elements.Enemy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Enemy1 extends Enemy {

    private int minX, minY, maxX;
    private int speedX = 10, speedY = 0;
    private static int width, height;

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
}
