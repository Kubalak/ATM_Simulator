package sound;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
/**
 * Klasa odpowiedzialna za udźwiękowienie programu.
 *
 * @author Patryk Jaworski
 * @version 1.0
 * @since 2021-04-20
 */

public class Sound{
    /**
     * Metod odpowiedzialna za odtworzenie pojedynczego dźwięku.
     * Korzysta ona z dźwięków zawartych jako zasób.
     * @param soundName <b style="color:#0B5E03;">String</b> - Ścieżka do dźwięku w zasobach.
     */
    public void playSound(String soundName)
    {
        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(soundName));
            Clip clip = AudioSystem.getClip( );
            clip.open(audioInputStream);
            clip.start( );
        }
        catch(Exception ex)
        {
            System.out.println("Error with playing sound.");
            ex.printStackTrace( );
        }
    }
    /**
     * Metod odpowiedzialna za odtwarzenie zapętlonego dźwięku.
     * Korzysta ona z dźwięków zawartych jako zasób.
     * @param musicName <b style="color:#0B5E03;">String</b> - Ścieżka do dźwięku w zasobach.
     */
    public void playBackgroundMusic(String musicName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(musicName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}
