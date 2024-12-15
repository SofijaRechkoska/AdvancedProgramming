package LabExercises.Lab2;

import java.util.*;
import java.util.stream.Collectors;

class Account {
    private String user;
    private double balance;
    private long id;


    Account(String user, double balance) {
        this.user = user;
        this.balance = balance;
        this.id = new Random().nextLong();
    }

    public String getUser() {
        return user;
    }

    public double getBalance() {
        return balance;
    }

    public long getId() {
        return id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nBalance: %.2f$\n", user, balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Double.compare(balance, account.balance) == 0 && id == account.id && Objects.equals(user, account.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, balance, id);
    }
}

abstract class Transaction {
    private  long fromId;
    private  long toId;
    private  String description;
    private  double amount;

    Transaction(long fromId, long toId,String description, double amount) {
        this.description=description;
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }
}

class FlatAmountProvisionTransaction extends Transaction {
    private double flatProvision;

    public FlatAmountProvisionTransaction(long fromId, long toId,double amount,double flatProvision) {
        super(fromId, toId,"FlatAmount", amount);
        this.flatProvision = flatProvision;
    }

    public double getFlatProvision() {
        return flatProvision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatAmountProvisionTransaction that = (FlatAmountProvisionTransaction) o;
        return Double.compare(flatProvision, that.flatProvision) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(flatProvision);
    }
}

class FlatPercentProvisionTransaction extends Transaction {
    private int centsPerDollar;

    public FlatPercentProvisionTransaction(long fromId, long toId, double amount,int centsPerDolar) {
        super(fromId, toId,"FlatPercent", amount);
        this.centsPerDollar = centsPerDolar;
    }

    public double getCentsPerDollar() {
        return centsPerDollar;
    }
    public double calculateProvision(){
        return getAmount() * centsPerDollar / 100.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatPercentProvisionTransaction that = (FlatPercentProvisionTransaction) o;
        return centsPerDollar == that.centsPerDollar;
    }

    @Override
    public int hashCode() {
        return Objects.hash(centsPerDollar);
    }
}

class Bank {
    private String name;
    private Account[] accounts;
    private int numAccounts;
    private double totalTransfers;
    private double totalProvision;
    Bank(String name, Account[] accounts) {
        this.name = name;
        this.accounts = new Account[accounts.length];
        this.numAccounts=accounts.length;
        System.arraycopy(accounts, 0, this.accounts, 0, numAccounts);
    }

//    public boolean makeTransaction(Transaction t) {
//        Account fromAccount = null;
//        Account toAccount = null;
//
//        for(int i=0;i<accounts.length;i++){
//            if (accounts[i].getId()==t.getFromId()){
//                fromAccount=accounts[i];
//            }else if (accounts[i].getId()==t.getToId()){
//                toAccount=accounts[i];
//            }
//        }
//        if(fromAccount==null || toAccount==null){
//            return false;
//        }
//
//        double totalAmount=0;
//
//        if (t instanceof FlatAmountProvisionTransaction){
//            totalAmount+=((FlatAmountProvisionTransaction) t).getFlatProvision();
//        }else if (t instanceof FlatPercentProvisionTransaction){
//            totalAmount+= ((FlatPercentProvisionTransaction) t).getCentsPerDolar()*t.getAmount()/100.0;
//        }
//
//        if (fromAccount.getBalance()>=(t.getAmount()+totalAmount)){
//            fromAccount.setBalance(fromAccount.getBalance()-(t.getAmount()+totalAmount));
//            toAccount.setBalance(toAccount.getBalance()+t.getAmount());
//            totalTransfers+=t.getAmount();
//            totalProvision+=totalAmount;
//            return true;
//        }else
//            return false;
//
//    }
//public boolean makeTransaction(Transaction t) {
//    Account fromAccount = null;
//    Account toAccount = null;
//        for(int i=0;i<numAccounts;i++){
//            if (accounts[i].getId()==t.getFromId()){
//                fromAccount=accounts[i];
//            }else if (accounts[i].getId()==t.getToId()){
//                toAccount=accounts[i];
//            }
//        }
//
//    if (fromAccount == null || toAccount == null || fromAccount.getBalance() < t.getAmount()) {
//        return false;
//    }
//
//    double provision = 0;
//    if (t instanceof FlatAmountProvisionTransaction) {
//        provision = ((FlatAmountProvisionTransaction) t).getFlatProvision();
//    } else if (t instanceof FlatPercentProvisionTransaction) {
//        provision = ((FlatPercentProvisionTransaction) t).calculateProvision();
//    }
//
//    double totalAmount = t.getAmount() + provision;
//    fromAccount.setBalance(fromAccount.getBalance() - totalAmount);
//    toAccount.setBalance(toAccount.getBalance() + t.getAmount());
//
//    totalTransfers += t.getAmount();
//    totalProvision += provision;
//    return true;
//}
private Account findAccountById(long id) {
    for (int i = 0; i < numAccounts; i++) {
        if (accounts[i].getId() == id) {
            return accounts[i];
        }
    }
    return null;
}

    public boolean makeTransaction(Transaction t) {
        Account fromAccount = findAccountById(t.getFromId());
        Account toAccount = findAccountById(t.getToId());

        if (fromAccount == null || toAccount == null || fromAccount.getBalance() < t.getAmount()) {
            return false;
        }

        double provision = 0;
        if (t instanceof FlatAmountProvisionTransaction) {
            provision = ((FlatAmountProvisionTransaction) t).getFlatProvision();
        } else if (t instanceof FlatPercentProvisionTransaction) {
            provision = ((FlatPercentProvisionTransaction) t).calculateProvision();
        }

        double totalAmount = t.getAmount() + provision;
        fromAccount.setBalance(fromAccount.getBalance() - totalAmount);
        toAccount.setBalance(toAccount.getBalance() + t.getAmount());

        totalTransfers += t.getAmount();
        totalProvision += provision;
        return true;
    }

    public double totalTransfers() {
        return totalTransfers;
    }

    public double totalProvision() {
        return totalProvision;
    }

    public Account[] getAccounts() {
        return accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Double.compare(totalTransfers, bank.totalTransfers) == 0 && Double.compare(totalProvision, bank.totalProvision) == 0 && Objects.equals(name, bank.name) && Arrays.equals(accounts, bank.accounts);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, totalTransfers, totalProvision);
        result = 31 * result + Arrays.hashCode(accounts);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("Name: "+name+"\n\n");
        for (int i=0;i<accounts.length;i++){
            stringBuilder.append(accounts[i].toString());
        }

        return stringBuilder.toString();
    }
}


public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static double parseAmount(String amount) {
        return Double.parseDouble(amount.replace("$", ""));
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", 20.0);
        Account a2 = new Account("Andrej", 20.0);
        Account a3 = new Account("Andrej", 30.0);
        Account a4 = new Account("Gajduk", 20.0);
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1) && !a1.equals(a2) && !a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, 20.0, 10.0);
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, 20.0, 10.0);
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, 20.0, 10.0);
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, 50.0, 50.0);
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, 20.0, 10.0);
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, 20.0, 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, 20.0, 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, 20.0, 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, 50.0, 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, 20.0, 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, 20.0, 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, 3.0, 3.0);
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), parseAmount(jin.nextLine()));
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    double amount = parseAmount(jin.nextLine());
                    double parameter = parseAmount(jin.nextLine());
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + String.format("%.2f$", t.getAmount()));
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + String.format("%.2f$", bank.totalProvision()));
                    System.out.println("Total transfers: " + String.format("%.2f$", bank.totalTransfers()));
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, double amount, double o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, (int) o);
        }
        return null;
    }


}

