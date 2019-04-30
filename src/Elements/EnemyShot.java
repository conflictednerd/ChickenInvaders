package Elements;

import java.awt.*;

public abstract class EnemyShot implements Movable, Drawable{
    private int x, y;
    private int speedX, speedY;
    private static Image image;

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, x - image.getWidth(null)/2, y-image.getHeight(null)/2, null);
    }

    @Override
    public void move() {
        x += speedX;
        y += speedY;
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

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }
}
