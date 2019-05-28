package Elements;

import java.awt.*;

public abstract class EnemyShot implements Movable, Drawable{
    protected int centerX, centerY;
    protected int speedX, speedY;
//    protected static Image image;

    public EnemyShot(int centerX, int centerY){
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public void move() {
        centerX += speedX;
        centerY += speedY;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int y) {
        this.centerY = y;
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
