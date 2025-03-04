package VtorKolokvium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Product{
    private int discountPrice;
    private int price;

    Product(int discountPrice, int price) {
        this.discountPrice = discountPrice;
        this.price = price;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public int getPrice() {
        return price;
    }
    public int getPercent() {
        return (int)Math.floor(100.0 - (100.0 / price * discountPrice));
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d", getPercent(), discountPrice, price);
    }
}
class Store {
    private String name;
    private List<Product> products;

    Store(String name) {
        this.name = name;
        this.products = new ArrayList<>();
    }

    public void addProduct(String discountPrice, String originalPrice) {
        this.products.add(new Product(Integer.parseInt(discountPrice), Integer.parseInt(originalPrice)));
    }

    public String getName() {
        return name;
    }

    public double avgDiscount() {
        return products.stream()
                .mapToInt(Product::getPercent)
                .average().orElse(0.0);
    }

    public int totalDiscount() {
        return products.stream()
                .mapToInt(p->Math.abs(p.getPrice()-p.getDiscountPrice()))
                .sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s%nAverage discount: %.1f%%%nTotal discount: %d%n", name, avgDiscount(), totalDiscount()));
        products.stream()
                .sorted(Comparator.comparing(Product::getPercent).thenComparing(Product::getPrice).reversed())
                .forEach(x -> sb.append(System.lineSeparator()).append(x));
        return sb.toString();
    }
}

class Discounts{
    private List<Store> stores;
    Discounts(){
        stores=new ArrayList<>();
    }
    public int readStores(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(" ");
            if (parts.length < 2) continue;

            String name = parts[0];
            Store store = new Store(name);

            for (int i = 1; i < parts.length; i++) {
                if (parts[i].isEmpty()) continue;

                String[] prices = parts[i].split(":");
                if (prices.length != 2) continue;

                try {
                    store.addProduct(prices[0], prices[1]);
                } catch (NumberFormatException e) {
                }
            }
            stores.add(store);
        }
        return stores.size();
    }

    public List<Store> byAverageDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::avgDiscount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::totalDiscount))
                .limit(3)
                .collect(Collectors.toList());
    }
}
public class DiscountsTest {
    public static void main(String[] args) throws IOException {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

