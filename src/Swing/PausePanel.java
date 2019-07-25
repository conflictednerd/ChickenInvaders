package Swing;

import Base.Data;
import Base.Player;
import Base.SaveData;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PausePanel extends JPanel {
    private PauseDialog pauseDialog;
    private Data data;

    public PausePanel(PauseDialog pauseDialog){
        this.pauseDialog = pauseDialog;
        this.data = pauseDialog.data;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Button resume = new Button("Resume"), save = new Button("Save")
                , exit = new Button("Quit"), addBoss = new Button("Add Boss"), addEnemy =  new Button("Add Enemy");

        resume.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE|Font.BOLD,30));
        save.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE|Font.BOLD,30));
        addEnemy.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE|Font.BOLD, 30));
        addBoss.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE|Font.BOLD, 30));
        exit.setFont(new Font(Font.DIALOG, Font.CENTER_BASELINE|Font.BOLD,30));

        resume.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {}
            @Override
            public void keyPressed(KeyEvent keyEvent) {}
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
                    pauseDialog.data.staticData.gamePanel.syncMouse();
//                    pauseDialog.data.dynamicData.isPaused = false;
                    pauseDialog.data.staticData.pauseRequest = false;
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
                    pauseDialog.data.staticData.gamePanel.syncMouse();
//                    pauseDialog.data.dynamicData.isPaused = false;
                    pauseDialog.data.staticData.pauseRequest = false;
                    pauseDialog.dispose();
                }
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
                    pauseDialog.data.staticData.gamePanel.syncMouse();
//                    pauseDialog.data.dynamicData.isPaused = false;
                    pauseDialog.dispose();
                }
            }
        });

        addEnemy.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {}
            @Override
            public void keyPressed(KeyEvent keyEvent) {}
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
                    pauseDialog.data.staticData.gamePanel.syncMouse();
//                    pauseDialog.data.dynamicData.isPaused = false;
                    pauseDialog.dispose();
                }
            }
        });

        addBoss.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {}
            @Override
            public void keyPressed(KeyEvent keyEvent) {}
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
                    pauseDialog.data.staticData.gamePanel.syncMouse();
//                    pauseDialog.data.dynamicData.isPaused = false;
                    pauseDialog.dispose();
                }
            }
        });

        if(this.pauseDialog.data.staticData.isMultiPlayer){
            save.setEnabled(false);
        }

        save.addActionListener(actionEvent -> {
            this.pauseDialog.data.save();
            save.setEnabled(false);
        });

        addEnemy.addActionListener(actionEvent -> {
            //todo check if is single player or multi-player.
//            if(!data.staticData.isMultiPlayer) {
            Class c = loadClass();
            data.staticData.waitingEnemyClasses.add(c);
//            }
//            else{
//
//            }
        });

        addBoss.addActionListener(actionEvent -> {
            data.staticData.waitingBossClasses.add(loadClass());
        });

        resume.addActionListener(actionEvent -> {
            pauseDialog.data.staticData.gamePanel.syncMouse();
//            pauseDialog.data.dynamicData.isPaused = false;
            pauseDialog.data.staticData.pauseRequest = false;
            pauseDialog.dispose();
        });

        exit.addActionListener(actionEvent -> {
            pauseDialog.data.staticData.database.close();
            System.exit(0);
        });

        add(resume);
        add(save);
        add(addEnemy);
        add(addBoss);
        add(exit);

        setBackground(Color.white);
        setVisible(true);
    }

    private Class loadClass() {
        Class c = null;

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Class File", "class");
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(this);

        URLClassLoader urlClassLoader = null;
        try {
            urlClassLoader = new URLClassLoader(new URL[]{new URL("file://" + chooser.getSelectedFile().getParent() + "/")});
        } catch (MalformedURLException e) {e.printStackTrace();}

        try {
            //regex removes the extension.
            c = urlClassLoader.loadClass(chooser.getSelectedFile().getName().replaceFirst("[.][^.]+$", ""));
        } catch (ClassNotFoundException e) {e.printStackTrace();}

        return c;
    }
}
