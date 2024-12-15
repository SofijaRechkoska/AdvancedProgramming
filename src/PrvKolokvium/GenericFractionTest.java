package PrvKolokvium;

import java.util.Scanner;

class ZeroDenominatorException extends Exception{
    ZeroDenominatorException(){
        super("Denominator cannot be zero");
    }
}
class GenericFraction<T extends Number,U extends Number>{
    private T numerator;
    private U denominator;

    GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {
        this.numerator = numerator;
        if (denominator.doubleValue()==0){
            throw new ZeroDenominatorException();
        }
        this.denominator = denominator;
    }
    public GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        double thisNumerator=this.numerator.doubleValue();
        double thisDenominator=this.denominator.doubleValue();
        double gfNumerator=gf.numerator.doubleValue();
        double gfDenominator=gf.denominator.doubleValue();

        double resultNumerator=thisNumerator*gfDenominator+gfNumerator*thisDenominator;
        double resulDenominator=thisDenominator*gfDenominator;

        return new GenericFraction<>(resultNumerator,resulDenominator);
    }

    public double toDouble() {
        return numerator.doubleValue()/denominator.doubleValue();
    }
    private double gdc(double a,double b){
        while (b!=0){
            double temp=b;
            b=a%b;
            a=temp;
        }
        return a;
    }

    @Override
    public String toString() {
        double gdc=gdc(numerator.doubleValue(),denominator.doubleValue());
        return String.format("%.2f / %.2f",numerator.doubleValue()/gdc,denominator.doubleValue()/gdc);
    }
}
public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}

// вашиот код овде
