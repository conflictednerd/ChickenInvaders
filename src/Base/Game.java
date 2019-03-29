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

    IntroPanel introPanel = new IntroPanel(this);

    public Game(){
        introPanel.setSize(Data.screenSize);
        data.gameFrame.contentPane.add(introPanel);
        introPanel.repaint();
        introPanel.revalidate();


        //start menu stuff.

        //player selection and all the stuff should happen here so start panel must have a button or
        //some kind of mechanism to select the player. after that the graphic engine and logic engine
        //must start and change the contentpane to gamepanel. and then the rendering and all the stuff.
//        data.panelLocationOnScreen = gamePanel.getLocationOnScreen();
//        data.gamePanel = gamePanel;
//        data.gameFrame = gameFrame;

    }

    public void play(){
        data.gameFrame.contentPane.remove(introPanel);
        data.gameFrame.contentPane.add(data.gamePanel);
        data.gamePanel.requestFocus();
        data.gameFrame.pack();
        GE = new GraphicEngine(data);
        LE = new LogicEngine(data);
        GE.start();
        LE.start();
        data.gamePanel.syncMouse();
    }
}
