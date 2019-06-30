package Base;

import Swing.GamePanel;

import java.awt.*;

public class GraphicEngine extends Thread {

    public static int fps = 0;
    private Data data;
    private GamePanel gamePanel;

    public GraphicEngine(Data data){
        super();
        this.data = data;
        this.gamePanel = data.staticData.gamePanel;
    }

    @Override
    public void run() {
        data.dynamicData.rocket.setOwner(data.dynamicData.player.name);
        long time = System.currentTimeMillis();
        gamePanel.drawStatPanel();
        while (data.dynamicData.GERunning) {
            if (!data.dynamicData.isPaused) {
                //THis might cause lower frame per second.
                synchronized (data) {
                    data.staticData.gamePanel.repaintStatPanel();
//                    gamePanel.syncMouse();
                    gamePanel.repaint();
                }
//                System.out.println("number of shots: " + data.dynamicData.shots.size());
                Toolkit.getDefaultToolkit().sync();
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
