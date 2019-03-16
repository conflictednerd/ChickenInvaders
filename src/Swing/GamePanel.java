package Swing;

import Base.Data;
import Elements.Shot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel {

    public Data data;
//    public Image back;

    public GamePanel(Data data) {
        this.data  = data;

//        try {
//            back = ImageIO.read(GamePanel.class.getResourceAsStream("../Assets/Optimized-Background.jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //Disappearing the cursor
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        setCursor(blankCursor);

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
                //TODO add a shooting thread to shoot every 0.2 seconds. when mouse is pressed it's activated and when mouse is released it pauses.
                //TODO It might be a good idea to make shooting thread part of Shot class so that every shot type has its own rate of fire. Or we can add rate_of_fire to Shot class.

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
        try {
            g.drawImage(ImageIO.read(GamePanel.class.getResourceAsStream("../Assets/Optimized-Background.jpg")), 0, 0, getWidth(), getHeight(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        data.rocket.draw((Graphics2D)g);
        for(Shot shot: data.shots) shot.draw((Graphics2D)g);
    }

}
