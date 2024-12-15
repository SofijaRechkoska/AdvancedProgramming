package PrvKolokvium;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class AmountNotAllowedException extends Exception {
    AmountNotAllowedException(int sum) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", sum));
    }
}

class Item1 {
    private int price;
    private String type;

    Item1(int price, String type) {
        this.price = price;
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public double tax() {
        if (type.equals("A")) {
            return 0.18 * price * 0.15;
        } else if (type.equals("B")) {
            return 0.05 * price * 0.15;
        } else
            return 0;
    }
}

class Receipt {
    private int id;
    private List<Item1> items;

    Receipt(int id, List<Item1> items) {
        this.id = id;
        this.items = items;
    }

    public static Receipt create(String line) {
        String[] parts = line.split("\\s+");
        int id = Integer.parseInt(parts[0]);
        List<Item1> items = new ArrayList<>();
        for (int i = 1; i < parts.length; i += 2) {
            int price = Integer.parseInt(parts[i]);
            String type = parts[i + 1];
            items.add(new Item1(price, type));
        }
        return new Receipt(id, items);
    }

    public int sumOfAmounts() {
        return items.stream()
                .mapToInt(Item1::getPrice)
                .sum();
    }

    public double taxReturn() {
        return items.stream()
                .mapToDouble(Item1::tax)
                .sum();
    }

    public int getId() {
        return id;
    }
}

class MojDDV {
    private List<Receipt> receipts;

    MojDDV() {
        this.receipts = new ArrayList<>();
    }

    public void readRecords(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<Receipt> validReceipts = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            try {
                Receipt receipt = Receipt.create(line);
                if (receipt.sumOfAmounts() > 30000) {
                    throw new AmountNotAllowedException(receipt.sumOfAmounts());
                }
                validReceipts.add(receipt);
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }

        this.receipts = validReceipts;
    }

    void printTaxReturns(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);
        receipts.stream()
                .forEach(i -> writer.println(String.format("%d %d %.2f", i.getId(), i.sumOfAmounts(), i.taxReturn())));
        writer.flush();
    }
}

public class MojDDVTest {

    public static void main(String[] args) throws IOException {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}
