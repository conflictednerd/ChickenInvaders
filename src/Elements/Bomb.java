package Elements;

import Base.Data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bomb implements Drawable, Movable {
    private double speedX = 0, speedY = 0, xd, yd;
    private int x, y;
    private int explosionAnimationCounter = 0;
    private Image image;
    private List<Image> explosions = new ArrayList<>();


    public volatile boolean isActive, isExploding;

    public Bomb(int x, int y){
        this.x = x; this.y = y;
        this.xd = x; this.yd = y;
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
//                g2.drawImage(explosions.get(explosionAnimationCounter / 5), x - explosions.get(explosionAnimationCounter / 10).getWidth(null) / 2, y - explosions.get(explosionAnimationCounter / 10).getHeight(null) / 2, null);
//                g2.drawImage(explosions.get(explosionAnimationCounter / 5), x - explosions.get(explosionAnimationCounter / 10).getWidth(null), y - explosions.get(explosionAnimationCounter / 10).getHeight(null), 800, 800,null);
                g2.drawImage(explosions.get(explosionAnimationCounter / 5), 100, 100, 1200, 1200,null);
            }
            else{
                g2.drawImage(image, x - image.getWidth(null) / 2, y - image.getHeight(null) / 2, null);
            }
        }
    }

    @Override
    public void move() {
        xd += speedX;
        yd += speedY;
        x = (int) xd;
        y = (int) yd;
        if(Math.abs(x-Data.screenSize.width/2) < 10 && Math.abs(y-Data.screenSize.height/2) < 10) explode();
    }

    private void explode() {
        //TODO KILL THE CHICKENS!!!
        isExploding = true;
    }

    public void calculateDefaultSpeeds(){
        //TODO magnitude of velocity(V) probably shouldnt be hardcoded.
        final int V = 5;
        int dy = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2 - y), dx = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2 - x);
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
