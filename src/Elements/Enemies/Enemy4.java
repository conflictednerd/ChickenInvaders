package Elements.Enemies;

import Elements.Enemy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 * Only one wave of these can be on the screen at any moment because of pivotX,Y being static.
 * Must assign pivotX,Y , radius, thetaDeg when creating the wave;
 */
public class Enemy4 extends Enemy {
    private static Image image = null;
    private static int width = 0, height = 0;

    public double thetaDeg;
    private double thetaRad, speedTheta;
    public static double pivotX = 0, pivotY = 0, V = 4, targetX = -1, targetY = -1;
    private static Random random = new Random();
    public int radius;

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    static {
        try {
            image = ImageIO.read(Enemy1.class.getResourceAsStream("../../Assets/Enemies/128/2.png"));
            width = image.getWidth(null);
            height = image.getHeight(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Enemy4(int radius){
        this.radius = radius;
        speedTheta = 1;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, getCenterX()-image.getWidth(null)/2, getCenterY()- image.getHeight(null)/2, null);
    }

    @Override
    public void move() {
        thetaDeg += speedTheta;
        thetaDeg %= 360;
        thetaRad = Math.toRadians(thetaDeg);

        int e = 10;
        if ((Math.abs(pivotX - targetX) < e && Math.abs(pivotY - targetY) < e) || targetX == -1) {
            targetX = 300 + random.nextInt((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 2 * 300));
            targetY = 300 + random.nextInt((int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 2 * 300));
            calculateSpeeds();
        }
        pivotY += speedY;
        pivotX += speedX;
        centerX = (int) (pivotX + radius * Math.cos(thetaRad));
        centerY = (int) (pivotY + radius * Math.sin(thetaRad));

    }

    private void calculateSpeeds() {
        int dy = (int) (targetY - pivotY), dx = (int) (targetX - pivotX);
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
}
