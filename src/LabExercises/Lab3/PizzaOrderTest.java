package LabExercises.Lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

class InvalidPizzaTypeException extends Exception{
    InvalidPizzaTypeException(){
        super("InvalidPizzaTypeException");
    }
}

class InvalidExtraTypeException extends Exception{
    InvalidExtraTypeException(){
        super("InvalidExtraTypeException");
    }
}
class OrderLockedException extends Exception{
    OrderLockedException(){
        super("OrderLockedException");
    }
}
class ItemOutOfStockException extends Exception{
    ItemOutOfStockException(Item item){
        super(String.format("%d",item));
    }
}
class EmptyOrder extends Exception{
    EmptyOrder(){
        super("EmptyOrder");
    }
}
interface Item{
    int getPrice();
    String getType();
}
class PizzaItem implements Item {
    private int price;
    private String type;


    PizzaItem(String type) throws InvalidPizzaTypeException {
        this.type=type;
        if (type.equals("Standard")){
            this.price=10;
        }else if (type.equals("Pepperoni")){
            this.price=12;
        }else if (type.equals("Vegetarian")){
            this.price=8;
        }else
            throw new InvalidPizzaTypeException();
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaItem pizzaItem = (PizzaItem) o;
        return price == pizzaItem.price && Objects.equals(type, pizzaItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, type);
    }
}
class ExtraItem implements Item{
    private String type;
    private int price;
    ExtraItem(String type) throws InvalidExtraTypeException {
        this.type=type;
        if (type.equals("Coke")){
            this.price=5;
        }else if (type.equals("Ketchup")){
            this.price=3;
        }else
            throw new InvalidExtraTypeException();
    }
    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraItem extraItem = (ExtraItem) o;
        return price == extraItem.price && Objects.equals(type, extraItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, price);
    }
}
class OrderItem{
    private Item item;
    private int count;

    OrderItem(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}

class Order{
    private List<OrderItem> items;
    private boolean locked;
    Order(){
        items=new ArrayList<>();
        locked=false;
    }
    public void addItem(Item item,int count) throws OrderLockedException, ItemOutOfStockException {
        if (locked){
            throw new OrderLockedException();
        }
        boolean found=false;
        if (count>10){
            throw new ItemOutOfStockException(item);
        }
        for (OrderItem i:items) {
            if (i.getItem().equals(item)){
                i.setCount(count);
                found=true;
                break;
            }
        }
        if (!found){
            items.add(new OrderItem(item,count));
        }
    }
    public int getPrice(){
        return items.stream().
                mapToInt(i->i.getItem().getPrice()*i.getCount())
                .sum();
    }
    public void displayOrder(){
        StringBuilder sb=new StringBuilder();
        items.stream().
                forEach(i->sb.append(String.format("%3d.%-15sx%2d%5d$\n",items.indexOf(i)+1,i.getItem().getType(),i.getCount(),i.getItem().getPrice()*i.getCount())));
        sb.append(String.format("%-22s%5d$","Total:",getPrice()));
        System.out.println(sb.toString());
    }

    public void removeItem (int idx) throws OrderLockedException {
        if (locked){
            throw new OrderLockedException();
        }
        if (idx<0 || idx>items.size()){
            throw new ArrayIndexOutOfBoundsException(idx);
        }
        items.remove(idx);

    }
    public void lock() throws EmptyOrder {
        if (items.isEmpty()){
            throw new EmptyOrder();
        }
        this.locked=true;
    }
}


public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}