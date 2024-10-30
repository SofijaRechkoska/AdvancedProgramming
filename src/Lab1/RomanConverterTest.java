package Lab1;

import java.util.Scanner;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
    private int n;
    static int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    static String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    RomanConverter(int n) {
        this.n = n;
    }

    public static String toRoman(int n) {
        StringBuilder sb=new StringBuilder();
        int number=n;
        int index=0;
        while (number>0){
            if (number>=values[index]){
                sb.append(symbols[index]);
                number-=values[index];
            }else{
                index++;
            }
        }
        return sb.toString();
    }

}
