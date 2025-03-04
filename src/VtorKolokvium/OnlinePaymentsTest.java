package VtorKolokvium;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

class Item{
    private String studentId;
    private String name;
    private int price;

    Item(String studentId, String name, int price) {
        this.studentId = studentId;
        this.name = name;
        this.price = price;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
class OnlinePayments{
    private Map<String, List<Item>> items;
    OnlinePayments(){
        items=new HashMap<>();
    }

    public void readItems(InputStream in) throws IOException {
        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line=reader.readLine())!=null){
            String[] parts=line.split(";");
            String studentIndex=parts[0];
            String itemName=parts[1];
            int price= Integer.parseInt(parts[2]);
            items.putIfAbsent(studentIndex,new ArrayList<>());
            items.get(studentIndex).add(new Item(studentIndex,itemName,price));
        }
    }

    public void printStudentReport(String id, OutputStream out) throws IOException {
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(out));

        List<Item> studentItems=items.getOrDefault(id,new ArrayList<>());
        if (studentItems==null || studentItems.isEmpty()){
            writer.write(String.format("Student %s not found!\n",id));
            writer.flush();
            return;
        }
        studentItems.sort(Comparator.comparing(Item::getPrice).reversed());

        int neto=studentItems.stream()
                .mapToInt(Item::getPrice).sum();
        double rawCommission=neto*0.0114;
        int commission= (int) Math.round(Math.min(Math.max(rawCommission,3),300));
        int totalAmount=neto+commission;

        writer.write(String.format("Student: %s Net: %d Fee: %d Total: %d\n",id,neto,commission,totalAmount));
        writer.write("Items:\n");
        int counter=1;
        for (Item item:studentItems){
            writer.write(String.format("%d. %s %d\n",counter++,item.getName(),item.getPrice()));
        }
        writer.flush();
    }
}
public class OnlinePaymentsTest {
    public static void main(String[] args) throws IOException {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> {
            try {
                onlinePayments.printStudentReport(id, System.out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}