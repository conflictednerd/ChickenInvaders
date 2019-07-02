package com.saeed.network;

import Base.Data;
import Base.Game;
import Base.Jsonable;
import com.gilecode.yagson.YaGson;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;

public class ClientSender extends Thread {
    private final int bombPressed = -24;

    private BufferedWriter bufferedWriter;
    private Data data;
    private Game game;
    private ClientToServerData clientToServerData;
    public ClientSender(BufferedWriter bufferedWriter, Data data, Game game){
        this.bufferedWriter = bufferedWriter;
        this.data = data;
        this.game = game;
        sendPrimaryInfo();
    }

    @Override
    public void run() {
//        sendPrimaryInfo();
        clientToServerData = new ClientToServerData(0, 0, null, false);
        //todo should be while(in_online_game)
        while(true) {
            //TODO? Will they be updated or should i copy the content in pressed keys every time??
            clientToServerData.x = data.dynamicData.rocket.getX();
            clientToServerData.y = data.dynamicData.rocket.getY();
            clientToServerData.pressedKeys = data.staticData.pressedKeys;
            clientToServerData.pauseRequest = data.staticData.pauseRequest;

            if (!send(clientToServerData.toJSON())) break;

            try {
                sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        game.load_intro();
    }

    private boolean send(String data) {
        try {
            bufferedWriter.write(data);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            synchronized (this.data) {
                this.data.dynamicData.GERunning = false;
                try {
                    bufferedWriter.close();
                } catch (IOException ex) {
                    System.err.println("Error in Server Sender");
                } finally {
                    return false;
                }
            }
        }
        return true;
    }

    private void sendPrimaryInfo() {
        send(data.dynamicData.player.toJSON());
        System.out.println("Player info sent from client");
    }

    public static class ClientToServerData implements Jsonable {
        public int x, y;
        public volatile HashSet<Integer> pressedKeys;
        public volatile boolean pauseRequest;

        public ClientToServerData(int x, int y, HashSet<Integer> pressedKeys, boolean pauseRequest){
            this.x = x;
            this.y = y;
            this.pressedKeys = pressedKeys;
            this.pauseRequest = pauseRequest;
        }

        @Override
        public String toJSON() {
            return new YaGson().toJson(this);
        }

        @Override
        public ClientToServerData fromJSON(String jsonString) {
            return new YaGson().fromJson(jsonString, this.getClass());
        }
    }
}
