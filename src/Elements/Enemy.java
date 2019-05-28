package Elements;

import Elements.EnemyShots.EnemyShot1;

import java.util.ArrayList;
import java.util.Random;

public abstract class Enemy implements Drawable, Movable {
    protected int centerX, centerY;
    protected int lvl = 1;
    protected double centerXd, centerYd;
    protected double defaultX = 500, defaultY = 0, defaultSpeedX, defaultSpeedY;
    protected double speedX = 0, speedY = 0;
    public double health = 1;
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
    public java.util.List<EnemyShot> shoot(){
        // 5 percent chance of shooting each second -> 1/1000 chance of shooting every 20mS(LEs refresh rate).
        java.util.List<EnemyShot> list = new ArrayList();
        if (random.nextInt(1000)==0)
            list.add(new EnemyShot1(centerX, centerY));
        if(lvl>2)
            for(int i = 0; i< list.size(); i++){
                list.get(i).speedX*=1.5;
                list.get(i).speedY*=1.5;
            }
        return list;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
        this.centerXd = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
        this.centerYd = centerY;
    }

    public double getDefaultY() {
        return defaultY;
    }

    public void setDefaultY(int defaultY) {
        this.defaultY = defaultY;
    }

    public double getDefaultX() {
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

    public void setLvl(int lvl){this.lvl = lvl;}

    /**
     * This function changes location of an enemy instance(centerX, centerY) one step at a time
     * so that they reach defaultX, defaultY. It's used to enter waves of enemies at the beginning of each
     * wave.
     * Currently it simply increase or decrease centerX, centerY by 5 in each step until they reach Default location.
     */
    public void transition(){
        centerXd += defaultSpeedX;
        centerYd += defaultSpeedY;

        centerX = (int) centerXd;
        centerY = (int) centerYd;

//        if(centerX>defaultX) centerX-=5;
//        else if(centerX<defaultX) centerX+=5;
//
//        if(centerY<defaultY) centerY+=5;
//        else if(centerY>defaultY) centerY-=5;
    }

    /**
     * Should be called after setting values centerX, centerY, DefaultX, DefaultY.
     */
    public void calculateDefaultSpeeds(){
        //TODO magnitude of velocity(V) probably shouldnt be hardcoded.
        final int V = 6;
        int dy = (int) (defaultY - centerY), dx = (int) (defaultX - centerX);
        //To handle tan(theta = NaN)
        if(dx == 0) {
            defaultSpeedY = V;
            defaultSpeedX = 0;
            if(dy<0) defaultSpeedY *= -1;
//            System.out.println("DspeedX = 0, DspeedY = 6");
            return;
        }

        double theta = Math.atan((double)(dy)/dx);

        if(dx>0){
            defaultSpeedX = V*Math.cos(theta);
            defaultSpeedY = V*Math.sin(theta);
        }
        else{
            defaultSpeedX = -V*Math.cos(theta);
            defaultSpeedY = -V*Math.sin(theta);
        }

    }
}
