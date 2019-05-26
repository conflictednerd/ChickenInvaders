package Elements.Enemies;

import Elements.Enemy;
import Elements.EnemyShot;
import Elements.EnemyShots.EnemyShot1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Random;


public class Enemy2 extends Enemy {

    private static Image image = null;
    private static int width = 0, height = 0;
    private int targetX = -1, targetY = -1;
    private static Random random = new Random();
    //Should change their value in LE every time we want to call move() on enemies.
    public static int rocketX = 0, rocketY = 0;

    public Enemy2(){
        speedX = 10;
        speedY = 10;
        try {
//            image = ImageIO.read(Enemy1.class.getResourceAsStream("../../Assets/Enemies/A/image1.png"));
            if(image == null){
            image = ImageIO.read(Enemy1.class.getResourceAsStream("../../Assets/Enemies/128/3.png"));
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
        int e = 5;
        if(random.nextInt(2000)==0){
            targetX = rocketX;
            targetY =rocketY;
        }

        //TODO use the same algorithm as in Enemy.transition to move the instance form Center point to target.
        //if(target == -1,-1) don't move
        //if (|target - center| < epsilon) target = -1,-1
        //else use the algorithm to move it.
        if(targetX == -1 && targetY == -1) return;
        if(Math.abs(targetX-centerX)<e && Math.abs(targetY-centerY)<e) {
            targetX = -1;
            targetY = -1;
            return;
        }

        if(centerX < targetX) centerX+=speedX;
        else if(centerX > targetX) centerX-=speedX;

        if(centerY < targetY) centerY+=speedY;
        else if(centerY > targetY) centerY-=speedY;
    }

    @Override
    public int getWidth() {return width;}

    @Override
    public int getHeight() {return height;}
}
