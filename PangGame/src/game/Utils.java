package game;

import javax.sound.sampled.*;
import java.io.File;

public class Utils {
    public static void playSound(String filename) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filename));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
          
        }
    }
}
