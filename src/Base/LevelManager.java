package Base;

import Elements.Enemies.Enemy4;
import Elements.Enemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class LevelManager {

    private Set<Enemy> originalEnemySet;
    private List<Wave> waveList = new ArrayList<>();
    private static int currentWave = -1;
    private final int maxLevels;
    private List<Player> players;

//todo it might be a good idea to also pass the dimension of panel to it but it might also cause complications.
    public LevelManager(List<Player> players, Set<Enemy> originalEnemySet, int maxLevels) {
        this.maxLevels = maxLevels;
        this.players = players;
        this.originalEnemySet = originalEnemySet;
        createWaves();
    }
    public LevelManager(List<Player> players, Set<Enemy> originalEnemySet){
        this(players, originalEnemySet, 4);
    }

    @Reflection
    /**
     * Adds a new wave of enemyClass type enemies to the enemy queue.
     * Called after loading a new enemy class from logic engine.
     */
    public void addEnemyType(Class<Enemy> enemyClass){

    }

    private void createWaves() {
        /**
         * Wave 0 Level 0 index 0 Type Enemy1
         * 5 rows each row with 9 enemy.
         */
        waveList.add(WaveFactory.Type1(5,10, 1));

        /**
         * Wave 1 Level 1 index 1 Type Enemy2
         * 2 rows of 10 enemies;
         */
        waveList.add(WaveFactory.Type2(2, 10, 1));

        /**
         * Wave 2 level 1 index 2 Type Enemy3
         * 2 circles of 15 enemies
         */
        waveList.add(WaveFactory.Type3((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2),
                300, 2, 15, 100, 20, 1));

        /**
         * Wave 3 level 1 index 2 Type Enemy4
         * 2 circles of 15 enemies
         */
        waveList.add(WaveFactory.Type4((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2),
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2), 350, 2,
                15, 100, 40,1));

        waveList.add(WaveFactory.TypeBoss(1));

        /**
         * Level 2
         */
        waveList.add(WaveFactory.Type1(5,10, 2));
        waveList.add(WaveFactory.Type2(2, 10, 2));
        waveList.add(WaveFactory.Type3((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2),
                300, 2, 15, 100, 20, 2));
        waveList.add(WaveFactory.Type4((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2),
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2), 350, 2,
                15, 100, 40,2));
        waveList.add(WaveFactory.TypeBoss(2));

        /**
         * Level 3
         */
        waveList.add(WaveFactory.Type1(5,12, 3));
        waveList.add(WaveFactory.Type2(2, 10, 3));
        waveList.add(WaveFactory.Type3((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2),
                300, 2, 20, 100, 20, 3));
        waveList.add(WaveFactory.Type4((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2),
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2), 350, 2,
                15, 100, 40,3));
        waveList.add(WaveFactory.TypeBoss(3));

        /**
         * Level 4
         */
        waveList.add(WaveFactory.Type1(5,12, 4));
        waveList.add(WaveFactory.Type2(2, 10, 4));
        waveList.add(WaveFactory.Type3((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2),
                300, 2, 20, 100, 20, 4));
        waveList.add(WaveFactory.Type4((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2),
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2), 350, 2,
                15, 100, 40,4));
        waveList.add(WaveFactory.TypeBoss(4));
    }


    public boolean transition(Set<Enemy> enemies) {
        boolean done = true;
        for(Enemy enemy: enemies){
            //TODO margin of error is hardcoded!!
            if(Math.abs(enemy.getCenterX()-enemy.getDefaultX())>10 || Math.abs(enemy.getCenterY()-enemy.getDefaultY())>10){
                done = false;
                enemy.transition();
            }
        }
        return !done;
    }

    /**
     * @param enemies
     * @return true if all waves are done.
     */
    public boolean nextWave(Set<Enemy> enemies) {
        for(Player p: players) {
            p.subLevel++;
            p.level = p.subLevel / 5;
        }
        currentWave++;
        //todo when All waves are done,
        if(currentWave >= waveList.size() || (currentWave/4)>=maxLevels) {
            System.err.println("DONE");
            return true;
        }
        //Todo havent checked it. maybe has a bug.
        if (waveList.get(currentWave).hasType4) {
            Enemy4.pivotX = Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2;
            Enemy4.pivotY = Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2;
            Enemy4.targetX = -1;
            Enemy4.targetY = -1;
        }
        System.err.println("Current Wave: " + currentWave + "maxLevels: " + maxLevels + "\t\t"+(currentWave/4));
        for(Enemy enemy: waveList.get(currentWave).enemies){
            enemies.add(enemy);
        }
        return false;
    }
}
