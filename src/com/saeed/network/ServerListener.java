package com.saeed.network;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerListener extends Thread {
    private ServerSocket serverSocket;
    private List<Socket> clients;
    private Boolean isRunning;
    private JLabel counterLabel;
    public ServerListener(int port, ArrayList<Socket> clients){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.clients = clients;
    }

    @Override
    public void run() {
        isRunning = true;
        Socket temp = null;
        while(isRunning){
            try {
                temp = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (clients){
                clients.add(temp);
            }
            counterLabel.setText(String.valueOf(clients.size()));
        }
    }

    public void finish(){
        this.isRunning = false;
    }

    public void setCounterLabel(JLabel counterLabel) {
        this.counterLabel = counterLabel;
    }
}
