package financeflow.com;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FinanceManager {
    private final List<Transaction> transactions;
    private final List<Category> categories;
    private final List<Budget> budgets;

    public FinanceManager() {
        this.transactions = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.budgets = new ArrayList<>();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Budget> getBudgets() {
        return budgets;
    }

    public void addCategory(String name) {
        if (findCategoryByName(name) != null) {
            throw new IllegalArgumentException("Category already exists.");
        }
        categories.add(new Category(name));
    }

    public void renameCategory(String oldName, String newName) {
        Category category = findCategoryByName(oldName);
        if (category == null) {
            throw new IllegalArgumentException("Category not found.");
        }
        if (findCategoryByName(newName) != null) {
            throw new IllegalArgumentException("New category name already exists.");
        }
        category.setName(newName);
    }

    public void deleteCategory(String name) {
        Category category = findCategoryByName(name);
        if (category == null) {
            throw new IllegalArgumentException("Category not found.");
        }

        boolean used = transactions.stream().anyMatch(t -> t.getCategory().getName().equalsIgnoreCase(name));
        if (used) {
            throw new IllegalArgumentException("Cannot delete category that is used by transactions.");
        }

        budgets.removeIf(b -> b.getCategory().getName().equalsIgnoreCase(name));
        categories.remove(category);
    }

    public Category findCategoryByName(String name) {
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void updateTransaction(int index, Transaction updatedTransaction) {
        if (index < 0 || index >= transactions.size()) {
            throw new IllegalArgumentException("Invalid transaction index.");
        }
        transactions.set(index, updatedTransaction);
    }

    public void deleteTransaction(int index) {
        if (index < 0 || index >= transactions.size()) {
            throw new IllegalArgumentException("Invalid transaction index.");
        }
        transactions.remove(index);
    }

    public void setBudget(String categoryName, double limit) {
        Category category = findCategoryByName(categoryName);
        if (category == null) {
            throw new IllegalArgumentException("Category not found.");
        }

        for (Budget budget : budgets) {
            if (budget.getCategory().getName().equalsIgnoreCase(categoryName)) {
                budget.setMonthlyLimit(limit);
                return;
            }
        }

        budgets.add(new Budget(category, limit));
    }

    public double getRemainingBudget(String categoryName, YearMonth month) {
        Budget budget = budgets.stream()
                .filter(b -> b.getCategory().getName().equalsIgnoreCase(categoryName))
                .findFirst()
                .orElse(null);

        if (budget == null) {
            return 0;
        }

        double spent = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .filter(t -> t.getCategory().getName().equalsIgnoreCase(categoryName))
                .filter(t -> YearMonth.from(t.getDate()).equals(month))
                .mapToDouble(Transaction::getAmount)
                .sum();

        return budget.getMonthlyLimit() - spent;
    }

    public MonthlyReport generateMonthlyReport(YearMonth month) {
        double income = 0;
        double expenses = 0;
        Map<String, Double> breakdown = new LinkedHashMap<>();

        for (Transaction transaction : transactions) {
            if (!YearMonth.from(transaction.getDate()).equals(month)) {
                continue;
            }

            if (transaction.getType() == TransactionType.INCOME) {
                income += transaction.getAmount();
            } else {
                expenses += transaction.getAmount();
                String categoryName = transaction.getCategory().getName();
                breakdown.put(categoryName,
                        breakdown.getOrDefault(categoryName, 0.0) + transaction.getAmount());
            }
        }

        return new MonthlyReport(income, expenses, breakdown);
    }
}