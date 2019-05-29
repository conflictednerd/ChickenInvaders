package Elements;

public abstract class Shot implements Drawable, Movable {

    protected int x, y;
    protected int speedX = 0, speedY = -10;
    protected int shotLevel = 1;
    /**
     * If shotHeat reaches maxHeat, no shots will be fired until shotHeat reaches 0 and we're in 'CoolDown mode'.
     * While in cool down mode (or not shooting in general), shotHeat will be reduced by heatReductionRate every coolDownTimeMillis.
     */
//    public long timeBetweenConsecutiveShots = 200;
    public volatile int heatIncreaseRate = 3;
    //heatIncreaseRate can be implemented separately in each Shot class.
    public static volatile double shotHeat = 0, maxHeat = 100, heatReductionRate = 5;
    public static long coolDownTimeMillis = 200;
    public int damage = 1;

    public static void reduceHeat() {
        shotHeat -= heatReductionRate;
        if(shotHeat < 0) shotHeat = 0;
    }

    //todo test it, probably it must be implemented in each shot class separately.
    protected void increaseHeat() {
        shotHeat += heatIncreaseRate;
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

    public Shot(int x, int y){
        this.x = x;
        this.y = y;
//        increaseHeat();
    }

    @Override
    public void move() {
        x += speedX;
        y += speedY;
    }
}
