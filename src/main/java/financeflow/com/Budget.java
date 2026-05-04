package financeflow.com;

import java.io.Serializable;

public class Budget implements Serializable {
    private Category category;
    private double monthlyLimit;

    public Budget(Category category, double monthlyLimit) {
        setCategory(category);
        setMonthlyLimit(monthlyLimit);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null.");
        }
        this.category = category;
    }

    public double getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(double monthlyLimit) {
        if (monthlyLimit < 0) {
            throw new IllegalArgumentException("Budget limit cannot be negative.");
        }
        this.monthlyLimit = monthlyLimit;
    }

    @Override
    public String toString() {
        return category.getName() + ": " + monthlyLimit;
    }
}