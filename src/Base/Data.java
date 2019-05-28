package Base;

import Elements.*;
import Swing.GameFrame;
import Swing.GamePanel;

import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Each game must have one instance of this class.
 * Base.Data holds a list of all chickens and shots and rocket and all the other elements of
 * the game. GE and LE access it and change its values.
 */
public class Data {
    public volatile boolean GERunning = true, LERunning = true;

    //ConcurrentHashSet below(Yes its a set) is very helpful!!
    public volatile Set<Shot> shots = ConcurrentHashMap.newKeySet();
    public volatile Set<EnemyShot> enemyShots = ConcurrentHashMap.newKeySet();
    public volatile Set<Bomb> bombs = ConcurrentHashMap.newKeySet();
    public volatile Set<Enemy> enemies = ConcurrentHashMap.newKeySet();
    public Rocket rocket = new Rocket();

    public volatile HashSet<Integer> pressedKeys = new HashSet<>();

    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public volatile boolean isPaused = false;

    public GamePanel gamePanel = new GamePanel(this);
    public GameFrame gameFrame = new GameFrame();

    File file = new File("game.data");
    public String savePath = file.getAbsolutePath();
    public volatile Player player = new Player("guest");
    public SaveData saveData = new SaveData();


    public Data(Player player){
        this.player = player;
    }

    public Data(){
        System.err.println(savePath);

//        //TEMPORARY
//        Enemy temp = new Enemy1(0,0,0,0);
//        temp = null;
//        int prevCenterX = 0, prevMinX = 0, prevMaxX = Toolkit.getDefaultToolkit().getScreenSize().width, y = 100;
//
//        for(int j = 0; j<5; j++) {
//            for (int i = 0; i < 10; i++) {
//                prevCenterX += Enemy1.getWidth() + 10;
//                enemies.add(new Enemy1(prevCenterX, y, prevMinX, prevMaxX));
////            prevMinX += Enemy1.getWidth() + 10;
//            }
//            y+=Enemy1.getHeight();
//            prevCenterX = 0;
//        }

    }
}
