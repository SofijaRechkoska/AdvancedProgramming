package VtorKolokvium;

import java.util.*;
import java.util.stream.Collectors;

class Student {
    private String index;
    private List<Integer> points;

    Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    public String getIndex() {
        return index;
    }

    public List<Integer> getPoints() {
        return points;
    }

    public int totalPoints() {
        return points.stream().mapToInt(Integer::intValue).sum();
    }

    public double averagePoints() {
        if (points.isEmpty()) return 0.0;
        return totalPoints() /10.0;
    }

    public int absences() {
        return 10 - points.size();
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f", index, absences() > 2 ? "NO" : "YES", averagePoints());
    }
}

class LabExercises {
    private Map<String, Student> studentMap;

    LabExercises() {
        studentMap = new HashMap<>();
    }

    public void addStudent(Student student) {
        studentMap.putIfAbsent(student.getIndex(), student);
    }

    public void printByAveragePoints(boolean ascending, int n) {
        if (ascending) {
            studentMap.values().stream()
                    .sorted(Comparator.comparingDouble(Student::averagePoints).thenComparing(Student::getIndex))
                    .limit(n)
                    .forEach(System.out::println);
        } else {
            studentMap.values().stream()
                    .sorted(Comparator.comparingDouble(Student::averagePoints).thenComparing(Student::getIndex).reversed())
                    .limit(n)
                    .forEach(System.out::println);
        }
    }

    public List<Student> failedStudents() {
        return studentMap.values().stream()
                .filter(student -> student.absences() > 2)
                .sorted(Comparator.comparing(Student::getIndex).thenComparingDouble(Student::averagePoints))
                .collect(Collectors.toList());
    }

    public Map<Integer, Double> getStatisticsByYear() {
        return studentMap.values().stream()
                .filter(student -> student.absences() <= 2)
                .collect(Collectors.groupingBy(
                        student -> 20 - Integer.parseInt(student.getIndex().substring(0, 2)),
                        Collectors.averagingDouble(Student::averagePoints)
                ));
    }
}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);
    }
}