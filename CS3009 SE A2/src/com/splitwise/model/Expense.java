package com.splitwise.model;
import java.util.*;

public class Expense {
    private String expenseId;
    private String description;
    private double totalAmount;
    private User paidBy;
    private ExpenseSplitType splitType;
    private List<Split> splits;
    private Date date;

    public Expense(String expenseId, String description, double totalAmount,
                  User paidBy, ExpenseSplitType splitType, List<Split> splits) {
        if (totalAmount < 0) throw new IllegalArgumentException("Total amount cannot be negative");
        this.expenseId = expenseId;
        this.description = description;
        this.totalAmount = totalAmount;
        this.paidBy = paidBy;
        this.splitType = splitType;
        this.splits = splits;
        this.date = new Date();
        validateSplits();
    }

    private void validateSplits() {
        if (splitType == ExpenseSplitType.EQUAL) {
            double each = totalAmount / splits.size();
            for (Split s : splits) s.setAmount(each);
        } else if (splitType == ExpenseSplitType.EXACT) {
            double sum = splits.stream().mapToDouble(Split::getAmount).sum();
            if (Math.abs(sum - totalAmount) > 0.01)
                throw new IllegalArgumentException("Exact split amounts do not sum to total");
        } else if (splitType == ExpenseSplitType.PERCENT) {
            double sumPercent = splits.stream().mapToDouble(Split::getPercentage).sum();
            if (Math.abs(sumPercent - 100) > 0.01)
                throw new IllegalArgumentException("Percentages must sum to 100");
            for (Split s : splits) {
                s.setAmount(totalAmount * s.getPercentage() / 100);
            }
        }
    }

    // Getters
    public String getExpenseId() { return expenseId; }
    public double getTotalAmount() { return totalAmount; }
    public User getPaidBy() { return paidBy; }
    public List<Split> getSplits() { return splits; }
}