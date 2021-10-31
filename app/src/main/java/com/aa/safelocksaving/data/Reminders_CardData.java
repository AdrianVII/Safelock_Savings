package com.aa.safelocksaving.data;

public class Reminders_CardData {
    private String name;
    private double amount;
    private double minAmount;
    private double settlement;
    private String cutoffDate;
    private String deadline;
    private int importance;
    private int month;

    public Reminders_CardData() {  }

    public Reminders_CardData(String name, double amount, double minAmount, double settlement, String cutoffDate, String deadline, int importance, int month) {
        this.name = name;
        this.amount = amount;
        this.minAmount = minAmount;
        this.settlement = settlement;
        this.cutoffDate = cutoffDate;
        this.deadline = deadline;
        this.importance = importance;
        this.month = month;
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

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public double getSettlement() {
        return settlement;
    }

    public void setSettlement(double settlement) {
        this.settlement = settlement;
    }

    public String getCutoffDate() {
        return cutoffDate;
    }

    public void setCutoffDate(String cutoffDate) {
        this.cutoffDate = cutoffDate;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
