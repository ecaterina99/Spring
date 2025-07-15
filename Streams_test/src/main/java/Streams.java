
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Streams {
    public static void main(String[] args) throws IOException {
        List<String> names = new ArrayList<>();
        names.add("john Smith");
        names.add("jane green");
        names.add("bob Smith");
        names.add("mary Taller");
        names.add("richard Gir");
        names.add("Jack dow");
        names.add("Jane Green");

        //count & distinct
       /* long noNames = names.stream().count();
        System.out.println("Number of names: " + noNames+  "\nDistinct names: ");
        names.stream().distinct().forEach(System.out::println);
        */

        //uppercase first letter
        Stream<String> namesStream = names.stream();
        Stream<String> namesUniqueStream = namesStream.distinct();
        Stream<String> namesUniqueFirstUpperStream = namesUniqueStream.map(
                (fullName) -> {
                    String[] nameComponents = fullName.split(" ");
                    byte k = 0;
                    for (String component : nameComponents) {
                        String firstUpper = component.substring(0, 1).toUpperCase();
                        String wordRemaining = component.substring(1);
                        String upperName = firstUpper + wordRemaining;
                        nameComponents[k++] = upperName;
                    }
                    return String.join(" ", nameComponents);
                }
        );
        namesUniqueFirstUpperStream.forEach(System.out::println);

        List<String> list = Arrays.asList("Ana", "Ion", "Maria", "Alex");
        System.out.println("Names which start with 'A': ");
        list.stream()
                .filter(name -> name.startsWith("A"))
                .forEach(System.out::println);


        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.stream()
                .map(n -> n * n)
                .forEach(n -> System.out.println(n));
        int sum = numbers.stream()
                .reduce(0, (a, b) -> a + b);

        System.out.println("The sum is:" + sum);
    }
}