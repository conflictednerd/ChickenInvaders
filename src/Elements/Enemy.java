package Elements;

import java.awt.*;

public abstract class Enemy implements Drawable, Movable {
    private int centerX, centerY;
    private int defaultX, defaultY, defaultSpeedX, defaultSpeedY;
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

    public int getDefaultY() {
        return defaultY;
    }

    public void setDefaultY(int defaultY) {
        this.defaultY = defaultY;
    }

    public int getDefaultX() {
        return defaultX;
    }

    public void setDefaultX(int defaultX) {
        this.defaultX = defaultX;
    }

    public void transition(){
        centerX += defaultSpeedX;
        centerY += defaultSpeedY;
    }

    /**
     * Should be called after setting values centerX, centerY, DefaultX, DefaultY.
     */
    public void calculateDefaultSpeeds(){
        //TODO magnitude of velocity(V) probably shouldnt be hardcoded.
        final int V = 6;
        int dy = defaultY - centerY, dx = defaultX - centerX;
        this.defaultSpeedY = (int)(V*dy/(Math.sqrt(dy*dy+dx*dx)));
        this.defaultSpeedX = (int)(V*dx/(Math.sqrt(dy*dy+dx*dx)));
    }
}
