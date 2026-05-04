package financeflow.com;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvExporter {
    public void exportTransactions(FinanceManager manager, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Type,Amount,Date,Category,Note");
            writer.newLine();

            for (Transaction transaction : manager.getTransactions()) {
                writer.write(transaction.getType() + ","
                        + transaction.getAmount() + ","
                        + transaction.getDate() + ","
                        + transaction.getCategory().getName() + ","
                        + transaction.getNote().replace(",", " "));
                writer.newLine();
            }
        }
    }
}