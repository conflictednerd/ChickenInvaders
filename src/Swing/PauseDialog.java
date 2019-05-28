package Swing;

import Base.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.RoundRectangle2D;

public class PauseDialog extends JDialog {

    public JPanel contentPane;
    public Data data;

    public PauseDialog(Data data){
        super(data.gameFrame);
        this.data = data;
        contentPane = (JPanel) getContentPane();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 500);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth())/2, (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight())/2);
        setUndecorated(true);
//        setOpacity(0.4f);
        requestFocus();
        setShape(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),50,50));
        contentPane.add(new PausePanel(this));
        setVisible(true);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {}
            @Override
            public void keyPressed(KeyEvent keyEvent) {}
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
                    data.gamePanel.syncMouse();
                    data.isPaused = false;
                    dispose();
                }
            }
        });
    }

}
