package financeflow.com;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Transaction implements Serializable {
    private double amount;
    private LocalDate date;
    private Category category;
    private String note;

    public Transaction(double amount, LocalDate date, Category category, String note) {
        setAmount(amount);
        setDate(date);
        setCategory(category);
        setNote(note);
    }

    public abstract TransactionType getType();

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        this.date = date;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = (note == null) ? "" : note.trim();
    }

    @Override
    public String toString() {
        return getType() + " | " + amount + " | " + date + " | " + category + " | " + note;
    }
}