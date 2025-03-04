package VtorKolokvium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

class OrderProcessor{
    OrderProcessor(){}

    public Map<String, Double> processOrders(InputStream is) throws IOException {
        Map<String,Double> result=new HashMap<>();
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line=reader.readLine())!=null){
            String[] parts=line.split(";");
            if (parts.length!=3){
                continue;
            }
            String id=parts[0].trim();
            String[] prices=parts[1].split(",");
            String[] quantities= parts[2].split(",");
            if (prices.length!=quantities.length){
                continue;
            }
            double sum=0.0;
            for (int i=0;i<prices.length;i++){
                sum+=Double.parseDouble(prices[i].trim())*Integer.parseInt(quantities[i].trim());
            }
            result.put(id,sum);
        }
        return result;
    }
}
public class OrderProcessorTest {
    public static void main(String[] args) throws IOException {
        String input = "101; 100.5, 200.0, 50.75; 2, 1, 4\n" +
                "102; 300.0, 150.0; 1, 2\n" +
                "103; 50.0, 30.0; 3\n"; // Овој ред ќе се игнорира

        InputStream is = new java.io.ByteArrayInputStream(input.getBytes());
        OrderProcessor processor = new OrderProcessor();
        Map<String, Double> results = processor.processOrders(is);

        results.forEach((id, total) -> System.out.println(id + " -> " + total));
    }
}