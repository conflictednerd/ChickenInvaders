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
            this.pauseDialog.data.save();
            save.setEnabled(false);

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
