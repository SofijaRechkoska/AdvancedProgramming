package VtorKolokvium;

import java.util.Scanner;

import java.util.*;
class Elections {
    private Map<Integer,Map<String,Integer>> votesDistrict;
    private Map<String,Integer> votesCandidate;
    Elections(){
        votesCandidate=new TreeMap<>();
        votesDistrict=new TreeMap<>();
    }

    public void addVotes(int districtId, String candidateName, int votes) {
        votesDistrict.putIfAbsent(districtId,new TreeMap<>());
        votesDistrict.get(districtId).put(candidateName,votesDistrict.get(districtId).getOrDefault(candidateName,0)+votes);
        votesCandidate.put(candidateName,votesCandidate.getOrDefault(candidateName,0)+votes);
    }

    public void printVotesByDistrict(int districtId) {
        System.out.println("District: "+districtId);
        votesDistrict.get(districtId).entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(s-> System.out.printf("Candidate: %s Votes: %d%n",s.getKey(),s.getValue()));
    }

    public void printTotalVotesByCandidate() {
        votesCandidate.entrySet().stream()
                .forEach(i->{
                    System.out.printf("Candidate %s Total votes %d%n",i.getKey(),i.getValue());
                });
    }
}




public class ElectionsTest {
    public static void main(String[] args) {
        Elections elections = new Elections();
        Scanner scanner = new Scanner(System.in);

        // Примерен инпут вметнат во кодот
        String[] inputLines = {
                "1:Hristijan Mickovski:10000",
                "1:Dimitar Kovacevski:8000",
                "2:Hristijan Mickovski:12000",
                "2:Dimitar Kovacevski:7500",
                "3:Hristijan Mickovski:9500",
                "3:Dimitar Kovacevski:9000",
                "1",
                "2",
                "3"
        };

        // Обработка на влезните податоци
        for (String line : inputLines) {
            if (line.contains(":")) { // Линија со податоци за гласови
                String[] parts = line.split(":");
                int districtId = Integer.parseInt(parts[0]);
                String candidateName = parts[1];
                int votes = Integer.parseInt(parts[2]);
                elections.addVotes(districtId, candidateName, votes);
            } else { // Линија со изборна единица за печатење
                elections.printVotesByDistrict(Integer.parseInt(line));
            }
        }

        // Печатење на вкупните гласови по кандидат
        elections.printTotalVotesByCandidate();
    }
}
