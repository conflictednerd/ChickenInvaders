package Swing;

import Base.Data;
import Swing.Template.RoundedPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class StatPanel extends RoundedPanel {
    public volatile Data data;
    Image heartIcon, coinIcon, bombIcon, shotIcon;
    public JLabel life = null, lifeCount, coin = null, coinCount, bomb = null, bombCount, shot = null, shotLevelCount;
    public Integer lifeINT, coinINT, bombINT, shotINT;


    public StatPanel(Data data) {
        super();
        this.data = data;
        initialize();
        readImages();
        readData();
        setLabels();

        addLabels();
    }

    private void setLabels() {
        lifeCount = new JLabel(lifeINT.toString());
        lifeCount.setFont(new Font(Font.DIALOG, Font.LAYOUT_LEFT_TO_RIGHT | Font.BOLD | Font.CENTER_BASELINE, 38));
        lifeCount.setForeground(Color.white);

        coinCount = new JLabel(coinINT.toString());
        coinCount.setFont(new Font(Font.DIALOG, Font.LAYOUT_LEFT_TO_RIGHT | Font.BOLD | Font.CENTER_BASELINE, 38));
        coinCount.setForeground(Color.white);

        bombCount = new JLabel(bombINT.toString());
        bombCount.setFont(new Font(Font.DIALOG, Font.LAYOUT_LEFT_TO_RIGHT | Font.BOLD | Font.CENTER_BASELINE, 38));
        bombCount.setForeground(Color.white);
    }

    private void readData() {
        lifeINT = data.dynamicData.player.life;
        coinINT = data.dynamicData.player.coins;
        bombINT = data.dynamicData.player.bombs;
        shotINT = data.dynamicData.player.shotLevel;
    }

    private void addLabels() {
        add(life);
        add(lifeCount);
        add(coin);
        add(coinCount);
        add(bomb);
        add(bombCount);
    }

    private void readImages() {
        try {
            heartIcon = ImageIO.read(StatPanel.class.getResourceAsStream("../Assets/icons/heart.png"));
            coinIcon = ImageIO.read(StatPanel.class.getResourceAsStream("../Assets/icons/coin.png"));
            bombIcon = ImageIO.read(StatPanel.class.getResourceAsStream("../Assets/icons/bomb.png"));
            life = new JLabel(new ImageIcon(heartIcon));
            life.setForeground(Color.white);
            coin = new JLabel(new ImageIcon(coinIcon));
            bomb = new JLabel(new ImageIcon(bombIcon));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initialize() {
        setSize(500, 70);
        setLocation(10, data.staticData.screenSize.height-getHeight()-40);
        setBackground(new Color(0,191,255,70));
        setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
    }

    public void refresh() {
        readData();
        lifeCount.setText(lifeINT.toString());
        coinCount.setText(coinINT.toString());
        bombCount.setText(bombINT.toString());
//        shotLevelCount.setText(shotINT.toString());
        lifeCount.repaint();
//        System.out.println("in refresh. life: "+lifeINT);
        coinCount.repaint();
        bombCount.repaint();
//        shotLevelCount.repaint();
    }
}
