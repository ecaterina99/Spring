package firstApp;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpring {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "appContext.xml"
        );

        MusicPlayer musicPlayer = context.getBean("musicPlayer", MusicPlayer.class);
        System.out.println(musicPlayer.getName());
        System.out.println(musicPlayer.getVolume());

        ClassicalMusic classicalMusic = context.getBean("someClassicalMusic", ClassicalMusic.class);
        context.close();

    }
}



/*
        MusicPlayer musicPlayer = context.getBean("musicPlayer", MusicPlayer.class);
        musicPlayer.playMusic();

        Computer computer = context.getBean("computer", Computer.class);
        System.out.println(computer);

 */
/*
        ClassicalMusic classicalMusic2 = (ClassicalMusic) context.getBean("someClassicalMusic", ClassicalMusic.class);
        System.out.

                println(classicalMusic == classicalMusic2);

        ClassicalMusic classicalMusic = context.getBean("someClassicalMusic", ClassicalMusic.class);

 */


