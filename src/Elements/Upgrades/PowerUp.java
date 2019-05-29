package Elements.Upgrades;

import Base.Player;
import Elements.Drawable;
import Elements.Movable;
import Elements.Shot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PowerUp extends Upgrade {
    private static Image image;
    private static int width, height;

    public PowerUp(int centerX, int centerY){
        this.centerX = centerX;
        this.centerY = centerY;
        try{
            if(image == null) {
                image = ImageIO.read(PowerUp.class.getResourceAsStream("../../Assets/Upgrades/PowerUp/1.png"));
                height = image.getHeight(null);
                width = image.getWidth(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void activate(Player player) {
        player.rocketLevel++;
        Shot.maxHeat+=5;
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
