package com.splitwise.service;

import com.splitwise.model.*;
import java.util.*;

public class SettlementEngine {
    private Map<String, Double> pendingPayments;

    public SettlementEngine() {
        pendingPayments = new HashMap<>();
    }

    public void recordPayment(User from, User to, double amount) {
        String key = from.getUserId() + "_" + to.getUserId();
        pendingPayments.put(key, pendingPayments.getOrDefault(key, 0.0) + amount);
    }

    public double getAmountOwed(User from, User to) {
        String key = from.getUserId() + "_" + to.getUserId();
        return pendingPayments.getOrDefault(key, 0.0);
    }

    public Map<User, Double> applySettlements(Map<User, Double> netBalance, Group group) {
        Map<User, Double> updated = new HashMap<>(netBalance);
        for (Map.Entry<String, Double> entry : pendingPayments.entrySet()) {
            String[] ids = entry.getKey().split("_");
            User from = findUserById(group, ids[0]);
            User to = findUserById(group, ids[1]);
            double amount = entry.getValue();
            if (from != null && to != null) {
                updated.put(from, updated.get(from) + amount);
                updated.put(to, updated.get(to) - amount);
            }
        }
        return updated;
    }

    private User findUserById(Group group, String id) {
        return group.getMembers().stream()
                .filter(u -> u.getUserId().equals(id))
                .findFirst()
                .orElse(null);
    }
}