package Swing;

import Base.Data;
import Elements.Bomb;
import Elements.Enemy;
import Elements.EnemyShot;
import Elements.Shot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/*
TODO Can't shoot with space and move with mouse. Don't know why yet.
TODO Adding (to the top) a label?? indicating weapon heat.
*/

public class GamePanel extends JPanel {

    final int mousePressed = -23;
    public Data data;
    private PauseDialog pauseDialog = null;
    private StatPanel statPanel;
    private JLabel scoreLabel = new JLabel("Score: 0");

    public GamePanel(Data data) {
        this.data  = data;

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
                        shootBomb();
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
                //TODO Better to move this to logic engine.
                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER) shootBomb();
                if(!data.isPaused) {
                    synchronized (data.pressedKeys) {
                        data.pressedKeys.remove(keyEvent.getKeyCode());
                    }
                }
            }
        });
    }

    private void shootBomb() {
        if(data.player.bombs>0) {
            data.player.bombs--;
            Bomb b = new Bomb(data.rocket.getX(), data.rocket.getY());
            b.calculateDefaultSpeeds();
            data.bombs.add(b);
            repaintStatPanel();
        }
    }

    public void drawStatPanel() {
        statPanel = new StatPanel(this.data);
        add(statPanel);

        scoreLabel.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE|Font.BOLD, 50));
        scoreLabel.setSize(400, 50);
        scoreLabel.setForeground(Color.white);
        scoreLabel.setLocation((int)data.screenSize.getWidth()-scoreLabel.getWidth()-200, 10);
        add(scoreLabel);
    }

    public void repaintStatPanel(){
        for(Component c: getComponents()){
            if(c instanceof StatPanel){
                StatPanel statPanel = (StatPanel)c;
                statPanel.refresh();
                break;
            }
        }
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
        for(Enemy enemy:data.enemies) enemy.draw((Graphics2D)g);
        for(EnemyShot enemyShot:data.enemyShots) enemyShot.draw((Graphics2D)g);
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
