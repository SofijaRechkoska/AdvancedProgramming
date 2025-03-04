package VtorKolokvium;

import java.util.*;

class Team{
    private String id;
    private String name;

    Team(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
class Player{
    private String id;
    private String name;

    Player(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
class SportsTeamManager {
    private Map<String,Team> teams;
    private Map<String,Player> players;
    private Map<String, List<Player>> teamPlayers;
    private Map<String,Map<String,Integer>> matchResults;
    private Map<String,Integer> teamWins;
    private Map<String,Integer> playerScore;
    SportsTeamManager(){
        teams=new HashMap<>();
        players=new HashMap<>();
        teamPlayers=new HashMap<>();
        matchResults=new HashMap<>();
        teamWins=new HashMap<>();
        playerScore=new HashMap<>();
    }

    public void addTeam(String id, String name) {
        teams.put(id,new Team(id,name));
        teamPlayers.putIfAbsent(id,new ArrayList<>());
    }

    public void addPlayer(String id, String name, String teamId) {
        players.put(id,new Player(id,name));
        if (teamPlayers.containsKey(teamId)){
            teamPlayers.get(teamId).add(new Player(id,name));
        }
        playerScore.put(id,playerScore.getOrDefault(id,0)+1);
    }

    public void recordMatch(String team1, String team2, int score1, int score2) {
        if (!teams.containsKey(team1) || !teams.containsKey(team2)){
            return;
        }
        matchResults.putIfAbsent(team1,new HashMap<>());
        matchResults.get(team1).put(team2,score1);

        matchResults.putIfAbsent(team2,new HashMap<>());
        matchResults.get(team2).put(team1,score2);

        if (score1>score2){
            teamWins.put(team1,teamWins.getOrDefault(team1,0)+1);
        }else if (score2>score1){
            teamWins.put(team2,teamWins.getOrDefault(team2,0)+1);
        }
    }

    public void topNTeams(int n) {
        teamWins.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(n)
                .forEach(System.out::println);
    }

    public void starPlayersForTeams(List<String> teamIds) {
        for (String team:teamIds){
            if (teams.containsKey(team)){
                String bestPlayer=teamPlayers.get(team).stream()
                        .max(Comparator.comparing(p->playerScore.getOrDefault(p.getId(),0)))
                        .get().getId();
                if (bestPlayer!=null){
                    teamPlayers.get(team).stream()
                            .filter(i->i.getId().equals(bestPlayer))
                            .forEach(System.out::println);
                }
            }
        }
    }
}
public class SportsTeamManagersTest {
    public static void main(String[] args) {
        SportsTeamManager manager = new SportsTeamManager();
        manager.addTeam("T1", "Lions");
        manager.addTeam("T2", "Tigers");
        manager.addPlayer("P1", "John", "T1");
        manager.addPlayer("P2", "Mike", "T1");
        manager.addPlayer("P3", "David", "T2");
        manager.addPlayer("P4", "Chris", "T2");
        manager.recordMatch("T1", "T2", 3, 2);
        manager.recordMatch("T1", "T2", 1, 4);
        System.out.println("Top Teams:");
        manager.topNTeams(2);
        System.out.println("Star Players:");
        manager.starPlayersForTeams(Arrays.asList("T1", "T2"));
//        System.out.println("Similar Teams to T1:");
//        manager.similarTeams("T1");
    }
}

