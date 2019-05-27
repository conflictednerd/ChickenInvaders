package Elements.Enemies;

import Elements.Enemy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * These enemies have a simple lateral left-to-right movement from minX to maxX with constant speedX.
 */
public class Enemy1 extends Enemy {

    private static Image image = null;
    private static int width, height;
    private int minX = 0, maxX = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()-20);

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

        minX = 0;
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
        setCenterX((int) (getCenterX()+speedX));
        setCenterY((int) (getCenterY()+speedY));
    }
}
