package Swing;

import Base.Game;

import javax.swing.*;
import java.awt.*;

public class ServerCreationPanel extends JPanel {
    private Game game;
    private JLabel infoLabel;
    private JTextField portField;
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

        portField = new JTextField("Enter port", 10);
        portField.setToolTipText("Try 2323!");
        portField.setFont(new Font(Font.DIALOG_INPUT, Font.ROMAN_BASELINE, 25));
        portField.setMaximumSize(new Dimension(400, 40));

        create = new JButton("Start!");
        create.setPreferredSize(new Dimension(50, 100));
        create.addActionListener(actionEvent -> {
            int port;
            try {
                port = Integer.parseInt(portField.getText());
                game.playAsServer(port);
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
        add(create);
        add(back);
    }

}
