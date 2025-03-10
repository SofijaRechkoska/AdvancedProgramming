package VtorKolokvium;

import java.util.*;
import java.util.stream.Collectors;

class Car{
    private String manufacturer;
    private String model;
    private int price;
    private float power;

    Car(String manufacturer, String model, int price, float power) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
        this.power = power;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public int getPrice() {
        return price;
    }

    public float getPower() {
        return power;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%dKW) %d",manufacturer,model,(int)power,price);
    }
}
class CarCollection{
    private List<Car> cars;
    private Map<String,List<Car>> carsByManufacturers;
    CarCollection(){
        cars=new ArrayList<>();
        carsByManufacturers=new TreeMap<>();
    }
    public void addCar(Car car) {
        cars.add(car);
        carsByManufacturers.putIfAbsent(car.getManufacturer().toLowerCase(),new ArrayList<>());
        carsByManufacturers.get(car.getManufacturer().toLowerCase()).add(car);
    }

    public void sortByPrice(boolean asc) {
        if (asc){
            this.cars=cars.stream()
                    .sorted(Comparator.comparing(Car::getPrice).thenComparing(Car::getPower))
                    .collect(Collectors.toList());
        }else{
            this.cars=cars.stream()
                    .sorted(Comparator.comparing(Car::getPrice).thenComparing(Car::getPower).reversed())
                    .collect(Collectors.toList());
        }
    }

    public List<Car> filterByManufacturer(String manufacturer) {
        return carsByManufacturers.get(manufacturer.toLowerCase()).stream()
                .sorted(Comparator.comparing(Car::getModel))
                .collect(Collectors.toList());
    }

    public List<Car> getList() {
        return new ArrayList<>(cars); // враќа копија за да избегне модификација
    }
}
public class CarTest {
    public static void main(String[] args) {
        CarCollection carCollection = new CarCollection();
        String manufacturer = fillCollection(carCollection);
        carCollection.sortByPrice(true);
        System.out.println("=== Sorted By Price ASC ===");
        print(carCollection.getList());
        carCollection.sortByPrice(false);
        System.out.println("=== Sorted By Price DESC ===");
        print(carCollection.getList());
        System.out.printf("=== Filtered By Manufacturer: %s ===\n", manufacturer);
        List<Car> result = carCollection.filterByManufacturer(manufacturer);
        print(result);
    }

    static void print(List<Car> cars) {
        for (Car c : cars) {
            System.out.println(c);
        }
    }

    static String fillCollection(CarCollection cc) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            if(parts.length < 4) return parts[0];
            Car car = new Car(parts[0], parts[1], Integer.parseInt(parts[2]),
                    Float.parseFloat(parts[3]));
            cc.addCar(car);
        }
        scanner.close();
        return "";
    }
}


// vashiot kod ovde