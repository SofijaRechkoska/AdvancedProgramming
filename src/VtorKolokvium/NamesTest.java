package VtorKolokvium;

import java.util.*;
import java.util.stream.Collectors;

class Names{
    private Map<String,Integer> names;
    Names(){
        names=new TreeMap<>();
    }

    public void addName(String name) {
        names.put(name,names.getOrDefault(name,0)+1);
    }

    public void printN(int n) {
        names.entrySet().stream()
                .filter(name->name.getValue()>=n)
                .forEach(name->{
                    Set<Character> chars=name.getKey().chars()
                            .mapToObj(c->(char)Character.toLowerCase(c))
                            .collect(Collectors.toCollection(HashSet::new));
                    System.out.printf("%s (%d) %d\n",name.getKey(),name.getValue(),chars.size());
                });
    }

    public String findName(int len, int index) {
        List<String> filtered=names.keySet().stream()
                .filter(name->name.length()<len)
                .collect(Collectors.toList());

        int count=0;
        int targetIndex=index % filtered.size();
        for (String name:filtered){
            if (count==targetIndex){
                return name;
            }
            count=(count+1) % filtered.size();
        }
        return "Not found";
    }
}
public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde