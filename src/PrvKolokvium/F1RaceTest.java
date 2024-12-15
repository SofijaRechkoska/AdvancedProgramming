package PrvKolokvium;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class F1RaceTest {

    public static void main(String[] args) {
        F1Race1 f1Race = new F1Race1();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class F1Race1 {
    private List<Driver1> drivers;

    F1Race1() {
        this.drivers = new ArrayList<>();
    }
    public void readResults(InputStream inputStream){
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        drivers=br.lines()
                .map(Driver1::create)
                .collect(Collectors.toList());

    }
    public void printSorted(OutputStream os){
        PrintWriter writer=new PrintWriter(os);
        drivers.stream()
                .sorted(Comparator.comparing(Driver1::bestLap)).forEach(writer::println);
        writer.flush();
    }
}
class Driver1{
    private static final DateTimeFormatter LAP_TIME=DateTimeFormatter.ofPattern("mm:ss:SSS");
    private String name;
    private List<String> laps;
    private static int i=0;


    Driver1(String name, List<String> laps) {
        this.name = name;
        this.laps = laps;
    }
    public static Driver1 create(String line){
        String []parts=line.split("\\s+");
        List<String> laps=new ArrayList<>();
        for (int i=1;i<parts.length;i++){
            laps.add(parts[i]);
        }
        return new Driver1(parts[0],laps);
    }

    public String bestLap(){
        return laps.stream()
//                .map(this::convertLap)
                .map(lap->LocalTime.parse(lap,LAP_TIME))
                .min(LocalTime::compareTo)
                .get().toString();
    }
//    private String convertLap(String lap){
//        return lap.replace(".",":");
//    }

    @Override
    public String toString() {
        return String.format("%d.%-10s%10s\n",i++,name,bestLap());
    }
}