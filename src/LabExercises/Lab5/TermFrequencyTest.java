package LabExercises.Lab5;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class TermFrequency {
    private final Map<String, Integer> words;
    private int count;

    public TermFrequency(InputStream inputStream, String[] stopWords) {
        Scanner scanner = new Scanner(inputStream);
        this.words = new HashMap<>();
        this.count = 0;

        while (scanner.hasNext()) {
            String word = scanner.next();
            word = word.toLowerCase().replaceAll("[,.-]", "").trim();

            if (!Arrays.asList(stopWords).contains(word) && !word.isEmpty()) {
                words.computeIfPresent(word, (k, v) -> ++v);
                words.putIfAbsent(word, 1);
                count++;
            }
        }
    }

    public int countTotal() {
//        return words.values().stream().mapToInt(Integer::intValue).sum();
        return count;
    }

    public int countDistinct() {
        return words.size();
    }

    public List<String> mostOften(int k) {
        return words.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry<String, Integer>::getValue).reversed()
                        .thenComparing(Map.Entry::getKey))
                .map(Map.Entry::getKey)
                .limit(k)
                .collect(Collectors.toList());
    }

}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[]{"во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја"};
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde
