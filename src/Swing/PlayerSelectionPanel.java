package Swing;

import Base.Game;
import Base.Player;
import Base.SaveData;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.SQLException;

public class PlayerSelectionPanel extends JPanel {
    private Game game;
    private Image background;
    private int x, y;

    {
        try {
            background = ImageIO.read(PlayerSelectionPanel.class.getResourceAsStream("../Assets/BackGrounds/playerSelection1.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerSelectionPanel(Game game){
        super();
        this.game = game;
        initialize();
        setPanel();
    }

    private void setPanel() {
        Label welcome = new Label("Welcome to the best game EVER!"), info1 = new Label("Please Enter your name. If you already have an account your data will automatically be loaded.");
        JTextField username = new JTextField("Enter your name here");
        JButton ok = new JButton("OK"), quit = new JButton("QUIT");


        welcome.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE, 50));
        info1.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE, 25));
        username.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE, 30));
        ok.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE, 25));
        quit.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE, 25));

        welcome.setForeground(Color.WHITE);
        info1.setForeground(Color.WHITE);
        welcome.setBackground(Color.black);
        info1.setBackground(Color.black);


        welcome.setSize(1000, 100);
        info1.setSize(1200, 100);
        username.setSize(500, 50);
        ok.setSize(120, 30);
        quit.setSize(120, 30);

        welcome.setLocation(x + 20, y + 20);
        info1.setLocation(welcome.getX(), (int)(welcome.getY()+welcome.getHeight()+40));
        username.setLocation(info1.getX(), info1.getY() + info1.getHeight()*4);
        ok.setLocation(username.getX()+10, username.getY()+username.getHeight()+10);
        quit.setLocation(ok.getX()+ok.getWidth()+5, ok.getY());

        quit.addActionListener(actionEvent -> {
            game.data.staticData.database.close();
            System.exit(0);
        });
        ok.addActionListener(actionEvent -> {
            String name = username.getText(), rawData = null;
            boolean done = false;

            /**
             * Database integration Code.
             */
            try {
                game.data.staticData.database.addPlayer(new Player(name));
                game.data.dynamicData.player = game.data.staticData.database.selectPlayer(name);
                if(game.data.dynamicData.player == null){
                    System.err.println("player not found in database");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            /**
             * End of database integration code.
             */

            //TODO It might be a good idea to NOT save guests.

            if(name.equals("")||name.equals(null)) name = "guest";
            try{
                BufferedReader bufferedReader = new BufferedReader(new FileReader(game.data.staticData.savePath));
                rawData = bufferedReader.readLine();
            } catch (FileNotFoundException e){
                //No saves found
                game.data.staticData.saveData = new SaveData();
//                game.data.dynamicData.player = new Player(name);
                game.data.staticData.saveData.players.add(game.data.dynamicData.player);
                File file = new File(game.data.staticData.savePath);
                done = true;
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.err.println("Error Creating save file");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(!done) {
                    try {
                        SaveData s = SaveData.fromJson(rawData);
                        boolean playerExists = false;
                        if(!(s == null) && !(s.players == null)) {
                            for (Player p : s.players) {
                                if (name.equals(p.name)) {
//                                    game.data.dynamicData.player = p;
                                    game.data.staticData.saveData = s;
                                    playerExists = true;
                                    break;
                                }
                            }
                        }
                        if (!playerExists) {
                            //new player
//                            game.data.dynamicData.player = new Player(name);
                            if(s == null) game.data.staticData.saveData = new SaveData();
                            else game.data.staticData.saveData = s;
                            game.data.staticData.saveData.players.add(game.data.dynamicData.player);
                        }
                    } catch (Exception e) {
                        //save corrupted.
                        e.printStackTrace();
                        game.data.staticData.saveData = new SaveData();
//                        game.data.dynamicData.player = new Player(name);
                        //TODO It might be a good idea to NOT save guests.
                        game.data.staticData.saveData.players.add(game.data.dynamicData.player);
                    }
                }
                if(game.data.dynamicData.player.maxHeat == 0){
                    game.data.dynamicData.player.maxHeat = 100;
                }
                game.load_intro();
            }
        });


        add(welcome);
        add(info1);
        add(username);
        add(ok);
        add(quit);

    }

    private void initialize() {
        //TODO maybe box layout??
        setLayout(null);
        x = (int)game.data.staticData.gameFrame.getLocationOnScreen().getX();
        y = (int)game.data.staticData.gameFrame.getLocationOnScreen().getY();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}
