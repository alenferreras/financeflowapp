package financeflow.com;

import java.util.LinkedHashMap;
import java.util.Map;

public class MonthlyReport {
    private final double totalIncome;
    private final double totalExpenses;
    private final double netBalance;
    private final Map<String, Double> categoryBreakdown;

    public MonthlyReport(double totalIncome, double totalExpenses, Map<String, Double> categoryBreakdown) {
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netBalance = totalIncome - totalExpenses;
        this.categoryBreakdown = new LinkedHashMap<>(categoryBreakdown);
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public double getNetBalance() {
        return netBalance;
    }

    public Map<String, Double> getCategoryBreakdown() {
        return categoryBreakdown;
    }
}