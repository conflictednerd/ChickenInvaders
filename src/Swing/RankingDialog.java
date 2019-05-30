package Swing;

import Base.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class RankingDialog extends JDialog {
    private JPanel contentPane;

    public RankingDialog(ArrayList<Player> players){
        contentPane = (JPanel) getContentPane();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 800);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth())/2, (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight())/2);
        setUndecorated(true);
//        setOpacity(0.4f);
        requestFocus();
        setShape(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),50,50));
        contentPane.add(new RankingPanel(players));
//        setBackground(Color.white);
        setVisible(true);
    }
}
