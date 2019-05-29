package Elements.Upgrades;

import Base.Player;
import Elements.Drawable;
import Elements.Movable;

public abstract class Upgrade  implements Movable, Drawable {

    protected int centerX, centerY, speedX = 0, speedY = 5;

    public abstract void activate(Player player);
    public abstract int getWidth();
    public abstract int getHeight();

    @Override
    public void move() {
        centerX += speedX;
        centerY += speedY;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

}
