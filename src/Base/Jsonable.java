package Base;

import com.google.gson.Gson;

public interface Jsonable {
    default String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    <T> T fromJSON(String jsonString);
}
