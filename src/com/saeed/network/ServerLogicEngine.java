package com.saeed.network;

import Base.LevelManager;
import Base.Player;
import Base.SoundThread;
import Elements.Bomb;
import Elements.Enemies.Boss;
import Elements.Enemies.Enemy2;
import Elements.Enemy;
import Elements.EnemyShot;
import Elements.Rocket;
import Elements.Shots.Shot;
import Elements.Shots.Shot1;
import Elements.Shots.Shot2;
import Elements.Shots.Shot3;
import Elements.Upgrades.*;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class ServerLogicEngine extends Thread {
    private final int maxLevels;
    private ServerData serverData;
    final int mousePressed = -23, bombPressed = -24;
    private LevelManager levelManager;
    private boolean inTransition = false, wavesFinished = false, finalSave = false;
    private SoundThread soundThread = new SoundThread();
    private Random random = new Random();
    private ClientsDataOnServer data;
    
    public ServerLogicEngine(int maxLevels){
        this.maxLevels = maxLevels;
        serverData = new ServerData();
        //todo should also pass max_level
        levelManager = new LevelManager(serverData.players,serverData.enemies, maxLevels);
        soundThread.start();
    }

    @Override
    public void run() {
        Long waitForNextWave = -1l;
        boolean flag;
        //todo while condition.
        while(true){

            //this will inform others that game is paused when serverData.isPaused is sent to clients
            //and then their requestPauses will be true until they change it.
            flag = true;
            for(String name: serverData.clients.keySet()){
                if(serverData.clients.get(name).requestedPause){
                    serverData.isPaused = true;
                    flag = false;
                    break;
                }
            }
            //if nobody was paused
            if(flag) serverData.isPaused = false;

            if(!serverData.isPaused){

                //todo some code goes here from logic Engine.

                /**
                 * Check for shot over-heat among all players
                 */
                for(String name:serverData.clients.keySet()) {
                    if (serverData.clients.get(name).player.shotHeat >= serverData.clients.get(name).player.maxHeat && !serverData.clients.get(name).player.waitingForShotCooldown) {
                        serverData.clients.get(name).player.waitingForShotCooldown = true;
                        serverData.clients.get(name).rocket.coolDown = true;
                        serverData.clients.get(name).player.coolDownTimer = System.currentTimeMillis();
                    }
                }

                //If one wave is over, wait for 3 second, then call level manager for next wave.
                if(serverData.enemies.size() == 0 && !wavesFinished){
                    if(waitForNextWave <= 0)
                        waitForNextWave = System.currentTimeMillis();
                    else if(System.currentTimeMillis() - waitForNextWave >= 3000) {
                        waitForNextWave = -1l;
                        inTransition = true;
                        wavesFinished = levelManager.nextWave(serverData.enemies);
                        double constant = Math.sqrt(serverData.players.size());
                        for(Enemy e: serverData.enemies){
                            e.health*=constant;
                        }
                    }
                }

                if(inTransition){
                    synchronized (serverData.enemies) {
                        inTransition = levelManager.transition(serverData.enemies);
                    }
                }
                else {
                    //todo!!!!
                    Enemy2.rocketX = serverData.rockets.get(0).getX();
                    Enemy2.rocketY = serverData.rockets.get(0).getY();
                    synchronized (serverData.enemies) {
                        for (Enemy enemy : serverData.enemies) {
                            enemy.move();
                            List<EnemyShot> tmp = enemy.shoot();
                            if (tmp != null && tmp.size() != 0) for(EnemyShot e:tmp ) serverData.enemyShots.add(e);
                        }
                    }
                }

                synchronized (serverData.shots) {
                    for (Shot shot : serverData.shots) {
                        if (shot.getY() < 0) serverData.shots.remove(shot);
                        else shot.move();
                    }
                }

                synchronized (serverData.enemyShots){
                    for(EnemyShot shot: serverData.enemyShots){
                        if(shot.getCenterY() > serverData.screenSize.getHeight()) serverData.enemyShots.remove(shot);
                        else shot.move();
                    }
                }

                synchronized (serverData.upgrades){
                    for(Upgrade u: serverData.upgrades){
                        if(u.getCenterY() > serverData.screenSize.getHeight()) serverData.upgrades.remove(u);
                        else u.move();
                    }
                }

                synchronized (serverData.rockets) {
                    for (Rocket r : serverData.rockets) {
                        if (!r.noLifeLeft) {
                            if (r.isReviving()) {
                                //3 seconds reviving time.
                                if (System.currentTimeMillis() > r.reviveTime + 3000) r.setReviving(false);
                                r.nextReviveAnimation();
                            }
                            else if(!r.isAlive()){
                                // 7seconds disappear time.
                                if (System.currentTimeMillis() > r.killTime + 7000) {
//                                    r.setMouse();
                                    r.setAlive(true);
                                    r.setReviving(true);
                                    r.reviveTime = System.currentTimeMillis();
                                }
                            }
                        }
                        else {
                            r.setAlive(false);
                            r.setReviving(false);
                        }
                    }
                }

                synchronized (serverData.bombs) {
                    for(Bomb bomb: serverData.bombs){
                        if(!bomb.isActive){
                            serverData.bombs.remove(bomb);
                            System.err.println("bomb exploded");
                            Player owner = serverData.clients.get(bomb.owner).player;
                            synchronized (serverData.enemies){
                                for(Enemy e: serverData.enemies){
                                    e.health -= 50;
                                    if(e.health<=0.1){
                                        owner.score += e.getLvl();
                                        serverData.enemies.remove(e);
                                        addPrize(e);
                                    }
                                }
                            }
                        }
                        else bomb.move();
                    }
                }

                collisionHandler();

                for(String name: serverData.clients.keySet()){
                    data = serverData.clients.get(name);
                    
                    //Code for handling keyboard input comes here.
                    synchronized (data.pressedKeys) {
                        if (data.pressedKeys.contains(KeyEvent.VK_DOWN) && data.rocket.getY() < serverData.screenSize.height) {
                            data.rocket.setY(data.rocket.getY() + 10);
                        }
                        if (data.pressedKeys.contains(KeyEvent.VK_UP) && data.rocket.getY() > 0) {
                            data.rocket.setY(data.rocket.getY() - 10);
                        }
                        if (data.pressedKeys.contains(KeyEvent.VK_LEFT) && data.rocket.getX() > 0) {
                            data.rocket.setX(data.rocket.getX() - 10);
                        }
                        if (data.pressedKeys.contains(KeyEvent.VK_RIGHT) && data.rocket.getX() < serverData.screenSize.width) {
                            data.rocket.setX(data.rocket.getX() + 10);
                        }
                        if(data.pressedKeys.contains(bombPressed)){
                            if(data.player.bombs>0 && data.rocket.isAlive()) {
                                data.player.bombs--;
                                synchronized (serverData.bombs) {
                                    Bomb b = new Bomb(data.rocket.getX(), data.rocket.getY(), data.player.name);
                                    b.calculateDefaultSpeeds();
                                    serverData.bombs.add(b);
                                }
                            }
                            data.pressedKeys.remove(bombPressed);
                        }
//                        System.err.println("time in IF: "+ (System.currentTimeMillis()-time) + "mS");
                    }

                    /**
                     * shooting mechanism. weapon heat and cooldown not implemented.
                     */
                    long timeBetweenConsecutiveShots = 200;
                    switch (data.player.shotType){
                        case 1: timeBetweenConsecutiveShots = Shot1.timeBetweenConsecutiveShots; break;
                        case 2: timeBetweenConsecutiveShots = Shot2.timeBetweenConsecutiveShots; break;
                        case 3: timeBetweenConsecutiveShots = Shot3.timeBetweenConsecutiveShots; break;
                    }

//Case1: not in cooldown mode and is shooting then shoot
                    if(!data.player.waitingForShotCooldown && (data.pressedKeys.contains(KeyEvent.VK_SPACE) || data.pressedKeys.contains(mousePressed))){
                        data.player.shootingTimer = null;
//                        if(data.player.timeOfLastShot == null) System.err.println("timeOfLastShot is null");
                        if(System.currentTimeMillis() - data.player.timeOfLastShot >= timeBetweenConsecutiveShots && data.rocket.isAlive()){
                            shoot(data.player.shotLevel, data.player.shotType, data.rocket, data.player);
                            soundThread.addShotSound();
                            data.player.timeOfLastShot = System.currentTimeMillis();
                        }
                    }

//Case2: not in cooldown mode and not shooting
                    else if(!data.player.waitingForShotCooldown && !data.pressedKeys.contains(KeyEvent.VK_SPACE) && !data.pressedKeys.contains(mousePressed)){
                        if(data.player.shootingTimer==null)
                            data.player.shootingTimer = System.currentTimeMillis();
                        else{
                            while (System.currentTimeMillis() - data.player.shootingTimer >= Shot.coolDownTimeMillis){
                                data.player.shotHeat -= Shot.heatReductionRate;
                                if(data.player.shotHeat < 0) data.player.shotHeat = 0;
                                data.player.shootingTimer += Shot.coolDownTimeMillis;
                            }
                        }
                    }

//Case3: in cooldown mode. don't care if shooting or not.
                    else {
                        if (data.player.waitingForShotCooldown && Shot.shotHeat>0) {
                            while (System.currentTimeMillis() - data.player.coolDownTimer >= Shot.coolDownTimeMillis) {
                                data.player.shotHeat -= Shot.heatReductionRate;
                                if(data.player.shotHeat < 0) data.player.shotHeat = 0;
                                data.player.coolDownTimer += Shot.coolDownTimeMillis;
                            }
                        }
                    }
                    if(data.player.shotHeat <= 0){
                        data.player.shotHeat = 0;
                        data.player.waitingForShotCooldown = false;
                        data.rocket.coolDown = false;
                    }
                }
//                System.err.println("Time spent in SLE: " + (System.currentTimeMillis()-time)+ " mS");
//                time = System.currentTimeMillis();
            }
            try {
                sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void collisionHandler() {
        /**
         * enemy collisions
         */
        synchronized (serverData.enemies) {
            for (Enemy enemy : serverData.enemies) {
                /**
                 * enemy-shot collision
                 */
                synchronized (serverData.shots) {
                    for (Shot shot : serverData.shots) {
                        if (intersect(enemy, shot)) {
                            enemy.health -= shot.damage;
                            if (enemy.health <= 0.1) {
                                addPrize(enemy);
                                serverData.clients.get(shot.owner).player.score += enemy.getLvl();
                                serverData.enemies.remove(enemy);
                            }
                            serverData.shots.remove(shot);
                        }
                    }
                }

                /**
                 * enemy-player collision
                 */
                synchronized (serverData.rockets) {
                    for (Rocket rocket : serverData.rockets) {
                        if (rocket.isAlive() && !rocket.isReviving() && intersect(rocket, enemy)) {
                            enemy.health -= 50;
                            die(serverData.clients.get(rocket.getOwner()).player, rocket);
                            if (enemy.health <= 0.1) {
                                addPrize(enemy);
                                serverData.clients.get(rocket.getOwner()).player.score += enemy.getLvl();
                                serverData.enemies.remove(enemy);
                            }
                        }
                    }
                }
            }
        }

        /**
         * rocket collisions
         */
        for (Rocket rocket : serverData.rockets) {
            Player owner = serverData.clients.get(rocket.getOwner()).player;
            if (rocket.isAlive() && !rocket.isReviving()) {
                /**
                 * rocket-enemyShot collision
                 */
                synchronized (serverData.enemyShots) {
                    for (EnemyShot enemyShot : serverData.enemyShots) {
                        if (intersect(rocket, enemyShot)) {
                            die(serverData.clients.get(rocket.getOwner()).player, rocket);
                            serverData.enemyShots.remove(enemyShot);
                            break;
                        }
                    }
                }
            }
            /**
             * rocket-upgrade collision
             */
            if (rocket.isAlive()) {
                synchronized (serverData.upgrades) {
                    for (Upgrade u : serverData.upgrades) {
                        if (intersect(rocket, u)) {
                            u.activate(serverData.clients.get(rocket.getOwner()).player);
                            serverData.upgrades.remove(u);
                        }
                    }
                }
            }
            /**
             * rocket-friendly fire collision
             */
            synchronized (serverData.shots) {
                for (Shot shot : serverData.shots) {
                    if (!shot.owner.equals(rocket.getOwner()) && intersect(rocket, shot)) {
                        die(serverData.clients.get(rocket.getOwner()).player, rocket);
                        serverData.shots.remove(shot);
                        break;
                    }
                }
            }
        }
    }

    private void die(Player player, Rocket rocket) {
        rocket.explode();
        player.life--;
        player.coins = 0;
        player.shotLevel = 1;
        player.maxHeat = 100;
    }

    private boolean intersect(Rocket rocket, Upgrade u) {
        if(u.getCenterX()<rocket.getX() + rocket.getWidth()/2
                && u.getCenterX()>rocket.getX()-rocket.getWidth()/2
                && u.getCenterY()<rocket.getY() + rocket.getHeight()/2
                && u.getCenterY()>rocket.getY() - rocket.getHeight()/2)
            return true;
        return false;
    }

    private boolean intersect(Rocket rocket, Shot shot) {
        if(shot.getX() <rocket.getX() + rocket.getWidth()/2
                && shot.getX()>rocket.getX()-rocket.getWidth()/2
                && shot.getY()<rocket.getY() + rocket.getHeight()/2
                && shot.getY()>rocket.getY() - rocket.getHeight()/2)
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

    private void addPrize(Enemy enemy) {
        if(enemy instanceof Boss){
            serverData.upgrades.add(new ShotPowerUp(enemy.getCenterX(), enemy.getCenterY()));
            serverData.upgrades.add(new ShotPowerUp(enemy.getCenterX(), enemy.getCenterY()));
            serverData.upgrades.add(new ShotPowerUp(enemy.getCenterX(), enemy.getCenterY()));
            serverData.upgrades.add(new PowerUp(enemy.getCenterX(), enemy.getCenterY()));
            serverData.upgrades.add(new ShotChanger(enemy.getCenterX(), enemy.getCenterY(),random.nextInt(3)+1));
        }
        else{
            if(random.nextInt(100)<6)
                serverData.upgrades.add(new Coin(enemy.getCenterX(), enemy.getCenterY()));
            if(random.nextInt(100)<3)
                serverData.upgrades.add(new ShotChanger(enemy.getCenterX(), enemy.getCenterY(),random.nextInt(3)+1));
            else if(random.nextInt(97)<3)
                serverData.upgrades.add(new ShotPowerUp(enemy.getCenterX(), enemy.getCenterY()));
            else if (random.nextInt(94)<3)
                serverData.upgrades.add(new PowerUp(enemy.getCenterX(), enemy.getCenterY()));
            if(random.nextInt(200) == 0)
                serverData.upgrades.add(new Heart(enemy.getCenterX(), enemy.getCenterY()));
        }
    }

    private void shoot(Integer shotLevel, Integer shotType, Rocket rocket, Player player) {
        player.shotHeat += Shot.heatIncreaseRate;
        if(shotLevel == 1){
            //fire one shot
            if(shotType == Shot1.ID){
                serverData.shots.add(new Shot1(rocket.getX(), rocket.getY(), 0, shotLevel, rocket.getOwner()));
            }
            else if(shotType == Shot2.ID){
                serverData.shots.add(new Shot2(rocket.getX(), rocket.getY(), 0,shotLevel, rocket.getOwner()));
            }
            else if(shotType == Shot3.ID){
                serverData.shots.add(new Shot3(rocket.getX(), rocket.getY(), 0, shotLevel, rocket.getOwner()));
            }
            else{
                System.err.println("shotType not found!\nIn Logic Engine.\nCurrent shotType is NULLLL");
            }
        }

        else if(shotLevel == 2){
            //fire two parallel shots
            if(shotType == Shot1.ID){
                serverData.shots.add(new Shot1(rocket.getX()+10, rocket.getY(), 0, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot1(rocket.getX()-10, rocket.getY(), 0, shotLevel, rocket.getOwner()));
            }
            else if(shotType == Shot2.ID){
                serverData.shots.add(new Shot2(rocket.getX()+10, rocket.getY(), 0, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot2(rocket.getX()-10, rocket.getY(), 0, shotLevel, rocket.getOwner()));
            }
            else if(shotType == Shot3.ID){
                serverData.shots.add(new Shot3(rocket.getX()+10, rocket.getY(), 0, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot3(rocket.getX()-10, rocket.getY(), 0, shotLevel, rocket.getOwner()));
            }
            else{
                System.err.println("shotType not found!\nIn Logic Engine.\nCurrent shotType is NULLLL");
            }
        }
        else if(shotLevel == 3){
            //fire three shots
            if(shotType == Shot1.ID){
                serverData.shots.add(new Shot1(rocket.getX(), rocket.getY(), 0, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot1(rocket.getX()-10, rocket.getY(), 2, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot1(rocket.getX()+10, rocket.getY(), 3, shotLevel, rocket.getOwner()));
            }
            else if(shotType == Shot2.ID){
                serverData.shots.add(new Shot2(rocket.getX(), rocket.getY(), 0, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot2(rocket.getX()-10, rocket.getY(), 2, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot2(rocket.getX()+10, rocket.getY(), 3, shotLevel, rocket.getOwner()));
            }
            else if(shotType == Shot3.ID){
                serverData.shots.add(new Shot3(rocket.getX(), rocket.getY(), 0, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot3(rocket.getX()-10, rocket.getY(), 2, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot3(rocket.getX()+10, rocket.getY(), 3, shotLevel, rocket.getOwner()));
            }
            else{
                System.err.println("shotType not found!\nIn Logic Engine.\nCurrent shotType is NULLLL");
            }
        }
        else if(shotLevel>3){
            //fire four shots
            if(shotType == Shot1.ID){
                serverData.shots.add(new Shot1(rocket.getX()+10, rocket.getY(), 0, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot1(rocket.getX()-10, rocket.getY(), 0, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot1(rocket.getX()-20, rocket.getY(), 1, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot1(rocket.getX()+20, rocket.getY(), 4, shotLevel, rocket.getOwner()));
            }
            else if(shotType == Shot2.ID){
                serverData.shots.add(new Shot2(rocket.getX()+10, rocket.getY(), 0, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot2(rocket.getX()-10, rocket.getY(), 0, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot2(rocket.getX()-20, rocket.getY(), 1, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot2(rocket.getX()+20, rocket.getY(), 4, shotLevel, rocket.getOwner()));
            }
            else if(shotType == Shot3.ID){
                serverData.shots.add(new Shot3(rocket.getX()+10, rocket.getY(), 0, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot3(rocket.getX()-10, rocket.getY(), 0, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot3(rocket.getX()-20, rocket.getY(), 1, shotLevel, rocket.getOwner()));
                serverData.shots.add(new Shot3(rocket.getX()+20, rocket.getY(), 4, shotLevel, rocket.getOwner()));
            }
        }
        else{
            System.err.println("Shot Level not valid!\nIn Logic Engine.");
        }
    }

    public ServerData getServerData(){
        return serverData;
    }

}
