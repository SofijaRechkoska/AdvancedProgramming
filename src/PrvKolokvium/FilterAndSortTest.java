package PrvKolokvium;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class EmptyResultException extends Exception{
    EmptyResultException(){
        super("No element met the criteria");
    }
}
class FilterAndSort {
    public static <T extends Comparable<T>> List<T> execute(List<T> list, Predicate<T> predicate) throws EmptyResultException {
        List<T> result=new ArrayList<>();
        for (T input:list) {
            if (predicate.test(input)) {
                result.add(input);
            }
        }
        if (result.isEmpty()){
            throw new EmptyResultException();
        }
        result=result.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return result;
    }
}

class Student implements Comparable<Student1> {
    String id;
    List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public double average() {
        return grades.stream().mapToDouble(i -> i).average().getAsDouble();
    }

    public int getYear() {
        return (24 - Integer.parseInt(id.substring(0, 2)));
    }

    public int totalCourses() {
        return Math.min(getYear() * 10, 40);
    }

    public double labAssistantPoints() {
        return average() * ((double) grades.size() / totalCourses()) * (0.8 + ((getYear() - 1) * 0.2) / 3.0);
    }

    @Override
    public int compareTo(Student1 o) {
        return Comparator.comparing(Student1::labAssistantPoints)
                .thenComparing(Student1::average)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("Student %s (%d year) - %d/%d passed exam, average grade %.2f.\nLab assistant points: %.2f", id, getYear(), grades.size(), totalCourses(), average(), labAssistantPoints());
    }
}


public class FilterAndSortTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());
        int n = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { // students
            int studentScenario = Integer.parseInt(sc.nextLine());
            List<Student1> students = new ArrayList<>();
            while (n > 0) {

                String line = sc.nextLine();
                String[] parts = line.split("\\s+");
                String id = parts[0];
                List<Integer> grades = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
                students.add(new Student1(id, grades));
                --n;
            }

            if (studentScenario == 1) {
                //TODO filter and sort all students who have at least 8.0 points and are at least 3rd year student
                try {
                    List<Student1> students1= FilterAndSort.execute(students, student -> student.labAssistantPoints()>=8.0 && student.getYear()>=3);
                    students1.stream().forEach(i-> System.out.println(i));
                } catch (EmptyResultException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                //TODO filter and sort all students who have passed at least 90% of their total courses with an average grade of at least 9.0
                try {
                    List<Student1> students1=FilterAndSort.execute(students, student ->student.grades.size()>0.9*student.totalCourses() && student.average()>=9.0 );
                    students1.stream().forEach(i-> System.out.println(i));
                } catch (EmptyResultException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else { //integers
            List<Integer> integers = new ArrayList<>();
            while (n > 0) {
                integers.add(Integer.parseInt(sc.nextLine()));
                --n;
            }

            //TODO filter and sort all even numbers divisible with 15
            try {
                List<Integer> integers1=FilterAndSort.execute(integers,integer -> integer%15==0);
                integers1.stream().forEach(i-> System.out.println(i));
            } catch (EmptyResultException e) {
                System.out.println(e.getMessage());
            }
        }

    }
}

//
//import java.util.*;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;
//
//class EmptyResultException extends Exception {
//    EmptyResultException() {
//        super("No element met the criteria");
//    }
//}
//
//class Student implements Comparable<Student> {
//    String id;
//    List<Integer> grades;
//
//    public Student(String id, List<Integer> grades) {
//        this.id = id;
//        this.grades = grades;
//    }
//
//    public double average() {
//        return grades.stream().mapToDouble(i -> i).average().getAsDouble();
//    }
//
//    public int getYear() {
//        return (24 - Integer.parseInt(id.substring(0, 2)));
//    }
//
//    public int totalCourses() {
//        return Math.min(getYear() * 10, 40);
//    }
//
//    public double labAssistantPoints() {
//        return average() * ((double) grades.size() / totalCourses()) * (0.8 + ((getYear() - 1) * 0.2) / 3.0);
//    }
//
//    @Override
//    public int compareTo(Student o) {
//        return Comparator.comparing(Student::labAssistantPoints)
//                .thenComparing(Student::average)
//                .compare(this, o);
//    }
//
//    @Override
//    public String toString() {
//        return String.format("Student %s (%d year) - %d/%d passed exam, average grade %.2f.\nLab assistant points: %.2f", id, getYear(), grades.size(), totalCourses(), average(), labAssistantPoints());
//    }
//}
//
//class FilterAndSort {
//    public static <T extends Comparable<T>> List<T> execute(List<T> list, Predicate<T> predicate) throws EmptyResultException {
//        List<T> result = list.stream()
//                .filter(predicate)
//                .sorted(Comparator.reverseOrder())
//                .collect(Collectors.toList());
//
//        if (result.isEmpty()) {
//            throw new EmptyResultException();
//        }
//        return result;
//    }
//}
//
//public class FilterAndSortTest {
//    public static void main(String[] args)  {
//        Scanner sc = new Scanner(System.in);
//        int testCase = Integer.parseInt(sc.nextLine());
//        int n = Integer.parseInt(sc.nextLine());
//
//        try{
//            if (testCase == 1) { // students
//                int studentScenario = Integer.parseInt(sc.nextLine());
//                List<Student> students = new ArrayList<>();
//                while (n > 0) {
//
//                    String line = sc.nextLine();
//                    String[] parts = line.split("\\s+");
//                    String id = parts[0];
//                    List<Integer> grades = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
//                    students.add(new Student(id, grades));
//                    --n;
//                }
//
//                if (studentScenario == 1) {
//                    //TODO filter and sort all students who have at least 8.0 points and are at least 3rd year student
//                    List<Student> students1 = FilterAndSort.execute(students, student -> student.labAssistantPoints() >= 8.0 && student.getYear() >= 3);
//                    students1.stream().forEach(i -> System.out.println(i));
//                } else {
//                    //TODO filter and sort all students who have passed at least 90% of their total courses with an average grade of at least 9.0
//                    List<Student> students1 = FilterAndSort.execute(students, student -> student.grades.size() >0.9* student.totalCourses() && student.average() >= 9.0);
//                    students1.stream().forEach(i -> System.out.println(i));
//
//                }
//            } else { //integers
//                List<Integer> integers = new ArrayList<>();
//                while (n > 0) {
//                    integers.add(Integer.parseInt(sc.nextLine()));
//                    --n;
//                }
//
//                //TODO filter and sort all even numbers divisible with 15
//                List<Integer> list = FilterAndSort.execute(integers, integer -> integer % 2 == 0 && integer % 15 == 0);
//                list.stream().forEach(integer -> System.out.println(integer));
//            }
//
//        }
//        catch (EmptyResultException e){
//            System.out.println(e.getMessage());
//        }
//    }
//}
