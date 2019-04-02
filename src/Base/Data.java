package Base;

import Elements.*;
import Swing.GameFrame;
import Swing.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Each game must have one instance of this class.
 * Base.Data holds a list of all chickens and shots and rocket and all the other elements of
 * the game. GE and LE access it and change its values.
 */
public class Data {
    //ConcurrentHashSet below(Yes its a set) is very helpful!!
    public volatile Set<Shot> shots = ConcurrentHashMap.newKeySet();
    public volatile Set<Bomb> bombs = ConcurrentHashMap.newKeySet();
    public Rocket rocket = new Rocket();
    public volatile HashSet<Integer> pressedKeys = new HashSet<>();
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public GamePanel gamePanel = new GamePanel(this);
    public GameFrame gameFrame = new GameFrame();
    public volatile boolean isPaused = false;
    //TODO change the path
    public String savePath = "/home/saeed/Desktop/game.data";
    public volatile Player player = new Player("guest");
    public SaveData saveData = new SaveData();

    public Data(Player player){
        this.player = player;
    }

    public Data(){}
}
