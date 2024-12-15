package LabExercises.Lab3;

import java.util.*;
import java.util.stream.Collectors;

class IntegerList {
    private List<Integer> numbers;

    IntegerList() {
        this.numbers = new ArrayList<>();
    }

    IntegerList(Integer... numbers) {
        this.numbers = new ArrayList<>(Arrays.asList(numbers));
    }

    public int size() {
        return numbers.size();
    }

    public void add(int el, int idx) {
        if (idx > size()) {
            while (idx > size()) {
                numbers.add(0);
            }
        }
        numbers.add(idx, el);
    }

    public int remove(int idx) {
        if (idx < 0 || idx > size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return numbers.remove(idx);
    }

    public void set(int el, int idx) {
        if (idx < 0 || idx > size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        numbers.set(idx, el);
    }

    public int get(int idx) {
        if (idx < 0 || idx > size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return numbers.get(idx);
    }

    public int count(int el) {
        return (int) numbers.stream()
                .limit(size())
                .filter(i -> i == el)
                .count();
    }

    public void removeDuplicates() {
        List<Integer> uniqueList = new ArrayList<>();

        for (int i = size() - 1; i >= 0; i--) {
            int currentElement = numbers.get(i);
            if (!uniqueList.contains(currentElement)) {
                uniqueList.add(currentElement);
            }
        }
        Collections.reverse(uniqueList);
        numbers = uniqueList;
    }

    public int sumFirst(int k) {
        return numbers.stream()
                .limit(k)
                .mapToInt(i -> i)
                .sum();
    }

    public int sumLast(int k) {
        return numbers.stream()
                .skip(Math.max(0, size() - k))
                .mapToInt(i -> i)
                .sum();
    }

    public void shiftRight(int idx, int k) {
        int newIdx = (idx+k) % numbers.size();
        if (newIdx < 0 ){
            newIdx += numbers.size();
        }
        int el = numbers.remove(idx);
        numbers.add(newIdx, el);
    }

    public void shiftLeft(int idx, int k) {
        int newIdx = (idx-k) % numbers.size();
        if(newIdx < 0){
            newIdx+=numbers.size();
        }
        int el = numbers.remove(idx);
        numbers.add(newIdx, el);
    }


    public IntegerList addValue(int value) {
        IntegerList newList = new IntegerList();

        for (int i = 0; i < numbers.size(); i++) {
            newList.add(numbers.get(i) + value, i);
        }

        return newList;
    }


}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test standard methods
            int subtest = jin.nextInt();
            if (subtest == 0) {
                IntegerList list = new IntegerList();
                while (true) {
                    int num = jin.nextInt();
                    if (num == 0) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if (num == 1) {
                        list.remove(jin.nextInt());
                    }
                    if (num == 2) {
                        print(list);
                    }
                    if (num == 3) {
                        break;
                    }
                }
            }
            if (subtest == 1) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for (int i = 0; i < n; ++i) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if (k == 1) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if (num == 1) {
                    list.removeDuplicates();
                }
                if (num == 2) {
                    print(list.addValue(jin.nextInt()));
                }
                if (num == 3) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
        if (k == 2) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if (num == 1) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if (num == 2) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if (num == 3) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if (il.size() == 0) System.out.print("EMPTY");
        for (int i = 0; i < il.size(); ++i) {
            if (i > 0) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}