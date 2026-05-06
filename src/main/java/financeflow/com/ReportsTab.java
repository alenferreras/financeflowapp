package financeflow.com;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ReportsTab {
    private TableView<Transaction> transactionTable;
    private ObservableList<Transaction> transactionsData; 
    TableView<Map.Entry<String, Double>> categoryTable;
    private ObservableList<Map.Entry<String, Double>> categoryData;

    private final FinanceManager manager;
    private final DataStore store;
    private final YearMonth month;
    private MonthlyReport report;

    private Label incomeLabel;
    private Label expenseLabel;
    private Label balanceLabel;
    private Label categoryBreakdownLabel;

    public ReportsTab(FinanceManager financeManager, DataStore dataStore){
        manager = financeManager;
        store = dataStore;
        month = YearMonth.from(LocalDate.now());
        Map<String, Double> map = new HashMap<>();
        report = new MonthlyReport(0, 0, map);
    }

    public Tab createTab(){
        //Transaction Table
        transactionTable = new TableView<>();
        transactionsData = FXCollections.observableArrayList(manager.getTransactions());
        transactionTable.setItems(transactionsData);

        // Table columns
        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType().toString()));

        TableColumn<Transaction, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getAmount())));

        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDate().toString()));

        TableColumn<Transaction, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategory().getName()));

        TableColumn<Transaction, String> noteCol = new TableColumn<>("Note");
        noteCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNote()));
        noteCol.setPrefWidth(200);

        transactionTable.getColumns().addAll(typeCol, amountCol, dateCol, categoryCol, noteCol);

        //Category Breakdown Table
        categoryTable = new TableView<>();
        categoryData = FXCollections.observableArrayList(report.getCategoryBreakdown().entrySet());
        categoryTable.setItems(categoryData);

        //Table Columns
        TableColumn<Map.Entry<String, Double>, String> catColumn = new TableColumn<>("Category");
        catColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getKey()));
        
        TableColumn<Map.Entry<String, Double>, String> budgetColumn = new TableColumn<>("Remaining Budget");
        budgetColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getValue())));
        budgetColumn.setPrefWidth(138);

        categoryTable.getColumns().addAll(catColumn, budgetColumn);

        // Buttons
        Button btn = new Button("Generate Monthly Report");
        btn.setPrefWidth(400);

        btn.setOnAction(e -> generateReport());

        // Summary labels
        incomeLabel = new Label();
        expenseLabel = new Label();
        balanceLabel = new Label();
        categoryBreakdownLabel = new Label();
        categoryBreakdownLabel.setText("Category Breakdown");

        VBox summary = new VBox(5, btn, incomeLabel, expenseLabel, balanceLabel, categoryBreakdownLabel, categoryTable);

        updateSummary();

        HBox root = new HBox(10, summary, transactionTable);
        root.setPadding(new Insets(15));

        Tab tab = new Tab("Reports");
        tab.setContent(root);
        tab.setClosable(false);
        return tab;
    }

    private void generateReport(){
        try {
            ChoiceDialog<String> typeDialog = new ChoiceDialog<>("JANUARY", "JANUARY", "FEBURARY",
                    "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER");
            typeDialog.setHeaderText("Select Month");
            String type = typeDialog.showAndWait().orElse(null);
            if (type == null) return;

            report = manager.generateMonthlyReport(YearMonth.from(Month.valueOf(type)));

            saveData();
            updateData();
            updateSummary();

        } catch (Exception e) {
            showError("Invalid Input.");
        }
    }

    private void updateData() {
        transactionsData.setAll(manager.getTransactions());
        categoryData.setAll(report.getCategoryBreakdown().entrySet());
    }

    private void updateSummary() {
        double income = report.getTotalIncome();

        double expenses = report.getTotalExpenses();

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

}
