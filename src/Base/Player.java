package Base;

public class Player {

    public String name;
    public volatile Integer life = 1,
            //maxHeat = rocketLevel*5 + 100 or in the beginning, upgrade maxHeat (rocketLevel) times.
            rocketLevel = 1,
            //changes the number of shots fired in each turn and their damage. May be read directly from Player class each time.
            shotLevel = 1  ,
            //Changes type of shots fired.
            shotType = 1   ,
            level = -1     ,
            subLevel = 0   ,
            score = 0      ,
            numberOfDeaths = 0,
            food = 0,
            bombs = 3,
            timePlayed = 0;


    public Player(String name){
        this.name = name;

        life = 1;
        rocketLevel = 1;
        shotLevel = 3;
        shotType = 2;
        level = -1;
        subLevel = 0;
        score = 0;
        numberOfDeaths = 0;
        food = 0;
        bombs = 3;
        timePlayed = 0;
    }

}
