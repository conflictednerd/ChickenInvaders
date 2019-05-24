package Base;

import Elements.Enemies.Enemy1;
import Elements.Enemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LevelManager {

    private Set<Enemy> originalEnemySet;
    private List<Wave> waveList = new ArrayList<>();
    private int currentLevel = -1;

    public LevelManager(Integer startLevel, Set<Enemy> originalEnemySet) {
        currentLevel = startLevel;
        this.originalEnemySet = originalEnemySet;
        createWaves();
    }

    private void createWaves() {
        /**
         * Wave 0 Level 0 index 0 Type Enemy1
         * 5 rows each row with 9 enemy.
         */
        Wave wave0 = new Wave();
        for(int i = 1; i<=5; i++){
            for(int j = 1; j<10; j++){
                Enemy enemy = new Enemy1();
                enemy.setCenterY(0);
                enemy.setCenterX((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2));
                enemy.setDefaultX(10 + j*Enemy1.getWidth());
                enemy.setDefaultY(10 + i*Enemy1.getHeight());
                enemy.calculateDefaultSpeeds();
                wave0.enemies.add(enemy);
            }
        }
        waveList.add(wave0);
    }


    public boolean transition(Set<Enemy> enemies) {
        boolean done = true;
        for(Enemy enemy: enemies){
            //TODO margin of error is hardcoded!!
            //TODO TODO !!!!!! They don't go where they are supposed to. It's probably Enemy.transition calculations that don't work   !!!!!!!!!!!!!!!!
            if(Math.abs(enemy.getCenterX()-enemy.getDefaultX())>5 && Math.abs(enemy.getCenterY()-enemy.getDefaultY())>5){
                done = false;
                enemy.transition();
            }
        }
        System.err.println(done);
        return !done;
    }

    public void nextWave(Set<Enemy> enemies) {
        currentLevel++;
        //TODO error prone!!!
//        originalEnemySet = waveList.get(currentLevel).enemies;
        for(Enemy enemy: waveList.get(0).enemies){
            enemies.add(enemy);
        }
//        enemies = waveList.get(0).enemies;
    }
}
