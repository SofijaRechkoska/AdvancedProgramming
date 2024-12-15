package LabExercises.Lab5;

import java.util.*;
import java.util.stream.Collectors;

class Student {
    private String id;
    private List<Integer> grades;

    Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public String getId() {
        return id;
    }

    public List<Integer> getGrades() {
        return grades;
    }
    public int passedExams(){
        return grades.size();
    }
    public double averageGrade(){
        return grades.stream()
                .mapToDouble(i->i.doubleValue())
                .average().getAsDouble();
    }


    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", grades=" + grades + '}';
    }


}

class Faculty {
    private final Map<String, Student> students;
    private final Comparator<Student> byAverageGrade=Comparator.comparingDouble(Student::averageGrade)
                    .reversed()
                    .thenComparing(Student::passedExams, Comparator.reverseOrder())
                    .thenComparing(Student::getId,Comparator.reverseOrder());
    private final Comparator<Student> byPassedExams=Comparator.comparing(Student::passedExams).reversed()
                    .thenComparing(Student::averageGrade, Comparator.reverseOrder())
                    .thenComparing(Student::getId, Comparator.reverseOrder());
    Faculty() {
        this.students = new HashMap<>();
    }

    public void addStudent(String id, List<Integer> grades) throws Exception {
        if (students.get(id) == null) {
            students.put(id, new Student(id, grades));
        }
        else {
            throw new Exception(String.format("Student with ID %s already exists", id));
        }
    }

    public void addGrade(String studentId, int grade) {
        students.get(studentId).getGrades().add(grade);
    }


    public Set<Student> getStudentsSortedByAverageGrade() {
        return students.values().stream()
                .sorted(byAverageGrade)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<Student> getStudentsSortedByCoursesPassed() {
        return students.values().stream()
                .sorted(byPassedExams)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}

public class SetsTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] tokens = input.split("\\s+");
            String command = tokens[0];

            switch (command) {
                case "addStudent":
                    String id = tokens[1];
                    List<Integer> grades = new ArrayList<>();
                    for (int i = 2; i < tokens.length; i++) {
                        grades.add(Integer.parseInt(tokens[i]));
                    }
                    try {
                        faculty.addStudent(id, grades);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "addGrade":
                    String studentId = tokens[1];
                    int grade = Integer.parseInt(tokens[2]);
                    faculty.addGrade(studentId, grade);
                    break;

                case "getStudentsSortedByAverageGrade":
                    System.out.println("Sorting students by average grade");
                    Set<Student> sortedByAverage = faculty.getStudentsSortedByAverageGrade();
                    for (Student student : sortedByAverage) {
                        System.out.println(student);
                    }
                    break;

                case "getStudentsSortedByCoursesPassed":
                    System.out.println("Sorting students by courses passed");
                    Set<Student> sortedByCourses = faculty.getStudentsSortedByCoursesPassed();
                    for (Student student : sortedByCourses) {
                        System.out.println(student);
                    }
                    break;

                default:
                    break;
            }
        }

        scanner.close();
    }
}
