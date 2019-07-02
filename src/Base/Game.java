package Base;

import Swing.*;
import com.saeed.network.*;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Game {
    public  GraphicEngine GE;
    private LogicEngine LE;
    private long startTime = 0;
    private int maxLevels = 1;
    public volatile Data data = new Data();

    private ClientSender CS;
    private ClientReceiver CR;
    private Socket socket;
    private ArrayList<Socket> clients;
    private ArrayList<ServerReceiver> serverReceivers = new ArrayList<>();
    private ArrayList<ServerSender> serverSenders = new ArrayList<>();
    private ServerLogicEngine SLE;
    private ServerListener serverListener;

    public Game(){
        load_player_selection();
    }

    public void play(){
        clearContentPane();
        load_game();

        GE = new GraphicEngine(data);
        LE = new LogicEngine(data);
        List<Player> players = new ArrayList<>();
        players.add(data.dynamicData.player);
        LE.setLevelManager(new LevelManager(players, data.dynamicData.enemies));
        GE.start();
        LE.start();
        data.staticData.startTime = System.currentTimeMillis();
        data.staticData.gamePanel.syncMouse();
    }

    public void playAsClient(String ip, int port){
        createClient(ip, port);

        clearContentPane();
        load_game();

        GE.start();
        CS.start();
        CR.start();

        data.staticData.startTime = System.currentTimeMillis();
        data.staticData.gamePanel.syncMouse();
    }

    private void createClient(String ip, int port) {
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            CR = new ClientReceiver(new BufferedReader(new InputStreamReader(socket.getInputStream())), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            CS = new ClientSender(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), data, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GE = new GraphicEngine(data);
    }

    /**
     * This method gets called when the player requests to create a new server for a new game.
     * @param port
     */
    public void playAsServer(int port, int maxPlayers, int maxLevels){
        this.maxLevels = maxLevels;
        clients = new ArrayList<>();
        serverListener = new ServerListener(port, clients, maxPlayers, this);
        serverListener.setCounterLabel(load_server_waiting());
        serverListener.start();
        createClient("localhost", port);
    }

    /**
     * This method gets called when the player request to start multi-player game with current participants.
     */
    public void startServerGame(){
        /**
         * create workerThreads and server logic engine.
         */
        SLE = new ServerLogicEngine(maxLevels);
        if(SLE.getServerData() == null){
            System.out.println("serverData is null");
        }
        CS.start();
        CR.start();
        for(Socket s:clients){
            createConnections(s);
        }
        /**
         * load gamePanel and start game for player that started the server
         */
        clearContentPane();
        load_game();


        for (ServerReceiver sr: serverReceivers) sr.start();
        for (ServerSender ss: serverSenders) ss.start();
        serverListener.gameStarted = true;

        GE.start();
//        CS.start();
//        CR.start();

        data.staticData.startTime = System.currentTimeMillis();
        data.staticData.gamePanel.syncMouse();
        /**
         * start server worker threads
         */

//        for (ServerReceiver sr: serverReceivers) sr.start();
//        for (ServerSender ss: serverSenders) ss.start();

        SLE.start();
    }

    public void load_intro(){
        clearContentPane();
        IntroPanel introPanel = new IntroPanel(this);
        introPanel.setSize(data.staticData.screenSize);
        data.staticData.gameFrame.contentPane.add(introPanel);
        introPanel.repaint();
        introPanel.revalidate();
        data.staticData.gameFrame.pack();
    }
    public void load_player_selection(){
        clearContentPane();
        PlayerSelectionPanel playerSelectionPanel = new PlayerSelectionPanel(this);
        playerSelectionPanel.setSize(data.staticData.screenSize);
        data.staticData.gameFrame.contentPane.add(playerSelectionPanel);
        playerSelectionPanel.repaint();
        playerSelectionPanel.revalidate();
        data.staticData.gameFrame.pack();
    }
    public void load_game(){
        data.staticData.gameFrame.contentPane.add(data.staticData.gamePanel);
        data.staticData.gamePanel.requestFocus();
        data.staticData.gameFrame.pack();
    }
    private void clearContentPane(){
        data.staticData.gameFrame.contentPane.removeAll();
    }

    public LogicEngine getLogicEngine(){
        return LE;
    }

    public void load_server_creation() {
        clearContentPane();
        ServerCreationPanel serverCreationPanel = new ServerCreationPanel(this);
        serverCreationPanel.setSize(data.staticData.screenSize);
        data.staticData.gameFrame.contentPane.add(serverCreationPanel);
        serverCreationPanel.repaint();
        serverCreationPanel.revalidate();
        data.staticData.gameFrame.pack();
    }

    public JLabel load_server_waiting() {
        clearContentPane();
        ServerWaitingPanel serverWaitingPanel = new ServerWaitingPanel(this);
        serverWaitingPanel.setSize(data.staticData.screenSize);
        data.staticData.gameFrame.contentPane.add(serverWaitingPanel);
        serverWaitingPanel.repaint();
        serverWaitingPanel.revalidate();
        data.staticData.gameFrame.pack();
        return serverWaitingPanel.getCounterLabel();
    }

    public void load_client_creation() {
        clearContentPane();
        ClientCreationPanel clientCreationPanel = new ClientCreationPanel(this);
        clientCreationPanel.setSize(data.staticData.screenSize);
        data.staticData.gameFrame.contentPane.add(clientCreationPanel);
        clientCreationPanel.repaint();
        clientCreationPanel.revalidate();
        data.staticData.gameFrame.pack();
    }

    private void createConnections(Socket s) {
        try {
            serverReceivers.add(new ServerReceiver(
                    new BufferedReader(new InputStreamReader(s.getInputStream()))
                    , SLE.getServerData()
            ).getInitialPacket());
            serverSenders.add(new ServerSender(
                    new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))
                    , SLE.getServerData()));
            serverSenders.get(serverSenders.size() - 1).clientName = serverReceivers.get(serverReceivers.size() - 1).clientName;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void activateNewClient(Socket s){
        createConnections(s);
        try {
            serverReceivers.get(serverReceivers.size() - 1).start();
        } catch (Exception e) {
            System.err.println("Thread already started(In Game)");
        } finally {
            try {
                serverSenders.get(serverSenders.size() - 1).start();
            } catch (Exception e) {
                System.err.println("Thread already started(In Game)");
            }
        }
    }
}
