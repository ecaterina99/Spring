package firstApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MusicPlayer {

    @Value("${musicPlayer.name}") private String name;
    @Value("${musicPlayer.volume}") private int volume;


    public int getVolume() {
        return volume;
    }

    public String getName() {
        return name;
    }


    private Music music1;
    private Music music2;

    @Autowired
    public MusicPlayer(@Qualifier("someClassicalMusic") Music music1, @Qualifier("somePopMusic")Music music2) {
        this.music1 = music1;
        this.music2 = music2;
    }
    public String playMusic() {
        return "Playing: " + music1.getSong()+", "+music2.getSong();
    }

}




   /*  private ClassicalMusic classicalMusic;
    private PopMusic popMusic;
    @Autowired
    public MusicPlayer(ClassicalMusic classicalMusic, PopMusic popMusic) {
        this.classicalMusic = classicalMusic;
        this.popMusic = popMusic;
    }

    @Autowired
    private Music music;

    @Autowired
    public void setMusic(Music music) {
        this.music = music;
    }

    @Autowired
    public MusicPlayer(Music music) {
        this.music = music;
    }

 */

