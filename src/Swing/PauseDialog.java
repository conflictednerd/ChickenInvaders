package Swing;

import javax.swing.*;
import java.awt.*;

public class PauseDialog extends JDialog {

    public JPanel contentPane;

    public PauseDialog(){
        super();
        contentPane = (JPanel) getContentPane();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 500);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth())/2, (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight())/2);
//        setUndecorated(true);
        requestFocus();
        setVisible(true);
    }

}
