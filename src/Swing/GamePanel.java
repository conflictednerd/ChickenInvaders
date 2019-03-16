package Swing;

import Base.Data;
import Elements.Shot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GamePanel extends JPanel {

    public Data data;

    public GamePanel(Data data) {
        this.data  = data;

        setLayout(null);
        setVisible(true);

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                //TODO should also fire.
                data.rocket.setX(mouseEvent.getX());
                data.rocket.setY(mouseEvent.getY());
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                data.rocket.setX(mouseEvent.getX());
                data.rocket.setY(mouseEvent.getY());
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                data.shots.add(new Shot(mouseEvent.getX(), mouseEvent.getY()));
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        data.rocket.draw((Graphics2D)g);
        for(Shot shot: data.shots) shot.draw((Graphics2D)g);
    }

}
