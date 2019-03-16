package Elements;

import java.awt.*;

public class Shot implements Drawable, Movable {

    int x, y;
    int speedX = 0, speedY = -1;
    public Shot(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(x-5, y-10, 10, 20);
//        System.out.println("In shot draw");
    }

    @Override
    public void move() {
//        System.err.println("In shot move");
        x += speedX;
        y += speedY;
    }
}
