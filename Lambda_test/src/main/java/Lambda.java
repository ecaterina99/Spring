import animal.Cat;
import animal.Dog;
import animal.IAnimal;
import calc.Multiply;
import family.Baby;
import family.Cake;
import family.Man;
import family.Woman;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Lambda {
    public static void main(String[] args) {
        //declararea tip concret
        Dog bob = new Dog();
        bob.makeSound("Bob");
        Cat tom = new Cat();
        tom.makeSound("Tom");

        //declararea tip abstract
        IAnimal rex = new Dog();
        rex.makeSound("Rex");
        IAnimal kitty = new Cat();
        kitty.makeSound("Kitty");

        //Clasa anonima
        IAnimal perry = new IAnimal() {
            @Override
            public void makeSound(String surname) {
                System.out.println("Parrot " + surname + " papagai");
            }
        };
        perry.makeSound("Perry");


        //Lambda expression. Metoda identica (creeaza o clasa anonima, implementeaza metoda si instantiază un obiect)
        //Implementeaza doar o singura metoda din interfata
        IAnimal harry = (lastname) -> {
            System.out.println("Parrot " + lastname + " papagai");
        };
        harry.makeSound("Harry");

        IAnimal berry = (lastname) -> System.out.println("Parrot " + lastname + " papagai");
        berry.makeSound("Berry");


        Multiply dublu = (a) -> {
            return a * 2;
        };
        int rez = dublu.multiply(13);
        System.out.println("Result: " + rez);


        Function<String, Integer> countLetters = word -> {
            return word.length();
        };
        System.out.println("The name 'Ecaterina' has  " + countLetters.apply("Ecaterina") + " letters");

        BiFunction<Man, Woman, Baby> family = (m, w) -> {
            Baby baby = new Baby(m, w);
            baby.setName("Cain");
            return baby;
        };
        Man adam = new Man("Adam");
        Woman eve = new Woman("Eve");
        Baby cain = family.apply(adam, eve);
        System.out.println("Baby " + cain);


        Predicate<Float> willRain = (percentage) -> {
            if (percentage > 70)
                return true;
            return Math.random() > 0.5;
        };
        System.out.println("75% chance to rain? " + willRain.test
                (75.0f));
        System.out.println("15% chance to rain? " + willRain.test
                (15.0f));
        System.out.println("20% chance to rain? " + willRain.test
                (20.0f));


        Consumer<Cake> eatCake = (x) -> {
            System.out.println("Cake " + x + " was eaten");
        };
        Cake diplomatCake = new Cake();
        diplomatCake.setCalories(5000);
        eatCake.accept(diplomatCake);

        Cake carrotCake = new Cake();
        carrotCake.setCalories(2000);
        eatCake.accept(carrotCake);

    }
}
