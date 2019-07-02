package com.saeed.network;

import Base.Data;

import java.io.BufferedWriter;
import java.io.IOException;

public class ServerSender extends Thread {
    private BufferedWriter bufferedWriter;
    public volatile String clientName;
    private volatile ServerData serverData;

    public ServerSender(BufferedWriter bufferedWriter, ServerData serverData){
        this.bufferedWriter = bufferedWriter;
        this.serverData = serverData;
    }

    @Override
    public void run() {
        Data.DynamicData dynamicData;
        long startTime = System.currentTimeMillis(), counter = 0;
        //todo while condition
        while(true){
            try {
                dynamicData = new Data.DynamicData();
                dynamicData.isPaused = serverData.isPaused;
                dynamicData.bombs = serverData.bombs;
                dynamicData.enemies = serverData.enemies;
                dynamicData.enemyShots = serverData.enemyShots;
                dynamicData.shots = serverData.shots;
                dynamicData.rockets = serverData.rockets;

                dynamicData.upgrades = serverData.upgrades;
                dynamicData.rocket = serverData.clients.get(clientName).rocket;
                dynamicData.player = serverData.clients.get(clientName).player;

                dynamicData.name = clientName;
                dynamicData.GERunning = true;
                dynamicData.LERunning = true;
            }catch (Exception e){
                return;
            }
            if(!send(dynamicData.toJSON())) return;
            try {
                sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean send(String data){
        try {
            bufferedWriter.write(data);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            synchronized (serverData){
                serverData.rockets.remove(serverData.clients.get(clientName).rocket);
                serverData.clients.remove(clientName);
                try {
                    bufferedWriter.close();
                } catch (IOException ex) {
                    System.err.println("Error in Server Sender");
                }
                finally {
                    return false;
                }
            }
        }
        return true;
    }
}
