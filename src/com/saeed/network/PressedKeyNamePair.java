package com.saeed.network;

import java.util.HashSet;

public class PressedKeyNamePair {
    public volatile HashSet<Integer> pressedKeys;
    public String playerName;

    public PressedKeyNamePair(String clientName, HashSet<Integer> pressedKeys) {
        this.playerName = clientName;
        this.pressedKeys = pressedKeys;
    }
}
