package com.aa.safelocksaving.data;

public class Reminders_ShopData {
    private String name;
    private double amount;
    private DateBasic cutoffDate;
    private DateBasic deadline;
    private String description;
    private int importance;
    private int month;

    public Reminders_ShopData(){ }

    public Reminders_ShopData(String name, double amount, DateBasic cutoffDate, DateBasic deadline, String description, int importance, int month) {
        this.name = name;
        this.amount = amount;
        this.cutoffDate = cutoffDate;
        this.deadline = deadline;
        this.description = description;
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

    public DateBasic getCutoffDate() {
        return cutoffDate;
    }

    public void setCutoffDate(DateBasic cutoffDate) {
        this.cutoffDate = cutoffDate;
    }

    public DateBasic getDeadline() {
        return deadline;
    }

    public void setDeadline(DateBasic deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
