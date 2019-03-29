package Elements;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Rocket implements Drawable{
    private final int rocketAnimationRefreshRate = 5;

    private int x = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2;
    private Image image, image2, image3;
    private List<Image> rocketImages = new ArrayList<>();
    private int animationCounter = 0, rocketAnimationCounter = 0;

    public volatile boolean coolDown = false;

    public Rocket(){
        try {
//            image = ImageIO.read(Rocket.class.getResourceAsStream("../Assets/SF01.png"));
            for(int i = 0;i<60; i++){
                rocketImages.add(ImageIO.read(Rocket.class.getResourceAsStream("../Assets/Rocket1/tile0"+i+".png")));
            }
            image2 = ImageIO.read(Rocket.class.getResourceAsStream("../Assets/spaceEffects_001.png"));
            image3 = ImageIO.read(Rocket.class.getResourceAsStream("../Assets/spaceEffects_005.png"));
            image = ImageIO.read(Rocket.class.getResourceAsStream("../Assets/Ships/1.png"));
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

    @Override
    public void draw(Graphics2D g2) {
        animationCounter++; animationCounter %= 8;
        rocketAnimationCounter++; rocketAnimationCounter %= rocketImages.size()*rocketAnimationRefreshRate;

        g2.drawImage(rocketImages.get(rocketAnimationCounter/rocketAnimationRefreshRate)
                ,x-rocketImages.get(rocketAnimationCounter/rocketAnimationRefreshRate).getWidth(null)/2
                ,y-rocketImages.get(rocketAnimationCounter/rocketAnimationRefreshRate).getHeight(null)/2, null
        );

//        g2.drawImage(image,x-image.getWidth(null)/2, y -image.getHeight(null)/2,null);
        if(animationCounter != 0) {
            if(coolDown)
                g2.drawImage(image3, x - image3.getWidth(null) / 2, y + rocketImages.get(rocketAnimationCounter/rocketAnimationRefreshRate).getHeight(null) / 5, null);
            else
                g2.drawImage(image2, x - image2.getWidth(null) / 2, y + rocketImages.get(rocketAnimationCounter/rocketAnimationRefreshRate).getHeight(null) / 5, null);
        }
    }
}
