package Swing;

import Base.Game;

import javax.swing.*;
import java.awt.*;

public class ClientCreationPanel extends JPanel {
    private Game game;
    private JLabel info1, info2;
    private JTextField ipField, portField;
    private JButton connect;
    private boolean isObserver = false;

    public ClientCreationPanel(Game game, boolean isObserver){
        this.game = game;
        this.isObserver = isObserver;

        initialize();

        setVisible(true);
    }

    private void initialize() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        info1 = new JLabel("Enter the ip of server:\t\t");
        info1.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 30));
        info1.setMaximumSize(info1.getPreferredSize());

        info2 = new JLabel("Enter the port of server:\t\t");
        info2.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 30));
        info2.setMaximumSize(info2.getPreferredSize());

        ipField = new JTextField("Enter ip", 10);
        ipField.setToolTipText("Try \"localhost\" or \"127.0.0.1\"!");
        ipField.setFont(new Font(Font.DIALOG_INPUT, Font.ROMAN_BASELINE, 25));
        ipField.setMaximumSize(new Dimension(400, 40));

        portField = new JTextField("Enter port", 10);
        portField.setToolTipText("Try 2323!");
        portField.setFont(new Font(Font.DIALOG_INPUT, Font.ROMAN_BASELINE, 25));
        portField.setMaximumSize(new Dimension(400, 40));

        connect = new JButton("Connect!");
        connect.setPreferredSize(new Dimension(50, 100));
        connect.addActionListener(actionEvent -> {
            try {
                game.playAsClient(ipField.getText(), Integer.parseInt(portField.getText()), isObserver);
            }
            catch (Exception e){
                info1.setText("Oops! Try again!");
            }
        });

        add(info1);
        add(ipField);
        add(info2);
        add(portField);
        add(connect);
    }
}
