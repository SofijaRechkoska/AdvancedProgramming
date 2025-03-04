package VtorKolokvium;

import java.util.*;
import java.util.stream.Collectors;


class DuplicateNumberException extends RuntimeException {
    public DuplicateNumberException(String message) {
        super(message);
    }
}
class Contact{
    private String name;
    private String number;

    Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format("%s %s",name,number);
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
class PhoneBook{
    private Map<String,Contact> contacts;
    PhoneBook(){
        contacts=new HashMap<>();
    }

    public void addContact(String name, String number) {
        if (contacts.containsKey(number)){
            throw new DuplicateNumberException("Duplicate number: " + number);
        }
        contacts.putIfAbsent(number,new Contact(name,number));
    }

    public void contactsByNumber(String number) {
        List<Contact> matched= contacts.values().stream()
                .filter(p->p.getNumber().contains(number))
                .sorted(Comparator.comparing(Contact::getName).thenComparing(Contact::getNumber))
                .collect(Collectors.toList());
        if (matched.isEmpty()){
            System.out.println("NOT FOUND");
        }else {
            matched.forEach(System.out::println);
        }
    }

    public void contactsByName(String name) {
        List<Contact> matched=contacts.values().stream()
                .filter(p->p.getName().equals(name))
                .sorted(Comparator.comparing(Contact::getName).thenComparing(Contact::getNumber))
                .collect(Collectors.toList());

        if (matched.isEmpty()){
            System.out.println("NOT FOUND");
        }else {
            matched.forEach(System.out::println);
        }

    }
}
public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

// Вашиот код овде

