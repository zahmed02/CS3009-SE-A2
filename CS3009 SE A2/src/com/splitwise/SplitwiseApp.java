package com.splitwise;

import com.splitwise.model.*;
import com.splitwise.service.*;
import java.util.*;

public class SplitwiseApp {
    public static void main(String[] args) {
        User ali = new User("1", "Ali", "ali@mail.com");
        User sara = new User("2", "Sara", "sara@mail.com");
        User ahmed = new User("3", "Ahmed", "ahmed@mail.com");

        Group group = new Group("G1", "Trip", ali);
        group.addMember(sara);
        group.addMember(ahmed);

        // Expense 1: Ali pays 3000 dinner, split equally
        List<Split> splits1 = Arrays.asList(
            new Split(ali, 0),
            new Split(sara, 0),
            new Split(ahmed, 0)
        );
        Expense dinner = new Expense("E1", "Dinner", 3000, ali, ExpenseSplitType.EQUAL, splits1);
        group.addExpense(dinner);

        // Expense 2: Sara pays 1500 fuel, split equally
        List<Split> splits2 = Arrays.asList(
            new Split(ali, 0),
            new Split(sara, 0),
            new Split(ahmed, 0)
        );
        Expense fuel = new Expense("E2", "Fuel", 1500, sara, ExpenseSplitType.EQUAL, splits2);
        group.addExpense(fuel);

        Map<User, Double> net = BalanceCalculator.computeNetBalances(group);
        System.out.println("Net Balances:");
        for (Map.Entry<User, Double> e : net.entrySet()) {
            System.out.printf("%s: %.2f\n", e.getKey().getName(), e.getValue());
        }

        List<String> simplified = BalanceCalculator.simplifyDebts(new HashMap<>(net));
        System.out.println("\nSimplified transactions:");
        simplified.forEach(System.out::println);
    }
}