package com.aa.safelocksaving.data;

public class Reminders_SubscriptionData {
    private String name;
    private double amount;
    private DateBasic date;
    private int importance;
    private int repeat;
    private long ID;
    private int progressRepeat;
    private double progressAmount;

    public Reminders_SubscriptionData() {  }

    public Reminders_SubscriptionData(long ID, String name, double amount, DateBasic date, int importance, int repeat, int progressRepeat, double progressAmount) {
        this.ID = ID;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.importance = importance;
        this.repeat = repeat;
        this.progressRepeat = progressRepeat;
        this.progressAmount = progressAmount;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
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

    public DateBasic getDate() { return date; }

    public void setDate(DateBasic date) {
        this.date = date;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getProgressRepeat() { return progressRepeat; }

    public void setProgressRepeat(int progressRepeat) { this.progressRepeat = progressRepeat; }

    public double getProgressAmount() { return progressAmount; }

    public void setProgressAmount(double progressAmount) { this.progressAmount = progressAmount; }
}

