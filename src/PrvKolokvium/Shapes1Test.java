package PrvKolokvium;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;


class Canvas {
    private String id;
    private List<Integer> sizes;

    Canvas(String id, List<Integer> sizes) {
        this.id = id;
        this.sizes = sizes;
    }

    public List<Integer> getSizes() {
        return sizes;
    }

    public int sizeOfSizesList() {
        return sizes.size();
    }
    public int squaresPerimeter(){
        return sizes.stream().mapToInt(s->s*4).sum();
    }

    @Override
    public String toString() {
        return String.format("%s %d %d",id,sizeOfSizesList(),squaresPerimeter());
    }
}

class ShapesApplication {
    private List<Canvas> canvasList;

    public ShapesApplication(int i) {
        this.canvasList =new ArrayList<>();
    }

    public int readCanvases(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        while ((line = br.readLine()) != null) {
            List<Integer> sizes = new ArrayList<>();
            String[] parts = line.split("\\s+");
            String id = parts[0];
            for (int i = 1; i < parts.length; i++) {
                sizes.add(Integer.valueOf(parts[i]));
            }
            canvasList.add(new Canvas(id, sizes));
        }
        return canvasList.stream()
                .mapToInt(canvas -> canvas.sizeOfSizesList())
                .sum();
    }
    public void printLargestCanvasTo(OutputStream outputStream){
        PrintWriter writer=new PrintWriter(new OutputStreamWriter(outputStream));
        OptionalInt max=canvasList.stream().mapToInt(canvas -> canvas.squaresPerimeter()).max();
        Canvas maxC=null;
        if (max.isPresent()){
            for (Canvas canvas:canvasList) {
                if (canvas.squaresPerimeter()==max.getAsInt()){
                   maxC=canvas;
                }
            }
        }
        writer.println(maxC);
        writer.flush();
    }
}

public class Shapes1Test {

    public static void main(String[] args) throws IOException {
        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}