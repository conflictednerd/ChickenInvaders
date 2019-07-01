package Elements;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Rocket implements Drawable{
    private volatile String owner;
    public int reviveAnimationCounter = 0;

    private int x = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private static transient Image image, image2, image3;

    private static int width, height;
    private int animationCounter = 0;

    public long killTime, reviveTime;
    private volatile boolean alive = true, reviving = false;
    public volatile boolean noLifeLeft = false;
    public boolean isAlive(){return alive;}

    public volatile boolean coolDown = false;

    static{
        try {
            image2 = ImageIO.read(Rocket.class.getResourceAsStream("../Assets/spaceEffects_001.png"));
            image3 = ImageIO.read(Rocket.class.getResourceAsStream("../Assets/spaceEffects_005.png"));
            image = ImageIO.read(Rocket.class.getResourceAsStream("../Assets/Ships/sf01.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        width = image.getWidth(null);
        height = image.getHeight(null);

    }

    public Rocket(String owner){
        this.owner = owner;
        y -= height;
    }

    public int getX() {return x;}

    public void setX(int x) {this.x = x;}

    public int getY() {return y;}

    public void setY(int y) {this.y = y;}

    public int getWidth() {return width;}

    public int getHeight() {return height;}


    public void explode(){
        alive = false;
        killTime = System.currentTimeMillis();
    }

    public void nextReviveAnimation(){
        reviveAnimationCounter++;
        reviveAnimationCounter %= 20;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!noLifeLeft) {
            if (reviving && reviveAnimationCounter < 10) {
                g2.drawImage(image, x - image.getWidth(null) / 2, y - image.getHeight(null) / 2, null);
            } else if (alive) {
                animationCounter++;
                animationCounter %= 8;
                g2.drawImage(image, x - image.getWidth(null) / 2, y - image.getHeight(null) / 2, null);
                g2.drawString(owner, x, y);

                if (animationCounter != 0) {
                    if (coolDown)
                        g2.drawImage(image3, x - image3.getWidth(null) / 2, y + image.getHeight(null) / 5, null);
                    else
                        g2.drawImage(image2, x - image2.getWidth(null) / 2, y + image.getHeight(null) / 5, null);
                }
            }
        }
    }

    public void setMouse() {
        try {
            Robot robot = new Robot();
            robot.mouseMove((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight(), (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public boolean isReviving() {
        return reviving;
    }

    public void setOwner(String name) {
        this.owner = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setReviving(boolean b) {
        reviving = b;
    }

    public void setAlive(boolean b) {
        alive = b;
    }
}
