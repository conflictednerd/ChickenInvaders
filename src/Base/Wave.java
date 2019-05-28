package Base;

import Elements.Enemy;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Wave {
    public boolean hasType4 = false;
    public volatile Set<Enemy> enemies = ConcurrentHashMap.newKeySet();
}
