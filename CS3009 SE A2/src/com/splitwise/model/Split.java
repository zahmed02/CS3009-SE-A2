package com.splitwise.model;
// Split.java
public class Split {
    private User user;
    private double amount;
    private double percentage; // for PERCENT split

    public Split(User user, double amount) {
        this.user = user;
        this.amount = amount;
    }

    public Split(User user, double amount, double percentage) {
        this.user = user;
        this.amount = amount;
        this.percentage = percentage;
    }

    public User getUser() { return user; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public double getPercentage() { return percentage; }
}