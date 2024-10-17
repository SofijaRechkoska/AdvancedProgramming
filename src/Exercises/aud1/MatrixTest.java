package Exercises.aud1;

class Matrix {
    public static double sum(double[][] a) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                sum += a[i][j];
            }
        }
        return sum;
    }

    public static double average(double[][] a) {
        return sum(a) / (a.length * a[0].length);
    }

}

public class MatrixTest {
    public static void main(String[] args) {
        double[][] a = {
                {1, 2, 3},
                {4, 5, 6}
        };
        System.out.println(Matrix.sum(a));
        System.out.println(Matrix.average(a));
    }
}
