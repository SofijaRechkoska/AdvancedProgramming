package PrvKolokvium;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class F1Race {
    private List<Driver> drivers;

    F1Race() {
        this.drivers = new ArrayList<>();
    }

    public void readResults(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        drivers = br.lines()
                .map(Driver::create)
                .collect(Collectors.toList());

    }

    public void printSorted(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);
        drivers.stream()
                .sorted(Comparator.comparingLong(Driver::bestLapTime))
                .forEach(i -> writer.println(i));
        writer.flush();
        ;
    }
}

class Driver {
    private String name;
    private List<String> time;
    private static int i = 1;


    Driver(String name, List<String> time) {
        this.name = name;
        this.time = time;
    }

    public static Driver create(String line) {
        String[] parts = line.split("\\s+");
        List<String> times = new ArrayList<>();
        String name = parts[0];
        times.add(parts[1]);
        times.add(parts[2]);
        times.add(parts[3]);
        return new Driver(name, times);
    }

    @Override
    public String toString() {
        return String.format("%d. %-10s%10s", i++, name, timeFormatted(bestLapTime()));
    }

    public String timeFormatted(long l) {
        long minutes = l / ( 60 * 1000);
        long seconds = (l % (60 * 1000)) / 1000;
        long millis = l % 1000;
        return String.format("%d:%02d:%03d", minutes, seconds, millis);

    }

    public long milliSeconds(String time) {
        String[] parts = time.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        int milliseconds = Integer.parseInt(parts[2]);
        return Duration.ofMinutes(minutes).plusSeconds(seconds).plusMillis(milliseconds).toMillis();
    }

    public long bestLapTime() {
        return time.stream()
                .mapToLong(this::milliSeconds)
                .min()
                .orElse(0);
    }
}
