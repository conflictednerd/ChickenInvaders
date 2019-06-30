package com.saeed.network;

import Base.Player;
import Elements.Bomb;
import Elements.Enemy;
import Elements.EnemyShot;
import Elements.Rocket;
import Elements.Shots.Shot;
import Elements.Shots.Shot1;
import Elements.Shots.Shot2;
import Elements.Shots.Shot3;
import Elements.Upgrades.Upgrade;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServerData {
    public volatile List<Player> players;
    public volatile Set<Shot> shots;
    public volatile Set<EnemyShot> enemyShots;
    public volatile Set<Bomb> bombs;
    public volatile Set<Enemy> enemies;
    public volatile Set<Upgrade> upgrades;
    public volatile List<Rocket> rockets;

//    public volatile List<PressedKeyNamePair> pressedKeys;
    public volatile HashMap<String, ClientsDataOnServer> clients;

    public transient Dimension screenSize;

    public volatile Boolean isPaused;

    public ServerData(){
        players = new ArrayList<>();
        clients = new HashMap<>();
        shots = ConcurrentHashMap.newKeySet();
        enemyShots = ConcurrentHashMap.newKeySet();
        bombs = ConcurrentHashMap.newKeySet();
        enemies = ConcurrentHashMap.newKeySet();
        upgrades = ConcurrentHashMap.newKeySet();
        rockets = new ArrayList<>();

//        pressedKeys = new ArrayList<>();

        isPaused = false;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    }
}