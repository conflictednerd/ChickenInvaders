package Base;

import com.gilecode.yagson.YaGson;
import com.google.gson.Gson;

public class Player implements Jsonable {

    public String name;
    public volatile Integer life = 2,
            //maxHeat = rocketLevel*5 + 100 or in the beginning, upgrade maxHeat (rocketLevel) times.
            rocketLevel = 1,
            //changes the number of shots fired in each turn and their damage. May be read directly from Player class each time.
            shotLevel = 1  ,
            //Changes type of shots fired.
            shotType = 1   ,
            level = 0     ,
            subLevel = 0   ,
            score = 0      ,
            coins = 0      ,
            numberOfDeaths = 0,
            bombs = 3,
            timePlayed = 0;

    public transient volatile boolean waitingForShotCooldown = false;
    public transient volatile Long coolDownTimer = 0l, shootingTimer = null, timeOfLastShot = 0l;
    public transient volatile double shotHeat = 0, maxHeat = 100;


    public Player(String name){
        this.name = name;

        life = 2;
        rocketLevel = 1;
        shotLevel = 2;
        shotType = 1;
        level = 0;
        subLevel = 0;
        score = 0;
        coins = 0;
        numberOfDeaths = 0;
        bombs = 3;
        timePlayed = 0;

        //Other info
        waitingForShotCooldown = false;
        coolDownTimer = 0l;
        shootingTimer = null;
        timeOfLastShot = 0l;
        shotHeat = 0;
        maxHeat = 100;
    }

    //for serialization purposes
    public Player(){}


    /**
     * Yagson didn't work with database(unusual behaviour when using from json resulted in not initializing
     * Players fields. So I wrote these two static methods that use plain Gson. Despite these strange behaviours,
     * I shall use these static methods only for read and write from and to database and not use them in any other context.
     * @param jsonString
     * @return
     */
    public static Player SfromJson(String jsonString){
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Player.class);
    }

    public static String StoJson(Player p){
        Gson gson = new Gson();
        return gson.toJson(p);
    }


    @Override
    public String toJSON() {
        YaGson yaGson = new YaGson();
        return yaGson.toJson(this);
    }

    @Override
    public Player fromJSON(String jsonString) {
        YaGson yaGson = new YaGson();
        return yaGson.fromJson(jsonString, this.getClass());
    }
}
