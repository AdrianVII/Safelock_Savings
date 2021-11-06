package com.aa.safelocksaving.data;

public class Budgets_Data {
    private long ID;
    private String name;
    private double amount;
    private String type;

    public Budgets_Data() {}

    public Budgets_Data(long ID, String name, double amount, String type) {
        this.ID = ID;
        this.name = name;
        this.amount = amount;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}