package Swing;

import Base.Data;
import Elements.Bomb;
import Elements.Shot;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/*
TODO Can't shoot with space and move with mouse. Don't know why yet.
TODO Adding (to the top) a label?? indicating weapon heat.
TODO Adding (to the bottom) a label?? containing info on stats.
TODO Adding Bomb.
TODO Adding Gson for save.
*/

public class GamePanel extends JPanel {

    final int mousePressed = -23;
    public Data data;
    private PauseDialog pauseDialog = null;

    public GamePanel(Data data) {
        this.data  = data;

        drawStatPanel();

        //Disappearing the cursor
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        setCursor(blankCursor);

        setFocusable(true);
        requestFocus();
        setLayout(null);
        setVisible(true);

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                if(!data.isPaused) {
                    data.rocket.setX(mouseEvent.getX());
                    data.rocket.setY(mouseEvent.getY());
                }
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                if(!data.isPaused) {
                    data.rocket.setX(mouseEvent.getX());
                    data.rocket.setY(mouseEvent.getY());
                }
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {}

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if(!data.isPaused) {
                    if(SwingUtilities.isLeftMouseButton(mouseEvent)) {
                        synchronized (data.pressedKeys) {
                            //-23 is used to indicate mouse pressed in KeyEvent.VK....
                            data.pressedKeys.add(mousePressed);
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                if(!data.isPaused) {
                    if(SwingUtilities.isLeftMouseButton(mouseEvent)) {
                        synchronized (data.pressedKeys) {
                            data.pressedKeys.remove(mousePressed);
                        }
                    }
                    else if(SwingUtilities.isRightMouseButton(mouseEvent)){
                        //TODO Fire Rocket:
                        data.bombs.add(new Bomb(data.rocket.getX(), data.rocket.getY()));
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {}
            @Override
            public void mouseExited(MouseEvent mouseEvent) {}
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {}

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(!data.isPaused) {
                    synchronized (data.pressedKeys) {
                        data.pressedKeys.add(keyEvent.getKeyCode());
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                //Code for game pause.
                if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) esc();
                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER) data.bombs.add(new Bomb(data.rocket.getX(), data.rocket.getY()));
                if(!data.isPaused) {
                    synchronized (data.pressedKeys) {
                        data.pressedKeys.remove(keyEvent.getKeyCode());
                    }
                }
            }
        });
    }

    private void drawStatPanel() {
        add(new StatPanel(this.data));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
//            g.drawImage(ImageIO.read(GamePanel.class.getResourceAsStream("../Assets/Optimized-Background.jpg")), 0, 0, getWidth(), getHeight(), this);
            g.drawImage(ImageIO.read(GamePanel.class.getResourceAsStream("../Assets/BackGrounds/0.png")), 0, 0, getWidth(), getHeight(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        data.rocket.draw((Graphics2D)g);
        for(Shot shot:data.shots) shot.draw((Graphics2D)g);
        for(Bomb bomb:data.bombs) bomb.draw((Graphics2D)g);
    }


    public void syncMouse(){
        try {
            Robot robot = new Robot();
            robot.mouseMove(data.rocket.getX() + (int)getLocationOnScreen().getX(), data.rocket.getY() + (int)getLocationOnScreen().getY());
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void esc(){
        if(data.isPaused){
            pauseDialog.dispose();
            syncMouse();
        }
        else{
            pauseDialog = new PauseDialog(data);
        }
        data.isPaused = !data.isPaused;
    }

}
