package financeflow.com;

import java.time.LocalDate;
import java.time.YearMonth;

public class BackendSmokeTest {
    public static void main(String[] args) {
        FinanceManager manager = new FinanceManager();

        manager.addCategory("Food");
        manager.addCategory("Salary");

        Category food = manager.findCategoryByName("Food");
        Category salary = manager.findCategoryByName("Salary");

        manager.addTransaction(new IncomeTransaction(2500, LocalDate.now(), salary, "Part-time job"));
        manager.addTransaction(new ExpenseTransaction(40, LocalDate.now(), food, "Lunch"));

        manager.setBudget("Food", 300);

        MonthlyReport report = manager.generateMonthlyReport(YearMonth.now());

        System.out.println("Income: " + report.getTotalIncome());
        System.out.println("Expenses: " + report.getTotalExpenses());
        System.out.println("Net: " + report.getNetBalance());
        System.out.println("Food budget left: " + manager.getRemainingBudget("Food", YearMonth.now()));
    }
}