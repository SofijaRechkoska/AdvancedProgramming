package PrvKolokvium;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class MinMax<T extends Comparable<T>> {
    private T min;
    private T max;
    private int count;
    private List<T>list;

    MinMax() {
        list=new ArrayList<>();
        min = null;
        max = null;
        count = 0;
    }

    public void update(T element) {
        if (list.isEmpty()) {
            min = element;
            max = element;
        } else {
            if (element.compareTo(min) < 0) {
                min = element;
            } else if (element.compareTo(max) > 0) {
                max = element;
            }
        }
        list.add(element);
    }
    public int diffMinMax(){
        return (int) list.stream()
                .filter(i->!i.equals(min) && !i.equals(max))
                .count();
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    @Override
    public String toString() {
        return getMin() + " " + getMax() + " " + diffMinMax() + "\n";
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for (int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}