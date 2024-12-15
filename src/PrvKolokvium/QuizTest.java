//package PrvKolokvium;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class InvalidOperationException1 extends Exception{
    InvalidOperationException1(String msg){
        super(msg);
    }
}
abstract class Question {
    private String text;
    private int points;

    Question(String text, int points) {
        this.text = text;
        this.points = points;
    }

    public String getText() {
        return text;
    }

    public int getPoints() {
        return points;
    }
    public abstract boolean isCorrect (String answer);
}

class TrueFalse extends Question{
    private boolean answer;

    TrueFalse(String text, int points,boolean answer) {
        super(text, points);
        this.answer=answer;
    }
    public static TrueFalse create(String line){
        String[] parts=line.split(";");
        return new TrueFalse(parts[1],Integer.parseInt(parts[2]),Boolean.parseBoolean(parts[3]));
    }

    @Override
    public String toString() {
        return String.format("True/False Question: %s Points: %d Answer: %s",getText(),getPoints(),answer);
    }

    @Override
    public boolean isCorrect(String answer) {
        return this.answer == Boolean.valueOf(answer);
    }
}

class MultipleChoice extends Question{
    private String answer;

    MultipleChoice(String text, int points,String answer) {
        super(text, points);
        this.answer=answer;
    }
    public static MultipleChoice create(String line) throws InvalidOperationException1 {
        String[] parts=line.split(";");
        if (parts[3].matches("[A-E]")){
            return new MultipleChoice(parts[1],Integer.parseInt(parts[2]),parts[3]);
        }else
            throw new InvalidOperationException1(String.format("%s is not allowed option for this question",parts[3]));

    }

    @Override
    public String toString() {
        return String.format("Multiple Choice Question: %s Points %d Answer: %s",getText(),getPoints(),answer);
    }

    @Override
    public boolean isCorrect(String answer) {
        return this.answer.equals(answer);
    }
}


class Quiz{
    private List<Question> questions;
    Quiz() {
        this.questions =new ArrayList<>();
    }

    public void addQuestion(String data) throws InvalidOperationException1 {
        String[] lines=data.split("\n");
        for (int i=0;i<lines.length;i++){
            String[] parts=lines[i].split(";");
            if (parts[0].equals("TF")){
                questions.add(TrueFalse.create(lines[i]));
            }else if (parts[0].equals("MC")){
                questions.add(MultipleChoice.create(lines[i]));

            }
        }

    }

    public void answerQuiz(List<String> answers, PrintStream out) throws InvalidOperationException1 {
        PrintWriter writer=new PrintWriter(out);
        if (answers.size()!=questions.size()){
            throw new InvalidOperationException1("Answers and questions must be of same length!");
        }
        double totalPoints=0;
        double points=0;
        for (int i=0;i<questions.size();i++){
            Question question=questions.get(i);
            String answer=answers.get(i);
            if (question.isCorrect(answer)){
                points=question.getPoints();
            }else{
                if (question instanceof MultipleChoice){
                    points = - (question.getPoints() * 0.2);
                }else{
                    points=0;
                }
            }
            totalPoints+=points;
            writer.println(String.format("%d. %.2f",i+1,points));
        }
        writer.println(String.format("Total points: %.2f",totalPoints));
        writer.flush();
    }

    public void printQuiz(PrintStream out) {
        PrintWriter writer=new PrintWriter(out);
        questions.stream()
                .sorted(Comparator.comparingInt(Question::getPoints).reversed())
                .forEach(i->writer.println(i));
        writer.flush();
    }
}
public class QuizTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i=0;i<questions;i++) {
            try {
                quiz.addQuestion(sc.nextLine());
            } catch (InvalidOperationException1 e) {
                System.out.println(e.getMessage());
            }
        }

        List<String> answers = new ArrayList<>();

        int answersCount =  Integer.parseInt(sc.nextLine());

        for (int i=0;i<answersCount;i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase==1) {
            quiz.printQuiz(System.out);
        } else if (testCase==2) {
            try {
                quiz.answerQuiz(answers, System.out);
            } catch (InvalidOperationException1 e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}
