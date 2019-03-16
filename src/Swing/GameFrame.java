package Swing;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public Container contentPane;

    public GameFrame(){
        super("Chicken Invaders v1.0");

        contentPane = getContentPane();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
    }

}
