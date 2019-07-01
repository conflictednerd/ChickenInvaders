package Swing;

import Base.Game;

import javax.swing.*;
import java.awt.*;

public class ServerWaitingPanel extends JPanel {
    private Game game;
    private JLabel counterLabel, textLabel;
    private JButton startButton;

    public ServerWaitingPanel(Game game){
        this.game = game;

        initialize();

        setVisible(true);
    }

    private void initialize() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        textLabel = new JLabel("Current connected players:    ");
        textLabel.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 30));
        textLabel.setMaximumSize(textLabel.getPreferredSize());

        counterLabel = new JLabel("0");
        counterLabel.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 30));
        counterLabel.setMaximumSize(counterLabel.getPreferredSize());

        startButton = new JButton("Start The Game!");
        startButton.addActionListener(actionEvent -> game.startServerGame());

        add(textLabel);
        add(counterLabel);
        add(startButton);
    }

    public JLabel getCounterLabel() {
        return counterLabel;
    }
}
