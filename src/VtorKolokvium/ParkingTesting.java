package VtorKolokvium;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class DateUtil {
    public static long durationBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }
}
class Vehicle{
    private String registration;
    private String spot;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    Vehicle(String registration, String spot, LocalDateTime entryTime) {
        this.registration = registration;
        this.spot = spot;
        this.entryTime = entryTime;
    }

    public String getRegistration() {
        return registration;
    }

    public String getSpot() {
        return spot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }
    public long getDuration(){
        return DateUtil.durationBetween(entryTime,exitTime);
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(registration, vehicle.registration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registration);
    }
}
class Parking{
    private int capacity;
    private List<Vehicle> currentVehicles;
    private Map<String,Vehicle> allVehicles;
    private Map<String,Integer> carStatistics;


    Parking(int capacity) {
        this.capacity = capacity;
        currentVehicles=new ArrayList<>();
        allVehicles=new HashMap<>();
        carStatistics=new TreeMap<>();
    }

    public void update(String registration, String spot, LocalDateTime timestamp, boolean entrance) {
        if (entrance){
            Vehicle vehicle=new Vehicle(registration,spot,timestamp);
            allVehicles.putIfAbsent(vehicle.getRegistration(),new Vehicle(registration,spot,timestamp));
            currentVehicles.add(vehicle);
            carStatistics.put(vehicle.getRegistration(),carStatistics.getOrDefault(vehicle.getRegistration(),0)+1);
        }else{
            Vehicle vehicle=allVehicles.get(registration);
            if (vehicle.getExitTime()==null){
                vehicle.setExitTime(timestamp);
                currentVehicles.remove(vehicle);
            }
        }
    }

    public void currentState() {
        double filled=currentVehicles.size()*100.0/capacity;
        System.out.printf("Capacity filled: %.2f%%%n",filled);
        currentVehicles.stream()
                .sorted(Comparator.comparing(Vehicle::getEntryTime).reversed())
                .forEach(v-> System.out.printf("Registration number: %s Spot: %s Start timestamp: %s%n",v.getRegistration(),v.getSpot(),v.getEntryTime()));
    }

    public void history() {
        allVehicles.values().stream()
                .filter(i->i.getExitTime()!=null)
                .sorted(Comparator.comparing(Vehicle::getDuration).reversed())
                .forEach(v-> System.out.printf("Registration number: %s Spot: %s Start timestamp: %s End timestamp: %s Duration in minutes: %d\n",v.getRegistration(),v.getSpot(),v.getEntryTime(),v.getExitTime(),v.getDuration()));
    }

    public Map<String, Integer> carStatistics(){
        return carStatistics;
    }

    public Map<String, Double> spotOccupancy(LocalDateTime start, LocalDateTime end) {
        Map<String, Double> occupancyMap = new HashMap<>();
        long totalIntervalMinutes = DateUtil.durationBetween(start, end);
        List<Vehicle> parkingHistory=allVehicles.values().stream().collect(Collectors.toList());

        for (Vehicle vehicle : parkingHistory) {
            LocalDateTime entryTime = vehicle.getEntryTime();
            LocalDateTime exitTime = vehicle.getExitTime() != null ? vehicle.getExitTime() : end;

            // Only consider times within the interval
            if (exitTime.isBefore(start) || entryTime.isAfter(end)) {
                continue;
            }

            LocalDateTime effectiveStart = entryTime.isBefore(start) ? start : entryTime;
            LocalDateTime effectiveEnd = exitTime.isAfter(end) ? end : exitTime;

            long occupiedMinutes = DateUtil.durationBetween(effectiveStart, effectiveEnd);

            occupancyMap.merge(vehicle.getSpot(), (double) occupiedMinutes, Double::sum);
        }

        // Convert occupied minutes to percentage, ensuring spots with 0% occupation are set to 0.0
        occupancyMap.forEach((spot, minutes) -> {
            double percentage = (minutes / totalIntervalMinutes) * 100;
            occupancyMap.put(spot, percentage == 0.0 ? 0.0 : percentage);
        });

        return occupancyMap;
    }
}
public class ParkingTesting {

    public static <K, V extends Comparable<V>> void printMapSortedByValue(Map<K, V> map) {
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(String.format("%s -> %s", entry.getKey().toString(), entry.getValue().toString())));

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int capacity = Integer.parseInt(sc.nextLine());

        Parking parking = new Parking(capacity);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equals("update")) {
                String registration = parts[1];
                String spot = parts[2];
                LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
                boolean entrance = Boolean.parseBoolean(parts[4]);
                parking.update(registration, spot, timestamp, entrance);
            } else if (parts[0].equals("currentState")) {
                System.out.println("PARKING CURRENT STATE");
                parking.currentState();
            } else if (parts[0].equals("history")) {
                System.out.println("PARKING HISTORY");
                parking.history();
            } else if (parts[0].equals("carStatistics")) {
                System.out.println("CAR STATISTICS");
                printMapSortedByValue(parking.carStatistics());
            } else if (parts[0].equals("spotOccupancy")) {
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                printMapSortedByValue(parking.spotOccupancy(start, end));
            }
        }
    }
}
