package com.saeed.network;

import Base.Game;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerListener extends Thread {
    private ServerSocket serverSocket;
    private List<Socket> clients;
    private JLabel counterLabel;
    private final int maxConnections;
    public boolean gameStarted;
    private Game game;
    public ServerListener(int port, ArrayList<Socket> clients, int maxConnections, Game game){
        this.game = game;
        gameStarted = false;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.maxConnections = maxConnections;
        this.clients = clients;
    }

    @Override
    public void run() {
        Socket temp = null;
        while(clients.size()<=maxConnections){
            try {
                temp = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (clients){
                clients.add(temp);
            }
            if(gameStarted){
                game.activateNewClient(temp);
            }
            counterLabel.setText(String.valueOf(clients.size()));
        }
    }

    public void setCounterLabel(JLabel counterLabel) {
        this.counterLabel = counterLabel;
    }
}
