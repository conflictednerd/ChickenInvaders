package Base;

import Elements.Enemies.Enemy1;
import Elements.Enemies.Enemy2;
import Elements.Enemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LevelManager {

    private Set<Enemy> originalEnemySet;
    private List<Wave> waveList = new ArrayList<>();
    private static int currentLevel = -1;

    public LevelManager(Integer startLevel, Set<Enemy> originalEnemySet) {
//        currentLevel = startLevel;
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
            for(int j = 1; j<5; j++){
                Enemy enemy = new Enemy1();
                enemy.setCenterY(0);
                enemy.setCenterX((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2));
                enemy.setDefaultX(10 + j*enemy.getWidth());
                enemy.setDefaultY(10 + i*enemy.getHeight());
                enemy.calculateDefaultSpeeds();
                wave0.enemies.add(enemy);
            }
        }
        waveList.add(wave0);

        /**
         * Wave 1 Level 1 index 1 Type Enemy2
         * 2 rows of 10 enemies;
         */
        Wave wave1 = new Wave();
        for(int i = 1; i<=2; i++){
            for(int j = 1; j<11;j++){
                Enemy enemy = new Enemy2();
                enemy.setCenterY(0);
                enemy.setCenterX((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2));
//                System.out.println(enemy.getCenterX() + ", " + enemy.getCenterY());
//                System.out.println("("+(10 + j*enemy.getWidth()) +" , "+(10 + i*enemy.getHeight()) + ")");
                enemy.setDefaultX(10 + j*enemy.getWidth());
                enemy.setDefaultY(10 + i*enemy.getHeight());
                enemy.calculateDefaultSpeeds();
                wave1.enemies.add(enemy);
            }
        }
        waveList.add(wave1);
    }


    public boolean transition(Set<Enemy> enemies) {
        boolean done = true;
        for(Enemy enemy: enemies){
            //TODO margin of error is hardcoded!!
            //TODO TODO !!!!!! They don't go where they are supposed to. It's probably Enemy.transition calculations that don't work   !!!!!!!!!!!!!!!!
            if(Math.abs(enemy.getCenterX()-enemy.getDefaultX())>5 || Math.abs(enemy.getCenterY()-enemy.getDefaultY())>5){
                done = false;
                enemy.transition();
            }
        }
        return !done;
    }

    public void nextWave(Set<Enemy> enemies) {
        currentLevel++;
        //TODO error prone!!!
        //todo All waves are done.
        System.out.println(currentLevel);
        if(currentLevel >= waveList.size()) return;
        for(Enemy enemy: waveList.get(currentLevel).enemies){
            enemies.add(enemy);
        }
    }
}
