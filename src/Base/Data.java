package Base;

import Elements.*;
import Elements.Upgrades.Upgrade;
import Swing.GameFrame;
import Swing.GamePanel;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    public volatile Set<Upgrade> upgrades = ConcurrentHashMap.newKeySet();
    public Rocket rocket = new Rocket();

    public volatile HashSet<Integer> pressedKeys = new HashSet<>();

    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public volatile boolean isPaused = false;

    public long startTime = 0;

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
        System.out.println(savePath);
    }

    public void save(){
        BufferedWriter bufferedWriter;
        try {
                /*
                TODO:it might be a good idea to hash the json before writing it to the file so that no one can mess with our game!!
                */
            bufferedWriter = new BufferedWriter(new FileWriter(savePath, false));
            bufferedWriter.write(SaveData.toJson(saveData));
            bufferedWriter.close();
            System.out.println("Game Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
