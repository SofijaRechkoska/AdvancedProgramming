package Exercises.aud2;

import java.util.Objects;

class Date {
    private final static int[] DAYS_OF_MONTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private final static int[] DAYS_OF_MONTH_LEAP_YEAR = {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private final static int DAYS_IN_YEAR = 365;
    private final static int DAYS_IN_LEAP_YEAR = 366;
    private final static int START_YEAR = 1800;
    private int days;

    Date(int days) {
        this.days = days;
    }

    public boolean isLeapYear(int year) {
        if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        int d = days;
        int year = START_YEAR;
        int month = 1;
        while (true) {
            if (isLeapYear(year)) {
                if (d < DAYS_IN_LEAP_YEAR) {
                    break;
                } else {
                    year++;
                    d -= DAYS_IN_LEAP_YEAR;
                }
            } else {
                if (d < DAYS_IN_YEAR) {
                    break;
                } else {
                    year++;
                    d -= DAYS_IN_YEAR;
                }
            }
        }
        for (int i = 0; i < 12; i++) {
            if (isLeapYear(year)) {
                if (d >= DAYS_OF_MONTH_LEAP_YEAR[i]) {
                    month = i + 1;
                    d -= DAYS_OF_MONTH_LEAP_YEAR[i];
                } else {
                    break;
                }
            } else {
                if (d >= DAYS_OF_MONTH[i]) {
                    month = i + 1;
                    d -= DAYS_OF_MONTH[i];
                } else {
                    break;
                }
            }
        }
        return String.format("%d-%d-%d", d, month, year);
    }
    public int subtract(Date date){
        return this.days-date.days;
    }
    public Date increment(Date date){
        int totalDays=this.days+date.days;
        return new Date(totalDays);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Date date = (Date) o;
        return days == date.days;
    }
}
public class DateTest {
    public static void main(String[] args) {
        System.out.println(new Date(2951));
    }
}
