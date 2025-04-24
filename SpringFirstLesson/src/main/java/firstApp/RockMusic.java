package firstApp;

public class RockMusic implements Music{
    private RockMusic(){}

    public static RockMusic getRockMusic(){
        return new RockMusic();
    }

    public void doMyInit(){
        System.out.println("Doing my initialization RockMusic");
    }

    public void doMyDestroy(){
        System.out.println("Doing my destruction RockMusic");
    }

    @Override
    public String getSong() {
        return "Sonne";
    }
}
