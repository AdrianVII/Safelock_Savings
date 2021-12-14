package com.aa.safelocksaving.data;

import java.text.DateFormat;
import java.util.Calendar;

public class DateBasic {
    private int Day;
    private int Month;
    private int Year;

    public DateBasic() {  }

    public DateBasic(int day, int month, int year) {
        Day = day;
        Month = month;
        Year = year;
    }

    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        Day = day;
    }

    public int getMonth() {
        return Month;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public String toString() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Year, Month, Day);
        return DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
    }

    public String fullDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Year, Month, Day);
        return DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
    }
}
