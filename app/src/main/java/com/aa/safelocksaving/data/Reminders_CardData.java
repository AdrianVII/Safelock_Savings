package com.aa.safelocksaving.data;

public class Reminders_CardData {
    private String name;
    private double amount;
    private double minAmount;
    private double settlement;
    private int importance;
    private int month;
    private DateBasic cutoffDate;
    private DateBasic deadline;
    private long ID;
    private double progressAmount;
    private int progressMonth;
    private double accumulatedAmount;

    public Reminders_CardData() {  }

    public Reminders_CardData(long ID, String name, double amount, double minAmount, double settlement, DateBasic cutoffDate, DateBasic deadline, int importance, int month, double progressAmount, int progressMonth, double accumulatedAmount) {
        this.ID = ID;
        this.name = name;
        this.amount = amount;
        this.minAmount = minAmount;
        this.settlement = settlement;
        this.cutoffDate = cutoffDate;
        this.deadline = deadline;
        this.importance = importance;
        this.month = month;
        this.progressAmount = progressAmount;
        this.progressMonth = progressMonth;
        this.accumulatedAmount = accumulatedAmount;
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

    public DateBasic getCutoffDate() { return cutoffDate; }

    public void setCutoffDate(DateBasic cutoffDate) {
        this.cutoffDate = cutoffDate;
    }

    public DateBasic getDeadline() {
        return deadline;
    }

    public void setDeadline(DateBasic deadline) {
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

    public double getProgressAmount() { return progressAmount; }

    public void setProgressAmount(double progressAmount) { this.progressAmount = progressAmount; }

    public int getProgressMonth() { return progressMonth; }

    public void setProgressMonth(int progressMonth) { this.progressMonth = progressMonth; }

    public double getAccumulatedAmount() { return accumulatedAmount; }

    public void setAccumulatedAmount(double accumulatedAmount) { this.accumulatedAmount = accumulatedAmount; }
}
