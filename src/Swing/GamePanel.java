package Swing;

import Base.Data;
import Base.GraphicEngine;
import Elements.Bomb;
import Elements.Enemy;
import Elements.EnemyShot;
import Elements.Rocket;
import Elements.Shots.Shot;
import Elements.Shots.Shot1;
import Elements.Shots.Shot2;
import Elements.Shots.Shot3;
import Elements.Upgrades.Upgrade;

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

    final int mousePressed = -23, bombPressed = -24;
    public Data data;
    private PauseDialog pauseDialog = null;
    private StatPanel statPanel;
    private JLabel scoreLabel = new JLabel("Score: 0");
    private Image back = null;

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
                if(!data.dynamicData.isPaused) {
                    data.dynamicData.rocket.setX(mouseEvent.getX());
                    data.dynamicData.rocket.setY(mouseEvent.getY());
                }
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                if(!data.dynamicData.isPaused) {
                    data.dynamicData.rocket.setX(mouseEvent.getX());
                    data.dynamicData.rocket.setY(mouseEvent.getY());
                }
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {}

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if(!data.dynamicData.isPaused) {
                    if(SwingUtilities.isLeftMouseButton(mouseEvent)) {
                        synchronized (data.staticData.pressedKeys) {
                            //-23 is used to indicate mouse pressed in KeyEvent.VK....
                            data.staticData.pressedKeys.add(mousePressed);
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                if(!data.dynamicData.isPaused) {
                    if(SwingUtilities.isLeftMouseButton(mouseEvent)) {
                        synchronized (data.staticData.pressedKeys) {
                            data.staticData.pressedKeys.remove(mousePressed);
                        }
                    }
                    else if(SwingUtilities.isRightMouseButton(mouseEvent)){
                        data.staticData.pressedKeys.add(bombPressed);
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
                if(!data.dynamicData.isPaused) {
                    synchronized (data.staticData.pressedKeys) {
                        data.staticData.pressedKeys.add(keyEvent.getKeyCode());
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                //Code for game pause.
                if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) esc();
                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
                    data.staticData.pressedKeys.add(bombPressed);
                }
                if(!data.dynamicData.isPaused) {
                    synchronized (data.staticData.pressedKeys) {
                        data.staticData.pressedKeys.remove(keyEvent.getKeyCode());
                    }
                }
            }
        });

        try {
            back = ImageIO.read(GamePanel.class.getResourceAsStream("../Assets/BackGrounds/0.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void drawStatPanel() {
        statPanel = new StatPanel(this.data);
        add(statPanel);

        scoreLabel.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE|Font.BOLD, 50));
        scoreLabel.setSize(400, 50);
        scoreLabel.setForeground(Color.white);
        scoreLabel.setLocation((int)data.staticData.screenSize.getWidth()-scoreLabel.getWidth()-200, 10);
        add(scoreLabel);
    }

    public void repaintStatPanel(){
        for(Component c: getComponents()){
            if(c instanceof StatPanel){
                StatPanel statPanel = (StatPanel)c;
                statPanel.data = data;
                statPanel.refresh();
            }
            if(c instanceof JLabel){
                if(((JLabel) c).getText().startsWith("Score: ")){
                    ((JLabel)c).setText("Score: "+ data.dynamicData.player.score);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
        data.dynamicData.rocket.draw((Graphics2D)g);
//        System.err.println("Number of shots = " + data.dynamicData.shots.size());
        for(Shot shot:data.dynamicData.shots) { shot.draw((Graphics2D)g); }
        for(Bomb bomb:data.dynamicData.bombs) bomb.draw((Graphics2D)g);
        for(Enemy enemy:data.dynamicData.enemies) enemy.draw((Graphics2D)g);
        for(EnemyShot enemyShot:data.dynamicData.enemyShots) enemyShot.draw((Graphics2D)g);
        for(Upgrade upgrade:data.dynamicData.upgrades) upgrade.draw((Graphics2D)g);
        for(Rocket r:data.dynamicData.rockets) {
            if(!r.getOwner().equals(data.dynamicData.rocket.getOwner()))
                r.draw((Graphics2D)g);
        }
        GraphicEngine.fps++;
    }


    public void syncMouse(){
        try {
            Robot robot = new Robot();
            robot.mouseMove(data.dynamicData.rocket.getX() + (int)getLocationOnScreen().getX(), data.dynamicData.rocket.getY() + (int)getLocationOnScreen().getY());
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void esc(){
        if(data.dynamicData.isPaused && data.staticData.pauseDialogOpened){
            pauseDialog.dispose();
            data.staticData.pauseRequest = false;
            syncMouse();
        }
        else{
            //todo order should come from server:
            pauseDialog = new PauseDialog(data);
            data.staticData.pauseDialogOpened = true;
            data.staticData.pauseRequest = true;
//            System.out.println(data.toJSON());
        }
//        System.err.println("In esc(). data.isPaused: "+ data.dynamicData.isPaused + " data.pauseRequest: "+ data.staticData.pauseRequest + " data.pauseDialogOpende: "+ data.staticData.pauseDialogOpened);
//        synchronized (data.dynamicData.isPaused) {
//            data.dynamicData.isPaused = !data.dynamicData.isPaused;
//        }
    }

}
