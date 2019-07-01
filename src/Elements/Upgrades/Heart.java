package Elements.Upgrades;

import Base.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Heart extends Upgrade {
    private static Image image;
    private static int width, height;

    static {
        try {
            image = ImageIO.read(Heart.class.getResourceAsStream("../../Assets/Upgrades/Heart/heart.png"));
            height = image.getHeight(null);
            width = image.getWidth(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Heart(int centerX, int centerY){
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public void activate(Player player) {
        player.life++;
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
