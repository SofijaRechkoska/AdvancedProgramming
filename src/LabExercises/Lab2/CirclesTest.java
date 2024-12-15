package LabExercises.Lab2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ObjectCanNotBeMovedException extends Exception {
    public ObjectCanNotBeMovedException(String message) {
        super(message);
    }
}
class MovableObjectNotFittableException extends Exception {
    public MovableObjectNotFittableException(String message) {
        super(message);
    }
}

interface Movable{
    void moveUp() throws ObjectCanNotBeMovedException;
    void moveDown() throws ObjectCanNotBeMovedException;
    void moveRight() throws ObjectCanNotBeMovedException;
    void moveLeft() throws ObjectCanNotBeMovedException;
    int getCurrentXPosition();
    int getCurrentYPosition();
    TYPE getType();
}
enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}
class MovablePoint implements Movable{
    private int x;
    private int y;
    private final int xSpeed;
    private final int ySpeed;

    MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    private void tryMoving(int newX, int newY) throws ObjectCanNotBeMovedException {
        if (newX > MovablesCollection.xMax || newX < 0 || newY > MovablesCollection.yMax || newY < 0) {
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds", newX, newY));
        }
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        int newY = y + ySpeed;
        tryMoving(x, newY);
        y = newY;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        int newY = y - ySpeed;
        tryMoving(x, newY);
        y = newY;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        int newX = x + xSpeed;
        tryMoving(newX, y);
        x = newX;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        int newX = x - xSpeed;
        tryMoving(newX, y);
        x = newX;
    }



    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public TYPE getType() {
        return TYPE.POINT;
    }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%d,%d)", this.x, this.y);    }
}

class MovableCircle implements Movable{
    private final int radius;
    private final MovablePoint center;

    MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        center.moveUp();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        center.moveDown();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        center.moveRight();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        center.moveLeft();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public TYPE getType() {
        return TYPE.CIRCLE;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return String.format("Movable circle with center coordinates (%d,%d) and radius %d",center.getCurrentXPosition(),center.getCurrentYPosition(),radius);
    }
}

class MovablesCollection{
    private final List<Movable> movables;
    public static int xMax;
    public static int yMax;

    MovablesCollection(int xMax, int yMax) {
        this.xMax = xMax;
        this.yMax = yMax;
        this.movables=new ArrayList<>();

    }


    public static void setxMax(int i) {
        xMax = i;
    }

    public static void setyMax(int i) {
        yMax = i;
    }

private boolean canFit(Movable m) {
    int x = m.getCurrentXPosition();
    int y = m.getCurrentYPosition();
    int r = 0;

    if (m.getType() == TYPE.CIRCLE)
        r = ((MovableCircle) m).getRadius();

    return x - r >= 0 && x + r <= MovablesCollection.xMax && y - r >= 0 && y + r <= MovablesCollection.yMax;
}

    public void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if (!canFit(m)) {
            if (m.getType() == TYPE.POINT) {
                throw new MovableObjectNotFittableException(String.format("Point (%d,%d) is out of bounds",m.getCurrentXPosition(),m.getCurrentYPosition()));
            } else {
                MovableCircle circle=(MovableCircle) m;
                throw new MovableObjectNotFittableException(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection",m.getCurrentXPosition(),m.getCurrentYPosition(),circle.getRadius()));
            }
        }
        movables.add(m);
    }


void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction){
    for (Movable m:movables) {
        if (((type == TYPE.POINT) && m instanceof MovablePoint) || (type == TYPE.CIRCLE) && m instanceof MovableCircle){
            try{
                if(direction == DIRECTION.UP){
                    m.moveUp();
                }else if(direction == DIRECTION.DOWN){
                    m.moveDown();
                }else if(direction == DIRECTION.LEFT){
                    m.moveLeft();
                }else if(direction == DIRECTION.RIGHT){
                    m.moveRight();
                }
            }catch (ObjectCanNotBeMovedException e){
                System.out.println(e.getMessage());
            }

        }
    }

    }


    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("Collection of movable objects with size " +movables.size()+":\n");
        for (Movable m:movables) {
            sb.append(m.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
public class CirclesTest {

    public static void main(String[] args) throws ObjectCanNotBeMovedException, MovableObjectNotFittableException {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            } else { //circle
                try {
                    int radius = Integer.parseInt(parts[5]);
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());
    }
}

