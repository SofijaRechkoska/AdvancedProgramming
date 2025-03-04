package VtorKolokvium;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

class CostCalculator{
    CostCalculator(){}

    public static Map<String,Double> calculateTotalCost(InputStream input) throws IOException {
        BufferedReader reader=new BufferedReader(new InputStreamReader(input));
        Map<String,Double> result=new HashMap<>();
        String line;
        while ((line=reader.readLine())!=null){
            double totalPrice=0.0;
            String[] parts=line.split(";");
            String id=parts[0];
            String[] prices=parts[1].split(",");
            String[] quantity=parts[2].split(",");
            if (prices.length!=quantity.length){
                continue;
            }
            for (int i=0;i<prices.length;i++){
                totalPrice+=Double.parseDouble(prices[i])*Integer.parseInt(quantity[i]);
            }
            result.put(id,totalPrice);
        }
        return result;
    }
}
public class TotalCostCalculatorTest {
    public static void main(String[] args) throws IOException {
        String inputData = "101;120.5,250.75;2,3\n" +
                "102;99.9,199.99,50;1,2,5\n" +
                "103;150,200,300;1,1,2\n";

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        Map<String, Double> results = CostCalculator.calculateTotalCost(inputStream);

        for (Map.Entry<String, Double> entry : results.entrySet()) {
            System.out.printf("Total cost for ID %s: %.2f%n", entry.getKey(), entry.getValue());
        }
    }
}