package animal;

public class Dog implements IAnimal {
    @Override
    public void makeSound(String name) {
        System.out.println("animal.Dog " + name + " barks");
    }

}
