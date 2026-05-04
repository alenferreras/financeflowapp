package financeflow.com;

import java.time.LocalDate;

public class IncomeTransaction extends Transaction {
    public IncomeTransaction(double amount, LocalDate date, Category category, String note) {
        super(amount, date, category, note);
    }

    @Override
    public TransactionType getType() {
        return TransactionType.INCOME;
    }
}