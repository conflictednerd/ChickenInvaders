package Base;

import Elements.*;
import Elements.Enemies.Boss;
import Elements.Enemies.Enemy2;
import Elements.Shots.Shot;
import Elements.Shots.Shot1;
import Elements.Shots.Shot2;
import Elements.Shots.Shot3;
import Elements.Upgrades.*;
import Swing.RankingDialog;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LogicEngine extends Thread{

    final int mousePressed = -23, bombPressed = -24;
    private LevelManager levelManager;
    private boolean inTransition = false, wavesFinished = false, finalSave = false;
    private Data data;
    private SoundThread soundThread = new SoundThread();
    private Random random = new Random();

    public LogicEngine(Data data){
        super();
        this.data = data;
        Shot.maxHeat += 5*data.dynamicData.player.rocketLevel;
        soundThread.start();
    }

    public void setLevelManager(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @Override
    public void run() {
        if(levelManager == null){
            throw new RuntimeException("LevelManager not set");
        }

        boolean waitingForShotCooldown = false;
        Long coolDownTimer = 0l, shootingTimer = null, timeOfLastShot = 0l, waitForNextWave = -1l;

        while(data.dynamicData.LERunning) {
            if(data.staticData.pauseRequest){
                data.dynamicData.isPaused = true;
            }
            else{
                data.dynamicData.isPaused = false;
            }

            if (!data.dynamicData.isPaused) {
                //Game Over
                if(data.dynamicData.player.life <= 0){
                    data.dynamicData.rocket.noLifeLeft = true;
//                    data.dynamicData.isPaused = true;
//                    GameOverDialog god = new GameOverDialog(data);
//                    data.dynamicData.LERunning = false;
//                    data.GERunning =false;
                }
                //First wave of new level
                if(data.dynamicData.player.subLevel == 0 && data.dynamicData.player.level > 0){
                    data.dynamicData.player.score += 3*data.dynamicData.player.coins;
                    data.dynamicData.player.coins =0;
                    data.staticData.gamePanel.repaintStatPanel();
                }
                //Game has been completed.
                if(wavesFinished && !finalSave){
                    data.dynamicData.player.timePlayed = Math.toIntExact((System.currentTimeMillis() - data.staticData.startTime) / 1000);
                    data.staticData.saveData.ranking.add(data.dynamicData.player);
                    data.save();
                    finalSave = true;
                    data.dynamicData.isPaused = true;

                    new RankingDialog((ArrayList<Player>) data.staticData.saveData.ranking);
                }

                if(Shot.shotHeat >= Shot.maxHeat && !waitingForShotCooldown){
                    waitingForShotCooldown = true;
                    data.dynamicData.rocket.coolDown=true;
                    coolDownTimer = System.currentTimeMillis();
                }

//If one wave is over, wait for 3 second, then call level manager for next wave.
                if(data.dynamicData.enemies.size() == 0 && !wavesFinished){
                    if(waitForNextWave <= 0)
                        waitForNextWave = System.currentTimeMillis();
                    else if(System.currentTimeMillis() - waitForNextWave >= 3000) {
                        waitForNextWave = -1l;
                        inTransition = true;
                        wavesFinished = levelManager.nextWave(data.dynamicData.enemies);
                    }
//                    System.err.println(data.dynamicData.enemies.size());
                }

                if(inTransition){
                    synchronized (data.dynamicData.enemies) {
                        inTransition = levelManager.transition(data.dynamicData.enemies);
                    }
                }
                else {
                    Enemy2.rocketX = data.dynamicData.rocket.getX();
                    Enemy2.rocketY = data.dynamicData.rocket.getY();
                    synchronized (data.dynamicData.enemies) {
                        for (Enemy enemy : data.dynamicData.enemies) {
                            enemy.move();
                            List<EnemyShot> tmp = enemy.shoot();
                            if (tmp != null && tmp.size() != 0) for(EnemyShot e:tmp)data.dynamicData.enemyShots.add(e);
                        }
                    }
                }

                synchronized (data.dynamicData.shots) {
                    for (Shot shot : data.dynamicData.shots) {
                        if (shot.getY() < 0) data.dynamicData.shots.remove(shot);
                        else shot.move();
                    }
                }

                synchronized (data.dynamicData.enemyShots){
                    for(EnemyShot shot: data.dynamicData.enemyShots){
                        if(shot.getCenterY() > data.staticData.screenSize.getHeight()) data.dynamicData.enemyShots.remove(shot);
                        else shot.move();
                    }
                }

                synchronized (data.dynamicData.upgrades){
                    for(Upgrade u: data.dynamicData.upgrades){
                        if(u.getCenterY() > data.staticData.screenSize.getHeight()) data.dynamicData.upgrades.remove(u);
                        else u.move();
                    }
                }

                synchronized (data.dynamicData.rocket) {
                    if (!data.dynamicData.rocket.noLifeLeft) {
                        if (data.dynamicData.rocket.isReviving()) {
                            //3 seconds reviving time.
                            if (System.currentTimeMillis() > data.dynamicData.rocket.reviveTime + 3000) data.dynamicData.rocket.setReviving(false);
                            data.dynamicData.rocket.nextReviveAnimation();
                        } else if (!data.dynamicData.rocket.isAlive()) {
                            // 7seconds disappear time.
                            if (System.currentTimeMillis() > data.dynamicData.rocket.killTime + 7000) {
                                data.dynamicData.rocket.setMouse();
                                data.dynamicData.rocket.setAlive(true);
                                data.dynamicData.rocket.setReviving(true);
                                data.dynamicData.rocket.reviveTime = System.currentTimeMillis();
                            }
                        }
                    } else {
                        data.dynamicData.rocket.setAlive(false);
                        data.dynamicData.rocket.setReviving(false);
                    }
                }

                synchronized (data.dynamicData.bombs) {
                    for(Bomb bomb: data.dynamicData.bombs){
                        if(!bomb.isActive){
                            data.dynamicData.bombs.remove(bomb);
                            synchronized (data.dynamicData.enemies){
                                for(Enemy e: data.dynamicData.enemies){
                                    e.health -= 50;
                                    if(e.health<=0.1){
                                        data.dynamicData.player.score += e.getLvl();
                                        data.dynamicData.enemies.remove(e);
                                        addPrize(e);
                                    }
                                }
                            }
                        }
                        else bomb.move();
                    }
                }


                collisionHandler();

                //Code for handling keyboard input comes here.
                synchronized (data.staticData.pressedKeys) {
                    if (data.staticData.pressedKeys.contains(KeyEvent.VK_DOWN) && data.dynamicData.rocket.getY() < data.staticData.screenSize.height) {
                        data.dynamicData.rocket.setY(data.dynamicData.rocket.getY() + 10);
                        data.staticData.gamePanel.syncMouse();
                    }
                    if (data.staticData.pressedKeys.contains(KeyEvent.VK_UP) && data.dynamicData.rocket.getY() > 0) {
                        data.dynamicData.rocket.setY(data.dynamicData.rocket.getY() - 10);
                        data.staticData.gamePanel.syncMouse();
                    }
                    if (data.staticData.pressedKeys.contains(KeyEvent.VK_LEFT) && data.dynamicData.rocket.getX() > 0) {
                        data.dynamicData.rocket.setX(data.dynamicData.rocket.getX() - 10);
                        data.staticData.gamePanel.syncMouse();
                    }
                    if (data.staticData.pressedKeys.contains(KeyEvent.VK_RIGHT) && data.dynamicData.rocket.getX() < data.staticData.screenSize.width) {
                        data.dynamicData.rocket.setX(data.dynamicData.rocket.getX() + 10);
                        data.staticData.gamePanel.syncMouse();
                    }
                    if(data.staticData.pressedKeys.contains(bombPressed)){
                        if(data.dynamicData.player.bombs>0 && data.dynamicData.rocket.isAlive()) {
                            data.dynamicData.player.bombs--;
                            Bomb b = new Bomb(data.dynamicData.rocket.getX(), data.dynamicData.rocket.getY(), data.dynamicData.player.name);
                            b.calculateDefaultSpeeds();
                            data.dynamicData.bombs.add(b);
                        }
                        data.staticData.pressedKeys.remove(bombPressed);
                    }

                    /**
                     * Shooting mechanism is here.
                     */

                    long timeBetweenConsecutiveShots = 200;
                    switch (data.dynamicData.player.shotType){
                        case 1: timeBetweenConsecutiveShots = Shot1.timeBetweenConsecutiveShots; break;
                        case 2: timeBetweenConsecutiveShots = Shot2.timeBetweenConsecutiveShots; break;
                        case 3: timeBetweenConsecutiveShots = Shot3.timeBetweenConsecutiveShots; break;
                    }

//Case1: not in cooldown mode and is shooting then shoot
                    if(!waitingForShotCooldown && (data.staticData.pressedKeys.contains(KeyEvent.VK_SPACE) || data.staticData.pressedKeys.contains(mousePressed))){
                        shootingTimer = null;
                        if(System.currentTimeMillis() - timeOfLastShot >= timeBetweenConsecutiveShots && data.dynamicData.rocket.isAlive()){
                            shoot(data.dynamicData.player.shotLevel, data.dynamicData.player.shotType);
                            soundThread.addShotSound();
                            timeOfLastShot = System.currentTimeMillis();
                        }
                    }

//Case2: not in cooldown mode and not shooting
                    else if(!waitingForShotCooldown && !data.staticData.pressedKeys.contains(KeyEvent.VK_SPACE) && !data.staticData.pressedKeys.contains(mousePressed)){
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
                        data.dynamicData.rocket.coolDown = false;
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
//        data.gameFrame.dispatchEvent(new WindowEvent(data.gameFrame, WindowEvent.WINDOW_CLOSING));
        System.err.println("LE out!");
        //These lines seem to work alongside setting GERunning = false;
//        Game game = new Game();
//        data.gameFrame.setVisible(false);
//        data.gameFrame.dispose();
    }

    private void shoot(Integer shotLevel, Integer shotType) {
        if(shotLevel == 1){
            //todo check if overheat works.
            //fire one shot
            if(shotType == Shot1.ID){
                data.dynamicData.shots.add(new Shot1(data.dynamicData.rocket.getX(), data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
            }
            else if(shotType == Shot2.ID){
                data.dynamicData.shots.add(new Shot2(data.dynamicData.rocket.getX(), data.dynamicData.rocket.getY(), 0,shotLevel, data.dynamicData.player.name));
            }
            else if(shotType == Shot3.ID){
                data.dynamicData.shots.add(new Shot3(data.dynamicData.rocket.getX(), data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
            }
            else{
                System.err.println("shotType not found!\nIn Logic Engine.\nCurrent shotType is " + data.dynamicData.player.shotType);
            }
        }

        else if(shotLevel == 2){
            //fire two parallel shots
            if(shotType == Shot1.ID){
                data.dynamicData.shots.add(new Shot1(data.dynamicData.rocket.getX()+10, data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot1(data.dynamicData.rocket.getX()-10, data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
            }
            else if(shotType == Shot2.ID){
                data.dynamicData.shots.add(new Shot2(data.dynamicData.rocket.getX()+10, data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot2(data.dynamicData.rocket.getX()-10, data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
            }
            else if(shotType == Shot3.ID){
                data.dynamicData.shots.add(new Shot3(data.dynamicData.rocket.getX()+10, data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot3(data.dynamicData.rocket.getX()-10, data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
            }
            else{
                System.err.println("shotType not found!\nIn Logic Engine.\nCurrent shotType is " + data.dynamicData.player.shotType);
            }
        }
        else if(shotLevel == 3){
            //fire three shots
            if(shotType == Shot1.ID){
                data.dynamicData.shots.add(new Shot1(data.dynamicData.rocket.getX(), data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot1(data.dynamicData.rocket.getX()-10, data.dynamicData.rocket.getY(), 2, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot1(data.dynamicData.rocket.getX()+10, data.dynamicData.rocket.getY(), 3, shotLevel, data.dynamicData.player.name));
            }
            else if(shotType == Shot2.ID){
                data.dynamicData.shots.add(new Shot2(data.dynamicData.rocket.getX(), data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot2(data.dynamicData.rocket.getX()-10, data.dynamicData.rocket.getY(), 2, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot2(data.dynamicData.rocket.getX()+10, data.dynamicData.rocket.getY(), 3, shotLevel, data.dynamicData.player.name));
            }
            else if(shotType == Shot3.ID){
                data.dynamicData.shots.add(new Shot3(data.dynamicData.rocket.getX(), data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot3(data.dynamicData.rocket.getX()-10, data.dynamicData.rocket.getY(), 2, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot3(data.dynamicData.rocket.getX()+10, data.dynamicData.rocket.getY(), 3, shotLevel, data.dynamicData.player.name));
            }
            else{
                System.err.println("shotType not found!\nIn Logic Engine.\nCurrent shotType is " + data.dynamicData.player.shotType);
            }
        }
        else if(shotLevel>3){
            //fire four shots
            if(shotType == Shot1.ID){
                data.dynamicData.shots.add(new Shot1(data.dynamicData.rocket.getX()+10, data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot1(data.dynamicData.rocket.getX()-10, data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot1(data.dynamicData.rocket.getX()-20, data.dynamicData.rocket.getY(), 1, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot1(data.dynamicData.rocket.getX()+20, data.dynamicData.rocket.getY(), 4, shotLevel, data.dynamicData.player.name));
            }
            else if(shotType == Shot2.ID){
                data.dynamicData.shots.add(new Shot2(data.dynamicData.rocket.getX()+10, data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot2(data.dynamicData.rocket.getX()-10, data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot2(data.dynamicData.rocket.getX()-20, data.dynamicData.rocket.getY(), 1, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot2(data.dynamicData.rocket.getX()+20, data.dynamicData.rocket.getY(), 4, shotLevel, data.dynamicData.player.name));
            }
            else if(shotType == Shot3.ID){
                data.dynamicData.shots.add(new Shot3(data.dynamicData.rocket.getX()+10, data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot3(data.dynamicData.rocket.getX()-10, data.dynamicData.rocket.getY(), 0, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot3(data.dynamicData.rocket.getX()-20, data.dynamicData.rocket.getY(), 1, shotLevel, data.dynamicData.player.name));
                data.dynamicData.shots.add(new Shot3(data.dynamicData.rocket.getX()+20, data.dynamicData.rocket.getY(), 4, shotLevel, data.dynamicData.player.name));
            }
        }
        else{
            System.err.println("Shot Level not valid!\nIn Logic Engine.");
        }
    }

    private void collisionHandler() {
        synchronized (data.dynamicData.enemies) {
            synchronized (data.dynamicData.shots) {
                for (Enemy enemy : data.dynamicData.enemies) {
                    for (Shot shot : data.dynamicData.shots) {
                        if (intersect(enemy, shot)) {
                            enemy.health -= shot.damage;
                            data.dynamicData.shots.remove(shot);
                            if (enemy.health <= 0.1) {
                                addPrize(enemy);
                                data.dynamicData.player.score += enemy.getLvl();
                                data.dynamicData.enemies.remove(enemy);
                                data.staticData.gamePanel.repaintStatPanel();
                            }
                        }
                    }
                }
            }
            for (Enemy enemy : data.dynamicData.enemies) {
                if (data.dynamicData.rocket.isAlive() && !data.dynamicData.rocket.isReviving()) {
                    if (intersect(data.dynamicData.rocket, enemy)) {
                        enemy.health -= 50;
                        die();
                        if (enemy.health <= 0.1) {
                            addPrize(enemy);
                            data.dynamicData.player.score += enemy.getLvl();
                            data.dynamicData.enemies.remove(enemy);
                            data.staticData.gamePanel.repaintStatPanel();
                        }
                    }
                }
            }
        }

        synchronized (data.dynamicData.enemyShots) {
            if (data.dynamicData.rocket.isAlive() && !data.dynamicData.rocket.isReviving()) {
                for (EnemyShot enemyShot : data.dynamicData.enemyShots) {
                    if (intersect(data.dynamicData.rocket, enemyShot)) {
                        die();
                        data.dynamicData.enemyShots.remove(enemyShot);
                        break;
                    }
                }
            }
        }

        synchronized (data.dynamicData.upgrades) {
            if (data.dynamicData.rocket.isAlive()) {
                for (Upgrade u : data.dynamicData.upgrades) {
                    if (intersect(data.dynamicData.rocket, u)) {
                        u.activate(data.dynamicData.player);
                        data.dynamicData.upgrades.remove(u);
                        data.staticData.gamePanel.repaintStatPanel();
                    }
                }
            }
        }
    }


    private void die() {
        data.dynamicData.rocket.explode();
        data.dynamicData.player.life--;
        data.dynamicData.player.coins = 0;
        data.dynamicData.player.shotLevel = 1;
        //todo add this to player class
        Shot.maxHeat = 100;
        data.staticData.gamePanel.repaintStatPanel();
    }

    private void addPrize(Enemy enemy) {
        if(enemy instanceof Boss){
            data.dynamicData.upgrades.add(new ShotPowerUp(enemy.getCenterX(), enemy.getCenterY()));
            data.dynamicData.upgrades.add(new ShotPowerUp(enemy.getCenterX(), enemy.getCenterY()));
            data.dynamicData.upgrades.add(new ShotPowerUp(enemy.getCenterX(), enemy.getCenterY()));
            data.dynamicData.upgrades.add(new PowerUp(enemy.getCenterX(), enemy.getCenterY()));
            data.dynamicData.upgrades.add(new ShotChanger(enemy.getCenterX(), enemy.getCenterY(),random.nextInt(3)+1));
        }
        else{
            if(random.nextInt(100)<6)
                data.dynamicData.upgrades.add(new Coin(enemy.getCenterX(), enemy.getCenterY()));
            if(random.nextInt(100)<3)
                data.dynamicData.upgrades.add(new ShotChanger(enemy.getCenterX(), enemy.getCenterY(),random.nextInt(3)+1));
            else if(random.nextInt(97)<3)
                data.dynamicData.upgrades.add(new ShotPowerUp(enemy.getCenterX(), enemy.getCenterY()));
            else if (random.nextInt(94)<3)
                data.dynamicData.upgrades.add(new PowerUp(enemy.getCenterX(), enemy.getCenterY()));
            if(random.nextInt(200) == 0)
                data.dynamicData.upgrades.add(new Heart(enemy.getCenterX(), enemy.getCenterY()));
        }
    }

    private boolean intersect(Rocket rocket, Upgrade u) {
        if(u.getCenterX()<rocket.getX() + rocket.getWidth()/2
                && u.getCenterX()>rocket.getX()-rocket.getWidth()/2
                && u.getCenterY()<rocket.getY() + rocket.getHeight()/2
                && u.getCenterY()>rocket.getY() - rocket.getHeight()/2)
            return true;
        return false;
    }


    private boolean intersect(Rocket rocket, EnemyShot shot){
        if(shot.getCenterX()<rocket.getX() + rocket.getWidth()/2
                && shot.getCenterX()>rocket.getX()-rocket.getWidth()/2
                && shot.getCenterY()<rocket.getY() + rocket.getHeight()/2
                && shot.getCenterY()>rocket.getY() - rocket.getHeight()/2)
            return true;
        return false;
    }

    private boolean intersect(Enemy enemy, Shot shot) {

        if(shot.getX()<enemy.getCenterX() + enemy.getWidth()/2
                && shot.getX()>enemy.getCenterX()-enemy.getWidth()/2
                && shot.getY()<enemy.getCenterY() + enemy.getHeight()/2
                && shot.getY()>enemy.getCenterY() - enemy.getHeight()/2){
            return true;
        }
        return false;
    }

    private boolean intersect(Rocket rocket, Enemy enemy) {
        if(enemy.getCenterX()<rocket.getX() + rocket.getWidth()/2
                && enemy.getCenterX()>rocket.getX()-rocket.getWidth()/2
                && enemy.getCenterY()<rocket.getY() + rocket.getHeight()/2
                && enemy.getCenterY()>rocket.getY() - rocket.getHeight()/2)
            return true;
        return false;
    }
}
