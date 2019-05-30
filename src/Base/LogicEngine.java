package Base;

import Elements.*;
import Elements.Enemies.Boss;
import Elements.Enemies.Enemy2;
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

    final int mousePressed = -23;
    private LevelManager levelManager;
    private boolean inTransition = false, wavesFinished = false, finalSave = false;
    private Data data;
    private SoundThread soundThread = new SoundThread();
    private Random random = new Random();

    public LogicEngine(Data data){
        super();
        this.data = data;
        Shot.maxHeat += 5*data.player.rocketLevel;
        soundThread.start();
    }

    public void setLevelManager(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @Override
    public void run() {
        boolean waitingForShotCooldown = false;
        Long coolDownTimer = 0l, shootingTimer = null, timeOfLastShot = 0l, waitForNextWave = -1l;

        while(data.LERunning) {
            if (!data.isPaused) {
                //Game Over
                if(data.player.life <= 0){
                    data.rocket.noLifeLeft = true;
//                    data.isPaused = true;
//                    GameOverDialog god = new GameOverDialog(data);
//                    data.LERunning = false;
//                    data.GERunning =false;
                }
                //First wave of new level
                if(data.player.subLevel == 0 && data.player.level > 0){
                    data.player.score += 3*data.player.coins;
                    data.player.coins =0;
                    data.gamePanel.repaintStatPanel();
                }
                //Game has been completed.
                if(wavesFinished && !finalSave){
                    data.player.timePlayed = Math.toIntExact((System.currentTimeMillis() - data.startTime) / 1000);
                    data.saveData.ranking.add(data.player);
                    data.save();
                    finalSave = true;
                    data.isPaused = true;

                    new RankingDialog((ArrayList<Player>) data.saveData.ranking);
                }

                if(Shot.shotHeat >= Shot.maxHeat && !waitingForShotCooldown){
                    waitingForShotCooldown = true;
                    data.rocket.coolDown=true;
                    coolDownTimer = System.currentTimeMillis();
                }

//If one wave is over, wait for 3 second, then call level manager for next wave.
                if(data.enemies.size() == 0 && !wavesFinished){
                    if(waitForNextWave <= 0)
                        waitForNextWave = System.currentTimeMillis();
                    else if(System.currentTimeMillis() - waitForNextWave >= 3000) {
                        waitForNextWave = -1l;
                        inTransition = true;
                        wavesFinished = levelManager.nextWave(data.enemies);
                    }
//                    System.err.println(data.enemies.size());
                }

                if(inTransition){
                    synchronized (data.enemies) {
                        inTransition = levelManager.transition(data.enemies);
                    }
                }
                else {
                    Enemy2.rocketX = data.rocket.getX();
                    Enemy2.rocketY = data.rocket.getY();
                    synchronized (data.enemies) {
                        for (Enemy enemy : data.enemies) {
                            enemy.move();
                            List<EnemyShot> tmp = enemy.shoot();
                            if (tmp != null && tmp.size() != 0) for(EnemyShot e:tmp)data.enemyShots.add(e);
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

                synchronized (data.upgrades){
                    for(Upgrade u: data.upgrades){
                        if(u.getCenterY() > Data.screenSize.getHeight()) data.upgrades.remove(u);
                        else u.move();
                    }
                }

                synchronized (data.bombs) {
                    for(Bomb bomb: data.bombs){
                        if(!bomb.isActive){
                            data.bombs.remove(bomb);
//                            System.err.println("bomb exploded");
                            synchronized (data.enemies){
                                for(Enemy e: data.enemies){
                                    e.health -= 50;
                                    if(e.health<=0.1){
                                        data.player.score += e.getLvl();
                                        data.enemies.remove(e);
                                        addPrize(e);
                                    }
                                }
                                data.gamePanel.repaintStatPanel();
                            }
                        }
                        else bomb.move();
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

                    /**
                     * Shooting mechanism is here.
                     */

                    long timeBetweenConsecutiveShots = 200;
//                    System.err.println(data.player.name + " " + data.player.shotType);
//                    while(data.player.shotType == null){
//                        System.out.println("still null");
//                    }
                    switch (data.player.shotType){
                        case 1: timeBetweenConsecutiveShots = Shot1.timeBetweenConsecutiveShots; break;
                        case 2: timeBetweenConsecutiveShots = Shot2.timeBetweenConsecutiveShots; break;
                        case 3: timeBetweenConsecutiveShots = Shot3.timeBetweenConsecutiveShots; break;
                    }

//Case1: not in cooldown mode and is shooting then shoot
                    if(!waitingForShotCooldown && (data.pressedKeys.contains(KeyEvent.VK_SPACE) || data.pressedKeys.contains(mousePressed))){
                        shootingTimer = null;
                        if(System.currentTimeMillis() - timeOfLastShot >= timeBetweenConsecutiveShots && data.rocket.isAlive()){
                            shoot(data.player.shotLevel, data.player.shotType);
//                            data.shots.add(new Shot(data.rocket.getX(), data.rocket.getY()));
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
//        data.gameFrame.dispatchEvent(new WindowEvent(data.gameFrame, WindowEvent.WINDOW_CLOSING));
        System.err.println("LE out!");
        //THese lines seem to work alongside setting GERunning = false;
//        Game game = new Game();
//        data.gameFrame.setVisible(false);
//        data.gameFrame.dispose();
    }

    private void shoot(Integer shotLevel, Integer shotType) {
        if(shotLevel == 1){
            //todo check if overheat works.
            //fire one shot
            if(shotType == Shot1.ID){
                data.shots.add(new Shot1(data.rocket.getX(), data.rocket.getY(), 0, shotLevel));
            }
            else if(shotType == Shot2.ID){
                data.shots.add(new Shot2(data.rocket.getX(), data.rocket.getY(), 0,shotLevel));
            }
            else if(shotType == Shot3.ID){
                data.shots.add(new Shot3(data.rocket.getX(), data.rocket.getY(), 0, shotLevel));
            }
            else{
                System.err.println("shotType not found!\nIn Logic Engine.\nCurrent shotType is " + data.player.shotType);
            }
        }

        else if(shotLevel == 2){
            //fire two parallel shots
            if(shotType == Shot1.ID){
                data.shots.add(new Shot1(data.rocket.getX()+10, data.rocket.getY(), 0, shotLevel));
                data.shots.add(new Shot1(data.rocket.getX()-10, data.rocket.getY(), 0, shotLevel));
            }
            else if(shotType == Shot2.ID){
                data.shots.add(new Shot2(data.rocket.getX()+10, data.rocket.getY(), 0, shotLevel));
                data.shots.add(new Shot2(data.rocket.getX()-10, data.rocket.getY(), 0, shotLevel));
            }
            else if(shotType == Shot3.ID){
                data.shots.add(new Shot3(data.rocket.getX()+10, data.rocket.getY(), 0, shotLevel));
                data.shots.add(new Shot3(data.rocket.getX()-10, data.rocket.getY(), 0, shotLevel));
            }
            else{
                System.err.println("shotType not found!\nIn Logic Engine.\nCurrent shotType is " + data.player.shotType);
            }
        }
        else if(shotLevel == 3){
            //fire three shots
            if(shotType == Shot1.ID){
                data.shots.add(new Shot1(data.rocket.getX(), data.rocket.getY(), 0, shotLevel));
                data.shots.add(new Shot1(data.rocket.getX()-10, data.rocket.getY(), 2, shotLevel));
                data.shots.add(new Shot1(data.rocket.getX()+10, data.rocket.getY(), 3, shotLevel));
            }
            else if(shotType == Shot2.ID){
                data.shots.add(new Shot2(data.rocket.getX(), data.rocket.getY(), 0, shotLevel));
                data.shots.add(new Shot2(data.rocket.getX()-10, data.rocket.getY(), 2, shotLevel));
                data.shots.add(new Shot2(data.rocket.getX()+10, data.rocket.getY(), 3, shotLevel));
            }
            else if(shotType == Shot3.ID){
                data.shots.add(new Shot3(data.rocket.getX(), data.rocket.getY(), 0, shotLevel));
                data.shots.add(new Shot3(data.rocket.getX()-10, data.rocket.getY(), 2, shotLevel));
                data.shots.add(new Shot3(data.rocket.getX()+10, data.rocket.getY(), 3, shotLevel));
            }
            else{
                System.err.println("shotType not found!\nIn Logic Engine.\nCurrent shotType is " + data.player.shotType);
            }
        }
        else if(shotLevel>3){
            //fire four shots
            if(shotType == Shot1.ID){
                data.shots.add(new Shot1(data.rocket.getX()+10, data.rocket.getY(), 0, shotLevel));
                data.shots.add(new Shot1(data.rocket.getX()-10, data.rocket.getY(), 0, shotLevel));
                data.shots.add(new Shot1(data.rocket.getX()-20, data.rocket.getY(), 1, shotLevel));
                data.shots.add(new Shot1(data.rocket.getX()+20, data.rocket.getY(), 4, shotLevel));
            }
            else if(shotType == Shot2.ID){
                data.shots.add(new Shot2(data.rocket.getX()+10, data.rocket.getY(), 0, shotLevel));
                data.shots.add(new Shot2(data.rocket.getX()-10, data.rocket.getY(), 0, shotLevel));
                data.shots.add(new Shot2(data.rocket.getX()-20, data.rocket.getY(), 1, shotLevel));
                data.shots.add(new Shot2(data.rocket.getX()+20, data.rocket.getY(), 4, shotLevel));
            }
            else if(shotType == Shot3.ID){
                data.shots.add(new Shot3(data.rocket.getX()+10, data.rocket.getY(), 0, shotLevel));
                data.shots.add(new Shot3(data.rocket.getX()-10, data.rocket.getY(), 0, shotLevel));
                data.shots.add(new Shot3(data.rocket.getX()-20, data.rocket.getY(), 1, shotLevel));
                data.shots.add(new Shot3(data.rocket.getX()+20, data.rocket.getY(), 4, shotLevel));
            }
        }
        else{
            System.err.println("Shot Level not valid!\nIn Logic Engine.");
        }
    }

    private void collisionHandler() {
        synchronized (data.enemies){
            synchronized (data.shots){
                for(Enemy enemy:data.enemies) {
                    for (Shot shot : data.shots) {
                        if (intersect(enemy, shot)) {
                            enemy.health -= shot.damage;
                            data.shots.remove(shot);
                            if (enemy.health <= 0.1) {
                                addPrize(enemy);
                                data.player.score += enemy.getLvl();
                                data.enemies.remove(enemy);
                                data.gamePanel.repaintStatPanel();
                            }
                        }
                    }
                    if (data.rocket.isAlive() && !data.rocket.isReviving()) {
                        if (intersect(data.rocket, enemy)) {
                            enemy.health -= 50;
                            die();
                            if (enemy.health <= 0.1) {
                                addPrize(enemy);
                                data.player.score += enemy.getLvl();
                                data.enemies.remove(enemy);
                                data.gamePanel.repaintStatPanel();
                            }
                        }
                    }
                }
            }
        }

        synchronized (data.enemyShots) {
            if (data.rocket.isAlive() && !data.rocket.isReviving()) {
                for (EnemyShot enemyShot : data.enemyShots) {
                    if (intersect(data.rocket, enemyShot)) {
                        die();
                        data.enemyShots.remove(enemyShot);
                        break;
                    }
                }
            }
        }

        synchronized (data.upgrades){
            if(data.rocket.isAlive()){
                for(Upgrade u: data.upgrades){
                    if(intersect(data.rocket, u)){
                        u.activate(data.player);
                        data.upgrades.remove(u);
                        data.gamePanel.repaintStatPanel();
                    }
                }
            }
        }
    }


    private void die() {
        data.rocket.explode();
        data.player.life--;
        data.player.coins = 0;
        data.player.shotLevel = 1;
        //todo add this to player class
        Shot.maxHeat = 100;
        data.gamePanel.repaintStatPanel();
    }

    private void addPrize(Enemy enemy) {
        if(enemy instanceof Boss){
            data.upgrades.add(new ShotPowerUp(enemy.getCenterX(), enemy.getCenterY()));
            data.upgrades.add(new ShotPowerUp(enemy.getCenterX(), enemy.getCenterY()));
            data.upgrades.add(new ShotPowerUp(enemy.getCenterX(), enemy.getCenterY()));
            data.upgrades.add(new PowerUp(enemy.getCenterX(), enemy.getCenterY()));
            data.upgrades.add(new ShotChanger(enemy.getCenterX(), enemy.getCenterY(),random.nextInt(3)+1));
        }
        else{
            if(random.nextInt(100)<6)
                data.upgrades.add(new Coin(enemy.getCenterX(), enemy.getCenterY()));
            if(random.nextInt(100)<3)
                data.upgrades.add(new ShotChanger(enemy.getCenterX(), enemy.getCenterY(),random.nextInt(3)+1));
            else if(random.nextInt(97)<3)
                data.upgrades.add(new ShotPowerUp(enemy.getCenterX(), enemy.getCenterY()));
            else if (random.nextInt(94)<3)
                data.upgrades.add(new PowerUp(enemy.getCenterX(), enemy.getCenterY()));
            if(random.nextInt(200) == 0)
                data.upgrades.add(new Heart(enemy.getCenterX(), enemy.getCenterY()));
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
