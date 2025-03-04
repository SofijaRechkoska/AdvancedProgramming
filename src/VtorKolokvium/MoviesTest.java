package VtorKolokvium;
import java.util.*;
import java.util.stream.Collectors;

class Movie1{
    private String title;
    private int[] ratings;

    Movie1(String title, int[] ratings) {
        this.title = title;
        this.ratings = ratings;
    }
    public double avgRating(){
        double sum=0.0;
        for (int i=0;i<ratings.length;i++){
            sum+=ratings[i];
        }
        return sum/ratings.length;
    }

    public String getTitle() {
        return title;
    }

    public int[] getRatings() {
        return ratings;
    }
    public double ratingCoef(){
        return avgRating()*this.ratings.length;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings",title,avgRating(),ratings.length);
    }
}
class MoviesList{
    private List<Movie1> movies;
    MoviesList(){
        movies=new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        movies.add(new Movie1(title,ratings));
    }

    public List<Movie1> top10ByAvgRating() {
        return movies.stream()
                .sorted(Comparator.comparing(Movie1::avgRating).reversed().thenComparing(Movie1::getTitle))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Movie1> top10ByRatingCoef() {
        int maxRating=movies.stream()
                .mapToInt(m->m.getRatings().length)
                .max().getAsInt();

        return movies.stream()
                .sorted(Comparator.comparing((Movie1 movie)->movie.ratingCoef()/maxRating).reversed().thenComparing(Movie1::getTitle))
                .limit(10)
                .collect(Collectors.toList());
    }
}
public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie1> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie1 movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie1 movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde