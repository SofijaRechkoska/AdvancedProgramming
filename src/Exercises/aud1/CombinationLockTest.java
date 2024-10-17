package Exercises.aud1;

class CombinationLock {
    private int password;
    private boolean isOpen;

    CombinationLock(int password) {
        this.password = password;
        this.isOpen = false;
    }

    @Override
    public String toString() {
        return "CombinationLock{" +
                "password=" + password +
                ", isOpen=" + isOpen +
                '}';
    }

    public boolean enterPassword(int password) {
        if (this.password == password) {
            isOpen = !isOpen;
            return true;
        }
        return false;
    }

    public boolean changePassword(int oldPass, int newPass) {
        if (isOpen) {
            if (this.password == oldPass) {
                this.password = newPass;
                return true;
            }
        }
        return false;

    }
}

public class CombinationLockTest {
    public static void main(String[] args) {
        CombinationLock combinationLock = new CombinationLock(111);
        System.out.println(combinationLock);
        System.out.println(combinationLock.enterPassword(111));
        System.out.println(combinationLock.changePassword(111, 141));
    }
}
