package Elements;

import Base.Data;

import javax.imageio.ImageIO;
import javax.sql.DataSource;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bomb implements Drawable, Movable {
    private int x, y;
    private int explosionAnimationCounter = 0;
    private Image image;
    private List<Image> explosions = new ArrayList<>();


    public volatile boolean isActive, isExploding;

    public Bomb(int x, int y){
        this.x = x; this.y = y;
        try {
            image = ImageIO.read(Rocket.class.getResourceAsStream("../Assets/icons/bomb.png"));
            for(int i = 1; i < 14; i++){
                explosions.add(ImageIO.read(Rocket.class.getResourceAsStream(("../Assets/explosion/"+i+".png"))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        isActive = true;
        isExploding = false;
    }

    @Override
    public void draw(Graphics2D g2) {
        if(isActive) {
            if (isExploding) {
                explosionAnimationCounter++;
                if (explosionAnimationCounter >= explosions.size() * 5 - 1) {
                    isExploding = false;
                    isActive = false;
                }
                g2.drawImage(explosions.get(explosionAnimationCounter / 5), x - explosions.get(explosionAnimationCounter / 10).getWidth(null) / 2, y - explosions.get(explosionAnimationCounter / 10).getHeight(null) / 2, null);
            }
            else{
                g2.drawImage(image, x - image.getWidth(null) / 2, y - image.getHeight(null) / 2, null);
            }
        }
    }

    @Override
    public void move() {
        if(x < Data.screenSize.width/2) x+=10;
        else if(x > Data.screenSize.width/2) x-=10;
        if(y < Data.screenSize.height/2) y+=10;
        else if(y > Data.screenSize.height/2) y-=10;
        if(Math.abs(x-Data.screenSize.width/2) < 10 && Math.abs(y-Data.screenSize.height/2) < 10) explode();
    }

    private void explode() {
        //TODO KILL THE CHICKENS!!!
        isExploding = true;
    }
}
