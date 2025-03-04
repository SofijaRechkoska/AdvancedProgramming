package VtorKolokvium;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

class StoreRevenueProcessor {
    public Map<String, Double> processRevenue(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Map<String, Double> result = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            String id = parts[0];
            double sum = 0.0;
            String[] prices = parts[1].split(",");
            try {
                for (int i = 0; i < prices.length - 1; i += 2) {
                    sum += Integer.parseInt(prices[i]) * Integer.parseInt(prices[i + 1]);
                }
                result.put(id, sum);
            } catch (NumberFormatException e) {
                System.err.println("Invalid line");
            }
        }
        return result;
    }
}

public class StoreRevenueProcessorTest {
    public static void main(String[] args) throws IOException {
        // Примерен влезен текст
        String inputData = """
                StoreA;10,2,20,3,15,1
                StoreB;5,10,10,5
                StoreC;7,2,8,INVALID
                StoreD;12,4,8,3,10,2
                """;

        // Конверзија на текстот во InputStream
        InputStream is = new ByteArrayInputStream(inputData.getBytes());

        // Инстанцирање на StoreRevenueProcessor
        StoreRevenueProcessor processor = new StoreRevenueProcessor();

        // Обработка на податоците
        Map<String, Double> revenueMap = processor.processRevenue(is);

        // Печатење на резултатите
        revenueMap.forEach((store, revenue) ->
                System.out.printf("Store: %s, Revenue: %.2f%n", store, revenue));
    }
}
