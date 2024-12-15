package PrvKolokvium;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Rule<IN,OUT>{
    private Predicate<IN> input;
    private Function<IN,OUT> result;

    Rule(Predicate<IN> input, Function<IN, OUT> result) {
        this.input = input;
        this.result = result;
    }

    public Optional<OUT> apply(IN in){
        if (input.test(in)){
            return Optional.of(result.apply(in));
        }else return Optional.empty();
    }
}

class RuleProcessor{
    public static <IN,OUT>void process(List<IN> inputs,List<Rule<IN,OUT>> rules){
        for (IN input:inputs) {
            System.out.println("Input: "+input.toString());
            for (Rule<IN,OUT> rule:rules) {
                if (rule.apply(input).isPresent()){
                    System.out.println("Result: "+rule.apply(input).get());
                }else System.out.println("Condition not met");
            }
        }
    }
}
class Student1 {
    String id;
    List<Integer> grades;

    public Student1(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public static Student1 create(String line) {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Integer> grades = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
        return new Student1(id, grades);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", grades=" + grades +
                '}';
    }
}

public class RuleTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { //Test for String,Integer
            List<Rule<String, Integer>> rules = new ArrayList<>();

            /*
            TODO: Add a rule where if the string contains the string "NP", the result would be index of the first occurrence of the string "NP"
            * */
            rules.add(new Rule<>(
                    s -> s.contains("NP"),
                    s -> s.indexOf("NP")
            ));


            /*
            TODO: Add a rule where if the string starts with the string "NP", the result would be length of the string
            * */
            rules.add(new Rule<>(
                    s -> s.startsWith("NP"),
                    s -> s.length()
            ));

            List<String> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(sc.nextLine());
            }

            RuleProcessor.process(inputs, rules);


        } else { //Test for Student, Double
            List<Rule<Student1, Double>> rules = new ArrayList<>();

            //TODO Add a rule where if the student has at least 3 grades, the result would be the max grade of the student
            rules.add(new Rule<>(
                    student1 -> student1.grades.size()>=3,
                    student1 -> student1.grades.stream().mapToDouble(i->i).max().getAsDouble()
            ));

            //TODO Add a rule where if the student has an ID that starts with 20, the result would be the average grade of the student
            //If the student doesn't have any grades, the average is 5.0
            rules.add(new Rule<>(
                    student1 -> student1.id.startsWith("20"),
                    student1 -> student1.grades.stream().mapToDouble(i->i).average().orElse(5.0)
            ));

            List<Student1> students = new ArrayList<>();
            while (sc.hasNext()){
                students.add(Student1.create(sc.nextLine()));
            }

            RuleProcessor.process(students, rules);
        }
    }
}


//package PrvKolokvium;
//
//import java.util.*;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;
//
//
//class Rule<IN, OUT> {
//    Predicate<IN> predicate;
//    Function<IN, OUT> function;
//
//    public Rule(Predicate<IN> predicate, Function<IN, OUT> function) {
//        this.predicate = predicate;
//        this.function = function;
//    }
//
//    public Optional<OUT> apply(IN input) {
//        if (predicate.test(input)) {
//            return (Optional.of(function.apply(input)));
//        } else
//            return Optional.empty();
//    }
//}
//
//class RuleProcessor {
//    public static <IN, OUT> void process(List<IN> inputs, List<Rule<IN, OUT>> rules) {
//        for (IN input : inputs) {
//            System.out.println(String.format("Input: %s", input.toString()));
//            for (Rule<IN, OUT> rule : rules) {
//                Optional<OUT> result = rule.apply(input);
//                if (result.isEmpty()) {
//                    System.out.println("Condition not met");
//                } else {
//                    System.out.println("Result: " + result.get());
//                }
//            }
//        }
//    }
//}
//
//class Student1 {
//    String id;
//    List<Integer> grades;
//
//    public Student1(String id, List<Integer> grades) {
//        this.id = id;
//        this.grades = grades;
//    }
//
//    public static Student1 create(String line) {
//        String[] parts = line.split("\\s+");
//        String id = parts[0];
//        List<Integer> grades = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
//        return new Student1(id, grades);
//    }
//
//    @Override
//    public String toString() {
//        return "Student{" +
//                "id='" + id + '\'' +
//                ", grades=" + grades +
//                '}';
//    }
//}
//
//public class RuleTester {
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        int testCase = Integer.parseInt(sc.nextLine());
//
//        if (testCase == 1) { //Test for String,Integer
//            List<Rule<String, Integer>> rules = new ArrayList<>();
//
//            /*
//            TODO: Add a rule where if the string contains the string "NP", the result would be index of the first occurrence of the string "NP"
//            * */
//            rules.add(new Rule<>(
//                    s -> s.contains("NP"),
//                    s -> s.indexOf("NP")
//            ));
//
//
//            /*
//            TODO: Add a rule where if the string starts with the string "NP", the result would be length of the string
//            * */
//            rules.add(new Rule<>(
//                    s -> s.startsWith("NP"),
//                    s -> s.length()
//            ));
//
//
//            List<String> inputs = new ArrayList<>();
//            while (sc.hasNext()) {
//                inputs.add(sc.nextLine());
//            }
//
//            RuleProcessor.process(inputs, rules);
//
//
//        } else { //Test for Student, Double
//            List<Rule<Student1, Double>> rules = new ArrayList<>();
//
//            //TODO Add a rule where if the student has at least 3 grades, the result would be the max grade of the student
//            rules.add(new Rule<>(
//                    student -> student.grades.size() >= 3,
//                    student -> student.grades.stream().mapToDouble(i -> i).max().getAsDouble()
//            ));
//
//            //TODO Add a rule where if the student has an ID that starts with 20, the result would be the average grade of the student
//            //If the student doesn't have any grades, the average is 5.0
//            rules.add(new Rule<>(
//                    student -> student.id.startsWith(String.valueOf(20)),
//                    student -> student.grades.stream().mapToDouble(i -> i).average().orElse(5.0)
//            ));
//
//            List<Student1> students = new ArrayList<>();
//            while (sc.hasNext()) {
//                students.add(Student1.create(sc.nextLine()));
//            }
//
//            RuleProcessor.process(students, rules);
//        }
//    }
//}
