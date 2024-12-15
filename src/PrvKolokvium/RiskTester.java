package PrvKolokvium;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

class Risk {
    public int processAttacksData(InputStream is) {
        Scanner scanner = new Scanner(is);

        int successfulAttacks = 0;

        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine().trim());
        }

        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length != 2)
                continue;

            String[] attacker = parts[0].split("\\s+");
            String[] defender = parts[1].split("\\s+");
            if (attacker.length != 3 || defender.length != 3) continue;

            List<Integer> attackers = Arrays.stream(attacker)
                    .map(Integer::parseInt)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());

            List<Integer> defenders = Arrays.stream(defender)
                    .map(Integer::parseInt)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());


            boolean isSuccessful = true;
            for (int i = 0; i < 3; i++) {
                if (attackers.get(i) <= defenders.get(i)) {
                    isSuccessful = false;
                    break;
                }
            }
            if (isSuccessful) {
                successfulAttacks++;
            }
        }
        return successfulAttacks;
    }
}
public class RiskTester {
    public static void main(String[] args) {

        Risk risk = new Risk();

        System.out.println(risk.processAttacksData(System.in));
    }
}