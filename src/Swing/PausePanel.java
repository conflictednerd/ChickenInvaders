package Swing;

import Base.Player;
import Base.SaveData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PausePanel extends JPanel {
    private PauseDialog pauseDialog;

    public PausePanel(PauseDialog pauseDialog){
        this.pauseDialog = pauseDialog;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Button resume = new Button("Resume"), save = new Button("Save"), exit = new Button("Quit");

        resume.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE|Font.BOLD,30));
        save.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE|Font.BOLD,30));
        exit.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE|Font.BOLD,30));

        resume.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {}
            @Override
            public void keyPressed(KeyEvent keyEvent) {}
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
                    pauseDialog.data.gamePanel.syncMouse();
                    pauseDialog.data.isPaused = false;
                    pauseDialog.dispose();
                }
            }
        });

        save.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {}
            @Override
            public void keyPressed(KeyEvent keyEvent) {}
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
                    pauseDialog.data.gamePanel.syncMouse();
                    pauseDialog.data.isPaused = false;
                    pauseDialog.dispose();
                }
            }
        });
        save.addActionListener(actionEvent -> {
            //TODO When selecting a player, use bufferedReader to read players data.
            BufferedWriter bufferedWriter;
            try {
                /*
                TODO:it might be a good idea to hash the json before writing it to the file so that no one can mess with our game!!
                */
                bufferedWriter = new BufferedWriter(new FileWriter(pauseDialog.data.savePath, false));
//                bufferedWriter.write(Player.toJson(pauseDialog.data.player));
                bufferedWriter.write(SaveData.toJson(pauseDialog.data.saveData));
                bufferedWriter.close();
                System.out.println("Game Saved");
                save.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        exit.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {}
            @Override
            public void keyPressed(KeyEvent keyEvent) {}
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
                    pauseDialog.data.gamePanel.syncMouse();
                    pauseDialog.data.isPaused = false;
                    pauseDialog.dispose();
                }
            }
        });

        resume.addActionListener(actionEvent -> {
            pauseDialog.data.gamePanel.syncMouse();
            pauseDialog.data.isPaused = false;
            pauseDialog.dispose();
        });

        exit.addActionListener(actionEvent -> System.exit(0));

        add(resume);
        add(save);
        add(exit);

        setBackground(Color.white);
        setVisible(true);
    }
}
