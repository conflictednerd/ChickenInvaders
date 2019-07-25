package Base;

import Elements.*;
import Elements.Shots.Shot;
import Elements.Upgrades.Upgrade;
import Swing.GameFrame;
import Swing.GamePanel;
import com.gilecode.yagson.YaGson;
import com.saeed.database.Database;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
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
public class Data implements Jsonable{
    public volatile DynamicData dynamicData;
    public StaticData staticData;

    /**
     * data that needs to be updated and received from server in multi-player.
     */
    public static class DynamicData implements Jsonable{
        public volatile String name;
        public volatile boolean GERunning, LERunning;

        //ConcurrentHashSet below(Yes its a set) is very helpful!!
        public volatile Set<Shot> shots;
        public volatile Set<EnemyShot> enemyShots;
        public volatile Set<Bomb> bombs;
        public volatile Set<Enemy> enemies;
        public volatile Set<Upgrade> upgrades;
        public volatile List<Rocket> rockets;
        public Rocket rocket;
        public Player player;

        public volatile Boolean isPaused;

        public DynamicData() {
            GERunning = true;
            LERunning = true;

            //ConcurrentHashSet below(Yes its a set) is very helpful!!
            shots = ConcurrentHashMap.newKeySet();
            enemyShots = ConcurrentHashMap.newKeySet();
            bombs = ConcurrentHashMap.newKeySet();
            enemies = ConcurrentHashMap.newKeySet();
            upgrades = ConcurrentHashMap.newKeySet();
            rockets = new ArrayList<>();
            rocket = new Rocket("DEFAULT");
            rockets.add(rocket);

            player = new Player("guest");
            isPaused = false;
        }

        @Override
        public String toJSON() {
            YaGson yaGson = new YaGson();
            return yaGson.toJson(this);
        }

        @Override
        public DynamicData fromJSON(String jsonString) {
            YaGson yaGson = new YaGson();
            return yaGson.fromJson(jsonString, this.getClass());
        }
    }

    public class StaticData{
        public transient boolean isMultiPlayer;
        public volatile HashSet<Integer> pressedKeys;

        public transient Dimension screenSize;

        public transient long startTime;

        public transient GamePanel gamePanel;
        public transient GameFrame gameFrame;
        public volatile boolean pauseDialogOpened;

        transient File file;
        public transient String savePath;
//        public transient volatile Player player;
        public transient SaveData saveData;
        //true: wants pause, false: wants un pause
        public volatile boolean pauseRequest;

        /**Should close it when we are done.*/
        public transient Database database;

        public transient volatile List<Class<? extends Enemy>> waitingEnemyClasses;
        public transient volatile List<Class<? extends Enemy>> addedEnemyClasses;
        public transient volatile List<Class<? extends Enemy>> waitingBossClasses;
        public transient volatile List<Class<? extends Enemy>> addedBossClasses;


        public StaticData(){
            isMultiPlayer = true;
            pressedKeys = new HashSet<>();

            pauseDialogOpened = false;
            screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            startTime = 0;

            gamePanel = new GamePanel(Data.this);
            gameFrame = new GameFrame();

            database = new Database();
            file = new File("game.data");
            savePath = file.getAbsolutePath();
            saveData = new SaveData();
            System.out.println(savePath);

            waitingEnemyClasses = new ArrayList<>();
            addedEnemyClasses = new ArrayList<>();
            waitingBossClasses = new ArrayList<>();
            addedBossClasses = new ArrayList<>();
        }
    }

    public Data() {
        staticData = new StaticData();
        dynamicData = new DynamicData();
        dynamicData.name = dynamicData.player.name;
//        dynamicData.rocket.setOwner(dynamicData.name);
    }

    public void save(){
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(staticData.savePath, false));
            bufferedWriter.write(SaveData.toJson(staticData.saveData));
            bufferedWriter.close();
            System.out.println("Game Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //update players table in database.

        try {
            this.staticData.database.updatePlayers(this.dynamicData.player);
        } catch (SQLException e) {
            System.err.println("Error saving changes to database");
            e.printStackTrace();
        }
    }

//
//    public static String toJson(Data data) {
////        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        Gson gson = new Gson();
//        return gson.toJson(data);
//    }

    @Override
    public String toJSON() {
        YaGson yaGson = new YaGson();
        return yaGson.toJson(this);
    }

    @Override
    public Data fromJSON(String jsonString) {
        YaGson yaGson = new YaGson();
        return yaGson.fromJson(jsonString, this.getClass());
    }
}
