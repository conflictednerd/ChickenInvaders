package Elements;

import java.awt.*;

public abstract class Enemy implements Drawable, Movable {
    private int centerX, centerY;
    protected static Image image;
    protected EnemyShot shot;

    public EnemyShot shoot(){
        //TODO Random stuff for any enemy goes here.


        //TODO Either return an EnemyShot or null and handle this on the other side.
        return null;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, centerX-image.getWidth(null)/2, centerY- image.getHeight(null)/2, null);
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

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }
}
