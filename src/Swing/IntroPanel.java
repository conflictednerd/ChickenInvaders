package Swing;

import Base.Game;
import Base.Player;
import Swing.Template.RoundedBorder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class IntroPanel extends JPanel {

    Game game;
    JButton play, createServer, joinServer, watch ,scores,setting, quit;
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
        play.addActionListener(actionEvent -> {
            game.data.staticData.isMultiPlayer = false;
            game.play();
        });
        setButton(play, 0);
        add(play);

        createServer = new JButton("Create Server");
        createServer.addActionListener(actionEvent -> {
            game.data.staticData.isMultiPlayer = true;
            game.load_server_creation();
        });
        setButton(createServer,1);
        add(createServer);

        joinServer = new JButton("Join a Server");
        joinServer.addActionListener(actionEvent -> {
            game.data.staticData.isMultiPlayer = true;
            game.load_client_creation(false);
        });
        setButton(joinServer,2);
        add(joinServer);

        watch = new JButton("Watch a game");
        watch.addActionListener(actionEvent -> {
            game.data.staticData.isMultiPlayer = true;
            game.load_client_creation(true);
        });
        setButton(watch, 3);
        add(watch);

        scores = new JButton("Scores");
        setButton(scores, 4);
        scores.addActionListener(actionEvent -> new RankingDialog((ArrayList<Player>) game.data.staticData.saveData.ranking));
        add(scores);

        setting = new JButton("Settings");
        setButton(setting, 5);
        add(setting);

        quit = new JButton("Quit");
        setButton(quit, 6);
        quit.addActionListener(actionEvent -> {
            game.data.staticData.database.close();
            System.exit(0);
        });
        add(quit);

    }

    private void setButton(JButton button, int index) {
        button.setBorder(new RoundedBorder(20));
        button.setOpaque(false);
        button.setBounds( (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/3 + (index-1)*(BUTTON_HEIGHT+BUTTON_SPACE), BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setForeground(Color.white);
        button.setBackground(Color.MAGENTA);
        button.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE, 40));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(intro1, 0, 0, getWidth(), getHeight(), this);
        g2.drawImage(title, (getWidth() - title.getWidth(null)) / 2, 20, this);
    }
}
