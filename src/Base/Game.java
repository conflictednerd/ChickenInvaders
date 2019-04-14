package Base;

import Swing.*;

public class Game {
    GraphicEngine GE;
    LogicEngine LE;
    public Data data = new Data();

    public Game(){
        load_player_selection();
    }

    public void play(){
        //TODO level selection happens here.
        clearContentPane();load_game(data.player.level);
        GE = new GraphicEngine(data);
        LE = new LogicEngine(data);
        GE.start();
        LE.start();
        data.gamePanel.syncMouse();
    }

    public void load_intro(){
        clearContentPane();
        IntroPanel introPanel = new IntroPanel(this);
        introPanel.setSize(Data.screenSize);
        data.gameFrame.contentPane.add(introPanel);
        introPanel.repaint();
        introPanel.revalidate();
        data.gameFrame.pack();
    }
    public void load_player_selection(){
        PlayerSelectionPanel playerSelectionPanel = new PlayerSelectionPanel(this);
        playerSelectionPanel.setSize(Data.screenSize);
        data.gameFrame.contentPane.add(playerSelectionPanel);
        playerSelectionPanel.repaint();
        playerSelectionPanel.revalidate();
        data.gameFrame.pack();
    }
    public void load_game(int level){
        data.gameFrame.contentPane.add(data.gamePanel);
        data.gamePanel.requestFocus();
        data.gameFrame.pack();
    }
    private void clearContentPane(){
        data.gameFrame.contentPane.removeAll();
    }
}
