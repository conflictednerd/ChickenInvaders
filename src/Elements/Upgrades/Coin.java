package Elements.Upgrades;

import Base.Player;
import Elements.Enemies.Boss;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Coin extends Upgrade {
    private static Image image;
    private static int width, height;

    private int animationCounter = 0;

    static {
        try {
            image = ImageIO.read(Coin.class.getResourceAsStream("../../Assets/Upgrades/Coin/1.png"));
            height = image.getHeight(null);
            width = image.getWidth(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Coin(int centerX, int centerY){
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public void activate(Player player) {
        player.coins++;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, centerX-width/2, centerY- height/2, null);
    }
}
