package Swing;

import Base.Game;

import javax.swing.*;
import java.awt.*;

public class ServerCreationPanel extends JPanel {
    private Game game;
    private JLabel infoLabel, infoLabel1, infoLabel2;
    private JTextField portField, maxPlayersField, maxLevelField;
    private JButton create, back;

    public ServerCreationPanel(Game game){
        this.game = game;

        initialize();

        setVisible(true);
    }

    private void initialize() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        infoLabel = new JLabel("Enter port to create a server on:");
        infoLabel.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 30));
        infoLabel.setMaximumSize(infoLabel.getPreferredSize());

        infoLabel1 = new JLabel("Maximum Players: ");
        infoLabel1.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 30));
        infoLabel1.setMaximumSize(infoLabel1.getPreferredSize());

        infoLabel2 = new JLabel("Number of Levels: ");
        infoLabel2.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 30));
        infoLabel2.setMaximumSize(infoLabel2.getPreferredSize());

        portField = new JTextField("Enter port", 10);
        portField.setToolTipText("Try 2323!");
        portField.setFont(new Font(Font.DIALOG_INPUT, Font.ROMAN_BASELINE, 25));
        portField.setMaximumSize(new Dimension(400, 40));

        maxLevelField = new JTextField("4", 2);
        maxLevelField.setToolTipText("must be less than or equal to 4");
        maxLevelField.setFont(new Font(Font.DIALOG_INPUT, Font.ROMAN_BASELINE, 25));
        maxLevelField.setMaximumSize(new Dimension(50, 40));

        maxPlayersField = new JTextField("2", 2);
        maxPlayersField.setFont(new Font(Font.DIALOG_INPUT, Font.ROMAN_BASELINE, 25));
        maxPlayersField.setMaximumSize(new Dimension(50, 40));

        create = new JButton("Start!");
        create.setPreferredSize(new Dimension(50, 100));
        create.addActionListener(actionEvent -> {
            int port, maxPlayers, maxLevels;
            try {
                port = Integer.parseInt(portField.getText());
                maxPlayers = Integer.parseInt(maxPlayersField.getText());
                maxLevels = Integer.parseInt(maxLevelField.getText());
                game.playAsServer(port, maxPlayers, maxLevels);
            }
            catch (Exception e){
                infoLabel.setText("Oops! Try again!");
            }


        });

        back = new JButton("Back");
        back.setMinimumSize(new Dimension(100, 300));
        back.addActionListener(actionEvent -> game.load_intro());

        add(infoLabel);
        add(portField);
        add(infoLabel1);
        add(maxPlayersField);
        add(infoLabel2);
        add(maxLevelField);
        add(create);
        add(back);
    }

}
