package Base;

import Elements.Shot;

import java.awt.*;
import java.awt.event.KeyEvent;

public class LogicEngine extends Thread{

    Data data;

    public LogicEngine(Data data){
        super();
        this.data = data;
    }

    @Override
    public void run() {
        //TODO running should be a shared volatile variable that's the same in Game, GE and LE.
        boolean running = true;
        //counter for space key pressed to handle shooting. can be used in future for animation and stuff.
        int shootCounter = 0;

        while(running) {
            long beginTime = System.currentTimeMillis();
            synchronized (data.shots) {
                for (Shot shot : data.shots) {
                    if (shot.getY() < 0) data.shots.remove(shot);
                    else shot.move();
                }
            }


            //Code for handling keyboard input comes here.
            synchronized (data.pressedKeys) {
                for (int i : data.pressedKeys) {
                    if (i == KeyEvent.VK_DOWN && data.rocket.getY() < data.screenSize.height) {
                        data.rocket.setY(data.rocket.getY() + 5);
                    }
                    if (i == KeyEvent.VK_UP && data.rocket.getY() > 0) {
                        data.rocket.setY(data.rocket.getY() - 5);
                    }
                    if (i == KeyEvent.VK_LEFT && data.rocket.getX() > 0) {
                        data.rocket.setX(data.rocket.getX() - 5);
                    }
                    if (i == KeyEvent.VK_RIGHT && data.rocket.getX() < data.screenSize.width) {
                        data.rocket.setX(data.rocket.getX() + 5);
                    }
                    if (i == KeyEvent.VK_SPACE) {
                        shootCounter++;
                        shootCounter %= 10;
                        if(shootCounter == 1)
                            data.shots.add(new Shot(data.rocket.getX(), data.rocket.getY()));
                    }
                }
            }
            try {
//                LogicEngine.sleep(15 - System.currentTimeMillis() + beginTime);
                LogicEngine.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
