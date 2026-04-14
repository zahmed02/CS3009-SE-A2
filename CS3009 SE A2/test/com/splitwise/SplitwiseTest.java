package com.splitwise;

import com.splitwise.model.*;
import com.splitwise.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class SplitwiseTest {
    private User ali, sara, ahmed;
    private Group group;

    @BeforeEach
    void setUp() {
        ali = new User("1", "Ali", "ali@test.com");
        sara = new User("2", "Sara", "sara@test.com");
        ahmed = new User("3", "Ahmed", "ahmed@test.com");
        group = new Group("G1", "TestGroup", ali);
        group.addMember(sara);
        group.addMember(ahmed);
    }

    @Test
    void testUserCreationValidEmail() {
        User u = new User("4", "John", "john@doe.com");
        assertEquals("john@doe.com", u.getEmail());
    }

    @Test
    void testUserCreationInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> new User("5", "Bad", "bademail"));
    }

    @Test
    void testAddContactNumber() {
        ali.addContactNumber("123456");
        assertTrue(ali.getContactNumbers().contains("123456"));
    }

    @Test
    void testRemoveAdminThrows() {
        assertThrows(IllegalArgumentException.class, () -> group.removeMember(ali));
    }

    @Test
    void testEqualSplitExpense() {
        List<Split> splits = Arrays.asList(new Split(ali,0), new Split(sara,0), new Split(ahmed,0));
        Expense e = new Expense("E1", "Dinner", 3000, ali, ExpenseSplitType.EQUAL, splits);
        assertEquals(1000.0, e.getSplits().get(0).getAmount());
        assertEquals(1000.0, e.getSplits().get(1).getAmount());
        assertEquals(1000.0, e.getSplits().get(2).getAmount());
    }

    @Test
    void testExactSplitMismatchThrows() {
        List<Split> splits = Arrays.asList(new Split(ali,1500), new Split(sara,600));
        assertThrows(IllegalArgumentException.class,
            () -> new Expense("E2", "Test", 2000, ali, ExpenseSplitType.EXACT, splits));
    }

    @Test
    void testPercentageSplit() {
        List<Split> splits = Arrays.asList(new Split(ali,0,50), new Split(sara,0,30), new Split(ahmed,0,20));
        Expense e = new Expense("E3", "Groceries", 1000, ali, ExpenseSplitType.PERCENT, splits);
        assertEquals(500.0, e.getSplits().get(0).getAmount());
        assertEquals(300.0, e.getSplits().get(1).getAmount());
        assertEquals(200.0, e.getSplits().get(2).getAmount());
    }

    @Test
    void testNetBalanceComputation() {
        List<Split> splits1 = Arrays.asList(new Split(ali,0), new Split(sara,0), new Split(ahmed,0));
        Expense dinner = new Expense("E1", "Dinner", 3000, ali, ExpenseSplitType.EQUAL, splits1);
        List<Split> splits2 = Arrays.asList(new Split(ali,0), new Split(sara,0), new Split(ahmed,0));
        Expense fuel = new Expense("E2", "Fuel", 1500, sara, ExpenseSplitType.EQUAL, splits2);
        group.addExpense(dinner);
        group.addExpense(fuel);
        Map<User, Double> net = BalanceCalculator.computeNetBalances(group);
        assertEquals(1500.0, net.get(ali), 0.01);
        assertEquals(0.0, net.get(sara), 0.01);
        assertEquals(-1500.0, net.get(ahmed), 0.01);
    }

    @Test
    void testSimplifyDebts() {
        Map<User, Double> net = new HashMap<>();
        net.put(ali, 1500.0);
        net.put(sara, 0.0);
        net.put(ahmed, -1500.0);
        List<String> transactions = BalanceCalculator.simplifyDebts(new HashMap<>(net));
        assertTrue(transactions.stream().anyMatch(t -> t.contains("Ahmed owes Ali")));
    }

    @Test
    void testZeroTotalExpense() {
        List<Split> splits = Arrays.asList(new Split(ali,0), new Split(sara,0));
        Expense e = new Expense("E4", "Zero", 0, ali, ExpenseSplitType.EQUAL, splits);
        assertEquals(0.0, e.getSplits().get(0).getAmount());
        assertEquals(0.0, e.getSplits().get(1).getAmount());
    }

    @Test
    void testNegativeTotalThrows() {
        List<Split> splits = Arrays.asList(new Split(ali,0), new Split(sara,0));
        assertThrows(IllegalArgumentException.class,
            () -> new Expense("E5", "Negative", -100, ali, ExpenseSplitType.EQUAL, splits));
    }
}