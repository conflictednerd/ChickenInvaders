package Elements;

import Elements.EnemyShots.EnemyShot1;

import java.awt.*;
import java.util.Random;

public abstract class Enemy implements Drawable, Movable {
    protected int centerX, centerY;
    protected int defaultX = 500, defaultY = 0, defaultSpeedX, defaultSpeedY;
    protected int speedX = 0, speedY = 0;
    //todo DELETE
    protected EnemyShot shot;
    private Random random = new Random();

    /**
     *Random stuff for any enemy comes here. E.g handling the probability of shooting.
     *
     *Note that shooting must be handled and called from LE.
     *
     * This is the default behaviour(i.e. 5% chance of shooting). For higher level enemies, the probability increases and this method should be overridden.
     * @return null if it doesn't shoot or an EnemyShot object.
     */
    public EnemyShot shoot(){
        // 5 percent chance of shooting each second -> 1/1000 chance of shooting every 20mS(LEs refresh rate).
        if (random.nextInt(1000)==0)
            return new EnemyShot1(getCenterX(),getCenterY());
        return null;
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

    public int getWidth(){
        return 0;
    }

    public int getHeight(){
        return 0;
    }

    /**
     * This function changes location of an enemy instance(centerX, centerY) one step at a time
     * so that they reach defaultX, defaultY. It's used to enter waves of enemies at the beginning of each
     * wave.
     * Currently it simply increase or decrease centerX, centerY by 5 in each step until they reach Default location.
     *
     * TODO This current algorithm must change so that default speeds are calculated and then each enemy moves alongside a straight line
     */
    public void transition(){
//        centerX += defaultSpeedX;
//        centerY += defaultSpeedY;

        if(centerX>defaultX) centerX-=5;
        else if(centerX<defaultX) centerX+=5;

        if(centerY<defaultY) centerY+=5;
        else if(centerY>defaultY) centerY-=5;
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
