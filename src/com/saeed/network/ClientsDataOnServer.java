package com.saeed.network;

import Base.Player;
import Elements.Rocket;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class ClientsDataOnServer {
    public Player player;
    public Rocket rocket;
    public volatile HashSet<Integer> pressedKeys;
    public volatile boolean requestedPause;

    public ClientsDataOnServer(){
        player = null;
        rocket = null;
        pressedKeys = new HashSet<>();
        requestedPause = false;
    }
}
