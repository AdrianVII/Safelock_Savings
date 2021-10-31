package com.aa.safelocksaving.data;

public class Reminders_SubscriptionData {
    private String name;
    private double amount;
    private DateBasic date;
    private int importance;
    private int period;

    public Reminders_SubscriptionData() {  }

    public Reminders_SubscriptionData(String name, double amount, DateBasic date, int importance, int period) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.importance = importance;
        this.period = period;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public DateBasic getDate() {
        return date;
    }

    public void setDate(DateBasic date) {
        this.date = date;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
