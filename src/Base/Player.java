package Base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public String name = "Saeed";
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


    /**
     * @param player
     * @returns json string
     */
    public static String toJson(Player player){
        GsonBuilder gsonBuilder  = new GsonBuilder();
// Allowing the serialization of static fields

        gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
        Gson gson = gsonBuilder.create();
        return gson.toJson(player);
    }

    public static Player JsonToPlayer(String data){
        Gson gson = new Gson();
        return gson.fromJson(data,Player.class);
    }

}
