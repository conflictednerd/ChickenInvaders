package Base;

import Swing.GamePanel;

import javax.swing.*;
import java.awt.*;

public class GraphicEngine extends Thread {

    private Data data;
    private GamePanel gamePanel;

    public GraphicEngine(GamePanel gamePanel){
        super();
        this.gamePanel = gamePanel;
    }
    public GraphicEngine(Data data){
        super();
        this.data = data;
        this.gamePanel = data.gamePanel;
    }

    @Override
    public void run() {
        super.run();
        long time = System.currentTimeMillis();
        int fps = 0;
        boolean running = true;
        while (running) {
            if (!data.isPaused) {
//            long beginTime = System.currentTimeMillis();
                //TODO for animation of elements I should add some code in here so that every .5 seconds the image for every element changes. It should use a counter for time and a method in animatable interface for animatable elements.
                gamePanel.repaint();
                Toolkit.getDefaultToolkit().sync();
                //fps code
                fps++;
                if (System.currentTimeMillis() - time >= 1000) {
                    time = System.currentTimeMillis();
                    System.out.println("fps: " + fps);
                    fps = 0;
                }

                try {
                    //around 90fps
                    GraphicEngine.sleep(10);
//                GraphicEngine.sleep(15 - (System.currentTimeMillis()-beginTime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
