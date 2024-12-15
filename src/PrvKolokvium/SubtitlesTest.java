package PrvKolokvium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


class Subtitle {
    private int id;
    private String start;
    private String end;
    private String text;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");

    Subtitle(int id, String start, String end, String text) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getText() {
        return text;
    }


    public void shifting(int ms) {
        Duration shiftDuration = Duration.ofMillis(ms);

        LocalTime startTime = LocalTime.parse(start, TIME_FORMAT);
        LocalTime endTime = LocalTime.parse(end, TIME_FORMAT);

        startTime = startTime.plus(shiftDuration);
        endTime = endTime.plus(shiftDuration);

        start = startTime.format(TIME_FORMAT);
        end = endTime.format(TIME_FORMAT);
    }

    @Override
    public String toString() {
        return String.format("%d\n%s --> %s\n%s\n", id, start, end, text);
    }
}

class Subtitles {
    List<Subtitle> subtitles;

    Subtitles() {
        subtitles = new ArrayList<>();
    }

    public int loadSubtitles(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            int id = Integer.parseInt(line.trim());
            String[] times = bufferedReader.readLine().split("-->");
            String start = times[0].trim();
            String end = times[1].trim();
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                sb.append(line).append("\n");
            }
            subtitles.add(new Subtitle(id, start, end, sb.toString().trim()));
        }
        return subtitles.size();

    }

    public void print() {
        subtitles.stream().forEach(i -> System.out.println(i));
    }

    public void shift(int ms) {
        for (Subtitle s : subtitles) {
            s.shifting(ms);
        }
    }
}

public class SubtitlesTest {
    public static void main(String[] args) throws IOException {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде
