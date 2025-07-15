import lombok.Builder;

@Builder
public class Car {
    private String make;
    private String model;
    private int capacity;
    private Fuel fuel;
    private int noDoors;
    private int noSeats;

    enum Fuel {
        GASOLINE,
        DIESEL,
        GPL,
        ELECTRIC,
    }
}
