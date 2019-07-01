package com.saeed.network;

import Base.Data;
import Base.Player;
import Elements.Rocket;

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
//            startTime = System.currentTimeMillis();
//            if(System.currentTimeMillis()>startTime+1000){
//                startTime = System.currentTimeMillis();
//                System.err.println("Packets sent in last second: " + counter);
//                counter = 0;
//            }
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

            send(dynamicData.toJSON());
            try {
                sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void send(String data){
//        long startTime = System.currentTimeMillis();
        try {
            bufferedWriter.write(data);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.err.println("Time spent in send: " + (System.currentTimeMillis()-startTime)+" mS");
    }
}
