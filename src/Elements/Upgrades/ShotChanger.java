package Elements.Upgrades;

import Base.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShotChanger extends Upgrade {
    private static List<Image> imageList = new ArrayList<>();
    private static int width, height;
    private int type = 1;

    static {
        try {
            for (int i = 1; i < 4; i++) {
                imageList.add(ImageIO.read(ShotChanger.class.getResourceAsStream("../../Assets/Upgrades/ShotChanger/" + i + ".png")));
            }
            height = imageList.get(0).getHeight(null);
            width = imageList.get(0).getWidth(null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param centerX
     * @param centerY
     * @param type 1, 2 or 3 each corresponding to a shot type
     */
    public ShotChanger(int centerX, int centerY, int type){
        this.centerX = centerX;
        this.centerY = centerY;
        this.type = type;
    }

    @Override
    public void activate(Player player) {
        if(player.shotType == type)
            player.shotLevel++;
        else
            player.shotType = type;
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
        g2.drawImage(imageList.get((type-1)%3), centerX-width/2, centerY- height/2, null);
    }
}
