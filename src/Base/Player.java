package Base;

import com.gilecode.yagson.YaGson;
import com.google.gson.Gson;

public class Player implements Jsonable {

    public String name;
    public volatile Integer life = 1,
            //maxHeat = rocketLevel*5 + 100 or in the beginning, upgrade maxHeat (rocketLevel) times.
            rocketLevel = 1,
            //changes the number of shots fired in each turn and their damage. May be read directly from Player class each time.
            shotLevel = 1  ,
            //Changes type of shots fired.
            shotType = 1   ,
            level = -1     ,
            subLevel = -1   ,
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
        level = -1;
        subLevel = -1;
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
