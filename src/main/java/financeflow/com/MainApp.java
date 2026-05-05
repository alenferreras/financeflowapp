package financeflow.com;

import java.time.LocalDate;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final FinanceManager manager = new FinanceManager();
    private final DataStore store = new DataStore();

    private TableView<Transaction> table;
    private ObservableList<Transaction> data;

    private Label incomeLabel;
    private Label expenseLabel;
    private Label balanceLabel;

    @Override
    public void start(Stage stage) {

        // Load data
        try {
            store.load(manager, "data/finance_data.txt");
        } catch (Exception e) {
            System.out.println("No previous data found.");
        }

        table = new TableView<>();
        data = FXCollections.observableArrayList(manager.getTransactions());
        table.setItems(data);

        // Table columns
        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType().toString()));

        TableColumn<Transaction, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getAmount())));

        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDate().toString()));

        TableColumn<Transaction, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategory().getName()));

        table.getColumns().addAll(typeCol, amountCol, dateCol, categoryCol);

        // Buttons
        Button addBtn = new Button("Add Transaction");
        Button deleteBtn = new Button("Delete Transaction");
        Button addCategoryBtn = new Button("Add Category");
        Button deleteCategoryBtn = new Button("Delete Category");

        addBtn.setOnAction(e -> addTransaction());
        deleteBtn.setOnAction(e -> deleteTransaction());
        addCategoryBtn.setOnAction(e -> addCategory());
        deleteCategoryBtn.setOnAction(e -> deleteCategory());

        HBox buttons = new HBox(10, addBtn, deleteBtn, addCategoryBtn, deleteCategoryBtn);

        // Summary labels
        incomeLabel = new Label();
        expenseLabel = new Label();
        balanceLabel = new Label();

        VBox summary = new VBox(5, incomeLabel, expenseLabel, balanceLabel);

        updateSummary();

        VBox root = new VBox(10, table, buttons, summary);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("FinanceFlow");
        stage.show();
    }

    private void addTransaction() {
        try {
            ChoiceDialog<String> typeDialog = new ChoiceDialog<>("Expense", "Income", "Expense");
            typeDialog.setHeaderText("Select Transaction Type");
            String type = typeDialog.showAndWait().orElse(null);
            if (type == null) return;

            TextInputDialog amountDialog = new TextInputDialog();
            amountDialog.setHeaderText("Enter Amount");
            double amount = Double.parseDouble(amountDialog.showAndWait().orElse("0"));

            List<Category> categoryList = manager.getCategories();
            ChoiceDialog<Category> categoryDialog = new ChoiceDialog<>(categoryList.get(0), categoryList);
            categoryDialog.setHeaderText("Select Category Type");
            Category category = categoryDialog.showAndWait().orElse(null);

            Transaction transaction;
            if (type.equals("Income")) {
                transaction = new IncomeTransaction(amount, LocalDate.now(), category, "");
            } else {
                transaction = new ExpenseTransaction(amount, LocalDate.now(), category, "");
            }

            manager.addTransaction(transaction);
            data.setAll(manager.getTransactions());

            saveData();
            updateSummary();

        } catch (Exception e) {
            showError("Invalid input.");
        }
    }

    private void addCategory() {
        try {
            TextInputDialog categoryDialog = new TextInputDialog();
            categoryDialog.setHeaderText("Enter Category");
            String categoryName = categoryDialog.showAndWait().orElse(null);

            Category category = manager.findCategoryByName(categoryName);
            if (category == null) {
                manager.addCategory(categoryName);
            }

            showSuccess("Category Saved");

        } catch (Exception e) {
            showError("Invalid input.");
        }
    }

    private void deleteTransaction() {
        int index = table.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            manager.deleteTransaction(index);
            data.setAll(manager.getTransactions());

            saveData();
            updateSummary();
        }
    }

    private void deleteCategory() {
        manager.deleteCategory(selectCategory("Delete Category").getName());
        showSuccess("Category Deleted");
    }

    private Category selectCategory(String msg) {
        try {
            List<Category> categoryList = manager.getCategories();
            ChoiceDialog<Category> categoryDialog = new ChoiceDialog<>(categoryList.get(0), categoryList);
            categoryDialog.setHeaderText(msg);
            Category category = categoryDialog.showAndWait().orElse(null);
            return category;
        } catch (Exception e) {
            showError("Category list is empty.");
            return null;
        }
        
    }

    private void updateSummary() {
        double income = manager.getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expenses = manager.getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

        incomeLabel.setText("Total Income: " + income);
        expenseLabel.setText("Total Expenses: " + expenses);
        balanceLabel.setText("Balance: " + (income - expenses));
    }

    private void saveData() {
        try {
            store.save(manager, "data/finance_data.txt");
        } catch (Exception e) {
            showError("Failed to save data.");
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }

    private void showSuccess(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}