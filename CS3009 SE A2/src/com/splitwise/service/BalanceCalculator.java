package com.splitwise.service;

import com.splitwise.model.*;
import java.util.*;

public class BalanceCalculator {
    public static Map<User, Double> computeNetBalances(Group group) {
        Map<User, Double> netBalance = new HashMap<>();
        for (User u : group.getMembers()) netBalance.put(u, 0.0);

        for (Expense expense : group.getExpenses()) {
            User paidBy = expense.getPaidBy();
            double total = expense.getTotalAmount();
            netBalance.put(paidBy, netBalance.get(paidBy) + total);

            for (Split split : expense.getSplits()) {
                User owes = split.getUser();
                double amount = split.getAmount();
                netBalance.put(owes, netBalance.get(owes) - amount);
            }
        }
        return netBalance;
    }

    public static List<String> simplifyDebts(Map<User, Double> netBalance) {
        List<User> creditors = new ArrayList<>();
        List<User> debtors = new ArrayList<>();
        for (Map.Entry<User, Double> entry : netBalance.entrySet()) {
            double val = entry.getValue();
            if (Math.abs(val) < 0.01) continue;
            if (val > 0) creditors.add(entry.getKey());
            else debtors.add(entry.getKey());
        }

        List<String> transactions = new ArrayList<>();
        int i = 0, j = 0;
        Map<User, Double> balanceCopy = new HashMap<>(netBalance);
        while (i < creditors.size() && j < debtors.size()) {
            User creditor = creditors.get(i);
            User debtor = debtors.get(j);
            double credit = balanceCopy.get(creditor);
            double debt = -balanceCopy.get(debtor);
            double settle = Math.min(credit, debt);
            transactions.add(String.format("%s owes %s %.2f", debtor.getName(), creditor.getName(), settle));
            balanceCopy.put(creditor, credit - settle);
            balanceCopy.put(debtor, debt + settle);
            if (Math.abs(balanceCopy.get(creditor)) < 0.01) i++;
            if (Math.abs(balanceCopy.get(debtor)) < 0.01) j++;
        }
        return transactions;
    }
}