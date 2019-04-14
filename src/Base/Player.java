package Base;

public class Player {

    public String name;
    public volatile Integer life = 1,
            rocketLevel = 1,
            shotLevel = 1  ,
            level = 1      ,
            score = 0      ,
            numberOfDeaths = 0,
            food = 0,
            bombs = 3;

    public Player(String name){
        this.name = name;
    }

}
