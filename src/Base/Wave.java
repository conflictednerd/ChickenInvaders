package Base;

import Elements.Enemy;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Wave {
    public volatile Set<Enemy> enemies = ConcurrentHashMap.newKeySet();
}
