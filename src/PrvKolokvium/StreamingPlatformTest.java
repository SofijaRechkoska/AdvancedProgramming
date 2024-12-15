package PrvKolokvium;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

abstract class Item {
    protected String name;
    protected List<String> genres;

    Item(String name, List<String> genres) {
        this.name = name;
        this.genres = genres;
    }

    public abstract double rating();

    public String getName() {
        return name;
    }

    public List<String> getGenres() {
        return genres;
    }
}

class Movies extends Item {
    private List<Double> ratings;

    Movies(String name, List<String> genres, List<Double> ratings) {
        super(name, genres);
        this.ratings = ratings;
    }

    public static Movies create(String line) {
        String[] parts = line.split(";");
        String name = parts[0];
        List<String> genres = List.of(parts[1].split(","));
        List<Double> ratings = Arrays.stream(parts[2].split("\\s+"))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
        return new Movies(name, genres, ratings);
    }

    @Override
    public double rating() {
        double average = ratings.stream()
                .mapToDouble(i -> i)
                .average().getAsDouble();
        return average * Math.min(ratings.size() / 20.0, 1.0);
    }

    @Override
    public String toString() {
        return String.format("Movie %s %.4f", name, rating());
    }
}

class Series extends Item {
    private List<String> episodes;
    private List<List<Integer>> ratings;

    Series(String name, List<String> genres, List<String> episodes, List<List<Integer>> ratings) {
        super(name, genres);
        this.episodes = episodes;
        this.ratings = ratings;

    }


    public static Series create(String line) {
        String[] parts = line.split(";");
        String name = parts[0];
        List<String> genres = List.of(parts[1].split(","));
        List<String> episodes = new ArrayList<>();
        List<List<Integer>> ratings = new ArrayList<>(); // List of lists to store ratings for each episode

        // Iterate through the parts that contain episode ratings
        for (int i = 2; i < parts.length; i++) {
            String[] innerParts = parts[i].split("\\s+");
            episodes.add(innerParts[0]); // Episode name (e.g., S1E1, S1E2)

            // Convert ratings for each episode to a list of integers
            List<Integer> episodeRatings = Arrays.stream(innerParts, 1, innerParts.length)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            ratings.add(episodeRatings); // Add the list of ratings for this episode
        }

        return new Series(name, genres, episodes, ratings);

    }

    @Override
    public double rating() {
        // List to hold the average ratings of each episode
        List<Double> episodeAvgRatings = new ArrayList<>();

        // Iterate over episodes and their corresponding ratings
        for (int i = 0; i < episodes.size(); i++) {
            List<Integer> episodeRatings = ratings.get(i);  // Get ratings for this episode

            // Calculate average rating for the episode
            double averageRating = episodeRatings.stream()
                    .mapToInt(Integer::intValue)  // Convert to int
                    .average()                    // Calculate the average
                    .orElse(0.0);                 // If no ratings, return 0.0

            episodeAvgRatings.add(averageRating);  // Add the average rating to the list
        }

        // Sort the episode average ratings in descending order
        episodeAvgRatings.sort(Comparator.reverseOrder());

        // Get the average of the top 3 episodes
        return episodeAvgRatings.stream()
                .limit(3)  // Take the top 3 episodes
                .mapToDouble(Double::doubleValue)  // Convert to primitive double
                .average()  // Calculate the average of the top 3
                .orElse(0.0);  // If no episodes, return 0.0
    }

    @Override
    public String toString() {
        return String.format("TV Show %s %.4f (%d episodes)", name, rating(), episodes.size());
    }
}

class StreamingPlatform {
    private List<Item> items;

    StreamingPlatform() {
        this.items = new ArrayList<>();
    }

    public void addItem(String data) {
        String[] lines = data.split("\n");
        for (String line : lines) {
            if (line.split(";").length == 3) {
                items.add(Movies.create(line));
            } else
                items.add(Series.create(line));
        }
    }

    public void listAllItems(PrintStream out) {
        PrintWriter writer = new PrintWriter(out);
        items.stream()
                .sorted(Comparator.comparingDouble(Item::rating).reversed())
                .forEach(i -> writer.println(i));
        writer.flush();
    }

    public void listFromGenre(String genre, PrintStream out) {
        PrintWriter writer = new PrintWriter(out);
//        for (Item item : items) {
//            if (item.genres.contains(genre)) {
//                writer.println(item);
//            }
//        }
        items.stream()
                        .filter(i->i.getGenres().contains(genre))
                                .sorted(Comparator.comparingDouble(Item::rating).reversed())
                                        .forEach(i->writer.println(i));
        writer.flush();
    }
}

public class StreamingPlatformTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StreamingPlatform sp = new StreamingPlatform();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");
            String method = parts[0];
            String data = Arrays.stream(parts).skip(1).collect(Collectors.joining(" "));
            if (method.equals("addItem")) {
                sp.addItem(data);
            } else if (method.equals("listAllItems")) {
                sp.listAllItems(System.out);
            } else if (method.equals("listFromGenre")) {
                System.out.println(data);
                sp.listFromGenre(data, System.out);
            }
        }

    }
}

