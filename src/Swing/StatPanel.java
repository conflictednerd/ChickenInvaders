package Swing;

import Base.Data;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class StatPanel extends RoundedPanel {
    Data data;
    Image heartIcon, foodIcon, bombIcon, shotIcon;
    JLabel life = null, lifeCount, food = null, foodCount, bomb = null, bombCount, shot = null, shotLevelCount;


    public StatPanel(Data data) {
        super();
        this.data = data;
        initialize();
        readData();

        lifeCount = new JLabel(data.player.life.toString());
        lifeCount.setFont(new Font(Font.SERIF, Font.LAYOUT_LEFT_TO_RIGHT | Font.BOLD | Font.CENTER_BASELINE, 38));

        foodCount = new JLabel(data.player.food.toString());
        foodCount.setFont(new Font(Font.SERIF, Font.LAYOUT_LEFT_TO_RIGHT | Font.BOLD | Font.CENTER_BASELINE, 38));

        bombCount = new JLabel(data.player.bombs.toString());
        bombCount.setFont(new Font(Font.SERIF, Font.LAYOUT_LEFT_TO_RIGHT | Font.BOLD | Font.CENTER_BASELINE, 38));

        addLabels();
    }

    private void addLabels() {
        add(life);
        add(lifeCount);
        add(food);
        add(foodCount);
        add(bomb);
        add(bombCount);
    }

    private void readData() {
        try {
            heartIcon = ImageIO.read(StatPanel.class.getResourceAsStream("../Assets/icons/heart.png"));
            foodIcon = ImageIO.read(StatPanel.class.getResourceAsStream("../Assets/icons/food.png"));
            bombIcon = ImageIO.read(StatPanel.class.getResourceAsStream("../Assets/icons/bomb.png"));
            life = new JLabel(new ImageIcon(heartIcon));
            food = new JLabel(new ImageIcon(foodIcon));
            bomb = new JLabel(new ImageIcon(bombIcon));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initialize() {
        setSize(500, 70);
        setLocation(10, data.screenSize.height-getHeight()-40);
        setBackground(new Color(0,191,255,100));
        setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
    }

}