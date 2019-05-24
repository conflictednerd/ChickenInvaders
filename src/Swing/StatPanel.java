package Swing;

import Base.Data;
import Base.Player;
import Swing.Template.RoundedPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class StatPanel extends RoundedPanel {
    Data data;
    Image heartIcon, foodIcon, bombIcon, shotIcon;
    public JLabel life = null, lifeCount, food = null, foodCount, bomb = null, bombCount, shot = null, shotLevelCount;
    public Integer lifeINT, foodINT, bombINT, shotINT;


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

        foodCount = new JLabel(foodINT.toString());
        foodCount.setFont(new Font(Font.DIALOG, Font.LAYOUT_LEFT_TO_RIGHT | Font.BOLD | Font.CENTER_BASELINE, 38));
        foodCount.setForeground(Color.white);

        bombCount = new JLabel(bombINT.toString());
        bombCount.setFont(new Font(Font.DIALOG, Font.LAYOUT_LEFT_TO_RIGHT | Font.BOLD | Font.CENTER_BASELINE, 38));
        bombCount.setForeground(Color.white);
    }

    private void readData() {
        lifeINT = data.player.life;
        foodINT = data.player.food;
        bombINT = data.player.bombs;
        shotINT = data.player.shotLevel;
    }

    private void addLabels() {
        add(life);
        add(lifeCount);
        add(food);
        add(foodCount);
        add(bomb);
        add(bombCount);
    }

    private void readImages() {
        try {
            heartIcon = ImageIO.read(StatPanel.class.getResourceAsStream("../Assets/icons/heart.png"));
            foodIcon = ImageIO.read(StatPanel.class.getResourceAsStream("../Assets/icons/food.png"));
            bombIcon = ImageIO.read(StatPanel.class.getResourceAsStream("../Assets/icons/bomb.png"));
            life = new JLabel(new ImageIcon(heartIcon));
            life.setForeground(Color.white);
            food = new JLabel(new ImageIcon(foodIcon));
            bomb = new JLabel(new ImageIcon(bombIcon));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initialize() {
        setSize(500, 70);
        setLocation(10, data.screenSize.height-getHeight()-40);
        setBackground(new Color(0,191,255,70));
        setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
    }

    public void refresh() {
        readData();
        lifeCount.setText(lifeINT.toString());
        foodCount.setText(foodINT.toString());
        bombCount.setText(bombINT.toString());
//        shotLevelCount.setText(shotINT.toString());
        lifeCount.repaint();
        foodCount.repaint();
        bombCount.repaint();
//        shotLevelCount.repaint();
    }
}
