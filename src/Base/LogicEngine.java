package Base;

import Elements.Bomb;
import Elements.Shot;

import java.awt.event.KeyEvent;

public class LogicEngine extends Thread{

    final int mousePressed = -23;
    private Data data;
    private SoundThread soundThread = new SoundThread();

    public LogicEngine(Data data){
        super();
        this.data = data;
        soundThread.start();
    }

    @Override
    public void run() {
        //TODO isPaused should be a shared volatile variable that's the same in Game, GE and LE.
        boolean waitingForShotCooldown = false;
        Long coolDownTimer = 0l, shootingTimer = null, timeOfLastShot = 0l;

        while(true) {
            if (!data.isPaused) {

                if(Shot.shotHeat >= Shot.maxHeat && !waitingForShotCooldown){
                    waitingForShotCooldown = true;
                    data.rocket.coolDown=true;
                    coolDownTimer = System.currentTimeMillis();
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

//Case1: not in cooldown mode and is shooting
                    if(!waitingForShotCooldown && (data.pressedKeys.contains(KeyEvent.VK_SPACE) || data.pressedKeys.contains(mousePressed))){
                        shootingTimer = null;
                        if(System.currentTimeMillis() - timeOfLastShot >= Shot.timeBetweenConsecutiveShots){
                            data.shots.add(new Shot(data.rocket.getX(), data.rocket.getY()));
                            soundThread.addShotSound();
                            timeOfLastShot = System.currentTimeMillis();
                        }
                    }

//Case2: not in cooldown mode and not shooting
                    else if(!waitingForShotCooldown && !data.pressedKeys.contains(KeyEvent.VK_SPACE) && !data.pressedKeys.contains(mousePressed)){
                        if(shootingTimer==null)
                            shootingTimer = System.currentTimeMillis();
                        else{
                            while (System.currentTimeMillis() - shootingTimer >= Shot.coolDownTimeMillis){
                                Shot.reduceHeat();
                                shootingTimer += Shot.coolDownTimeMillis;
                            }
                        }
                    }

//Case3: in cooldown mode. don't care if shooting or not.
                    else {
                        if (waitingForShotCooldown && Shot.shotHeat>0) {
                            while (System.currentTimeMillis() - coolDownTimer >= Shot.coolDownTimeMillis) {
                                Shot.reduceHeat();
                                coolDownTimer += Shot.coolDownTimeMillis;
                            }
                        }
                    }
                    if(Shot.shotHeat <= 0){
                        Shot.shotHeat = 0;
                        waitingForShotCooldown = false;
                        data.rocket.coolDown = false;
                    }
                }
                try {
//                LogicEngine.sleep(20 - System.currentTimeMillis() + beginTime);
                    LogicEngine.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
