package VtorKolokvium;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class ExamStatsProcessor{
    public Map<String, Double> processExamResults(InputStream is) throws IOException{
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        Map<String,Double> result=new TreeMap<>();
        String line;
        while ((line=reader.readLine())!=null){
            String[] parts=line.split(";");
            String id=parts[0];
            try {
                List<Double> points= Arrays.stream(parts[1].split(","))
                        .map(Double::parseDouble)
                        .toList();
                result.put(id,result.getOrDefault(id,0.0)+points.stream().mapToDouble(i->i).average().getAsDouble());
            }
            catch (NumberFormatException e){
                System.err.println("Skipping invalid line: "+line);
            }

        }
        return result;
    }
}
public class ExamStatsProcessorTest {
    public static void main(String[] args) throws IOException {
        String inputData = """
                Student1;85,90,78
                Student2;95,INVALID,82
                Student3;70,88,92,85
                Student4;65,73
                Student5;90
                """;

        InputStream is = new ByteArrayInputStream(inputData.getBytes());
        ExamStatsProcessor processor = new ExamStatsProcessor();
        Map<String, Double> results = processor.processExamResults(is);

        results.forEach((student, avgScore) ->
                System.out.printf("Student: %s, Average: %.2f%n", student, avgScore));
    }
}