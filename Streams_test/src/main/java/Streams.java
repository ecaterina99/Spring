
import java.io.IOException;
import java.util.ArrayList;
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
        Stream <String> namesStream = names.stream();
        Stream <String> namesUniqueStream = namesStream.distinct();
        Stream <String> namesUniqueFirstUpperStream = namesUniqueStream.map(
                (fullName)->{
                    String[] nameComponents = fullName.split(" ");
                    byte k=0;
                    for (String component : nameComponents) {
                        String firstUpper = component.substring(0,1).toUpperCase();
                        String wordRemaining = component.substring(1);
                        String upperName = firstUpper+wordRemaining;
                        nameComponents[k++]=upperName;
                    }
                    return String.join(" ",nameComponents);
                }
        );
        namesUniqueFirstUpperStream.forEach(System.out::println);

    }
}
