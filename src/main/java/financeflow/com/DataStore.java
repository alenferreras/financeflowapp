package financeflow.com;

import java.io.*;

public class DataStore {
    public void save(FinanceManager manager, String filePath) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(manager.getCategories());
            out.writeObject(manager.getTransactions());
            out.writeObject(manager.getBudgets());
        }
    }

    @SuppressWarnings("unchecked")
    public void load(FinanceManager manager, String filePath) throws IOException, ClassNotFoundException {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            manager.getCategories().clear();
            manager.getTransactions().clear();
            manager.getBudgets().clear();

            manager.getCategories().addAll((java.util.List<Category>) in.readObject());
            manager.getTransactions().addAll((java.util.List<Transaction>) in.readObject());
            manager.getBudgets().addAll((java.util.List<Budget>) in.readObject());
        }
    }
}