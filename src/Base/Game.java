package Base;

import Swing.GameFrame;
import Swing.GamePanel;
import Swing.IntroPanel;
import Swing.StartPanel;

import java.awt.*;

public class Game {
    GraphicEngine GE;
    LogicEngine LE;
    Data data = new Data();
    /*
    Start menu and setting and player and ... should be handled here after all that graphic engine should
    start its work to go to game panel. so it might be a good idea to have the main frame in here.
     */

//    GameFrame gameFrame = new GameFrame();
//    GamePanel gamePanel = new GamePanel(data);



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
        load_intro();
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
        IntroPanel introPanel = new IntroPanel(this);
        introPanel.setSize(Data.screenSize);
        data.gameFrame.contentPane.add(introPanel);
        introPanel.repaint();
        introPanel.revalidate();
    }
    public void load_player_selection(){

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
