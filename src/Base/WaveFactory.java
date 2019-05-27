package Base;

import Elements.Enemies.Enemy1;
import Elements.Enemies.Enemy2;
import Elements.Enemies.Enemy3;
import Elements.Enemies.Enemy4;
import Elements.Enemy;

import java.awt.*;

public class WaveFactory {
    private static WaveFactory ourInstance = new WaveFactory();

    private WaveFactory() {}


    public static Wave Type1(int rows, int cols, int lvl){
        Wave wave = new Wave();
        for(int i = 1; i<=rows; i++){
            for(int j = 1; j<=cols; j++){
                Enemy enemy = new Enemy1();
                enemy.health += lvl;
                enemy.setCenterY(0);
                enemy.setCenterX((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2));
                enemy.setDefaultX(10 + j*enemy.getWidth());
                enemy.setDefaultY(10 + i*enemy.getHeight());
                enemy.calculateDefaultSpeeds();
                wave.enemies.add(enemy);
            }
        }
        return wave;
    }

    public static Wave Type2(int rows, int cols, int lvl){
        Wave wave = new Wave();
        for(int i = 1; i<=rows; i++){
            for(int j = 1; j<cols;j++){
                Enemy enemy = new Enemy2();
                enemy.health += lvl;
                enemy.setCenterY(0);
                enemy.setCenterX((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2));
                enemy.setDefaultX(10 + j*enemy.getWidth());
                enemy.setDefaultY(10 + i*enemy.getHeight());
                enemy.calculateDefaultSpeeds();
                wave.enemies.add(enemy);
            }
        }
        return wave;
    }

    public static Wave Type3(int startPivotX, int startPivotY, int radius, int layers, int perCircle,
                             int padding, double angleDifference, int lvl){
        Wave wave = new Wave();
        double thetaDeg = 0;
        for(int i = 0; i<layers; i++){
            radius += padding;
            for(int j = 0; j<perCircle; j++){
                thetaDeg = angleDifference + i*layers + j*360/perCircle;

                Enemy enemy = new Enemy3(startPivotX, startPivotY, radius);
                enemy.health += lvl;
                ((Enemy3) enemy).thetaDeg = thetaDeg;
                //These conditions check initial position of enemies and determine defaultX,Y using that.
                if(thetaDeg < 45 || thetaDeg > 315) {
                    enemy.setCenterY(startPivotY);
                    enemy.setCenterX(2*startPivotX);
                }
                else if(thetaDeg<180-45){
                    enemy.setCenterY(2*startPivotY);
                    enemy.setCenterX(startPivotX);
                }
                else if(thetaDeg<180+45){
                    enemy.setCenterY(startPivotY);
                    enemy.setCenterX(0);
                }
                else{
                    enemy.setCenterY(0);
                    enemy.setCenterX(startPivotX);
                }
                enemy.setDefaultY((int) (startPivotY + radius*Math.sin(Math.toRadians(thetaDeg))));
                enemy.setDefaultX((int) (startPivotX + radius*Math.cos(Math.toRadians(thetaDeg))));
                enemy.calculateDefaultSpeeds();
                wave.enemies.add(enemy);
            }
        }
        return wave;
    }

    public static Wave Type4(int startPivotX, int startPivotY, int radius, int layers, int perCircle,
                             int padding, int angleDifference, int lvl){
        Wave wave = new Wave();
        double thetaDeg = 0;
        Enemy4.pivotX = startPivotX;
        Enemy4.pivotY = startPivotY;
        Enemy4.V = 4;
        for(int i = 0; i<layers; i++){
            radius += padding;
            for(int j = 0; j<perCircle; j++){
                thetaDeg = angleDifference + i*layers + j*360/perCircle;

                Enemy enemy = new Enemy4(radius);
                ((Enemy4) enemy).thetaDeg = thetaDeg;
                //These conditions check initial position of enemies and determine defaultX,Y using that.
                if(thetaDeg < 45 || thetaDeg > 315) {
                    enemy.setCenterY(startPivotY);
                    enemy.setCenterX(2*startPivotX);
                }
                else if(thetaDeg<180-45){
                    enemy.setCenterY(2*startPivotY);
                    enemy.setCenterX(startPivotX);
                }
                else if(thetaDeg<180+45){
                    enemy.setCenterY(startPivotY);
                    enemy.setCenterX(0);
                }
                else{
                    enemy.setCenterY(0);
                    enemy.setCenterX(startPivotX);
                }
                enemy.setDefaultY((int) (startPivotY + radius*Math.sin(Math.toRadians(thetaDeg))));
                enemy.setDefaultX((int) (startPivotX + radius*Math.cos(Math.toRadians(thetaDeg))));
                enemy.calculateDefaultSpeeds();
                wave.enemies.add(enemy);
            }
        }
        return wave;
    }

}
