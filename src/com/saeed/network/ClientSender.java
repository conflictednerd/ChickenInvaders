package com.saeed.network;

import Base.Data;
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
    private ClientToServerData clientToServerData;
    public ClientSender(BufferedWriter bufferedWriter, Data data){
        this.bufferedWriter = bufferedWriter;
        this.data = data;
        sendPrimaryInfo();
    }

    @Override
    public void run() {
//        sendPrimaryInfo();
        clientToServerData = new ClientToServerData(0, 0, null, false);
        //todo should be while(in_online_game)
        while(true){
//            if(!data.dynamicData.isPaused){
                //TODO? Will they be updated or should i copy the content in pressed keys every time??
                clientToServerData.x = data.dynamicData.rocket.getX();
                clientToServerData.y = data.dynamicData.rocket.getY();
                clientToServerData.pressedKeys = data.staticData.pressedKeys;
                clientToServerData.pauseRequest = data.staticData.pauseRequest;
                send(clientToServerData.toJSON());
//                if(data.staticData.pressedKeys.contains(bombPressed)){
//                    data.staticData.pressedKeys.remove(bombPressed);
//                    System.err.println("bomb sent");
//                }

                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }

    private void send(String data) {
        try {
            bufferedWriter.write(data);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
