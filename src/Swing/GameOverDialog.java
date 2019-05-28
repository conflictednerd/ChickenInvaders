package Swing;

import Base.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Because of technical issues!! I don't use it in the code but if i just uncomment the parts in logic engine
 * ...it's good to go.
 * I can even start a new game here instead of closing the program. I just need to dispose previous gameFrame...
 * ... stop previous LE and GE, and create a new game.
 */
public class GameOverDialog extends JDialog {
    private JPanel contentPane;
    private Data data;

    public GameOverDialog(Data data){
        super(data.gameFrame);
        this.data = data;
        contentPane = (JPanel) getContentPane();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth())/2, (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight())/2);
        setUndecorated(true);
//        setOpacity(0.4f);
        requestFocus();
        setShape(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),50,50));
//        contentPane.add(new PausePanel(this));
        contentPane.add(new GameOverPanel(this));
        setVisible(true);
    }
}
