package PrvKolokvium;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class InvalidCanvasException extends Exception {
    InvalidCanvasException(String id, double maxArea) {
        super(String.format("Canvas %s has a shape with area larger than %.2f.", id, maxArea));
    }
}

abstract class Shape {
    abstract double getArea();
}

class Square extends Shape {
    private double a;

    public Square(double a) {
        this.a = a;
    }


    @Override
    double getArea() {
        return a * a;
    }
}

class Circle extends Shape {
    private double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    double getArea() {
        return radius * radius * Math.PI;
    }
}


class Canvas1 {
    private String id;
    private  List<Shape> shapes;

    Canvas1(String id) {
        this.id = id;
        shapes=new ArrayList<>();
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public double totalArea() {
        return shapes.stream()
                .mapToDouble(Shape::getArea)
                .sum();
    }
    public int totalCircles(){
        return (int) shapes.stream()
                .filter(i->i instanceof Circle)
                .count();
    }
    public int totalSquares(){
        return (int) shapes.stream()
                .filter(i->i instanceof Square)
                .count();
    }
    public double minArea(){
        return shapes.stream()
                .mapToDouble(Shape::getArea)
                .min().getAsDouble();
    }
    public double maxArea(){
        return shapes.stream()
                .mapToDouble(Shape::getArea)
                .max().getAsDouble();
    }
    public double averageArea(){
        return shapes.stream()
                .mapToDouble(Shape::getArea)
                .average().getAsDouble();
    }


    @Override
    public String toString() {
        return String.format("%s %d %d %d %.2f %.2f %.2f",id,shapes.size(),totalCircles(),totalSquares(),minArea(),maxArea(),averageArea());
    }
}

class ShapesApplication1 {
    private double maxArea;
    private List<Canvas1> canvasList;

    ShapesApplication1(double maxArea) {
        this.maxArea = maxArea;
        this.canvasList=new ArrayList<>();
    }

    public void readCanvases(InputStream is) throws IOException, InvalidCanvasException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("\\s+");
            Canvas1 canvas = new Canvas1(parts[0]);
            boolean notAllowed=false;

            for (int i = 1; i < parts.length; i += 2) {
                String type = parts[i];
                double size = Double.parseDouble(parts[i + 1]);
                Shape shape;
                if (type.equals("S")) {
                    shape = new Square(size);
                } else if (type.equals("C")) {
                    shape = new Circle(size);
                } else continue;

                if (shape.getArea() > maxArea) {
                    notAllowed=true;
                    System.out.println(String.format("Canvas %s has a shape with area larger than %.2f", parts[0], maxArea));
                    break;
                }
                canvas.addShape(shape);
            }
            if (!notAllowed){
                canvasList.add(canvas);
            }
        }
    }
    public void printCanvases(OutputStream os){
        PrintWriter writer=new PrintWriter(os);
        canvasList.stream().sorted(Comparator.comparingDouble(Canvas1::totalArea).reversed())
                .forEach(writer::println);
        writer.flush();
    }


}

public class Shapes2Test {

    public static void main(String[] args) throws IOException {

        ShapesApplication1 shapesApplication = new ShapesApplication1(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        try {
            shapesApplication.readCanvases(System.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidCanvasException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}