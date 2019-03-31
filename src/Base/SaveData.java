package Base;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SaveData {
    public List<Player> players = new ArrayList<>();

    public static String toJson(SaveData saveData) {
        Gson gson = new Gson();
        return gson.toJson(saveData);
    }
}
