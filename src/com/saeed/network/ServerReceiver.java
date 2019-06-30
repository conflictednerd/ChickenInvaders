package com.saeed.network;

import Base.Data;
import Base.Player;
import Elements.Rocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class ServerReceiver extends Thread {
    private BufferedReader bufferedReader;
    private ServerData serverData;
    public volatile String clientName;

    public ServerReceiver(BufferedReader bufferedReader, ServerData serverData){
        this.bufferedReader = bufferedReader;
        this.serverData = serverData;
    }

    @Override
    public void run() {
        ClientSender.ClientToServerData receivedData = new ClientSender.ClientToServerData(0,0,null);
//        getInitialPacket();

        //todo while condition.
        while(true){
            try {
                receivedData =  receivedData.fromJSON(bufferedReader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(Rocket r:serverData.rockets){
                if(clientName.equals(r.getOwner())){
                    synchronized (r){
                        r.setX(receivedData.x);
                        r.setY(receivedData.y);
                    }
                }
            }
            // update or add pressed keys
            synchronized (serverData.clients) {
                serverData.clients.get(clientName).pressedKeys = receivedData.pressedKeys;
            }
//            boolean flag = true;
//            for(PressedKeyNamePair p:serverData.pressedKeys){
//                if(clientName.equals(p.playerName)){
//                    p.pressedKeys = receivedData.pressedKeys;
//                    flag = false;
//                    break;
//                }
//            }
//            if(flag){
//                serverData.pressedKeys.add(new PressedKeyNamePair(clientName, receivedData.pressedKeys));
//            }
        }
    }

    /**
     * should manually be called after creation.
     * @return itself for chaining
     */
    public ServerReceiver getInitialPacket() {
        Player p = new Player("");
        try {
            p = p.fromJSON( bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClientsDataOnServer temp = new ClientsDataOnServer();
        temp.player = p;
        temp.rocket = new Rocket(p.name);
        serverData.clients.put(p.name, temp);
        serverData.players.add(p);
        serverData.rockets.add(temp.rocket);
        //todo because client name in server sender and receiver both point to one string this will probably:) work.
        this.clientName = p.name;
        System.out.println("player info received in server");
        return this;
    }
}
