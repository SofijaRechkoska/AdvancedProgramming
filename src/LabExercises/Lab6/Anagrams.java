package LabExercises.Lab6;

import java.io.InputStream;
import java.util.*;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        Scanner scanner=new Scanner(inputStream);
        List<String> words=new ArrayList<>();

        while (scanner.hasNextLine()){
            String word=scanner.nextLine().trim();
            words.add(word);
        }

        Map<String,List<String>> anagram=new HashMap<>();

        for (String word:words) {
            char[] arr=word.toCharArray();
            Arrays.sort(arr);
            String sortedWord=new String(arr);

            anagram.computeIfAbsent(sortedWord,k->new ArrayList<>()).add(word);
        }

        List<List<String>> result=new ArrayList<>();
        for (Map.Entry<String,List<String>> entry:anagram.entrySet()){
            List<String> group=entry.getValue();
            if (group.size()>=5){
                Collections.sort(group);
                result.add(group);
            }
        }

        result.sort(Comparator.comparing(group->group.get(0)));

        for (List<String> group:result){
            System.out.println(String.join(" ",group));
        }
    }
}
