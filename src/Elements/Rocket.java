package Elements;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Rocket implements Drawable{
    private final int rocketAnimationRefreshRate = 5;

    private int x = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private Image image, image2, image3;

    private int width, height;
    private List<Image> rocketImages = new ArrayList<>();
    private int animationCounter = 0, rocketAnimationCounter = 0;

    private long killTime;
    private volatile boolean alive = true;
    public boolean isAlive(){return alive;}

    public volatile boolean coolDown = false;

    public Rocket(){
        try {
//            image = ImageIO.read(Rocket.class.getResourceAsStream("../Assets/SF01.png"));
            for(int i = 0;i<60; i++){
                rocketImages.add(ImageIO.read(Rocket.class.getResourceAsStream("../Assets/Rocket1/tile0"+i+".png")));
            }
//            width = rocketImages.get(0).getWidth(null);
//            height = rocketImages.get(0).getHeight(null);

            image2 = ImageIO.read(Rocket.class.getResourceAsStream("../Assets/spaceEffects_001.png"));
            image3 = ImageIO.read(Rocket.class.getResourceAsStream("../Assets/spaceEffects_005.png"));
            image = ImageIO.read(Rocket.class.getResourceAsStream("../Assets/Ships/sf01.png"));

            width = image.getWidth(null);
            height = image.getHeight(null);
            y -= height;
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public int getWidth() { return width; }

    public int getHeight() { return height; }


    public void explode(){
        //TODO
        alive = false;
        killTime = System.currentTimeMillis();
    }


    @Override
    public void draw(Graphics2D g2) {
        if (alive) {
            animationCounter++;
            animationCounter %= 8;
            rocketAnimationCounter++;
            rocketAnimationCounter %= rocketImages.size() * rocketAnimationRefreshRate;

//            g2.drawImage(rocketImages.get(rocketAnimationCounter / rocketAnimationRefreshRate)
//                    , x - rocketImages.get(rocketAnimationCounter / rocketAnimationRefreshRate).getWidth(null) / 2
//                    , y - rocketImages.get(rocketAnimationCounter / rocketAnimationRefreshRate).getHeight(null) / 2, null
//            );

            g2.drawImage(image, x - image.getWidth(null)/2, y - image.getHeight(null)/2,null);

//        g2.drawImage(image,x-image.getWidth(null)/2, y -image.getHeight(null)/2,null);
            if (animationCounter != 0) {
                if (coolDown)
                    g2.drawImage(image3, x - image3.getWidth(null) / 2, y + rocketImages.get(rocketAnimationCounter / rocketAnimationRefreshRate).getHeight(null) / 5, null);
                else
                    g2.drawImage(image2, x - image2.getWidth(null) / 2, y + rocketImages.get(rocketAnimationCounter / rocketAnimationRefreshRate).getHeight(null) / 5, null);
            }
        }
        else{
            // 7seconds disappear time.
            if(System.currentTimeMillis()>killTime+7000) {
                setMouse();
                alive = true;
            }
        }
    }

    private void setMouse() {
        try {
            Robot robot = new Robot();
            robot.mouseMove((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight(), (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
