package financeflow.com;

import java.time.LocalDate;

public class ExpenseTransaction extends Transaction {
    public ExpenseTransaction(double amount, LocalDate date, Category category, String note) {
        super(amount, date, category, note);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.EXPENSE;
    }
}