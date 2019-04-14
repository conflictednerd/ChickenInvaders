package Base;

import Elements.Shot;

import javax.sound.sampled.*;
import java.io.IOException;

public class SoundThread extends Thread{
    private Integer fireSoundsToPlay = 0;


    private static AudioInputStream audioInputStream;
    private static Clip clip;

    static {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(Shot.class.getResourceAsStream("../Assets/sounds/shot/shot2.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException ex) {
            System.err.println("Exception 1");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public SoundThread(){
    }

    @Override
    public void run() {
        super.run();
        while(true){
//            System.err.println(clip.isActive());
            if(clip.isRunning() && fireSoundsToPlay>0){
                fireSoundsToPlay--;
            }

//            if(fireSoundsToPlay>0){
//                clip.setMicrosecondPosition(0);
//                clip.start();
//                fireSoundsToPlay--;
//            }

            while(fireSoundsToPlay>0 && !clip.isRunning())
                try {
                    clip.setMicrosecondPosition(0);
                    clip.start();
                    fireSoundsToPlay--;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
        }
    }
    public void addShotSound(){
        fireSoundsToPlay++;
    }
}
