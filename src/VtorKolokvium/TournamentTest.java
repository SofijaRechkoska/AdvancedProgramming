package VtorKolokvium;

import java.util.*;
import java.util.stream.Collectors;

class Team1{
    private String name;
    private int position;
    private int points;

    Team1(String name, int position, int points) {
        this.name = name;
        this.position = position;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return String.format("%s - Position: %d, Points: %d\n",name,position,points);
    }
}
class Tournament{
    private Map<String, List<Team1>> teams;
    Tournament(){
        teams=new HashMap<>();
    }

    public void addTeam(String city, String teamName, int position, int points) {
        teams.putIfAbsent(city,new ArrayList<>());
        teams.get(city).add(new Team1(teamName,position,points));
    }


    public void listCityTeams(String city) {
        teams.get(city).stream()
                .sorted(Comparator.comparing(Team1::getPosition).thenComparing(Team1::getPoints))
                .forEach(System.out::println);
    }

    public void getTopTeamsByCity(String city) {
        teams.get(city).stream()
                .sorted(Comparator.comparing(Team1::getPoints).reversed().thenComparing(Team1::getPosition))
                .limit(3)
                .forEach(System.out::println);
    }

    public void getTopCityByPoints() {
        Map<String,Integer> cityPoints=new HashMap<>();
        teams.forEach((city,teams)->{
            int totalPoints=teams.stream().mapToInt(Team1::getPoints).sum();
            cityPoints.put(city,totalPoints);
        });
        int maxPoints=cityPoints.values().stream().mapToInt(i->i).max().getAsInt();
        cityPoints.entrySet().stream()
                .filter(entry->entry.getValue()==maxPoints)
                .sorted(Map.Entry.comparingByKey())
                .forEach(t-> System.out.println(t.getKey()+" - Total Points: "+t.getValue()));

    }
}
public class TournamentTest {
    public static void main(String[] args) {
        Tournament tournament = new Tournament();

        tournament.addTeam("Skopje", "Team A", 1, 15);
        tournament.addTeam("Skopje", "Team B", 2, 12);
        tournament.addTeam("Skopje", "Team C", 3, 20);
        tournament.addTeam("Bitola", "Team D", 2, 15);
        tournament.addTeam("Bitola", "Team E", 1, 10);
        tournament.addTeam("Skopje", "Team D", 1, 25);

        System.out.println("--- Teams in Skopje ---");
        tournament.listCityTeams("Skopje");

        System.out.println("--- Top 3 Teams in Skopje ---");
        tournament.getTopTeamsByCity("Skopje");

        System.out.println("--- Top City by Points ---");
        tournament.getTopCityByPoints();
    }
}
