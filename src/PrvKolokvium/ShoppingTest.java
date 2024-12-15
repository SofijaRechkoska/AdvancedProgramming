package PrvKolokvium;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class InvalidOperationException extends Exception{
    InvalidOperationException(String message){
        super(message);
    }
}
abstract class Product{
    protected String id;
    protected String name;
    protected double price;
    protected double quantity;

    Product(String id, String name, double price, double quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    public double totalPrice(){
        return price*quantity;
    }
    public abstract void applyDiscount();

    public String getId() {
        return id;
    }

}

class WholeSaleItem extends Product{

    WholeSaleItem(String id, String name, double price, double quantity) {
        super(id, name, price, quantity);
    }

    @Override
    public void applyDiscount() {
        price=price*0.9;
    }

}

class PerishableItem extends Product{

    PerishableItem(String id, String name, double price, double quantity) {
        super(id, name, price, quantity/1000.0);
    }

    @Override
    public void applyDiscount() {
        price=price*0.9;
    }
}


class ShoppingCart{
    private List<Product> products;

    ShoppingCart() {
        this.products = new ArrayList<>();
    }

    public void addItem(String s) throws InvalidOperationException1 {
        String[] parts=s.split(";");
        String id=parts[1];
        String name=parts[2];
        double price= Double.parseDouble(parts[3]);
        double quantity= Double.parseDouble(parts[4]);

        if (parts[0].equals("WS")){
            if (quantity==0){
                throw new InvalidOperationException1(String.format("The quantity of the product with id %s can not be 0.",id));
            }
            products.add(new WholeSaleItem(id,name,price,quantity));
        }else if (parts[0].equals("PS")){
            if (quantity==0){
                throw new InvalidOperationException1(String.format("The quantity of the product with id %s can not be 0.",id));
            }
            products.add(new PerishableItem(id,name,price,quantity));
        }
    }

    public void printShoppingCart(OutputStream os){
        PrintWriter writer=new PrintWriter(os);
        products.stream()
                .sorted(Comparator.comparingDouble(Product::totalPrice).reversed())
                .forEach(i-> writer.println(String.format("%s - %.2f",i.getId(),i.totalPrice())));
        writer.flush();
    }

    public void blackFridayOffer(List<Integer> discountItems, PrintStream out) throws InvalidOperationException1 {
        if (discountItems.isEmpty()){
            throw new InvalidOperationException1("There are no products with discount.");
        }
        PrintWriter writer=new PrintWriter(out);
        products.stream()
                        .filter(item->discountItems.contains(Integer.parseInt(item.getId())))
                        .forEach(item-> {
                            double oldPrice=item.totalPrice();
                            item.applyDiscount();
                            double newPrice=item.totalPrice();
                            writer.println(String.format("%s - %.2f", item.getId(), oldPrice-newPrice));
                        });
        writer.flush();
    }
}
public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException1 e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException1 e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}