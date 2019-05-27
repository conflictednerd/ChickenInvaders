package Elements.Enemies;

import Elements.Enemy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Enemy3 extends Enemy {

    private static Image image = null;
    private static int width = 0, height = 0;

    public double thetaDeg;
    private double thetaRad, speedTheta;
    private int pivotX, pivotY, radius;

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public Enemy3(int pivotX, int pivotY, int radius){
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        this.radius = radius;
        speedTheta = 1;

        try {
            if(image == null){
                image = ImageIO.read(Enemy1.class.getResourceAsStream("../../Assets/Enemies/128/4.png"));
                width = image.getWidth(null);
                height = image.getHeight(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        centerX = (int) (pivotX + radius*Math.cos(thetaRad));
        centerY = (int) (pivotY + radius*Math.sin(thetaRad));
    }
}
