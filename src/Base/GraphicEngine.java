package Base;

import Swing.GamePanel;

import java.awt.*;

public class GraphicEngine extends Thread {

    private Data data;
    private GamePanel gamePanel;

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
        gamePanel.drawStatPanel();
        while (data.GERunning) {
            if (!data.isPaused) {
//            long beginTime = System.currentTimeMillis();
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
        System.err.println("GE out!");
    }
}
