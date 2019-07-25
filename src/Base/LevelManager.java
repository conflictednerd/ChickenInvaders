package Base;

import Elements.Enemies.Boss;
import Elements.Enemies.Enemy1;
import Elements.Enemies.Enemy4;
import Elements.Enemy;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class LevelManager {

    private Set<Enemy> originalEnemySet;
    private List<Wave> waveList = new ArrayList<>();
    private static int currentWave = -1;
    private final int maxLevels, startLevel, startSubLevel;
    private List<Player> players;

//todo it might be a good idea to also pass the dimension of panel to it but it might also cause complications.
    public LevelManager(List<Player> players, Set<Enemy> originalEnemySet, int maxLevels, int startLevel, int startSubLevel) {
        this.maxLevels = maxLevels;
        this.startLevel = startLevel;
        this.startSubLevel = startSubLevel;
        this.players = players;
        this.originalEnemySet = originalEnemySet;
        createWaves();
    }
    public LevelManager(List<Player> players, Set<Enemy> originalEnemySet, int startLevel, int startSubLevel){
        this(players, originalEnemySet, 4, startLevel, startSubLevel);
    }

    @Reflection
    /**
     * Adds a new wave of enemyClass type enemies to the enemy queue.
     * Called after loading a new enemy class from logic engine.
     */
    public void addEnemyType(Class<? extends Enemy> enemyClass) {
        final int number = 10;
        final int lvl = 2;
        Wave wave = new Wave();
        try {
            Constructor<? extends Enemy> constructor = enemyClass.getConstructor();
            for (int j = 1; j <= number; j++) {
                Enemy enemy = constructor.newInstance();
                enemy.health += lvl;
                enemy.setLvl(lvl);
                enemy.setCenterY(0);
                enemy.setCenterX((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2));
                enemy.setDefaultX(10 + j * enemy.getWidth());
                enemy.setDefaultY(10 + enemy.getHeight());
                enemy.calculateDefaultSpeeds();
                wave.enemies.add(enemy);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        waveList.add(currentWave+1, wave);
    }

    @Reflection
    public void addBossType(Class<? extends Enemy> enemyClass) {
        final int lvl = 2;
        try {
            Constructor<? extends Enemy> constructor = enemyClass.getConstructor();
            Enemy b = constructor.newInstance();
            b.setCenterX((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2));
            b.setCenterY(0);
            b.setDefaultX(b.getCenterX());
            b.setDefaultY(200);
            b.health *= lvl;
            b.calculateDefaultSpeeds();
            waveList.add(currentWave+1, new Wave().of(b) );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void createWaves() {
        /**
         * Wave 0 Level 0 index 0 Type Enemy1
         * 5 rows each row with 9 enemy.
         */
        if(startSubLevel<=0)
            waveList.add(WaveFactory.Type1(5,10, 1));

        /**
         * Wave 1 Level 1 index 1 Type Enemy2
         * 2 rows of 10 enemies;
         */
        if(startSubLevel <= 1)
            waveList.add(WaveFactory.Type2(2, 10, 1));


        /**
         * Wave 2 level 1 index 2 Type Enemy3
         * 2 circles of 15 enemies
         */
        if(startSubLevel <= 2)
            waveList.add(WaveFactory.Type3((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2),
                300, 2, 15, 100, 20, 1));

        /**
         * Wave 3 level 1 index 2 Type Enemy4
         * 2 circles of 15 enemies
         */
        if(startSubLevel <= 3)
            waveList.add(WaveFactory.Type4((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2),
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2), 350, 2,
                15, 100, 40,1));

        if(startSubLevel <= 4)
            waveList.add(WaveFactory.TypeBoss(1));

        /**
         * Level 2
         */
        if(startSubLevel <= 5)
            waveList.add(WaveFactory.Type1(5,10, 2));
        if(startSubLevel <= 6)
            waveList.add(WaveFactory.Type2(2, 10, 2));
        if(startSubLevel <= 7)
            waveList.add(WaveFactory.Type3((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2),
                300, 2, 15, 100, 20, 2));
        if(startSubLevel <= 8)
            waveList.add(WaveFactory.Type4((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2),
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2), 350, 2,
                15, 100, 40,2));
        if(startSubLevel <= 9)
            waveList.add(WaveFactory.TypeBoss(2));

        /**
         * Level 3
         */
        if(startSubLevel <= 10)
            waveList.add(WaveFactory.Type1(5,12, 3));
        if(startSubLevel <= 11)
            waveList.add(WaveFactory.Type2(2, 10, 3));
        if(startSubLevel <= 12)
            waveList.add(WaveFactory.Type3((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2),
                300, 2, 20, 100, 20, 3));
        if(startSubLevel <= 13)
            waveList.add(WaveFactory.Type4((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2),
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2), 350, 2,
                15, 100, 40,3));
        if(startSubLevel <= 14)
            waveList.add(WaveFactory.TypeBoss(3));

        /**
         * Level 4
         */
        if(startSubLevel <= 15)
            waveList.add(WaveFactory.Type1(5,12, 4));
        if(startSubLevel <= 16)
            waveList.add(WaveFactory.Type2(2, 10, 4));
        if(startSubLevel <= 17)
            waveList.add(WaveFactory.Type3((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2),
                    300, 2, 20, 100, 20, 4));
        if(startSubLevel <= 18)
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
