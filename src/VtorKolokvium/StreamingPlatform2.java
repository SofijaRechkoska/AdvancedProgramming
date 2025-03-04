package VtorKolokvium;

import java.util.*;
import java.util.stream.Collectors;

class CosineSimilarityCalculator {

    public static double cosineSimilarity(Map<String, Integer> c1, Map<String, Integer> c2) {
        return cosineSimilarity(c1.values(), c2.values());
    }

    public static double cosineSimilarity(Collection<Integer> c1, Collection<Integer> c2) {
        int[] array1;
        int[] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1 = 0, down2 = 0;

        for (int i = 0; i < c1.size(); i++) {
            up += (array1[i] * array2[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down1 += (array1[i] * array1[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down2 += (array2[i] * array2[i]);
        }

        return up / (Math.sqrt(down1) * Math.sqrt(down2));
    }
}

 class Movie{
    private String id;
    private String name;

     Movie(String id, String name) {
         this.id = id;
         this.name = name;
     }

     public String getId() {
         return id;
     }

     public String getName() {
         return name;
     }
 }
 class User{
    private String id;
    private String name;

     User(String id, String name) {
         this.id = id;
         this.name = name;
     }

     public String getId() {
         return id;
     }

     public String getName() {
         return name;
     }
 }

class StreamingPlatform {
    private Map<String, Movie> movies;
    private Map<String, User> users;
    private Map<String, Map<String, List<Integer>>> ratings;

    StreamingPlatform() {
        movies = new HashMap<>();
        users = new HashMap<>();
        ratings = new HashMap<>();

    }

    public void addMovie(String id, String name) {
        movies.put(id, new Movie(id, name));
    }

    public void addUser(String id, String name) {
        users.put(id, new User(id, name));
    }

    public void addRating(String userId, String movieId, int rating) {
        ratings.putIfAbsent(userId, new HashMap<>());
        ratings.get(userId).putIfAbsent(movieId, new ArrayList<>());
        ratings.get(userId).get(movieId).add(rating);
    }

    public Map<String, Double> getAvgRating() {
        Map<String, List<Integer>> ratingPerMovie = ratings.values().stream()
                .flatMap(entry -> entry.entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.flatMapping(i -> i.getValue().stream(), Collectors.toList())
                ));
        return ratingPerMovie.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().mapToDouble(Double::valueOf).average().orElse(0.0)
                ));
    }

    public void topNMovies(int n) {
        getAvgRating().entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(n)
                .forEach(m -> {
                    Movie movie = movies.get(m.getKey());
                    String title = movie.getName();
                    System.out.printf("Movie ID: %s Title: %s Rating: %.2f%n", m.getKey(), title, m.getValue());

                });
    }

    public void favouriteMoviesForUsers(List<String> users1) {
        for (String user : users1) {
            if (ratings.containsKey(user)) {
                System.out.printf("User ID: %s Name: %s%n", user, users.get(user).getName());
                Map<String, List<Integer>> moviesRatings = ratings.get(user);

                Map<String, Double> avgRatings = moviesRatings.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().stream().mapToDouble(Double::valueOf).average().orElse(0.0)
                        ));

                double maxRating = avgRatings.values().stream()
                        .mapToDouble(Double::doubleValue).max().orElse(0.0);

                List<String> moviesIds = avgRatings.entrySet().stream()
                        .filter(i -> i.getValue() == maxRating)
                        .map(Map.Entry::getKey).collect(Collectors.toList());

                Map<String, Double> avgRatingPerMovie = getAvgRating();
                Map<String, Double> favMovies = new HashMap<>();
                for (String m : moviesIds) {
                    avgRatingPerMovie.entrySet().stream()
                            .filter(i -> i.getKey().equals(m))
                            .forEach(i -> favMovies.put(i.getKey(), i.getValue()));
                }

                favMovies.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .forEach(m -> {
                            String movieId = m.getKey();
                            Movie movie1 = movies.get(m.getKey());
                            String title = movie1.getName();
                            System.out.printf("Movie ID: %s Title: %s Rating: %.2f%n", movieId, title, m.getValue());
                        });
            }
            System.out.println();
        }
    }


    public void similarUsers(String userId) {
    }
}

public class StreamingPlatform2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StreamingPlatform sp = new StreamingPlatform();

        while (sc.hasNextLine()){
            String line = sc.nextLine();
            String [] parts = line.split("\\s+");

            if (parts[0].equals("addMovie")) {
                String id = parts[1];
                String name = Arrays.stream(parts).skip(2).collect(Collectors.joining(" "));
                sp.addMovie(id ,name);
            } else if (parts[0].equals("addUser")){
                String id = parts[1];
                String name = parts[2];
                sp.addUser(id ,name);
            } else if (parts[0].equals("addRating")){
                //String userId, String movieId, int rating
                String userId = parts[1];
                String movieId = parts[2];
                int rating = Integer.parseInt(parts[3]);
                sp.addRating(userId, movieId, rating);
            } else if (parts[0].equals("topNMovies")){
                int n = Integer.parseInt(parts[1]);
                System.out.println("TOP " + n + " MOVIES:");
                sp.topNMovies(n);
            } else if (parts[0].equals("favouriteMoviesForUsers")) {
                List<String> users = Arrays.stream(parts).skip(1).collect(Collectors.toList());
                System.out.println("FAVOURITE MOVIES FOR USERS WITH IDS: " + users.stream().collect(Collectors.joining(", ")));
                sp.favouriteMoviesForUsers(users);
            } else if (parts[0].equals("similarUsers")) {
                String userId = parts[1];
                System.out.println("SIMILAR USERS TO USER WITH ID: " + userId);
                sp.similarUsers(userId);
            }
        }
    }
}
