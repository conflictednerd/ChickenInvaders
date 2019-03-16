package Base;

import Elements.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Each game must have one instance of this class.
 * Base.Data holds a list of all chickens and shots and rocket and all the other elements of
 * the game. GE and LE access it and change its values.
 */
public class Data {
    public List<Shot> shots = new ArrayList<>();
    public Rocket rocket = new Rocket();
}
