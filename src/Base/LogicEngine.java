package Base;

import Elements.*;
import Elements.Enemies.Enemy1;

import java.awt.event.KeyEvent;

public class LogicEngine extends Thread{

    final int mousePressed = -23;
    private LevelManager levelManager;
    private boolean inTransition = false;
    private Data data;
    private SoundThread soundThread = new SoundThread();

    public LogicEngine(Data data){
        super();
        this.data = data;
        soundThread.start();
    }

    public void setLevelManager(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @Override
    public void run() {
        boolean waitingForShotCooldown = false;
        Long coolDownTimer = 0l, shootingTimer = null, timeOfLastShot = 0l;

        while(true) {
            if (!data.isPaused) {

                if(Shot.shotHeat >= Shot.maxHeat && !waitingForShotCooldown){
                    waitingForShotCooldown = true;
                    data.rocket.coolDown=true;
                    coolDownTimer = System.currentTimeMillis();
                }


                if(data.enemies.size() == 0){
                    inTransition = true;
                    levelManager.nextWave(data.enemies);
                    System.err.println(data.enemies.size());
                }

                if(inTransition){
                    synchronized (data.enemies) {
                        inTransition = levelManager.transition(data.enemies);
                    }
                }
                else {
                    synchronized (data.enemies) {
                        for (Enemy enemy : data.enemies) {
                            enemy.move();
                            EnemyShot tmp = enemy.shoot();
                            if (tmp != null) data.enemyShots.add(tmp);
                        }
                    }
                }

                synchronized (data.shots) {
                    for (Shot shot : data.shots) {
                        if (shot.getY() < 0) data.shots.remove(shot);
                        else shot.move();
                    }
                }

                synchronized (data.enemyShots){
                    for(EnemyShot shot: data.enemyShots){
                        if(shot.getCenterY() > Data.screenSize.getHeight()) data.enemyShots.remove(shot);
                        else shot.move();
                    }
                }

                synchronized (data.bombs) {
                    for(Bomb bomb: data.bombs){
                        bomb.move();
                    }
                }


                collisionHandler();

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

//Case1: not in cooldown mode and is shooting then shoot
                    if(!waitingForShotCooldown && (data.pressedKeys.contains(KeyEvent.VK_SPACE) || data.pressedKeys.contains(mousePressed))){
                        shootingTimer = null;
                        if(System.currentTimeMillis() - timeOfLastShot >= Shot.timeBetweenConsecutiveShots && data.rocket.isAlive()){
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

    private void collisionHandler() {
        synchronized (data.enemies){
            synchronized (data.shots){
                for(Enemy enemy:data.enemies){
                    for(Shot shot: data.shots){
                        if(intersect((Enemy1)enemy, shot)){
                            data.shots.remove(shot);
                            data.enemies.remove(enemy);
                        }
                    }
                }
            }
        }

        synchronized (data.enemyShots) {
            if (data.rocket.isAlive()) {
                for (EnemyShot enemyShot : data.enemyShots) {
                    if (intersect(data.rocket, enemyShot)) {
                        data.rocket.explode();
                        data.player.life--;
                        data.gamePanel.repaintStatPanel();
                        data.enemyShots.remove(enemyShot);
                        break;
                    }
                }
            }
        }
    }


    private boolean intersect(Rocket rocket, EnemyShot shot){
        if(shot.getCenterX()<rocket.getX() + rocket.getWidth()/2
                && shot.getCenterX()>rocket.getX()-rocket.getWidth()/2
                && shot.getCenterY()<rocket.getY() + rocket.getHeight()/2
                && shot.getCenterY()>rocket.getY() - rocket.getHeight()/2)
            return true;
        return false;
    }

    private boolean intersect(Enemy1 enemy, Shot shot) {

        if(shot.getX()<enemy.getCenterX() + enemy.getWidth()/2
                && shot.getX()>enemy.getCenterX()-enemy.getWidth()/2
                && shot.getY()<enemy.getCenterY() + enemy.getHeight()/2
                && shot.getY()>enemy.getCenterY() - enemy.getHeight()/2){
            return true;
        }
        return false;
    }
}
