package com.saeed.network;

import Base.Data;
import Elements.Rocket;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientReceiver extends Thread {
    private BufferedReader bufferedReader;
    private Data data;
    private Rocket tempRocket;

    public ClientReceiver(BufferedReader bufferedReader, Data data){
        this.bufferedReader = bufferedReader;
        this.data = data;
    }

    @Override
    public void run() {
        String tmp = "";
        while (true) {
            try {
                tmp = bufferedReader.readLine();
            } catch (IOException e) {
                synchronized (data){
                    data.dynamicData.GERunning = false;
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        System.err.println("Error in Client Receiver");
                    }
                    finally {
                        return;
                    }
                }
            }

            synchronized (data) {
                tempRocket = data.dynamicData.rocket;
                data.dynamicData = data.dynamicData.fromJSON(tmp);
                //dont reload the rocket to prevent lag.
                //todo when rocket is in revive mode it doesnt work normally.
                data.dynamicData.rocket.setX(tempRocket.getX());
                data.dynamicData.rocket.setY(tempRocket.getY());
            }
//            System.out.println("data received in client. Current number of shots: " + data.dynamicData.shots.size());
        }

    }
}
