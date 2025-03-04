package VtorKolokvium;//package VtorKolokvium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

class QuizProcessor{

    public static Map<String, Double> processAnswers(InputStream in) throws Exception {
        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        Map<String,Double> result=new TreeMap<>();
        String line;
        while ((line=reader.readLine())!=null){
            String[] parts=line.split(";");
            String id=parts[0].trim();
            if (parts[1].length()!=parts[2].length()){
                System.out.println("A quiz must have same number of correct and selected answers");
                continue;
            }
            double points=0.0;
            String[] correctAnswers=parts[1].split(", ");
            String[] studentAnswers=parts[2].split(", ");
            for (int i=0;i<correctAnswers.length;i++){
                if (correctAnswers[i].equals(studentAnswers[i])){
                    points+=1;
                }else{
                    points-=0.25;
                }
            }
            result.put(id,points);
        }
        return result;
    }
}
public class QuizProcessorTest {
    public static void main(String[] args) throws IOException {
        try {
            QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}