package com.splitwise.model;
import java.util.*;

public class Group {
    private String groupId;
    private String groupName;
    private Set<User> members;
    private User admin;
    private List<Expense> expenses;

    public Group(String groupId, String groupName, User admin) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.admin = admin;
        this.members = new HashSet<>();
        this.expenses = new ArrayList<>();
        members.add(admin);
    }

    public void addMember(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        members.add(user);
    }

    public void removeMember(User user) {
        if (user.equals(admin)) throw new IllegalArgumentException("Cannot remove admin");
        members.remove(user);
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public Set<User> getMembers() { return members; }
    public User getAdmin() { return admin; }
    public List<Expense> getExpenses() { return expenses; }
    public String getGroupId() { return groupId; }
}