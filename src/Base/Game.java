package Base;

import Swing.*;

import java.awt.*;

public class Game {
    GraphicEngine GE;
    LogicEngine LE;
    public Data data = new Data();


//TODO NOPE DELETE IT
    public Game(Data data){
        this.data = data;
        //TODO level loader goes here for selected player.
        switch (data.player.level){
            case 1:
                System.err.println("blah");
                System.out.println("blah");
                break;
            case 2:
                break;
        }
    }

    public Game(){
        load_player_selection();
//        load_intro();
    }

    public void play(){
//        data.gameFrame.contentPane.remove(introPanel);
        clearContentPane();load_game(1);
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
