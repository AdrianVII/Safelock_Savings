package com.aa.safelocksaving.data;

public class Reminders_SubscriptionData {
    private String name;
    private double amount;
    private String date;
    private int importance;
    private int period;

    public Reminders_SubscriptionData() {  }

    public Reminders_SubscriptionData(String name, double amount, String date, int importance, int period) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
