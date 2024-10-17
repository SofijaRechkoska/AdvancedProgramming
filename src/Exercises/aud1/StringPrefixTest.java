package Exercises.aud1;

import java.util.stream.IntStream;

class StringPrefix {
    public static boolean isPrefix(String str1, String str2) {
        if (str1.length() > str2.length()) {
            return false;
        }
        for (int i = 0; i < str1.length(); i++) {
            if (str1.toLowerCase().charAt(i) != str2.toLowerCase().charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPrefixShort(String str1, String str2) {
        return str2.toLowerCase().startsWith(str1.toLowerCase());
    }

    public static boolean isPrefixStream(String str1, String str2) {
        if (str1.length() > str2.length()) {
            return false;
        }
        return IntStream.range(0, str1.length()).allMatch(i -> str1.toLowerCase().charAt(i) == str2.toLowerCase().charAt(i));
    }
}

public class StringPrefixTest {
    public static void main(String[] args) {
        System.out.println(StringPrefix.isPrefix("NaPrEdNo", "Napredno programiranje"));
        System.out.println(StringPrefix.isPrefixShort("NaPrEdNo", "Napredno programiranje"));
        System.out.println(StringPrefix.isPrefixStream("NaPrEdNo", "Naperedno programiranje"));

    }
}
