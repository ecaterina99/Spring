package animal;

public class Cat implements IAnimal {
    @Override
    public void makeSound(String name) {
        System.out.println("animal.Cat " + name + " meow");
    }
}
