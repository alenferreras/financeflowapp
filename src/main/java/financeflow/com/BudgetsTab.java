package financeflow.com;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BudgetsTab {
    private TableView<Budget> table;
    private ObservableList<Budget> data;

    private final FinanceManager manager;
    private final DataStore store;
    private final YearMonth month;

    public BudgetsTab(FinanceManager financeManager, DataStore dataStore){
        manager = financeManager;
        store = dataStore;
        month = YearMonth.from(LocalDate.now());
    }

    public Tab createTab(){
        table = new TableView<>();
        data = FXCollections.observableArrayList(manager.getBudgets());
        table.setItems(data);
        
        // Table columns
        TableColumn<Budget, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategory().getName()));

        TableColumn<Budget, String> limitCol = new TableColumn<>("Monthly Limit");
        limitCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getMonthlyLimit())));
        limitCol.setPrefWidth(200);

        TableColumn<Budget, String> remainingCol = new TableColumn<>("Budget Remaining");
        remainingCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(manager.getRemainingBudget(c.getValue().getCategory().getName(), month))));
        remainingCol.setPrefWidth(200);

        table.getColumns().addAll(categoryCol, limitCol, remainingCol);

        // Buttons
        Button addBtn = new Button("Add Budget");
        Button modifyBtn = new Button("Modify Budget");

        addBtn.setOnAction(e -> addBudget());
        modifyBtn.setOnAction(e -> modifyBudget());

        HBox buttons = new HBox(10, addBtn, modifyBtn);


        VBox root = new VBox(10, table, buttons);
        root.setPadding(new Insets(15));

        Tab tab = new Tab("Budgets");
        tab.setContent(root);
        tab.setClosable(false);
        return tab;
    }

    private void addBudget() {
        try {
            List<Category> categoryList = manager.getCategories();
            ChoiceDialog<Category> categoryDialog = new ChoiceDialog<>(categoryList.get(0), categoryList);
            categoryDialog.setHeaderText("Select Category");
            Category category = categoryDialog.showAndWait().orElse(null);

            TextInputDialog amountDialog = new TextInputDialog();
            amountDialog.setHeaderText("Set Budget");
            double amount = Double.parseDouble(amountDialog.showAndWait().orElse("0"));

            manager.setBudget(category.getName(), amount);
            data.setAll(manager.getBudgets());
            saveData();

        } catch (Exception e) {
            showError("Invalid input.");
        }
    }

    private void modifyBudget() {
        try {
            int index = table.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                TextInputDialog amountDialog = new TextInputDialog();
                amountDialog.setHeaderText("Set Budget");
                double amount = Double.parseDouble(amountDialog.showAndWait().orElse("0"));

                manager.setBudget(data.get(index).getCategory().getName(), amount);
                data.setAll(manager.getBudgets());
                saveData();
            }
        } catch (Exception e) {
            showError("Invalid input.");
        }
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
