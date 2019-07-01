package Elements.Enemies;

import Elements.Enemy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Random;


/**
 * This type moves randomly with a chance of suddenly attacking the player.
 */
public class Enemy2 extends Enemy {

    private static Image image = null;
    private static int width = 0, height = 0;
    private int targetX = -1, targetY = -1;
    private int V = 6;
    private static Random random = new Random();
    //Should change their value in LE every time we want to call move() on enemies.
    public static int rocketX = 0, rocketY = 0;

    static {
        try {
            image = ImageIO.read(Enemy1.class.getResourceAsStream("../../Assets/Enemies/128/3.png"));
            width = image.getWidth(null);
            height = image.getHeight(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Enemy2(){
        speedX = 10;
        speedY = 10;
    }


    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, getCenterX()-image.getWidth(null)/2, getCenterY()- image.getHeight(null)/2, null);
    }


    @Override
    public void move() {
        int e = 5;
        if((Math.abs(targetX-centerX)<e && Math.abs(targetY-centerY)<e) || targetX == -1) {
            if (random.nextInt(2000) == 0) {
                targetX = rocketX;
                targetY = rocketY;
                V = 8;
            }
            else{
                targetX = 10 + random.nextInt((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()-100));
                targetY = 10 + random.nextInt((int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()-100));
                V = 4;
            }
            centerXd = centerX;
            centerYd = centerY;
            calculateSpeeds();
        }
        else{
            centerXd += speedX;
            centerYd += speedY;
            centerX = (int) centerXd;
            centerY = (int) centerYd;
        }
    }

    private void calculateSpeeds() {
        int dy = targetY - centerY, dx = targetX - centerX;
        //To handle tan(theta = NaN)
        if(dx == 0) {
            speedY = V;
            speedX = 0;
            if(dy<0) speedY *= -1;
            return;
        }

        double theta = Math.atan((double)(dy)/dx);

        if(dx>0){
            speedX = V*Math.cos(theta);
            speedY = V*Math.sin(theta);
        }
        else{
            speedX = -V*Math.cos(theta);
            speedY = -V*Math.sin(theta);
        }
    }

    @Override
    public int getWidth() {return width;}

    @Override
    public int getHeight() {return height;}
}
