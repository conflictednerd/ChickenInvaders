package Elements;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Shot implements Drawable, Movable {

    private int x, y;

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
    int speedX = 0, speedY = -1;
    private static Image image;

    public Shot(int x, int y){
        this.x = x;
        this.y = y;
        try {
            image = ImageIO.read(Shot.class.getResourceAsStream("../Assets/food.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, x-image.getWidth(null)/2, y-image.getHeight(null)/2, null);
    }

    @Override
    public void move() {
//        System.err.println("In shot move");
        x += speedX;
        y += speedY;
    }
}
