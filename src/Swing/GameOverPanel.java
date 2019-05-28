package Swing;

import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {
    private GameOverDialog god;
    public GameOverPanel(GameOverDialog gameOverDialog) {
        this.god = gameOverDialog;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("You Lost:((");
        Button exit = new Button("Quit");

        label.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE|Font.BOLD, 60));
        exit.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE|Font.BOLD,60));

        exit.addActionListener(actionEvent -> System.exit(-1));

        add(label);
        add(exit);

        setBackground(Color.white);
        setVisible(true);

    }
}
