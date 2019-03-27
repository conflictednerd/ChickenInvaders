package Base;

import Elements.Bomb;
import Elements.Shot;

import java.awt.*;
import java.awt.event.KeyEvent;

public class LogicEngine extends Thread{

    final int mousePressed = -23;
    private Data data;

    public LogicEngine(Data data){
        super();
        this.data = data;
    }

    @Override
    public void run() {
        //TODO isPaused should be a shared volatile variable that's the same in Game, GE and LE.
        boolean waitingForShotCooldown = false;
        int shotCoolDownCounter = 0;
        int shootCounter = 0;

        while(true) {
            if (!data.isPaused) {
                long beginTime = System.currentTimeMillis();

                if(Shot.shotHeat >= Shot.maxHeat){
                    waitingForShotCooldown = true;
                    data.rocket.coolDown = true;
//                    System.err.println("COOLINGDOWN");
                }

                synchronized (data.shots) {
                    for (Shot shot : data.shots) {
                        if (shot.getY() < 0) data.shots.remove(shot);
                        else shot.move();
                    }
                }

                synchronized (data.bombs) {
                    for(Bomb bomb: data.bombs){
                        bomb.move();
                    }
                }


                //Code for handling keyboard input comes here.
                synchronized (data.pressedKeys) {
                    if (data.pressedKeys.contains(KeyEvent.VK_DOWN) && data.rocket.getY() < data.screenSize.height) {
                        data.rocket.setY(data.rocket.getY() + 10);
                        data.gamePanel.syncMouse();
                    }
                    if (data.pressedKeys.contains(KeyEvent.VK_UP) && data.rocket.getY() > 0) {
                        data.rocket.setY(data.rocket.getY() - 10);
                        data.gamePanel.syncMouse();
                    }
                    if (data.pressedKeys.contains(KeyEvent.VK_LEFT) && data.rocket.getX() > 0) {
                        data.rocket.setX(data.rocket.getX() - 10);
                        data.gamePanel.syncMouse();
                    }
                    if (data.pressedKeys.contains(KeyEvent.VK_RIGHT) && data.rocket.getX() < data.screenSize.width) {
                        data.rocket.setX(data.rocket.getX() + 10);
                        data.gamePanel.syncMouse();
                    }

                    if(!waitingForShotCooldown && (data.pressedKeys.contains(KeyEvent.VK_SPACE) || data.pressedKeys.contains(mousePressed))){
                        shootCounter++;
                        shootCounter %= 10;
                        if (shootCounter == 1)
                            data.shots.add(new Shot(data.rocket.getX(), data.rocket.getY()));
                    }
                    else{
                        if(Shot.shotHeat > 0) Shot.shotHeat--;
                        shotCoolDownCounter++;
                        shotCoolDownCounter %= Shot.HEAT_OFF_TIME;
                        if(shotCoolDownCounter == 0) {
                            waitingForShotCooldown = false;
                            data.rocket.coolDown = false;
                            shotCoolDownCounter = Shot.HEAT_OFF_TIME;
//                            System.err.println("FIRING!");
                        }
                    }
                }
                try {
//                LogicEngine.sleep(15 - System.currentTimeMillis() + beginTime);
                    LogicEngine.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
