package Swing;

import Base.Game;
import Swing.Template.RoundedBorder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class IntroPanel extends JPanel {

    Game game;
    JButton play, scores,setting, quit;
    Image intro1, title;
    final int BUTTON_WIDTH = 700 ,BUTTON_HEIGHT = 50, BUTTON_SPACE = 20;

    public IntroPanel(Game game){
        super();
        this.game = game;
        initialize();

        setVisible(true);
        repaint();
    }

    private void initialize(){
        setLayout(null);

        try {
            intro1 = ImageIO.read(IntroPanel.class.getResourceAsStream("../Assets/BackGrounds/intro1.jpg"));
            title = ImageIO.read(IntroPanel.class.getResourceAsStream("../Assets/BackGrounds/title.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        play = new JButton("Play!");
        play.setBorder(new RoundedBorder(20));
        play.setOpaque(false);
        play.setBounds( (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/3, BUTTON_WIDTH, BUTTON_HEIGHT);
        play.setForeground(Color.white);
        play.setBackground(Color.MAGENTA);
        play.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE, 40));
        play.addActionListener(actionEvent -> game.play());
        add(play);



        scores = new JButton("Scores");
        scores.setBorder(new RoundedBorder(20));
        scores.setOpaque(false);
        scores.setBounds( (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/3 + BUTTON_HEIGHT + BUTTON_SPACE, BUTTON_WIDTH, BUTTON_HEIGHT);
        scores.setForeground(Color.white);
        scores.setBackground(Color.MAGENTA);
        scores.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE, 40));
        add(scores);

        setting = new JButton("Settings");
        setting.setBorder(new RoundedBorder(20));
        setting.setOpaque(false);
        setting.setBounds( (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/3 + 2*BUTTON_HEIGHT + 2* BUTTON_SPACE, BUTTON_WIDTH, BUTTON_HEIGHT);
        setting.setForeground(Color.white);
        setting.setBackground(Color.MAGENTA);
        setting.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE, 40));
        add(setting);

        quit = new JButton("Quit");
        quit.setBorder(new RoundedBorder(20));
        quit.setOpaque(false);
        quit.setBounds( (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/3 + 3*BUTTON_HEIGHT + 3*BUTTON_SPACE, BUTTON_WIDTH, BUTTON_HEIGHT);
        quit.setForeground(Color.white);
        quit.setBackground(Color.MAGENTA);
        quit.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE, 40));
        quit.addActionListener(actionEvent -> System.exit(0));
        add(quit);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(intro1, 0, 0, getWidth(), getHeight(), this);
        g2.drawImage(title, (getWidth() - title.getWidth(null)) / 2, 20, this);
    }
}
