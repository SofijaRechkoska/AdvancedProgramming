package PrvKolokvium;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class LineProcessor{
    private List<String> lines;

    LineProcessor() {
        this.lines = new ArrayList<>();
    }
    public int countOccurrences(String line,char c){
        int counter=0;
        for (int i=0;i<line.length();i++){
            if (line.toLowerCase().charAt(i)==c){
                counter++;
            }
        }
        return counter;
    }

    public void readLines(InputStream in, PrintStream out, char a) {
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        Comparator<String> comparator=(o1,o2)->(int) (countOccurrences(o1,a) - countOccurrences(o2,a));
        lines=br.lines().collect(Collectors.toList());
        String result= lines.stream()
                .max(comparator.thenComparing(Comparator.naturalOrder())).orElse(null);
        PrintWriter writer=new PrintWriter(out);
        writer.println(result);
        writer.flush();
    }
}
public class LineProcessorTest {
    public static void main(String[] args) {
        LineProcessor lineProcessor = new LineProcessor();

        lineProcessor.readLines(System.in, System.out, 'a');
    }

}