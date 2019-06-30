package Elements.Enemies;

import Elements.Enemy;
import Elements.EnemyShot;
import Elements.EnemyShots.BossShot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Boss extends Enemy {
    public int width, height;
    private int minX = 50, maxX = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()-50), minY = 50, maxY = 600;
    //todo !! NETWORK can be static??
    private transient List<Image> imageList = new ArrayList<>();
    private int animationCounter = 0;
    private static Random random = new Random();

    public Boss(){
        speedX = 10;
        speedY = 10;
        health = 250;
        try{
            for(int i = 0; i < 25; i++){
                imageList.add(ImageIO.read(Boss.class.getResourceAsStream("../../Assets/Boss/Version1_Attack1_" + i + ".png")));
            }
            width = imageList.get(0).getWidth(null);
            height = imageList.get(0).getHeight(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void draw(Graphics2D g2) {
        animationCounter++;
        animationCounter %= imageList.size();
        g2.drawImage(imageList.get(animationCounter), centerX-width/2, centerY- height/2, null);
    }

    @Override
    public void move() {
        if (centerX + width/2 >= maxX || centerX - width/2 <= minX) speedX*=-1;
        if (centerY + height/2 >= maxY || centerY - height/2 <= minY) speedY*=-1;
        centerX += speedX;
        centerY += speedY;
    }

    @Override
    public List<EnemyShot> shoot() {
        //Random at 8 directions with 50% chance in each second (=1% each 20mS(LEs refresh rate))
        List<EnemyShot> shots = new ArrayList<>();
        for(int i = 0; i< 8; i++){
            if(random.nextInt(100) == 1)
                shots.add(new BossShot(centerX, centerY, i));
        }
        return shots;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
