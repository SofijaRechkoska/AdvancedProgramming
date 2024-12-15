package PrvKolokvium;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Runner {
    private int id;
    private static LocalTime start;
    private LocalTime end;
    private int runtime;

    Runner(int id, LocalTime start, LocalTime end) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.runtime = end.toSecondOfDay() - start.toSecondOfDay();
    }

    public static Runner create(String line) {
        String[] parts = line.split("\\s+");
        return new Runner(Integer.parseInt(parts[0]), LocalTime.parse(parts[1]), LocalTime.parse(parts[2]));
    }

    public LocalTime getRuntime() {
        return LocalTime.ofSecondOfDay(runtime);
    }

    public int getRuntimeSeconds() {
        return runtime;
    }

    @Override
    public String toString() {
        return id + " " + getRuntime();

    }
}

class TeamRace {

    public static void findBestTeam(InputStream is, OutputStream out) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        List<Runner> runners = new ArrayList<>();
        runners = br.lines()
                .map(Runner::create)
                .collect(Collectors.toList());

        runners.sort(Comparator.comparing(Runner::getRuntime));
        runners.stream()
                .limit(4)
                .forEach(i -> System.out.println(i));

        List<Runner> fourBest = runners.stream().limit(4).collect(Collectors.toList());
        int totalTime = 0;
        for (Runner runner : fourBest) {
            totalTime += runner.getRuntimeSeconds();
        }
        System.out.println(LocalTime.ofSecondOfDay(totalTime));
    }
}

public class RaceTest {
    public static void main(String[] args) {
        try {
            TeamRace.findBestTeam(System.in, System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}