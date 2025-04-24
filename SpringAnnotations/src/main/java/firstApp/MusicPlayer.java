package firstApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicPlayer {
    private ClassicalMusic classicalMusic;
    private PopMusic popMusic;

    @Autowired
    public MusicPlayer(ClassicalMusic classicalMusic, PopMusic popMusic) {
        this.classicalMusic = classicalMusic;
        this.popMusic = popMusic;
    }


   /*
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

    public String playMusic() {
        return "Playing: " + classicalMusic.getSong();
    }

}
