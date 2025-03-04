package VtorKolokvium;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

class WrongDateException extends Exception {
    public WrongDateException(Date msg) {
        super("Wrong date: " + msg);
    }
}


class Event {
    String name;
    String location;
    Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy HH:mm");
        return String.format("%s at %s, %s", dateFormat.format(date), location, name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

class EventCalendar {
    int year;
    Map<Date, List<Event>> events;

    public EventCalendar(int year) {
        this.year = year;
        this.events = new HashMap<>();
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) != year) {
            throw new WrongDateException(date);
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date day = cal.getTime();

        events.putIfAbsent(day, new ArrayList<>());
        events.get(day).add(new Event(name, location, date));
    }

    public void listEvents(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date day = cal.getTime();

        List<Event> eventList = events.get(day);
        if (eventList != null && !eventList.isEmpty()) {
            eventList.stream()
                    .sorted(Comparator.comparing(Event::getDate).thenComparing(Event::getName))
                    .forEach(System.out::println);
        } else {
            System.out.println("No events on this day!");
        }
    }

    public void listByMonth() {
        int[] monthCounts = new int[12];

        events.values().stream()
                .flatMap(List::stream)
                .forEach(event -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(event.getDate());
                    int month = cal.get(Calendar.MONTH);
                    monthCounts[month]++;
                });

        for (int i = 0; i < monthCounts.length; i++) {
            System.out.printf("%d : %d%n", i + 1, monthCounts[i]);
        }
    }
}



public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde
